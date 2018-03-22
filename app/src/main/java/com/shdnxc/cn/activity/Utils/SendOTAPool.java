package com.shdnxc.cn.activity.Utils;

import com.shdnxc.cn.activity.MainActivity;

/**
 * ***********************************************************
 * author: Dove
 * time: 2017/3/5 17:33
 * name:
 * overview:
 * usage:
 * *************************************************************
 */
public class SendOTAPool implements Runnable {
    String ta = "";
    private String all;
    private String macAddress;
    private boolean _run = true;

    public void stopThread(boolean run) {
        this._run = !run;
    }

    public SendOTAPool(String macAddress, String all) {
        this.macAddress = macAddress;
        this.all = all;
    }

    @Override
    public void run() {
        while (_run) {
            try {
                for (int i = 0; i < 7; i++) {

                    if (i == 6) {
                        ta = all.substring(i * 40, all.length());
                        stopThread(true);
                        if (MainActivity.mLeProxy != null) {
                            MainActivity.mLeProxy.send(macAddress, ta, false);

                        }


                    /*    if (MainActivity.mLeService != null) {
                            MainActivity.mLeService.send(macAddress, ta, false);
                        }*/

                    } else {
                        ta = all.substring(i * 40, i * 40 + 40);
                       /* if (MainActivity.mLeService != null) {
                            MainActivity.mLeService.send(macAddress, ta, false);
                        }*/
                        if (MainActivity.mLeProxy != null) {
                            MainActivity.mLeProxy.send(macAddress, ta, false);

                        }
                    }
                    Thread.sleep(20);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setAll(String all) {
        this.all = all;
    }
}
