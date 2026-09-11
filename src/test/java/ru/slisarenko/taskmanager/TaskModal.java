package ru.slisarenko.taskmanager;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class TaskModal extends BasePage{
    private final SelenideElement overlay = $("[data-testid='task-modal-overlay']");
    private final SelenideElement windowModalTask = $("[data-testid='task-modal']");
    private final SelenideElement titleModalTask = $("[data-testid='task-modal-title']");
    private final SelenideElement closeBtn = $("[data-testid='task-modal-close']");
    private final SelenideElement titleTask = $("[data-testid='task-title-input']");
    private final SelenideElement descriptionTitle = $("[data-testid='task-description-input']");
    private final SelenideElement selectPriority = $("[data-testid='task-priority-select']");
    private final SelenideElement selectStatus = $("[data-testid='task--status-select']");
    private final SelenideElement dueDate = $("[data-testid='task-due-input']");
    private final SelenideElement saveBtn = $("[data-testid='task-save-btn']");
    private final SelenideElement cancelBtn = $("[data-testid='task-cancel-btn']");
    private final SelenideElement comments = $("[data-testid='task-comments-section']");
    private final SelenideElement comment = $("[data-testid='comment-input']");
    private final SelenideElement submitCommitBtn = $("[data-testid='commit-submit-btn']");
    private final SelenideElement messageError = $("[data-testid='task-form-error']");
}
