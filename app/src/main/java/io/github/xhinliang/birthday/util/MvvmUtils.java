package io.github.xhinliang.birthday.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.github.xhinliang.birthday.model.Contact;
import io.github.xhinliang.lib.util.CalendarUtils;
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
    public static String getBirthday(Date date, Boolean isLunar) {
        if (date == null)
            return "";
        if (isLunar == null || !isLunar)
            return format.format(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return LunarCalendar.getInstance(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH))
                .getFullLunarStr();
    }

    /**
     * recycler_item_contact.xml
     * 获取今天与生日距离的天数
     *
     * @param contact contact
     * @return 天数
     */
    public static String getDayRange(Contact contact) {
        Calendar bornCalendar = Calendar.getInstance();
        bornCalendar.setTime(contact.getBornDay());
        if (contact.getIsLunar()) {
            return CalendarUtils.computeLunarRange(bornCalendar) + "";
        }
        return CalendarUtils.getGregorianRange(bornCalendar) + "";
    }


}
