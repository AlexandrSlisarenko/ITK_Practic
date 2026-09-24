package ru.slisarenko.studyhub;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumStudyHubCoursePage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By navigation;

    public SeleniumStudyHubCoursePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(4));
        this.navigation = By.className("breadcrumbs");
    }
    public SeleniumStudyHubCoursePage loadPage (){
        wait.until( ExpectedConditions.visibilityOfElementLocated(navigation));
        return this;
    }

    public String getBreadcrumbsActualCourse(){
        WebElement navbar = driver.findElement(navigation);
        return navbar.findElement(By.cssSelector("[class=breadcrumbs] li:nth-child(2) [class$=current]")).getText();
    }

    public SeleniumStudyHubCoursePage navigateToCourse(){
        lastLessons();
        return this;
    }

    private SeleniumStudyHubCoursePage lastLessons(){
        WebElement navbar = getElementWithWait(By.cssSelector("[aria-label='Навигация по материалам'] "));
        navbar.findElement(By.cssSelector(":first-child'] ")).click();
        return this;
    }

    private WebElement getElementWithWait(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }


}
