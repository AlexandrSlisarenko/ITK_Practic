package ru.slisarenko.junit5.taskmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.specification.RequestSpecification;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import ru.slisarenko.dto.response.CommentResponse;
import ru.slisarenko.dto.response.ProjectResponse;
import ru.slisarenko.dto.response.TaskResponse;
import ru.slisarenko.dto.response.User;
import ru.slisarenko.enums.TaskStatus;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

public class Specifications {

    public static void initMapper(){
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        RestAssured.config = RestAssuredConfig.config()
                .objectMapperConfig(ObjectMapperConfig.objectMapperConfig()
                        .jackson2ObjectMapperFactory((type, charset) -> mapper));
    }

    public static RequestSpecification commonRequestSpec() {
        String token = getToken();
        return given()
                .baseUri("http://45.141.103.56:8090")
                .auth().oauth2(token)
                .contentType("application/json")
                .basePath("api/v1");
    }

    private static Map<String, String> getCredentials(){
        Map<String, String> credentials = new HashMap<>();
        //credentials.put("email", "qa@demo.com");
        credentials.put("email", "admin@demo.com");
        credentials.put("password", "Demo123!");
        return credentials;
    }

    private static String getToken() {
        Map<String, String> credentials = getCredentials();

        String token = given()
                .baseUri("http://45.141.103.56:8090")
                .contentType("application/json")
                .basePath("api/v1")
                .body(credentials)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .log().ifValidationFails()
                .extract()
                .path("accessToken");
        return token;
    }

    public static User getUser() {
        Map<String, String> credentials = getCredentials();
        return given()
                .baseUri("http://45.141.103.56:8090")
                .contentType("application/json")
                .basePath("api/v1")
                .body(credentials)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .log().ifValidationFails()
                .extract()
                .jsonPath()
                .getObject("user", User.class);
    }

    public static ProjectResponse getProjectByName(String projectName) {
        String token = getToken();
        List<ProjectResponse> projects = given()
                .spec(commonRequestSpec())
                .auth().oauth2(token)
                .param("size", 20)
                .when()
                .get("/projects")
                .then()
                .statusCode(200)
                .body("content", not(empty()))
                .body("content.key", hasItem(projectName))
                .log().ifValidationFails()
                .extract()
                .jsonPath()
                .getList("content", ProjectResponse.class);


        return projects.stream()
                .filter(p -> p.getKey().equals(projectName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not found project with key " + projectName));
    }

    public static List<TaskResponse> getTasksByProjectId(UUID projectId, TaskStatus status) {
        String token = getToken();

        return given()
                .spec(commonRequestSpec())
                .auth().oauth2(token)
                .queryParam("projectId", projectId)
                .queryParam("status", status.toString())
                .when()
                .get("/tasks")
                .then()
                .statusCode(200)
                .body("content", not(empty()))
                .log().ifValidationFails()
                .extract()
                .jsonPath()
                .getList("content", TaskResponse.class);
    }

    public static String getCommentId(int commentNumber) {
        int i = 0;
        CommentResponse comment = null;
        String token = getToken();
        UUID projectId = getProjectByName("DEMO").getId();
        List<TaskResponse> tasks = getTasksByProjectId(projectId, TaskStatus.TODO);


        while (i < tasks.size()) {
            List<CommentResponse> comments = given()
                    .spec(commonRequestSpec())
                    .auth().oauth2(token)
                    .pathParams("taskId", tasks.get(i).getId())
                    .when()
                    .get("/tasks/{taskId}/comments")
                    .then()
                    .statusCode(200)
                    .log().ifValidationFails()
                    .extract()
                    .jsonPath()
                    .getList("content", CommentResponse.class);
            if (!comments.isEmpty()) {
                if (commentNumber < comments.size()) {
                    comment = comments.get(commentNumber);
                    break;
                } else {
                    comment = comments.get(0);
                    break;
                }
            }
            i++;
        }

        return comment == null ? "" : comment.getId().toString();
    }
}
