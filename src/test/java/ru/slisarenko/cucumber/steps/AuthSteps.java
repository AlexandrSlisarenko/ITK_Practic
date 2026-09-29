package ru.slisarenko.cucumber.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Assertions;
import ru.slisarenko.cucumber.world.ScenarioWorld;

public class AuthSteps {
    private final ScenarioWorld world;

    public AuthSteps(ScenarioWorld world) {
        this.world = world;
    }

    @Given("Создаем запрос на авторизацию для {string}")
    public void createBody(String role) {
        world.setCredentials("rrr");
    }

    @When("Отправляем запрос по адресу {string}")
    public void send(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(world.getCredentials()))
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = world.getClient().send(request,HttpResponse.BodyHandlers.ofString());
            world.setBodyLastAnswer(response.body());
            world.setStausLastAnswer(response.statusCode());
        } catch (IOException | InterruptedException exception){
            System.out.println(exception.getMessage());
        }
    }

    @Then("Проверка статуса ответа")
    public void statusOk() {
        Assertions.assertEquals(200, world.getStausLastAnswer());
    }



}
