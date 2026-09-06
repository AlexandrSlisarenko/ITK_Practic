package ru.slisarenko.hw;

import io.restassured.specification.RequestSpecification;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class SlisarenkoTaskLifecycleApiTest {
    private static Map<String, String> credentials;

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
                .extract()
                .path("accessToken");

        credentials.put("accessToken", token);
    }

    @Test
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
                .extract()
                .jsonPath()
                .getList("content", ProjectResponse.class);
        assertNotNull(projects);

        UUID id = projects.stream()
                .filter(p -> p.getKey().equals("DEMO"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not found project with key DEMO"))
                .getId();

        credentials.put("projectId", id.toString());
    }

    @Test
    @DisplayName("Создать задачу в проекте DEMO")
    public void givenTask_whenTasks_thenCreateTask() {
        givenAccessToken_whenProjects_thenListProjects();
        if( credentials.get("taskId") == null ) {
            String id = given()
                    .spec(commonRequestSpec())
                    .auth().oauth2(credentials.get("accessToken"))
                    .when()
                    .get("/auth/me")
                    .then()
                    .statusCode(200)
                    .extract()
                    .path("id");
            credentials.put("userId", id);

            TaskCreateRequest taskCreateRequest = TaskCreateRequest.builder()
                    .projectId(UUID.fromString(credentials.get("projectId")))
                    .title("HW-CAA-" + System.currentTimeMillis())
                    .assigneeId(UUID.fromString(credentials.get("userId")))
                    .description("Проверка создания задания в Rest Assure")
                    .dueDate(Instant.now().plusSeconds(300))
                    .priority(TaskPriority.MEDIUM)
                    .build();


            TaskResponse task = given()
                    .spec(commonRequestSpec())
                    .auth().oauth2(credentials.get("accessToken"))
                    .body(taskCreateRequest)
                    .when()
                    .post("/tasks")
                    .then()
                    .statusCode(201)
                    .extract()
                    .as(TaskResponse.class);
            assertEquals(task.status(), TaskStatus.TODO);
            assertEquals(task.version(), 0);
            credentials.put("taskId", task.id().toString());
            credentials.put("versionTask", task.version().toString());
            credentials.put("titleTask", task.title());
        }
    }

    @Test
    @DisplayName("Получить созданную задачу по ID")
    public void givenTaskId_whenTasks_thenTask() {
        givenAccessToken_whenProjects_thenListProjects();
        givenTask_whenTasks_thenCreateTask();
        TaskResponse task = given()
                .spec(commonRequestSpec())
                .auth().oauth2(credentials.get("accessToken"))
                .pathParam("taskId", credentials.get("taskId"))
                .when()
                .get("/tasks/{taskId}")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getObject("task", TaskResponse.class);

        assertEquals(task.id(), UUID.fromString(credentials.get("taskId")));
        assertEquals(task.title(),credentials.get("titleTask"));
        credentials.put("versionTask", task.version().toString());
    }

    @Test
    @DisplayName("Обновить статус задачи (TODO → IN_PROGRESS)")
    public void givenTaskIdAndStatus_whenTasks_thenUpdateStatusTask() {
        if(credentials.get("statusTask") == null) {
            givenAccessToken_whenProjects_thenListProjects();
            givenTask_whenTasks_thenCreateTask();
            givenTaskId_whenTasks_thenTask();

            StatusTaskUpdateRequest request = StatusTaskUpdateRequest.builder()
                    .status(TaskStatus.IN_PROGRESS)
                    .version(Integer.parseInt(credentials.get("versionTask")))
                    .build();
            TaskResponse task = given()
                    .spec(commonRequestSpec())
                    .auth().oauth2(credentials.get("accessToken"))
                    .pathParam("taskId", credentials.get("taskId"))
                    .body(request)
                    .when()
                    .patch("/tasks/{taskId}/status")
                    .then()
                    .statusCode(200)
                    .extract()
                    .as(TaskResponse.class);

            assertTrue(task.version() > Integer.parseInt(credentials.get("versionTask")));
            assertEquals(TaskStatus.IN_PROGRESS, task.status());
            credentials.put("versionTask", task.version().toString());
            credentials.put("statusTask", TaskStatus.IN_PROGRESS.toString());
        }
    }

    @Test
    @DisplayName("Удалить задачу")
    public void givenTaskId_whenTasks_thenDeleteTask() {
        givenAccessToken_whenProjects_thenListProjects();
        givenTask_whenTasks_thenCreateTask();
        givenTaskId_whenTasks_thenTask();
        givenTaskIdAndStatus_whenTasks_thenUpdateStatusTask();

        given()
                .spec(commonRequestSpec())
                .auth().oauth2(credentials.get("accessToken"))
                .pathParam("taskId", credentials.get("taskId"))
                .when()
                .delete("/tasks/{taskId}")
                .then()
                .statusCode(204);
    }

    @Test
    @DisplayName("Проверить, что задача удалена")
    public void givenTaskId_whenTasks_thenNotFoundTask() {
        givenAccessToken_whenProjects_thenListProjects();
        givenTask_whenTasks_thenCreateTask();
        givenTaskId_whenTasks_thenTask();
        givenTaskIdAndStatus_whenTasks_thenUpdateStatusTask();
        givenTaskId_whenTasks_thenDeleteTask();

        given()
                .spec(commonRequestSpec())
                .auth().oauth2(credentials.get("accessToken"))
                .pathParam("taskId", credentials.get("taskId"))
                .when()
                .delete("/tasks/{taskId}")
                .then()
                .statusCode(404);
    }
}
