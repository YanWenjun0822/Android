package com.app.weather.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.app.weather.App;

/**
 * @author: 105032016090颜文君
 * @description: Android常用的工具类sharedpreferences封装类 SpUtils
 * synchronized是对类的当前实例进行加锁，防止其他线程同时访问该类的该实例的所有synchronized块
 * @data: 2019/5/25
*/
public class SpUtil {
    public static final String PREFERENCE_NAME = "weather_config";
    private static SpUtil spUtils;
    private final App mApp;
    //创建一个写入器
    private final SharedPreferences mPreferences;
    private final SharedPreferences.Editor mEditor;

    public synchronized static SpUtil getInstance() {
        synchronized (SpUtil.class) {
            //一个全局的静态对象
            if (spUtils == null) {
                spUtils = new SpUtil();
            }
        }
        return spUtils;
    }

    private SpUtil() {
        mApp = App.getInstance();
        mPreferences = mApp.getApplicationContext()
                .getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();//写入数据
    }

    public String getString(String key, String defValue) {
        if (mPreferences != null) {
            return mPreferences.getString(key, defValue);
        }
        return "";
    }

    public String getString(String key) {
        return getString(key, "");
    }

    public boolean getBoolean(String key, boolean defValue) {
        if (mPreferences != null) {
            return mPreferences.getBoolean(key, defValue);
        }
        return false;
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public int getInt(String key, int defValue) {
        if (mPreferences != null) {
            return mPreferences.getInt(key, defValue);
        }
        return 0;
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public boolean putStr(String key, String var) {
        return mEditor.putString(key, var).commit();
    }

    public boolean putBoolean(String key, boolean var) {
        return mEditor.putBoolean(key, var).commit();
    }

    public boolean putInt(String key, int var) {
        return mEditor.putInt(key, var).commit();
    }
}
