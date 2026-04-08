package ru.slisarenko.geometrylibrary;

public abstract class GeometricShare {
    private double length;
    private double width;

    protected GeometricShare(double length, double width) {
        this.length = length;
        this.width = width;
    }

    public double getLength() {
        return length;
    }

    public double getWidth() {
        return width;
    }
}
