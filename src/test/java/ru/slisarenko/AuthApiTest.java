package ru.slisarenko;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;




@Epic("Task Manager")
@Feature("Аутентификация")
public class AuthApiTest {



    @Test
    @Story("Успешный логин")
    @Severity(SeverityLevel.CRITICAL)
    public void login_withValidCredentials_returnsToken() {
        // тест
    }
    @Test
    @Story("Доступ без токена")
    public void getMe_withoutToken_returns401() {
        // тест
    }

    @Step("Авторизация под пользователем {email}")
    public String login(String email, String password) {
        // логика получения токена
        return "token";
    }

    @Test
    public void testTaskLifecycle() {
        Allure.step("Создание задачи", () -> {
            System.out.println("код создания");
        });
        Allure.step("Проверка статуса", () -> {
            System.out.println("код создания");
        });
    }

    @Step("Создать задачу с именем {title}")
    public void createTask(String title) {
        Allure.step("Отправить POST-запрос", () -> {
            System.out.println("SEND");
        });
        Allure.step("Проверить статус 201", () -> {
            System.out.println("OK");
        });
    }

    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("TC-005")
    @Story("Создание задачи в проекте DEMO")
    @Test
    public void createTask_inDemo_returns201() {
        Allure.step("Подготовка данных", () -> {
            // создание DTO
        });
        Allure.step("Отправка POST /api/v1/tasks", () -> {
            // выполнение запроса
        });
        Allure.step("Проверка ответа", () -> {
            // проверки
        });
    }

    @Test
    public void testWithAttachment() {
        String requestBody = "{\"name\":\"Test\"}";

        Allure.step("Сохраняем тело запроса как текстовое вложение", () -> {
            Allure.attachment("Запрос", requestBody);
        });
        //


        // Отправляем запрос...
        Allure.step("Затем прикрепляем ответ", ()->{
            String responseBody = "{\"id\":1,\"name\":\"Test\"}";
            Allure.attachment("Ответ", responseBody);
        });

    }

    @Attachment(value = "Ответ сервера", type = "application/json")
    public String attachResponse(String response) {
        return response;
    }
    @Test
    public void testWithAnnotatedMethod() {
        String response = "given().when().get(/users/1).asString();";
        attachResponse(response); // вызов метода создаст вложение
    }


    @Test
    public void getUsers_withAllureLogging() {
        RequestSpecification spec = given()
                .filter(new AllureRestAssured())
                .baseUri("https://api.example.com");
        given()
                .filter(new AllureRestAssured())  // добавляем фильтр
                .when()
                .get("/users")
                .then()
                .statusCode(200);
    }
}
