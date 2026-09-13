package ru.slisarenko.taskmanager;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.page;

public class HomePage extends BasePage{


    private final String boardKanbanSelector = "[data-testid='kanban-board'] ";
    private final String columnTodoSelector = "[data-testid='kanban-column-TODO'] ";
    private final String cardsSelector = "[data-testid^='task-card']";
    private final String columnInProgressSelector = "[data-testid='kanban-column-IN_PROGRESS'] ";
    private final String columnReviewSelector = "[data-testid='kanban-column-REVIEW'] ";
    private final String columnDoneSelector = "[data-testid='kanban-column-DONE'] ";


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
    private final SelenideElement boardKanban = $(boardKanbanSelector);
    private final ElementsCollection cardsTodo = $$(boardKanbanSelector + columnTodoSelector + cardsSelector);
    private final ElementsCollection cardsInProgress = $$(boardKanbanSelector + columnInProgressSelector + cardsSelector);
    private final ElementsCollection cardsReview = $$(boardKanbanSelector + columnReviewSelector + cardsSelector);
    private final ElementsCollection cardsDone = $$(boardKanbanSelector + columnDoneSelector + cardsSelector);
    private final ElementsCollection cardsBoardKanban = boardKanban.$$("[data-testid^='task-card']");


    public HomePage waitForBoardLoaded(){
        boardPage.shouldBe(visible);
        return this;
    }

    public String getUserName(){
        return userName.getText();
    }

    public LoginPage logout(){
        logout.click();
        return page(LoginPage.class);
    }

    public LoginPage loginPage(){
        return page(LoginPage.class);
    }

    public void createTask(String taskName, String priority, String dueDate){
        openTaskModal()
                .setTitle(taskName)
                .setPriority(priority)
                .setDueDate(dueDate)
                .save();
    }

    public TaskModalForm openTaskByTitle(String title){
        cardsBoardKanban.stream()
                .filter(element -> element.$("[data-testid='task-title']").getText().equals(title))
                .findFirst().orElseThrow(()->new RuntimeException("Task not found"))
                .click();
        return page(TaskModalForm.class);
    }

    public SelenideElement getTaskCardByTitle(String title){
        return cardsBoardKanban.stream()
                .filter(element -> element.$("[data-testid='task-title']").getText().equals(title))
                .findFirst().orElseThrow(()->new RuntimeException("Task not found"));
    }

    public SelenideElement getColumn(String status){
        return boardKanban.$("[data-testid$='" + status + "']");
    }

    public String getStatValue(String statTestId){
        return statsBar.$("[data-testid$='stat-" + statTestId + "']")
                .$("strong")
                .getText();
    }

    public String getFirstTitleCardInColumn(String columnName){
        return switch (columnName){
            case "TODO" -> cardsTodo.first().$("[data-testid='task-title']").getText();
            case "DONE" -> cardsDone.first().$("[data-testid='task-title']").getText();
            case "REVIEW" -> cardsReview.first().$("[data-testid='task-title']").getText();
            case "IN_PROGRESS" -> cardsInProgress.first().$("[data-testid='task-title']").getText();
            default -> throw new IllegalStateException("Unexpected value: " + columnName);
        };
    }

    public boolean existsCardWithTitleInColumnTODO(String title){
        return existsCardInCollect(cardsTodo, title);
    }

    public boolean existsCardWithTitleInColumnIN_PROGRESS(String title){
        return existsCardInCollect(cardsInProgress, title);
    }

    private boolean existsCardInCollect(ElementsCollection collect, String title){
        return collect.stream()
                .anyMatch(element -> element.$("[data-testid='task-title']").getText().equals(title));
    }

    private TaskModalForm openTaskModal(){
        taskCreate.click();
        return page(TaskModalForm.class);
    }
}
