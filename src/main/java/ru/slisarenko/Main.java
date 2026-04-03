package ru.slisarenko;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public class Main {
    public static void main(String[] args) {
        var array = List.of(3,3,6,4,4,8,8,1,1,1).toArray();
        var map = getMapCountOfElements(array);
        map.forEach((k, v) -> System.out.println(k + " : " + v));
    }

    public static <T> Map<T, Long> getMapCountOfElements (T[] array) {
        if (array == null || array.length == 0) {
            return new HashMap<>();
        }
        return Arrays.stream(array).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }
}