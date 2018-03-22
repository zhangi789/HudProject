package com.shdnxc.cn.activity.Utils;

import android.content.Context;
import android.util.Log;

import com.amap.api.navi.enums.IconType;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Zheng Jungen on 2017/2/21.
 */
public class SendUtils {
    /**
     *
     *    1366-》0x557
     *
     * 10进制数据转化为16进制的字符串
     * @param data
     * @return
     */
    /*public  static String hexStr1Byte(int data){

    }*/

    /**
     * 十进制数据
     *
     * @param all
     * @param startPostion
     * @param endPostion
     * @param hexStr
     * @return
     */
    //适合发送特殊类型的 整个字节的
    public static String getSendData(String all, int startPostion, int endPostion, String hexStr) {
        StringBuilder sb = new StringBuilder(all);
        String sysTime = getSysTime();
        int len = all.length();
        //拼接时间
        sb.replace(len - 14, len - 2, sysTime);
        //需要替代的部分
        sb.replace(startPostion, endPostion, hexStr);
        String all2 = sb.toString();
        //用于计算Crc校验码的长度
        String crcStr2 = all2.substring(8, len - 2);
        String aToHCRC = CRC64.getAToHCRC(crcStr2);
        StringBuffer sb2 = new StringBuffer(all2);
        sb2.replace(4, 8, aToHCRC);
        return sb2.toString();
    }

    /**
     * 1  获得系统的时间
     *
     * @return
     */
    public static String getSysTime() {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");//设置日期格式
        String sh = df.format(new Date());
        String[] split = sh.split("-");
        return getDataTime(split);
    }

    private static String getDataTime(String[] split) {
        String s0 = discusLen(Long.toHexString(Long.parseLong(split[0].substring(2, 4))).toUpperCase());
        String s1 = discusLen(Long.toHexString(Long.parseLong(split[1])).toUpperCase());
        String s2 = discusLen(Long.toHexString(Long.parseLong(split[2])).toUpperCase());
        String s3 = discusLen(Long.toHexString(Long.parseLong(split[3])).toUpperCase());
        String s4 = discusLen(Long.toHexString(Long.parseLong(split[4])).toUpperCase());
        String s5 = discusLen(Long.toHexString(Long.parseLong(split[5])).toUpperCase());

        return s0 + s1 + s2 + s3 + s4 + s5;
    }

    private static String discusLen(String st) {

        if (st.length() < 2) {
            st = "0" + st;
        } else {
            st = st;
        }
        return st;
    }


    /**
     * 3 默认储存reserve  保留2个字节
     *
     * @return
     */
    public static String get2Reverve() {
        return "0000";
    }

    /**
     * 3 默认储存reserve  保留4个字节
     *
     * @return
     */
    public static String get4Reverve() {
        return "00000000";
    }

    /**
     * 功能对于一个字节长度 16进制的字符串 添加0操作
     *
     * @param hexStr
     * @return
     */


    public static String hexAdd0Oprate(String hexStr) {
        String result = "";
        if (hexStr.length() == 2) {
            result = hexStr;
        } else {
            result = "0" + hexStr;
        }
        return result;
    }

    /**
     * TBT    信息   控制路名转换的图标
     *
     * @param iconName
     * @return
     */
    public static String getTBTInfo(String iconName) {
        String st = "";
        List<String> tb = getTb();
        int iTBT = tb.indexOf(iconName);
        String hexStr = Integer.toHexString(iTBT);
        return hexAdd0Oprate(hexStr);
    }

