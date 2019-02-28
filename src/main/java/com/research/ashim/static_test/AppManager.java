package com.research.ashim.static_test;

import java.util.Random;

public class AppManager {

    public boolean managerResponse(String question) {
        return AppManager.isResponsePositive(question);
    }

    public static boolean isResponsePositive(String value) {
        if (value == null)
            return false;
        int length = value.length();
        return length == mood();
    }

    private static int mood() {
        return new Random().nextInt(7);
    }

    private static Integer stringToInteger(String num) {
        return Integer.parseInt(num);
    }
}
