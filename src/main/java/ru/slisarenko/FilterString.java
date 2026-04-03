package ru.slisarenko;

public class FilterString implements Filter<String> {
    @Override
    public String apply(String element) {
        return element + " + 5";
    }
}
