package ru.slisarenko.geometrylibrary;

public class Triangle extends GeometricShare implements AreaAndPerimeter{
    private double footing;
    public Triangle(double length, double width, double footing) {
        super(length, width);
        this.footing = length;
    }


    @Override
    public double calculateArea() {
        return (super.getLength() * this.footing) / 2;
    }

    @Override
    public double calculatePerimeter() {
        return this.footing + super.getLength() + super.getWidth();
    }
}
