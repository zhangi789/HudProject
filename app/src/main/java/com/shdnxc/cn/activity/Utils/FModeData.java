package com.shdnxc.cn.activity.Utils;

/**
 * Created by Zheng Jungen on 2017/2/23.
 */
public class FModeData {
    public static String m_Head = "AA";

    public static String mAllLength = "4a";
    public static String m_CrcCode = "521d";
    public static String m_MagicCode = ConstantUils.BAIDU_MAGIC_CODE;
    //数据短信息 1-2
    public static String m_VersionInfo = "0000";
    /**
     * 3 0E
     * mBootHudInfo  不是整字节  1个字节长度
     */
    public static String m_BootHudInfo = "00";
    //4
    public static String m_TbtInfo = "00";
    //5-6
    public static String m_NextRoadDistance = "ffff";
    //7-8
    public static String m_DestInfo = "ffff";
    /**
     * 9-12  4个字节
     */
    public static String m_DestComp = "00000000";
    //13-14
    public static String m_CurCarSpeed = "0000";
    //15_16
    public static String m_LimitCarSpeed = "0000";
    //17  辅助诱导信息
    public static String m_AssInfo = "00";
    //18-19  车道信息
    public static String m_CarRoadInfo = "0000";
    //20  道路信息信息
    public static String m_RoadLimitInfo = "00";
    //21-24  保留信息
    public static String m_StoreReserveInfo = "00000000";

    //25 音量设置
    public static String m_VoiceSett = "01";
    /**
     * 26  不是整字节  1个字节长度
     * <p>
     * 主题设置
     */

    public static String m_ThemeSett = "00";

    //27-28  保留位
    public static String m_StoreReserveInfo2 = "0000";

    //29-48 下一个路口名称  20
    public static String m_NextRoadName = "0000000000000000000000000000000000000000";
    //49-68  当前路口名称   20
    public static String m_CurRoadName = "0000000000000000000000000000000000000000";
    // 69 北京时间
    public static String m_SysTimeInfo = "11020b0a0c22";
    //帧 尾
    public static String m_End = "55";

}
