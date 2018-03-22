package com.shdnxc.cn.activity.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.navi.AMapHudViewListener;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.enums.AimLessMode;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapCongestionLink;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.shdnxc.cn.activity.MainActivity;
import com.shdnxc.cn.activity.R;
import com.shdnxc.cn.activity.Utils.BaseUtils;
import com.shdnxc.cn.activity.Utils.CRC64;
import com.shdnxc.cn.activity.Utils.ConstantUils;
import com.shdnxc.cn.activity.Utils.FModeData2;
import com.shdnxc.cn.activity.Utils.SendDefPool;
import com.shdnxc.cn.activity.Utils.SendUtils;
import com.shdnxc.cn.activity.Utils.TTSController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andy on 2017/5/12.
 * interface  AMapHudViewListener   onHudViewCancel 方法
 * interface  AMapNaviViewListener  导航视图事件监听。
 * <p>
 * 、模拟导航不会触发到达途经点、到达目的的回调函数。
 */
public class GaoActivity extends AppCompatActivity implements AMapNaviListener, AMapNaviViewListener, AMapHudViewListener {
    private AMapNaviView navi_view;
    private AMapNavi mAmapNavi;
    public static String APP_TEST = "GaoDe";
    public static String APP_TEST2 = "GaoDe2";
    public static String APP_TEST3 = "GaoDe3";
    public static String APP_TEST4 = "GaoDe4";
    public static String APP_TEST5 = "GaoDe5";
    //   protected NaviLatLng mEndLatlng = new NaviLatLng(40.084894, 116.603039);
    public NaviLatLng mEndLatlng;
    // protected NaviLatLng mStartLatlng = new NaviLatLng(30.342972, 120.522972);
    public NaviLatLng mStartLatlng;
    private final List<NaviLatLng> sList = new ArrayList<NaviLatLng>();
    private final List<NaviLatLng> elist = new ArrayList<NaviLatLng>();
    protected List<NaviLatLng> mWayPointList;
    protected TTSController mTtsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String mStartLa = intent.getStringExtra("mStartLa");
        Log.i("GaoDe", "mStartLa " + mStartLa);
        String mEndLon = intent.getStringExtra("mEndLon");
        Log.i("GaoDe", "mEndLon " + mEndLon);
        String mEndLa = intent.getStringExtra("mEndLa");
        Log.i("GaoDe", "mEndLa " + mEndLa);
        String mStartLon = intent.getStringExtra("mStartLon");
        Log.i("GaoDe", "mStartLon " + mStartLon);
        mStartLatlng = new NaviLatLng(Double.parseDouble(mStartLa), Double.parseDouble(mStartLon));
        mEndLatlng = new NaviLatLng(Double.parseDouble(mEndLa), Double.parseDouble(mEndLon));
        setContentView(R.layout.activity_gao);
        //实例化语音引擎
        mTtsManager = TTSController.getInstance(getApplicationContext());
        mTtsManager.init();
        navi_view = (AMapNaviView) findViewById(R.id.navi_view);
        navi_view.onCreate(savedInstanceState);
        navi_view.setAMapNaviViewListener(this);
        mAmapNavi = AMapNavi.getInstance(getApplicationContext());
        mAmapNavi.addAMapNaviListener(this);
        mAmapNavi.addAMapNaviListener(mTtsManager);

