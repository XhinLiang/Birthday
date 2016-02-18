package io.github.xhinliang.birthday.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.github.xhinliang.lunarcalendar.LunarCalendar;

/**
 * Created by xhinliang on 16-1-29.
 * xhinliang@gmail.com
 */
public class MvvmUtils {
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    /**
     * activity_contact_detail.xml
     *
     * @param date date
     * @return dateString
     */
    @SuppressWarnings("unused")
    public static String getBirthday(Date date, Boolean isLunar) {
        if (date == null)
            return "";
        if (isLunar == null || !isLunar)
            return format.format(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return LunarCalendar
                .getInstance(calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH))
                .getFullLunarStr();
    }


}
