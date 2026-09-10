package ru.slisarenko.studyhub;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.element;
import static com.codeborne.selenide.Selenide.open;

public class StudyHubHomePage {

    private final SelenideElement loginPage = $("#root .login-page");
    private final SelenideElement inputLogin = $("#root .login-form input[placeholder='Ваш логин']");
    private final SelenideElement inputPassword = $("#root .login-form input[type='password']");
    private final SelenideElement buttonSubmit = $("#root .login-form button[type='submit']");
    private final SelenideElement buttonSubmitCookie = $(".btn.btn--accent");
    private final SelenideElement spanLogo = $(byText("SH"));
    private final SelenideElement buttonSidebarToggle = $(By.xpath(".//button[@class='sidebar__rail-toggle']"));
    private final SelenideElement divSidebarBrand = $("div[class$='brand']");
    private final SelenideElement divBrandText = $("div[class$='brand-text']");
    private final SelenideElement divSliderBody = $("div[class='sidebar__body']");
    private final ElementsCollection spanLinkText = $$("div[class='sidebar__body'] span.sidebar__link-text");
    private final ElementsCollection aLink = $$("div[class='sidebar__body'] a.sidebar__link");

    public StudyHubHomePage() {
        Configuration.browser = "chrome";
        Configuration.timeout = 20000;
        Configuration.pollingInterval = 1000;
        Configuration.reportsFolder = "target/selenide-reports";
        Configuration.baseUrl = "https://academy.siamsoftware.tech";
        //Configuration.holdBrowserOpen = true;
    }

    public boolean checkLogin() {
        open("/login");
        loginToPlatform();
        return true;
    }

    public void viewHomePage() {
        open("/");
        loginToPlatform();
    }

    public boolean isDisplayedLogo() {
        return spanLogo.isDisplayed();
    }

    public boolean isEnabledLogo() {
        return spanLogo.isEnabled();
    }

    public void clickSidebarToggleButton() {
        buttonSidebarToggle.click();
    }

    public boolean isDisplayedLinkTextAndBrandText(boolean titleMastBe) {
        divSidebarBrand.shouldBe(visible);
        divSliderBody.shouldBe(visible);

        boolean displayedBrandText = divBrandText.isDisplayed();

        boolean displayedLinkText = spanLinkText.stream()
                .allMatch(WebElement::isDisplayed);

        boolean existsTitle = isExistsTitle(titleMastBe);

        return displayedBrandText && displayedLinkText && !existsTitle;
    }

    private boolean isExistsTitle(boolean mastBe) {
        if (mastBe) {
            aLink.forEach(element -> element.shouldHave(attribute("title")));
            return true;
        } else {
            aLink.forEach(element -> element.shouldHave(attribute("title", "")));
            return false;
        }


    }


    private void loginToPlatform() {
        loginPage.should(visible);
        inputLogin.setValue("alexander_slisarenko");
        inputPassword.setValue("gH5$mK9@rT2#vN7&");
        buttonSubmit.click();
        buttonSubmitCookie.should(visible);
        buttonSubmitCookie.click();
    }
}
