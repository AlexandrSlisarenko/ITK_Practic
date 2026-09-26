package ru.slisarenko.demoqa;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BaseSeleniumPageObject {
    private WebDriver driver;
    private WebDriverWait waitElement;
    private Actions actions;
    private String baseUrl;


    public BaseSeleniumPageObject() {
        this.driver = new ChromeDriver();
        this.waitElement = new WebDriverWait(this.driver, Duration.ofSeconds(20));
        this.actions = new Actions(this.driver);
        this.baseUrl = "https://demoqa.com/";
    }

    public WebDriver getDriver() {
        return driver;
    }

    public WebDriverWait getWaitElement() {
        return waitElement;
    }

    public Actions getActions() {
        return actions;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void quitDriver() {
        driver.quit();
    }

    public WebElement getWebElementToBeClickable(By locator) {
        return waitElement.until(ExpectedConditions.elementToBeClickable(locator));
    }

}
