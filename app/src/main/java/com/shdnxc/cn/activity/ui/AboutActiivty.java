package com.shdnxc.cn.activity.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.shdnxc.cn.activity.R;

/**
 * Created by Zheng Jungen on 2017/3/22.
 */
public class AboutActiivty extends Activity implements View.OnClickListener {

    private ImageView ivBack;
    private RelativeLayout re_back_down;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initviews();
        iniLisenter();
    }

    private void iniLisenter() {
        re_back_down.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    private void initviews() {
        re_back_down = (RelativeLayout) findViewById(R.id.re_back_down);
        ivBack = (ImageView) findViewById(R.id.ivBack);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.re_back_down:
                AboutActiivty.this.finish();
                break;
            case R.id.ivBack:
                AboutActiivty.this.finish();
                break;
        }
    }
}
