package ru.slisarenko.taskmanager;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.page;

public class TaskModalForm {
    private final SelenideElement overlay = $("[data-testid='task-modal-overlay']");
    private final SelenideElement windowModalTask = $("[data-testid='task-modal']");
    private final SelenideElement titleModalTask = $("[data-testid='task-modal-title']");
    private final SelenideElement closeBtn = $("[data-testid='task-modal-close']");
    private final SelenideElement titleTask = $("[data-testid='task-title-input']");
    private final SelenideElement descriptionTask = $("[data-testid='task-description-input']");
    private final SelenideElement priorityTask = $("[data-testid='task-priority-select']");
    private final SelenideElement selectStatus = $("[data-testid='task-status-select']");
    private final SelenideElement dueDate = $("[data-testid='task-due-input']");
    private final SelenideElement saveBtn = $("[data-testid='task-save-btn']");
    private final SelenideElement cancelBtn = $("[data-testid='task-cancel-btn']");
    private final ElementsCollection comments = $$("[data-testid='task-comments-section'] [class=comments-list] [data-testid^='comment-']");
    private final SelenideElement comment = $("[data-testid='comment-input']");
    private final SelenideElement submitCommitBtn = $("[data-testid='comment-submit-btn']");
    private final SelenideElement messageError = $("[data-testid='task-form-error']");


    public TaskModalForm setTitle(String title) {
        titleTask.setValue(title);
        return this;
    }

    public TaskModalForm setDescription(String description) {
        descriptionTask.setValue(description);
        return this;
    }

    public TaskModalForm setPriority(String priority) {
        priorityTask.selectOptionContainingText(priority);
        return this;
    }

    public TaskModalForm setStatus(String status) {
        selectStatus.selectOptionByValue(status);
        return this;
    }

    public TaskModalForm setDueDate(String due_Date) {
        dueDate.setValue(due_Date);
        return this;
    }

    public void save() {
        saveBtn.click();
    }

    public HomePage cancel() {
        cancelBtn.click();
        return page(HomePage.class);
    }

    public TaskModalForm addComment(String comment) {
        this.comment.setValue(comment);
        submitCommitBtn.click();
        return this;
    }

    public boolean existsCommentByText(String text) {
        comments.shouldBe(CollectionCondition.sizeGreaterThan(comments.size()));
        return comments.stream()
                .anyMatch(comment -> comment.$(" span").getText().equals(text));
    }

    public String getErrorMessage() {
        return messageError.getText();
    }



}


