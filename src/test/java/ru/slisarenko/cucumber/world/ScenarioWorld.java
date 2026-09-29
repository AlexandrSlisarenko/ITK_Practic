package ru.slisarenko.cucumber.world;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ScenarioWorld {
    private String token;
    private String idProject;
    private String idCreatedTask;
    private String titleTask;
    private int version;
    private int stausLastAnswer;
    private String bodyLastAnswer;
    private String credentials;
    private HttpClient client;
    private HttpRequest request;
}
