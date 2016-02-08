package io.github.xhinliang.birthday.util;

import io.github.xhinliang.lunarcalender.DPCNCalendar;

/**
 * Created by xhinliang on 16-2-3.
 * xhinliang@gmail.com
 */
public class LunarUtils {
    private static LunarUtils ourInstance = new LunarUtils();
    private DPCNCalendar calendar;

    public static LunarUtils getInstance() {
        return ourInstance;
    }

    private LunarUtils() {
        calendar = new DPCNCalendar();
    }

    public String getSubDay(int year, int month, int day) {
        return this.calendar.getSubDay(year, month, day);
    }

    public String getFullDay(int year, int month, int day) {
        return this.calendar.getFullDay(year, month, day);
    }
}
