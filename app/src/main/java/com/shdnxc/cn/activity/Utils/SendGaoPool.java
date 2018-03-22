package com.shdnxc.cn.activity.Utils;

import android.content.Context;
import android.util.Log;

import com.shdnxc.cn.activity.MainActivity;
import com.shdnxc.cn.activity.bean.NormalSendMode;

/**
 * Created by Zheng Jungen on 2017/2/28.
 */
public class SendGaoPool implements Runnable {
    private NormalSendMode stu;
    String ta = "";
    private String sta;
    private String macAddress;
    private boolean _run = true;
    private Context context;

    public void stopThread(boolean run) {
        this._run = !run;
    }

    public SendGaoPool(String sta, NormalSendMode stu, String macAddres) {
        this.macAddress = macAddres;
        this.sta = sta;
        this.setStu(stu);
    }

    public SendGaoPool(Context context, String sta, NormalSendMode stu, String macAddres) {
        this.context = context;
        this.macAddress = macAddres;
        this.sta = sta;
        this.setStu(stu);
    }

    @Override
    public void run() {
        while (true) {
            if (_run) {
                NormalSendMode stu = getStu();
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
                                    stu.setChanged(true);
                                    Log.i("TYU", "每次开始 " + ta.substring(20, 22) + "------------------------------------------------");
                                }
                            }
                        }
                        if (MainActivity.mLeProxy != null) {
                            MainActivity.mLeProxy.send(macAddress, ta, false);
                           // Log.i("GaoDe3", "发送的数据---------->" + getSta());
                        }
                        Thread.sleep(20);
                    }
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

    public NormalSendMode getStu() {
        return stu;
    }

    public void setStu(NormalSendMode stu) {
        this.stu = stu;
    }


}
