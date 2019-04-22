package com.art.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtils {

    private static SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    private static List<Calendar> holidayList;
    private static boolean holidayFlag;

    /**
     * 计算工作日
     * 具体节日包含哪些,可以在HolidayMap中修改
     *
     * @param src     日期(源)
     * @param adddays 要加的天数
     * @throws throws [违例类型] [违例说明]
     */
    public static Calendar addDateByWorkDay(Calendar src, int adddays) {
        holidayFlag = false;
        for (int i = 0; i < adddays; i++) {
            //把源日期加一天
            src.add(Calendar.DAY_OF_MONTH, 1);
            holidayFlag = checkHoliday(src);
            if (holidayFlag) {
                i--;
            }
        }
        return src;
    }

    /**
     * 校验指定的日期是否在节日列表中
     * 具体节日包含哪些,可以在HolidayMap中修改
     *
     * @param src 要校验的日期(源)
     */
    public static boolean checkHoliday(Calendar src) {
        boolean result = false;
        //先检查是否是周六周日(有些国家是周五周六)
        if (src.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                || src.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return true;
        }
        return result;
    }

    /**
     * 初始化节日List,如果需要加入新的节日,请在这里添加
     * 加的时候请尽量使用Calendar自带的常量而不是魔鬼数字
     * 注:年份可以随便写,因为比的时候只比月份和天
     */
    private static void initHolidayList() {
        holidayList = new ArrayList<Calendar>();
        //五一劳动节
        Calendar may1 = Calendar.getInstance();
        may1.set(Calendar.MONTH, Calendar.MAY);
        may1.set(Calendar.DAY_OF_MONTH, 1);
        holidayList.add(may1);
        Calendar may2 = Calendar.getInstance();
        may2.set(Calendar.MONTH, Calendar.MAY);
        may2.set(Calendar.DAY_OF_MONTH, 2);
        holidayList.add(may2);
        Calendar may3 = Calendar.getInstance();
        may3.set(Calendar.MONTH, Calendar.MAY);
        may3.set(Calendar.DAY_OF_MONTH, 3);
        holidayList.add(may3);
        Calendar h3 = Calendar.getInstance();
        h3.set(2000, 1, 1);
        holidayList.add(h3);
        Calendar h4 = Calendar.getInstance();
        h4.set(2000, 12, 25);
        holidayList.add(h4);
        Calendar may5 = Calendar.getInstance();
        may5.set(Calendar.MONTH, Calendar.MAY);
        may5.set(Calendar.DAY_OF_WEEK_IN_MONTH, 2);
        may5.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        holidayList.add(may5);
    }

    public static String formatDate(Date date) {
        return dateFormatter.format(date);
    }

    public static Date getCurrentDate(Date date) {
        String currentDayStr = dateFormatter.format(date);
        try {
            return dateFormatter.parse(currentDayStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static String formatTime(Date date) {
        return timeFormatter.format(date);
    }

    public static Date formatTime(String date) {
        try {
            return timeFormatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static String formatGMT8Time(Date date) {
        TimeZone gmtTime = TimeZone.getTimeZone("GMT+08:00");
        timeFormatter.setTimeZone(gmtTime);
        return timeFormatter.format(date);
    }

}