package ru.slisarenko;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        Map<Integer,StringBuilderSnapshot> storage= new HashMap<>();

        StringBuilderWithUndo builder= new StringBuilderWithUndo();

        storage.put(0, builder.saveState());
        int t = 123;
        builder.write(t);

        boolean b = true;
        builder.write(b);

        storage.put(1, builder.saveState());

        double d = 34.12;
        builder.write(d);

        storage.put(2, builder.saveState());

        builder.write(" => Итого");

        System.out.println(builder.getText());
        builder.restoreState(storage.get(2));
        System.out.println(builder.getText());
        builder.restoreState(storage.get(1));
        System.out.println(builder.getText());
        builder.restoreState(storage.get(0));
        System.out.println(builder.getText());
    }
}