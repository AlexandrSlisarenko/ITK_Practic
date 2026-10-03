package ru.slisarenko.saucedemo;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

public class LoginTest {
    private SeleniumPageObject pageSelenium;
    private SelenidePageObject pageSelenide;
    private static Map<String, String> loginData;

    @BeforeAll
    public static void beforeAll() {
        loginData = new HashMap<>();
        loginData.put("username","standard_user");
        loginData.put("password","secret_sauce");
    }

    @BeforeEach
    public void initPage() {
        this.pageSelenium = new SeleniumPageObject();
        Configuration.browser = "chrome";
        Configuration.pollingInterval = 5000;
        Configuration.baseUrl = "https://www.saucedemo.com";
        Configuration.reportsFolder = "target/selenide-reports";
        Configuration.timeout = 20000;
        this.pageSelenide = open("/", SelenidePageObject.class);
    }

    @Test
    public void loginSeleniumTest() {
        String expectedUrl = pageSelenium.getUrl();
        Map<String, String> credsMap = pageSelenium.getLoginCredentials();
        String actualUrl = pageSelenium.clickLogin(credsMap).getUrl();

        Assertions.assertNotEquals(expectedUrl, actualUrl);
    }

    @Test
    public void loginSelenideTest() {
        Map<String,String> credentials = pageSelenide.getLoginCredentials();
        String startUrl = com.codeborne.selenide.WebDriverRunner.url();
        Selenide.closeWebDriver();
        open("/",SelenidePageObject.class).clickLogin(credentials);
        String actualUrl = com.codeborne.selenide.WebDriverRunner.url();

        Assertions.assertNotEquals(startUrl, actualUrl);
    }

    @Test
    public void checkErrorLoginTest() {
        Map<String,String> credentials = new HashMap<>();
        credentials.put("username","standard_user");
        credentials.put("password","wrong_password");

        String expectedMessage = "Username and password do not match";

        String actualMessage = pageSelenium.clickLogin(credentials).getErrorMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void checkLoginLockedUserTest() {
        Map<String,String> credentials = new HashMap<>();
        credentials.put("username","locked_out_user");
        credentials.put("password","secret_sauce");

        String expectedMessage = "Sorry, this user has been locked out.";

        String actualMessage = pageSelenium.clickLogin(credentials).getErrorMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void checkAddProductToCartTest() {
        String cartState = pageSelenium.clickLogin(loginData).getCartState();
        String buttonText = pageSelenium.getStringButton();

        Assertions.assertEquals("Cart, empty", cartState);
        Assertions.assertEquals("Add to cart", buttonText);

        String count = pageSelenium.clickAddProduct().getCountProductInCart();

        buttonText = pageSelenium.getStringButton();

        Assertions.assertEquals("1", count);
        Assertions.assertEquals("Remove",buttonText);

    }

    @Test
    public void checkProductInCartTest(){
        String cartState = pageSelenium.clickLogin(loginData).getCartState();
        String buttonText = pageSelenium.getStringButton();
        List<String> nameEndPrice = pageSelenium.getNameEndPrice();

        Assertions.assertEquals("Cart, empty", cartState);
        Assertions.assertEquals("Add to cart", buttonText);

        List<String> nameEndPriceInCart = pageSelenium.clickAddProduct().clickCart().getNameAndPriceInCart();

        Assertions.assertEquals(nameEndPrice.get(0), nameEndPriceInCart.get(0));
        Assertions.assertEquals(nameEndPrice.get(1), nameEndPriceInCart.get(1));

    }

    @Test
    public void checkOrderProductByPriceAscTest() {
        List<String> prices = pageSelenium.clickLogin(loginData)
                .clickSortPriceAsc()
                .getPrices();
        List<Double> original = toIntArray(prices);
        List<Double> sorted = original.stream().sorted().collect(Collectors.toList());

        Assertions.assertEquals(original, sorted);

    }


    @Test
    public void loginErrorSelenideTest() {
        Map<String,String> credentials = new HashMap<>();
        credentials.put("username","standard_user");
        credentials.put("password","secret_sauce123");
        String expectedMessage = "Username and password do not match";

        String actualMessage = pageSelenide.clickLogin(credentials)
                .getTextError();

        Assertions.assertNotEquals(expectedMessage, actualMessage);
    }

    @Test
    public void loginLockedSelenideTest() {
        Map<String,String> credentials = new HashMap<>();
        credentials.put("username","locked_out_user");
        credentials.put("password","secret_sauce");
        String expectedMessage = "Username and password do not match";

        String actualMessage = pageSelenide.clickLogin(credentials)
                .getTextError();

        Assertions.assertNotEquals(expectedMessage, actualMessage);
    }

    @Test
    public void addFirstProductToCartTest() {
        int productInOrder = 3;
        String expectedEmptyCartText = "Cart, empty";

        String actualCartText = pageSelenide.clickLogin(loginData)
                .getCartAriaLabelText();

        Assertions.assertEquals(expectedEmptyCartText, actualCartText);

        actualCartText = pageSelenide.clickAddToCart(productInOrder)
                .getCartAriaLabelText()
                .split(" ")[1];
        String expectedTitleButton = "Remove";
        String actualTitleButton = pageSelenide.titleButton(productInOrder);

        Assertions.assertEquals(expectedTitleButton, actualTitleButton);
        Assertions.assertEquals("1", actualCartText);
    }

    @Test
    public void checkProductInCartNameAndPriceTest() {
        int productInOrder = 3;
        String expectedEmptyCartText = "Cart, empty";

        String actualCartText = pageSelenide.clickLogin(loginData)
                .getCartAriaLabelText();

        Assertions.assertEquals(expectedEmptyCartText, actualCartText);

        actualCartText = pageSelenide.clickAddToCart(productInOrder)
                .getCartAriaLabelText()
                .split(" ")[1];
        List<String> expectedNameAndPrice = pageSelenide.getNameAndPriceProduct(productInOrder);

        Assertions.assertEquals("1", actualCartText);

        List<String> actualNameAndPrice = pageSelenide.clickCart().checkExistsProductInCart(expectedNameAndPrice);

        Assertions.assertEquals(expectedNameAndPrice.get(0), actualNameAndPrice.get(0));
        Assertions.assertEquals(expectedNameAndPrice.get(1), actualNameAndPrice.get(1));

    }

    @Test
    public void checkOrderProductByPriceAscSelenideTest() {
       List<String> prices = pageSelenide.clickLogin(loginData)
                .clickSortPriceAsc()
               .getPriceProduct();
       List<Double> original = toIntArray(prices);
       List<Double> sorted = original.stream().sorted().collect(Collectors.toList());

       Assertions.assertEquals(original, sorted);

    }



    @AfterEach
    public void pageQuit() {
        pageSelenium.quit();

    }



    private List<Double> toIntArray(List<String> list) {
        List<Double> intList = new ArrayList<>();
        list.forEach(item -> {
            String temp = item.substring(1);
            intList.add(Double.parseDouble(temp));
        });
        return intList;
    }


}
