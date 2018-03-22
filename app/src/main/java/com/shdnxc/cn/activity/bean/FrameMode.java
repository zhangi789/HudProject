package com.shdnxc.cn.activity.bean;

/**
 * Created by Zheng Jungen on 2017/2/23.
 */
public class FrameMode {
    //A A 4a 03c1 4547222a 1812  00 00  32c8  ffff  00000000
    //帧 头
    private String mHead;
    //数据的总长度
    private String mAllLength;
    private String mCrcCode;
    private String mMagicCode;
    //数据短信息 1-2
    private String mVersionInfo;
    /**
     * 3
     * mBootHudInfo  不是整字节  1个字节长度
     */
    private String mBootHudInfo;
    //4
    private String mTbtInfo;
    //5-6
    private String mNextRoadDistance;
    //7-8  目的地距离
    private String mDestInfo;
    /**
     * 9-12  4个字节
     */
    private String mDestComp;
    //13-14
    private String mCurCarSpeed;
    //15_16
    private String mLimitCarSpeed;
    //17  辅助诱导信息
    private String mAssInfo;
    //18-19  车道信息
    private String mCarRoadInfo;
    //20  道路信息信息
    private String mRoadLimitInfo;
    //21-14  保留信息
    private String mStoreReserveInfo;

    //25 音量设置
    private String mVoiceSett;
    /**
     * 26  不是整字节  1个字节长度
     * <p>
     * 主题设置
     */

    private String mThemeSett;

    //27-28  保留位
    private String mStoreReserveInfo2;

    //29-48  下一个路口名称  20
    private String mNextRoadName;
    //49-68  当前路口名称   20
    private String mCurRoadName;
    // 69 北京时间
    private String mSysTimeInfo;
    //帧 尾
    private String mEnd;

    private boolean changed;

    public FrameMode(String mHead, String mAllLength, String mCrcCode, String mMagicCode, String mVersionInfo, String mBootHudInfo, String mTbtInfo, String mNextRoadDistance, String mDestInfo, String mDestComp, String mCurCarSpeed, String mLimitCarSpeed, String mAssInfo, String mCarRoadInfo, String mRoadLimitInfo, String mStoreReserveInfo, String mVoiceSett, String mThemeSett, String mStoreReserveInfo2, String mNextRoadName, String mCurRoadName, String mSysTimeInfo, String mEnd) {
        this.mHead = mHead;
        this.mAllLength = mAllLength;
        this.mCrcCode = mCrcCode;
        this.mMagicCode = mMagicCode;
        this.mVersionInfo = mVersionInfo;
        this.mBootHudInfo = mBootHudInfo;
        this.mTbtInfo = mTbtInfo;
        this.mNextRoadDistance = mNextRoadDistance;
        this.mDestInfo = mDestInfo;
        this.mDestComp = mDestComp;
        this.mCurCarSpeed = mCurCarSpeed;
        this.mLimitCarSpeed = mLimitCarSpeed;
        this.mAssInfo = mAssInfo;
        this.mCarRoadInfo = mCarRoadInfo;
        this.mRoadLimitInfo = mRoadLimitInfo;
        this.mStoreReserveInfo = mStoreReserveInfo;
        this.mVoiceSett = mVoiceSett;
        this.mThemeSett = mThemeSett;
        this.mStoreReserveInfo2 = mStoreReserveInfo2;
        this.mNextRoadName = mNextRoadName;
        this.mCurRoadName = mCurRoadName;
        this.mSysTimeInfo = mSysTimeInfo;
        this.mEnd = mEnd;
    }


    public String getmHead() {
        return mHead;
    }

    public void setmHead(String mHead) {
        this.mHead = mHead;
    }

    public String getmAllLength() {
        return mAllLength;
    }

    public void setmAllLength(String mAllLength) {
        this.mAllLength = mAllLength;
    }

    public String getmCrcCode() {
        return mCrcCode;
    }

    public void setmCrcCode(String mCrcCode) {
        this.mCrcCode = mCrcCode;
    }

    public String getmMagicCode() {
        return mMagicCode;
    }

    public void setmMagicCode(String mMagicCode) {
        this.mMagicCode = mMagicCode;
    }

    public String getmVersionInfo() {
        return mVersionInfo;
    }

