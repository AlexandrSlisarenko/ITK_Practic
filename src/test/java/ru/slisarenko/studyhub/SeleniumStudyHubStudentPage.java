package ru.slisarenko.studyhub;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumStudyHubStudentPage {

    private WebDriver driver;
    private WebDriverWait waitElement;
    private JavascriptExecutor js;
    private Actions actions;

    private By loginPage;
    private By inputLogin;
    private By inputPassword;
    private By buttonSubmit;
    private By coursPercent;


    public SeleniumStudyHubStudentPage() {
        this.driver = new ChromeDriver();
        this.waitElement = new WebDriverWait(this.driver, Duration.ofSeconds(10));
        this.driver.get("https://academy.siamsoftware.tech");

        this.js = (JavascriptExecutor) driver;
        actions = new Actions(driver);

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

    public SeleniumStudyHubCoursePage openCourseByPercent(String percent) {
        WebElement coursePercent = waitElement.until(
                ExpectedConditions.visibilityOfElementLocated(this.coursPercent)
        );
        WebElement course = coursePercent.findElement(By.xpath("//span[contains(text(),'" + percent + "')]"));
        course.findElement(By.xpath("following::span[substring-before(@class,'__cta')][1]")).click();

        return new SeleniumStudyHubCoursePage(driver);
    }

    public void quit() {
        driver.quit();
    }

    public String getTitle() {

        return driver.getTitle();
    }


    public SeleniumStudyHubCoursePage openCourseByCourseName(String nameCourse) {

        WebElement course = getElementWithWait(By.xpath("//a[contains(@aria-label,'" + nameCourse + "')]"));
        course.click();
        return new SeleniumStudyHubCoursePage(driver);
    }

    public SeleniumStudyHubCoursePage openCourseByCourseNameCssSelector(String nameCourse) {
        WebElement doneCourse = getElementWithWait(By.cssSelector(".course-done__summary"));
        scrollToElement(doneCourse);
        //js.executeScript("arguments[0].click();", doneCourse);
        doneCourse.sendKeys(Keys.ENTER);
        //actions.sendKeys(doneCourse, Keys.ENTER).perform();
        js.executeScript("arguments[0].setAttribute('open','');", doneCourse);
        String result = doneCourse.getAttribute("open");
        WebElement course = getClickableElement(By.cssSelector("a[aria-label*='"+nameCourse+"']"));
        course.click();
        return new SeleniumStudyHubCoursePage(driver);
    }

    public int getCountCoursesInPageOnSecondPage() {
        int countCourses = 0;
        WebElement button = getElementWithWait(By.cssSelector(".pagination__pages-nav li:last-child > button"));
        scrollToElement(button);
        button.sendKeys(Keys.ENTER);
        WebElement catalog = getElementWithWait(By.xpath("//div[@class='student-catalog']"));
        countCourses = catalog.findElements(By.xpath("//div[@class='student-catalog']//div[@class='course-list']/article")).size();
        return countCourses;
    }

    private WebElement getElementWithWait(By locator) {
        return waitElement.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    private WebElement getClickableElement(By locator) {
        return waitElement.until(ExpectedConditions.elementToBeClickable(locator));
    }

    private void scrollToElement(WebElement element) {
        actions.scrollToElement(element).perform();
    }
}
