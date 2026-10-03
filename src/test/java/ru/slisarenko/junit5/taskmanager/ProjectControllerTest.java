package ru.slisarenko.junit5.taskmanager;

import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.slisarenko.dto.request.CreateProjectRequest;
import ru.slisarenko.dto.request.UpdateProjectRequest;
import ru.slisarenko.dto.response.ProjectDetailResponse;
import ru.slisarenko.dto.response.ProjectMemberResponse;
import ru.slisarenko.dto.response.ProjectResponse;
import ru.slisarenko.dto.response.User;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.slisarenko.junit5.taskmanager.Specifications.commonRequestSpec;
import static ru.slisarenko.junit5.taskmanager.Specifications.getUser;

@Epic("Task Manager Test")
@Feature("Проверка функционала работы с проектами")
@DisplayName("Проверка функционала работы с проектами")
public class ProjectControllerTest {
    private static User user;

    @BeforeAll
    public static void setup() {
        user = getUser();
    }

    @Test
    @DisplayName("Получение списка проектров")
    @Story("Получение списка проектров")
    public void getProjectsTest() {
        String totalElement = given()
                .spec(commonRequestSpec())
                .queryParam("size", 0)
                .when()
                .get("/projects")
                .then()
                .statusCode(200)
                .body("meta.totalElements", not(empty()))
                .log().all()
                .extract()
                .jsonPath()
                .getString("meta.totalElements");

        List<ProjectResponse> actualList = given()
                .spec(commonRequestSpec())
                .queryParam("size", 1)
                .when()
                .get("/projects")
                .then()
                .statusCode(200)
                .body("content", not(empty()))
                .log().ifValidationFails()
                .extract()
                .jsonPath()
                .getList("content", ProjectResponse.class);

        Allure.step("Запросили 1 поект и получили 1 проект. Всего активных проектов " + totalElement, () -> {
            Allure.attachment("Response", actualList.toString());
        });

    }

    @Story("Создание нового проекта")
    @DisplayName("Создание нового проекта")
    @ParameterizedTest(name = "Имя=>{0}, описание=>{1}, key=>{2}")
    @CsvSource({
            "Test Project TP,Check create TP,TP",
            "Test Project 09,Check create 09,09",
            "Test Project AZ_09_QWER,Check create AZ_09_QWER,AZ_09_QWER",
    })
    public void checkHappyPathCreateNewProject(String nameProject, String descriptionProject, String keyProject) {
        CreateProjectRequest request = CreateProjectRequest.builder()
                .name(nameProject)
                .description(descriptionProject)
                .key(keyProject)
                .build();
        Allure.step("Данные для создания проекта", () -> Allure.attachment("Request", request.toString()));

        ProjectResponse projectActual = given()
                .spec(commonRequestSpec())
                .body(request)
                .when()
                .post("/projects")
                .then()
                .statusCode(201)
                .body("name", equalTo(nameProject))
                .body("description", equalTo(descriptionProject))
                .body("key", equalTo(keyProject))
                .body("ownerId", equalTo(user.getId()))
                .log().ifValidationFails()
                .extract()
                .as(ProjectResponse.class);

        Allure.step("Проект в результате сохранения", () -> Allure.attachment("Response", projectActual.toString()));
    }

