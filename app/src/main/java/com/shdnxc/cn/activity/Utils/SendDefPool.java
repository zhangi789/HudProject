package com.shdnxc.cn.activity.Utils;

import android.content.Context;
import android.util.Log;

import com.shdnxc.cn.activity.MainActivity;
import com.shdnxc.cn.activity.app.IAPI;
import com.shdnxc.cn.activity.bean.FrameMode;
import com.shdnxc.cn.activity.bean.NormalSendMode;

/**
 * Created by Zheng Jungen on 2017/2/28.
 */
public class SendDefPool implements Runnable {
    public static FrameMode stu;

    public static NormalSendMode getStu2() {
        return stu2;
    }

    public void setStu2(NormalSendMode stu2) {
        this.stu2 = stu2;
    }

    public static NormalSendMode stu2;
    String ta = "";
    private String sta;
    private String macAddress;
    private boolean _run = true;
    private Context context;
    private String whichMap = "";

    public void stopThread(boolean run) {
        this._run = !run;
    }

    public SendDefPool(String macAddres, String whichMap) {
        this.macAddress = macAddres;
        this.setStu(stu);
        this.whichMap = whichMap;
        stu = new FrameMode(FModeData.m_Head, FModeData.mAllLength, FModeData.m_CrcCode, FModeData.m_MagicCode, FModeData.m_VersionInfo, FModeData.m_BootHudInfo,
                FModeData.m_TbtInfo, FModeData.m_NextRoadDistance, FModeData.m_DestInfo, FModeData.m_DestComp, FModeData.m_CurCarSpeed, FModeData.m_LimitCarSpeed, FModeData.m_AssInfo, FModeData.m_CarRoadInfo,
                FModeData.m_RoadLimitInfo, FModeData.m_StoreReserveInfo, FModeData.m_VoiceSett, FModeData.m_ThemeSett, FModeData.m_StoreReserveInfo2, FModeData.m_NextRoadName, FModeData.m_CurRoadName, FModeData.m_SysTimeInfo,
                FModeData.m_End
        );
        this.sta = stu.toString();
        stu2 = new NormalSendMode(
                FModeData2.m_Head,
                FModeData2.m_AllLength,
                FModeData2.m_CrcCode,
                FModeData2.m_MagicCode,
                FModeData2.m_VersionInfo,
                FModeData2.m_BootHudInfo,
                FModeData2.m_TbtInfo,
                FModeData2.m_NextRoadDistance,
                FModeData2.m_DestInfo,
                FModeData2.m_DestComp,
                FModeData2.m_CurCarSpeed,
                FModeData2.m_CurCarLimitSpped,
                FModeData2.m_ForntLimitSpeedDistance,
                FModeData2.m_CamereStyle,
                FModeData2.m_Cur_Camere_Distance,
                FModeData2.m_CarRoadInfo,
                FModeData2.m_InduceInfo,
                FModeData2.m_VoiceSett,
                FModeData2.m_ThemeSett,
                FModeData2.m_Cur_Use_Car_info,
                FModeData2.m_StoreReserveInfo2,
                FModeData2.m_TextBgColor,
                FModeData2.m_NextRoadName,
                FModeData2.m_CurRoadName,
                FModeData2.m_SysTimeInfo,
                FModeData2.m_End
        );

    }

//    public SendDefPool(Context context, String sta, FrameMode stu, String macAddres) {
//        this.context = context;
//        this.macAddress = macAddres;
//        this.sta = sta;
//        this.setStu(stu);
//
//    }

