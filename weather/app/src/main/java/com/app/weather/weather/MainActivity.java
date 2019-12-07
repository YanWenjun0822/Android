package com.app.weather.weather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.app.weather.App;
import com.app.weather.BaseActivity;
import com.app.weather.R;
import com.app.weather.bean.Citys;
import com.app.weather.db.CitysDao;
import com.app.weather.events.RefeshEvent;
import com.app.weather.utils.LocationManager;
import com.app.weather.utils.NetWorkUtil;
import com.app.weather.utils.ToastUtil;
import com.app.weather.weather.adapter.CityPagerAdapter;
import com.app.weather.weather.fragments.CityPageFragment;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

import static com.app.weather.R.id.indicator;
import static com.app.weather.R.id.viewpager;

public class MainActivity extends BaseActivity implements BDLocationListener {


    public static final int REQUEST_CODE = 0x00001;
    private List<CityPageFragment> mFragments = new ArrayList<>();
    private ViewPager mViewPager;
    private CircleIndicator mIndicator;
    private ImageButton mImageButton;
    private Button mButtonRefesh;

    private CitysDao mCitysDao;
    private CityPagerAdapter mAdapter;
    //    private LinearLayout mLayoutBg;
    private LocationManager mLocationManager;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        transparentBar(this);
        mViewPager = (ViewPager) findViewById(viewpager);
        mIndicator = (CircleIndicator) findViewById(indicator);
        mImageButton = (ImageButton) findViewById(R.id.ib_setup);
        mButtonRefesh = (Button) findViewById(R.id.but_refesh);
//        mLayoutBg = (LinearLayout) findViewById(R.id.bg_layout);
    }


    @Override
    protected void initDatas() {

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在定位...");
        mProgressDialog.show();

        mLocationManager = new LocationManager(this);

        mCitysDao = new CitysDao(this);

        mAdapter = new CityPagerAdapter
                (getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mViewPager);
        mLocationManager.start();
        mViewPager.setOffscreenPageLimit(3);
    }

    private void getLocData() {
        List<Citys> citysList = mCitysDao.findAllCity();

        if (!citysList.isEmpty()) {
            //有数据解析
//            if (!citysList.isEmpty()) {
//                mFragments.clear();
            for (int i = 0; i < citysList.size(); i++) {
                Bundle bundle = new Bundle();
                bundle.putString("city", citysList.get(i).getCity());
                mFragments.add(CityPageFragment.newInstance(bundle));
            }
//            }
        }
    }

    @Override
    protected void setEvents() {
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CitysActivity.class);
                if (mFragments != null && mFragments.size() > 0) {
                    CityPageFragment pageFragment = mFragments.get(0);
                    String city = pageFragment.getCity();
                    String temperatureInfo = pageFragment.getTemperatureInfo();
                    String weatherInfo = pageFragment.getWeatherInfo();

                    intent.putExtra("city", city == null ? "" : city);
                    intent.putExtra("temperatureInfo", temperatureInfo == null ? "" : temperatureInfo);
                    intent.putExtra("weatherInfo", weatherInfo == null ? "" : weatherInfo);
                }
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        mButtonRefesh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonRefesh.setText("正在重试");
                mLocationManager.start();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefeshEvent event) {
        Logger.e(event.toString());
        String city = event.getCity();
        for (CityPageFragment f : mFragments) {
            if (f.getCity() != null && f.getCity().equals(city)) {
                boolean remove = mFragments.remove(f);
                if (remove) Logger.d(remove);
                mAdapter.notifyDataSetChanged();

                mIndicator.setViewPager(mViewPager);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE == requestCode) {
            switch (resultCode) {
                case RESULT_OK:
                    if (data != null) {
                        String city = data.getStringExtra("city");
                        Bundle bundle = new Bundle();
                        bundle.putString("city", city);
                        mFragments.add(CityPageFragment.newInstance(bundle));
                        mAdapter.notifyDataSetChanged();
                        int count = mAdapter.getCount();
                        mViewPager.setCurrentItem(count - 1);
                        mIndicator.setViewPager(mViewPager);
                    }
                    break;
                case CitysActivity.RESULTCODE_SELECT:
                    if (data != null) {
                        int position = data.getIntExtra("position", 0);
                        mViewPager.setCurrentItem(position);
                    }
                    break;

            }
        }
    }

//    public LinearLayout getLayoutBg() {
//        return mLayoutBg;
//    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {

        mLocationManager.stop();
        if (bdLocation != null) {
            String city = bdLocation.getCity();
            if (!TextUtils.isEmpty(city)) {

                if (!hasFragmentTo(city)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("city", city);
                    mFragments.add(CityPageFragment.newInstance(bundle));
                    getLocData();
                    mAdapter.notifyDataSetChanged();
                    mIndicator.setViewPager(mViewPager);
                    dismissDialog();
                    mButtonRefesh.setVisibility(View.GONE);
                }
            } else {

                dismissDialog();

                if (!NetWorkUtil.getInternetState(App.getInstance().getApplicationContext())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("网络");
                    builder.setMessage("当前网络不可用 请打开网络后重试");
                    builder.setPositiveButton("知道了", null);
                    builder.show();
                    mButtonRefesh.setVisibility(View.VISIBLE);
                    mButtonRefesh.setText("重试");
                    return;
                }
                ToastUtil.showToastLong(bdLocation.getLocType() + "  定位异常");
                if (mFragments.size() == 0) {
                    ToastUtil.showToastLong(bdLocation.getLocType() + "  定位异常，请手动选择城市");
                    startActivityForResult(new Intent(MainActivity.this, CitysActivity.class)
                            , REQUEST_CODE);
                }
            }
        }
    }

    private void dismissDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) mProgressDialog.dismiss();
    }

    private boolean hasFragmentTo(String city) {
        if (city.contains("市")) {
            int i = city.indexOf("市");
            city = city.substring(0, i);
        }
        for (CityPageFragment f : mFragments) {
            if (f.getCity() != null && f.getCity().equals(city)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }
}