    @Story("Проверка граничных значений длинны имени, описания и ключа проекта")
    @DisplayName("Проверка граничных значений длинны имени, описания и ключа проекта")
    @ParameterizedTest(name = "Имя=>{0}, описание=>{1}, key=>{2}")
    @CsvSource({
            ",TestName0,TP6",
            "Test200,TestName200,TP6",
            "TestDescription0,Test200,TP6",
            "TestKey,TestKey,T",
            "TestKey,TestKey,TTTTTTTTTTT"
    })
    public void checkSizeNameAndDescriptionAndKeyWhenCreateProjectResultThrow(String nameProject, String descriptionProject, String keyProject) {
        nameProject = nameProject != null && nameProject.equals("Test200") ? nameProject.repeat(29) : nameProject;
        descriptionProject = descriptionProject != null && descriptionProject.equals("Test200") ? descriptionProject.repeat(286) : descriptionProject;


        CreateProjectRequest request = CreateProjectRequest.builder()
                .name(nameProject)
                .description(descriptionProject)
                .key(keyProject)
                .build();
        Allure.step("Данные для создания проекта", () -> Allure.attachment("Request", request.toString()));

        String errorDetails = given()
                .spec(commonRequestSpec())
                .body(request)
                .when()
                .post("/projects")
                .then()
                .log().ifValidationFails()
                .statusCode(400)
                .body("code", equalTo("VALIDATION_ERROR"))
                .body("message", equalTo("Request validation failed"))
                .extract()
                .asString();

        Allure.step("Детали результата", () -> Allure.attachment("Request", errorDetails));
    }

    @Story("Проверка получения ошибки при дублировании ключа проекта")
    @DisplayName("Проверка получения ошибки при дублировании ключа проекта")
    @Test
    public void checkDuplicateKeyWhenCreateProjectResultThrow() {
        CreateProjectRequest request = CreateProjectRequest.builder()
                .name("nameProject")
                .description("descriptionProject")
                .key("DEMO")
                .build();
        Allure.step("Данные для создания проекта", () -> Allure.attachment("Request", request.toString()));

        String errorDetails = given()
                .spec(commonRequestSpec())
                .body(request)
                .when()
                .post("/projects")
                .then()
                .log().ifValidationFails()
                .statusCode(409)
                .body("code", equalTo("CONFLICT"))
                .body("message", equalTo("Project key already exists"))
                .extract()
                .asString();

        Allure.step("Детали результата", () -> Allure.attachment("Request", errorDetails));
    }

    @Story("Получаем детали проекта")
    @DisplayName("Получаем детали проекта")
    @ParameterizedTest(name = "Получаем детали проекта по id = {0}")
    @CsvSource({
            "fd6ad15e-1091-43c4-a5f7-0a1b7c7b1882, c6a14de8-7dfa-4d87-a50f-46b0f3a16e01",
    })
    public void getProjectInformationTest(String uuid, String ownerId) {
        Allure.step("Id проекта" + uuid);

        ProjectDetailResponse response = given()
                .spec(commonRequestSpec())
                .pathParam("projectId", UUID.fromString(uuid))
                .when()
                .get("/projects/{projectId}")
                .then()
                .statusCode(200)
                .log().ifValidationFails()
                .extract()
                .as(ProjectDetailResponse.class);

        Allure.step("Получили информацию о проекте",
                () -> Allure.attachment("Response", response.toString()));

        assertNotNull(response);
        assertTrue(response.getTasksCount() > 0);
        assertEquals(uuid, response.getProject().getId().toString());
        assertEquals(ownerId, response.getProject().getOwnerId().toString());
        assertFalse(response.getMembers().isEmpty());
        assertTrue(response.getMembers().stream()
                .anyMatch(member -> member.getUserId().toString().equals(user.getId())));

    }


    @Story("Проверка получения ошибки при отправке не известного id проекта")
    @DisplayName("Проверка получения ошибки при отправке не известного id проекта")
    @Test
    public void checkExceptionWhereRandomUUIDTest() {
        String uuid = "fd6ad15e-1091-43c4-a5f7-1a1b7c7b1893";
        Allure.step("Id проекта" + uuid);

        String response = given()
                .spec(commonRequestSpec())
                .pathParam("projectId", uuid)
                .when()
                .get("/projects/{projectId}")
                .then()
                .log().ifValidationFails()
                .statusCode(403)
                .body("code", equalTo("FORBIDDEN"))
                .body("message", equalTo("You are not a member of this project"))
                .extract()
                .asString();

        Allure.step("Получили исключение. Какое то странное исключение...",
                () -> Allure.attachment("Response", response.toString()));
    }

