package ru.slisarenko.taskmanager;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class LoginPage {

    private final SelenideElement divLoginPage = $("div[data-testid='login-page']");
    private final SelenideElement inputLoginEmail = $("input[data-testid='login-email-input']");
    private final SelenideElement inputLoginPassword = $("input[data-testid='login-password-input']");
    private final SelenideElement buttonLoginSubmit = $("button[data-testid='login-submit-btn']");
    private final SelenideElement loginError = $("[data-testid='login-error']");


    public LoginPage open(){
        Selenide.open("/login");
        return this;
    }

    public LoginPage login(String email, String password){
        inputLoginEmail.setValue(email);
        inputLoginPassword.setValue(password);
        buttonLoginSubmit.click();
        return this;
    }

    public HomePage loadHomePage(){
        return page(HomePage.class);
    }

    public String getErrorMessage(){
        loginError.shouldBe(visible);
        return loginError.getText();
    }

    public boolean isOnLoginPage(){
        divLoginPage.shouldBe(visible);
        return true;
    }
}