    @Override
    public void run() {
        while (true) {
            if (_run) {
                FrameMode stu = getStu();
                NormalSendMode stu2 = getStu2();
                String nomal = stu.getmBootHudInfo();
                boolean b = false;
                try {
                    for (int i = 0; i < 5; i++) {

                        if (i == 4) {
                            ta = getSta().substring(i * 40, getSta().length());
                        } else {
                            ta = getSta().substring(i * 40, i * 40 + 40);
                            if (i == 0) {
                                if (!ta.substring(20, 22).equals("0E") && !ta.substring(20, 22).equals("0e")) {
                                    if (IAPI.getWhichMap().equals("1")) {
                                        stu.setChanged(true);
                                    } else {
                                        stu2.setChanged(true);
                                    }
                                    Log.i("TYU", "每次开始 " + ta.substring(20, 22) + "------------------------------------------------");
                                }
                            }
                        }

                        Log.i("aaaa", "发送的数据---------->" + getSta());
                        if (MainActivity.mLeProxy != null) {
                            MainActivity.mLeProxy.send(macAddress, ta, false);
                        }
                        Thread.sleep(20);
                    }
                    Log.i("aaaa", "run: ------ " + IAPI.getWhichMap());
                    if (IAPI.getWhichMap().equals("1")) {
                        nomal = stu.getmBootHudInfo();
                        stu.setmSysTimeInfo(CRC64.getSysTime());
                        stu.setmCrcCode(CRC64.getAToHCRC(stu.toString().substring(8, stu.toString().length() - 2)));
                        setSta(stu.toString());

                        if (stu.isChanged()) {
                            if (nomal.equals("00")) {
                                stu.setmBootHudInfo(SendUtils.setHudLight(7, nomal));
                                Log.i("nnn", "  转化亮度的值后  " + "-------------------------------------------------------------------------- " + SendUtils.setHudLight(7, nomal));
                            } else if (nomal.equals("02")) {
                                stu.setmBootHudInfo(SendUtils.setHudLight(7, nomal));
                                Log.i("nnn", "  转化亮度的值后  " + "-------------------------------------------------------------------------- " + SendUtils.setHudLight(7, nomal));
                            } else if (nomal.equals("04")) {
                                stu.setmBootHudInfo(SendUtils.setHudLight(7, nomal));
                                Log.i("nnn", "  转化亮度的值后  " + "-------------------------------------------------------------------------- " + SendUtils.setHudLight(7, nomal));
                            } else if (nomal.equals("06")) {
                                stu.setmBootHudInfo(SendUtils.setHudLight(7, nomal));
                                Log.i("nnn", "  转化亮度的值后  " + "-------------------------------------------------------------------------- " + SendUtils.setHudLight(7, nomal));
                            }
                        }
                    } else {
                        nomal = stu2.getmBootHudInfo();
                        stu2.setmSysTimeInfo(CRC64.getSysTime());
                        stu2.setmCrcCode(CRC64.getAToHCRC(stu2.toString().substring(8, stu2.toString().length() - 2)));
                        setSta(stu2.toString());

                        if (stu2.isChanged()) {
                            if (nomal.equals("00")) {
                                stu2.setmBootHudInfo(SendUtils.setHudLight(7, nomal));
                                Log.i("nnn", "  转化亮度的值后  " + "-------------------------------------------------------------------------- " + SendUtils.setHudLight(7, nomal));
                            } else if (nomal.equals("02")) {
                                stu2.setmBootHudInfo(SendUtils.setHudLight(7, nomal));
                                Log.i("nnn", "  转化亮度的值后  " + "-------------------------------------------------------------------------- " + SendUtils.setHudLight(7, nomal));
                            } else if (nomal.equals("04")) {
                                stu2.setmBootHudInfo(SendUtils.setHudLight(7, nomal));
                                Log.i("nnn", "  转化亮度的值后  " + "-------------------------------------------------------------------------- " + SendUtils.setHudLight(7, nomal));
                            } else if (nomal.equals("06")) {
                                stu2.setmBootHudInfo(SendUtils.setHudLight(7, nomal));
                                Log.i("nnn", "  转化亮度的值后  " + "-------------------------------------------------------------------------- " + SendUtils.setHudLight(7, nomal));
                            }
                        }

                    }
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getSta() {
        return sta;
    }

    public void setSta(String sta) {
        this.sta = sta;
    }

    public FrameMode getStu() {
        return stu;
    }

    public void setStu(FrameMode stu) {
        this.stu = stu;
    }


}
