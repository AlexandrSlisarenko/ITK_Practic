package ru.slisarenko;


public class FilterInteger implements  Filter<Integer> {

    @Override
    public Integer apply(Integer element) {
        return element + 5;
    }
}
