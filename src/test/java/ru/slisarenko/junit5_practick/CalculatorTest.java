package ru.slisarenko.junit5_practick;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class CalculatorTest {

    @Test
    @DisplayName("checking arithmetic addition")
    void add() {
        Calculator calc = new Calculator();
        int a = 2;
        int b = 2;
        int expected = 4;

        int result = calc.add(a, b);

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("checking arithmetic subtraction")
    void subtract() {
        Calculator calc = new Calculator();
        int a = 2;
        int b = 2;
        int expected = 0;

        int result = calc.subtract(a, b);

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("checking arithmetic multiplication")
    void multiply() {
        Calculator calc = new Calculator();
        int a = 2;
        int b = 2;
        int expected = 4;

        int result = calc.multiply(a, b);

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("checking arithmetic division")
    void divide() {
        Calculator calc = new Calculator();
        int a = 2;
        int b = 2;
        double expected = 1;

        double result = calc.divide(a, b);

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("check divide by zero")
    void check_Divide_By_Zero(){
        Calculator calc = new Calculator();
        int a = 2;
        int b = 0;

        ArithmeticException exception = assertThrows(ArithmeticException.class, () -> calc.divide(a, b));

        assertEquals("Divide by zero", exception.getMessage());
    }
}