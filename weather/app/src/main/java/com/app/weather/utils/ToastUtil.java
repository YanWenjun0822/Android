package com.app.weather.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.app.weather.App;

/**
 * @author: 105032016090颜文君
 * @description： ToastUtils 提示工具类
 * @data: 2019/5/25
 */

public class ToastUtil {
    private static Context sContext = App.getInstance().getBaseContext();
    private static Toast toast;

    /**
     *
     * @param str 短时间显示Toast
     */
    public static void showToastShort( String str) {
        if (toast == null) {
            toast = Toast.makeText(sContext, str, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 20);
        } else {
            toast.setText(str);
        }
        toast.show();
    }

    /**
     *
     * @param str 长时间显示Toast
     */
    public static void showToastLong( String str) {
        if (toast == null) {
            toast = Toast.makeText(sContext, str, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 0, 20);
        } else {
            toast.setText(str);
        }
        toast.show();
    }
}
