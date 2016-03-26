package io.github.xhinliang.birthday.model;


import org.parceler.Parcel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.github.xhinliang.birthday.util.MvvmUtils;
import io.github.xhinliang.lib.util.MathUtils;
import io.github.xhinliang.lunarcalendar.Lunar;
import io.github.xhinliang.lunarcalendar.LunarCalendar;
import io.realm.ContactRealmProxy;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by xhinliang on 16-1-28.
 * xhinliang@gmail.com
 */
@Parcel(implementations = {ContactRealmProxy.class}, value = Parcel.Serialization.BEAN, analyze = {Contact.class})
public class Contact extends RealmObject {

    @Ignore
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    @Ignore
    public static final String FIELD_GROUP = "group";
    @Ignore
    private static final int DEFAULT_DAY_RANGE = 4000;

    @PrimaryKey
    private long createTimeMillis;
    @Required
    private String name;

    private int dayRange;
    private String bornDayStr;

    private boolean isLunar;
    private String telephone;
    private Date bornDay;
    private String description;
    private String group;
    private String picture;

    private long lastModifyMillis;


    public boolean getIsLunar() {
        return isLunar;
    }

    public void setIsLunar(boolean isLunar) {
        this.isLunar = isLunar;
        calculateBornDayStr();
    }

    public long getDayRange() {
        return dayRange;
    }

    public long getCreateTimeMillis() {
        return createTimeMillis;
    }

    public void setCreateTimeMillis(long createTimeMillis) {
        this.createTimeMillis = createTimeMillis;
    }

    public Contact() {
        this.createTimeMillis = System.currentTimeMillis();
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Date getBornDay() {
        return bornDay;
    }

    public void setBornDay(Date bornDay) {
        this.bornDay = bornDay;
        calculateBornDayStr();
    }

    public String getBornDayStr() {
        return bornDayStr;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    private boolean modifyToday() {
        long now = System.currentTimeMillis();
        long oneDay = 24 * 60 * 60 * 1000;
        long abs = Math.abs(now - lastModifyMillis);
        return abs < oneDay;
    }

    public void calculateDateRange() {
        // 今天已经修改过，数据已是最新
        if (modifyToday()) {
            return;
        }
        // 没有设置生日，返回一个最大值
        if (bornDay == null) {
            dayRange = DEFAULT_DAY_RANGE;
            return;
        }
        // 准备一个 Calendar 用于计算
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(bornDay);
        // 如果这个人过农历生日
        if (isLunar) {
            dayRange = computeLunarRange(calendar);
            lastModifyMillis = System.currentTimeMillis();
            return;
        }
        // 如果这个人过公历生日
        dayRange = getGregorianRange(calendar);
        lastModifyMillis = System.currentTimeMillis();
    }

    /**
     * 在更改公历农历或者更改出生日期的时候，内部调用此方法，更新表示生日的 String
     */
    private void calculateBornDayStr() {
        if (bornDay == null) {
            bornDayStr = "";
            return;
        }
        bornDayStr = MvvmUtils.getBirthday(bornDay, isLunar);
    }

    private static int getGregorianRange(Calendar bornCalendar) {
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


    private static Calendar getBirthCalendar(Calendar bornCalendar, Calendar today) {
        int bornYear = bornCalendar.get(Calendar.YEAR);
        int bornMonth = bornCalendar.get(Calendar.MONTH);
        int bornDay = bornCalendar.get(Calendar.DAY_OF_MONTH);
        int thisYear = today.get(Calendar.YEAR);
        // 闰年2月29出生
        if (bornYear % 4 == 0 && bornMonth == Calendar.FEBRUARY && bornDay == 29) {
            if (thisYear % 4 == 0) {
                bornCalendar.set(thisYear, bornMonth, bornDay);
                int t = MathUtils.compare(today.get(Calendar.DAY_OF_YEAR), bornCalendar.get(Calendar.DAY_OF_YEAR));
                switch (t) {
                    case 1:
                        bornCalendar.roll(Calendar.YEAR, 4);
                        return bornCalendar;
                    case 0:
                        return today;
                    default:
                        return bornCalendar;
                }
            }
            while (thisYear % 4 != 0)
                ++thisYear;
            bornCalendar.set(thisYear, bornMonth, bornDay);
            return bornCalendar;
        }
        bornCalendar.set(thisYear, bornMonth, bornDay);
        int t = MathUtils.compare(today.get(Calendar.DAY_OF_YEAR), bornCalendar.get(Calendar.DAY_OF_YEAR));
        switch (t) {
            case 1:
                bornCalendar.roll(Calendar.YEAR, true);
                return bornCalendar;
            case 0:
                return today;
            default:
                bornCalendar.set(thisYear, bornMonth, bornDay);
                return bornCalendar;
        }
    }


    private static int computeLunarRange(Calendar calendar) {
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