    public static List<String> getTb() {
        List<String> tbt = new ArrayList<>();
        tbt.add("TURN_INVALID");
        tbt.add("TURN_FRONT");
        tbt.add("TURN_RIGHT_FRONT");
        tbt.add("TURN_RIGHT");
        tbt.add("TURN_RIGHT_BACK");
        tbt.add("TURN_BACK");
        tbt.add("TURN_LEFT_BACK");
        tbt.add("TURN_LEFT");
        tbt.add("TURN_LEFT_FRONT");
        tbt.add("TURN_RING");
        tbt.add("TURN_RING_OUT");
        tbt.add("TURN_LEFT_SIDE");
        tbt.add("TURN_RIGHT_SIDE");
        tbt.add("TURN_LEFT_SIDE_MAIN");
        tbt.add("TURN_BRANCH_LEFT_STRAIGHT");
        tbt.add("TURN_RIGHT_SIDE_MAIN");
        tbt.add("TURN_BRANCH_RIGHT_STRAIGHT");
        tbt.add("TURN_BRANCH_CENTER");
        tbt.add("TURN_LEFT_SIDE_IC");
        tbt.add("TURN_RIGHT_SIDE_IC");
        tbt.add("TURN_BRANCH_LEFT");
        tbt.add("TURN_BRANCH_RIGHT");
        tbt.add("TURN_BRANCH_CENTER");
        tbt.add("TURN_START");
        tbt.add("TURN_DEST");
        tbt.add("TURN_VIA_1");
        tbt.add("TURN_VIA_2");
        tbt.add("TURN_VIA_3");
        tbt.add("TURN_VIA_4");
        tbt.add("TURN_INFERRY");
        tbt.add("TURN_OUTFERRY");
        tbt.add("TURN_TOLLGATE");
        tbt.add("TURN_LEFT_SIDE_MAIN");
        tbt.add("TURN_RIGHT_SIDE_MAIN");
        tbt.add("TURN_LEFT_SIDE_MAIN");
        tbt.add("TURN_RIGHT_SIDE_MAIN");
        tbt.add("TURN_BRANCH_LEFT_STRAIGHT");
        tbt.add("TURN_BRANCH_CENTER");
        tbt.add("TURN_BRANCH_RIGHT_STRAIGHT");
        tbt.add("TURN_BRANCH_LEFT");
        tbt.add("TURN_BRANCH_CENTER");
        tbt.add("TURN_BRANCH_RIGHT");
        tbt.add("TURN_BRANCH_LEFT_STRAIGHT");
        tbt.add("TURN_BRANCH_CENTER");
        tbt.add("TURN_BRANCH_RIGHT_STRAIGHT");
        tbt.add("TURN_LEFT_SIDE_MAIN");
        tbt.add("TURN_RIGHT_SIDE_MAIN");
        tbt.add("TURN_BRANCH_LEFT_STRAIGHT");
        tbt.add("TURN_BRANCH_CENTER");
        tbt.add("TURN_BRANCH_RIGHT_STRAIGHT");
        tbt.add("TURN_LEFT_2BRANCH_LEFT");
        tbt.add("TURN_LEFT_2BRANCH_RIGHT");
        tbt.add("TURN_LEFT_3BRANCH_LEFT");
        tbt.add("TURN_LEFT_3BRANCH_MIDDLE");
        tbt.add("TURN_LEFT_3BRANCH_RIGHT");
        tbt.add("TURN_RIGHT_2BRANCH_LEFT");
        tbt.add("TURN_RIGHT_2BRANCH_RIGHT");
        tbt.add("TURN_RIGHT_3BRANCH_LEFT");
        tbt.add("TURN_RIGHT_3BRANCH_MIDDLE");
        tbt.add("TURN_RIGHT_3BRANCH_RIGHT");
        tbt.add("TURN_LF_2BRANCH_LEFT");
        tbt.add("TURN_LF_2BRANCH_RIGHT");
        tbt.add("TURN_RF_2BRANCH_LEFT");
        tbt.add("TURN_RF_2BRANCH_RIGHT");
        tbt.add("TURN_BACK_2BRANCH_LEFT_BASE");
        tbt.add("TURN_BACK_2BRANCH_RIGHT_BASE");
        tbt.add("TURN_BACK_3BRANCH_LEFT_BASE");
        tbt.add("TURN_BACK_3BRANCH_MIDDLE_BASE");
        tbt.add("TURN_BACK_3BRANCH_RIGHT_BASE");
        tbt.add("TURN_RING_FRONT");
        tbt.add("TURN_RING_LEFT");
        tbt.add("TURN_RING_LEFTBACK");
        tbt.add("TURN_RING_LEFTFRONT");
        tbt.add("TURN_RING_RIGHT");
        tbt.add("TURN_RING_RIGHTBACK");
        tbt.add("TURN_RING_RIGHTFRONT");
        tbt.add("TURN_RING_BACK");
        return tbt;
    }


