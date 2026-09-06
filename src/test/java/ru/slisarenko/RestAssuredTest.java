package ru.slisarenko;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class RestAssuredTest {

    public static RequestSpecification commonRequestSpec() {
        return given()
                .baseUri("http://127.0.0.1:8080")
                .contentType("application/json")
                .basePath("api/v1");
    }

    @Test
    public void getUsers_shouldReturn200() {
        // Настраиваем базовый URI
        baseURI = "https://jsonplaceholder.typicode.com";
        given()
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .headers("content-type", containsString("application/json"));

    }

    @Test
    public void getTokenAndUseIt() {
        // Данные для логина
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", "qa@demo.com");
        credentials.put("password", "Demo123!");
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
        // Теперь используем токен в защищённом запросе
       /* given()
                .spec(commonRequestSpec())
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/auth/me")
                .then()
                .log().body()
                .statusCode(200)
                .body("email", equalTo(credentials.get("email")));*/

        Response response = given()
                .spec(commonRequestSpec())
                .header("Authorization", "Bearer " + token)
                .when().get("/auth/me");
        response.prettyPrint();          // красиво напечатает тело
        response.prettyPeek();          // напечатает и статус, и заголовки, и тело
    }
}
