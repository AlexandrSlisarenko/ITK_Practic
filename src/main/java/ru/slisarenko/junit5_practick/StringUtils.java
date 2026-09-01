package ru.slisarenko.junit5_practick;

public class StringUtils {
    boolean isPalindrome(String input) {
        if (input == null) {
            return false;
        }
        if (input.isEmpty()) {
            return true;
        }
        if (input.length() % 2 == 0) {
            return false;
        }
        int i = 0, j = input.length() - 1;
        char[] chars = input.toLowerCase().toCharArray();
        int k = input.length() / 2;
        while (i < k) {
            if (chars[i] != chars[j]) {
                return false;
            } else {
                i++;
                j--;
            }
        }
        return true;
    }

    int countVowels(String input) {
        if (input == null) {
            return 0;
        }
        if (input.isEmpty()) {
            return 0;
        }
        char[] chars = input.toCharArray();
        int i = 0, count = 0;
        while (i < chars.length) {
            switch (chars[i]) {
                case 'a':
                case 'e':
                case 'i':
                case 'o':
                case 'u':
                case 'A':
                case 'E':
                case 'I':
                case 'O':
                case 'U':
                    count++;
            }
            i++;
        }
        return count;
    }
}
