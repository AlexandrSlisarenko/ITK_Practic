package ru.slisarenko;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        var arrayInt = new Integer[]{3,3,6,4};
        var arrayString = new String[]{"3","3","6","4"};
        var filterInteger = new FilterInteger();
        var filterString = new FilterString();
        var myFilter = new FilterProcessot();

        var resultStr = myFilter.filter(arrayString, filterString);
        System.out.println(Arrays.toString(resultStr));

        var resultInt = myFilter.filter(arrayInt, filterInteger);
        System.out.println(Arrays.toString(resultInt));


    }


}