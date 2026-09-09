package ru.slisarenko;

import java.sql.BatchUpdateException;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class MainTest {
    WebDriver driver = new ChromeDriver();
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

   /* @Test
    public void startSeleniumEnterToPlatform(){
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(10));
        driver.get("https://academy.siamsoftware.tech/login");
        WebElement loginInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#root .login-page"))
        );
        WebElement input = driver.findElement(By.cssSelector("#root .login-form input[placeholder='Ваш логин']"));
        input.sendKeys("");

        WebElement input1 = driver.findElement(By.cssSelector("#root .login-form input[type='password']"));
        input1.sendKeys("");

        WebElement input2 = driver.findElement(By.cssSelector("#root .login-form button[type='submit']"));
        input2.click();
        driver.quit();
    }

    @Test
    public void checkChrome(){
        String startPath = "form[action='/search']";
        driver.get("https://www.google.com/");
        WebElement form = driver.findElement(By.cssSelector(startPath));

        WebElement textarea = driver.findElement(By.cssSelector(startPath + " textarea"));
        textarea.sendKeys("SELENIUM JAVA");
        //textarea.sendKeys(Keys.ENTER);
        WebElement buttonSearchII = textarea.findElement(By.xpath("./../following-sibling::div[1]/button"));
        //buttonSearch.click();

        WebElement luckButton = form.findElement(By.cssSelector("input[name='btnK']"));

        WebElement logo = driver.findElement(By.cssSelector("svg[aria-label='Google'][role='img']"));

        WebElement searchButton = form.findElement(By.cssSelector("input[name='btnI']"));

        WebElement aboutGoogle = driver.findElement(By.xpath("//a[text()='Всё о Google']"));

        WebElement reklama = driver.findElement(By.xpath("//a[contains(@href, '/howsearchworks/')]"));

        driver.quit();
    }*/
    
    
}