    public static String getGAOTb(int iconType) {
        int tbtInfo = 0;
        switch (iconType) {
            case IconType.NONE:///< 0 无定义
                tbtInfo = 0;
                break;

            case IconType.DEFAULT://< 1 车图标
                tbtInfo = 0;
                break;

            case IconType.LEFT://< 2 左转图标
                tbtInfo = 7;
                break;

            case IconType.RIGHT://< 3 右转图标
                tbtInfo = 3;
                break;
            case IconType.LEFT_FRONT://< 4 左前方图标
                tbtInfo = 8;
                break;
            case IconType.RIGHT_FRONT://< 5 右前方图标
                tbtInfo = 2;
                break;
            case IconType.LEFT_BACK://< 6 左后方图标
                tbtInfo = 6;
                break;
            case IconType.RIGHT_BACK://< 7 右后方图标
                tbtInfo = 4;
                break;
            case IconType.LEFT_TURN_AROUND://< 8 左转掉头图标
                tbtInfo = 5;
                break;
            case IconType.STRAIGHT://< 9 直行图标
                tbtInfo = 1;
                break;
            case IconType.ARRIVED_WAYPOINT://< 10 到达途经点图标
                tbtInfo = 25;
                break;
            case IconType.ENTER_ROUNDABOUT://< 11 进入环岛图标
                tbtInfo = 9;
                break;
            case IconType.OUT_ROUNDABOUT://< 12 驶出环岛图标
                tbtInfo = 10;
                break;
            case IconType.ARRIVED_SERVICE_AREA://< 13 到达服务区图标

                tbtInfo = 0;

                break;
            case IconType.ARRIVED_TOLLGATE://< 14 到达收费站图标
                tbtInfo = 31;
                break;
            case IconType.ARRIVED_DESTINATION://< 15 到达目的地图标
                tbtInfo = 24;
                break;
            case IconType.ARRIVED_TUNNEL://< 16 进入隧道图标

                tbtInfo = 0;

                break;
            case IconType.CROSSWALK://< 17 通过人行横道图标

                tbtInfo = 0;

                break;
            case IconType.OVERPASS://< 18 通过过街天桥图标

                tbtInfo = 0;
                break;
            case IconType.UNDERPASS://< 19 通过地下通道图标
                tbtInfo = 0;
                break;
            default:
                tbtInfo = 0;
                break;
        }
        String hexStr = Integer.toHexString(tbtInfo);
        return hexAdd0Oprate(hexStr);
    }


    /**
     * 下一个路口距离
     * 参数  str 表示 字符串类型子  “87”
     */
    public static String nextRoadDis(String str) {
        long l = Long.parseLong(str);
        str = Long.toHexString(l).toUpperCase();
        String str2 = null;
        if (str.length() == 4) {
            str2 = str.substring(2, 4) + str.substring(0, 2);
        } else if (str.length() == 3) {
            str2 = "0" + str;
            str2 = str2.substring(2, 4) + str2.substring(0, 2);
        } else if (str.length() == 2) {

            str2 = str + "00";
        } else if (str.length() == 1) {
            str2 = "000" + str;
            str2 = str2.substring(2, 4) + str2.substring(0, 2);
        }
        return str2;

    }

    /**
     * 目的地距离
     *
     * @param str
     * @return
     */
    public static String destinationDis(String str) {
        long l = Long.parseLong(str);
        str = Long.toHexString(l).toUpperCase();
        String str2 = null;
        if (str.length() == 4) {
            str2 = str.substring(2, 4) + str.substring(0, 2);
        } else if (str.length() == 3) {
            str2 = "0" + str;
            str2 = str2.substring(2, 4) + str2.substring(0, 2);
        } else if (str.length() == 2) {

            str2 = str + "00";
        } else if (str.length() == 1) {
            str2 = "000" + str;
            str2 = str2.substring(2, 4) + str2.substring(0, 2);
        }
        return str2;
    }


    /**
     * 音量设置值
     * 音量设置值，范围1-8，由app发送给hud
     */
    public static String getVoice() {
        return "01";
    }


    /**
     * 主题设置
     * 0|  其他  保持不变
     * 1&其他   保持不变
     * 1|  其他   变1
     * 0&其他   变0
     *
     * @return
     */
    public static String setTheme(int value, String nomal) {
        String setThme = "";
        int origialData = Integer.valueOf(nomal, 16);
        if (value == 0) {
            int coverseData = origialData & 0xF8;
            String hexStr = Integer.toHexString(coverseData);
            setThme = hexAdd0Oprate(hexStr);
        } else if (value == 1) {
            int coverseData = ((origialData & 0xF8) | 0x01);
            String hexStr = Integer.toHexString(coverseData);
            setThme = hexAdd0Oprate(hexStr);
        } else if (value == 2) {
            int coverseData = ((origialData & 0xF8) | 0x02);
            String hexStr = Integer.toHexString(coverseData);
            setThme = hexAdd0Oprate(hexStr);
        } else if (value == 3) {
            int coverseData = ((origialData & 0xF8) | 0x04);
            String hexStr = Integer.toHexString(coverseData);
            setThme = hexAdd0Oprate(hexStr);
        }
        return setThme;
    }

