package ru.slisarenko.junit5.taskmanager;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.slisarenko.dto.request.RequestPage;
import ru.slisarenko.dto.request.RequestUpdateUser;
import ru.slisarenko.dto.response.TaskResponse;
import ru.slisarenko.dto.response.User;
import ru.slisarenko.enums.TaskStatus;

import static io.restassured.RestAssured.given;
import static ru.slisarenko.junit5.taskmanager.Specifications.commonRequestSpec;

@Epic("Task Manager Test")
@Feature("Проверка функционала работы с пользователем")
@DisplayName("Проверка функционала работы с пользователем")
public class UserControllerTest {

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
                .log().all()
                .extract().jsonPath()
                .getList("content", User.class);

        Assertions.assertNotNull(users);
        Assertions.assertFalse(users.isEmpty());
    }

    @Story("Получаем пользователя по Id")
    @DisplayName("Получаем пользователя по Id")
    @Test
    public void getUserByIdTest(){
        String id = "1745baa9-d847-45ab-8a07-29ebf9020b79";

        User user = given()
                .spec(commonRequestSpec())
                .pathParam("userId", id)
                .when()
                .get("/users/{userId}")
                .then()
                .statusCode(200)
                .log().all()
                .extract()
                .as(User.class);

        Assertions.assertNotNull(user);
        Assertions.assertEquals("lead@demo.com", user.getEmail());
    }

    @Story("Получаем пользователя по Id")
    @DisplayName("Получаем пользователя по Id")
    @Test
    public void softDeleteUserByIdTest(){
        String id = "1745baa9-d847-45ab-8a07-29ebf9020b79";

            given()
                .spec(commonRequestSpec())
                .pathParam("userId", id)
                .when()
                .delete("/users/{userId}")
                .then()
                .statusCode(204)
                .log().all();

    }

    @Story("Обновляем пользователя по Id")
    @DisplayName("Обновляем пользователя по Id")
    @Test
    public void updateUserByIdTest(){
        String id = "c620d776-e7f9-435b-b2a5-710981e5c44d";

        User user = given()
                .spec(commonRequestSpec())
                .pathParam("userId", id)
                .when()
                .get("/users/{userId}")
                .then()
                .statusCode(200)
                .log().all()
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
                .pathParam("userId", id)
                .body(updateUser)
                .when()
                .patch("/users/{userId}")
                .then()
                .statusCode(200)
                .log().all()
                .extract()
                .as(User.class);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(user.getFirstName() + "1", result.getFirstName());

    }

    @Story("Получаем задачи пользователя по Id")
    @DisplayName("Получаем задачи пользователя по Id")
    @Test
    public void getTasksByUserIdTest(){
        String id = "5404a5c9-1559-49de-99a8-12a5c5ed3f89";
        String testStatus = TaskStatus.TODO.name();
        RequestPage requestPage = RequestPage.builder()
                .page(1)
                .size(10)
                .sort(List.of("firstName", "lastName"))
                .build();

        List<TaskResponse> listTask = given()
                .spec(commonRequestSpec())
                .pathParam("userId", id)
                .queryParam("status", testStatus)
                .body(requestPage)
                .when()
                .get("/users/{userId}/tasks")
                .then()
                .statusCode(200)
                .log().all()
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
                .log().all()
                .extract()
                .jsonPath()
                .getObject("user", User.class);

        Assertions.assertNotNull(user);
    }
}
