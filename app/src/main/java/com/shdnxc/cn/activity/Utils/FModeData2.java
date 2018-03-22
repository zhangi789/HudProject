package com.shdnxc.cn.activity.Utils;

/**
 * Created by Zheng Jungen on 2017/2/23.
 */
public class FModeData2 {
    public static String m_Head = "AA";
    public static String m_AllLength = "4a";
    /* public static String m_CrcCode = "31c5";
     public static String m_MagicCode = "5053ffff";*/
    //数据短信息 1-2
    public static String m_CrcCode = "e6f4";
    public static String m_MagicCode = ConstantUils.GAODE_MAGIC_CODE;
    public static String m_VersionInfo = "0000";
    /**
     * 3
     * mBootHudInfo  不是整字节  1个字节长度
     */
    public static String m_BootHudInfo = "0E";
    //4   tbt
    public static String m_TbtInfo = "00";
    //5-6    下一个路口信息
    public static String m_NextRoadDistance = "ffff";
    //7-8  目的地 信息
    public static String m_DestInfo = "ffff";
    /**
     * 9-12  4个字节
     */
    public static String m_DestComp = "00000000";
    //13  当前车速
    public static String m_CurCarSpeed = "00";
    //  14   当前限速值
    public static String m_CurCarLimitSpped = "00";
    //15  前方限速距离
    public static String m_ForntLimitSpeedDistance = "00";
    //16  电子眼类型 道路拥挤状态
    public static String m_CamereStyle = "00";
    //17  当前距电子眼的距离
    public static String m_Cur_Camere_Distance = "00";
    //18-21 车道信息
    public static String m_CarRoadInfo = "00000000";
    //22 诱导信息
    public static String m_InduceInfo = "00";
    //23 音量设置
    public static String m_VoiceSett = "01";
    //24  主题设置
    public static String m_ThemeSett = "00";
    //25  当前可用车道信息
    public static String m_Cur_Use_Car_info = "00";


    //26  保留位
    public static String m_StoreReserveInfo2 = "00";
    //27-28  字体颜色
    public static String m_TextBgColor = "9DE7";
    //29-48 下一个路口名称  20
    public static String m_NextRoadName = "0000000000000000000000000000000000000000";
    //49-68  当前路口名称   20
    public static String m_CurRoadName = "0000000000000000000000000000000000000000";
    // 69 北京时间
    public static String m_SysTimeInfo = "110510093007";
    //帧 尾
    public static String m_End = "55";
}