    /**
     * 辅助诱导信息type
     */

    public static String getAss() {
        return "00";
    }

    /**
     * 车道信息
     */
    public static String getCarInfo() {
        return "0000";
    }


    /**
     * 道路限制信息
     */
    public static String getRoadLimitInfo() {
        return "00";
    }


    /**
     * byte 4
     * <p>
     * sDesmin  目的地第分钟
     * sRoadType  道路类型
     * sCop 指南针
     * sGps GPS 定位
     * sNAv 是否导航
     * sV 是否违规
     * sDesHor  目的地第小时
     * <p>
     * 转化好顺序了
     */
    public static String hexByte4info(int[] bytes) {
        String stt = "";
        String s;
        for (int i = 0; i < bytes.length; i++) {
            s = Integer.toHexString(bytes[i]);
            if (s.length() != 2) {
                s = "0" + s;
            }
            stt = s + stt;
        }
        return stt;
    }

    public static int[] setASS(int sAss, String nomal) {
        int[] bytes = new int[4];
        int xASss = 0;
        bytes[0] = (int) (Integer.valueOf(nomal.substring(6, 8), 16));
        bytes[1] = (int) (Integer.valueOf(nomal.substring(4, 6), 16));
        bytes[2] = (int) (Integer.valueOf(nomal.substring(2, 4), 16));
        bytes[3] = (int) ((((xASss << 6) & 0xC0)) | (Integer.valueOf(nomal.substring(0, 2), 16) & (0x0C ^ 0xFFFF)));
        return bytes;
    }

    public static int[] setRed(int sRed, String nomal) {
        int[] bytes = new int[4];

        int xRedss = 0;

        bytes[0] = (int) (Integer.valueOf(nomal.substring(6, 8), 16));
        bytes[1] = (int) (Integer.valueOf(nomal.substring(4, 6), 16));


        bytes[2] = (int) (Integer.valueOf(nomal.substring(2, 4), 16));
        bytes[3] = (int) ((((sRed << 4) & 0x30)) | (Integer.valueOf(nomal.substring(0, 2), 16) & (0x30 ^ 0xFFFF)));
        return bytes;
    }

    /**
     * @param sDesHor 目的地时间
     * @param nomal   上一次保存信息
     * @return
     */
    public static int[] setDesHor(String sDesHor, String nomal) {
        int[] bytes = new int[4];
        int K = Integer.parseInt(sDesHor);
        long xDeshorss = 0;
        bytes[0] = (int) (Integer.valueOf(nomal.substring(6, 8), 16));
        bytes[1] = (int) (Integer.valueOf(nomal.substring(4, 6), 16));
        xDeshorss = Integer.valueOf(nomal.substring(2, 4), 16) | (Integer.valueOf(nomal.substring(0, 2), 16) << 8);
        xDeshorss |= (K << 4);
        bytes[2] = (int) (xDeshorss & 0x00FF);
        bytes[3] = (int) ((xDeshorss & 0xFF00) >> 8);
        return bytes;
    }

    /**
     * @param sV
     * @param nomal
     * @return
     */
    public static int[] setBrakRules(int sV, String nomal) {
        int[] bytes = new int[4];
        bytes[0] = (int) (Integer.valueOf(nomal.substring(6, 8), 16));
        bytes[1] = (int) (Integer.valueOf(nomal.substring(4, 6), 16));
        bytes[2] = (int) ((((Integer.valueOf(nomal.substring(2, 4), 16)) & (0x0008 ^ 0xFFFF)) | ((sV << 3) & 0x08)));
        bytes[3] = (int) (Integer.valueOf(nomal.substring(0, 2), 16));
        return bytes;
    }


