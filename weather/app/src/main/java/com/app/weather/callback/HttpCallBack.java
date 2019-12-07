package com.app.weather.callback;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.internal.$Gson$Types;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.callback.Callback;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Request;
import okhttp3.Response;
/**
 * @author: 105032016090颜文君
 * @description: OkHttp请求callBack
 *
 * @data: 2019/5/26
 */
public abstract class HttpCallBack<T> extends Callback<T> {
    private static final String TAG = "HttpCallBack";
    private final Gson gson;
    private final Type mType;

    public HttpCallBack() {
        gson = new GsonBuilder().create();
        mType = getSuperclassTypeParameter(getClass());

    }
    /*
     * 将Gson泛型类型T转换成Type类型
     * 根据T 获取Type
     */
    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    @Override
    public T parseNetworkResponse(Response response, int id) throws Exception {
        String str = response.body().string();
        Logger.e(TAG, "parseNetworkResponse  -->   string:" + str);
        Object o;
        try {
            //解析json 返回bean对象
            o = gson.fromJson(str, mType);
        } catch (JsonParseException e) {
            throw new JsonParseException("json parse error");
        }
        return (T) o;
    }

    @Override
    public boolean validateReponse(Response response, int id) {
        return super.validateReponse(response, id);
    }

    //执行之后
    @Override
    public void onAfter(int id) {
        Logger.e(TAG, "onAfter  -->  " + id);
//        UiUtils.hide();
    }

    //执行之前
    @Override
    public void onBefore(Request request, int id) {
        StringBuffer sb = new StringBuffer("onBefore  --> " + " id :" + id +
                "  headers:" + request.headers() != null ? "" : request.headers().toString());
        if (request != null && request.body() != null) {
            sb.append("Request  :" + request.toString());
        }
        Logger.e(TAG, sb.toString());
    }
}
