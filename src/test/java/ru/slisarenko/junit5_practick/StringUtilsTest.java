package ru.slisarenko.junit5_practick;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    @Test
    @DisplayName("Checking the word level for a polindrome returns true")
    void isPalindrome() {
        StringUtils utils = new StringUtils();
        String test = "level";
        boolean expected = true;

        boolean actual = utils.isPalindrome(test);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Checking the word hello for a polindrome returns true")
    void isNotPalindrome() {
        StringUtils utils = new StringUtils();
        String test = "hello";
        boolean expected = false;

        boolean actual = utils.isPalindrome(test);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Checking the empty word for a polindrome returns true")
    void checkEmptyWordForPalindromeReturnTrue() {
        StringUtils utils = new StringUtils();
        String test = " ";
        boolean expected = true;

        boolean actual = utils.isPalindrome(test);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Checking null for a polindrome returns false")
    void checkNullForPalindromeReturnFalse() {
        StringUtils utils = new StringUtils();
        String test = null;
        boolean expected = false;

        boolean actual = utils.isPalindrome(test);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Checking the camel case word for a polindrome returns true")
    void checkCamelCaseForPalindromeReturnTrue() {
        StringUtils utils = new StringUtils();
        String test = "RaceCar";
        boolean expected = true;

        boolean actual = utils.isPalindrome(test);

        assertEquals(expected, actual);
    }


    @Test
    @DisplayName("Checking count vowels")
    void countVowels() {
        StringUtils utils = new StringUtils();
        String test = "hello";
        int expected = 2;

        int actual = utils.countVowels(test);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Checking zero vowels")
    void checkZeroVowels(){
        StringUtils utils = new StringUtils();
        String test = "xyz";
        int expected = 0;

        int actual = utils.countVowels(test);

        assertEquals(expected, actual);
    }
    @Test
    @DisplayName("Checking uppercase vowels")
    void checkUpperCaseVowels(){
        StringUtils utils = new StringUtils();
        String test = "AEIOU";
        int expected = 5;

        int actual = utils.countVowels(test);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Checking input null")
    void checkNullInput() {
        StringUtils utils = new StringUtils();
        String test = null;
        int expected = 0;

        int actual = utils.countVowels(test);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Checking empty string")
    void checkEmptyString() {
        StringUtils utils = new StringUtils();
        String test = " ";
        int expected = 0;

        int actual = utils.countVowels(test);

        assertEquals(expected, actual);
    }
}