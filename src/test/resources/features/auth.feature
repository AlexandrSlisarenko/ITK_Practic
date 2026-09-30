Feature: Авторизация
  @api @smoke
  Scenario: Успешная авторизация
    Given Создаем запрос на авторизацию для "ADMIN"
    When Отправляем запрос по адресу "https://demo.itklabs.online/api/v1/auth/login"
    Then Проверка статуса ответа