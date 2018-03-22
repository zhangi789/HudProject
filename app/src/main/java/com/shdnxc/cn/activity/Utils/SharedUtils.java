package com.shdnxc.cn.activity.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * ***********************************************************
 * author: alex
 * time: 16/5/9 下午12:07
 * name:
 * overview:
 * usage:
 * *************************************************************
 */
public class SharedUtils {
    public static String SP_NAME = "config_name";
    public static SharedPreferences mSp;
    public static String SP_SPLASH="config_splash";

    public static String IS_FIRST="is_first";

    public static void saveBoolean(Context context, String key, boolean value) {
        if (mSp == null) {
            mSp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        mSp.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        if (mSp == null) {
            mSp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return mSp.getBoolean(key, defValue);
    }


    public static  void saveString(Context context, String key, String value){
        if (mSp == null) {
            mSp = context.getSharedPreferences(SP_SPLASH, Context.MODE_PRIVATE);
        }

        mSp.edit().putString(key,value).commit();
    }
    public static String getString(Context context, String key, String defValue) {
        if (mSp == null) {
            mSp = context.getSharedPreferences(SP_SPLASH, Context.MODE_PRIVATE);
        }
        return mSp.getString(key, defValue);
    }










}