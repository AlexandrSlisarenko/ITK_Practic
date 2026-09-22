package ru.slisarenko.studyhub;

import com.codeborne.selenide.SelenideElement;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class SeleniumStudyHubStudentPage {

    private WebDriver driver;
    private WebDriverWait waitElement;

    private By loginPage;
    private By inputLogin;
    private By inputPassword;
    private By buttonSubmit;
    private By coursPercent;

    public SeleniumStudyHubStudentPage() {
        this.driver = new ChromeDriver();
        this.waitElement = new WebDriverWait(this.driver, Duration.ofSeconds(4));
        this.driver.get("https://academy.siamsoftware.tech");
        this.loginPage = By.cssSelector("#root .login-page");
        this.inputLogin = By.cssSelector("#root .login-form input[placeholder='Ваш логин']");
        this.inputPassword = By.cssSelector("#root .login-form input[type='password']");
        this.buttonSubmit = By.cssSelector("#root .login-form button[type='submit']");
        this.coursPercent = By.xpath("//section[@id='student-courses']//span[substring-before(@class,'__progress')]");
    }

    public SeleniumStudyHubStudentPage loginToPlatform() {
        WebElement loginInput = waitElement.until(
                ExpectedConditions.visibilityOfElementLocated(loginPage)
        );
        WebElement inputLogin = driver.findElement(this.inputLogin);
        inputLogin.sendKeys("alexander_slisarenko");
        WebElement inputPassword = driver.findElement(this.inputPassword);
        inputPassword.sendKeys("gH5$mK9@rT2#vN7&");
        WebElement buttonSubmit = driver.findElement(this.buttonSubmit);
        buttonSubmit.click();
        waitElement.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".main-content--student"))
        );
        return this;
    }

    public SeleniumStudyHubStudentPage openCourseByPercent(String percent) {
        WebElement coursePercent = driver.findElement(this.coursPercent);
        WebElement course = coursePercent.findElement(By.xpath("/span[contains(text(),'" + percent + "')]"));
        course.findElement(By.xpath("/./following::span[substring-before(@class,'__cta')][1]")).click();

        return this;
    }

    public void quit() {
        driver.quit();
    }

    public String getTitle() {

        return driver.getTitle();
    }


}
