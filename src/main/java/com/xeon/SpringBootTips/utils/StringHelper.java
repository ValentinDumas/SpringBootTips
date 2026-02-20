package com.xeon.SpringBootTips.utils;

public class StringHelper {

    public static boolean isEmptyOrBlank(String string) {
        return string.isBlank();
    }

    public static boolean isAlpha(String name) {
        return name.matches("[a-zA-Z]+");
    }
}