    public void setmVersionInfo(String mVersionInfo) {
        this.mVersionInfo = mVersionInfo;
    }

    public String getmBootHudInfo() {
        return mBootHudInfo;
    }

    public void setmBootHudInfo(String mBootHudInfo) {
        this.mBootHudInfo = mBootHudInfo;
    }

    public String getmTbtInfo() {
        return mTbtInfo;
    }

    public void setmTbtInfo(String mTbtInfo) {
        this.mTbtInfo = mTbtInfo;
    }
    public String getmNextRoadDistance() {
        return mNextRoadDistance;
    }

    public void setmNextRoadDistance(String mNextRoadDistance) {
        this.mNextRoadDistance = mNextRoadDistance;
    }

    public String getmDestInfo() {
        return mDestInfo;
    }

    public void setmDestInfo(String mDestInfo) {
        this.mDestInfo = mDestInfo;
    }
    public String getmDestComp() {
        return mDestComp;
    }
    public void setmDestComp(String mDestComp) {
        this.mDestComp = mDestComp;
    }
    public String getmCurCarSpeed() {
        return mCurCarSpeed;
    }
    public void setmCurCarSpeed(String mCurCarSpeed) {
        this.mCurCarSpeed = mCurCarSpeed;
    }
    public String getmLimitCarSpeed() {
        return mLimitCarSpeed;
    }
    public void setmLimitCarSpeed(String mLimitCarSpeed) {
        this.mLimitCarSpeed = mLimitCarSpeed;
    }
    public String getmAssInfo() {
        return mAssInfo;
    }
    public void setmAssInfo(String mAssInfo) {
        this.mAssInfo = mAssInfo;
    }

    public String getmCarRoadInfo() {
        return mCarRoadInfo;
    }

    public void setmCarRoadInfo(String mCarRoadInfo) {
        this.mCarRoadInfo = mCarRoadInfo;
    }

    public String getmRoadLimitInfo() {
        return mRoadLimitInfo;
    }

    public void setmRoadLimitInfo(String mRoadLimitInfo) {
        this.mRoadLimitInfo = mRoadLimitInfo;
    }

    public String getmStoreReserveInfo() {
        return mStoreReserveInfo;
    }

    public void setmStoreReserveInfo(String mStoreReserveInfo) {
        this.mStoreReserveInfo = mStoreReserveInfo;
    }

    public String getmVoiceSett() {
        return mVoiceSett;
    }

    public void setmVoiceSett(String mVoiceSett) {
        this.mVoiceSett = mVoiceSett;
    }

    public String getmThemeSett() {
        return mThemeSett;
    }

    public void setmThemeSett(String mThemeSett) {
        this.mThemeSett = mThemeSett;
    }

    public String getmStoreReserveInfo2() {
        return mStoreReserveInfo2;
    }

    public void setmStoreReserveInfo2(String mStoreReserveInfo2) {
        this.mStoreReserveInfo2 = mStoreReserveInfo2;
    }

    public String getmNextRoadName() {
        return mNextRoadName;
    }

    public void setmNextRoadName(String mNextRoadName) {
        this.mNextRoadName = mNextRoadName;
    }

    public String getmCurRoadName() {
        return mCurRoadName;
    }

    public void setmCurRoadName(String mCurRoadName) {
        this.mCurRoadName = mCurRoadName;
    }

    public String getmEnd() {
        return mEnd;
    }

    public void setmEnd(String mEnd) {
        this.mEnd = mEnd;
    }

    public String getmSysTimeInfo() {
        return mSysTimeInfo;
    }

    public void setmSysTimeInfo(String mSysTimeInfo) {
        this.mSysTimeInfo = mSysTimeInfo;
    }

    @Override
    public String toString() {
        return mHead +
                mAllLength +
                mCrcCode +
                mMagicCode +
                mVersionInfo +
                mBootHudInfo +
                mTbtInfo +
                mNextRoadDistance +
                mDestInfo +
                mDestComp +
                mCurCarSpeed +
                mLimitCarSpeed +
                mAssInfo +
                mCarRoadInfo +
                mRoadLimitInfo +
                mStoreReserveInfo +
                mVoiceSett +
                mThemeSett +
                mStoreReserveInfo2 +
                mNextRoadName +
                mCurRoadName +
                mSysTimeInfo +
                mEnd;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }
}
