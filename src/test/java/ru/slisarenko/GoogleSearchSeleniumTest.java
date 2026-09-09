package ru.slisarenko;

import java.time.Duration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.*;

public class GoogleSearchSeleniumTest {
    private GoogleSearchPage page;

    @BeforeEach
    public void start() {

        page = new GoogleSearchPage();
    }

    @Test
    @DisplayName("Проверка функциональности поиска")
    public void googleSearchTest() {
        String searchText = "Selenide vs Selenium";
        String expected = "Selenide: concise UI tests in Java";

        String actual = this.page.getHeaderResultSearchText(searchText);

        assertEquals(expected, actual);
    }

    /*@AfterEach
    public void quitDriver() {
        page.quit();
    }*/
}
