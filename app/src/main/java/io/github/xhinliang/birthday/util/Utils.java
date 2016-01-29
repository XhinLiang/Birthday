package io.github.xhinliang.birthday.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by xhinliang on 16-1-29.
 * xhinliang@gmail.com
 */
public class Utils {
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public static String getBirthday(Date date) {
        if (date == null)
            return "";
        return format.format(date);
    }
}
