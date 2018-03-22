package com.shdnxc.cn.activity.Utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Created by Zheng Jungen on 2017/2/10.
 */
public class HandlerUtil {
    public static final int NORMAL_FROM_HUD_TO_APP_DATA = 5;
    public static final int NORMAL_FROM_APP_TO_HUD_DATA = 4;
    public static final int  Take_PHOTE=12;
    public static final int  SELECT_PHOTE=13;
    public static final String Take_PHOTE_DATA = "took_phote";
    public static final String EXTRA_DATA = "extra_data";
    public static final String SELECT_PHOTE_DATA = "sele_phote";
    public static final String EXTRA_MAC = "extra_mac";
    public static final String EXTRA_SEND = "send_data";
    public static void handleMsg(Handler mHandler, int what, Bundle data){
        Message msg = new Message();
        msg.what = what;
        msg.setData(data);
        mHandler.sendMessage(msg);
    }
}