    public static int[] setGPS(int sGps, String nomal) {
        int[] bytes = new int[4];
        bytes[0] = (int) (Integer.valueOf(nomal.substring(0, 2), 16));
        bytes[1] = (int) (Integer.valueOf(nomal.substring(2, 4), 16));
        bytes[2] = (int) (((Integer.valueOf(nomal.substring(4, 6), 16)) & (0x01 ^ 0xFFFF)) | (sGps & 0x01));
        bytes[3] = (int) (Integer.valueOf(nomal.substring(6, 8), 16));
        return bytes;

    }


    /**
     * 指南针
     *
     * @param sCop
     * @param nomal
     * @return
     */
    public static int[] setCop(int sCop, String nomal) {
        int[] bytes = new int[4];
        int mRoadTyle = 0;
        mRoadTyle = Integer.valueOf(nomal.substring(4, 6), 16);

        mRoadTyle = (int) (((sCop & 0xF8)) | (Integer.valueOf(nomal.substring(4, 6), 16) & (0xF8 ^ 0xFFFF)));
        bytes[0] = (int) (Integer.valueOf(nomal.substring(6, 8), 16));
        bytes[1] = (int) mRoadTyle;
        bytes[2] = (int) (Integer.valueOf(nomal.substring(2, 4), 16));
        bytes[3] = (int) (Integer.valueOf(nomal.substring(0, 2), 16));
        return bytes;

    }

    public static int[] setRoadType(int sRoadType, String nomal) {
        int[] bytes = new int[4];
        long mRoadTyle = 0;
        mRoadTyle = Integer.valueOf(nomal.substring(4, 8), 16) & (0x1F ^ 0xFFFF);
        mRoadTyle |= (long) ((sRoadType & 0x1F) << 6);

        bytes[0] = (int) (mRoadTyle & 0x00FF);
        bytes[1] = (int) ((mRoadTyle & 0xFF00) >> 8);
        bytes[2] = (int) (Integer.valueOf(nomal.substring(2, 4), 16));
        bytes[3] = (int) (Integer.valueOf(nomal.substring(0, 2), 16));
        return bytes;

    }

    public static int[] setDesMis2(int mArrDesMin, int mRemainHourse, String nomal) {
        Log.i("daole", mArrDesMin + " " + mRemainHourse + " " + nomal);
        int[] bytes = new int[4];
        Long xLong;
        int mHourse = mRemainHourse;
        int K = mArrDesMin;
        xLong = (long) (K & 0x3F);
        //第一个字节
        bytes[0] = (int) (xLong | (Integer.valueOf(nomal.substring(0, 2), 16) & 0xC0));
        //第二个字节
        bytes[1] = (int) (Integer.valueOf(nomal.substring(2, 4), 16));
        int byte3Value = Integer.valueOf(nomal.substring(4, 6), 16);
        byte3Value = byte3Value & 0x0f;
        int byte4Value = Integer.valueOf(nomal.substring(6, 8), 16);
        byte4Value = byte4Value & 0xf0;
        bytes[2] = ((mHourse & 0x0f) << 4) | byte3Value;
        bytes[3] = (((mHourse & 0x0f0) >> 4) | byte4Value);
        return bytes;
    }

    public static int[] setDesMis(int mArrDesMin, int mRemainHourse, String nomal) {
        Log.i("daole", mArrDesMin + " " + mRemainHourse + " " + nomal);
        Log.i("UUU", nomal + " 上一次数据nomal");
        Log.i("UUU", mArrDesMin + "   分钟 mArrDesMin");
        Log.i("UUU", mRemainHourse + "   小时 mRemainHourse");
        int[] bytes = new int[4];
        Long xLong;
        //   int mHourse = Integer.parseInt(mRemainHourse);
        int mHourse = mRemainHourse;
        // String mRemainHourse = Integer.toHexString(mRemainHourse);
        Log.i("UUU", mHourse + "   小时 mHourse");
        // int K = Integer.parseInt(mArrDesMin);
        int K = mArrDesMin;
        Log.i("UUU", K + " K");
        xLong = (long) (K & 0x3F);
        Log.i("UUU", xLong + " ");
        bytes[0] = (int) (xLong | (Integer.valueOf(nomal.substring(6, 8), 16) & 0xC0));
        Log.i("UUU", Integer.valueOf(nomal.substring(6, 8), 16) + " Integer.valueOf(nomal.substring(6, 8), 16)");
        Log.i("UUU", bytes[0] + " bytes[0]");
        bytes[1] = (int) (Integer.valueOf(nomal.substring(4, 6), 16));
        Log.i("UUU", bytes[1] + " bytes[1]");
        int byte3Value = Integer.valueOf(nomal.substring(2, 4), 16);
        Log.i("UUU", byte3Value + " byte3Value");
        byte3Value = byte3Value & 0x0f;
        Log.i("UUU", byte3Value + " byte3Value2");
        //  bytes[2] = (int) (Integer.valueOf(nomal.substring(2, 4), 16));
        int byte4Value = Integer.valueOf(nomal.substring(0, 2), 16);
        Log.i("UUU", byte4Value + " byte4Value");
        byte4Value = byte4Value & 0xf0;
        Log.i("UUU", byte4Value + " byte4Value2");

        bytes[2] = ((mHourse & 0x0f) << 4) | byte3Value;
        Log.i("UUU", bytes[2] + " bytes[2]");
        //bytes[3] = (int) (Integer.valueOf(nomal.substring(0, 2), 16));
        bytes[3] = (((mHourse & 0x0f0) >> 4) | byte4Value);
        Log.i("UUU", bytes[3] + " bytes[3]");
        return bytes;
    }

