package com.app.weather.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author: 105032016090颜文君
 * @description: 使用网络状态的判断
 * @data: 2019/5/25
 */
public class NetWorkUtil {

    private static boolean hasMoblie;
    private static boolean hasWifi;
    private static boolean hasInternet;


    public static boolean isHasMoblie() {
        return hasMoblie;
    }

    public static void setHasMoblie(boolean hasMoblie) {
        NetWorkUtil.hasMoblie = hasMoblie;
    }

    public static boolean isHasWifi() {
        return hasWifi;
    }

    public static void setHasWifi(boolean hasWifi) {
        NetWorkUtil.hasWifi = hasWifi;
    }

    public static boolean isHasInternet() {
        return hasInternet;
    }

    public static void setHasInternet(boolean hasInternet) {
        NetWorkUtil.hasInternet = hasInternet;
    }

    public static boolean getInternetState(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (!wifiNetInfo.isConnected() && !mobNetInfo.isConnected()) {
            return false;
        }
        if (wifiNetInfo.isConnected() || mobNetInfo.isConnected()) {
            NetWorkUtil.setHasInternet(true);
            return true;
        }
        return false;
    }
}
