package com.app.weather.weather.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.weather.BaseFragment;
import com.app.weather.R;
import com.app.weather.weather.adapter.DayWeatherAdapter;
import com.app.weather.weather.adapter.WeatherAdapter;
import com.app.weather.bean.WeatherData;
import com.app.weather.http.WeatherRequst;
import com.app.weather.utils.ToastUtil;
import com.app.weather.utils.WeatherUtils;
import com.orhanobut.logger.Logger;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.Call;

/**
 * Created by wangshuo on 16/12/8.
 */

public class CityPageFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private String mCity;

    private RecyclerView mRecyclerView;
    private RecyclerView mDayRecyclerView;
    private List<WeatherData.ResultBean.DataBean.F3hBean.TemperatureBean> mTemperature = new ArrayList<>();
    private WeatherAdapter mWeatherAdapter;
    private List<WeatherData.ResultBean.DataBean.WeatherBeanX> mWeather = new ArrayList<>();
    private HeaderAndFooterWrapper mDayWeatherAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mTextViewCity, mTextViewWeather, mTextViewDu,
            mTextViewNow, mTextViewMax,
            mTextViewMin, mTextViewNowDate;
    private View mHeaderViews;
    private LinearLayout mLayoutBg;
    private String mWeatherInfo;
    private String mTemperatureInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle =
                getArguments();
        if (bundle != null) {
            mCity = bundle.getString("city");
        }

    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_citypager;
    }

    @Override
    protected void initViews(View view) {


        mDayRecyclerView = findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = findViewById(R.id.swipe_layout);

        mHeaderViews = mLayoutInflater.inflate(R.layout.layout_banner, mRootView, false);
        mRecyclerView = (RecyclerView) mHeaderViews.findViewById(R.id.recycler_weather);
        mTextViewCity = (TextView) mHeaderViews.findViewById(R.id.tv_city);
        mTextViewWeather = (TextView) mHeaderViews.findViewById(R.id.tv_weather);
        mTextViewDu = (TextView) mHeaderViews.findViewById(R.id.tv_du);
        mTextViewNow = (TextView) mHeaderViews.findViewById(R.id.tv_now);
        mTextViewMax = (TextView) mHeaderViews.findViewById(R.id.tv_max);
        mTextViewMin = (TextView) mHeaderViews.findViewById(R.id.tv_min);
        mTextViewNowDate = (TextView) mHeaderViews.findViewById(R.id.tv_nowdate);

        mLayoutBg = (LinearLayout) findViewById(R.id.bg_layout);
    }

    @Override
    protected void initDatas() {
        mTextViewCity.setText(mCity);
        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                getData();
            }
        }, 400);
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        mTextViewNowDate.setText(month + 1 + "/" + day);
    }

    @Override
    protected void setEvents() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        mDayRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DayWeatherAdapter adapter = new DayWeatherAdapter(getActivity(), R.layout.item_day_weather, mWeather);
        mDayWeatherAdapter = new HeaderAndFooterWrapper(adapter);
        mDayWeatherAdapter.addHeaderView(mHeaderViews);

        mDayRecyclerView.setAdapter(mDayWeatherAdapter);

        mRecyclerView.setLayoutManager(layoutManager);
        mWeatherAdapter = new WeatherAdapter(getActivity(), R.layout.item_weather, mTemperature);
        mRecyclerView.setAdapter(mWeatherAdapter);


        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    /**
     * 获取数据
     */
    private void getData() {
        WeatherRequst.getData(mCity, new WeatherRequst.HttpCallBack() {

            @Override
            public void onResponse(WeatherData response, int id) {
                success(response);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void success(WeatherData response) {
        mSwipeRefreshLayout.setRefreshing(false);
        Logger.e(response.toString());
        if (response.getError_code() != 0) {
            ToastUtil.showToastLong(response.getReason());
        } else {
            WeatherData.ResultBean.DataBean data = response.getResult().getData();
            WeatherData.ResultBean.DataBean.RealtimeBean realtime = data.getRealtime();


            //7日天气
            if (!mWeather.isEmpty() && mWeather.size() > 0) mWeather.clear();

            mWeather.addAll(data.getWeather());


            mDayWeatherAdapter.notifyDataSetChanged();

            WeatherData.ResultBean.DataBean.WeatherBeanX.InfoBeanX
                    info = mWeather.get(0).getInfo();

            //实时天气
            if (!mTemperature.isEmpty() && mTemperature.size() > 0) mTemperature.clear();
            mTemperature.addAll(data.getF3h().getTemperature());
            String s = WeatherUtils.toDateByWeather(info);
            mWeatherAdapter.setImgRid(WeatherUtils.setImg(WeatherUtils.toDateByWeather(info)));

            mWeatherAdapter.notifyDataSetChanged();
            setTopData(realtime);
        }
    }

    private void setTopData(WeatherData.ResultBean.DataBean.RealtimeBean b) {
        mTextViewCity.setText(b.getCity_name());
        mWeatherInfo = b.getWeather().getInfo();
        mTextViewWeather.setText(mWeatherInfo);
        mTemperatureInfo = b.getWeather().getTemperature();
        mTextViewDu.setText(mTemperatureInfo+"°");
        mTextViewNow.setText(WeatherUtils.numberToString(b.getWeek()));
        List<WeatherData.ResultBean.DataBean.F3hBean.TemperatureBean> temp = new ArrayList<>();
        temp.addAll(mTemperature);
        Collections.sort(temp, new Comparator<WeatherData.ResultBean.DataBean.F3hBean.TemperatureBean>() {
            @Override
            public int compare(WeatherData.ResultBean.DataBean.F3hBean.TemperatureBean
                                       o1,
                               WeatherData.ResultBean.DataBean.F3hBean.TemperatureBean o2) {
                return Integer.parseInt(o1.getJb()) - Integer.parseInt(o2.getJb());
            }
        });
        mTextViewMax.setText(mWeather.get(0).getInfo().getDay().get(2));
        mTextViewMin.setText(temp.get(0).getJb());

        int bgImg = WeatherUtils.setLayoutBgImg(b.getWeather().getInfo());
//        ((MainActivity)getActivity()).getLayoutBg().setBackgroundResource(bgImg);
        mLayoutBg.setBackgroundResource(bgImg);
    }


    public static CityPageFragment newInstance(Bundle bundle) {
        CityPageFragment fragment = new CityPageFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }, 500);
    }



    public String getCity() {
        return mCity;
    }

    public String getWeatherInfo() {
        return mWeatherInfo;
    }

    public String getTemperatureInfo() {
        return mTemperatureInfo;
    }

}