    /**
     * mDestComp
     * 设置导航  Gps 等的入口拼成4字节长度
     *
     * @param data
     * @return
     */
    public static String getNvGpsAll(int[] data) {
        String data0 = hexAdd0Oprate(Integer.toHexString(data[0]));
        String data1 = hexAdd0Oprate(Integer.toHexString(data[1]));
        String data2 = hexAdd0Oprate(Integer.toHexString(data[2]));
        String data3 = hexAdd0Oprate(Integer.toHexString(data[3]));
      /*  Log.i("daole", "------------结束数据--------------" + data0 + data1 + data2 + data3);*/
        return data0 + data1 + data2 + data3;
    }


    /**
     * 导航状态
     *
     * @param sNAv
     * @param nomal
     * @return
     */
    public static int[] setNavState(int sNAv, String nomal) {
        Log.i("dao", sNAv + " :sNAv");
        Log.i("daole", "setNavState-------进来的值-----------> " + sNAv + " " + nomal + "-----");
        int[] bytes = new int[4];
        bytes[0] = (int) (Integer.valueOf(nomal.substring(0, 2), 16));
        bytes[1] = (int) (Integer.valueOf(nomal.substring(2, 4), 16));

        Integer integer = Integer.valueOf(nomal.substring(4, 6), 16);
        Log.i("dao", integer + " :integer");

        int code = ((sNAv << 1) & 0x06);
        Log.i("dao", code + " :code");
        bytes[2] = (int) (((Integer.valueOf(nomal.substring(4, 6), 16)) & (0x0006 ^ 0xFFFF) | code));
        Log.i("dao", bytes[2] + " :bytes[2]");
        bytes[3] = (int) (Integer.valueOf(nomal.substring(6, 8), 16));
        return bytes;
    }

    /**
     * HUd 开关设置
     */
    public static String setHudOpenClose(int openData, String nomal) {
        String HudOpenClos = "";
        int origialData = Integer.valueOf(nomal, 16);
        if (openData == 0) {
            int coverseData = origialData & 0xEF;
            String hexStr = Integer.toHexString(coverseData);
            HudOpenClos = hexAdd0Oprate(hexStr);
        } else if (openData == 1) {
            int coverseData = origialData | 0x10;
            String hexStr = Integer.toHexString(coverseData);
            HudOpenClos = hexAdd0Oprate(hexStr);
        }
        return HudOpenClos;
    }

    /**
     * 设置app 导航中是否语音播报
     */
    public static String setHudNavVoice(int voiceData, String nomal) {
        String mVoiceData = "";
        int origialData = Integer.valueOf(nomal, 16);
        if (voiceData == 0) {
            int coverseData = origialData & 0xBF;
            String hexStr = Integer.toHexString(coverseData);
            mVoiceData = hexAdd0Oprate(hexStr);
        } else if (voiceData == 1) {
            int coverseData = origialData | 0x40;
            String hexStr = Integer.toHexString(coverseData);
            mVoiceData = hexAdd0Oprate(hexStr);
        }
        return mVoiceData;
    }


