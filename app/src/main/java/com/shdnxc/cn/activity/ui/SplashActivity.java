package com.shdnxc.cn.activity.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.shdnxc.cn.activity.MainActivity;
import com.shdnxc.cn.activity.R;
import com.shdnxc.cn.activity.Utils.ConstantUils;
import com.shdnxc.cn.activity.Utils.SharedUtils;
import com.shdnxc.cn.activity.app.IAPI;

/**
 * Created by Zheng Jungen on 2017/1/19.
 */
public class SplashActivity extends Activity {
    private String token;
    private String sn;
    IAPI mAPi;
    private boolean mShow;  //mShow: true: login; false: logout
    private boolean isBindshow; //isBindshow: true: Bindin; false: bindOut

    private Button btn_loginss;

    /**
     * 为了防止空指针   Token  默认为0  length=1,  大于1表示登录过
     *
     * @param savedInstanceState
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (!isTaskRoot()) {
            finish();
            return;
        }
        setContentView(R.layout.activity_splash);
        Log.i("create", "Splash onCreate: ");

        token = SharedUtils.getString(SplashActivity.this, ConstantUils.CU_LOGIN, "0");
        sn = SharedUtils.getString(SplashActivity.this, ConstantUils.CU_SPLASH, "0");
        mAPi = new IAPI();
        String mAppBackState = SharedUtils.getString(SplashActivity.this, "appback", "loginout");
        String mDeviceBindState = SharedUtils.getString(SplashActivity.this, "devicebindstate", "bindout");
        if (mAppBackState.equals("loginout")) {
            mAPi.setBackApp(false);
        } else {
            mAPi.setBackApp(true);
        }
        mShow = mAPi.isBackApp();
        Log.i("ASP", "SplashActivity mShow " + mShow);
        if (mDeviceBindState.equals("bindout")) {
            mAPi.setScann(false);
        } else {
            mAPi.setScann(true);
        }
        isBindshow = mAPi.isScann();
        Log.i("ASP", "SplashActivity isBindshow " + isBindshow);
//        setContentView(R.layout.activity_splash);
      /*  new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, BaiDuIndexActivity.class));
                SplashActivity.this.finish();
            }
        }, 1000);*/    //启动动画持续3秒钟
        if (token.equals("0")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//                    ActivityAnimation.SliderToFade(SplashActivity.this, LoginActivity.class);
                    SplashActivity.this.finish();

                }
            }, 1000);    //启动动画持续3秒钟


        } else if (token.length() > 1 && sn.equals("0")) {


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SplashActivity.this.finish();
                    startActivity(new Intent(SplashActivity.this, CameraActivity.class));

                }
            }, 1000);    //启动动画持续3秒钟

        } else if (token.length() > 1 && sn.length() > 1) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                    ActivityAnimation.SliderToFade(SplashActivity.this, MainActivity.class);
                    SplashActivity.this.finish();
                }
            }, 1000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("create", "Splash onDestory: ");
    }
}
