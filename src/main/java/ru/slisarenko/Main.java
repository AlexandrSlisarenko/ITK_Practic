package ru.slisarenko;

import java.util.Arrays;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static List<Order> orders = List.of(
            new Order("Laptop", 1200.0),
            new Order("Smartphone", 800.0),
            new Order("Laptop", 1500.0),
            new Order("Tablet", 500.0),
            new Order("Smartphone", 900.0)
    );

    public static void main(String[] args) {

        var test = StreamCollectorsExample.getOrdersGroupByProduct(orders);

        test.forEach((e, list) -> System.out.println("product = " + e + " order count = " + list.size()));
        System.out.println("----------------------------------------------------------------");

        var test2 = StreamCollectorsExample.getTotalPriceByProduct(orders);

        test2.forEach((order, totalPrice) -> System.out.println("order = " + order + " TotalPrice = " + totalPrice));
        System.out.println("----------------------------------------------------------------");

        var test3 = StreamCollectorsExample.getThreeProductMyMaxPrice(orders);

        test3.forEach((order) -> System.out.println("order = " + order.getKey() + " TotalPrice = " + order.getValue()));
        System.out.println("----------------------------------------------------------------");

        var test4 = StreamCollectorsExample.getTotalStream(orders);

        test4.forEach((order) -> System.out.println("order = " + order.getKey() + " TotalPrice = " + order.getValue()));
        System.out.println("----------------------------------------------------------------");
    }
}