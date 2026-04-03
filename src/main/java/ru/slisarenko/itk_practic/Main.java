package ru.slisarenko.itk_practic;


public class Main {
    public static void main(String[] args) {

        StringBuilderWithUndo builder = new StringBuilderWithUndo();

        int t = 123;
        builder.write(t);

        boolean b = true;
        builder.write(b);

        double d = 34.12;
        builder.write(d);

        builder.write(" => Итого");

        System.out.println(builder.getText());
        builder.restoreStateByKey(2);
        System.out.println(builder.getText());
        builder.restoreStateByKey(1);
        System.out.println(builder.getText());
        builder.restoreStateDefault();
        System.out.println(builder.getText());
    }
}