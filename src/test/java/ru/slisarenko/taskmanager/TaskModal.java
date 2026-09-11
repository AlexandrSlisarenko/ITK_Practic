package ru.slisarenko.taskmanager;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class TaskModal extends BasePage{
    private final SelenideElement overlay = $("[data-testid='task-modal-overlay']");
    private final SelenideElement windowModalTask = $("[data-testid='task-modal']");
    private final SelenideElement titleModalTask = $("[data-testid='task-modal-title']");
    private final SelenideElement closeBtn = $("[data-testid='task-modal-close']");
    private final SelenideElement titleTask = $("[data-testid='task-title-input']");
    private final SelenideElement descriptionTask = $("[data-testid='task-description-input']");
    private final SelenideElement priorityTask = $("[data-testid='task-priority-select']");
    private final SelenideElement selectStatus = $("[data-testid='task--status-select']");
    private final SelenideElement dueDate = $("[data-testid='task-due-input']");
    private final SelenideElement saveBtn = $("[data-testid='task-save-btn']");
    private final SelenideElement cancelBtn = $("[data-testid='task-cancel-btn']");
    private final ElementsCollection comments = $$("[data-testid='task-comments-section'] [data-testid^='comment-']");
    private final SelenideElement comment = $("[data-testid='comment-input']");
    private final SelenideElement submitCommitBtn = $("[data-testid='commit-submit-btn']");
    private final SelenideElement messageError = $("[data-testid='task-form-error']");


    public void setTitle(String title) {
        titleTask.setValue(title);
    }

    public void setDescription(String description) {
        descriptionTask.setValue(description);
    }

    public void setPriority(String priority) {
        priorityTask.selectOptionContainingText(priority);
    }

    public void setStatus(String status) {
        priorityTask.selectOptionContainingText(status);
    }

    public void setDueDate(String due_Date) {
        dueDate.setValue(due_Date);
    }

    public void save(){
        saveBtn.click();
    }

    public void cancel(){
        cancelBtn.click();
    }

    public void addComment(String comment) {
        this.comment.setValue(comment);
        submitCommitBtn.click();
    }

    public SelenideElement getCommentByText(String text) {
       return comments.stream()
                .filter(comment -> comment.getText().equals(text))
                .findFirst().orElseThrow(()-> new RuntimeException("Comment not found"));
    }

    public String getErrorMessage() {
        return messageError.getText();
    }
}


