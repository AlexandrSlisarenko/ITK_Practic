package ru.slisarenko;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.swing.border.Border;

public class StreamCollectorsExample {


    public static Map<String,List<Order>> getOrdersGroupByProduct(List<Order> orders) {
        return orders.stream()
                .collect(Collectors.groupingBy(Order::getProduct));
    }

    public static Map<String, Double> getTotalPriceByProduct(List<Order> orders) {
        return orders.stream()
                .collect(Collectors.groupingBy(Order::getProduct, Collectors.summingDouble(Order::getCost)));
    }

    public static List<Entry<String, Double>> getThreeProductMyMaxPrice(List<Order> orders) {
        return getTotalPriceByProduct(orders).entrySet().stream()
                .sorted((e1,e2)-> e2.getValue().compareTo(e1.getValue()))
                .limit(3)
                .collect(Collectors.toList());
    }

    public static List<Entry<String, Double>> getTotalStream(List<Order> orders) {
        return  orders.stream()
                .collect(Collectors.groupingBy(Order::getProduct, Collectors.summingDouble(Order::getCost)))
                .entrySet().stream()
                .sorted((e1,e2)-> e2.getValue().compareTo(e1.getValue()))
                .limit(3)
                .collect(Collectors.toList());
    }



}
