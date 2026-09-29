package ru.slisarenko.cucumber.hooks;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.BeforeStep;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import lombok.AllArgsConstructor;
import ru.slisarenko.cucumber.world.ScenarioWorld;
import ru.slisarenko.dto.response.ResponseAuthorization;


@AllArgsConstructor
public class ApiHooks {
    private static Map<String, String> env;
    private final ScenarioWorld world;


    @BeforeAll
    public static void beforeAll() {
        env = new HashMap<>();
        Properties props = new Properties();
        try (InputStream is = ApiHooks.class.getClassLoader().getResourceAsStream("application.properties")) {
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Set<String> keys = props.stringPropertyNames();
        keys.forEach(key -> {
            env.put(key, props.getProperty(key));
        });
    }

    @Before("@api or @smoke")
    public void setEnv() {
        world.setCreds(env);
        world.setClient(HttpClient.newHttpClient());

    }

    @BeforeStep("@api or @smoke")
    public void setToken() {
        getToken();
    }

   private void getToken() {

        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(world.getCreds().get("ADMIN")))
                .uri(URI.create(world.getCreds().get("UrlAuthorization")))
                .build();
        try {
            HttpResponse<String> response = world.getClient().send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper();
            ResponseAuthorization responseAuthorization = objectMapper.readValue(response.body(), ResponseAuthorization.class);
            world.setToken(responseAuthorization.getAccessToken());
        }  catch (IOException | InterruptedException exception){
            System.out.println(exception.getMessage());
        }

    }
}
