package com.app.weather.weather.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import com.app.weather.weather.fragments.CityPageFragment;

import java.util.List;

/**
 * Created by wangshuo on 16/12/8.
 */

public class CityPagerAdapter extends FragmentStatePagerAdapter {
    List<CityPageFragment> mFragments;

    public CityPagerAdapter(FragmentManager fm, List<CityPageFragment> fragments) {
        super(fm);
        this.mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
//    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
    @Override
    public int getCount() {
        return mFragments.size();
    }

    public void onDestroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);}
}
