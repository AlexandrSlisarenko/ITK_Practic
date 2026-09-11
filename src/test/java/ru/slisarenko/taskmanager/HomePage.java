package ru.slisarenko.taskmanager;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class HomePage extends BasePage{

    private final SelenideElement boardPage = $("[data-testid='board-page']");
    private final SelenideElement loadingScreen = $("[data-testid='loading-screen']");
    private final SelenideElement userName = $("[data-testid='user-name']");
    private final SelenideElement logout = $("[data-testid='logout-btn']");
    private final SelenideElement project = $("[data-testid='project-select']");
    private final SelenideElement statsBar = $("[data-testid='stats-bar']");
    private final SelenideElement statTotalTasks = statsBar.$("[data-testid='stat-total-tasks']");
    private final SelenideElement statOverdue = statsBar.$("[data-testid='stat-overdue']");
    private final SelenideElement statInProgress = statsBar.$("[data-testid='stat-in-progress']");
    private final SelenideElement statsDone = statsBar.$("[data-testid='stat-done']");
    private final SelenideElement taskCreate = $("[data-testid='task-create-btn']");
    private final SelenideElement boardKanban = $("[data-testid='kanban-board']");
    private final SelenideElement columnTodo = boardKanban.$("[data-testid='kanban-column-TODO']");
    private final ElementsCollection cardsTodo = columnTodo.$$("[data-testid^='task-card']");
    private final SelenideElement columnInProgress = boardKanban.$("[data-testid='kanban-column-IN_PROGRESS']");
    private final ElementsCollection cardsInProgress = columnInProgress.$$("[data-testid^='task-card']");
    private final SelenideElement columnReview = boardKanban.$("[data-testid='kanban-column-REWIEW']");
    private final ElementsCollection cardsReview = columnReview.$$("[data-testid^='task-card']");
    private final SelenideElement columnDone = boardKanban.$("[data-testid='kanban-column-DONE']");
    private final ElementsCollection cardsDone = columnDone.$$("[data-testid^='task-card']");

    public void waitForBoardLoaded(){
        boardPage.shouldBe(visible);
    }

    public String getUserName(){
        return userName.getText();
    }

    public void createTask(String taskName, String priority, String dueDate){

    }
}
