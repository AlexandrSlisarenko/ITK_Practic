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
import ru.slisarenko.dto.response.ProjectDetailResponse;
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

        Allure.step("Запросили 1 поект и получили 1 проект. Всего активных проектов " + totalElement, () ->{
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

        Allure.step("Проект в результате сохранения",()-> Allure.attachment("Response", projectActual.toString()));
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
                ()-> Allure.attachment("Response", response.toString()));

        assertNotNull(response);
        assertTrue(response.getTasksCount() > 0);
        assertEquals(uuid, response.getProject().getId().toString());
        assertEquals(ownerId, response.getProject().getOwnerId().toString());
        assertFalse(response.getMembers().isEmpty());
        assertTrue(response.getMembers().stream()
                .anyMatch(member -> member.getUserId().toString().equals(user.getId())));

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
}
