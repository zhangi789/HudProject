package com.shdnxc.cn.activity.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.shdnxc.cn.activity.R;
import com.shdnxc.cn.activity.Utils.BaseUtils;
import com.shdnxc.cn.activity.Utils.ConstantUils;
import com.shdnxc.cn.activity.Utils.SharedUtils;
import com.shdnxc.cn.activity.adapter.BaiduIndexAdapter;
import com.shdnxc.cn.activity.app.IAPI;
import com.shdnxc.cn.activity.bean.BaiduIndexBean;
import com.shdnxc.cn.activity.dbDao.RecordsDao;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static com.shdnxc.cn.activity.Utils.GPSUtil.gcj02_To_Bd09;

/**
 * Created by Zheng Jungen on 2017/4/7.
 */
public class BaiDuIndexActivity extends Activity implements View.OnClickListener, PermissionListener {
    //请求失败标志
    private static final int REQUEST_CODE_PERMISSION_OTHER = 101;
    // 用户取消后重新提醒
    private static final int REQUEST_CODE_SETTING = 300;
    //判断是否需要权限
    private boolean isOrNotRequestPer;
    private RelativeLayout mBack;
    private ImageView iv_change, edit_line, edit_line2;
    private RelativeLayout re_contor1, re_contor2;
    private EditText edit_current, edit_aim;
    private EditText edit_current2, edit_aim2;
    private boolean isShowRe;
    private Button btn_nav;
    private ListView lv_nav;
    private PoiSearch mPoiSearch;
    private SuggestionSearch mSuggestionSearch;
    public static BaiDuIndexActivity instance = null;
    List<String> recordsList;

    public AMapLocationClientOption mLocationOption = null;
    public AMapLocationClient mlocationClient;
    private ArrayList<BaiduIndexBean> mBaiIndexInfo;
    //是否是第二次
    private boolean isBaiduSecord;

    private Button btn_delefe;
    //阿里当前定位地址，经度，纬度,城市
    private String staRoadName, staLong, staLat, curCity;
    //将获得的数据接收  在updateReceivedData更新数据
    private final BroadcastReceiver mBbleIntentReceiver = new BroadcastReceiver() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onReceive(Context context, Intent intent) {
            Message mMsg = Message.obtain();
            final String action = intent.getAction();
            double mLatude = intent.getDoubleExtra("mLatude", 0.0);
            double mLongtude = intent.getDoubleExtra("mLongtude", 0.0);
            String mAddName = intent.getStringExtra("mAddName");
            Log.i("=====", " " + mAddName + " " + ConstantUils.BAIDU_END);
            //成功的回掉
            if (ConstantUils.BAIDU_END.equals(action)) {
                Log.i("=====", " " + ConstantUils.BAIDU_END);
                edit_aim.setText(mAddName);

                SharedUtils.saveString(BaiDuIndexActivity.this, "end", mAddName + "," + mLatude + "," + mLongtude);

            } else if (ConstantUils.BAIDU_SECORD_END.equals(action)) {
                Log.i("====", " " + ConstantUils.BAIDU_SECORD_END);
                edit_aim2.setText(mAddName);
                SharedUtils.saveString(BaiDuIndexActivity.this, "end", mAddName + "," + mLatude + "," + mLongtude);
            } else if (ConstantUils.BAIDU_CURRENT.equals(action)) {
                Log.i("====", " " + ConstantUils.BAIDU_CURRENT);
                edit_current.setText(mAddName);
                SharedUtils.saveString(BaiDuIndexActivity.this, "start", mAddName + "," + mLatude + "," + mLongtude);

            } else if (ConstantUils.BAIDU_SECORD_CURRENT.equals(action)) {
                Log.i("====", " " + ConstantUils.BAIDU_SECORD_CURRENT);
                edit_current2.setText(mAddName);
                SharedUtils.saveString(BaiDuIndexActivity.this, "start", mAddName + "," + mLatude + "," + mLongtude);
            } else if (ConstantUils.BAIDU_CONN_STATE_SUCCESS.equals(action)) {
                isBaiduSecord = true;
            } else if (ConstantUils.BAIDU_CONN_STATE_FAILSE.equals(action)) {
                isBaiduSecord = false;
            }
        }
    };
    private RecordsDao mDbDao;
    private BaiduIndexAdapter baiduIndexAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidu_index);
        instance = this;
        SharedUtils.saveString(BaiDuIndexActivity.this, ConstantUils.CU_CAR, "YES");
        initview();
        mDbDao = new RecordsDao(this);
        mBaiIndexInfo = new ArrayList<>();
