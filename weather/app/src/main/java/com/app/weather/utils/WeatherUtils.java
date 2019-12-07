package com.app.weather.utils;

import com.app.weather.R;
import com.app.weather.bean.WeatherData;
import com.orhanobut.logger.Logger;

import java.util.Calendar;
import java.util.List;

/**
 * @author: 105032016090颜文君
 * @description： 设置天气图标 根据天气状况切换背景图 定义星期
 * @data: 2019/5/25
 */

public class WeatherUtils {
    public static String toDateByWeather(WeatherData.ResultBean.DataBean.WeatherBeanX.InfoBeanX info) {
        String weather = "";
        Calendar instance = Calendar.getInstance();

        int hour = instance.get(Calendar.HOUR_OF_DAY);
        List<String> dawn = info.getDawn();
        List<String> night = info.getNight();
        List<String> day = info.getDay();
        if (hour >= 6) {
            if (dawn != null && !dawn.isEmpty()) {
                weather = dawn.get(1);
            } else {
                weather = day.get(1);
            }
        } else if (hour >= 9) {
            weather = day.get(1);
        } else if (hour >= 18) {
            weather = night.get(1);
        }
        return weather;
    }

    public static int setImg(String weather) {
        Logger.d("setImg" + weather);
        int rId = R.mipmap.ic_default_samll;
        if (weather == null) return rId;
        switch (weather) {
            case "晴":
                rId = R.mipmap.ic_q;
                break;
            case "多云":
                rId = R.mipmap.ic_dy;
                break;
            case "阵雨":
                rId = R.mipmap.ic_zy;
                break;
            case "大雨":
                rId = R.mipmap.ic_zy;
                break;
            case "小雨":
                rId = R.mipmap.ic_zy;
                break;
            case "雷阵雨":
                rId = R.mipmap.ic_lzy;
                break;
            case "阴":
                rId = R.mipmap.ic_y;
                break;
            case "霾":
                rId = R.mipmap.ic_fog_samll;
                break;
        }

        return rId;
    }

    public static String numberToString(int i) {
        String week = null;
        switch (i) {
            case 1:
                week = "星期一";
                break;
            case 2:
                week = "星期二";
                break;
            case 3:
                week = "星期三";
                break;
            case 4:
                week = "星期四";
                break;
            case 5:
                week = "星期五";
                break;
            case 6:
                week = "星期六";
                break;
            case 7:
                week = "星期日";
                break;
        }
        return week;
    }


    public static int setLayoutBgImg(String weather) {

        int rId = R.mipmap.bg_sunny_day;
        if (weather == null) return rId;
        int hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        switch (weather) {
            case "晴":
                if (hourOfDay <= 17) {
                    rId = R.mipmap.bg_sunny_day;
                } else {
                    rId = R.mipmap.bg_sunny_night;
                }

                break;
            case "多云":
                if (hourOfDay <= 17) {
                    rId = R.mipmap.bg_fog_day;
                } else {
                    rId = R.mipmap.bg_fog_night;
                }
                break;
            case "阵雨":
                rId = R.mipmap.bg_yu_night;
                break;
            case "大雨":
                rId = R.mipmap.bg_yu_night;
                break;
            case "小雨":
                rId = R.mipmap.bg_yu_night;
                break;
            case "雷阵雨":
                rId = R.mipmap.bg_yu_night;
                break;
            case "阴":
                if (hourOfDay <= 17) {
                    rId = R.mipmap.bg_fog_and_haze;
                } else {
                    rId = R.mipmap.bg_fog_and_haze;
                }
                break;
            case "大雪":
                if (hourOfDay <= 17) {
                    rId = R.mipmap.bg_snow_day;
                } else {
                    rId = R.mipmap.bg_snow_night;
                }
                break;
            case "霾":
                if (hourOfDay <= 17) {
                    rId = R.mipmap.bg_fog;
                } else {
                    rId = R.mipmap.bg_fog;
                }
                break;
            case "小雪":
                if (hourOfDay <= 17) {
                    rId = R.mipmap.bg_snow_day;
                } else {
                    rId = R.mipmap.bg_snow_night;
                }
                break;
        }
        return rId;
    }

}
