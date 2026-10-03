package ru.slisarenko.cucumber.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import ru.slisarenko.cucumber.world.ScenarioWorld;
import ru.slisarenko.dto.request.RequestUpdateTaskStatus;
import ru.slisarenko.dto.request.TaskRequest;
import ru.slisarenko.dto.response.TaskResponse;
import ru.slisarenko.enums.TaskPriority;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskSteps {
    private final ScenarioWorld world;
    private ObjectMapper objectMapper;

    public TaskSteps(ScenarioWorld world) {
        this.world = world;
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Given("Поиск проекта {word}")
    public void findProject(String key) {
        String token = "Bearer " + world.getToken();
        String url = world.getCreds().get("baseUrl") + world.getCreds().get("start_path") + "/projects";
        extractedObject(token, url);
        findProjectIdByName(key);
    }

    @And("Генерация уникального заголовка")
    public void generateTitle() {
        world.setTitleTask("Test Project Cucumber " + System.currentTimeMillis());
    }

    @When("Создадим новую задачу в проекте")
    public void createNewTask() {
        TaskRequest request = TaskRequest.builder()
                .projectId(UUID.fromString(world.getIdProject()))
                .title(world.getTitleTask())
                .description("Description")
                .assigneeId(UUID.fromString(world.getUserId()))
                .dueDate(Instant.now().plusSeconds(300))
                .priority(TaskPriority.MEDIUM)
                .build();

        String token = "Bearer " + world.getToken();
        String url = world.getCreds().get("baseUrl") + world.getCreds().get("start_path") + "/tasks";
        try {
            String json = objectMapper.writeValueAsString(request);
            HttpRequest request1 = HttpRequest.newBuilder()
                    .header("Content-Type", "application/json")
                    .header("Authorization", token)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = world.getClient().send(request1, HttpResponse.BodyHandlers.ofString());
            world.setBodyLastAnswer(response.body());
            world.setStausLastAnswer(response.statusCode());


        } catch (IOException | InterruptedException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Then("В результате статус ответа {int}, статус задачи {word}, версия {int}")
    public void checkCreateTask(int statusResponse, String statusTask, int version) {
        try {
            TaskResponse task = objectMapper.readValue(world.getBodyLastAnswer(), TaskResponse.class);
            assertEquals(statusResponse, world.getStausLastAnswer());
            assertEquals(statusTask, task.getStatus().name());
            assertEquals(version, task.getVersion());
            world.setIdCreatedTask(task.getId().toString());
            world.setVersion(task.getVersion());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    @When("Получаем созданную задачу")
    public void findCreatedTask() {
        String token = "Bearer " + world.getToken();
        String url = world.getCreds().get("baseUrl") + world.getCreds().get("start_path") + "/tasks/" + world.getIdCreatedTask();
        try {
            HttpRequest request1 = HttpRequest.newBuilder()
                    .header("Content-Type", "application/json")
                    .header("Authorization", token)
                    .GET()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = world.getClient().send(request1, HttpResponse.BodyHandlers.ofString());
            world.setBodyLastAnswer(response.body());
            world.setStausLastAnswer(response.statusCode());

        } catch (IOException | InterruptedException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Then("В результате заголовки совпадают")
    public void checkTitleTask() {
        try {
            String taskTitle = this.objectMapper.readTree(world.getBodyLastAnswer()).at("/task/title").asText();
            assertEquals(world.getTitleTask(),taskTitle);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @When("Переводим статус в {word}")
    public void updateStatus(String status) {

        RequestUpdateTaskStatus updateTaskStatus = RequestUpdateTaskStatus.builder()
                .status(status)
                .version(world.getVersion())
                .build();
        String token = "Bearer " + world.getToken();
        String url = world.getCreds().get("baseUrl")
                     + world.getCreds().get("start_path")
                     + "/tasks/"
                     + world.getIdCreatedTask()
                     + "/status";
        try {
            String json = objectMapper.writeValueAsString(updateTaskStatus);
            HttpRequest request1 = HttpRequest.newBuilder()
                    .header("Content-Type", "application/json")
                    .header("Authorization", token)
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(json))
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = world.getClient().send(request1, HttpResponse.BodyHandlers.ofString());
            world.setBodyLastAnswer(response.body());
            world.setStausLastAnswer(response.statusCode());

        } catch (IOException | InterruptedException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @When("Удаляем созданную задачу")
    public void deleteTask() {
        String token = "Bearer " + world.getToken();
        String url = world.getCreds().get("baseUrl")
                     + world.getCreds().get("start_path")
                     + "/tasks/"
                     + world.getIdCreatedTask();
        try {
            HttpRequest request1 = HttpRequest.newBuilder()
                    .header("Content-Type", "application/json")
                    .header("Authorization", token)
                    .DELETE()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = world.getClient().send(request1, HttpResponse.BodyHandlers.ofString());
            world.setBodyLastAnswer(response.body());
            world.setStausLastAnswer(response.statusCode());

        } catch (IOException | InterruptedException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Then("В результате статус {int}")
    public void checkDeleteTask(int statusResponse) {
            assertEquals(statusResponse, world.getStausLastAnswer());
    }

    private void findProjectIdByName(String key){
        try {
            Iterator<JsonNode> actualList = this.objectMapper.readTree(world.getBodyLastAnswer()).at("/content").elements();
            actualList.forEachRemaining(jsonNode -> {
                if (jsonNode.at("/key").asText().equals(key)) {
                    world.setIdProject(jsonNode.at("/id").asText());
                }
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void extractedObject(String token, String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .header("Content-Type", "application/json")
                    .header("Authorization", token)
                    .GET()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = world.getClient().send(request, HttpResponse.BodyHandlers.ofString());
            world.setBodyLastAnswer(response.body());
            world.setStausLastAnswer(response.statusCode());


        } catch (IOException | InterruptedException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
