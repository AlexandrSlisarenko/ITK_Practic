package ru.slisarenko;

@FunctionalInterface
public interface Filter<T> {
    T apply(T element);
}