    @Story("Проверка получения ошибки при введении строки в поле id проекта")
    @DisplayName("Проверка получения ошибки при введении строки в поле id проекта")
    @Test
    public void checkExceptionWhereRandomStringTest() {
        String uuid = "asdfasdfasdfasdfasdfasdfasdfasdfasdf";
        Allure.step("Id проекта" + uuid);

        String response = given()
                .spec(commonRequestSpec())
                .pathParam("projectId", uuid)
                .when()
                .get("/projects/{projectId}")
                .then()
                .log().all()
                .statusCode(403)
                .body("code", equalTo("FORBIDDEN"))
                .body("message", equalTo("You are not a member of this project"))
                .extract()
                .asString();

        Allure.step("Получили исключение. Какое то странное исключение...",
                () -> Allure.attachment("Response", response.toString()));
    }

    @Story("Проверка получения ошибки при пустой строке в поле id проекта")
    @DisplayName("Проверка получения ошибки при пустой строке в поле id проекта")
    @Test
    public void checkExceptionWhereNullTest() {
        String uuid = "";
        Allure.step("Id проекта" + uuid);

        String response = given()
                .spec(commonRequestSpec())
                .pathParam("projectId", uuid)
                .when()
                .get("/projects/{projectId}")
                .then()
                .log().all()
                .statusCode(404)
                .body("code", equalTo("NOT_FOUND"))
                .body("message", equalTo("Resource not found"))
                .extract()
                .asString();

        Allure.step("Получили исключение",
                () -> Allure.attachment("Response", response.toString()));
    }

    @Story("Удаление проекта")
    @DisplayName("Удаление проекта")
    @ParameterizedTest(name = "Удаление проекта")
    @CsvSource({
            "0fee862c-020c-4047-af2e-08247802e218",
            "d738ab59-9f0f-4dab-9acf-5a87403e64c3",
            "83bc6e34-ffe6-4674-872f-087fafa89822",
    })
    public void checkDeleteProject(String uuid) {
        Allure.step("Id проекта на удаление " + uuid);

        given()
                .spec(commonRequestSpec())
                .pathParam("projectId", UUID.fromString(uuid))
                .when()
                .delete("/projects/{projectId}")
                .then()
                .statusCode(204)
                .log().ifValidationFails();
    }

    @Story("Проверка обновления имени описания и статуса проекта в архиве")
    @DisplayName("Проверка обновления имени описания и статуса проекта в архиве")
    @ParameterizedTest(name = "name => {0}, isArchived => {1}, id = {2}, description => {3}")
    @CsvSource({
            "Demo Project in archived,true,68727cbb-41a5-40d9-a62b-6d2d3b3b4f89,Demo Project in archived1",
            "Demo Project in archived,false,68727cbb-41a5-40d9-a62b-6d2d3b3b4f89,Demo Project in archived2"
    })
    public void patchNameDescriptionAndArchiveStatusTest(String name, String isArchived, String uuid, String description) {
        UpdateProjectRequest updateProjectRequest = UpdateProjectRequest.builder()
                .archived(Boolean.parseBoolean(isArchived))
                .name(name)
                .description(description)
                .build();

        ProjectResponse response = given()
                .spec(commonRequestSpec())
                .body(updateProjectRequest)
                .pathParams("projectId", uuid)
                .when()
                .patch("/projects/{projectId}")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(ProjectResponse.class);

        assertEquals(name, response.getName());
        assertEquals(description, response.getDescription());
        assertEquals(Boolean.parseBoolean(isArchived), response.isArchived());
    }

    @Story("Получиение списка участников проекта")
    @DisplayName("Получиение списка участников проекта")
    @Test
    public void getMembersTest() {
        String projectId = "fd6ad15e-1091-43c4-a5f7-0a1b7c7b1882";

        List<ProjectMemberResponse> response = given()
                .spec(commonRequestSpec())
                .pathParam("projectId", projectId)
                .when()
                .get("/projects/{projectId}/members")
                .then()
                .log().all()
                .extract()
                .jsonPath()
                .getList(".", ProjectMemberResponse.class);

        assertNotNull(response);
        assertFalse(response.isEmpty());
    }


}
