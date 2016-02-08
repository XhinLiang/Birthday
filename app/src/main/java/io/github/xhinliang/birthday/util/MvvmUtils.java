package io.github.xhinliang.birthday.util;

import com.wdullaer.materialdatetimepicker.Lunar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
        return LunarUtils.getInstance().getFullDay(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }


}
