package ru.slisarenko.studyhub;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
        String expected = "Selenium и Selenide";
        String actual = page.loginToPlatform()
                .openCourseByPercent("96")
                .loadPage()
                .getBreadcrumbsActualCourse();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void checkClickLastLessonsButton() {
        String expected = "Selenium и Selenide";
        String actual = page.loginToPlatform()
                .openCourseByPercent("96")
                .loadPage()
                .navigateToCourse()
                .getBreadcrumbsActualCourse();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void checkSelectCourseByName() {

        String nameCourse = "Kafka для AQA";
        String actualCourseName = page.loginToPlatform()
                .openCourseByCourseName(nameCourse)
                .loadPage()
                .getBreadcrumbsActualCourse();
        Assertions.assertEquals(nameCourse, actualCourseName);
    }

    @Test
    public void checkSelectCourseByNameWhichIsHidden() {

        String nameCourse = "Основы unit-тестирования";
        String actualCourseName = page.loginToPlatform()
                .openCourseByCourseNameCssSelector(nameCourse)
                .loadPage()
                .getBreadcrumbsActualCourse();
        Assertions.assertEquals(nameCourse, actualCourseName);
    }

    @Test
    public void checkCountCourse() {
        int expected = 8;

        int actual = page.loginToPlatform()
                .getCountCoursesInPageOnSecondPage();

        Assertions.assertEquals(expected, actual);
    }

    @AfterEach
    public void closeBrowser() {
        page.quit();
    }
}
