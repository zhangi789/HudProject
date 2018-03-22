package com.shdnxc.cn.activity.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;

import com.baidu.mapapi.SDKInitializer;
import com.lzy.okgo.OkGo;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * ***********************************************************
 * Created by 张海洋 on 2017/1/16
 * name: 全局的初始化操作
 * overview: ImageLoader的初始化，网络的加载类， 地图， 三方sdk初始化操作
 * usage:
 * *************************************************************
 */
public class IAPI extends Application {
    public static int mScreenHeight;
    public static int mScreenWidth;
    public boolean isBackApp;
    /**
     * 1為百度，2為高德
     */
    public static String WHICH_MAP = "1";

    public static Map<String, Activity> destoryMap;
    /**
     * log的开关
     * true:打印log   一般测试时候，改为true
     * false:关闭log  上线时候改为false
     */
    public static boolean isDebug = false;

    public boolean isScann() {
        return isScann;
    }

    public void setScann(boolean scann) {
        isScann = scann;
    }

    public boolean isScann;

    public boolean isBackApp() {
        return isBackApp;
    }

    public void setBackApp(boolean backApp) {
        isBackApp = backApp;
    }

    public static IAPI instances;

    @Override
    public void onCreate() {
        super.onCreate();
        initScreen();
        SDKInitializer.initialize(this);
        instances = this;
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        isBackApp = false;
        isScann = false;
        destoryMap = new HashMap<>();
        OkGo.init(this);
        try {
            //以下都不是必须的，根据需要自行选择,一般来说只需要 debug,缓存相关,cookie相关的 就可以了
            OkGo.getInstance()
                    //如果使用默认的 60秒,以下三行也不需要传
                    .setConnectTimeout(6000)  //全局的连接超时时间
                    .setReadTimeOut(6000)     //全局的读取超时时间
                    .setWriteTimeOut(6000)    //全局的写入超时时间
                    //可以全局统一设置超时重连次数,默认为三次,那么最差的情况会请求4次(一次原始请求,三次重连请求),不需要可以设置为0
                    .setRetryCount(3);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initScreen() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        mScreenHeight = displayMetrics.heightPixels;
        mScreenWidth = displayMetrics.widthPixels;
    }

    /**
     * 判断是否有网
     *
     * @param context
     * @return
     */
    public static boolean isNet(Context context) {
        boolean bisConnFlag = false;
        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = conManager.getActiveNetworkInfo();
        if (network != null) {
            bisConnFlag = conManager.getActiveNetworkInfo().isAvailable();
        }
        return bisConnFlag;
    }

    /**
     * 添加到销毁队列
     *
     * @param activity 要销毁的activity
     */

    public static void addDestoryActivity(String activityName, Activity activity) {
        destoryMap.put(activityName, activity);
    }

    public static void removeDestoryActivity(String activity) {


        destoryMap.remove(activity);

    }

    public static String getWhichMap() {
        return WHICH_MAP;
    }

    public static void setWhichMap(String whichMap) {
        WHICH_MAP = whichMap;
    }

    /**
     * 销毁指定Activity
     */
    public static void destoryActivity(String activityName) {
        Set<String> keySet = destoryMap.keySet();
        for (String key : keySet) {
            if (activityName == key) {
                destoryMap.get(key).finish();
                destoryMap.remove(activityName);
            }

        }
    }


}
