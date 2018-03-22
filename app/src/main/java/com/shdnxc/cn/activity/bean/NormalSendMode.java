package com.shdnxc.cn.activity.bean;

/**
 * Created by Zheng Jungen on 2017/5/12.
 */
public class NormalSendMode {
    //A A 4a 03c1 4547222a 1812  00 00  32c8  ffff  00000000
    //帧 头  1
    private String mHead;
    //数据的总长度   1
    private String mAllLength;
    //CRC校验码  1
    private String mCrcCode;
    //Magic 码  1
    private String mMagicCode;
    //数据短信息 1-2
    private String mVersionInfo;

    /**
     * 3
     * mBootHudInfo  不是整字节  1个字节长度
     */
    private String mBootHudInfo;
    //4  Tbt
    private String mTbtInfo;
    //5-6  下一个路口距离
    private String mNextRoadDistance;
    //7-8  目的地剩余距离
    private String mDestInfo;
    /**
     * 9-12  4个字节
     */
    private String mDestComp;

    //13 当前车速
    private String mCurCarSpeed;
    //14  当前限速值
    private String mCurCarLimitSpped;
    //15   前方限速距离
    private String mForntLimitSpeedDistance;
    //16  电子眼类型和道路拥挤状态
    private String mCamereStyle;
    //17  当前距电子眼的距离
    private String mCur_Camere_Distance;
    //18-21  车道信息
    private String mCarRoadInfo;
    //22  诱导信息
    private String mInduceInfo;
    //23  音量值
    private String mVoiceSett;
    //24 主题设置
    private String mThemeSett;
    //25  当前可用车道信息
    private String mCur_Use_Car_info;
    //26
    private String mStoreReserveInfo2;
    //27-28 字体颜色
    private String mTextBgColor;

    //29-48   下一个路口名称   20
    private String mNextRoadName;

    //48-68   当前路口名称
    private String mCurRoadName;
    // 69 北京时间
    private String mSysTimeInfo;
    //帧 尾
    private String mEnd;
    private boolean changed;

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

    public String getmCurCarLimitSpped() {
        return mCurCarLimitSpped;
    }

    public void setmCurCarLimitSpped(String mCurCarLimitSpped) {
        this.mCurCarLimitSpped = mCurCarLimitSpped;
    }

    public String getmForntLimitSpeedDistance() {
        return mForntLimitSpeedDistance;
    }

    public void setmForntLimitSpeedDistance(String mForntLimitSpeedDistance) {
        this.mForntLimitSpeedDistance = mForntLimitSpeedDistance;
    }

    public String getmCamereStyle() {
        return mCamereStyle;
    }

    public void setmCamereStyle(String mCamereStyle) {
        this.mCamereStyle = mCamereStyle;
    }

    public String getmCur_Camere_Distance() {
        return mCur_Camere_Distance;
    }

    public void setmCur_Camere_Distance(String mCur_Camere_Distance) {
        this.mCur_Camere_Distance = mCur_Camere_Distance;
    }

    public String getmCarRoadInfo() {
        return mCarRoadInfo;
    }

    public void setmCarRoadInfo(String mCarRoadInfo) {
        this.mCarRoadInfo = mCarRoadInfo;
    }

    public String getmInduceInfo() {
        return mInduceInfo;
    }

    public void setmInduceInfo(String mInduceInfo) {
        this.mInduceInfo = mInduceInfo;
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

    public String getmCur_Use_Car_info() {
        return mCur_Use_Car_info;
    }

    public void setmCur_Use_Car_info(String mCur_Use_Car_info) {
        this.mCur_Use_Car_info = mCur_Use_Car_info;
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

    public String getmSysTimeInfo() {
        return mSysTimeInfo;
    }

    public void setmSysTimeInfo(String mSysTimeInfo) {
        this.mSysTimeInfo = mSysTimeInfo;
    }

    public String getmEnd() {
        return mEnd;
    }

    public void setmEnd(String mEnd) {
        this.mEnd = mEnd;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }


    public String getmTextBgColor() {
        return mTextBgColor;
    }

    public void setmTextBgColor(String mTextBgColor) {
        this.mTextBgColor = mTextBgColor;
    }

    public NormalSendMode(String mHead, String mAllLength, String mCrcCode, String mMagicCode, String mVersionInfo, String mBootHudInfo, String mTbtInfo, String mNextRoadDistance, String mDestInfo, String mDestComp, String mCurCarSpeed, String mCurCarLimitSpped, String mForntLimitSpeedDistance, String mCamereStyle, String mCur_Camere_Distance, String mCarRoadInfo, String mInduceInfo, String mVoiceSett, String mThemeSett, String mCur_Use_Car_info, String mStoreReserveInfo2, String mTextBgColor, String mNextRoadName, String mCurRoadName, String mSysTimeInfo, String mEnd) {
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
        this.mCurCarLimitSpped = mCurCarLimitSpped;
        this.mForntLimitSpeedDistance = mForntLimitSpeedDistance;
        this.mCamereStyle = mCamereStyle;
        this.mCur_Camere_Distance = mCur_Camere_Distance;
        this.mCarRoadInfo = mCarRoadInfo;
        this.mInduceInfo = mInduceInfo;

        this.mVoiceSett = mVoiceSett;
        this.mThemeSett = mThemeSett;
        this.mCur_Use_Car_info = mCur_Use_Car_info;
        this.mStoreReserveInfo2 = mStoreReserveInfo2;
        this.mTextBgColor = mTextBgColor;
        this.mNextRoadName = mNextRoadName;
        this.mCurRoadName = mCurRoadName;
        this.mSysTimeInfo = mSysTimeInfo;
        this.mEnd = mEnd;
    }

    @Override
    public String toString() {
        return mHead
                + mAllLength
                + mCrcCode
                + mMagicCode
                + mVersionInfo
                + mBootHudInfo
                + mTbtInfo
                + mNextRoadDistance
                + mDestInfo
                + mDestComp
                + mCurCarSpeed
                + mCurCarLimitSpped
                + mForntLimitSpeedDistance
                + mCamereStyle
                + mCur_Camere_Distance
                + mCarRoadInfo
                + mInduceInfo
                + mVoiceSett
                + mThemeSett
                + mCur_Use_Car_info
                + mStoreReserveInfo2
                + mTextBgColor
                + mNextRoadName
                + mCurRoadName
                + mSysTimeInfo
                + mEnd;

    }
}
