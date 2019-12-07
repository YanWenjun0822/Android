package com.app.weather.weather.adapter;

import android.content.Context;

import com.app.weather.R;
import com.app.weather.bean.Citys;
import com.app.weather.utils.WeatherUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by wangshuo on 16/12/9.
 */

public class CitysAdapter extends CommonAdapter<Citys> {
    public CitysAdapter(Context context, int layoutId, List<Citys> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, Citys citys, int position) {
        if (position == 0) {
            holder.setVisible(R.id.iv_loction, true);
        } else {
            holder.setVisible(R.id.iv_loction, false);
        }
        holder.setText(R.id.tv_city, citys.getCity())
                .setText(R.id.tv_du, citys.getDuMax() + "°")
                .setText(R.id.tv_weather, citys.getWeather())
                .setImageResource(R.id.iv_weather, WeatherUtils.setImg(citys.getWeather()));
    }
}
