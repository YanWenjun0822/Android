package com.app.weather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.app.weather.utils.NetWorkUtil;

/**
 * @author: 105032016090颜文君
 * @description: 监听网络的接收者
 * 需要权限：
 * <!-- 用于访问wifi网络信息，wifi信息会用于进行网络定位 -->
 * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
 * <!-- 获取运营商信息，用于支持提供运营商信息相关的接口 -->
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
 * <!-- 这个权限用于获取wifi的获取权限，wifi信息会用来进行网络定位 -->
 * <uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>
 *
 * @data: 2019/5/26
 */

public class NetWorkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //获取网络连接管理器
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //当前的移动网络连接信息
        NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        //当前的WIFI网络连接信息
        NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (!mobNetInfo.isConnected()) {
            NetWorkUtil.setHasMoblie(false);
        } else {
            NetWorkUtil.setHasMoblie(true);
        }

        if (!wifiNetInfo.isConnected()) {
            NetWorkUtil.setHasWifi(false);
        } else {
            NetWorkUtil.setHasWifi(true);
        }
        if (!wifiNetInfo.isConnected() && !mobNetInfo.isConnected()) {
            NetWorkUtil.setHasInternet(false);
        }
        if (wifiNetInfo.isConnected() || mobNetInfo.isConnected()) {
            NetWorkUtil.setHasInternet(true);
        }
    }
}
