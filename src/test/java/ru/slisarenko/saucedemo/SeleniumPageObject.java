package ru.slisarenko.saucedemo;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumPageObject {
    private WebDriver driver;
    private WebDriverWait waitElement;
    private Actions actions;

    private WebElement username;
    private WebElement password;
    private WebElement login;
    private WebElement login_credentials;
    private WebElement login_password;
    private WebElement error;
    private WebElement firstProduct;
    private WebElement cart;

    public SeleniumPageObject() {
        this.driver = new ChromeDriver();
        waitElement = new WebDriverWait(driver, Duration.ofSeconds(5));
        actions = new Actions(driver);

        driver.get("https://www.saucedemo.com/");
        username = waitClickable(By.cssSelector("[data-test='username']"));
        password = waitClickable(By.cssSelector("[data-test='password']"));
        login = waitClickable(By.cssSelector("[data-test='login-button']"));
        login_credentials = waitClickable(By.cssSelector("[data-test='login-credentials']"));
        login_password = waitClickable(By.xpath("//*[@data-test='login-password']"));


    }

    public String getUrl() {
        return driver.getCurrentUrl();
    }

    public Map<String,String> getLoginCredentials() {
        Map<String,String> credentials = new HashMap<>();
        String credLogin = login_credentials.getText().split("\n")[1];
        credentials.put("username", credLogin);
        String credPassword = login_password.getText().split("\n")[1];
        credentials.put("password", credPassword);
        return credentials;
    }



    public SeleniumPageObject clickLogin(Map<String,String> credMap) {
        username.sendKeys(credMap.get("username"));
        password.sendKeys(credMap.get("password"));
        login.click();
        return this;
    }

    public String getCartState() {
        cart = waitClickable(By.cssSelector("[data-test='shopping-cart-link']"));
        return cart.getAttribute("aria-label");
    }

    public SeleniumPageObject clickCart() {
        waitClickable(By.cssSelector("[data-test='shopping-cart-link']")).click();
        return this;
    }

    public String getErrorMessage(){
        error = waitClickable(By.xpath("//*[@data-test='error']"));
        return error.getText();
    }

    public SeleniumPageObject clickAddProduct() {
        firstProduct = waitClickable(By.cssSelector("[data-test='inventory-list'] [data-test='inventory-item']:nth-child(1)"));
        firstProduct.findElement(By.xpath("//*[contains(@data-test,'add-to-cart')]")).click();
        return this;
    }

    public List<String> getNameEndPrice() {
        List<String> nameEndPrice = new ArrayList<>();
        firstProduct = waitClickable(By.cssSelector("[data-test='inventory-list'] [data-test='inventory-item']:nth-child(1)"));
        nameEndPrice.add(firstProduct.findElement(By.cssSelector("[data-test='inventory-item-name']")).getText());
        nameEndPrice.add(firstProduct.findElement(By.cssSelector("[data-test='inventory-item-price']")).getText());
        return nameEndPrice;
    }



    public List<String> getNameAndPriceInCart(){
        List<String> nameEndPrice = new ArrayList<>();
        nameEndPrice.add(waitClickable(By.cssSelector("[data-test='inventory-item-name']")).getText());
        nameEndPrice.add(waitClickable(By.cssSelector("[data-test='inventory-item-price']")).getText());
        return nameEndPrice;
    }

    public String getCountProductInCart() {
        WebElement counter = waitClickable(By.xpath("//*[@data-test='shopping-cart-badge']"));
        return counter.getText();
    }

    public String getStringButton() {
        firstProduct = waitClickable(By.cssSelector("[data-test='inventory-list'] [data-test='inventory-item']:nth-child(1)"));
        WebElement button = firstProduct.findElement(By.cssSelector("[data-test='inventory-item-price'] + button"));
        return button.getText();
    }

    public SeleniumPageObject clickSortPriceAsc() {
        By selector = By.cssSelector("[data-test='product-sort-container']");
        Select dropdown = new Select(waitClickable(selector));
        dropdown.selectByValue("lohi");
        return this;
    }

    public List<String> getPrices(){
        List<String> prices = new ArrayList<>();
        By locator = By.cssSelector("[data-test='inventory-list'] [data-test='inventory-item-price']");
        List<WebElement> elements = driver.findElements(locator);
        elements.forEach(element -> prices.add(element.getText()));
        return prices;
    }



    public void quit(){
        driver.quit();
    }

    public WebElement waitClickable(By locator) {
        return waitElement.until(ExpectedConditions.elementToBeClickable(locator));
    }
}
