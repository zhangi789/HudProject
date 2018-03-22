package com.shdnxc.cn.activity.Utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zheng Jungen on 2017/1/19.
 */
public class BaseUtils {
    /**
     * 防止android  6.0 需要请求权限
     *
     * @return
     */
    public static boolean isNeedPerssion() {
        boolean needPerrsion = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            needPerrsion = true;
        } else {
            needPerrsion = false;
        }
        return needPerrsion;
    }

    //解析mac地址
    public static String[] getMacStr(String str) {
        String[] str2 = new String[3];
        if (str.length() < 20) {

        } else {
            str2[0] = str.substring(0, 4);
            str2[1] = str.substring(4, 8);
            str2[2] = str.substring(8, 10) + ":" + str.substring(10, 12) + ":" + str.substring(12, 14) + ":" + str.substring(14, 16) + ":" + str.substring(16, 18) + ":" + str.substring(18, 20);
        }
        return str2;
    }

    //去除空格
    public static String removeSpace(String str) {
        return str.trim().replace(" ", "");
    }

    //  格式化数据
    public static String[] getArrayFromHudToApp(String info) {
        String[] defInfo = new String[6];
        defInfo[0] = info.substring(0, 2);
        defInfo[1] = info.substring(2, 4);
        defInfo[2] = info.substring(4, 8);
        defInfo[3] = info.substring(8, 16);
        defInfo[4] = info.substring(16, info.length() - 2);
        defInfo[5] = info.substring(info.length() - 2, info.length());
        return defInfo;
    }

    // 活得数据段信息
    public static String[] getDataSegment(String data) {
        String[] dataSegment = new String[14];
        // 1-2  版本信息

        dataSegment[0] = data.substring(2, 4) + data.substring(0, 2);
        // 3  boot
        dataSegment[1] = data.substring(4, 6);
        // 4
        dataSegment[2] = data.substring(6, 8);
        // 5-16
        dataSegment[3] = data.substring(8, 32);
        // 17-18      88 00  0088  油耗
        dataSegment[4] = data.substring(34, 36) + data.substring(32, 34);
        // 19-21   DB 0B 01   01 0BDB
        dataSegment[5] = data.substring(40, 42) + data.substring(38, 40) + data.substring(36, 38);
        //22
        dataSegment[6] = data.substring(42, 44);
        //23
        dataSegment[7] = data.substring(44, 46);
        //24-25     侯倩   DE18   18DE
        dataSegment[8] = data.substring(48, 50) + data.substring(46, 48);
        //26-30
        dataSegment[9] = data.substring(50, 60);
        //31-70
        dataSegment[10] = data.substring(60, 140);
        //71
        dataSegment[11] = data.substring(140, 142);
        //  72
        dataSegment[12] = data.substring(142, 144);
        dataSegment[13] = data.substring(144, 150);
        return dataSegment;
    }

    /**
     * 故障码解析
     */
    public static String getOBDDate(String obdData) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < obdData.length(); i += 8) {
            String data = obdData.substring(i, i + 8);
            if (i == 72) {
                sb.append(getSiglObdDate(data));
            } else {
                sb.append(getSiglObdDate(data)).append(",");
            }
        }
        return sb.toString();
    }


    /**
     * 一个故障码用4个字节表示, 一共10组,
     * Byte4: 表示故障码状态， 0x03表示当前故障码，0x07表示永久故障码。
     * Byte3: 故障码类型， 0x00: 'P', 0x01: 'C', 0x02:'B', 0x03: 'U'
     * Byte2-1:故障码代码
     * 如果为0x00000000, 则表示无故障码
     * 故障码详细代码文档可以参见附件,可以查表获得
     * 例如;   03 00 02 03 -> P0203
     * 表示当前故障码为P0203，查表得到第三缸喷油嘴控制线失效
     *
     * @param data
     * @return
     */

    public static String getSiglObdDate(String data) {
        String byte1Value = data.substring(0, 2);
        String byte2Value = data.substring(2, 4);
        String byte3Value = data.substring(4, 6);
        String byte4Value = data.substring(6, 8);
        String byte3 = "";


        if (byte1Value.equals("00") && byte2Value.equals("00") && byte3Value.equals("00")) {

        } else if (byte1Value.equals("00") && byte2Value.equals("00")) {

        } else {
            if (byte3Value.equals("00")) {
                byte3 = "P";
            } else if (byte3Value.equals("01")) {
                byte3 = "C";
            } else if (byte3Value.equals("02")) {
                byte3 = "B";
            } else if (byte3Value.equals("03")) {
                byte3 = "U";
            }
        }
        return byte3 + data.substring(2, 4) + data.substring(0, 2);

    }


    /**
     * 检查手机上是否安装了指定的软件
     */

    public static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    /**
     * 判断百度地图的版本
     *
     * @param context
     * @param packageName
     * @return
     */
    public static int baiDuVersion(Context context, String packageName) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(packageName, 0);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info.versionCode;
    }


    /**
     * 或得发送列表
     *
     * @param s2
     * @return
     */
    public static String[] getSendData(String s2) {
        String ret;
        String[] sendData = new String[s2.length() / 40 + 1];
        for (int i = 0; i <= s2.length() / 40; i++) {
            if (i == 4) {
                ret = s2.substring(i * 40, s2.length());
                sendData[i] = ret;

            } else {
                ret = s2.substring(i * 40, i * 40 + 40);
                sendData[i] = ret;
            }
        }
        return sendData;
    }

    public static String toStringHex(String s) {
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "utf-8");//UTF-16le:Not
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    public static String str2HexStr(String str) throws UnsupportedEncodingException {
        byte[] bs = str.getBytes();
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        int bit;
        int bit2;
        for (int i = 0; i < bs.length; i++) {

            if (bs[i] < 0) {

                bit = (bs[i] & 0x0FF);
                bit2 = (bit & 0x0f0) >> 4;
                sb.append(chars[bit2]);
                bit2 = bit & 0x0f;
                sb.append(chars[bit2]);
            } else {
                bit = (bs[i] & 0x0f0) >> 4;
                sb.append(chars[bit]);
                bit = bs[i] & 0x0f;
                sb.append(chars[bit]);
            }

        }
        return sb.toString();
    }


    public static String stringToAscii(String value) {
        StringBuffer sbu = new StringBuffer();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            sbu.append(getHexAdd0(Integer.toHexString((int) chars[i])));
        }
        return sbu.toString();
    }

    public static String getHexAdd0(String hex10) {
        String result = "";
        if (hex10.length() != 2) {
            result = "0" + hex10;
        } else {
            result = hex10;
        }
        return result;

    }

    public static String getCurRoadHex(String roadName, int byteValue) {
        String hexRoad = "";
//        String cy = getCurRoad(roadName, byteValue);
        try {
            byte[] byteTemp = new byte[20];
            byte[] gb2312s = roadName.getBytes("GB2312");
            for (int i = 0; i < gb2312s.length; i++) {
                byteTemp[i] = gb2312s[i];
            }
            for (int i = 0; i < byteTemp.length; i++) {
                int i1 = byteTemp[i] & 0xff;
                String sfs = Integer.toHexString(i1);
                hexRoad = hexRoad + sfs;

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return hexRoad;
    }

    public static int getErrorCode(String obdData) {
        String[] split = obdData.split(",");
        int mNumber = 0;
        for (int i = 0; i < split.length; i++) {
            if (split[i].contains("P") || split[i].contains("C") || split[i].contains("B") || split[i].contains("U")) {
                mNumber++;
            }
        }
        return mNumber;

    }


    public static String getSiglP(String obdDate) {
        StringBuffer sP = new StringBuffer();
        String[] split = obdDate.split(",");
        for (int i = 0; i < split.length; i++) {
            if (i == split.length - 1 && split[i].contains("P")) {
                sP.append(split[i]);
            } else if (split[i].contains("P")) {
                sP.append(split[i]).append(",");
            }
        }
        return getSpitData(sP.toString());
    }

    public static String getSiglB(String obdDate) {

        StringBuffer sB = new StringBuffer();
        String[] split = obdDate.split(",");
        for (int i = 0; i < split.length; i++) {
            if (i == split.length - 1 && split[i].contains("B")) {
                sB.append(split[i]);
            } else if (split[i].contains("B")) {
                sB.append(split[i]).append(",");
            }
        }
        return getSpitData(sB.toString());
    }

    public static String getSiglC(String obdDate) {
        StringBuffer sC = new StringBuffer();
        String[] split = obdDate.split(",");
        for (int i = 0; i < split.length; i++) {
            if (i == split.length - 1 && split[i].contains("C")) {
                sC.append(split[i]);
            } else if (split[i].contains("C")) {
                sC.append(split[i]).append(",");
            }
        }
        return getSpitData(sC.toString());
    }

    public static String getSiglU(String obdDate) {
        StringBuffer sU = new StringBuffer();
        String[] split = obdDate.split(",");
        for (int i = 0; i < split.length; i++) {
            if (i == split.length - 1 && split[i].contains("U")) {
                sU.append(split[i]);
            } else if (split[i].contains("U")) {
                sU.append(split[i]).append(",");
            }
        }


        return getSpitData(sU.toString());
    }


    public static String getSpitData(String data) {
        String result = "";
        if (data.length() == 0) {
            result = "0";
        } else {
            int length = data.length();
            if (data.substring(length - 1, length).equals(",")) {
                result = data.substring(0, length - 1);
            } else {
                result = data.substring(0, length);
            }
        }
        return result;

    }

    /**
     * 水温的数据判断真假
     *
     * @param iocData
     * @param iolValue
     * @return
     */
    public static boolean isWaterTrue(int iocData, String iolValue) {
        boolean isTrue = false;
        if (iocData <= 9 && !iolValue.equals("0.0")) {
            isTrue = true;
        } else {
            isTrue = false;
        }

        return isTrue;
    }

    public static boolean isBattleTrue(double fx) {
        boolean isTrue = false;
        int batteData = (int) fx;
        if (fx != 0.0 && fx < 9 || fx > 16) {
            isTrue = true;
        } else {
            isTrue = false;
        }
        return isTrue;
    }

    /**
     * 2进制字符串转化16进制的字符串
     *
     * @param bString
     * @return
     */
    public static String getBinaryString2hexString(String bString) {
        if (bString == null || bString.equals("") || bString.length() % 8 != 0)
            return null;
        StringBuffer tmp = new StringBuffer();
        int iTmp = 0;
        for (int i = 0; i < bString.length(); i += 4) {
            iTmp = 0;
            for (int j = 0; j < 4; j++) {
                iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
            }
            tmp.append(Integer.toHexString(iTmp));
        }
        return tmp.toString();
    }

    /**
     * 16进制的字符串转化2禁止的字符串
     *
     * @param hexString
     * @return
     */
    public static String getHexString2binaryString(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0)
            return null;
        String bString = "", tmp;
        for (int i = 0; i < hexString.length(); i++) {
            tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        return bString;
    }

    /**
     * 一个字节8位
     * 把不够一个字节的填写成八位
     *
     * @param mMessage
     * @return
     */
    public static String getFill1ByteInfo(String mMessage) {
        int length = mMessage.length();
        if (length != 8) {
            for (int i = length; i < 8; i++) {
                mMessage = "0" + mMessage;
            }
        }
        return mMessage;
    }


    /**
     * 8位字符串反转
     *
     * @param
     * @return
     */


    public static String getVerseString(String mBit8Str) {
        String result = mBit8Str.substring(7, 8) + mBit8Str.substring(6, 7) +
                mBit8Str.substring(5, 6) + mBit8Str.substring(4, 5) +
                mBit8Str.substring(3, 4) + mBit8Str.substring(2, 3) +
                mBit8Str.substring(1, 2) + mBit8Str.substring(0, 1);


        return result;
    }


    /**
     * @param mMessage
     * @return
     */
    public static String getFill1fByteInfo(String mMessage) {
        int length = mMessage.length();
        if (length != 8) {
            for (int i = length; i < 8; i++) {
                mMessage += "0";
            }
        }
        return mMessage;
    }

    /**
     * 对车道数据的处理
     *
     * @param mInfo
     * @return
     */
    public static String getLanCarInfo(String mInfo) {
        String mMessge = "";
        switch (mInfo.length()) {
            case 1:
                mMessge = mInfo + "0";
                break;
            case 2:
                mMessge = mInfo;
                break;
            case 3:
                mInfo = mInfo + "0";
                mMessge = mInfo.substring(2, 4) + mInfo.substring(0, 2);
                break;
            case 4:
                mMessge = mInfo.substring(2, 4) + mInfo.substring(0, 2);
                break;
            case 5:
                mInfo = mInfo + "0";
                mMessge = mInfo.substring(4, 6) + mInfo.substring(2, 4) + mInfo.substring(0, 2);
                break;
            case 6:
                mMessge = mInfo.substring(4, 6) + mInfo.substring(2, 4) + mInfo.substring(0, 2);
                break;
            case 7:
                mInfo = mInfo + "0";
                mMessge = mInfo.substring(6, 8) + mInfo.substring(4, 6) + mInfo.substring(2, 4) + mInfo.substring(0, 2);
                break;
            case 8:
                mMessge = mInfo.substring(6, 8) + mInfo.substring(4, 6) + mInfo.substring(2, 4) + mInfo.substring(0, 2);
                break;
        }
        return mMessge;

    }


    /**
     * 对当前车道车到信息处理
     */


    public static String getLanCurCarInfo(String mInfo) {
        String mMessge = "";
        switch (mInfo.length()) {
            case 1:
                mMessge = mInfo + "0";
                break;
            case 2:
                mMessge = mInfo;
                break;
            case 3:
                mInfo = mInfo + "0";
                mMessge = mInfo.substring(2, 4) + mInfo.substring(0, 2);
                break;
            case 4:
                mMessge = mInfo.substring(2, 4) + mInfo.substring(0, 2);
                break;
            case 5:
                mInfo = mInfo + "0";
                mMessge = mInfo.substring(4, 6) + mInfo.substring(2, 4) + mInfo.substring(0, 2);
                break;
            case 6:
                mMessge = mInfo.substring(4, 6) + mInfo.substring(2, 4) + mInfo.substring(0, 2);
                break;
            case 7:
                mInfo = mInfo + "0";
                mMessge = mInfo.substring(6, 8) + mInfo.substring(4, 6) + mInfo.substring(2, 4) + mInfo.substring(0, 2);
                break;
            case 8:
                mMessge = mInfo.substring(6, 8) + mInfo.substring(4, 6) + mInfo.substring(2, 4) + mInfo.substring(0, 2);
                break;
        }
        return mMessge;

    }
}
