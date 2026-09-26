package ru.slisarenko.demoqa.text_box;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.slisarenko.demoqa.BaseSeleniumPageObject;

public class TextBoxSeleniumPageObject extends BaseSeleniumPageObject {
    private final static String userNameSelector = "#userName";
    private final static String userEmailSelector = "#userEmail";
    private final static String currentAddressSelector = "#currentAddress";
    private final static String permanentAddressSelector = "#permanentAddress";
    private final static String submitSelector = "#submit[type='button']";

    private By userName;
    private By userEmail;
    private By currentAddress;
    private By permanentAddress;
    private By submit;



    public TextBoxSeleniumPageObject() {
        super();
        WebDriver driver = getDriver();
        driver.get(getBaseUrl() + "/text-box");
        this.userName = By.cssSelector(userNameSelector);
        this.userEmail = By.cssSelector(userEmailSelector);
        this.currentAddress = By.cssSelector(currentAddressSelector);
        this.permanentAddress = By.cssSelector(permanentAddressSelector);
        this.submit = By.cssSelector(submitSelector);
    }


    /*public TextBoxSeleniumPageObject submitData() {
        WebElement userNameElement = getWebElementToBeClickable(userName);
        String userNamePlaceHolderText = userNameElement.getAttribute("placeholder");
        userName.sendKeys(userNamePlaceHolderText + " Selenium");
        String userEmailPlaceHolderText = userEmail.getAttribute("placeholder");
        userEmail.sendKeys(userEmailPlaceHolderText + " Selenium");
        String userAddressPlaceHolderText = currentAddress.getAttribute("placeholder");
        currentAddress.sendKeys(userAddressPlaceHolderText + " Selenium");
        String userPermanentAddressPlaceHolderText = permanentAddress.getAttribute("placeholder");
        permanentAddress.sendKeys(userPermanentAddressPlaceHolderText + " Selenium");
        submit.click();
        return this;
    }*/
}
