package ru.slisarenko.taskmanager;


import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Task Manager Test")
@Feature("Проверка функционала работы с задачами в модальном окне")
public class TaskModalFormTest {

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        Configuration.browser = "chrome";
        Configuration.timeout = 20000;
        Configuration.pollingInterval = 1000;
        Configuration.reportsFolder = "target/selenide-reports";
        Configuration.baseUrl = "http://45.141.103.56:8090";
        Configuration.holdBrowserOpen = true;
    }

    @Test
    @DisplayName("Создание задачи")
    @Story("Создание задачи")
    public void createTaskTest() {
        String titleTest = "titleTest " + System.currentTimeMillis();
        String priorityTest = "HIGH";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String dateTest = LocalDate.now().plusDays(1).format(formatter);

        HomePage homePage = open("/", LoginPage.class)
                .login("qa@demo.com", "Demo123!")
                .loadHomePage()
                .waitForBoardLoaded();

        int totalTaskBefore = Integer.parseInt(homePage.getStatValue("total-tasks"));
        homePage.createTask(titleTest, priorityTest, dateTest);
        int totalTaskAfter = Integer.parseInt(homePage.getStatValue("total-tasks"));
        boolean existsNewTitleInColumnTODO = homePage.existsCardWithTitleInColumnTODO(titleTest);

        assertTrue(existsNewTitleInColumnTODO);
        assertEquals(1, totalTaskAfter - totalTaskBefore);
    }

    @Test
    @DisplayName("Перемещение задачи по статусу(TODO -> IN_PROGRESS)")
    @Story("Перемещение задачи по статусу(TODO -> IN_PROGRESS)")
    public void switchTaskStatusTest() {
        HomePage homePage = open("/", LoginPage.class)
                .login("qa@demo.com", "Demo123!")
                .loadHomePage()
                .waitForBoardLoaded();

        String testTitle = homePage.getFirstTitleCardInColumn("TODO");
        TaskModalForm taskModalForm = homePage.openTaskByTitle(testTitle);
        taskModalForm.setStatus("IN_PROGRESS").save();

        boolean existsTaskInColumnInProgress = homePage.existsCardWithTitleInColumnIN_PROGRESS(testTitle);
        boolean existsTaskInColumnTODO = homePage.existsCardWithTitleInColumnTODO(testTitle);

        assertTrue(existsTaskInColumnInProgress);
        assertFalse(existsTaskInColumnTODO);

    }

    @Test
    @DisplayName("Добавление комментария к задаче")
    @Story("Добавление комментария к задаче")
    public void addCommentInTaskTest() {

        String titleComment = "Test comment { " + System.currentTimeMillis() + " }";

        HomePage homePage = open("/", LoginPage.class)
                .login("qa@demo.com", "Demo123!")
                .loadHomePage()
                .waitForBoardLoaded();

        String testTitle = homePage.getFirstTitleCardInColumn("IN_PROGRESS");
        TaskModalForm taskModalForm = homePage.openTaskByTitle(testTitle);
        taskModalForm = taskModalForm.addComment(titleComment);
        boolean existNewComment = taskModalForm.existsCommentByText(titleComment);


        assertTrue(existNewComment);

    }

    @AfterEach
    public void closeBrowser() {
        Selenide.closeWebDriver();
    }
}
