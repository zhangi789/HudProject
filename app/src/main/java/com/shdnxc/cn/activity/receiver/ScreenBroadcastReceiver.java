package com.shdnxc.cn.activity.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.shdnxc.cn.activity.ui.OnePixelActivity;

/**
 * Created by Android on 2017/5/9.
 */

public class ScreenBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "MyLog";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action) {
            case Intent.ACTION_SCREEN_ON: { //
                Log.i(TAG, "screen_on");
                // 关闭一像素Activity
                if (OnePixelActivity.instance != null) {
                    OnePixelActivity.instance.finish();
                }
                break;
            }
            case Intent.ACTION_SCREEN_OFF: {
                Log.i(TAG, "screen_off");
                // 开启一像素Activity
                Intent activityIntent = new Intent(context, OnePixelActivity.class);
                activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(activityIntent);
                break;
            }
            default:
                break;
        }
    }
}
