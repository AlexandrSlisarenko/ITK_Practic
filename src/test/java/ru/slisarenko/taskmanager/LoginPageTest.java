package ru.slisarenko.taskmanager;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Story;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

public class LoginPageTest {

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        Configuration.browser = "chrome";
        Configuration.timeout = 20000;
        Configuration.pollingInterval = 1000;
        Configuration.reportsFolder = "target/selenide-reports";
        Configuration.baseUrl = "http://45.141.103.56:8090";
        Configuration.holdBrowserOpen = true;
    }

    @Test
    @DisplayName("Проверка процедуры входа")
    @Story("Проверка процедуры входа")
    public void checkLoginProcedure() {
       open("/login", LoginPage.class)
               .login("qa@demo.com", "Demo123!")
               .waitForBoardLoaded();
    }

    /*@AfterEach
    public void closeBrowser() {
        Selenide.closeWebDriver();
    }*/
}
