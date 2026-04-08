package ru.slisarenko.geometrylibrary;

public class Circle extends GeometricShare implements AreaAndPerimeter {
    private double radius;

    public Circle(double length, double width) {
        super(length, width);
    }

    @Override
    public double calculateArea() {
        return Math.pow(super.getLength(), 2) / (4 * Math.PI);
    }

    @Override
    public double calculatePerimeter() {
        return 2 * Math.PI * this.radius;
    }

    private double setRadius() {
        var area = calculateArea();
        return this.radius = Math.sqrt((area / Math.PI));
    }
}
