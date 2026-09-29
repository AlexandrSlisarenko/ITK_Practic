package ru.slisarenko.cucumber.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Iterator;
import ru.slisarenko.cucumber.world.ScenarioWorld;
import ru.slisarenko.dto.request.TaskRequest;

public class TaskSteps {
    private final ScenarioWorld world;
    private ObjectMapper objectMapper;

    public TaskSteps(ScenarioWorld world) {
        this.world = world;
        this.objectMapper = new ObjectMapper();
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
                .build();
        String token = "Bearer " + world.getToken();
        String url = world.getCreds().get("baseUrl") + world.getCreds().get("start_path") + "/projects";
        try {
            HttpRequest request1 = HttpRequest.newBuilder()
                    .header("Content-Type", "application/json")
                    .header("Authorization", token)
                    .POST(HttpRequest.BodyPublishers.ofString(request.toString()))
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = world.getClient().send(request1, HttpResponse.BodyHandlers.ofString());
            world.setBodyLastAnswer(response.body());
            world.setStausLastAnswer(response.statusCode());


        } catch (IOException | InterruptedException exception) {
            System.out.println(exception.getMessage());
        }
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
    /*
    * Given Поиск проекта DEMO
    And Генерация уникальным заголовком
    When Создадим новую задачу в проектре
    Then В результате статус ответа 201, статус задачи TODO, версия 0

    When Получаем созданную задачу
    Then В результате заголовки совпадают

    When Переводим статус в IN_PROGRESS
    Then В результате статус ответа 200, статус задачи IN_PROGRESS, версия 1

    When Удаляем созданную задачу
    Then В результате статус 204, код NET

    When Получаем созданную задачу
    Then В результате статус 404, код NOT_FOUND
    * */
}
