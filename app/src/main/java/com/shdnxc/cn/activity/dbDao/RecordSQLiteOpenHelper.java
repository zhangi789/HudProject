package com.shdnxc.cn.activity.dbDao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Zheng Jungen on 2017/5/2.
 */
public class RecordSQLiteOpenHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "addressrecord.db";
    private final static int DB_VERSION = 1;

    public RecordSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlStr = "CREATE TABLE IF NOT EXISTS records (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT  NOT NULL);";
        String mDeviceStr = "CREATE TABLE IF NOT EXISTS devices (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);";
        db.execSQL(sqlStr);
        db.execSQL(mDeviceStr);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
