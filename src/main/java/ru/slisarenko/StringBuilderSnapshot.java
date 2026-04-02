package ru.slisarenko;

public class StringBuilderSnapshot {
    private final StringBuilder builder;

    public StringBuilderSnapshot(StringBuilder builder) {
        this.builder = new StringBuilder(builder);
    }

    StringBuilder getSnapshot() {
        return this.builder;
    }
}
