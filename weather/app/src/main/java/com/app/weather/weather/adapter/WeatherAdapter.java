package com.app.weather.weather.adapter;

import android.content.Context;

import com.app.weather.R;
import com.app.weather.bean.WeatherData;
import com.app.weather.utils.Util;
import com.orhanobut.logger.Logger;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.Date;
import java.util.List;

/**
 * Created by wangshuo on 16/12/8.
 */

public class WeatherAdapter extends CommonAdapter<WeatherData.ResultBean.DataBean.F3hBean.TemperatureBean> {
    private int imgRid = R.mipmap.ic_dy;

    public WeatherAdapter(Context context, int layoutId, List<WeatherData.ResultBean.DataBean.F3hBean.TemperatureBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, WeatherData.ResultBean.DataBean.F3hBean.TemperatureBean b, int position) {
        Date date = Util.stringToDate(b.getJg(), "yyyyMMddHHmmss");
        holder.setText(R.id.tv_time, date == null ? "" : String.valueOf(date.getHours()) + "时")
                .setText(R.id.tv_du, b.getJb() + "°").setImageResource(R.id.iv_state, getImgRid());
    }

    public int getImgRid() {
        Logger.d(imgRid);
        return imgRid;
    }
    public void setImgRid(int imgRid) {
        this.imgRid = imgRid;
    }
}
