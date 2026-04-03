package ru.slisarenko;

import java.lang.reflect.Array;

public class FilterProcessot {

    @SuppressWarnings("unchecked")
    public <T> T[] filter(T[] array, Filter<T> filter) {
        if (array == null || array.length == 0) {
            return (T[]) Array.newInstance(Object.class, 0);
        }
        for (int i = 0; i < array.length; i++) {
            array[i] = filter.apply(array[i]);
        }
        return array;
    }
}
