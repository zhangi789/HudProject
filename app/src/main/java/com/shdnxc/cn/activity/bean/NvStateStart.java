package com.shdnxc.cn.activity.bean;

/**
 * Created by Zheng Jungen on 2017/2/25.
 */
public class NvStateStart {
    private boolean isFristCurrRoad = false;
    private boolean isFristNextRoad = false;
    private boolean isFristDestDistance=false;
    public int  getDiscuss(boolean isFristCurrRoad,boolean isFristNextRoad,boolean isFristDestDistance){
        int i=0;
        if (isFristDestDistance&&isFristDestDistance&&isFristNextRoad){
            i=1;
        }else{
            i=0;
        }
        return i;
    }
}
