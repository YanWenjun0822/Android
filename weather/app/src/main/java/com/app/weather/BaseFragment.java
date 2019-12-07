package com.app.weather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author: 105032016090颜文君
 * @description： 封装fragment
 * 当fragment和activity发生关联时调用
 * @data: 2019/5/25
 */

public abstract class BaseFragment extends Fragment {
    protected ViewGroup mRootView;
    protected LayoutInflater mLayoutInflater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        mRootView = (ViewGroup) inflater.inflate(layoutId(), container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setEvents();
        initDatas();

    }
    protected abstract int layoutId();
    /**
     * 初始化组件
     * @param view
     */
    protected abstract void initViews(View view);
    /**
     * 初始化数据
     */
    protected abstract void initDatas();
    /**
     * 初始化事件
     */
    protected abstract void setEvents();

    /**
     * 简化findViewById
     * @param id
     * @param <T>
     * @return
     */
    protected <T extends View> T findViewById(int id) {
        return (T) mRootView.findViewById(id);
    }
}
