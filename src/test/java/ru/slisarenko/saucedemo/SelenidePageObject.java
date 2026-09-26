package ru.slisarenko.saucedemo;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

public class SelenidePageObject {

    private String usernameSelector = "[data-test='username']";
    private String passwordSelector = "[data-test='password']";
    private String loginSelector = "[data-test='login-button']";
    private String login_credentialsSelector = "[data-test='login-credentials']";
    private String login_passwordSelector = "[data-test='login-password']";
    private String errorXpathSelector = "//*[@data-test='error']";
    private String cartXpathSelector = "//*[@data-test='shopping-cart-link']";
    private String selectXpathSelector = "//*[@data-test='product-sort-container']";
    private String pricesXpathSelector = "//*[@data-test='inventory-list']//*[@data-test='inventory-item-price']";

    private SelenideElement username;
    private SelenideElement password;
    private SelenideElement login;
    private SelenideElement login_credentials;
    private SelenideElement login_password;
    private SelenideElement error;
    private SelenideElement cart;
    private SelenideElement product;
    private SelenideElement select;
    private ElementsCollection pricesProduct;

    public SelenidePageObject() {
        username = $(usernameSelector);
        password = $(passwordSelector);
        login = $(loginSelector);
        login_credentials = $(login_credentialsSelector);
        login_password = $(login_passwordSelector);
        error = $x(errorXpathSelector);
        cart = $x(cartXpathSelector);
        select = $x(selectXpathSelector);
        pricesProduct = $$x(pricesXpathSelector);

    }

    public Map<String, String> getLoginCredentials() {

        Map<String, String> credentials = new HashMap<>();
        String credLogin = login_credentials.getText().split("\n")[1];
        credentials.put("username", credLogin);
        String credPassword = login_password.getText().split("\n")[1];
        credentials.put("password", credPassword);
        return credentials;
    }

    public SelenidePageObject clickLogin(Map<String, String> credMap) {
        username.sendKeys(credMap.get("username"));
        password.sendKeys(credMap.get("password"));
        login.click();
        return this;
    }

    public String getTextError() {
        return this.error.getText();
    }

    public String getCartAriaLabelText() {
        return this.cart.getAttribute("aria-label");
    }
    public SelenidePageObject clickCart() {
        this.cart.click();
        return this;
    }

    public SelenidePageObject clickAddToCart(int inOrder) {
        initProduct(inOrder)
                .find(By.xpath(".//*[starts-with(@data-test,'add-to-cart')]"))
                .click();
        return this;
    }

    public String titleButton(int inOrder){
        return initProduct(inOrder)
                .find(By.xpath(".//*[starts-with(@data-test,'remove')]"))
                .text();
    }

    public List<String> getNameAndPriceProduct(int inOrder) {
        List<String> nameAndPrice = new ArrayList<>();
        product = initProduct(inOrder);
        nameAndPrice.add(product.find(By.xpath(".//*[contains(@data-test,'item-name')]")).text());
        nameAndPrice.add(product.find(By.xpath(".//*[contains(@data-test,'price')]")).text());
        return nameAndPrice;
    }

    public List<String> checkExistsProductInCart(List<String> nameAndPrice) {
        List<String> nameAndPriceInCart = new ArrayList<>();
        String nameSelector = String.format("//*[text()='%s']", nameAndPrice.get(0));
        nameAndPriceInCart.add($x(nameSelector).getText());
        String priceSelector = "//*[text()='$']";
        nameAndPriceInCart.add($x(priceSelector).getText());
        return nameAndPriceInCart;
    }

    public SelenidePageObject clickSortPriceAsc() {
        select.selectOptionByValue("lohi");
        return this;
    }

    public List<String> getPriceProduct() {
        List<String> prices = new ArrayList<>();
        pricesProduct.stream().forEach(price -> prices.add(price.getText()));
        return prices;
    }

    private SelenideElement initProduct(int inOrder) {
        return product = $x(String.format("//*[@data-test='inventory-item'][%s]", inOrder));
    }
}
