package ru.slisarenko.geometryapp;


import ru.slisarenko.Cube;
import ru.slisarenko.geometrylibrary.Circle;
import ru.slisarenko.geometrylibrary.Triangle;
import ru.slisarenko.geometryutils.ShapeAreaComparator;

public class Main {
    public static void main(String[] args) {
        var triangle = new Triangle(34.5, 43, 20);
        System.out.println("S = " + triangle.calculateArea() + " P = " + triangle.calculatePerimeter());
        System.out.println(triangle.getDescription());
        var circle = new Circle(34.5, 43);
        System.out.println("C = " + circle.calculateArea() + " P = " + circle.calculatePerimeter());
        var comparator = new ShapeAreaComparator();
        System.out.println("Area circle compere area triangle " + comparator.compare(circle, triangle));

        Cube cube = new Cube(3);
        System.out.printf("Cube: volume=%.2f, surface=%.2f%n", cube.volume(), cube.surfaceArea());

    }
}