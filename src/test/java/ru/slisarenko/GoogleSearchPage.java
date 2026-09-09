package ru.slisarenko;

import java.time.Duration;
import java.util.Collections;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GoogleSearchPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private By formSearch;
    private By textAreaSearch;
    private By buttonSearchII;
    private By luckButton;
    private By searchButton;
    private By logo;
    private By aboutGoogle;
    private By reklama;
    private By howsearchworks;
    ;

    public GoogleSearchPage() {

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/152.0.0.0 Safari/537.36");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        this.driver.get("https://www.google.com");
        this.formSearch = By.cssSelector("form[action='/search']");
        this.textAreaSearch = By.cssSelector("form[action='/search'] textarea");
        this.buttonSearchII = By.xpath("./../following-sibling::div[1]/button");
        this.luckButton = By.xpath("//input[@name='btnI']");
        this.searchButton = By.cssSelector("input[name='btnK']");
        this.logo = By.cssSelector("svg[aria-label='Google']");
        this.aboutGoogle = By.xpath("//a[text()='Всё о Google']");
        this.reklama = By.xpath("//a[text()='Реклама']");
        this.howsearchworks = By.xpath("//a[contains(@href, '/howsearchworks/')]");

    }
    public void quit() {
        driver.quit();
    }

    public String getHeaderResultSearchText(String searchText) {
        WebElement textarea = driver.findElement(this.textAreaSearch);
        textarea.sendKeys(searchText);
        textarea.sendKeys(Keys.ENTER);
        return "";
    }

    public boolean isDisplayedLuckButton() {
        WebElement loginInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(luckButton)
        );
        WebElement button = driver.findElement(luckButton);
        return button.isDisplayed();
    }

    public boolean isDisplayedAboutGoogle() {
        return isDisplayedElement(aboutGoogle);
    }

    public boolean isDisplayedLogo() {
        return isDisplayedElement(logo);
    }

    public boolean isEnabledLogo() {
        return isEnabledElement(logo);
    }

    public boolean isEnabledLuckButton() {
        return isEnabledElement(luckButton);
    }

    public boolean isEnabledAboutGoogle() {
        return isEnabledElement(aboutGoogle);
    }

    public boolean checkHrefAboutGoogleNotExistsOrNotEmpty() {
        String href = getHref(aboutGoogle);
        if (href != null) {
            return !href.isEmpty();
        } else {
            return false;
        }
    }

    public boolean checkHrefAdvertising(String startHref){
        String href = getHref(reklama);
        if (href != null) {
            return href.startsWith(startHref);
        } else {
            return false;
        }
    }

    private boolean isEnabledElement(By locator) {
        return driver.findElement(locator).isEnabled();
    }

    private boolean isDisplayedElement(By locator) {
        return driver.findElement(locator).isDisplayed();
    }

    private String getHref(By locator) {
        return driver.findElement(locator).getAttribute("href");
    }





}
