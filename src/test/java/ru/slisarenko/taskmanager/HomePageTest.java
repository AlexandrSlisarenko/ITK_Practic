package ru.slisarenko.taskmanager;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.url;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Task Manager Test")
@Feature("Проверка проверка функционала выхода")
public class HomePageTest {

    private static final Logger log = LoggerFactory.getLogger(HomePageTest.class);

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        Configuration.browser = "chrome";
        Configuration.timeout = 20000;
        Configuration.pollingInterval = 1000;
        Configuration.reportsFolder = "target/selenide-reports";
        Configuration.baseUrl = "http://45.141.103.56:8090";
    }

    @Test
    @DisplayName("Проверка функционала выхода")
    @Story("Проверка функционала выхода")
    public void checkLogoutTest() {
        HomePage homePage = open("/", LoginPage.class)
                .login("qa@demo.com", "Demo123!")
                .loadHomePage()
                .waitForBoardLoaded();
        LoginPage loginPage = homePage.logout();

        boolean isLoginPage = loginPage.isOnLoginPage();
        String actualUrlPage = url();
        String expectedUrlPage = Configuration.baseUrl + "/login";

        assertEquals(expectedUrlPage, actualUrlPage);
        assertTrue(isLoginPage);
    }

    @AfterEach
    public void closeBrowser() {
        Selenide.closeWebDriver();
    }

}
