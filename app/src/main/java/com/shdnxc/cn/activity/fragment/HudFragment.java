package com.shdnxc.cn.activity.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.navisdk.hudsdk.BNRemoteMessage;
import com.baidu.navisdk.hudsdk.client.BNRemoteVistor;
import com.baidu.navisdk.hudsdk.client.HUDSDkEventCallback;
import com.shdnxc.cn.activity.MainActivity;
import com.shdnxc.cn.activity.R;
import com.shdnxc.cn.activity.Utils.BaseUtils;
import com.shdnxc.cn.activity.Utils.CRC64;
import com.shdnxc.cn.activity.Utils.ConstantUils;
import com.shdnxc.cn.activity.Utils.FModeData;
import com.shdnxc.cn.activity.Utils.FModeData2;
import com.shdnxc.cn.activity.Utils.SendDefPool;
import com.shdnxc.cn.activity.Utils.SendUtils;
import com.shdnxc.cn.activity.Utils.SharedUtils;
import com.shdnxc.cn.activity.Utils.StringUtils;
import com.shdnxc.cn.activity.app.IAPI;
import com.shdnxc.cn.activity.bean.NvStateStart;
import com.shdnxc.cn.activity.ui.BaiDuIndexActivity;
import com.shdnxc.cn.activity.view.CarCircleProgressBar;
import com.shdnxc.cn.activity.view.CircleProgressBar;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Created by Zheng Jungen on 2017/1/16.
 */
public class HudFragment extends Fragment implements View.OnClickListener {
    Activity mActivity;
    public static String APP_TEST = "YYY";
    public static String APP_TEST2 = "UUU";
    private TextView bleConnState;
    private Handler mMainHandler;
    private boolean connected = false;
    Runnable runnable;
    private String macAddress;
    private int startAngle = 0;
    private int endAngle = 0;
    private int carStartAngle = 0;
    private int carEndAngle = 0;
    //设备地址
    NvStateStart mFrameDis;
    String reciveMacData = "";
    private boolean isConnServe = false;
    private boolean isFristCurrRoad = false;
    private boolean isFristDestDistance = false;
    private boolean isFristNextRoad = false;
    private ImageButton voicePic, baiduPic;
    private boolean isNeedRe = false;
    private String mDeviceNames;
    private boolean belSate;
    private TextView tvMapName;
    private TextView tv_speed, tv_rotate, tv_water_ral, tv_iol_ral, tv_battle_ral;
    private TextView toor_other_title;
  /*  private ProgressView speedPRV, rotarePRV;*/

    private CircleProgressBar rotarePRV;
    private CarCircleProgressBar speedPRV;

    private boolean isSetVersionState;

    private boolean isBaiduSecord;

    private ImageView imageView;

    private boolean isControlState = false;

    //水温
    private ImageView iv_water_1, iv_iol_1, iv_battle_1;
    private AnimationDrawable waterAni, iolAni, battleAni;

