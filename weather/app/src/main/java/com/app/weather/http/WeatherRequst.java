package com.app.weather.http;

import com.app.weather.App;
import com.app.weather.bean.WeatherData;
import com.app.weather.utils.NetWorkUtil;
import com.app.weather.utils.ToastUtil;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;

import okhttp3.Call;

/**
 * @author: 105032016090颜文君
 * @description: 请求网络访问调用接口访问天气数据
 * @data: 2019/5/26
 */

public final class WeatherRequst {

    //天气预报信息获取接口
    private final static String mUrl = "http://op.juhe.cn/onebox/weather/query";
    private final static String mKey = "7a9914bae16a2d9d43bb115177312fc2";

    public static void getData(String city, final HttpCallBack back) {
        if (!NetWorkUtil.getInternetState(App.getInstance().getApplicationContext())) {
            ToastUtil.showToastLong("网络不可用...");
            back.onError(null, null, -1);
            return;
        }
        OkHttpUtils.get().url(mUrl).addParams("cityname", city)
                .addParams("dtype", "json")
                .addParams("key", mKey).build().execute(new com.app.weather.callback.HttpCallBack<WeatherData>() {

            @Override
            public void onError(Call call, Exception e, int id) {//请求网络失败时 回调
                Logger.e("error....");
                ToastUtil.showToastLong("网络异常...");
                if (back != null) {
                    back.onError(call, e, id);
                }
            }

            @Override
            public void onResponse(WeatherData response, int id) {
                if (back != null) {
                    back.onResponse(response, id);
                }
            }
        });
    }

    public interface HttpCallBack
    {
        public void onResponse(WeatherData response, int id);

        public void onError(Call call, Exception e, int id);
    }
}
