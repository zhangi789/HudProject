package com.shdnxc.cn.activity.base;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by 张海洋 on 2017/1/16
 * project_name: HudProject
 * function:
 */
public  abstract class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    /**
     * 初始化控件
     */
    protected abstract void initView();
    /**
     * 初始化数据
     */
    protected abstract void initData();
    /**
     * 设置监听
     */
    protected abstract void initListener();



    public void showShortToast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

}
