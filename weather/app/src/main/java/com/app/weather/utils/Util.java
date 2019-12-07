package com.app.weather.utils;

import com.orhanobut.logger.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
* @author: 105032016090颜文君
 * @description： 日期转化类 插入数据库时 存入当前日期 需要格式转换为数据库中的日期类型
 * @data: 2019/5/25
 */

public class Util {

    public static Date stringToDate(String date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        }
        return null;
    }

    public static Date stringToDate(String date) {
        return stringToDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String dateToString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String dateToString(Date date) {
        return dateToString(date, "yyyy-MM-dd HH:mm:ss");
    }
}
