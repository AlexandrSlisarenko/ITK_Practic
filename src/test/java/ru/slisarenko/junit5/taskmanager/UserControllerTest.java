package ru.slisarenko.junit5.taskmanager;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.slisarenko.dto.request.RequestPage;
import ru.slisarenko.dto.request.RequestUpdateUser;
import ru.slisarenko.dto.response.TaskResponse;
import ru.slisarenko.dto.response.User;
import ru.slisarenko.enums.TaskStatus;

import static io.restassured.RestAssured.given;
import static ru.slisarenko.junit5.taskmanager.Specifications.commonRequestSpec;
import static ru.slisarenko.junit5.taskmanager.Specifications.getUser;

@Epic("Task Manager Test")
@Feature("Проверка функционала работы с пользователем")
@DisplayName("Проверка функционала работы с пользователем")
public class UserControllerTest {
    private static User userTest;

    @BeforeAll
    public static void setup() {
        userTest = getUser();
    }

    @Story("Получаем список пользователей")
    @DisplayName("Получаем список пользователей")
    @Test
    public void getUsers(){
        RequestPage requestPage = RequestPage.builder()
                .page(1)
                .size(10)
                .sort(List.of("firstName", "lastName"))
                .build();
        List<User> users = given()
                .spec(commonRequestSpec())
                .body(requestPage)
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .log().ifValidationFails()
                .extract().jsonPath()
                .getList("content", User.class);

        Assertions.assertNotNull(users);
        Assertions.assertFalse(users.isEmpty());
    }

    @Story("Получаем пользователя по Id")
    @DisplayName("Получаем пользователя по Id")
    @Test
    public void getUserByIdTest(){
        User user = given()
                .spec(commonRequestSpec())
                .pathParam("userId", userTest.getId())
                .when()
                .get("/users/{userId}")
                .then()
                .statusCode(200)
                .log().ifValidationFails()
                .extract()
                .as(User.class);

        Assertions.assertNotNull(user);
    }

    @Disabled("Надо подумать как данные брать")
    @Story("Мягко удаляем пользователя по Id")
    @DisplayName("Мягко удаляем пользователя по Id")
    @Test
    public void softDeleteUserByIdTest(){
                given()
                .spec(commonRequestSpec())
                .pathParam("userId", userTest)
                .when()
                .delete("/users/{userId}")
                .then()
                .statusCode(204)
                .log().ifValidationFails();

    }

    @Story("Обновляем пользователя по Id")
    @DisplayName("Обновляем пользователя по Id")
    @Test
    public void updateUserByIdTest(){
        User user = given()
                .spec(commonRequestSpec())
                .pathParam("userId", userTest.getId())
                .when()
                .get("/users/{userId}")
                .then()
                .statusCode(200)
                .log().ifValidationFails()
                .extract()
                .as(User.class);

        Assertions.assertNotNull(user);

        RequestUpdateUser updateUser = RequestUpdateUser.builder()
                .avatarUrl(user.getAvatarUrl())
                .firstName(user.getFirstName() + "1")
                .lastName(user.getLastName() + "1")
                .build();

        User result = given()
                .spec(commonRequestSpec())
                .pathParam("userId", userTest.getId())
                .body(updateUser)
                .when()
                .patch("/users/{userId}")
                .then()
                .statusCode(200)
                .log().ifValidationFails()
                .extract()
                .as(User.class);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(user.getFirstName() + "1", result.getFirstName());

    }
    @Story("Получаем задачи пользователя по Id")
    @DisplayName("Получаем задачи пользователя по Id")
    @Test
    public void getTasksByUserIdTest(){
        String testStatus = TaskStatus.TODO.name();
        RequestPage requestPage = RequestPage.builder()
                .page(1)
                .size(10)
                .sort(List.of("firstName", "lastName"))
                .build();

        List<TaskResponse> listTask = given()
                .spec(commonRequestSpec())
                .pathParam("userId", userTest.getId())
                .queryParam("status", testStatus)
                .body(requestPage)
                .when()
                .get("/users/{userId}/tasks")
                .then()
                .statusCode(200)
                .log().ifValidationFails()
                .extract()
                .jsonPath()
                .getList("content", TaskResponse.class);

        Assertions.assertNotNull(listTask);
        Assertions.assertFalse(listTask.isEmpty());
    }
    @Story("Получаем задачи пользователя по Id")
    @DisplayName("Получаем задачи пользователя по Id")
    @Test
    public void getProfileUserTest(){


       User user = given()
                .spec(commonRequestSpec())
                .when()
                .get("/users/me/profile")
                .then()
                .statusCode(200)
                .log().ifValidationFails()
                .extract()
                .jsonPath()
                .getObject("user", User.class);

        Assertions.assertNotNull(user);
    }
}
