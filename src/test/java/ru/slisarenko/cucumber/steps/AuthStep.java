package ru.slisarenko.cucumber.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Assertions;
import ru.slisarenko.cucumber.world.ScenarioWorld;
import ru.slisarenko.dto.response.ResponseAuthorization;
import ru.slisarenko.dto.response.User;

public class AuthStep {
    private final ScenarioWorld world;
    private ObjectMapper objectMapper;

    public AuthStep(ScenarioWorld world) {
        this.world = world;
        this.objectMapper = new ObjectMapper();
    }

    @Given("Создаем запрос на авторизацию для {string}")
    public void createBody(String role) {
        world.setBodyRequest(world.getCreds().get(role));
    }

    @When("Отправляем запрос")
    public void send() {
        String body = world.getBodyRequest();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .uri(URI.create(world.getCreds().get("UrlAuthorization")))
                    .build();

            HttpResponse<String> response = world.getClient().send(request, HttpResponse.BodyHandlers.ofString());
            world.setBodyLastAnswer(response.body());
            world.setStausLastAnswer(response.statusCode());
        } catch (IOException | InterruptedException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @When("Запрос информации о текущем пользователе {int}")
    public void getUser(int withToken) {
        String token = withToken == 1 ? "Bearer " + world.getToken() : "";
        String url = world.getCreds().get("baseUrl") + world.getCreds().get("start_path") + "/auth/me";
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

    @Then("Проверка статуса ответа {int}")
    public void statusOk(int status) {
        Assertions.assertEquals(status, world.getStausLastAnswer());
    }

    @And("Проверку полей в ответе")
    public void checkFields() throws JsonProcessingException {
        ResponseAuthorization authData = objectMapper.readValue(world.getBodyLastAnswer(), ResponseAuthorization.class);
        Assertions.assertFalse(authData.getAccessToken().isEmpty());
    }

    @Then("Проверяем почту пользователя")
    public void checkEmail() throws JsonProcessingException {
        User user = objectMapper.readValue(world.getBodyLastAnswer(), User.class);
        Assertions.assertEquals("admin@demo.com", user.getEmail());
    }


}
