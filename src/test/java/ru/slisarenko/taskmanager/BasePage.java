package ru.slisarenko.taskmanager;

import com.codeborne.selenide.Configuration;

public class BasePage {
    public BasePage() {
        Configuration.browser = "chrome";
        Configuration.timeout = 20000;
        Configuration.pollingInterval = 1000;
        Configuration.reportsFolder = "target/selenide-reports";
        Configuration.baseUrl = "http://45.141.103.56:8090/";
    }
}
