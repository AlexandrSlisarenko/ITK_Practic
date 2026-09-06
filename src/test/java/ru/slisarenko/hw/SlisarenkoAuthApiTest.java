package ru.slisarenko.hw;

import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.specification.RequestSpecification;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


@Epic("Task Manager")
@Feature("Аутентификация")
public class SlisarenkoAuthApiTest {
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
    }
    @Test
    @DisplayName("Получение токена по email и паролю")
    @Story("Получение токена по email и паролю")
    public void givenEmailAndPassword_whenLogin_thenAccessToken() {
        // Отправляем POST и извлекаем токен
        String token = given()
                .spec(commonRequestSpec())
                .body(credentials)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .path("accessToken");   // предполагаем, что токен в поле "token"
        Allure.step("Получили токен", () -> {
            Allure.attachment("Ответ", token);
        });
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Получение информации о пользователе по токену")
    @Story("Получение информации о пользователе по токену")
    public void givenToken_whenMe_thenInfo() {

        String token = given()
                .spec(commonRequestSpec())
                .body(credentials)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .path("accessToken");
        Allure.step("Получили токен", () -> {
            Allure.attachment("Ответ", token);
        });
        // Отправляем POST и извлекаем токен
        String email = given()
                .spec(commonRequestSpec())
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/auth/me")
                .then()
                .statusCode(200)
                .extract()
                .path("email");
        Allure.step("Получили почту пользователя", () -> {
            Allure.attachment("email", email);
        });
        assertEquals(email, credentials.get("email"));
    }

    @Test
    @DisplayName("Проверка не доступности информации без токена")
    public void givenNotToken_whenMe_then401Unauthorized() {

        String code = given()
                .spec(commonRequestSpec())
                .when()
                .get("/auth/me")
                .then()
                .statusCode(401)
                .extract()
                .path("code");

        assertEquals(code, "UNAUTHORIZED");
    }

}