    private Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String verson = (String) msg.obj;
            SendDefPool.stu.setmVersionInfo(verson);
            String all2 = CRC64.getAll2(SendDefPool.stu.toString());
            MainActivity.mt.setSta(all2);
            isSetVersionState = true;
        }
    };


    //对蓝牙服务的支持
    private static IntentFilter bleIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantUils.BLE_CONN_FALSE);
        intentFilter.addAction(ConstantUils.BLE_CONN_SUCCESS);
        intentFilter.addAction(ConstantUils.BLE_CONN_BREAK);
        intentFilter.addAction(ConstantUils.BLE_HUD_TO_APP);
        intentFilter.addAction(ConstantUils.BLE_ADDRESS_NAME);
        intentFilter.addAction(ConstantUils.BEL_SYSTEM_CLOSE);
        intentFilter.addAction(ConstantUils.BLE_CRC_ERROR);
        intentFilter.addAction(ConstantUils.BEL_SYSTEM_NO_SUPPORT);
        //20170414  添加的代码
        intentFilter.addAction(ConstantUils.BLE_START_NO_SUPPORT_BLE);
        intentFilter.addAction(ConstantUils.BLE_START_NO_SUPPORT_LANYA);
        intentFilter.addAction(ConstantUils.BLE_START_NO_START);
        // faise counts
        intentFilter.addAction(ConstantUils.BEL_CONN_COUNTS);

        return intentFilter;
    }

    //将获得的数据接收  在updateReceivedData更新数据
    private final BroadcastReceiver bleIntentReceiver = new BroadcastReceiver() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onReceive(Context context, Intent intent) {
            Message mMsg = Message.obtain();
            final String action = intent.getAction();
            mDeviceNames = SharedUtils.getString(getActivity(), ConstantUils.USER_DEVICE_NAME, "YES");
            //成功的回掉
            if (ConstantUils.BEL_CONN_COUNTS.equals(action)) {
                String macDD = "蓝牙连接超时请手动连接";
                setBleConnStateVaue(macDD);
                imageView.setImageResource(R.drawable.bluetooth2);
            } else if (ConstantUils.BLE_START_NO_SUPPORT_BLE.equals(action)) {
                String macDD = "不支持BLE";
                setBleConnStateVaue(macDD);
                imageView.setImageResource(R.drawable.bluetooth2);
            } else if (ConstantUils.BLE_START_NO_SUPPORT_LANYA.equals(action)) {
                String macDD = "手机不支持蓝牙";
                setBleConnStateVaue(macDD);
                imageView.setImageResource(R.drawable.bluetooth2);
            } else if (ConstantUils.BLE_START_NO_START.equals(action)) {
                String macDD = "请先打开系统蓝牙再尝试连接设备";
                setBleConnStateVaue(macDD);
                imageView.setImageResource(R.drawable.bluetooth2);

//                Log.i("result", "蓝牙未打开:----------------------------->> HudFragment");
            } else if (ConstantUils.BLE_CONN_SUCCESS.equals(action)) {
                belSate = true;
                if (mDeviceNames.equals("YES")) {
                    String macDD = "蓝牙连接成功";
                    setBleConnStateVaue(macDD);
                    imageView.setImageResource(R.drawable.icon_bluetooth_blue);
//                    Log.i("result", "蓝牙连接成功:----------------------------->> HudFragment");
                } else {
                    String macDD = mDeviceNames + "蓝牙连接成功";
                    setBleConnStateVaue(macDD);
                    imageView.setImageResource(R.drawable.icon_bluetooth_blue);
//                    Log.i("result", "蓝牙连接成功:----------------------------->> HudFragment");
                }
            } else if (ConstantUils.BLE_CONN_BREAK.equals(action)) {
                belSate = false;
                String macDD = "HUD连接断开";
                bleConnState.setText(macDD);
                imageView.setImageResource(R.drawable.bluetooth2);
//                Log.i("result", "HUD连接断开:----------------------------->> HudFragment");
            } else if (ConstantUils.BLE_CONN_FALSE.equals(action)) {
                belSate = false;
                String macDD = "HUD连接失败";
                bleConnState.setText(macDD);
                imageView.setImageResource(R.drawable.bluetooth2);
//                Log.i("result", "HUD连接失败:----------------------------->> HudFragment");
            } else if (ConstantUils.BEL_SYSTEM_CLOSE.equals(action)) {
                belSate = false;
                String macDD = "请先打开系统蓝牙再尝试连接设备";
                bleConnState.setText(macDD);
                imageView.setImageResource(R.drawable.bluetooth2);

            } else if (ConstantUils.BLE_CRC_ERROR.equals(action)) {
                belSate = false;
                String macDD = "CRC校验失败60次请手动连接";
                bleConnState.setText(macDD);
                imageView.setImageResource(R.drawable.bluetooth2);

            } else if (ConstantUils.BEL_SYSTEM_NO_SUPPORT.equals(action)) {
                belSate = false;
                String macDD = "系统蓝牙错误";
                bleConnState.setText(macDD);
                imageView.setImageResource(R.drawable.bluetooth2);
            } else if (ConstantUils.BLE_HUD_TO_APP.equals(action)) {
                String stringExtra = intent.getStringExtra(ConstantUils.BLE_HUD_TO_APP_KEY);
                String[] defInformation = BaseUtils.getArrayFromHudToApp(BaseUtils.removeSpace(stringExtra));
                //获得数据段信息   数据段信息处理
                String[] dataSegment = BaseUtils.getDataSegment(defInformation[4]);
                String verInfo = dataSegment[0];
                String hexVerInfo = verInfo.substring(2, 4) + verInfo.substring(0, 2);
                String botStateInfo = dataSegment[1];
                //控制版本信息
                if (!isSetVersionState) {
                    mMsg.obj = hexVerInfo;
                    mHander.sendMessage(mMsg);
                }
                tv_speed.setText("0");
                //实际车速
                String nowCarSpeed = dataSegment[11];
                int carSeData = Integer.parseInt(nowCarSpeed, 16);
                String carSepStr = String.valueOf(carSeData);
                if (carSeData > 120) {
                    tv_speed.setTextColor(Color.rgb(249, 133, 86));
                } else {
                    tv_speed.setTextColor(Color.rgb(255, 255, 255));
                }
                tv_speed.setText(carSepStr);
                carEndAngle = carSeData;
                speedPRV.setProgress(carEndAngle);
                speedPRV.setmText("0");
                speedPRV.setAnimProgress(carEndAngle, carStartAngle);
                carStartAngle = carEndAngle;
                //转速
                String rotateSpeed = dataSegment[8];
                System.out.println("转速: " + Integer.parseInt(rotateSpeed, 16) + "km");
                int rotateV2 = Integer.parseInt(rotateSpeed, 16);
                int mRotateData = Integer.parseInt(rotateSpeed, 16);
                String ssss = String.valueOf(mRotateData);
                if (mRotateData > 7000) {
                    tv_rotate.setTextColor(Color.rgb(249, 133, 86));
                } else {
                    tv_rotate.setTextColor(Color.rgb(255, 255, 255));
                }
                tv_rotate.setText(ssss);
                endAngle = rotateV2;
                rotarePRV.setProgress(endAngle);
                rotarePRV.setmText("0");
                rotarePRV.setAnimProgress(endAngle, startAngle);
                startAngle = endAngle;
                //水温
                String waterTemperature = dataSegment[7];
                System.out.println("水温: " + (Integer.parseInt(waterTemperature, 16) - 40));
                int waterData = Integer.parseInt(waterTemperature, 16) - 40;
                Log.i("results", waterData + " ");
                if (waterData > 119) {
                    waterAni.start();
                } else {
                    waterAni.selectDrawable(0);
                    waterAni.stop();
                }
                if (waterData == -40) {
                    tv_water_ral.setText("---");
                } else {
                    tv_water_ral.setText(waterData + " ");
                }
                //平均油耗  0x88 0x00   2201   0122  OK
                String youhao = dataSegment[4];
                System.out.println("平均油耗" + " : " + (double) Integer.parseInt(youhao, 16) / (double) 100 + "L/100km");
                double fy = (double) Integer.parseInt(youhao, 16);
                String iolValue2 = String.valueOf(fy);
                int iocData = (int) fy;
                Log.i("hhhh", iocData + "  iocData");
                String iolValue = String.valueOf(fy);
                if (iocData <= 9 && !iolValue.equals("0.0")) {
                    iolAni.start();
                    tv_iol_ral.setTextColor(Color.rgb(255, 255, 255));
                } else {
                    iolAni.selectDrawable(0);
                    iolAni.stop();
                    tv_iol_ral.setTextColor(Color.rgb(249, 133, 86));
                }
                if (iolValue.equals("0.0")) {
                    tv_iol_ral.setText("---");
                    tv_iol_ral.setTextColor(Color.rgb(255, 255, 255));
                } else {
                    tv_iol_ral.setText(String.valueOf(iocData));
                }
                //总里程 OK
                String distance = dataSegment[5];
                System.out.println("总里程" + " : " + distance + "  " + Integer.parseInt(distance, 16) + "km");
                //   电池电压  OK
                String battery = dataSegment[6];
                System.out.println("电池电压: " + Integer.parseInt(battery, 16));
                double fx = (double) (Integer.parseInt(battery, 16) / (double) 10);
                int batteData = (int) fx;
                if (fx != 0.0 && fx < 9 || fx > 16) {
                    battleAni.start();
                } else {
                    battleAni.selectDrawable(0);
                    battleAni.stop();
                }
             /*   boolean waterTrue = BaseUtils.isWaterTrue(iocData, iolValue);
                boolean battleTrue = BaseUtils.isBattleTrue(fx);
                if (waterTrue && battleTrue) {
                    boolean isiolTrue = iolAni.isRunning();
                    boolean isBattleTrue = battleAni.isRunning();
                    if (isiolTrue && isBattleTrue) {
                    } else if (isiolTrue && !isBattleTrue) {
                        battleAni.start();
                    } else if (!isiolTrue && !isBattleTrue) {
                        iolAni.start();
                        tv_iol_ral.setTextColor(Color.rgb(255, 255, 255));
                        battleAni.start();
                    } else if (!isiolTrue && isBattleTrue) {
                        iolAni.start();
                        tv_iol_ral.setTextColor(Color.rgb(255, 255, 255));
                    }

                } else if (waterTrue && !battleTrue) {
                    iolAni.start();
                    tv_iol_ral.setTextColor(Color.rgb(255, 255, 255));
                    battleAni.selectDrawable(0);
                    battleAni.stop();
                } else if (!waterTrue && battleTrue) {
                    iolAni.selectDrawable(0);
                    iolAni.stop();
                    tv_iol_ral.setTextColor(Color.rgb(249, 133, 86));
                    battleAni.start();
                } else if (!waterTrue && !battleTrue) {
                    iolAni.selectDrawable(0);
                    iolAni.stop();
                    tv_iol_ral.setTextColor(Color.rgb(249, 133, 86));
                    battleAni.selectDrawable(0);
                    battleAni.stop();
                }*/
                String batteleValue = String.valueOf(fx);
                if (fx == 0.0) {
                    tv_battle_ral.setText("---");
                } else {
                    tv_battle_ral.setText(batteleValue);
                }
                System.out.println(dataSegment.length);
            } else if (ConstantUils.BLE_ADDRESS_NAME.equals(action)) {
                if (belSate) {
                    if (mDeviceNames.equals("YES")) {
                        String macDD = "蓝牙连接成功";
                        setBleConnStateVaue(macDD);
                        imageView.setImageResource(R.drawable.icon_bluetooth_blue);
                    } else {
                        String macDD = mDeviceNames + "蓝牙连接成功";
                        setBleConnStateVaue(macDD);
                        imageView.setImageResource(R.drawable.icon_bluetooth_blue);
                    }
                }
            }
        }
    };

    private void setBleConnStateVaue(String info) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                bleConnState.setText(info);
            }
        });
    }

    private HUDSDkEventCallback.OnConnectCallback mConnectCallback = new HUDSDkEventCallback.OnConnectCallback() {
        //导航链接成功
        @Override
        public void onConnected() {
            connected = true;
            isConnServe = true;
            isBaiduSecord = true;

            Intent intent = new Intent();
            intent.setAction(ConstantUils.BAIDU_CONN_STATE_SUCCESS);
            getActivity().sendBroadcast(intent);

            Log.i(APP_TEST, "连接回调 server onConnected 连接成功");
        }

        //导航重新链接成功
        @Override
        public void onReConnected() {
            isBaiduSecord = true;
            Intent intent = new Intent();
            intent.setAction(ConstantUils.BAIDU_CONN_STATE_SUCCESS);
            getActivity().sendBroadcast(intent);
            Log.i(APP_TEST, "连接回调 onReConnected 重新连接成功");
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    Log.i(APP_TEST, "连接回调 server onReConnected Suuce");
                }
            });
        }


        //导航关闭
        @Override
        public void onClose(int i, String s) {
            isBaiduSecord = false;
       /*     isControlState = false;
            Log.i("hjk", isControlState + " onClose");*/
       /*     isNeedRe = false;*/
            Intent intent = new Intent();
            intent.setAction(ConstantUils.BAIDU_CONN_STATE_FAILSE);
            getActivity().sendBroadcast(intent);
            if (connected == true) {
                SendDefPool.stu.getmDestComp();
                Log.i(APP_TEST, "onClose 导航关闭");
                isControlState = false;
                //导航  0x03  结束
                String s8 = SendDefPool.stu.getmDestComp();
                String alterhor = SendUtils.getNvGpsAll(SendUtils.setNavState(3, s8));
                SendDefPool.stu.setmDestComp(alterhor);
                Log.i("RRR", "onClose--->" + SendDefPool.stu.getmDestComp());
                SendDefPool.stu.setmDestInfo(FModeData.m_DestInfo);
                SendDefPool.stu.setmCurCarSpeed(FModeData.m_CurCarSpeed);
                SendDefPool.stu.setmLimitCarSpeed(FModeData.m_LimitCarSpeed);
                SendDefPool.stu.setmAssInfo(FModeData.m_AssInfo);
                SendDefPool.stu.setmCarRoadInfo(FModeData.m_CarRoadInfo);
                SendDefPool.stu.setmRoadLimitInfo(FModeData.m_RoadLimitInfo);
                SendDefPool.stu.setmStoreReserveInfo(FModeData.m_StoreReserveInfo);
                SendDefPool.stu.setmStoreReserveInfo2(FModeData.m_StoreReserveInfo2);
                SendDefPool.stu.setmNextRoadName(FModeData.m_NextRoadName);
                SendDefPool.stu.setmNextRoadDistance(FModeData.m_NextRoadDistance);
                SendDefPool.stu.setmCurRoadName(FModeData.m_CurRoadName);
                SendDefPool.stu.setmTbtInfo(FModeData.m_TbtInfo);
                String all2 = CRC64.getAll2(SendDefPool.stu.toString());
                MainActivity.mt.setSta(all2);
                connected = false;
            }
        }

        @Override
        public void onAuth(BNRemoteMessage.BNRGAuthSuccess bnrgAuthSuccess) {
            Log.i(APP_TEST, "连接回调 onAuth HUD服务端授权成功 " + bnrgAuthSuccess.getAuthResMsg() + bnrgAuthSuccess.getAuthResMsg());

            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    Log.i(APP_TEST, "connect to server onAuth");
                }
            });
        }

        @Override
        public void onStartLBSAuth() {

            Log.i(APP_TEST, "连接回调 onStartLBSAuth 开始向百度LBS开放平台申请鉴权");
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    Log.i(APP_TEST, "connect to server onStartLBSAuth");
                }
            });
        }

        @Override
        public void onEndLBSAuth(int i, String s) {
            Log.i(APP_TEST, "connect to server onEndLBSAuth");
        }
    };
    private HUDSDkEventCallback.OnRGInfoEventCallback mRGEventCallback = new HUDSDkEventCallback.OnRGInfoEventCallback() {
        @Override
        public void onCurrentRoad(BNRemoteMessage.BNRGCurrentRoad bnrgCurrentRoad) {
            Log.i(APP_TEST, "当前路口 ： onCurrentRoad");
            if (bnrgCurrentRoad.getCurrentRoadName().equals("")) {
                isFristCurrRoad = false;
            } else {
                isFristCurrRoad = true;
                String currentRoadName = bnrgCurrentRoad.getCurrentRoadName();
                Log.i(APP_TEST, "当前路口 ： " + currentRoadName);
                String curRoadHex = SendUtils.getCurRoadHex(currentRoadName, 20);
                Log.i(APP_TEST, "当前路名:转化后的" + curRoadHex + " " + curRoadHex.length());
                SendDefPool.stu.setmCurRoadName(curRoadHex);
            }
            int discuss = mFrameDis.getDiscuss(isFristCurrRoad, isFristNextRoad, isFristDestDistance);
            if (discuss == 1 && isControlState == false) {
                isControlState = true;
                Log.i("daole", "onCurrentRoad--->" + SendDefPool.stu.getmDestComp());
                String nomal = SendDefPool.stu.getmDestComp();
                //   String alterhor= SendUtils.setNavStateValue(discuss, destComp);
                String alterhor = SendUtils.getNvGpsAll(SendUtils.setNavState(discuss, nomal));
                SendDefPool.stu.setmDestComp(alterhor);
                Log.i("daole", "onCurrentRoad------------------>" + SendDefPool.stu.getmDestComp());
            }
          /*  if (discuss == 1 && isControlState == false) {
                isControlState = true;
                Log.i("hjk", isControlState + " onCurrentRoad");
                String nomal = SendDefPool.stu.getmDestComp();

                //   String alterhor= SendUtils.setNavStateValue(discuss, destComp);
                String alterhor = SendUtils.getNvGpsAll(SendUtils.setNavState(discuss, nomal));
                SendDefPool.stu.setmDestComp(alterhor);
            }*/
            String all2 = CRC64.getAll2(SendDefPool.stu.toString());
            MainActivity.mt.setSta(all2);

        }

        //机动点信息
        @Override
        public void onManeuver(BNRemoteMessage.BNRGManeuver bnrgManeuver) {
   /* turnName = turnName.replace("turn_", "");*/
            int maneuverDistance = bnrgManeuver.getManeuverDistance();
            String s = Integer.toHexString((int) maneuverDistance);
            String destHex = SendUtils.getDestHex(s);
            SendDefPool.stu.setmNextRoadDistance(destHex);
            Log.i(APP_TEST, "onManeuver 下一个路口  距离： " + bnrgManeuver.getManeuverDistance());
            Log.i(APP_TEST, "onManeuver 下一个路口  名称： " + bnrgManeuver.getNextRoadName());
            if (bnrgManeuver.getNextRoadName().equals("")) {
                isFristNextRoad = false;
            } else {
                isFristNextRoad = true;
                String nextRoadName = bnrgManeuver.getNextRoadName();
                String curRoadHex = SendUtils.getCurRoadHex(nextRoadName, 20);
                SendDefPool.stu.setmNextRoadName(curRoadHex);
                String turnName = bnrgManeuver.name;
                String tbtInfo = SendUtils.getTBTInfo(turnName.toUpperCase());
                SendDefPool.stu.setmTbtInfo(tbtInfo);
            }
            int discuss = mFrameDis.getDiscuss(isFristCurrRoad, isFristNextRoad, isFristDestDistance);
            if (discuss == 1 && isControlState == false) {
                isControlState = true;
                Log.i("daole", SendDefPool.stu.getmDestComp() + " onManeuve ");
                String destComp = SendDefPool.stu.getmDestComp();
                String alterhor = SendUtils.getNvGpsAll(SendUtils.setNavState(discuss, destComp));
                SendDefPool.stu.setmDestComp(alterhor);
                Log.i("daole", SendDefPool.stu.getmDestComp() + "onManeuver------------------>");
            }
            String all2 = CRC64.getAll2(SendDefPool.stu.toString());
            MainActivity.mt.setSta(all2);
        }

        @Override
        public void onServiceArea(BNRemoteMessage.BNRGServiceArea bnrgServiceArea) {
            Log.i(APP_TEST, "服务区的名称：onServiceArea 不处理 " + bnrgServiceArea.getServiceAreaName() + " " + bnrgServiceArea.getServiceAreaDistance());
        }

        //辅助诱导信息 导航过程中的转向、限速、诱导距离信息
        @Override
        public void onAssistant(BNRemoteMessage.BNRGAssistant bnrgAssistant) {
            Log.i(APP_TEST, "onAssistant");
          /*  Log.i("QQQ", "诱导类型 " );
            Log.i("QQQ", "诱导类型：onAssistant " + bnrgAssistant.getAssistantType());
            Log.i("QQQ", "诱导类型：onAssistant  限速  "+bnrgAssistant.getAssistantLimitedSpeed());
            Log.i("QQQ", "诱导类型：onAssistant  诱导 "+bnrgAssistant.getAssistantType());*/
        }

        /**
         * 目的地剩与距离
         * @param bnrgRemainInfo
         */
        @Override
        public void onRemainInfo(BNRemoteMessage.BNRGRemainInfo bnrgRemainInfo) {

            int remainTime = bnrgRemainInfo.getRemainTime();
            Log.i(APP_TEST2, "---目的地剩余总时间 -----" + remainTime);
            int hourse = remainTime / 3600;
            String mRemainHourse = Integer.toHexString(hourse);
            int minies = (remainTime / 60) % 60;
            String mRemainMinse = Integer.toHexString(minies);
            Log.i(APP_TEST2, "---目的地剩余总距离小时 -------------------  " + hourse + " 16进制的数据： " + mRemainHourse);
            int i1 = remainTime / 60 % 60;
            Log.i(APP_TEST2, "---目的地剩余总距离分钟--------------------  " + minies + " 16进制的数据： " + mRemainMinse);
            String nomal = SendDefPool.stu.getmDestComp();
            Log.i(APP_TEST2, " ---上一次的数据------ " + nomal);
            int remainDistance = bnrgRemainInfo.getRemainDistance();
            if (remainDistance != 0 || remainDistance != 65535) {
                isFristDestDistance = true;
                long destTotalDist = remainDistance / 100;
                String s = Integer.toHexString((int) destTotalDist);
                String destHex = SendUtils.getDestHex(s);
                SendDefPool.stu.setmDestInfo(destHex);
                Log.i(APP_TEST2, "---目的地剩余总距离 ------------   " + remainDistance + "    " + destHex);
            } else {
                isFristDestDistance = false;
            }
            int discuss = mFrameDis.getDiscuss(isFristCurrRoad, isFristNextRoad, isFristDestDistance);
            if (discuss == 1 && isControlState == false) {
                isControlState = true;
                Log.i("daole", " onRemainInfo--->" + nomal);
                String alterhor = SendUtils.getNvGpsAll(SendUtils.setNavState(discuss, nomal));
                SendDefPool.stu.setmDestComp(alterhor);
                Log.i("daole", SendDefPool.stu.getmDestComp() + " onRemainInfo------------------>");
            }
            Log.i("daole", SendDefPool.stu.getmDestComp() + " onRemainInfo------------------设置前--->");
            String alterMins = SendUtils.getNvGpsAll(SendUtils.setDesMis2(minies, hourse, SendDefPool.stu.getmDestComp()));
            SendDefPool.stu.setmDestComp(alterMins);
            Log.i("daole", SendDefPool.stu.getmDestComp() + "onRemainInfo------------------分钟设置后--->");
            Log.i(APP_TEST2, "---目的地剩余分钟 " + mRemainMinse + " " + alterMins);
            String all2 = CRC64.getAll2(SendDefPool.stu.toString());
            Log.i(APP_TEST2, "---目的地所有的信息全部数据 ------- " + all2);
            MainActivity.mt.setSta(all2);

        }


        @Override
        public void onNextRoad(BNRemoteMessage.BNRGNextRoad bnrgNextRoad) {
        }


        //GPS丢失
        @Override
        public void onGPSLost(BNRemoteMessage.BNRGGPSLost bnrggpsLost) {
            Log.i(APP_TEST, "onGPSLost ");
            Log.i("daole", SendDefPool.stu.getmDestComp() + " onGPSLost------------------GPS--->");
            String nomal = SendDefPool.stu.getmDestComp();
            String alterhor = SendUtils.getNvGpsAll(SendUtils.setGPS(ConstantUils.GPS_STATE_ClOSE, nomal));
            SendDefPool.stu.setmDestComp(alterhor);
            Log.i("daole", SendDefPool.stu.getmDestComp() + " onGPSLost---------------------------------------GPS--->");
            String all2 = CRC64.getAll2(SendDefPool.stu.toString());
            MainActivity.mt.setSta(all2);
        }

        @Override
        public void onGPSNormal(BNRemoteMessage.BNRGGPSNormal bnrggpsNormal) {
            Log.i(APP_TEST, "GPs正常 ");
            Log.i("daole", "onGPSNormal--->" + SendDefPool.stu.getmDestComp());
            String nomal = SendDefPool.stu.getmDestComp();
            String alterhor = SendUtils.getNvGpsAll(SendUtils.setGPS(ConstantUils.GPS_STATE_OPEN, nomal));
            SendDefPool.stu.setmDestComp(alterhor);
            Log.i("daole", "onGPSNormal------------------------------------>" + SendDefPool.stu.getmDestComp());
            String all2 = CRC64.getAll2(SendDefPool.stu.toString());
            MainActivity.mt.setSta(all2);
        }

        //导航开始
        @Override
        public void onNaviStart(BNRemoteMessage.BNRGNaviStart bnrgNaviStart) {
            //
            Log.i(APP_TEST, "onNaviStart");
        }

        //导航结束
        @Override
        public void onNaviEnd(BNRemoteMessage.BNRGNaviEnd bnrgNaviEnd) {
            Log.i(APP_TEST, "onNaviEnd");
        }

        @Override
        public void onRoutePlanYawing(BNRemoteMessage.BNRGRPYawing bnrgrpYawing) {
            Log.i(APP_TEST, " 重新规划路线  偏航中 onRoutePlanYawing");

            Log.i("daole", "onRoutePlanYawing--->" + SendDefPool.stu.getmDestComp());
            String s = SendDefPool.stu.getmDestComp();
            String alterhor = SendUtils.getNvGpsAll(SendUtils.setNavState(2, s));
            SendDefPool.stu.setmDestComp(alterhor);
            Log.i("daole", "onRoutePlanYawing------------->" + SendDefPool.stu.getmDestComp());
            String all2 = CRC64.getAll2(SendDefPool.stu.toString());
            MainActivity.mt.setSta(all2);
        }

        @Override
        public void onRoutePlanYawComplete(BNRemoteMessage.BNRGRPYawComplete bnrgrpYawComplete) {
            Log.i(APP_TEST, " 重新规划路线完成 偏航完整onRoutePlanYawComplete");
        }

        @Override
        public void onCruiseStart(BNRemoteMessage.BNRGCruiseStart bnrgCruiseStart) {

            Log.i(APP_TEST, " 开启电子狗信息 onCruiseStart");
        }

        @Override
        public void onCruiseEnd(BNRemoteMessage.BNRGCruiseEnd bnrgCruiseEnd) {
            Log.i(APP_TEST, " 关闭电子狗信息 onCruiseEnd");
        }


        @Override
        public void onEnlargeRoad(BNRemoteMessage.BNEnlargeRoad bnEnlargeRoad) {
            Log.i(APP_TEST, " 路口放大图封装信息 " + bnEnlargeRoad.getRoadName());
        }

        //
        @Override
        public void onCarInfo(BNRemoteMessage.BNRGCarInfo bnrgCarInfo) {
            Log.i(APP_TEST, " 车点信息   当前车速 车标当前方向 " + bnrgCarInfo.getCurSpeed() + " " + bnrgCarInfo.getCarAngle());
        }

        @Override
        public void onCarFreeStatus(BNRemoteMessage.BNRGCarFreeStatus bnrgCarFreeStatus) {
            Log.i(APP_TEST, "车标自由状态信息" + bnrgCarFreeStatus.getCarFreeStatus());
        }

        @Override
        public void onCarTunelInfo(BNRemoteMessage.BNRGCarTunelInfo bnrgCarTunelInfo) {
            Log.i("BBB", "当前是否处于隧道中 " + bnrgCarTunelInfo.getIsInTunel());

        }

        @Override
        public void onDestInfo(BNRemoteMessage.BNRGDestInfo bnrgDestInfo) {
            Log.i(APP_TEST, " onDestInfo 目的地总距离  没有用到 " + bnrgDestInfo.getDestTotalDist());

        }

        @Override
        public void onRouteInfo(BNRemoteMessage.BNRGRouteInfo bnrgRouteInfo) {
            Log.i(APP_TEST, "路线信息" + bnrgRouteInfo.mRouteId);
        }

        @Override
        public void onCurShapeIndexUpdate(BNRemoteMessage.BNRGCurShapeIndexUpdate bnrgCurShapeIndexUpdate) {
            Log.i(APP_TEST, "当前形状点索引信息" + bnrgCurShapeIndexUpdate.getFromStartDist());
        }

        @Override
        public void onNearByCamera(BNRemoteMessage.BNRGNearByCameraInfo bnrgNearByCameraInfo) {

            ArrayList<BNRemoteMessage.BNRGNearByCameraInfo.CameraInfo> nearByCameraInfoList = bnrgNearByCameraInfo.getNearByCameraInfoList();

            if (nearByCameraInfoList.size() != 0) {
                BNRemoteMessage.BNRGNearByCameraInfo.CameraInfo cameraInfo = nearByCameraInfoList.get(0);
                int mFromStartDist = cameraInfo.mFromStartDist;
                int mCameraType = cameraInfo.mCameraType;
                Log.i(APP_TEST, "车标附近摄像头信息" + mFromStartDist + " " + mCameraType);
            }
        }
    };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //    getHUDToAppData();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mActivity = getActivity();
        mMainHandler = new Handler(getActivity().getMainLooper());
        mFrameDis = new NvStateStart();
        macAddress = BaseUtils.getMacStr(SharedUtils.getString(getActivity(), ConstantUils.CU_SPLASH, "0"))[2];
        //  tv_show.setText(BaseUtils.getMacStr(macAddress)[2] + "");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_hud, null);
        getActivity().registerReceiver(bleIntentReceiver, bleIntentFilter());
        mMainHandler = new Handler(getContext().getMainLooper());
        initviews(view);
        Log.i("ASP", startAngle + " startAngle");

        speedPRV.setProgress(startAngle);
        speedPRV.setmText("0");
        speedPRV.setAnimProgress(startAngle);
        rotarePRV.setProgress(startAngle);
        rotarePRV.setmText("0");
        rotarePRV.setAnimProgress(startAngle);


        /**
         * 初始化闪图数据
         */
        iv_water_1.setImageResource(R.drawable.water_animlist);
        waterAni = (AnimationDrawable) iv_water_1.getDrawable();
        iv_battle_1.setImageResource(R.drawable.battle_animlist);
        battleAni = (AnimationDrawable) iv_battle_1.getDrawable();
        iv_iol_1.setImageResource(R.drawable.ioc_animlist);
        iolAni = (AnimationDrawable) iv_iol_1.getDrawable();

      /*  toor_other_title.setText("HUD");*/
        initLisenter();
        initHudSdk();
        return view;
    }

    private void initHudSdk() {
        BNRemoteVistor.getInstance().init(mActivity.getApplicationContext(), mActivity.getPackageName(),
                getAppVersionName(mActivity, mActivity.getPackageName()), mRGEventCallback, mConnectCallback);
        BNRemoteVistor.getInstance().setShowLog(true);
        runnable = new Runnable() {
            boolean bool = connected;

            @Override
            public void run() {
                if (!connected) {
                    connect();
                }
                mMainHandler.postDelayed(this, 2000);
            }
        };
        mMainHandler.postDelayed(runnable, 2000);
    }

    private void initLisenter() {
        voicePic.setOnClickListener(this);
        baiduPic.setOnClickListener(this);
        imageView.setOnClickListener(this);
        baiduPic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (IAPI.WHICH_MAP.equals("1")) {
                    tvMapName.setText("高德导航");
                    IAPI.setWhichMap("2");
                    baiduPic.setBackgroundResource(R.mipmap.gao);
                    SendDefPool.stu2.setmMagicCode(FModeData2.m_MagicCode);

                } else {
                    tvMapName.setText("百度导航");
                    IAPI.setWhichMap("1");
                    baiduPic.setBackgroundResource(R.drawable.bai_press);
                    SendDefPool.stu.setmMagicCode(FModeData.m_MagicCode);
                }

                return true;
            }
        });
    }

    //   private TextView car_speed,car_raote,car_oil,car_volatage,car_wether;
    private void initviews(View view) {
      /*  toor_other_title = (TextView) view.findViewById(R.id.toor_other_title);*/
        bleConnState = (TextView) view.findViewById(R.id.tv_conn);
        voicePic = (ImageButton) view.findViewById(R.id.voicePic);
        baiduPic = (ImageButton) view.findViewById(R.id.baiduPic);
        tv_speed = (TextView) view.findViewById(R.id.tv_speed);
        tv_rotate = (TextView) view.findViewById(R.id.tv_rotate);
        tv_water_ral = (TextView) view.findViewById(R.id.tv_water_ral);
        tv_iol_ral = (TextView) view.findViewById(R.id.tv_iol_ral);
        tv_battle_ral = (TextView) view.findViewById(R.id.tv_battle_ral);
        speedPRV = (CarCircleProgressBar) view.findViewById(R.id.crpv);
        rotarePRV = (CircleProgressBar) view.findViewById(R.id.crpv2);
        imageView = (ImageView) view.findViewById(R.id.imageView);

        iv_water_1 = (ImageView) view.findViewById(R.id.iv_water_1);
        iv_iol_1 = (ImageView) view.findViewById(R.id.iv_iol_1);
        iv_battle_1 = (ImageView) view.findViewById(R.id.iv_battle_1);
        tvMapName = (TextView) view.findViewById(R.id.tv_map_name);
    }

 /*   private void getHUDToAppData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (MainActivity.mLeService != null) {
                    MainActivity.mLeService.addBleCallBack(mBleCallBack);
                }
            }
        }, 300);
    }*/

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {


        super.onResume();
        Log.i("VVV", "onPause");

    }


    @Override
    public void onPause() {
        super.onPause();
        Log.i("main", "onPause");
    }

    @Override
    public void onDestroy() {
        Log.i("main", "onDestroy");
        SendDefPool.stu.setmBootHudInfo(FModeData.m_BootHudInfo);
        String all2 = CRC64.getAll2(SendDefPool.stu.toString());
        MainActivity.mt.setSta(all2);
        MainActivity.mt.stopThread(true);
        isBaiduSecord = false;
        Intent intent = new Intent();
        intent.setAction(ConstantUils.BAIDU_CONN_STATE_FAILSE);
        getActivity().sendBroadcast(intent);
        /**
         * 释放 资源
         */
        if (BNRemoteVistor.getInstance().isConnect()) {
            BNRemoteVistor.getInstance().close(HUDSDkEventCallback.OnConnectCallback.CLOSE_NORMAL, "User Exit");
        }
        BNRemoteVistor.getInstance().unInit();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //控制蓝牙连接
            case R.id.imageView:
                break;

            case R.id.voicePic:
                Toast.makeText(getActivity(), "敬请期待", Toast.LENGTH_SHORT).show();
                break;
            case R.id.baiduPic:
                if (IAPI.WHICH_MAP.equals("1")) {
                    if (isBaiduSecord) {
                        Intent i1 = new Intent();
// 展示地图图区
                        i1.setData(Uri.parse("baidumap://map?"));

                        startActivity(i1);
                    } else {
                        startActivity(new Intent(getActivity(), BaiDuIndexActivity.class));
                    }
                } else {
                    startActivity(new Intent(getActivity(), BaiDuIndexActivity.class));
                }

                break;
        }
    }

    /**
     * 两个日期之间相隔的天数
     *
     * @param fDate
     * @param oDate
     * @return
     */
    private static int getIntervalDays(Date fDate, Date oDate) {
        if (null == fDate || null == oDate) {
            return 0;
        }

        long intervalMilli = oDate.getTime() - fDate.getTime();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            fDate = (Date) sdf.parse(sdf.format(fDate));
            oDate = (Date) sdf.parse(sdf.format(oDate));
            intervalMilli = oDate.getTime() - fDate.getTime();
        } catch (Exception e) {
        }
        return (int) (intervalMilli / (24 * 60 * 60 * 1000));

    }

    private String calculateArriveTime(int remainTime) {
        long mArriveTime = System.currentTimeMillis();
        Date curDate = new Date(mArriveTime);
        mArriveTime += (remainTime * 1000);
        Date arriveDate = new Date(mArriveTime);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String mArriveTimeS = sdf.format(arriveDate);

        GregorianCalendar curCal = new GregorianCalendar();
        curCal.setTime(curDate);
        GregorianCalendar arriveCal = new GregorianCalendar();
        arriveCal.setTime(arriveDate);

        if (curCal.get(GregorianCalendar.DAY_OF_MONTH) == arriveCal.get(GregorianCalendar.DAY_OF_MONTH)) {
            if (0 == arriveCal.get(GregorianCalendar.HOUR_OF_DAY)) {
                mArriveTimeS = String.format(getString(R.string.nsdk_string_rg_arrive_time_at_wee), mArriveTimeS);
            } else {
                mArriveTimeS = String.format(getString(R.string.nsdk_string_rg_arrive_time), mArriveTimeS);
            }
        } else {
            int interval = getIntervalDays(curDate, arriveDate);
            if (interval == 1) {
                if (arriveCal.get(GregorianCalendar.HOUR_OF_DAY) >= 0 && arriveCal.get(GregorianCalendar.HOUR_OF_DAY) < 4) {
                    mArriveTimeS = String.format(getString(R.string.nsdk_string_rg_arrive_time),
                            getString(R.string.nsdk_string_rg_wee_hours));
                } else {
                    mArriveTimeS = String.format(getString(R.string.nsdk_string_rg_arrive_time),
                            getString(R.string.nsdk_string_rg_tomorrow));
                }
            } else if (interval == 2) {
                mArriveTimeS = String.format(getString(R.string.nsdk_string_rg_arrive_time),
                        getString(R.string.nsdk_string_rg_the_day_after_tomorrow));
            } else if (interval > 2) {
                mArriveTimeS = String.format(getString(R.string.nsdk_string_rg_arrive_time_after_day), "" + interval);
            } else {
                mArriveTimeS = String.format(getString(R.string.nsdk_string_rg_arrive_time), mArriveTimeS);
            }
        }
        return mArriveTimeS;
    }

    private String calculateTotalRemainDistString(int nDist) {
        StringBuffer builder = new StringBuffer();
        StringUtils.formatDistance(nDist, StringUtils.UnitLangEnum.ZH, builder);
        String totalRemainDistS = builder.toString();

        return totalRemainDistS;
    }

    /**
     * 根据剩余距离获取格式化的字符串，如  200米后
     *
     * @param nextRemainDist
     * @return
     */
    private String getFormatAfterMeters(int nextRemainDist) {
        StringBuffer distance = new StringBuffer();
        StringUtils.formatDistance(nextRemainDist, StringUtils.UnitLangEnum.ZH, distance);
        return getResources().getString(R.string.nsdk_string_rg_sg_after_meters, distance);
    }

    private String getAppVersionName(Context context, String appName) {
        String versionName = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(appName, 0);
            versionName = packageInfo.versionName;
            if (TextUtils.isEmpty(versionName)) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public void connect() {
        BNRemoteVistor.getInstance().open();
    }


    private void startConn() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String s = BaseUtils.getMacStr(SharedUtils.getString(getActivity(), ConstantUils.CU_SPLASH, "0"))[2];
                Log.i("EEE", "MAC " + s);
                MainActivity.mLeService.connect(BaseUtils.getMacStr(SharedUtils.getString(getActivity(), ConstantUils.CU_SPLASH, "0"))[2], false);
            }
        }, 300);
    }
}
