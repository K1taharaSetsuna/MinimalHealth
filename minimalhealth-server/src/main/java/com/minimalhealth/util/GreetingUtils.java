package com.minimalhealth.util;

import java.time.LocalTime;

public class GreetingUtils {
    public static String getGreeting(int hour) {
        if (hour >= 5 && hour < 12) return "早上好";
        if (hour >= 12 && hour < 18) return "下午好";
        if (hour >= 18 && hour < 23) return "晚上好";
        return "夜深了";
    }

    public static String getGreeting() {
        return getGreeting(LocalTime.now().getHour());
    }
}
