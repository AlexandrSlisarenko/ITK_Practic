package ru.slisarenko.geometrylibrary;

public interface AreaAndPerimeter {
    public double calculateArea();
    public double calculatePerimeter();
    default String getDescription() {
        return "This is a geometric shape " + this.getClass().getName();
    }
}
