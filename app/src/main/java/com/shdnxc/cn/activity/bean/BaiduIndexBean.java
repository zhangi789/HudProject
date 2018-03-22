package com.shdnxc.cn.activity.bean;

/**
 * Created by Zheng Jungen on 2017/5/2.
 */
public class BaiduIndexBean {

    private String addresName;
    private double  mLatitude;
    private double mLongitude;
    public String getAddresName() {
        return addresName;
    }

    public void setAddresName(String addresName) {
        this.addresName = addresName;
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }



    public BaiduIndexBean(String addresName, double mLatitude, double mLongitude) {
        this.addresName = addresName;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
    }
}
