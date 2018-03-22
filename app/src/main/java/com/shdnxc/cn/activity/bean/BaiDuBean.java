package com.shdnxc.cn.activity.bean;

/**
 * Created by Zheng Jungen on 2017/4/17.
 */
public class BaiDuBean {

    private String addresName;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String address;
    private double  mLatitude;
    private double mLongitude;

    public BaiDuBean(String address ,String addresName, double mLatitude, double mLongitude) {
        this.addresName = addresName;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.address = address;
    }

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
}
