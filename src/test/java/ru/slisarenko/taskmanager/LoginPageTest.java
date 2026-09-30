package ru.slisarenko.taskmanager;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.url;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("Task Manager Test")
@Feature("Проверка функционала входа")
@DisplayName("Проверка функционала входа")
public class LoginPageTest {

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        Configuration.browser = "chrome";
        Configuration.timeout = 20000;
        Configuration.pollingInterval = 1000;
        Configuration.reportsFolder = "target/selenide-reports";
        Configuration.baseUrl = "https://demo.itklabs.online";
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

    @Disabled("Нужны корректные данные")
    @ParameterizedTest(name = "Проверка процедуры входа с email =>{0}, password => {1}")
    @DisplayName("Проверка процедуры входа с email и password")
    @CsvSource({"qa@demo.com, Demo123!, https://demo.itklabs.online/",
            "qa123@demo.com, Demo123!, https://demo.itklabs.online/login",
            "qa@demo.com, Demo!, https://demo.itklabs.online/login"
    })
    public void checkLoginProcedure(String email, String password, String expected) {
        if(expected.equals("https://demo.itklabs.online/")) {
            open("/login", LoginPage.class)
                    .login("qa@demo.com", "Demo123!")
                    .loadHomePage()
                    .waitForBoardLoaded();
        } else {
            open("/login", LoginPage.class)
                    .login(email, password)
                    .getErrorMessage();
        }

        String actualUrlPage = url();

        assertEquals(expected, actualUrlPage);
    }

    @AfterEach
    public void closeBrowser() {
        Selenide.closeWebDriver();
    }
}
