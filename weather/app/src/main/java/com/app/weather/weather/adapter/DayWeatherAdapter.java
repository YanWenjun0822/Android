package com.app.weather.weather.adapter;

import android.content.Context;

import com.app.weather.R;
import com.app.weather.bean.WeatherData;
import com.app.weather.utils.WeatherUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by wangshuo on 16/12/8.
 */

public class DayWeatherAdapter extends CommonAdapter<WeatherData.ResultBean.DataBean.WeatherBeanX> {


    public DayWeatherAdapter(Context context, int layoutId, List<WeatherData.ResultBean.DataBean.WeatherBeanX> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, WeatherData.ResultBean.DataBean.WeatherBeanX b, int position) {
        String date = b.getDate();
        String time = date.substring(5, date.length());
        time = time.replace("-", "/");

        holder.setText(R.id.tv_max, b.getInfo().getDay().get(2))
                .setText(R.id.tv_min, b.getInfo().getNight().get(2))
                .setText(R.id.tv_week, time + "星期" + b.getWeek())
                .setText(R.id.tv_nongl, b.getNongli())
                .setText(R.id.tv_weather, b.getInfo().getDay().get(1))
                .setImageResource(R.id.iv_weather, WeatherUtils.setImg(
                        WeatherUtils.toDateByWeather(b.getInfo())));
    }

}
