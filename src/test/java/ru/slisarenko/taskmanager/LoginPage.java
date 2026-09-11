package ru.slisarenko.taskmanager;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends BasePage{

    private final SelenideElement divLoginPage = $("div[data-testid='login-page']");
    private final SelenideElement inputLoginEmail = $("input[data-testid='login-email-input']");
    private final SelenideElement inputLoginPassword = $("input[data-testid='login-password-input']");
    private final SelenideElement buttonLoginSubmit = $("button[data-testid='login-submit-btn']");
    private final SelenideElement LoginError = $("[data-testid='login-error']");

}