    /**
     * hud亮度设置
     * <p>
     * 0|  其他  保持不变
     * 1&其他   保持不变
     * 1|  其他   变1
     * 0&其他   变0
     */
    public static String setHudLight(int lightData, String nomal) {
        String hudLight = "";
        int origialData = Integer.valueOf(nomal, 16);
        if (lightData == 0) {
            int coverseData = origialData & 0xF1;
            String hexStr = Integer.toHexString(coverseData);
            hudLight = hexAdd0Oprate(hexStr);
        } else if (lightData == 1) {
            int coverseData = ((origialData & 0xF1) | 0x02);
            String hexStr = Integer.toHexString(coverseData);
            hudLight = hexAdd0Oprate(hexStr);
        } else if (lightData == 2) {
            int coverseData = ((origialData & 0xF1) | 0x04);
            String hexStr = Integer.toHexString(coverseData);
            hudLight = hexAdd0Oprate(hexStr);
        } else if (lightData == 3) {
            int coverseData = ((origialData & 0xF1) | 0x06);
            String hexStr = Integer.toHexString(coverseData);
            hudLight = hexAdd0Oprate(hexStr);
        } else if (lightData == 7) {
            int coverseData = (origialData & 0xF1) | 0x0E;
            String hexStr = Integer.toHexString(coverseData);
            hudLight = hexAdd0Oprate(hexStr);
        }
        return hudLight;
    }

    public static String getActural(Context contenxt, String nomal) {
        String result = "";
        String results = "00020406";

        boolean contains = results.contains(nomal);
        if (contains) {
            result = SendUtils.setHudLight(7, nomal);
            Log.i("nnn", "转化后设置亮度  " + result + "-------------------------------------------------------------------------- ");
            SharedUtils.saveString(contenxt, "sed", result);
        } else {
            result = nomal;
            Log.i("nnn", "转化后设置语音 " + result + "-------------------------------------------------------------------------- ");
            SharedUtils.saveString(contenxt, "sed", result);

        }
        return result;
    }


    /**
     * 设置boot
     *
     * @param bootData
     * @param nomal
     * @return
     */
    public static String setBoot(int bootData, String nomal) {
        String boot = "";
        int origialData = Integer.valueOf(nomal, 16);
        if (bootData == 0) {
            int coverseData = origialData & 0xFE;
            String hexStr = Integer.toHexString(coverseData);
            boot = hexAdd0Oprate(hexStr);

        } else if (bootData == 1) {
            int coverseData = origialData | 0x01;
            String hexStr = Integer.toHexString(coverseData);
            boot = hexAdd0Oprate(hexStr);
        }
        return boot;
    }


