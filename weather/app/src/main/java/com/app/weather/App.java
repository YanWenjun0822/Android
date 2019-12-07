package com.app.weather;

import android.app.Application;

import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * @author: 105032016090颜文君
 * @description： 初始化网络请求
 * @data: 2019/5/25
 */


public class App extends Application {
    private static App sApp;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        init();//初始化okHttpUtils 网络请求框架
    }

    private void init() {
        Logger.init("weather");
        initNet();
    }

    private void initNet() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)//链接超时
                .readTimeout(10000L, TimeUnit.MILLISECONDS)//读取超时
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    public static App getInstance() {
        return sApp;
    }
}
