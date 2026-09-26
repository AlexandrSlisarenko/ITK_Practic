package ru.slisarenko.demoqa.text_box;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TextBoxTest {
    private TextBoxSeleniumPageObject page;

    @BeforeEach
    public void setUp() {
        page = new TextBoxSeleniumPageObject();
    }

    @Test
    public void testTextBox() {
        page.submitData();
    }

    @AfterEach
    public void quit() {
        page.quitDriver();
    }
}
