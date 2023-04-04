package com.qy.ntf.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by songYu on 2018/6/26 15:22
 * DESC : 字符串工具
 */
public class StringUtil {

    /**
     * 字符串不为空和Null
     *
     * @param string
     * @return
     */
    public static Boolean notNullOrEmpty(String string) {
        if (null == string || "".equals(string.trim()) || "null".equals(string.trim()) || "undefined".equals(string.trim())) {
            return false;
        }
        return true;
    }

    /**
     * 是否可以转成数字
     *
     * @param string
     * @return
     */
    public static Boolean isNum(String string) {
        try {
            new BigDecimal(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 在one里检查two
     *
     * @param one
     * @param two
     * @return
     */
    public static Boolean include(String one, String two) {
        one = one.trim();
        two = two.trim();
        int index = one.indexOf(two);
        if (index == -1) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * 字符串转集合
     *
     * @param string
     * @param incise
     * @param <T>
     * @return
     */
    public static <T> List<T> stringTurnList(String string, String incise) {
        String[] strings = string.split(incise);// 按给的切割
        List<T> list = new ArrayList<T>(strings.length);
        for (int i = 0; i < strings.length; i++) {
            list.add((T) strings[i]);
        }
        return list;
    }

    /**
     * 字符串转日期
     *
     * @param string
     * @param patten
     * @return
     */
    public static Date stringTrunDate(String string, String patten) {
        try {
            return new SimpleDateFormat(patten).parse(string);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 字符串转日历
     *
     * @param string
     * @return
     */
    public static Calendar stringTrunCalendar(String string, String patten) {
        SimpleDateFormat sdf = new SimpleDateFormat(patten);
        Calendar calendar = null;
        try {
            Date date = sdf.parse(string);
            calendar = Calendar.getInstance();
            calendar.setTime(date);
        } catch (ParseException e) {
        }
        return calendar;
    }

    /**
     * 字符串转时间戳
     *
     * @param string
     * @return
     */
    public static Long stringTrunLong(String string, String patten) {
        SimpleDateFormat sdf = new SimpleDateFormat(patten);
        Long l = null;
        try {
            Date date = sdf.parse(string);
            l = date.getTime();
        } catch (ParseException e) {
        }
        return l;
    }

    /**
     * 对象转字符串
     *
     * @param object
     * @return
     */
    public static String valueOf(Object object) {
        try {
            return String.valueOf(object);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 获取like字段
     *
     * @param column
     * @return
     */
    public static String getLikeColumn(String column) {
        return new StringBuffer("%").append(column).append("%").toString();
    }

}
