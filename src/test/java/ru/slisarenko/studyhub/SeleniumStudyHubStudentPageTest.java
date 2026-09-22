package ru.slisarenko.studyhub;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("Домашняя страница Study Hub")
@Feature("Проверка функционала")
public class SeleniumStudyHubStudentPageTest {
    private SeleniumStudyHubStudentPage page;

    @BeforeEach
    public void setUpBeforeClass() throws Exception {
        page = new SeleniumStudyHubStudentPage();
    }

    @Test
    public void checkPageTitle() {
        String title = page.loginToPlatform()
                .getTitle();

        assertEquals("Study Hub", title);
    }

    @Test
    public void checkClickNextButton() {
        page.loginToPlatform()
                .openCourseByPercent("96");
    }

    @AfterEach
    public void closeBrowser() {
        //page.quit();
    }
}
