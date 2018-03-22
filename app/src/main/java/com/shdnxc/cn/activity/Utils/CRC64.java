package com.shdnxc.cn.activity.Utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Zheng Jungen on 2017/2/13.
 */
public class CRC64 {
    public static String getDoucment(String filename) {
        byte[] buffer = new byte[8];
        StringBuffer sb = new StringBuffer();
        File file = new File(filename);
        try {
            int result = 0;
            InputStream is = new FileInputStream(file);
            while (is.read(buffer) != -1) {
                ByteArrayInputStream bs = new ByteArrayInputStream(buffer);
                while ((result = bs.read()) != -1) {
                    String tmp = Integer.toHexString(result);
                    if (tmp.length() == 1)
                        sb.append("0" + tmp + " ");
                    else
                        sb.append(tmp + " ");

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();

    }

    public static String getSendData(String all, int startPostion, int endPostion, String hexStr) {
        StringBuilder sb = new StringBuilder(all);
        String sysTime = CRC64.getSysTime();
        int len = all.length();
        //拼接时间
        sb.replace(len - 14, len - 2, sysTime);
        //需要替代的部分
        sb.replace(startPostion, endPostion, hexStr);
        String all2 = sb.toString();
        //用于计算Crc校验码的长度
        String crcStr2 = all2.substring(8, len - 2);
        String aToHCRC = getAToHCRC(crcStr2);
        StringBuffer sb2 = new StringBuffer(all2);
        sb2.replace(4, 8, aToHCRC);
        return sb2.toString();
    }

    /**
     * 获得magic 码  4位
     *
     * @param a 一部到位
     * @param b
     * @param c
     * @param d
     * @return
     */
    public static String getMagicHexString(char a, char b, char c, char d) {
        int magic_hash = (((int) a << 24) | (((int) b << 16)) | (((int) c << 8)) | ((int) d)) * 0x9e370001;
        String hexString = Long.toHexString(Long.parseLong(String.valueOf(magic_hash & 0x0FFFFFFFF)));
        int length = hexString.length();
        return hexString.substring(length - 2, length) + hexString.substring(length - 4, length - 2) + hexString.substring(2, 4) + hexString.substring(0, 2);
    }

    /**
     * CRC 校验
     */
    static final int crc16_half_byte[] = {0x0000, 0x1021, 0x2042, 0x3063,
            0x4084, 0x50a5, 0x60c6, 0x70e7, 0x8108, 0x9129, 0xa14a, 0xb16b,
            0xc18c, 0xd1ad, 0xe1ce, 0xf1ef};

    /**
     * 获得校验码  APP—>HUD CRC
     * res= migic+数据段信息
     * String crcResData = getCRC(CRC64.api_cal_crc(CRC64.hexString(removeSpace(res))));
     */
    public static String getAToHCRC(String res) {
        int k = api_cal_crc(hexString(removeSpace(res)));
        int G = k & 0x0FFFF;
        long b = Long.parseLong(String.valueOf(G));
        String s = Long.toHexString(b);
        if (s.length() == 4) {
            s = s;
        } else if (s.length() == 3) {
            s = "0" + s;
        } else if (s.length() == 2) {
            s = "00" + s;
        } else if (s.length() == 1) {
            s = "000" + s;
        }
        String s1 = s.substring(2, 4) + s.substring(0, 2);
        return s1;
    }

    /**
     * 获得 目的地信息
     *
     * @param s
     * @return
     */
    public static String getDestHex(String s) {
        String str = "";
        int length = s.length();
        switch (length) {
            case 1:
                str = "000" + s;
                break;
            case 2:
                str = "00" + s;
                break;
            case 3:
                str = "0" + s;
                break;
            case 4:
                str = s;
                break;
        }
        return getString2(str);
    }

    public static String getString2(String string) {
        return string.substring(2, 4) + string.substring(0, 2);
    }

    /**
     * crc 校验    字符串转化为int数组
     *
     * @param src
     * @return
     */
    public static int[] hexString(String src) {

        int len = src.length() / 2;
        byte[] ret = new byte[len];
        int[] ret2 = new int[len];

        for (int i = 0; i < len; i++) {
            ret[i] = (byte) (Integer.valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue());
            ret2[i] = ret[i] & 0x0FF;
        }
        return ret2;
    }

    /**
     * CRC 校验  返回Int
     *
     * @param data
     * @return
     */
    public static int api_cal_crc(int[] data) {

        int da = 0;
        int crc = 0;
        for (int i = 0; i < data.length; i++) {
            da = (((crc / 256) & 0x0ff) / 16);
            crc <<= 4;
            crc &= 0x0FFFF;
            int i2 = da ^ (data[i] / 16);
            crc ^= crc16_half_byte[(da ^ ((data[i] / 16) & 0x0ff)) & 0x0ff];
            da = ((((crc / 256) & 0x0ff) / 16) & 0x0ff);
            crc <<= 4;
            crc &= 0x0FFFF;
            int i1 = (da ^ (data[i] & 0x00f)) & 0x0ff;
            crc ^= crc16_half_byte[i1];
        }
        return crc;
    }

    /**
     * getReverseHexTo8Byte("5A")
     * “7B” 转化为               1个字节8
     * 正
     * 0111 1011 b7......b0
     * 反序
     * 1101 1110 b0.......b7
     *
     * @param hexString1Byte
     * @return
     */
    public static String getReverseHexTo8Byte(String hexString1Byte) {
        String zheng = getHexToBinbyte(Integer.valueOf(hexString1Byte.substring(0, 1), 16)) + getHexToBinbyte(Integer.valueOf(hexString1Byte.substring(1, 2), 16));
        StringBuffer buff = new StringBuffer(zheng);
        return buff.reverse().toString();

    }

    /**
     * 1个字节正序转化
     *
     * @param hexString1Byte
     * @return
     */
    public static String getHexTo8Byte(String hexString1Byte) {
        String zheng = getHexToBinbyte(Integer.valueOf(hexString1Byte.substring(0, 1), 16)) + getHexToBinbyte(Integer.valueOf(hexString1Byte.substring(1, 2), 16));
        StringBuffer buff = new StringBuffer(zheng);
        return buff.toString();

    }

    public static String getCRC(int k) {
        int G = k & 0x0FFFF;
        long b = Long.parseLong(String.valueOf(G));
        String s = Long.toHexString(b);
        return s.substring(2, 4) + s.substring(0, 2);
    }

    public static String getHexToBinbyte(int integer) {
        String hexString = null;

        if (integer > 10) {
            hexString = Integer.toBinaryString(integer);
        } else {
            hexString = getStrubg(Integer.toBinaryString(integer));
        }
        return hexString;
    }

    public static String getStrubg(String s3) {
        int lengtu = s3.length();
        switch (lengtu) {
            case 0:
                s3 = "0000";
                break;
            case 1:
                s3 = "000" + s3;
                break;
            case 2:
                s3 = "00" + s3;
                break;
            case 3:
                s3 = "0" + s3;
                break;
            case 4:
                s3 = s3;
                break;
        }
        return s3;
    }

    //去除字符串中的空格
    public static String removeSpace(String res) {
        return res.trim().replace(" ", "");
    }


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

    public static String getNavInfo(String destComp) {
        String mN = destComp.substring(2, 4);
        int nMMM = Integer.parseInt(mN, 16);
        int mmm = nMMM | 0x02;
        //10进制的数字转化为16进制的字符串
        String s = Integer.toHexString(mmm);
        if (s.length() != 2) {
            s = "0" + s;
        }
        return destComp.substring(0, 2) + s + destComp.substring(4, 8);
    }

    public static String getAllData(String all) {
        StringBuilder sb = new StringBuilder(all);
        String all2 = sb.toString();
        //用于计算Crc校验码的长度
        String crcStr2 = all2.substring(8, all2.length() - 2);
        String aToHCRC = CRC64.getAToHCRC(crcStr2);
        StringBuffer sb2 = new StringBuffer(all2);
        sb2.replace(4, 8, aToHCRC);
        return sb2.toString();
    }

    public static String getAll2(String all) {
        StringBuilder sb = new StringBuilder(all);
        String all2 = sb.toString();
        //用于计算Crc校验码的长度
        String crcStr2 = all2.substring(8, all2.length() - 2);
        String aToHCRC = CRC64.getAToHCRC(crcStr2);
        StringBuffer sb2 = new StringBuffer(all2);
        sb2.replace(4, 8, aToHCRC);
        return sb2.toString();
    }

    /**
     *
     */


    public static long UpdateCRC16(long crcTrans, int strSize) {
        long crc = crcTrans;
        long in = strSize | 0x100;

        do {
            crc <<= 1;
            in <<= 1;
            if ((in & 0x100) > 0)
                ++crc;
            if ((crc & 0x10000) > 0)
                crc ^= 0x1021;
        }
        while (!((in & 0x10000) > 0));
        return crc & 0xffff;
    }

    public static int[] getintArr(String hexStr) {
        int[] data = new int[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length(); i += 2) {
            String dataew = hexStr.substring(i, i + 2);
            Integer data10 = Integer.valueOf(dataew, 16);
            data[i / 2] = data10;
        }
        return data;
    }

    public static long Cal_CRC16(String hexStr, long size) {
        int[] data = getintArr(hexStr);
        long crc = 0;
        int dataEnd = data[data.length - 1];
        for (int i = 0; i < data.length; i++) {
            crc = UpdateCRC16(crc, data[i]);
        }
        crc = UpdateCRC16(crc, 0);
        crc = UpdateCRC16(crc, 0);

        return crc & 0xffff;
    }

    /**
     * CRC  校验OTA最终数据
     * long  i= Cal_CRC16
     *
     * @param hexStr
     * @param
     * @return
     */
    public static String getOTACRC(String hexStr, long data) {

        String result = "";
        String dataw = Long.toHexString(data);
        int length = dataw.length();
        switch (length) {
            case 4:
                result = hexStr + dataw;
                break;
            case 3:
                result = hexStr + "0" + dataw;
                break;
            case 2:
                result = hexStr + "00" + dataw;
                break;
            case 1:
                result = hexStr + "000" + dataw;
                break;
        }
        return result;

    }


    /**
     * OTA 第一帧补0
     */

    public static String getZero(int activeData) {
        int sumSize = 266;
        StringBuffer sb = new StringBuffer();

        int act = sumSize - activeData;
        for (int i = 0; i < act; i++) {
            sb.append("0");
        }
        return sb.toString();

    }

    /**
     * fileName  是从服务器。bin获得名称 HUD_APP_V-1.3.bin
     * <p>
     * fileSize 是从存储文件读取的字符串的长度 为了方便
     * <p>
     *
     * int fileSises = fileSize.length() / 2;
     * 第一帧的数据
     *
     * @return
     */
    public static String fristAni(String fileName, String fileStr) {
        int fileSises = 0;
        String fileNames = "";
        String head = "0100ff";
        fileNames = fileName.replace("-", "");
        String fileNameStr = stringToAscii(fileNames);

        int leghhh = fileStr.length();

        int eg = leghhh / 256;
        if (leghhh % 256 == 0) {
            eg = eg;
        } else {
            eg = eg + 1;
        }
        fileSises = ((eg + 1) * 256) / 2;
      /*  fileSises = fileStr.length() / 2;*/
        String fileSizeStr = String.valueOf(fileSises);
        String fileSize = stringToAscii(fileSizeStr);
        String fri = head + fileNameStr + "00" + fileSize;
        int length = fri.length();
        int actives = length + 4;
        String zero = getZero(actives);
        String sec = fri + zero;
        String otaCrc = sec.substring(6, sec.length());
        long level = CRC64.Cal_CRC16(otaCrc, otaCrc.length());
        String otacrc1 = CRC64.getOTACRC(otaCrc, level);
        return head + otacrc1;
    }


    /**
     * 字符串转化Ascii
     *
     * @param value
     * @return
     */
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

    public static int getCounnts(int sumSize, int partSize) {
        int counts = 0;
        int count = (sumSize / partSize);
        if (sumSize % partSize == 0) {
            counts = count;
        } else {
            counts = count + 1;
        }
        return counts;
    }

    public static String getOTACRC2(long data) {

        String result = "";
        String dataw = Long.toHexString(data);
        int length = dataw.length();
        switch (length) {
            case 4:
                result = dataw;
                break;
            case 3:
                result = "0" + dataw;
                break;
            case 2:
                result = "00" + dataw;
                break;
            case 1:
                result = "000" + dataw;
                break;
        }
        return result;

    }


    public static String getZero2(int activeData) {
        int sumSize = 256;
        StringBuffer sb = new StringBuffer();

        int act = sumSize - activeData;
        for (int i = 0; i < act; i++) {
            sb.append("0");
        }
        return sb.toString();

    }


    /**
     * 发送数据  OTA
     *
     * @param flag
     * @param value
     * @param patSex
     * @param alData
     * @param fileName
     * @return
     */
    public static String sendOTAData(int flag, int value, int patSex, String alData, String fileName) {
        //patSex  256
        int normal = getCounnts(alData.length(), patSex);

        String all2 = "";

        String head = "01";

        String s = Integer.toHexString(value);

        String sec = getHexAdd0(Integer.toHexString(value));
        int va = (~value) + patSex;
        String three = CRC64.getHexAdd0(Integer.toHexString(va));

        if (flag == 0) {
            String s1 = fristAni(fileName, alData);
            all2 = s1;

        } else if (flag == (normal)) {

            String crc0TA = alData.substring((flag - 1) * patSex, alData.length());
            if (crc0TA.length() < patSex) {
                crc0TA = crc0TA + getZero2(crc0TA.length());
            }
            long level = CRC64.Cal_CRC16(crc0TA, crc0TA.length());
            String otacrc2 = getOTACRC2(level);

            String all = head + sec + three + crc0TA + otacrc2;
            all2 = all;
            System.out.println("第后1针： " + all);
            //发送String  最后一次的数据

        } else if (flag > 0 && flag < (normal)) {
            String crc0TA = alData.substring((flag - 1) * patSex, flag * patSex);
            long level = CRC64.Cal_CRC16(crc0TA, crc0TA.length());
            String otacrc2 = getOTACRC2(level);
            String all = head + sec + three + crc0TA + otacrc2;
            all2 = all;
            //发送即可
            System.out.println("第实际第一则针： " + all);
        }
        return all2;

    }

    /**
     * 16进制的字符串转化成汉字
     *
     * @param string
     * @param encoding
     * @return
     */
    public static String decodeString(String string, String encoding) {
        try {
            byte[] data = string2Bytes(string);
            return new String(data, encoding);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static byte[] string2Bytes(String string) {
        int blen = string.length() / 2;
        byte[] data = new byte[blen];
        for (int i = 0; i < blen; i++) {
            String bStr = string.substring(2 * i, 2 * (i + 1));
            data[i] = (byte) Integer.parseInt(bStr, 16);
        }
        return data;
    }

}
