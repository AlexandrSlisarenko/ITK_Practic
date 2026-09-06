package ru.slisarenko;

import io.qameta.allure.Step;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    @Step("Start")
    @DisplayName("Проверяем, заработал ли Allure?")
    void checkStartAllure() {
        System.out.println("Поехали!!!!");
    }
}