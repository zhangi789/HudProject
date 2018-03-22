package com.shdnxc.cn.activity.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shdnxc.cn.activity.R;
import com.shdnxc.cn.activity.Utils.BaseUtils;
import com.shdnxc.cn.activity.Utils.ConstantUils;
import com.shdnxc.cn.activity.view.MediaImageView;
import com.shdnxc.cn.activity.view.MediaImageView2;
import com.shdnxc.cn.activity.view.MediaImageView3;
import com.shdnxc.cn.activity.view.MediaImageView4;

/**
 * Created by Zheng Jungen on 2017/3/20.
 */
public class ObdActivity extends Activity implements View.OnClickListener {
    private ImageView mBack;
    private TextView mTitle;
    private ImageView mPower, mChassis, mBody, mCommunication;
    String obdBackData = "";
    private LinearLayout mContorl;
    String pData = null;
    String bData = null;
    String cData = null;
    String uData = null;
    private boolean isPower;
    private boolean isChassis;
    private boolean isBody;
    private boolean isCommunication;
    private Handler mMainHandler;
    private MediaImageView btn_body1;
    private MediaImageView2 btn_body2;
    private MediaImageView3 btn_body3;
    private MediaImageView4 btn_body4;
    private TextView tv_communication2, tv_di2, tv_body1, tv_power1;
    private Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent();
            String obdBackData = (String) msg.obj;
            Log.i("ASP", "ASP: " + obdBackData);
            pData = BaseUtils.getSiglP(obdBackData);
            Log.i("ASP", "pData: " + pData);
            bData = BaseUtils.getSiglB(obdBackData);
            Log.i("ASP", "bData: " + bData);
            cData = BaseUtils.getSiglC(obdBackData);
            Log.i("ASP", "cData: " + cData);
            uData = BaseUtils.getSiglU(obdBackData);
            Log.i("ASP", "uData: " + uData);
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (pData.length() != 1) {
                        btn_body1.setChecked(true);
                        tv_power1.setTextColor(Color.rgb(255,81,101));
                        isPower = true;
                        btn_body1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (isPower) {
                                    intent.putExtra("pdata", pData);
                                    intent.setClass(ObdActivity.this, BreakActivity.class);
                                    startActivity(intent);
                                }
                            }
                        });
                    } else {
                        btn_body1.setChecked(false);
                        tv_power1.setTextColor(Color.rgb(255,255,255));
                        isPower = false;
                    }
                    if (bData.length() != 1) {
                        btn_body2.setChecked(true);
                        tv_body1.setTextColor(Color.rgb(255,81,101));
                        isBody = true;
                        btn_body2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (isBody) {
                                    intent.putExtra("pdata", bData);
                                    intent.setClass(ObdActivity.this, BreakActivity.class);
                                    startActivity(intent);
                                }
                            }
                        });
                    } else {
                        btn_body2.setChecked(false);
                        tv_body1.setTextColor(Color.rgb(255,255,255));
                        isBody = false;
                    }
                    if (cData.length() != 1) {
                        btn_body3.setChecked(true);
                        isCommunication = true;
                        tv_di2.setTextColor(Color.rgb(255,81,101));

                        btn_body3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (isCommunication) {
                                    intent.putExtra("pdata", cData);
                                    intent.setClass(ObdActivity.this, BreakActivity.class);
                                    startActivity(intent);
                                }
                            }
                        });
                    } else {
                        btn_body3.setChecked(false);
                        tv_di2.setTextColor(Color.rgb(255,255,255));
                        isCommunication = false;
                    }
                    if (uData.length() != 1) {
                        btn_body4.setChecked(true);
                        isChassis = true;
                        tv_communication2.setTextColor(Color.rgb(255,81,101));
                        btn_body4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (isChassis) {
                                    intent.putExtra("pdata", uData);
                                    intent.setClass(ObdActivity.this, BreakActivity.class);
                                    startActivity(intent);
                                }
                            }
                        });
                    } else {
                        btn_body4.setChecked(false);
                        tv_communication2.setTextColor(Color.rgb(255,255,255));
                        isChassis = false;
                    }
                }
            });

        }
    };
    //将获得的数据接收  在updateReceivedData更新数据
    private final BroadcastReceiver bleIntentReceiver = new BroadcastReceiver() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            //成功的回掉
            if (ConstantUils.BLE_HUD_TO_APP.equals(action)) {

                String stringExtra = intent.getStringExtra(ConstantUils.BLE_HUD_TO_APP_KEY);

                String[] defInformation = BaseUtils.getArrayFromHudToApp(BaseUtils.removeSpace(stringExtra));
                //获得数据段信息   数据段信息处理
                String[] dataSegment = BaseUtils.getDataSegment(defInformation[4]);
                obdBackData = BaseUtils.getOBDDate(dataSegment[10]);
                Message msg = Message.obtain();
                msg.obj = obdBackData;
                mHander.sendMessage(msg);

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obd);
        mMainHandler = new Handler(getMainLooper());
        initviews();
        registerReceiver(bleIntentReceiver, bleIntentFilter());
        mTitle.setText("车况检测");
        Log.i("ASP", isPower + " ");
        initListener();
    }

    private void initListener() {
        mBack.setOnClickListener(this);
    }

    private void initviews() {
        mBack = (ImageView) findViewById(R.id.iv_find_back);
        mTitle = (TextView) findViewById(R.id.tv_title);
        btn_body1 = (MediaImageView) findViewById(R.id.btn_body1);
        btn_body2 = (MediaImageView2) findViewById(R.id.btn_body2);
        btn_body3 = (MediaImageView3) findViewById(R.id.btn_body3);
        btn_body4 = (MediaImageView4) findViewById(R.id.btn_body4);
        tv_power1 = (TextView) findViewById(R.id.tv_power1);
        tv_body1 = (TextView) findViewById(R.id.tv_body1);
        tv_di2 = (TextView) findViewById(R.id.tv_di2);
        tv_communication2 = (TextView) findViewById(R.id.tv_communication2);

    }

    private static IntentFilter bleIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantUils.BLE_HUD_TO_APP);
        return intentFilter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_find_back:
                ObdActivity.this.finish();
                break;

        }
    }
}
