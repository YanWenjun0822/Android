package com.app.weather.weather;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.app.weather.BaseActivity;
import com.app.weather.R;
import com.app.weather.weather.adapter.CitysAdapter;
import com.app.weather.bean.Citys;
import com.app.weather.bean.WeatherData;
import com.app.weather.db.CitysDao;
import com.app.weather.events.RefeshEvent;
import com.app.weather.http.WeatherRequst;
import com.app.weather.utils.ToastUtil;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.miguelcatalan.materialsearchview.utils.AnimationUtil;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class CitysActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, MultiItemTypeAdapter.OnItemClickListener {

    public static final int RESULTCODE_REFRSH = 0X1110;
    public static final int RESULTCODE_SELECT = 0X1111;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Citys> mCitysList = new ArrayList<>();
    private EmptyWrapper mAdapter;
    private CitysDao mCitysDao;
    private Toolbar mToolbar;
    private MaterialSearchView mSearchView;
    private ProgressDialog mDialog;


    @Override
    protected int layoutId() {
        return R.layout.activity_citys;
    }

    @Override
    protected void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mSearchView = (MaterialSearchView) findViewById(R.id.search_view);
    }

    @Override
    protected void initDatas() {
        mCitysDao = new CitysDao(this);
        getData();
        CitysAdapter adapter = new CitysAdapter(this, R.layout.item_citys, mCitysList);
        mAdapter = new EmptyWrapper(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setEmptyView(R.layout.layout_empty);
        adapter.setOnItemClickListener(this);
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("搜索中");
    }

    private void getData() {
        Intent intent = getIntent();
        String city = intent.getStringExtra("city");
        String temperatureInfo = intent.getStringExtra("temperatureInfo");
        String weatherInfo = intent.getStringExtra("weatherInfo");

        if (!mCitysList.isEmpty() && mCitysList.size() > 0) mCitysList.clear();
        mCitysList.add(0, new Citys(city, weatherInfo, temperatureInfo, temperatureInfo));
        mCitysList.addAll(mCitysDao.findAllCity());
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void setEvents() {
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setOnRefreshListener(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mDialog.show();
                //Do some magic
                WeatherRequst.getData(query, new WeatherRequst.HttpCallBack() {
                    @Override
                    public void onResponse(WeatherData response, int id) {
                        querySuccess(response);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic

                AnimationUtil.fadeOutView(mSwipeRefreshLayout, 600);
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
//                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                AnimationUtil.fadeInView(mSwipeRefreshLayout, 600);
            }
        });


    }

    private void querySuccess(WeatherData response) {

        if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();

        if (response.getError_code() != 0) {
            ToastUtil.showToastLong(response.getReason());
        } else {
            mSearchView.closeSearch();
            WeatherData.ResultBean.DataBean.RealtimeBean b = response.getResult().getData().getRealtime();
            mCitysDao.add(new Citys(b.getCity_name(), b.getWeather().getInfo(),
                    b.getWeather().getTemperature(), b.getWeather().getTemperature()));
            Intent intent = getIntent();
            intent.putExtra("city", b.getCity_name());
            setResult(RESULT_OK, intent);
            finish();
        }
    }


    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }, 500);

    }   //返回

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            if (item.getItemId() == R.id.action_add) {

                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        getMenuInflater().inflate(R.menu.menu_citys, menu);
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_citys, menu);

        MenuItem item = menu.findItem(R.id.action_add);
        mSearchView.setMenuItem(item);

        return true;
    }


    @Override
    public void onBackPressed() {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        Intent intent = getIntent();
        intent.putExtra("position", position);
        setResult(RESULTCODE_SELECT, intent);
        finish();
    }
    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, final int position) {
        if (position == 0) return true;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("删除这个城市?");
        builder.setPositiveButton("取消", null);
        builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String city = mCitysList.get(position).getCity();

                mCitysDao.delete(mCitysList.get(position));
//                Intent intent = getIntent();
//                intent.putExtra("position", position);
//                intent.putExtra("city", city);
//                setResult(RESULTCODE_REFRSH, intent);
                RefeshEvent event = new RefeshEvent(position, city);
                EventBus.getDefault().post(event);

                mCitysList.remove(position);
                mAdapter.notifyDataSetChanged();
//                finish();
            }
        });
        builder.show();
        return true;
    }
}
