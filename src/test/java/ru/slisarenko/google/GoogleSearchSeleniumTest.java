package ru.slisarenko.google;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("Домашняя страница Google")
@Feature("Проверка внешнего вида")
public class GoogleSearchSeleniumTest {
    private GoogleSearchPage page;

    @BeforeEach
    public void start() {
        page = new GoogleSearchPage();
    }

    @Test
    @DisplayName("Проверка функциональности поиска")
    @Story("Проверка функциональности поиска(не работает из за капчи)")
    public void googleSearchTest() {
        String searchText = "Selenide vs Selenium";
        String expected = "Selenide: concise UI tests in Java";

        String actual = this.page.getHeaderResultSearchText(searchText);

        assertEquals(expected, actual);

    }

    @Test
    @DisplayName("Проверка активности кнопки 'Мне повезет'")
    @Story("Проверка активности кнопки 'Мне повезет'")
    public void checkEnableLuckButton() {
        boolean isEnabled = page.isEnabledLuckButton();
        Assertions.assertTrue(isEnabled);
    }

    @Test
    @DisplayName("Проверка isDisplayed кнопки 'Мне повезет'")
    @Story("Проверка isDisplayed кнопки 'Мне повезет'")
    public void checkDisplayedLuckButton() {
        boolean isDisplayed = page.isDisplayedLuckButton();
        Assertions.assertTrue(isDisplayed);
    }

    @Test
    @DisplayName("Проверка ссылки 'Все о Google'")
    @Story("Проверка ссылки 'Все о Google'")
    public void checkLinkSettings() {
        boolean isEnabledAboutGoogle = page.isEnabledAboutGoogle();
        boolean isDisplayedAboutGoogle = page.isDisplayedAboutGoogle();
        boolean checkHref = page.checkHrefAboutGoogleNotExistsOrNotEmpty();

        Assertions.assertTrue(isEnabledAboutGoogle);
        Assertions.assertTrue(isDisplayedAboutGoogle);
        Assertions.assertTrue(checkHref);
    }

    @Test
    @DisplayName("Проверка логотипа 'Google'")
    @Story("Проверка логотипа 'Google'")
    public void checkLogo() {
        boolean isEnabled = page.isEnabledLogo();
        boolean isDisplayed = page.isDisplayedLogo();

        Assertions.assertTrue(isEnabled);
        Assertions.assertTrue(isDisplayed);
    }

    @Test
    @DisplayName("Проверка ссылки 'Реклама'")
    @Story("Проверка ссылки 'Реклама'")
    public void checkAdvertising() {
        boolean isContain = page.checkHrefAdvertising("https://www.google.com");

        Assertions.assertTrue(isContain);
    }

    @AfterEach
    public void quitDriver() {
        page.quit();
    }
}
