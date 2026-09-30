Feature: Smoke проверка
  Scenario: Система доступна
    Given система запущена
    When я проверяю статус
    Then статус 200 OK