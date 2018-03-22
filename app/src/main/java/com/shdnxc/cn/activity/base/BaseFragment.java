package com.shdnxc.cn.activity.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 张海洋 on 2016/6/9 0009
 * project_name: VolleyApp   create：下午 1:59
 * function: b
 */
public   abstract class BaseFragment   extends Fragment{
    protected Activity mActivity;

    /**\
     * 添加上下文
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mActivity = getActivity();

    }

    /**
     * 添加布局
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return loadXml(inflater);
    }

    /**
     * 初始化控件
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    /**
     * 初始化数据
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }
    /**
     * 初始化布局
     *
     * @param inflater
     * @return
     */
    public abstract View loadXml(LayoutInflater inflater);

    /**
     * 初始化控件
     *
     * @param view
     */
    protected abstract void initView(View view);
    /**
     * 初始化数据
     */
    protected abstract void initData();
}
