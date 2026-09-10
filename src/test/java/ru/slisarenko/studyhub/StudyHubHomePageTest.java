package ru.slisarenko.studyhub;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Домашняя страница Study Hub")
@Feature("Проверка функционала")
public class StudyHubHomePageTest {
   private StudyHubHomePage homePage;

   @BeforeEach
   public void setUp() {
       homePage = new StudyHubHomePage();
   }

   @Test
   @DisplayName("Проверка входа на портал")
   @Story("Проверка входа на портал")
   public void checkLogin() {
       boolean isLogin = homePage.checkLogin();
       assertTrue(isLogin);
   }

   @Test
   @DisplayName("Проверка отображения логотипа")
   @Story("Проверка отображения логотипа")
   public void checkDisplayedAndEnabledLogo(){
       homePage.viewHomePage();
       boolean isDisplayed = homePage.isDisplayedLogo();
       boolean isEnabled = homePage.isEnabledLogo();

       assertTrue(isEnabled);
       assertTrue(isDisplayed);
   }

   @Test
   @DisplayName("Проверка сворачивания бокового слайдера")
   @Story("Проверка сворачивания бокового слайдера")
   public void checkSideSliderCollapse(){
       homePage.viewHomePage();
       boolean sliderIsOpen = homePage.isDisplayedLinkTextAndBrandText(false);
       assertTrue(sliderIsOpen);
       Allure.step("Слайдер изначально раскрыт");
       homePage.clickSidebarToggleButton();
       Allure.step("Нажимаем на слайдер");
       sliderIsOpen = homePage.isDisplayedLinkTextAndBrandText(true);
       Assertions.assertFalse(sliderIsOpen);
       Allure.step("Слайдер свернулся");
       homePage.clickSidebarToggleButton();
       Allure.step("Нажимаем на слайдер");
       sliderIsOpen = homePage.isDisplayedLinkTextAndBrandText(false);
       assertTrue(sliderIsOpen);
       Allure.step("Слайдер развернулся");
   }

   @AfterEach
   public void closeBrowser() {
       Selenide.closeWebDriver();
   }
}