//        initBaiduSerchData();
        initLenter();

     /*   lv_nav.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("RTYU", mBaiIndexInfo.get(position).getAddresName() + "-------->" + position);
                Log.i("RTYU", mBaiIndexInfo.size()-position + " --------> " + position);
                BaiduIndexBean baiduIndexBean = mBaiIndexInfo.get(position);
                String s = baiduIndexBean.getAddresName() + "," + String.valueOf(baiduIndexBean.getmLatitude()) + "," + String.valueOf(baiduIndexBean.getmLongitude());
                return true;
            }
        });*/
        lv_nav.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BaiduIndexBean baiduIndexBean = mBaiIndexInfo.get(position);
                String addresName = baiduIndexBean.getAddresName();
                double mLa = baiduIndexBean.getmLatitude();
                String mmLa = String.valueOf(mLa);
                double mLo = baiduIndexBean.getmLongitude();
                String mmL0 = String.valueOf(mLo);
                Log.i("====", mLa + " ---------------> " + mLo);
                boolean net = IAPI.isNet(BaiDuIndexActivity.this);
                if (net) {
                    if (IAPI.WHICH_MAP.equals("1")) {

                        if (staLat != null && staLong != null) {
                            Intent intent = null;
                            try {
                                intent = Intent.getIntent("intent://map/direction?" +
                                        "origin=当前位置&" +
                                        "origin=name:" + staRoadName + "|latlng:" + staLat + "," + staLong +
                                        "&destination=name:" + addresName + "|latlng:" + mLa + "," + mLo +
                                        "&mode=driving&" +
                                        "&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                                startActivity(intent); //启动调用
                                SharedUtils.saveString(BaiDuIndexActivity.this, "start", "YES");
                                SharedUtils.saveString(BaiDuIndexActivity.this, "end", "YES");
                                BaiDuIndexActivity.instance.finish();
                            } catch (URISyntaxException e) {
                                Log.e("intent", e.getMessage());
                            }
                        } else {
                            Toast.makeText(BaiDuIndexActivity.this, "定位失败,重新定位", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (staLat != null && staLong != null) {
                            Intent intent = new Intent(BaiDuIndexActivity.this, GaoActivity.class);
                            intent.putExtra("mStartLa", staLat);
                            intent.putExtra("mStartLon", staLong);
                            intent.putExtra("mEndLa", mmLa);
                            intent.putExtra("mEndLon", mmL0);
                            startActivity(intent);
                            BaiDuIndexActivity.instance.finish();
                        } else {
                            Toast.makeText(BaiDuIndexActivity.this, "定位失败,重新定位", Toast.LENGTH_SHORT).show();
                        }
                    }


                } else {
                    Toast.makeText(BaiDuIndexActivity.this, "网络没有打开请打开网络", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setRequest() {
        AndPermission.with(this)
                .requestCode(REQUEST_CODE_PERMISSION_OTHER)
                .permission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
                .rationale((requestCode, rationale) ->
                        // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                        AndPermission.rationaleDialog(BaiDuIndexActivity.this, rationale).show()
                )
                .send();
    }

//    private void initBaiduSerchData() {
//        mSuggestionSearch.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
//            public void onGetSuggestionResult(SuggestionResult res) {
//                if (res == null || res.getAllSuggestions() == null) {
//                    return;
//                    //未找到相关结果
//                } else {
//                    //获取在线建议检索结果
//                    List<SuggestionResult.SuggestionInfo> allSuggestions = res.getAllSuggestions();
//                    for (SuggestionResult.SuggestionInfo pi : allSuggestions) {
//                        Log.i("====", "===信息：" + pi.key + " " + pi.pt);
//                    }
//                }
//
//            }
//        });
//    }

    private void initLenter() {
        iv_change.setOnClickListener(this);
        mBack.setOnClickListener(this);
        btn_nav.setOnClickListener(this);

        edit_current.setOnClickListener(this);
        edit_aim.setOnClickListener(this);

        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();

        //设置定位监听
        mlocationClient.setLocationListener(locationListener);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        mLocationOption.setGpsFirst(true);
        mLocationOption.setWifiScan(true);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
    }

    private void initview() {
        btn_delefe = (Button) findViewById(R.id.btn_delefe);
        btn_delefe.setOnClickListener(this);
        //添加百度地图功能
        mSuggestionSearch = SuggestionSearch.newInstance();
        lv_nav = (ListView) findViewById(R.id.lv_nav);
        btn_nav = (Button) findViewById(R.id.btn_nav);
        mBack = (RelativeLayout) findViewById(R.id.re_btn_enent);
        iv_change = (ImageView) findViewById(R.id.iv_change);
        edit_line = (ImageView) findViewById(R.id.edit_line);
        edit_line2 = (ImageView) findViewById(R.id.edit_line2);
        re_contor1 = (RelativeLayout) findViewById(R.id.re_contor1);
        re_contor2 = (RelativeLayout) findViewById(R.id.re_contor2);
        edit_current = (EditText) findViewById(R.id.edit_current);
        edit_aim = (EditText) findViewById(R.id.edit_aim);
        edit_current2 = (EditText) findViewById(R.id.edit_curren2);
        edit_aim2 = (EditText) findViewById(R.id.edit_aim2);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_delefe:
                mDbDao.deleteAllRecords();
                mBaiIndexInfo.clear();
                baiduIndexAdapter = new BaiduIndexAdapter(BaiDuIndexActivity.this, mBaiIndexInfo);
                lv_nav.setAdapter(baiduIndexAdapter);
                btn_delefe.setVisibility(View.INVISIBLE);
                break;
            //界面正常
            case R.id.edit_current:
                SharedUtils.saveString(BaiDuIndexActivity.this, ConstantUils.BAIDU_CURRENT, "YES");
//                startActivity(new Intent(BaiDuIndexActivity.this, BaiDuSerchActivity.class));
                ActivityAnimation.explodeToSlide(BaiDuIndexActivity.this, BaiDuSerchActivity.class);
                break;
            //界面反转
            case R.id.edit_curren2:

                SharedUtils.saveString(BaiDuIndexActivity.this, ConstantUils.BAIDU_SECORD_CURRENT, "YES");
                ActivityAnimation.explodeToSlide(BaiDuIndexActivity.this, BaiDuSerchActivity.class);
                break;

            //界面正常
            case R.id.edit_aim:
                SharedUtils.saveString(BaiDuIndexActivity.this, ConstantUils.BAIDU_END, "YES");
                ActivityAnimation.explodeToSlide(BaiDuIndexActivity.this, BaiDuSerchActivity.class);

                break;
            //界面反转
            case R.id.edit_aim2:
                SharedUtils.saveString(BaiDuIndexActivity.this, ConstantUils.BAIDU_SECORD_END, "YES");
                ActivityAnimation.explodeToSlide(BaiDuIndexActivity.this, BaiDuSerchActivity.class);
                break;
            //切换布局方式
            case R.id.iv_change:
                if (isShowRe) {
                    re_contor1.setVisibility(View.INVISIBLE);
                    re_contor2.setVisibility(View.VISIBLE);
                    isShowRe = false;
                } else {
                    re_contor1.setVisibility(View.VISIBLE);
                    re_contor2.setVisibility(View.INVISIBLE);
                    isShowRe = true;
                }

                break;
            //返回键处理
            case R.id.re_btn_enent:
                BaiDuIndexActivity.this.finish();
                break;
            case R.id.btn_nav:

                // startActivity(new Intent(this, IntelligentBroadcastActivity.class));
                setFinishNav();
                break;
        }
    }

    private void setFinishNav() {
        String mEnd = SharedUtils.getString(BaiDuIndexActivity.this, "end", "YES");
        String mStart = SharedUtils.getString(BaiDuIndexActivity.this, "start", "YES");
        //第二次运行
        boolean net = IAPI.isNet(BaiDuIndexActivity.this);
        if (net) {
            if (IAPI.WHICH_MAP.equals("1")) {
                if (isBaiduSecord) {
                    Intent i1 = new Intent();
                    i1.setData(Uri.parse("baidumap://map?"));
                    startActivity(i1);
                } else {
                    //是否存在百度地图
                    if (BaseUtils.isAvilible(BaiDuIndexActivity.this, ConstantUils.BAIDUNAME)) {
                        //并且已经存在版本大于788
                        if (BaseUtils.baiDuVersion(BaiDuIndexActivity.this, ConstantUils.BAIDUNAME) >= 788) {
                            if (!mEnd.equals("YES") && !mStart.equals("YES")) {
                                String[] mEndPostion = mEnd.split(",");
                                String mEndRoadName = mEndPostion[0];
                                String mEndLa = mEndPostion[1];
                                String mEndLon = mEndPostion[2];
                                String[] mStartPostion = mStart.split(",");
                                String mStartRoadName = mStartPostion[0];
                                String mStartLa = mStartPostion[1];
                                String mStartLon = mStartPostion[2];


                                Intent intent = null;
                                try {
                                    intent = Intent.getIntent("intent://map/direction?" +
                                            "origin=name:" + mStartRoadName + "|latlng:" + mStartLa + "," + mStartLon +
                                            "&destination=name:" + mEndRoadName + "|latlng:" + mEndLa + "," + mEndLon +
                                            "&mode=driving&" +
                                            "&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                                    startActivity(intent); //启动调用
                                    SharedUtils.saveString(BaiDuIndexActivity.this, "start", "YES");
                                    SharedUtils.saveString(BaiDuIndexActivity.this, "end", "YES");
                                    BaiDuIndexActivity.instance.finish();
                                } catch (URISyntaxException e) {
                                    Log.e("intent", e.getMessage());
                                }
                            } else if (!mEnd.equals("YES") && mStart.equals("YES")) {
                                String[] mEndPostion = mEnd.split(",");
                                String mEndRoadName = mEndPostion[0];
                                String mEndLa = mEndPostion[1];
                                String mEndLon = mEndPostion[2];

                                if (staLat != null) {
                                    Intent intent = null;
                                    try {
                                        intent = Intent.getIntent("intent://map/direction?" +
                                                "origin=name:" + staRoadName + "|latlng:" + staLat + "," + staLong +
                                                "&destination=name:" + mEndRoadName + "|latlng:" + mEndLa + "," + mEndLon +
                                                "&mode=driving&" +
                                                "&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                                        startActivity(intent); //启动调用
                                        SharedUtils.saveString(BaiDuIndexActivity.this, "start", "YES");
                                        SharedUtils.saveString(BaiDuIndexActivity.this, "end", "YES");
                                        BaiDuIndexActivity.instance.finish();
                                    } catch (URISyntaxException e) {
                                        Log.e("intent", e.getMessage());
                                    }
                                } else {
                                    Toast.makeText(BaiDuIndexActivity.this, "正在定位中请稍等...", Toast.LENGTH_SHORT).show();
                                }

                            } else if ((mEnd.equals("YES"))) {
                                Toast.makeText(BaiDuIndexActivity.this, "请输入目的地", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(BaiDuIndexActivity.this, "百度地图版本太低请删除下载高版本", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        //未安装直接跳到安装界面
                        Uri uri = Uri.parse("market://details?id=com.baidu.BaiduMap");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }

                }
            } else {

                if (staLat != null) {

                    if (!mEnd.equals("YES")) {
                        String[] mEndPostion = mEnd.split(",");
                        String mEndRoadName = mEndPostion[0];
                        String mEndLa = mEndPostion[1];
                        String mEndLon = mEndPostion[2];
                        Intent intent = new Intent(BaiDuIndexActivity.this, GaoActivity.class);
                        intent.putExtra("mStartLa", staLat);
                        intent.putExtra("mStartLon", staLong);
                        intent.putExtra("mEndLa", mEndLa);
                        intent.putExtra("mEndLon", mEndLon);
                        startActivity(intent);
                        BaiDuIndexActivity.instance.finish();
                    } else {
                        Toast.makeText(BaiDuIndexActivity.this, "目的地不能为空", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(BaiDuIndexActivity.this, "正在定位中请稍等...", Toast.LENGTH_SHORT).show();
                }

            }

        } else {
            Toast.makeText(BaiDuIndexActivity.this, "网络没有打开请打开网络", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onDestroy() {
        mlocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
        unregisterReceiver(mBbleIntentReceiver);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        isOrNotRequestPer = BaseUtils.isNeedPerssion();
        //请求权限
        if (isOrNotRequestPer) {
            setRequest();
        }
        registerReceiver(mBbleIntentReceiver, mBleIntentFilter());
        mBaiIndexInfo.clear();
        recordsList = mDbDao.getRecordsList();
        Log.i("====", "===信息：" + recordsList.size() + "              recordsList");
        if (recordsList.size() != 0) {
            for (int i = recordsList.size() - 1; i >= 0; i--) {
                lv_nav.setVisibility(View.VISIBLE);
                btn_delefe.setVisibility(View.VISIBLE);
                String names = recordsList.get(i);
                String[] split = names.split(",");
                double v = Double.parseDouble(split[1]);
                Double.parseDouble(split[2]);
                mBaiIndexInfo.add(new BaiduIndexBean(split[0], Double.parseDouble(split[1]), Double.parseDouble(split[2])));

                baiduIndexAdapter = new BaiduIndexAdapter(BaiDuIndexActivity.this, mBaiIndexInfo);
                lv_nav.setAdapter(baiduIndexAdapter);
                lv_nav.setSelection(0);
                lv_nav.setDivider(null);
                lv_nav.setOverScrollMode(View.OVER_SCROLL_NEVER);
                Log.i("====", i + "     ----------------" + split[0] + "->" + Double.parseDouble(split[1]) + " ->" + Double.parseDouble(split[2]));
            }
        }
        super.onResume();
    }

    //对蓝牙服务的支持
    private static IntentFilter mBleIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantUils.BAIDU_END);
        intentFilter.addAction(ConstantUils.BAIDU_SECORD_END);
        intentFilter.addAction(ConstantUils.BAIDU_CURRENT);
        intentFilter.addAction(ConstantUils.BAIDU_SECORD_CURRENT);
        intentFilter.addAction(ConstantUils.BAIDU_CONN_STATE_SUCCESS);
        intentFilter.addAction(ConstantUils.BAIDU_CONN_STATE_FAILSE);
        return intentFilter;
    }


    @Override
    public void onSucceed(int requestCode, List<String> grantPermissions) {

    }

    @Override
    public void onFailed(int requestCode, List<String> deniedPermissions) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_OTHER:
                Toast.makeText(this, "权限申请失败请重新请求", Toast.LENGTH_SHORT).show();

                break;
        }
        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            //第二种：用自定义的提示语。
            AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING)
                    .setTitle("权限申请失败")
                    .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
                    .setPositiveButton("好，去设置")
                    .show();
        }
    }

    //----------------------------------权限回调处理----------------------------------//
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /**
         * 转给AndPermission分析结果。
         *
         * @param requestCode  请求码。
         * @param permissions  权限数组，一个或者多个。
         * @param grantResults 请求结果。
         * @param listener PermissionListener 对象。
         */
        AndPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_SETTING) {
            Toast.makeText(this, "重新请求权限成功可以正常使用", Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {
                double[] newpotion = gcj02_To_Bd09(location.getLatitude(), location.getLongitude());
                StringBuffer sb = new StringBuffer();
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if (location.getErrorCode() == 0) {
                    if (location.getLocationType() != 6 && location.getAccuracy() < 200) {
                        staRoadName = location.getAddress();
                        if (IAPI.WHICH_MAP.equals("1")) {
                            staLong = String.valueOf(newpotion[1]);
                            staLat = String.valueOf(newpotion[0]);
                        } else {
                            staLong = String.valueOf(location.getLongitude());
                            staLat = String.valueOf(location.getLatitude());
                        }
                        edit_current.setHint("当前位置:" + location.getAddress());

                        mlocationClient.stopLocation();
                        sb.append("定位成功" + "\n");
                        sb.append("定位类型: " + location.getLocationType() + "\n");
                        sb.append("经    度    : " + location.getLongitude() + "\n");
                        sb.append("纬    度    : " + location.getLatitude() + "\n");
                        sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
                        sb.append("提供者    : " + location.getProvider() + "\n");

                        sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
                        sb.append("角    度    : " + location.getBearing() + "\n");
                        // 获取当前提供定位服务的卫星个数
                        sb.append("星    数    : " + location.getSatellites() + "\n");
                        sb.append("国    家    : " + location.getCountry() + "\n");
                        sb.append("省            : " + location.getProvince() + "\n");
                        sb.append("市            : " + location.getCity() + "\n");
                        sb.append("城市编码 : " + location.getCityCode() + "\n");
                        sb.append("区            : " + location.getDistrict() + "\n");
                        sb.append("区域 码   : " + location.getAdCode() + "\n");
                        sb.append("地    址    : " + location.getAddress() + "\n");
                        sb.append("兴趣点    : " + location.getPoiName() + "\n");
                        //定位完成的时间
//                    sb.append("定位时间: " + Utils.formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");
                    }
                } else {
                    //定位失败
                    sb.append("定位失败" + "\n");
                    sb.append("错误码:" + location.getErrorCode() + "\n");
                    sb.append("错误信息:" + location.getErrorInfo() + "\n");
                    sb.append("错误描述:" + location.getLocationDetail() + "\n");
                }
                //定位之后的回调时间
//                sb.append("回调时间: " + Utils.formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");

                //解析定位结果，
                String result = sb.toString();
                Log.i("BaiDuIndexActivity", "onLocationChanged: " + sb);
//                tv.setText(result);
            } else {
//                tv.setText("定位失败，loc is null");
            }
        }
    };
}
