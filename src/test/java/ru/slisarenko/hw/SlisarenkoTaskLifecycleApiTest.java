package ru.slisarenko.hw;

import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.specification.RequestSpecification;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.not;


@Epic("Task Manager")
@Feature("Жизненный цикл задачи")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SlisarenkoTaskLifecycleApiTest {
    private static Map<String, String> credentials;
    private static String uuidRegex = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";

    public static RequestSpecification commonRequestSpec() {
        return given()
                .baseUri("http://127.0.0.1:8080")
                .contentType("application/json")
                .basePath("api/v1");
    }

    @BeforeAll
    public static void initData() {
        credentials = new HashMap<>();
        credentials.put("email", "qa@demo.com");
        credentials.put("password", "Demo123!");

        String token = given()
                .spec(commonRequestSpec())
                .body(credentials)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .log().ifValidationFails()
                .extract()
                .path("accessToken");

        credentials.put("accessToken", token);
    }

    @Test
    @Order(1)
    @DisplayName("Получить список проектов и выбрать проект с ключем DEMO")
    public void givenAccessToken_whenProjects_thenListProjects() {
        List<ProjectResponse> projects = given()
                .spec(commonRequestSpec())
                .auth().oauth2(credentials.get("accessToken"))
                .param("size", 20)
                .when()
                .get("/projects")
                .then()
                .statusCode(200)
                .body("content", not(empty()))
                .body("content.key", hasItem("DEMO"))
                .log().ifValidationFails()
                .extract()
                .jsonPath()
                .getList("content", ProjectResponse.class);


        UUID id = projects.stream()
                .filter(p -> p.getKey().equals("DEMO"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not found project with key DEMO"))
                .getId();

        credentials.put("projectId", id.toString());
        Allure.step("Получили проект", () -> {
            Allure.attachment("projectId", id.toString());
        });
    }

    @Test
    @Order(2)
    @DisplayName("Создать задачу в проекте DEMO")
    public void givenTask_whenTasks_thenCreateTask() {
            String id = given()
                    .spec(commonRequestSpec())
                    .auth().oauth2(credentials.get("accessToken"))
                    .when()
                    .get("/auth/me")
                    .then()
                    .statusCode(200)
                    .body("id", not(empty()))
                    .body("id", matchesPattern(uuidRegex))
                    .log().ifValidationFails()
                    .extract()
                    .path("id");
            credentials.put("userId", id);

        Allure.step("Получили id пользователя", () -> {
            Allure.attachment("assigneeId", id);
        });

            TaskCreateRequest taskCreateRequest = TaskCreateRequest.builder()
                    .projectId(UUID.fromString(credentials.get("projectId")))
                    .title("HW-CAA-" + System.currentTimeMillis())
                    .assigneeId(UUID.fromString(credentials.get("userId")))
                    .description("Проверка создания задания в Rest Assure")
                    .dueDate(Instant.now().plusSeconds(300))
                    .priority(TaskPriority.MEDIUM)
                    .build();

        Allure.step("Приготовили новую задачу", () -> {
            Allure.attachment("task", taskCreateRequest.toString());
        });

            TaskResponse task = given()
                    .spec(commonRequestSpec())
                    .auth().oauth2(credentials.get("accessToken"))
                    .body(taskCreateRequest)
                    .when()
                    .post("/tasks")
                    .then()
                    .statusCode(201)
                    .body("assigneeId", equalTo(taskCreateRequest.getAssigneeId().toString()))
                    .body("title", equalTo(taskCreateRequest.getTitle()))
                    .body("version", equalTo(0))
                    .log().ifValidationFails()
                    .extract()
                    .as(TaskResponse.class);
            credentials.put("taskId", task.id().toString());
            credentials.put("versionTask", task.version().toString());
            credentials.put("titleTask", task.title());

            Allure.step("Загрузили новую задачу", () -> {
                Allure.attachment("task", task.toString());
            });
    }

    @Test
    @Order(3)
    @DisplayName("Получить созданную задачу по ID")
    public void givenTaskId_whenTasks_thenTask() {

        TaskResponse task = given()
                .spec(commonRequestSpec())
                .auth().oauth2(credentials.get("accessToken"))
                .pathParam("taskId", credentials.get("taskId"))
                .when()
                .get("/tasks/{taskId}")
                .then()
                .statusCode(200)
                .body("task.id", not(empty()))
                .body("task.id", matchesPattern(uuidRegex))
                .body("task.title", equalTo(credentials.get("titleTask")))
                .log().ifValidationFails()
                .extract()
                .jsonPath()
                .getObject("task", TaskResponse.class);

        credentials.put("versionTask", task.version().toString());
        Allure.step("Созданная задача", () -> {
            Allure.attachment("task", task.toString());
        });
    }

    @Test
    @Order(4)
    @DisplayName("Обновить статус задачи (TODO → IN_PROGRESS)")
    public void givenTaskIdAndStatus_whenTasks_thenUpdateStatusTask() {
        StatusTaskUpdateRequest request = StatusTaskUpdateRequest.builder()
                .status(TaskStatus.IN_PROGRESS)
                .version(Integer.parseInt(credentials.get("versionTask")))
                .build();
        Allure.step("Запрос на обновление статуса", () -> {
            Allure.attachment("task", request.toString());
        });
        TaskResponse task = given()
                .spec(commonRequestSpec())
                .auth().oauth2(credentials.get("accessToken"))
                .pathParam("taskId", credentials.get("taskId"))
                .body(request)
                .when()
                .patch("/tasks/{taskId}/status")
                .then()
                .statusCode(200)
                .body("id", not(empty()))
                .body("id", matchesPattern(uuidRegex))
                .body("status", equalTo(request.getStatus().toString()))
                .body("version", greaterThan(request.getVersion()))
                .log().ifValidationFails()
                .extract()
                .as(TaskResponse.class);

        credentials.put("versionTask", task.version().toString());
        credentials.put("statusTask", task.status().toString());
        Allure.step("Задача с обновленным статусом", () -> {
            Allure.attachment("task", task.toString());
        });
    }

    @Test
    @Order(5)
    @DisplayName("Удалить задачу")
    public void givenTaskId_whenTasks_thenDeleteTask() {

        given()
                .spec(commonRequestSpec())
                .auth().oauth2(credentials.get("accessToken"))
                .pathParam("taskId", credentials.get("taskId"))
                .when()
                .delete("/tasks/{taskId}")
                .then()
                .statusCode(204)
                .log().ifValidationFails();
    }

    @Test
    @Order(6)
    @DisplayName("Проверить, что задача удалена")
    public void givenTaskId_whenTasks_thenNotFoundTask() {
               given()
                .spec(commonRequestSpec())
                .auth().oauth2(credentials.get("accessToken"))
                .pathParam("taskId", credentials.get("taskId"))
                .when()
                .delete("/tasks/{taskId}")
                .then()
                .statusCode(404)
                .body("code", equalTo("NOT_FOUND"))
                .body("message", equalTo("Task not found"))
                .log().ifValidationFails();
        Allure.step("Эадача удалена", () -> {
            Allure.attachment("message", "Task not found");
        });
    }
}
