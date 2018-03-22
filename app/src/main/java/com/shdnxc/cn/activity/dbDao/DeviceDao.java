package com.shdnxc.cn.activity.dbDao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zheng Jungen on 2017/5/2.
 */
public class DeviceDao {
    RecordSQLiteOpenHelper devicesHelper;
    SQLiteDatabase mDevicesDb;

    public DeviceDao(Context context) {
        devicesHelper = new RecordSQLiteOpenHelper(context);
    }

    //添加搜索记录
    public void addRecords(String record) {

        if (!isHasRecord(record)) {
            mDevicesDb = devicesHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", record);
            //添加
            mDevicesDb.insert("devices", null, values);
            //关闭
            mDevicesDb.close();
        }
    }

    //判断是否含有该搜索记录
    public boolean isHasRecord(String record) {
        boolean isHasRecord = false;
        mDevicesDb = devicesHelper.getReadableDatabase();
        Cursor cursor = mDevicesDb.query("devices", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            if (record.equals(cursor.getString(cursor.getColumnIndexOrThrow("name")))) {
                isHasRecord = true;
            }
        }
        //关闭数据库
        mDevicesDb.close();
        return isHasRecord;
    }

    //获取全部搜索记录
    public List<String> getRecordsList() {
        List<String> devicesList = new ArrayList<>();
        mDevicesDb = devicesHelper.getReadableDatabase();
        Cursor cursor = mDevicesDb.query("devices", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            devicesList.add(name);
        }
        //关闭数据库
        mDevicesDb.close();
        return devicesList;
    }

    //模糊查询
    public List<String> querySimlarRecord(String record) {
        String queryStr = "select * from devices where name like '%" + record + "%' order by name ";
        List<String> similarRecords = new ArrayList<>();
        Cursor cursor = devicesHelper.getReadableDatabase().rawQuery(queryStr, null);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            similarRecords.add(name);
        }
        return similarRecords;
    }

    //清空搜索记录
    public void deleteAllRecords() {
        mDevicesDb = devicesHelper.getWritableDatabase();
        mDevicesDb.execSQL("delete from devices");

        mDevicesDb.close();
    }
}
