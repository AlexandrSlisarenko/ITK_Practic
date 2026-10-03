package ru.slisarenko.cucumber.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class FirstSteps {
    @Given("система запущена")
    public void systemIsRunning() {
        System.out.println("systemIsRunning");
    }
    @When("я проверяю статус")
    public void checkStatus() {
        System.out.println("checkStatus");
    }
    @Then("статус 200 OK")
    public void statusOk() {
        System.out.println("statusOk");
    }
}
