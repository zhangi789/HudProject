package com.shdnxc.cn.activity.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.shdnxc.cn.activity.R;


public class OnePixelActivity extends AppCompatActivity {
    private static final String TAG = "MyLog";

    public static OnePixelActivity instance = null;

    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_pixel);

        Window window = getWindow();
        // 放在左上角
        window.setGravity(Gravity.START | Gravity.TOP);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        // 宽高为1px
        layoutParams.width = 1;
        layoutParams.height = 1;
        // 起始坐标
        layoutParams.x = 0;
        layoutParams.y = 0;
        window.setAttributes(layoutParams);
        instance = this;
        Log.d(TAG, "OnePixelActivity onCreate");
//        Log.i(TAG, "是否为top activity: "+isTopActivity(this,"OnePixelActivity"));

    }


    @Override
    protected void onDestroy() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        Log.d(TAG, "activity onDestroy");
        instance = null;
//        Log.i(TAG, "是否为top activity: "+isTopActivity(this,"OnePixelActivity"));
        super.onDestroy();
    }

}

