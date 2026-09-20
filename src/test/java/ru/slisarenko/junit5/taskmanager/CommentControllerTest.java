package ru.slisarenko.junit5.taskmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.slisarenko.dto.request.UpdateCommentRequest;
import ru.slisarenko.dto.response.CommentResponse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.slisarenko.junit5.taskmanager.Specifications.commonRequestSpec;
import static ru.slisarenko.junit5.taskmanager.Specifications.getCommentId;

@Epic("Task Manager Test")
@Feature("Проверка функционала Комментарии")
@DisplayName("Проверка функционала Комментарии")
public class CommentControllerTest {
    @BeforeAll
    public static void setup() {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        RestAssured.config = RestAssuredConfig.config()
                .objectMapperConfig(ObjectMapperConfig.objectMapperConfig()
                        .jackson2ObjectMapperFactory((type, charset) -> mapper));
    }

    @Test
    @Story("Проверка получения комментария")
    @DisplayName("Проверка получения комментария")
    public void getCommentTest() {
        String id = getCommentId(0);
        UUID commentID = UUID.fromString(id);

        CommentResponse comment = given()
                .spec(commonRequestSpec())
                .pathParam("id", commentID)
                .when()
                .get("/comments/{id}")
                .then()
                .statusCode(200)
                .extract()
                .as(CommentResponse.class);
        Allure.step("По id " + id + "получили коммент", () -> {
            Allure.attachment("коммент", comment.toString());
        });
        assertEquals(commentID, comment.getId());
    }

    @Test
    @Story("Получаем и обновляем текст и версию комментария")
    @DisplayName("Получаем и обновляем текст и версию комментария")
    public void getCommentAndUpdateTextAndVersionTest() {
        String commentID = getCommentId(0);

        CommentResponse commentResponse = given()
                .spec(commonRequestSpec())
                .pathParam("id", commentID)
                .when()
                .get("/comments/{id}")
                .then()
                .statusCode(200)
                .extract()
                .as(CommentResponse.class);

        Allure.step("По id " + commentID + "получили коммент", () -> {
            Allure.attachment("коммент", commentResponse.toString());
        });

        UpdateCommentRequest requestUpdate = UpdateCommentRequest.builder()
                .body(commentResponse.getBody() + "\n Обновили в " + LocalDateTime.now().toString())
                .version(commentResponse.getVersion() + 1L)
                .build();

        Allure.step("Тело нового комментария", () -> {
            Allure.attachment("Новый комментарий", requestUpdate.toString());
        });

        CommentResponse commentUpdateResponse = given()
                .spec(commonRequestSpec())
                .pathParams("commentId", commentResponse.getId().toString())
                .body(requestUpdate)
                .when()
                .patch("/comments/{commentId}")
                .then()
                .statusCode(200)
                .extract()
                .as(CommentResponse.class);


        Allure.step("Обновленный комментарий", () -> {
            Allure.attachment("Обновленный комментарий", commentUpdateResponse.toString());
        });

        assertEquals(commentResponse.getVersion() + 1L, commentUpdateResponse.getVersion());
        assertTrue(commentUpdateResponse.getBody().contains("Обновили"));
    }

    @Test
    @Story("Удаление комментария")
    @DisplayName("Удаление комментария")
    public void deleteCommentTest() {
        String id = getCommentId(0);
        UUID commentID = UUID.fromString(id);

        given()
                .spec(commonRequestSpec())
                .pathParam("id", commentID)
                .when()
                .delete("/comments/{id}")
                .then()
                .statusCode(204)
                .log().all();

        given()
                .spec(commonRequestSpec())
                .pathParam("id", commentID)
                .when()
                .get("/comments/{id}")
                .then()
                .statusCode(404)
                .body("code", equalTo("NOT_FOUND"))
                .body("message", equalTo("Comment not found"))
                .log().ifValidationFails();
    }

}
