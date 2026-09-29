package ru.slisarenko.cucumber.hooks;

import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import lombok.AllArgsConstructor;
import ru.slisarenko.cucumber.world.ScenarioWorld;


@AllArgsConstructor
public class ApiHooks {
    private final ScenarioWorld world;


    @BeforeAll
    public static void beforeAll() {

    }

    @Before("@api")
    public void openBrowser() {
        Map<String, String> env = new HashMap<>();
        Properties props = new Properties();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Set<String> keys = props.stringPropertyNames();
        keys.forEach(key -> {
            env.put(key, props.getProperty(key));
        });
        world.setCredentials(env.toString());
        world.setClient(HttpClient.newHttpClient());
    }
}