        //设置模拟导航的行车速度
        mAmapNavi.setEmulatorNaviSpeed(1500);
        sList.add(mStartLatlng);
        elist.add(mEndLatlng);
        //开启巡航模式
        mAmapNavi.startAimlessMode(AimLessMode.CAMERA_AND_SPECIALROAD_DETECTED);
        mWayPointList = new ArrayList();
        mWayPointList.add(new NaviLatLng(39.925846, 116.442765));
    }

    ;

    //初始化成功
    @Override
    public void onInitNaviSuccess() {
        int strategy = 0;
        Log.i(APP_TEST, " 初始化成功 ");
        strategy = mAmapNavi.strategyConvert(false, false, false, false, false);
        mAmapNavi.calculateDriveRoute(sList, elist, null, strategy);
    }

    //初始化失败
    @Override
    public void onInitNaviFailure() {
        Log.i(APP_TEST, " 初始化失败 ");
        Toast.makeText(this, "init navi Failed", Toast.LENGTH_SHORT).show();

    }

    //     导航状态包括：  导航开始
    @Override
    public void onStartNavi(int i) {
        Log.i(APP_TEST, " 导航开始 " + i);
        String mStartNomal = SendDefPool.stu2.getmDestComp();
        String mNVStartStare = SendUtils.getNvGpsAll(SendUtils.setNavState(1, mStartNomal));
        SendDefPool.stu2.setmDestComp(mNVStartStare);
        String all2 = CRC64.getAll2(SendDefPool.stu2.toString());
        MainActivity.mt.setSta(all2);
    }

    //模拟导航结束时 导航结束
    @Override
    public void onEndEmulatorNavi() {
        Log.i(APP_TEST, " 模拟导航结束时导航结束  ");
        String s8 = SendDefPool.stu2.getmDestComp();
        String alterhor = SendUtils.getNvGpsAll(SendUtils.setNavState(3, s8));
        SendDefPool.stu2.setmDestComp(alterhor);
        SendDefPool.stu2.setmTbtInfo(FModeData2.m_TbtInfo);
        SendDefPool.stu2.setmNextRoadDistance(FModeData2.m_NextRoadDistance);
        SendDefPool.stu2.setmDestInfo(FModeData2.m_DestInfo);
        SendDefPool.stu2.setmCurCarSpeed(FModeData2.m_CurCarSpeed);
        SendDefPool.stu2.setmCurCarLimitSpped(FModeData2.m_CurCarLimitSpped);
        SendDefPool.stu2.setmForntLimitSpeedDistance(FModeData2.m_ForntLimitSpeedDistance);
        SendDefPool.stu2.setmCamereStyle(FModeData2.m_CamereStyle);
        SendDefPool.stu2.setmCur_Camere_Distance(FModeData2.m_Cur_Camere_Distance);
        SendDefPool.stu2.setmCarRoadInfo(FModeData2.m_CarRoadInfo);
        SendDefPool.stu2.setmInduceInfo(FModeData2.m_InduceInfo);
        SendDefPool.stu2.setmCur_Use_Car_info(FModeData2.m_Cur_Use_Car_info);
        SendDefPool.stu2.setmNextRoadName(FModeData2.m_NextRoadName);
        SendDefPool.stu2.setmCurRoadName(FModeData2.m_CurRoadName);
        String all2 = CRC64.getAll2(SendDefPool.stu2.toString());
        MainActivity.mt.setSta(all2);
        finish();

    }

    // //偏航后重新计算路线回调
    @Override
    public void onReCalculateRouteForYaw() {
        Log.i(APP_TEST, "偏航重算，驾车或者步行导航过程中，偏离了当前规划路径时");
    }

    ////拥堵后重新计算路线回调
    @Override
    public void onReCalculateRouteForTrafficJam() {
        Log.i(APP_TEST, "拥堵重算，驾车导航过程中，前方遇到拥堵时重新计算路径");
    }

    @Override
    public void onTrafficStatusUpdate() {
    }

    //自车信息  自车位置  自车方向
    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {
        NaviLatLng coord = aMapNaviLocation.getCoord();
        double latitude = coord.getLatitude();
        double longitude = coord.getLongitude();
        float bearing = aMapNaviLocation.getBearing();
        Log.i(APP_TEST, "自车信息  " + bearing);

    }

    //播报类型和播报文字回调
    @Override
    public void onGetNavigationText(int i, String s) {


    }

    //到达目的地
    @Override
    public void onArriveDestination() {
        Log.i(APP_TEST, "  到达目的地  ");

        String s8 = SendDefPool.stu2.getmDestComp();
        String alterhor = SendUtils.getNvGpsAll(SendUtils.setNavState(3, s8));
        SendDefPool.stu2.setmDestComp(alterhor);
        SendDefPool.stu2.setmTbtInfo(FModeData2.m_TbtInfo);
        SendDefPool.stu2.setmNextRoadDistance(FModeData2.m_NextRoadDistance);
        SendDefPool.stu2.setmDestInfo(FModeData2.m_DestInfo);
        SendDefPool.stu2.setmCurCarSpeed(FModeData2.m_CurCarSpeed);
        SendDefPool.stu2.setmCurCarLimitSpped(FModeData2.m_CurCarLimitSpped);
        SendDefPool.stu2.setmForntLimitSpeedDistance(FModeData2.m_ForntLimitSpeedDistance);
        SendDefPool.stu2.setmCamereStyle(FModeData2.m_CamereStyle);
        SendDefPool.stu2.setmCur_Camere_Distance(FModeData2.m_Cur_Camere_Distance);
        SendDefPool.stu2.setmCarRoadInfo(FModeData2.m_CarRoadInfo);
        SendDefPool.stu2.setmInduceInfo(FModeData2.m_InduceInfo);
        SendDefPool.stu2.setmCur_Use_Car_info(FModeData2.m_Cur_Use_Car_info);
        SendDefPool.stu2.setmNextRoadName(FModeData2.m_NextRoadName);
        SendDefPool.stu2.setmCurRoadName(FModeData2.m_CurRoadName);
        String all2 = CRC64.getAll2(SendDefPool.stu2.toString());
        MainActivity.mt.setSta(all2);
        finish();
    }

    //路线计算成功     模拟导航跟新
    @Override
    public void onCalculateRouteSuccess() {
        mAmapNavi.startNavi(NaviType.EMULATOR);

    }

    //路线计算失败
    @Override
    public void onCalculateRouteFailure(int errorInfo) {
//路线计算失败
        Log.e(APP_TEST, "--------------------------------------------");
        Log.i(APP_TEST, "路线计算失败：错误码=" + errorInfo + ",Error Message= ");
        Log.i(APP_TEST, "错误码详细链接见：http://lbs.amap.com/api/android-navi-sdk/guide/tools/errorcode/");
        Log.e(APP_TEST, "--------------------------------------------");
        Toast.makeText(this, "errorInfo：" + errorInfo + ",Message：", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAmapNavi.destroy();
        mAmapNavi.stopAimlessMode();
        mTtsManager.destroy();
    }

    ////GPS开关状态回调
    @Override
    public void onGpsOpenStatus(boolean b) {
        String nomal = SendDefPool.stu2.getmDestComp();
        if (b) {
            String alterhor = SendUtils.getNvGpsAll(SendUtils.setGPS(ConstantUils.GPS_STATE_OPEN, nomal));
            SendDefPool.stu2.setmDestComp(alterhor);
        } else {
            String alterhor = SendUtils.getNvGpsAll(SendUtils.setGPS(ConstantUils.GPS_STATE_ClOSE, nomal));
            SendDefPool.stu2.setmDestComp(alterhor);
        }
        String all2 = CRC64.getAll2(SendDefPool.stu2.toString());
        MainActivity.mt.setSta(all2);


        Log.i(APP_TEST, "GPS开关状态回调  -> " + b);
    }

    //道路信息  默认的导航界面上，会显示当前正在行驶的道路的名称，以及下一道路名称。
    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {
        String currentRoadName = naviInfo.getCurrentRoadName();
        String mCurRoadHex = SendUtils.getCurRoadHex(currentRoadName, 20);
        SendDefPool.stu2.setmCurRoadName(mCurRoadHex);
        Log.i(APP_TEST, "道路信息 当前路名 " + currentRoadName + " 转化为16进制16进制字符串 " + mCurRoadHex);
        String nextRoadName = naviInfo.getNextRoadName();
        String mNextRoadHex = SendUtils.getCurRoadHex(nextRoadName, 20);
        SendDefPool.stu2.setmNextRoadName(mNextRoadHex);
        Log.i(APP_TEST, "道路信息 下一个路口名称 " + nextRoadName + " 转化为16进制字符串 " + mNextRoadHex);
        //获取导航过程中当前的车速   高德是 13位    一个字节    防止一个字节不够添加０　    操作
        int currentSpeed = naviInfo.getCurrentSpeed();
        String mHexCurrentSpeed = SendUtils.hexAdd0Oprate(Integer.toHexString(currentSpeed));
//  设置当前车速
        SendDefPool.stu2.setmCurCarSpeed(mHexCurrentSpeed);
        Log.i(APP_TEST, "道路信息 获取导航过程中当前的车速 " + currentSpeed + " 转化为16进制字符串 " + mHexCurrentSpeed);
        //获取当前路段剩余距离
        int curStepRetainDistance = naviInfo.getCurStepRetainDistance();
        String nextRoadDistance = Integer.toHexString((int) curStepRetainDistance);
        String mNextRoadDistance = SendUtils.getDestHex(nextRoadDistance);
        SendDefPool.stu2.setmNextRoadDistance(mNextRoadDistance);
        //处理之后转化后看  f800   00f8  ->  248
        Log.i(APP_TEST, "道路信息 获取当前路段剩余距离 " + curStepRetainDistance + " 转化为16进制的字符串 " + mNextRoadDistance);
        //获取导航转向图标 TBT
        int curStepRetainTime = naviInfo.getCurStepRetainTime();
        Log.i(APP_TEST, "道路信息 获取当前路段剩余时间 " + curStepRetainTime);
        //获取导航转向图标 TBT
        int iconType = naviInfo.getIconType();
        String gaoTb = SendUtils.getGAOTb(iconType);
        SendDefPool.stu2.setmTbtInfo(gaoTb);
        Log.i(APP_TEST4, "道路信息 获取导航转向图标类型TBT " + iconType + " " + gaoTb);
        //导航信息类型   naviType - 1 GPS导航更新,2 模拟导航更新
        int naviType = naviInfo.getNaviType();

        Log.i(APP_TEST, "道路信息 导航信息类型" + naviType);
        //获取路线剩余距离
        int pathRetainDistance = naviInfo.getPathRetainDistance();
        Log.i(APP_TEST, "道路信息 获取路线剩余距离 " + pathRetainDistance);
        if (pathRetainDistance != 0 || pathRetainDistance != 65535) {
            long destTotalDist = pathRetainDistance / 100;
            String s = Integer.toHexString((int) destTotalDist);
            String destHex = SendUtils.getDestHex(s);
            Log.i(APP_TEST, "道路信息 获取路线剩余距离" + pathRetainDistance + " 转化为16进制字符串   " + destHex);
            SendDefPool.stu2.setmDestInfo(destHex);
        }
        //获取路线剩余时间
        int pathRetainTime = naviInfo.getPathRetainTime();
        int hourse = pathRetainTime / 3600;
        int minies = (pathRetainTime / 60) % 60;      //需要默认数据支持
        Log.i(APP_TEST, "道路信息 获取路线剩余时间小时和分钟 " + pathRetainTime + "小时" + hourse + " 分钟 " + minies);
        String alterMins = SendUtils.getNvGpsAll(SendUtils.setDesMis2(minies, hourse, SendDefPool.stu2.getmDestComp()));
        SendDefPool.stu2.setmDestComp(alterMins);
        String all2 = CRC64.getAll2(SendDefPool.stu2.toString());
        MainActivity.mt.setSta(all2);

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    //摄像头信息  14   摄像头显示信息  17 距离摄像头的距离  16  字节
    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {
        AMapNaviCameraInfo aMapNaviCameraInfo = aMapNaviCameraInfos[0];
        //电子眼类型: 0 测速摄像头 1为监控摄像头 2为闯红灯拍照 3为违章拍照 4为公交专用道摄像头  16
        int cameraType = aMapNaviCameraInfo.getCameraType();
        String nomal = SendDefPool.stu2.getmCamereStyle();
        if (cameraType == 0) {
            cameraType = cameraType + 1;
            int cameraSpeed = aMapNaviCameraInfo.getCameraSpeed();
            String mCarameLimitSpeed = SendUtils.hexAdd0Oprate(Integer.toHexString(cameraSpeed));
            SendDefPool.stu2.setmCurCarLimitSpped(mCarameLimitSpeed);
            String gaoCamereTyle = SendUtils.getGaoCamereTyle(cameraType, nomal);
            Log.i(APP_TEST2, "获取当前摄像头类型-> " + nomal + " " + cameraType + " " + "转化为16禁止二的字符串" + gaoCamereTyle);
            Log.i(APP_TEST2, "获取当前摄像头的限速大小-> " + cameraSpeed + " " + "转化为16禁止二的字符串" + mCarameLimitSpeed);
            SendDefPool.stu2.setmCamereStyle(gaoCamereTyle);
            int cameraDistance = aMapNaviCameraInfo.getCameraDistance();
            String mCarameLimitDistance = SendUtils.hexAdd0Oprate(Integer.toHexString(cameraDistance / 10));
            Log.i(APP_TEST2, "获取当前限速距离-> " + cameraDistance + " " + cameraDistance / 10 + "转化为16禁止二的字符串" + mCarameLimitDistance);
            SendDefPool.stu2.setmForntLimitSpeedDistance(mCarameLimitDistance);
        } else {
            cameraType = cameraType;
            String gaoCamereTyle = SendUtils.getGaoCamereTyle(cameraType, nomal);
            Log.i(APP_TEST2, "获取当前摄像头类型-> " + nomal + " " + cameraType + " " + gaoCamereTyle);
            SendDefPool.stu2.setmCamereStyle(gaoCamereTyle);
            int cameraSpeed = aMapNaviCameraInfo.getCameraSpeed();
            String mCarameLimitSpeed = SendUtils.hexAdd0Oprate(Integer.toHexString(cameraSpeed));
            Log.i(APP_TEST2, "获取当前摄像头的限速大小-> " + cameraSpeed + " " + cameraSpeed + "转化为16禁止二的字符串" + mCarameLimitSpeed);
            SendDefPool.stu2.setmCurCarLimitSpped(mCarameLimitSpeed);
            int cameraDistance = aMapNaviCameraInfo.getCameraDistance();
            String mCarameLimitDistance = SendUtils.hexAdd0Oprate(Integer.toHexString(cameraDistance / 10));
            Log.i(APP_TEST2, "获取当前位置到摄像头的距离-> " + cameraDistance + " " + cameraDistance / 10 + "转化为16禁止二的字符串" + mCarameLimitDistance);
            SendDefPool.stu2.setmCur_Camere_Distance(mCarameLimitDistance);
        }
        String all2 = CRC64.getAll2(SendDefPool.stu2.toString());
        MainActivity.mt.setSta(all2);

    }

    //服务区
    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {
        AMapServiceAreaInfo aMapServiceAreaInfo = aMapServiceAreaInfos[0];
        //最近一个服务区的名称
        String name = aMapServiceAreaInfo.getName();
        //当前位置到服务区的距离
        int remainDist = aMapServiceAreaInfo.getRemainDist();
        int type = aMapServiceAreaInfo.getType();
        Log.i(APP_TEST, "服务区信息name " + name + " remainDist  " + remainDist);
    }

    //路口放大图
    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {
        Log.i(APP_TEST, "路口放大图 ");
    }

    //关闭路口放大图回调。
    @Override
    public void hideCross() {
        Log.i(APP_TEST, "关闭路口放大图回调 ");
    }

    //显示道路信息回调。
    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {
        Log.i(APP_TEST3, "显示道路信息回调");
        /*AMapLaneInfo aMapLaneInfo = aMapLaneInfos[0];*/
        String mInfo = "";
        StringBuffer sb = new StringBuffer();
        String mCurRoadUseInfo = "";
        for (int i = 0; i < aMapLaneInfos.length; i++) {
            String laneTypeIdHexString = aMapLaneInfos[i].getLaneTypeIdHexString();
            boolean recommended = aMapLaneInfos[i].isRecommended();
            Log.i(APP_TEST3, i + " ->  " + laneTypeIdHexString + " " + recommended);
            if (recommended) {
                mCurRoadUseInfo += "1";
            } else {
                mCurRoadUseInfo += "0";
            }
            String substring = laneTypeIdHexString.substring(0, 1);
            int i1 = Integer.parseInt(substring) + 1;
            String s = String.valueOf(i1);
            sb.append(s);
        }
        Log.i(APP_TEST3, sb.toString() + "前 ");
        String lanCarInfo = BaseUtils.getLanCarInfo(sb.toString());
        Log.i(APP_TEST3, lanCarInfo + "中 ");
        String mAllCarRoadInfo = BaseUtils.getFill1ByteInfo(lanCarInfo);
        SendDefPool.stu2.setmCarRoadInfo(mAllCarRoadInfo);
        String mUseCurUseInfo = BaseUtils.getFill1fByteInfo(mCurRoadUseInfo);
        String mHexCurUseInfo = BaseUtils.getBinaryString2hexString(mUseCurUseInfo);
        Log.i(APP_TEST3, mCurRoadUseInfo + "  后 " + " " + mHexCurUseInfo);
        SendDefPool.stu2.setmCur_Use_Car_info(mHexCurUseInfo);
        String all2 = CRC64.getAll2(SendDefPool.stu2.toString());
        MainActivity.mt.setSta(all2);
    }

    //关闭道路信息回调。
    @Override
    public void hideLaneInfo() {
        Log.i(APP_TEST, "关闭道路信息回调");
        SendDefPool.stu2.setmCarRoadInfo(FModeData2.m_CarRoadInfo);
        SendDefPool.stu2.setmCur_Use_Car_info(FModeData2.m_Cur_Use_Car_info);
        String all2 = CRC64.getAll2(SendDefPool.stu2.toString());
        MainActivity.mt.setSta(all2);
    }

    @Override
    public void onCalculateMultipleRoutesSuccess(int[] ints) {
        AMapNaviPath path = new AMapNaviPath();
    }

    //通知当前是否显示平行路切换
    @Override
    public void notifyParallelRoad(int i) {
        if (i == 0) {

            Log.d("wlx", "当前在主辅路过渡");
            return;
        }
        if (i == 1) {


            Log.d("wlx", "当前在主路");
            return;
        }
        if (i == 2) {
            Log.d("wlx", "当前在辅路");
        }

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {
        int broadcastType = aMapNaviTrafficFacilityInfo.getBroadcastType();
        Log.i(APP_TEST4, " 诱导信息------------------已过时->  broadcastType " + broadcastType + " " + SendUtils.getInduceInfo(broadcastType));


        //已过时
    }

    /**
     * 道路设施信息结构体   对应的诱导信息
     * OnUpdateTrafficFacility
     *
     * @param aMapNaviTrafficFacilityInfos 在巡航过程中，出现特殊道路设施
     *                                     （如：测速摄像头、测速雷达；违章摄像头；铁路道口；应急车道等等）时，
     *                                     回进到 OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] infos)，
     *                                     通过 AMapNaviTrafficFacilityInfo  对象可获取道路交通设施信息。
     */
    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {
//setmInduceInfo    22
        int broadcastType1 = aMapNaviTrafficFacilityInfos[0].getBroadcastType();
        String induceInfo = SendUtils.getInduceInfo(broadcastType1);
        SendDefPool.stu2.setmInduceInfo(induceInfo);
        String all2 = CRC64.getAll2(SendDefPool.stu2.toString());
        MainActivity.mt.setSta(all2);

     /*   for (AMapNaviTrafficFacilityInfo info :
                aMapNaviTrafficFacilityInfos) {
            int broadcastType = info.getBroadcastType();
            Toast.makeText(this, info.getBroadcastType(), Toast.LENGTH_SHORT).show();
            Log.i(APP_TEST4, " 诱导信息------------------->  broadcastType " + broadcastType + " " + SendUtils.getInduceInfo(broadcastType));
        }*/
    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

        //已过时
    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {
        Toast.makeText(this, "看log", Toast.LENGTH_SHORT).show();
        Log.d(APP_TEST4, "distance=" + aimLessModeStat.getAimlessModeDistance());
        Log.d(APP_TEST4, "time=" + aimLessModeStat.getAimlessModeTime());
    }

    /**
     * 道路拥堵信息
     * AimLessModeCongestionInfo
     *
     * @param aimLessModeCongestionInfo 巡航模式（无路线规划）下道路拥堵信息类
     */

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

        String nomal = SendDefPool.stu2.getmCamereStyle();
        //setmCamereStyle  16   0 1 2 3 4
        AMapCongestionLink[] amapCongestionLinks = aimLessModeCongestionInfo.getAmapCongestionLinks();
        int congestionStatus = amapCongestionLinks[0].getCongestionStatus();
        String gaoCrowdInfo = SendUtils.getGaoCrowdInfo(congestionStatus, nomal);
        SendDefPool.stu2.setmCamereStyle(gaoCrowdInfo);
        String all2 = CRC64.getAll2(SendDefPool.stu2.toString());
        MainActivity.mt.setSta(all2);
      /*  for (int i = 0; i < amapCongestionLinks.length; i++) {
            int congestionStatus = amapCongestionLinks[i].getCongestionStatus();



            ArrayList<NaviLatLng> coords = amapCongestionLinks[i].getCoords();
            for (int j=0;j<coords.size();j++){
                double latitude = coords.get(j).getLatitude();
                double longitude = coords.get(j).getLongitude();
            }
            Log.i(APP_TEST5, "拥堵道路状态值  ---》》》" + congestionStatus);
        }*/
    }

    //回调各种类型的提示音，类似高德导航"叮".
    @Override
    public void onPlayRing(int i) {


    }

    /**
     * interface AMapNaviViewListener  导航视图事件监听。
     */

    //界面右下角功能设置按钮的回调接口。
    @Override
    public void onNaviSetting() {

    }


    //导航页面左下角返回按钮点击后弹出的『退出导航对话框』中选择『确定』后的回调接口。
    @Override
    public void onNaviCancel() {
        Log.i(APP_TEST, "onNaviCancel 导航取消");
        String s8 = SendDefPool.stu2.getmDestComp();
        String alterhor = SendUtils.getNvGpsAll(SendUtils.setNavState(3, s8));
        SendDefPool.stu2.setmDestComp(alterhor);
        SendDefPool.stu2.setmTbtInfo(FModeData2.m_TbtInfo);
        SendDefPool.stu2.setmNextRoadDistance(FModeData2.m_NextRoadDistance);
        SendDefPool.stu2.setmDestInfo(FModeData2.m_DestInfo);
        SendDefPool.stu2.setmCurCarSpeed(FModeData2.m_CurCarSpeed);
        SendDefPool.stu2.setmCurCarLimitSpped(FModeData2.m_CurCarLimitSpped);
        SendDefPool.stu2.setmForntLimitSpeedDistance(FModeData2.m_ForntLimitSpeedDistance);
        SendDefPool.stu2.setmCamereStyle(FModeData2.m_CamereStyle);
        SendDefPool.stu2.setmCur_Camere_Distance(FModeData2.m_Cur_Camere_Distance);
        SendDefPool.stu2.setmCarRoadInfo(FModeData2.m_CarRoadInfo);
        SendDefPool.stu2.setmInduceInfo(FModeData2.m_InduceInfo);
        SendDefPool.stu2.setmCur_Use_Car_info(FModeData2.m_Cur_Use_Car_info);
        SendDefPool.stu2.setmNextRoadName(FModeData2.m_NextRoadName);
        SendDefPool.stu2.setmCurRoadName(FModeData2.m_CurRoadName);
        String all2 = CRC64.getAll2(SendDefPool.stu2.toString());
        MainActivity.mt.setSta(all2);
        finish();
    }

    //Log.i(APP_TEST, "导航页面左下角返回按钮的回调接口 false-由SDK主动弹出『退出导航』对话框，true-SDK不主动弹出『退出导航对话框』，由用户自定义");
    @Override
    public boolean onNaviBackClick() {
        Log.i(APP_TEST, "导航页面左下角返回按钮的回调接口 false-由SDK主动弹出『退出导航』对话框，true-SDK不主动弹出『退出导航对话框』，由用户自定义");
        return false;
    }

    //导航界面地图状态的回调。
    @Override
    public void onNaviMapMode(int i) {

    }

    //界面左上角转向操作的点击回调。
    @Override
    public void onNaviTurnClick() {
        Log.i(APP_TEST, "界面左上角转向操作的点击回调。");
    }

    //界面下一道路名称的点击回调。
    @Override
    public void onNextRoadClick() {
        Log.i(APP_TEST, "界面下一道路名称的点击回调。");
    }

    //界面全览按钮的点击回调
    @Override
    public void onScanViewButtonClick() {

    }

    //是否锁定地图的回调。
    @Override
    public void onLockMap(boolean b) {

    }

    //导航view加载完成回调。
    @Override
    public void onNaviViewLoaded() {

    }

    // interface  AMapHudViewListener  点击AMapHudView中的返回按钮将回调此接口
    @Override
    public void onHudViewCancel() {
        Log.i(APP_TEST, "点击AMapHudView中的返回按钮将回调此接口");
    }


}