    /**
     * @param roadName       路的名称
     * @param nomalByteValue 默认 20字节长度  20
     * @return byteLength  实际的字节长度
     */
    public static String getCurRoadHex(String roadName, int nomalByteValue) {
        String result = "";
        try {
            int realLen = roadName.getBytes("GB2312").length;
            if (realLen == nomalByteValue) {
                String equalActive = getLimitByteStr(roadName, nomalByteValue + 4, "");
                result = toCovHexStr(equalActive);
            } else if (realLen > nomalByteValue) {
                String bigActive = getLimitByteStr(roadName, nomalByteValue - 4, "....");
                result = toCovHexStr(bigActive);
            } else if (realLen < nomalByteValue) {
                result = getSamllStr(roadName, nomalByteValue);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 路名称小于20字节处理
     *
     * @param roadName       路的名称
     * @param nomalByteValue 默认 20字节长度  20
     * @return
     */
    public static String getSamllStr(String roadName, int nomalByteValue) {
        StringBuffer sb = new StringBuffer();
        String cov = toCovHexStr(roadName);
        int length = cov.length();
        for (int i = 0; i < (nomalByteValue * 2 - length) / 2; i++) {
            sb.append("00");
        }
        return cov + sb.toString();
    }

    /**
     * 把汉字转化为GB2312编码的ASCII码
     *
     * @param chinseStr 汉字字符串
     * @return
     */
    public static String toCovHexStr(String chinseStr) {
        String chineseResult = "";
        try {
            byte[] chiByte = chinseStr.getBytes("GB2312");

            for (int i = 0; i < chiByte.length; i++) {
                int i1 = chiByte[i] & 0xff;
                String sfs = Integer.toHexString(i1);
                chineseResult = chineseResult + sfs;

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return chineseResult;
    }

    /**
     * 限制制定（汉字字符串）制定字节长度显示
     *
     * @param roadName 路名称
     * @param len      截取制定字节长度
     * @param symbol   超过部分默认用什么取代
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getLimitByteStr(String roadName, int len, String symbol) throws UnsupportedEncodingException {
        byte b[];
        int counterOfDoubleByte = 0;
        b = roadName.getBytes("GBK");
        if (b.length <= len)
            return roadName;
        for (int i = 0; i < len; i++) {
            if (b[i] < 0)
                counterOfDoubleByte++;
        }

        if (counterOfDoubleByte % 2 == 0)
            return new String(b, 0, len, "GBK") + symbol;
        else
            return new String(b, 0, len - 1, "GBK") + symbol;
    }


    /**
     * 获得 目的地信息
     *
     * @param hexStr
     * @return
     */
    public static String getDestHex(String hexStr) {
        String result = hexAdd2Oprate(hexStr);
        return getConvseSequesn(result);
    }

    /**
     * 特殊处理转化一下次序
     *
     * @param hexStr
     * @return
     */
    public static String getConvseSequesn(String hexStr) {
        return hexStr.substring(2, 4) + hexStr.substring(0, 2);
    }

    /**
     * 填补2个字节长度
     *
     * @param hexStr
     * @return
     */
    public static String hexAdd2Oprate(String hexStr) {
        String result = "";
        StringBuffer sb = new StringBuffer();
        int length = hexStr.length();
        if (length == 4) {
            result = hexStr;
        } else {
            for (int i = 0; i < 4 - length; i++) {
                sb.append("0");
            }
            result = sb.toString() + hexStr;
        }
        return result;


    }


    /**
     * 高德地图的摄像头类型
     *
     * @param nomal
     * @return
     */
    public static String getGaoCamereTyle(int mCarameStyle, String nomal) {
        int mGaoCameStyle = Integer.valueOf(nomal, 16);
        String camareStyle = "";
        // 默认值
        int coverseData = 0;
        if (mCarameStyle == 0) {
            coverseData = mGaoCameStyle & 0xF0;
        } else if (mCarameStyle == 1) {
            coverseData = (mGaoCameStyle & 0xF0) | 0x01;
        } else if (mCarameStyle == 2) {
            coverseData = (mGaoCameStyle & 0xF0) | 0x02;

        } else if (mCarameStyle == 3) {
            coverseData = (mGaoCameStyle & 0xF0) | 0x03;

        } else if (mCarameStyle == 4) {
            coverseData = (mGaoCameStyle & 0xF0) | 0x04;
        } else if (mCarameStyle == 5) {
            coverseData = (mGaoCameStyle & 0xF0) | 0x05;
        }
        String hexStr = Integer.toHexString(coverseData);
        camareStyle = hexAdd0Oprate(hexStr);
        return camareStyle;


    }


    public static String getGaoCanUseCarRoadInfo(String mCanUseCarRoadInfo) {
        String mCanInfo = "";
        StringBuffer sb = new StringBuffer();
        int length = mCanUseCarRoadInfo.length();


        if (length == 8) {
            mCanInfo = mCanUseCarRoadInfo;
        } else {
            for (int i = length; length <= 8; i++) {
                sb.append("0");
            }
            mCanInfo = mCanUseCarRoadInfo + sb.toString();
        }
        return mCanInfo;
    }


    /**
     * 高德的诱导信息
     */
    public static String getInduceInfo(int broadcastType) {
        String hexStr = Integer.toHexString(broadcastType);
        return hexAdd0Oprate(hexStr);
    }


    /**
     * 高德拥堵信息
     */
    public static String getGaoCrowdInfo(int value, String nomal) {
        int mGaoCrowdStyle = Integer.valueOf(nomal, 16);
        String mGaoCrowd = "";
        int mNomalValue = 0;
        if (value == 0) {
            mNomalValue = mGaoCrowdStyle & 0x0F;
        } else if (value == 1) {
            mNomalValue = (mGaoCrowdStyle & 0x0F) | 0x10;
        } else if (value == 2) {
            mNomalValue = (mGaoCrowdStyle & 0x0F) | 0x20;
        } else if (value == 3) {
            mNomalValue = (mGaoCrowdStyle & 0x0F) | 0x30;
        } else if (value == 4) {
            mNomalValue = (mGaoCrowdStyle & 0x0F) | 0x40;
        }
        String hexStr = Integer.toHexString(mNomalValue);
        mGaoCrowd = hexAdd0Oprate(hexStr);
        return mGaoCrowd;
    }
}
