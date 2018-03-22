package com.shdnxc.cn.activity.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shdnxc.cn.activity.R;
import com.shdnxc.cn.activity.view.SwitchButton;

/**
 * Created by Zheng Jungen on 2017/3/30.
 */
public class AppSettingActivity extends Activity implements View.OnClickListener {
    private ImageView mBack;
    private TextView mTitle;
    private PowerManager.WakeLock mWakeLock = null;
    private SwitchButton mLightSwith, mMsgSwith;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_app_set);
        initviews();
        mTitle.setText("设置");
        mBack.setOnClickListener(this);
   /*     mMsgSwith.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked == true) {
                    Toast.makeText(AppSettingActivity.this, "敬请期待亲", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AppSettingActivity.this, "敬请期待哦", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
        mLightSwith.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked == true) {
                    if (mWakeLock != null) {
                        mWakeLock.release();
                        mWakeLock = null;
                    }
                    PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
                    if (powerManager != null) {
                        mWakeLock = powerManager.newWakeLock(
                                PowerManager.PARTIAL_WAKE_LOCK, "MicRecording");
                        mWakeLock.setReferenceCounted(false);
                        mWakeLock.acquire();
                    }
                } else if (isChecked == false) {
                    if (mWakeLock != null) {
                        releaseWakeLock();
                    }
                }
            }
        });
    }

    private void initviews() {
        mTitle = (TextView) findViewById(R.id.tv_title);
        mBack = (ImageView) findViewById(R.id.iv_find_back);
        mLightSwith = (SwitchButton) findViewById(R.id.light_switch);
    /*    mMsgSwith = (SwitchButton) findViewById(R.id.msg_switch);*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_find_back:
                AppSettingActivity.this.finish();
                break;
        }
    }

    // 释放设备电源锁
    public void releaseWakeLock() {
        if (null != mWakeLock) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }

    @Override
    protected void onDestroy() {
        if (mWakeLock != null) {
            this.releaseWakeLock();
        }
        super.onDestroy();
    }
}
