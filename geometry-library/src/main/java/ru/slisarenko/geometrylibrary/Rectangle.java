package ru.slisarenko.geometrylibrary;

public class Rectangle extends GeometricShare implements AreaAndPerimeter{
    public Rectangle(double length, double width) {
        super(length, width);
    }

    @Override
    public double calculateArea() {
        return super.getLength() * super.getWidth();
    }

    @Override
    public double calculatePerimeter() {
        return (super.getLength() + super.getWidth() * 2);
    }
}
