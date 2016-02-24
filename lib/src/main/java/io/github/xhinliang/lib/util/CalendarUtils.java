package io.github.xhinliang.lib.util;

import java.util.Calendar;

import io.github.xhinliang.lunarcalendar.Lunar;
import io.github.xhinliang.lunarcalendar.LunarCalendar;

/**
 * Created by xhinliang on 16-2-23.
 * CalendarUtils
 */
public class CalendarUtils {


    public static int getGregorianRange(Calendar bornCalendar) {
        Calendar today = Calendar.getInstance();
        Calendar birthCalendar = getBirthCalendar(bornCalendar, today);
        int range = 0;
        while (today.compareTo(birthCalendar) == -1) {
            ++range;
            if (today.get(Calendar.MONTH) == Calendar.DECEMBER && today.get(Calendar.DAY_OF_MONTH) == 31)
                today.roll(Calendar.YEAR, true);
            today.roll(Calendar.DAY_OF_YEAR, true);
        }
        return range;
    }


    public static Calendar getBirthCalendar(Calendar birthCalendar, Calendar today) {
        int bornYear = birthCalendar.get(Calendar.YEAR);
        int bornMonth = birthCalendar.get(Calendar.MONTH);
        int bornDay = birthCalendar.get(Calendar.DAY_OF_MONTH);
        int thisYear = today.get(Calendar.YEAR);
        // 闰年2月29出生
        if (bornYear % 4 == 0 && bornMonth == Calendar.FEBRUARY && bornDay == 29) {
            if (thisYear % 4 == 0) {
                birthCalendar.set(thisYear, bornMonth, bornDay);
                int t = MathUtils.compare(today.get(Calendar.DAY_OF_YEAR), birthCalendar.get(Calendar.DAY_OF_YEAR));
                switch (t) {
                    case 1:
                        birthCalendar.roll(Calendar.YEAR, 4);
                        return birthCalendar;
                    case 0:
                        return today;
                    default:
                        return birthCalendar;
                }
            }
            while (thisYear % 4 != 0)
                ++thisYear;
            birthCalendar.set(thisYear, bornMonth, bornDay);
            return birthCalendar;
        }
        birthCalendar.set(thisYear, bornMonth, bornDay);
        int t = MathUtils.compare(today.get(Calendar.DAY_OF_YEAR), birthCalendar.get(Calendar.DAY_OF_YEAR));
        switch (t) {
            case 1:
                birthCalendar.roll(Calendar.YEAR, true);
                return birthCalendar;
            case 0:
                return today;
            default:
                birthCalendar.set(thisYear, bornMonth, bornDay);
                return birthCalendar;
        }
    }


    public static int computeLunarRange(Calendar calendar) {
        int dayRange = 0;
        Lunar birth = getLunar(calendar).getLunar();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int iMonth = calendar.get(Calendar.MONTH) + 1;
        int iYear = calendar.get(Calendar.YEAR);
        while (true) {
            LunarCalendar[][] month = LunarCalendar.getInstanceMonth(iYear, iMonth);
            for (LunarCalendar[] week : month)
                for (LunarCalendar day : week) {
                    if (day == null)
                        continue;
                    if (day.isToday())
                        dayRange = 0;
                    Lunar temp = day.getLunar();
                    if (temp.month == birth.month && temp.day == birth.day)
                        return dayRange;
                    ++dayRange;
                }
            if (iMonth == 12) {
                iMonth = 1;
                ++iYear;
                continue;
            }
            ++iMonth;
        }
    }

    public static LunarCalendar getLunar(Calendar calendar) {
        return LunarCalendar.getInstance(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }
}
