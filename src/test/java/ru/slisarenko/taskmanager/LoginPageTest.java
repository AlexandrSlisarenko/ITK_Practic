package ru.slisarenko.taskmanager;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.url;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("Task Manager Test")
@Feature("Проверка функционала входа")
public class LoginPageTest {

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        Configuration.browser = "chrome";
        Configuration.timeout = 20000;
        Configuration.pollingInterval = 1000;
        Configuration.reportsFolder = "target/selenide-reports";
        Configuration.baseUrl = "http://45.141.103.56:8090";
    }

    @Test
    @DisplayName("Проверка процедуры входа")
    @Story("Проверка процедуры входа")
    public void checkLoginProcedureHappyPath() {
       HomePage homePage = open("/login", LoginPage.class)
               .login("qa@demo.com", "Demo123!")
               .loadHomePage()
               .waitForBoardLoaded();
       String userName = homePage.getUserName();

       String actualUrlPage = url();
       String expectedUrlPage = Configuration.baseUrl + "/";

       assertEquals("QA Engineer", userName);
       assertEquals(expectedUrlPage, actualUrlPage);
    }

    @Test
    @DisplayName("Проверка процедуры входа c не верным login")
    @Story("Проверка процедуры входа c не верным login")
    public void checkLoginProcedureErrorLogon() {
        String errorMessage = open("/login", LoginPage.class)
                .login("qa123@demo.com", "Demo123!")
                .getErrorMessage();
        String actualUrlPage = url();
        String expectedUrlPage = Configuration.baseUrl + "/login";

        assertEquals(expectedUrlPage, actualUrlPage);
        assertEquals("Authentication failed", errorMessage);
    }

    @Test
    @DisplayName("Проверка процедуры входа c не верным password")
    @Story("Проверка процедуры входа c не верным password")
    public void checkLoginProcedureErrorPassword() {
        String errorMessage = open("/login", LoginPage.class)
                .login("qa@demo.com", "Demo!")
                .getErrorMessage();
        String actualUrlPage = url();
        String expectedUrlPage = Configuration.baseUrl + "/login";

        assertEquals(expectedUrlPage, actualUrlPage);
        assertEquals("Authentication failed", errorMessage);
    }

    @AfterEach
    public void closeBrowser() {
        Selenide.closeWebDriver();
    }
}
