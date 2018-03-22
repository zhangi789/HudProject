package com.shdnxc.cn.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ble.api.DataUtil;
import com.ble.ble.BleService;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.shdnxc.cn.activity.Utils.BaseUtils;
import com.shdnxc.cn.activity.Utils.BitmapUtils;
import com.shdnxc.cn.activity.Utils.CRC64;
import com.shdnxc.cn.activity.Utils.ConstantUils;
import com.shdnxc.cn.activity.Utils.FileUtils;
import com.shdnxc.cn.activity.Utils.HandlerUtil;
import com.shdnxc.cn.activity.Utils.LeProxy;
import com.shdnxc.cn.activity.Utils.SendDefPool;
import com.shdnxc.cn.activity.Utils.SendUtils;
import com.shdnxc.cn.activity.Utils.SharedUtils;
import com.shdnxc.cn.activity.Utils.WeiYunURL;
import com.shdnxc.cn.activity.app.IAPI;
import com.shdnxc.cn.activity.bean.CarDevBean;
import com.shdnxc.cn.activity.bean.NormalSendMode;
import com.shdnxc.cn.activity.fragment.DiscoverFragment;
import com.shdnxc.cn.activity.fragment.HudFragment;
import com.shdnxc.cn.activity.fragment.SettingFragment;
import com.shdnxc.cn.activity.okgo.JsonCallbackABC;
import com.shdnxc.cn.activity.receiver.ScreenBroadcastReceiver;
import com.shdnxc.cn.activity.servce.GrayService;
import com.shdnxc.cn.activity.servce.LocalService;
import com.shdnxc.cn.activity.servce.WatchService;
import com.shdnxc.cn.activity.ui.AboutActiivty;
import com.shdnxc.cn.activity.ui.AppSettingActivity;
import com.shdnxc.cn.activity.ui.DeviceActivity;
import com.shdnxc.cn.activity.ui.LoginActivity;
import com.shdnxc.cn.activity.ui.ObdActivity;
import com.shdnxc.cn.activity.ui.OpinonActivity;
import com.shdnxc.cn.activity.ui.PersonInfoActivity;
import com.shdnxc.cn.activity.view.RoundImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.UUID;

import it.sephiroth.android.library.picasso.Picasso;
import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends FragmentActivity implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int PERMISSIONS_REQUEST_PHOTO = 0x01;
    private static final int PERMISSIONS_REQUEST_FILE = 0x02;
    private static final int REQUEST_CODE_TAKING_PHOTO = 0x03;
    private static final int REQUEST_CODE_SELECT_PHOTO_FROM_LOCAL = 0x04;
    private static final int REQUEST_CODE_CUT_PHOTO = 0x05;
    private String mPath;
    private CharSequence[] mItems = {"拍照上传", "选择图片"};
    private boolean isTakePhoto = false;
    private boolean isGetPic = false;
    private static final int TARGET_HEAD_SIZE = 150;
    private static final String IMAGE_SAVE_DIR = Environment.getExternalStorageDirectory().getPath() + "/yulin/photo";
    private static final String IMAGE_SAVE_PATH = IMAGE_SAVE_DIR + "/demo.jpg";
    private Uri mUri;
    /**
     * 图片上传
     */
    private final static String BLACK_WAKE_ACTION2 = "com.wake.black";
    private final static String BLACK_WAKE_ACTION = "com.wake.gray";
    private PowerManager.WakeLock mWakeLock = null;
    DrawerLayout layout;
    NavigationView nv;
    Toolbar tb;
    TabLayout tabButton;
    RoundImageView mImg;
    ArrayList<Fragment> fragments = new ArrayList<>();
    private FragmentManager manager;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private boolean isOpen = false;
    public static BleService mLeService;
    private String token;
    private IAPI mApi;
    public static MainActivity instance = null;
    String reciveMacData = "";

    public static SendDefPool mt;

    private BluetoothAdapter mBluetoothAdapter;
    private static final String TAG = "MainActivity";
    private static final int REQUEST_ENABLE_BT = 2;
    public static boolean isConn = true;
    private boolean connected = false;
    private Handler mMainHandler;
    private Runnable runnable;
    private String macAddress;
    private RelativeLayout re_back_login;
    private ImageView toor_iv_detection;
    private Fragment[] mFragments;
    private RadioGroup rgTools;
    private RadioButton rbHome, rbSett, rbfind;
    private int mIndex;
    private static Boolean isAppExit = false;
    public int flag = 1;


    public static NormalSendMode mSendGaoValue;
    int mFailseCount = 0;
    int mCRCFaiseCount = 0;
    int mTimerCounts = 0;
    private boolean isOpenSystem = true;
    Timer timer;
    private ImageView iv_hud;
    private TextView toor_other_title;
    RelativeLayout rl_mac, rl_app, rl_theme, rl_msg, rl_back, rl_min, rl_hepe;
    //头像各性name    昵称
    private TextView mCharName;
    private TextView mName;
    IAPI mAPi;
    private String mShareNiName;
    private String mShareChaName;

    private Button btn_cricle;
    //用户昵称
    private String realname = "";
    //用户头像网址
    private String avatar = "";
    private boolean isShowFind = true;
    Handler mConnHander = new Handler();


    //屏幕开关广播
    ScreenBroadcastReceiver receiver;
    private String mStoreLight;
    private String mStoreVoice;

    private void regScreen() {
        receiver = new ScreenBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(receiver, filter);
    }


    /**
     * 状态控制蓝牙再次进来连接的状态
     */
    Runnable mConnRunable = new Runnable() {
        @Override
        public void run() {
            try {
                if (mBluetoothAdapter.isEnabled() && LeProxy.mBleService.getConnectionState(BaseUtils.getMacStr(SharedUtils.getString(MainActivity.this, ConstantUils.CU_SPLASH, "0"))[2]) != 2) {
                    Log.i("result", "蓝牙打开:---->> " + isAgainOpenBle);
                    mLeProxy.connect(BaseUtils.getMacStr(SharedUtils.getString(MainActivity.this, ConstantUils.CU_SPLASH, "0"))[2], false);
                } else if (!mBluetoothAdapter.isEnabled()) {
                    Intent mConnIntent = new Intent();
                    mConnIntent.setAction(ConstantUils.BLE_START_NO_START);
                    sendBroadcast(mConnIntent);
                    Log.i("result", "蓝牙未打开:----------------------------->> " + mBluetoothAdapter.isEnabled());
                }
                mConnHander.postDelayed(this, 3000);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };
    private final BroadcastReceiver mLocalReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String address = intent.getStringExtra(LeProxy.EXTRA_ADDRESS);
            switch (intent.getAction()) {
                case LeProxy.ACTION_GATT_CONNECTED:
                    Log.i("result", "蓝牙连接成功 ");
                    break;
                case LeProxy.ACTION_GATT_DISCONNECTED:
                    Intent mConnIntent = new Intent();
                    mConnIntent.setAction(ConstantUils.BLE_CONN_BREAK);
                    sendBroadcast(mConnIntent);
                    isAgainOpenBle = false;
                    Log.i("result", "蓝牙HUD断开");
                    Log.i("result", "蓝牙HUD断开----------------------------->> ");
                    break;
                case LeProxy.ACTION_CONNECT_ERROR:
                    Intent mConnIntent2 = new Intent();
                    mConnIntent2.setAction(ConstantUils.BLE_CONN_FALSE);
                    sendBroadcast(mConnIntent2);
                    Log.i("result", "蓝牙连接错误");
                    isAgainOpenBle = false;
                    break;
                case LeProxy.ACTION_CONNECT_TIMEOUT:
                 /*   Intent mConnIntent3 = new Intent();
                    mConnIntent3.setAction(ConstantUils.BLE_CONN_FALSE);
                    sendBroadcast(mConnIntent3);*/
                    Log.i("result", "蓝牙连接超时");
                    isAgainOpenBle = false;
                    break;
                case LeProxy.ACTION_DATA_AVAILABLE:
                    Log.i("result", "蓝牙开发数据");
                    String stringExtra = intent.getStringExtra(LeProxy.EXTRA_ADDRESS);
                    String hexData = DataUtil.byteArrayToHex(intent.getByteArrayExtra(LeProxy.EXTRA_DATA));
//成功接收的数据
                    reviceHudData(stringExtra, hexData);
                    break;
            }
        }
    };
    public static LeProxy mLeProxy;

    private IntentFilter mMikeFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(LeProxy.ACTION_GATT_CONNECTED);
        filter.addAction(LeProxy.ACTION_GATT_DISCONNECTED);
        filter.addAction(LeProxy.ACTION_CONNECT_ERROR);
        filter.addAction(LeProxy.ACTION_CONNECT_TIMEOUT);
        filter.addAction(LeProxy.ACTION_GATT_SERVICES_DISCOVERED);
        filter.addAction(LeProxy.ACTION_DATA_AVAILABLE);
        return filter;
    }

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.w(TAG, "onServiceDisconnected()");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LeProxy.getInstance().setBleService(service);
        }
    };
    private boolean isAgainOpenBle = false;
    Handler mHandler = new Handler() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            Bundle data = msg.getData();
            switch (msg.what) {
                case 111:
                    isAppExit = false;
                    break;
                case HandlerUtil.NORMAL_FROM_HUD_TO_APP_DATA:
                    String mac = data.getString(HandlerUtil.EXTRA_MAC);
                    String getData = data.getString(HandlerUtil.EXTRA_DATA);
                    String[] defInformation = BaseUtils.getArrayFromHudToApp(BaseUtils.removeSpace(getData));
                    String mCRC = defInformation[2];
                    String mMagice = defInformation[3];
                    String mDataSegemt = defInformation[4];
                    String mCRCInfo = mMagice + mDataSegemt;

                    String aToHCRC = CRC64.getAToHCRC(mCRCInfo);
                    /**
                     *  计算CRC校验码失败的次数   等于0  表示相等
                     *  次数等于60   则发送CRC校验失败的广播
                     *
                     */
                    int equals = mCRC.compareToIgnoreCase(aToHCRC);
                    if (equals != 0) {
                        mCRCFaiseCount++;
                        if (mCRCFaiseCount == 30) {
                            Log.i("nnn", mCRCFaiseCount + " ");
                            Intent intent = new Intent();
                            intent.setAction(ConstantUils.BLE_CRC_ERROR);
                            sendBroadcast(intent);
                            connected = true;
                            mCRCFaiseCount = 0;
                        }
                    }
                    String[] dataSegment = BaseUtils.getDataSegment(defInformation[4]);
                    //控制蓝牙连接逻辑
                    String s = dataSegment[2];
                    //0BD  数据
                    String obdDate = BaseUtils.getOBDDate(dataSegment[10]);

                    int errorCode = BaseUtils.getErrorCode(obdDate);
                    if (errorCode != 0 && isShowFind == true) {
                        btn_cricle.setVisibility(View.VISIBLE);
                        btn_cricle.setText(String.valueOf(errorCode));
                        Log.i("ASP", errorCode + "   errorCode----------------------------------------------------------");
                    } else {
                        btn_cricle.setVisibility(View.INVISIBLE);
                    }
                    String hexTo8Byte = CRC64.getHexTo8Byte(s);
                    String mBleRealState = hexTo8Byte.substring(3, 4);
                    if (mBleRealState.equals("0")) {
                        Intent intent = new Intent();
                        intent.setAction(ConstantUils.BLE_CONN_FALSE);
                        sendBroadcast(intent);
                        mFailseCount++;
                        Log.i("result", " ---失败的状态-->" + isAgainOpenBle);
                    } else {
                        Intent intent = new Intent();
                        intent.setAction(ConstantUils.BLE_CONN_SUCCESS);
                        sendBroadcast(intent);
                        connected = true;
                        isAgainOpenBle = true;
                        mTimerCounts = 0;
                        Log.i("nnn", "---成功的状态-->" + connected + "---->" + isAgainOpenBle);
                    }
                    //设置广播放松出去所有的数据
                    Intent intent = new Intent();
                    intent.setAction(ConstantUils.BLE_HUD_TO_APP);
                    intent.putExtra(ConstantUils.BLE_HUD_TO_APP_KEY, getData);
                    sendBroadcast(intent);
                    break;


            }

        }
    };

    private void reviceHudData(String mac, String hexData) {
        reciveMacData += hexData;
        if (reciveMacData.contains("AA")) {
            int aPostion = reciveMacData.indexOf("AA");
            String reToZero = reciveMacData.substring(0, aPostion);
            reciveMacData = reciveMacData.replace(reToZero, "");
            if (reciveMacData.startsWith("AA") && reciveMacData.endsWith("55")) {
                Bundle data = new Bundle();
                data.putString(HandlerUtil.EXTRA_MAC, mac);
                data.putString(HandlerUtil.EXTRA_DATA, reciveMacData);
                HandlerUtil.handleMsg(mHandler, HandlerUtil.NORMAL_FROM_HUD_TO_APP_DATA, data);
                reciveMacData = "";
            }
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("Path", mPath);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);
        regScreen();
        String nomalTheme = SharedUtils.getString(MainActivity.this, "localTheme", "0");
        instance = this;
        mApi = new IAPI();
        mMainHandler = new Handler(this.getMainLooper());
        token = SharedUtils.getString(MainActivity.this, ConstantUils.CU_LOGIN, "0");

        macAddress = BaseUtils.getMacStr(SharedUtils.getString(MainActivity.this, ConstantUils.CU_SPLASH, "0"))[2];
        mt = new SendDefPool(macAddress, IAPI.WHICH_MAP);
        //主题设置
        if (nomalTheme.equals("1")) {
            String nomal = SendDefPool.stu.getmThemeSett();
            SendDefPool.stu.setmThemeSett(SendUtils.setTheme(1, nomal));
            SendDefPool.stu2.setmThemeSett(SendUtils.setTheme(1, nomal));
        }
        //亮度设置
        mStoreLight = SharedUtils.getString(instance, ConstantUils.GAO_BAIDU_LIGHT, "7");
        Log.i("ASP", "  mStoreLight ->" + mStoreLight);
        if (!mStoreLight.equals("7")) {
            String nomal1 = SendDefPool.stu.getmBootHudInfo();
            Log.i("ASP", "  nomal1 ->" + nomal1);
            String engry = SendUtils.setHudLight(Integer.parseInt(mStoreLight), nomal1);
            Log.i("ASP", "  nomal1 ->" + engry);
            SendDefPool.stu.setChanged(false);
            SendDefPool.stu.setmBootHudInfo(engry);
            SendDefPool.stu2.setChanged(false);
            SendDefPool.stu2.setmBootHudInfo(engry);
        }
        //声音同步
        mStoreVoice = SharedUtils.getString(instance, ConstantUils.GAO_BAIDU_VOICE, "10");
        if (!mStoreVoice.equals("10")) {
            int progress = Integer.parseInt(mStoreVoice);
            String hexData = SendUtils.hexAdd0Oprate(Integer.toHexString(progress));
            SendDefPool.stu.setmVoiceSett(hexData);
        }
        new Thread(mt).start();
        if (savedInstanceState != null) mPath = savedInstanceState.getString("Path");
        File file = new File(IMAGE_SAVE_DIR);
        if (!file.exists()) file.mkdirs();
        mLeProxy = LeProxy.getInstance();
        checkBLEFeature();
        bindService(new Intent(this, BleService.class), mConnection, BIND_AUTO_CREATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(mLocalReceiver, mMikeFilter());
        mConnHander.postDelayed(mConnRunable, 1000); //每隔1s执行
        initDrawer();
        initviews();
        if (mWakeLock != null) {
            mWakeLock.release();
            mWakeLock = null;
        }
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            mWakeLock = powerManager.newWakeLock(
                    PowerManager.PARTIAL_WAKE_LOCK, "MicRecording");
            /* Set it be no reference */
            mWakeLock.setReferenceCounted(false);
            mWakeLock.acquire();
        }
        //请求用户信息
        OkGo.post(WeiYunURL.GET_ALL_USER_INFO).isMultipart(true)
                .headers("Content-Type", "application/x-www-form-urlencoded")// 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                .params("token", token)
                .params("app_token", WeiYunURL.NOMAL_TOKEN)  // 这里可以上传参数
                // 可以添加文件上传
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject json = new JSONObject(s);
                            int status = json.getInt("status");
                            if (status == 200) {
                                JSONObject mUserData = json.getJSONObject("data");
                                //用户昵称
                                realname = mUserData.getString("realname");
                                Log.i("JKS", realname.length() + " realname  " + realname);
                                if (!realname.equals("")) {
                                    Log.i("JKS", realname + " realname");
                                    SharedUtils.saveString(MainActivity.this, ConstantUils.USER_REALNAME, realname);
                                }
                                //图片网址
                                avatar = mUserData.getString("avatar");
                                if (avatar.length() > 2) {
                                    Log.i("JKS", avatar + " avatar");
                                    SharedUtils.saveString(MainActivity.this, ConstantUils.USER_HEAD_PIC, avatar);
                                }


                                //用户性别
                                String gender = mUserData.getString("gender");
                                if (gender != null) {
                                    Log.i("JKS", gender + " gender");
                                    SharedUtils.saveString(MainActivity.this, ConstantUils.USER_SEX, gender);
                                }

                                //用户住址
                                String region = mUserData.getString("region");
                                if (!region.equals("")) {
                                    Log.i("JKS", region + " region");
                                    SharedUtils.saveString(MainActivity.this, ConstantUils.USER_ADDRESS_CITY, region);
                                }

                                //用户手机号
                                String mobile = mUserData.getString("mobile");
                                if (mobile.length() == 11) {
                                    SharedUtils.saveString(MainActivity.this, ConstantUils.USER_MOBLIE, mobile);
                                }
                                Log.i("JKS", mobile + " mobile");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        //请求设备信息
        OkGo.post(WeiYunURL.MAC_DEVIDE_LISTS)
                .tag(this)//
                .isMultipart(true)
                .headers("Content-Type", "application/x-www-form-urlencoded")// 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                .params("token", token)
                .params("app_token", WeiYunURL.NOMAL_TOKEN)
                .execute(new JsonCallbackABC<CarDevBean>() {
                    @Override
                    public void onSuccess(CarDevBean carDevBean, Call call, Response response) {
                        List<CarDevBean.DataBean> data1 = carDevBean.getData();
                        if (data1.size() == 0) {
                        } else {
                            CarDevBean.DataBean dataBean = data1.get(0);
                            String remark = dataBean.getRemark();
                            Log.i("JKS", remark + " remark" + remark.length() + "-----------------------------------------");
                            if (!remark.equals("")) {
                                SharedUtils.saveString(MainActivity.this, ConstantUils.USER_DEVICE_NAME, remark);
                            }
                            Log.i("JKS", remark + " remark");
                            String modelsName = dataBean.getModelsName();
                            Log.i("JKS", modelsName + " modelsName  " + modelsName + "-----------------------------------------");
                            if (modelsName.length() > 0) {
                                SharedUtils.saveString(MainActivity.this, ConstantUils.USER_DEVICE_CAR_NAME, modelsName);
                            }
                            Log.i("JKS", modelsName + " modelsName");

                        }
                    }
                });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void checkBLEFeature() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }

    /**
     * 初始化控件
     */
    private void initviews() {
        // tabButton = (TabLayout) findViewById(R.id.tabbutton);
        iv_hud = (ImageView) findViewById(R.id.iv_hud);
        rgTools = (RadioGroup) findViewById(R.id.rgTools);
        rbHome = (RadioButton) findViewById(R.id.rbHome);
        rbSett = (RadioButton) findViewById(R.id.rbSett);
        rbfind = (RadioButton) findViewById(R.id.rbfind);
        toor_other_title = (TextView) findViewById(R.id.tv_title);
        btn_cricle = (Button) findViewById(R.id.btn_cricle);
        toor_iv_detection = (ImageView) findViewById(R.id.toor_iv_detection);
        //初始化界面
        //  initTab();
        // initData();

        initFragment();
        iv_hud.setOnClickListener(this);
        toor_iv_detection.setOnClickListener(this);
    }


    private void initFragment() {
        //首页

        HudFragment homeFragment = new HudFragment();

        //设置
        SettingFragment messageFragment = new SettingFragment();

        //购物车

        DiscoverFragment mineFragment = new DiscoverFragment();

        //添加到数组
        mFragments = new Fragment[]{homeFragment, messageFragment, mineFragment};

        //开启事务

        FragmentTransaction ft =
                getSupportFragmentManager().beginTransaction();

        //添加首页
        ft.add(R.id.frameLayout, homeFragment).commit();
        //默认设置为第0个
        setIndexSelected(0);
        toor_other_title.setText("HUD");
        rgTools.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbHome:
                        isShowFind = true;
                        setIndexSelected(0);
                        iv_hud.setVisibility(View.VISIBLE);
                        toor_iv_detection.setVisibility(View.VISIBLE);
                        toor_other_title.setText("HUD");
                        break;
                    case R.id.rbSett:
                        isShowFind = true;
                        setIndexSelected(1);
                        iv_hud.setVisibility(View.VISIBLE);
                        toor_iv_detection.setVisibility(View.VISIBLE);
                        toor_other_title.setText("HUD设置");
                        break;
                    case R.id.rbfind:
                        isShowFind = false;
                        setIndexSelected(2);
                        iv_hud.setVisibility(View.INVISIBLE);
                        toor_iv_detection.setVisibility(View.INVISIBLE);
                        toor_other_title.setText("发现");
                        break;
                }
            }
        });
    }

    private void setIndexSelected(int index) {

        if (mIndex == index) {
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();


        //隐藏
        ft.hide(mFragments[mIndex]);
        //判断是否添加
        if (!mFragments[index].isAdded()) {
            ft.add(R.id.frameLayout, mFragments[index]).show(mFragments[index]);
        } else {
            ft.show(mFragments[index]);
        }

        ft.commit();
        //再次赋值
        mIndex = index;
    }

    private void initData() {
        //初始化所有要显示的Fragment对象
        fragments.add(new HudFragment());
        fragments.add(new SettingFragment());
        fragments.add(new DiscoverFragment());
        //初始化页面默认显示的fragment
        manager = getSupportFragmentManager();
        //默认布局 初始化页面默认显示的fragment
        manager.beginTransaction().replace(R.id.frameLayout, fragments.get(0), "0").commit();
        //对业务逻辑的处理
        tabButton.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            //当页面选择的时候
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tag = Integer.parseInt(tab.getTag().toString());
                Fragment frag = manager.findFragmentByTag("" + tag);
                //如果不存在则设置当前页
                if (frag == null) {
                    manager.beginTransaction().add(R.id.frameLayout, fragments.get(tag), tag + "").commit();
                }
//查看是否是当前页 如果恰好是 则设置显示  否则隐藏
                FragmentTransaction ft = manager.beginTransaction();
                for (int i = 0; i < fragments.size(); i++) {
                    if (i == tag) {
                        ft.show(fragments.get(i));
                    } else {
                        ft.hide(fragments.get(i));
                    }
                }
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    //对蓝牙的处理
    private void initTab() {
        //getResources().getString(R.string.hud)  .setText(getResources().getString(R.string.hud))
        //getResources().getString(R.string.set).setText(getResources().getString(R.string.set))
        //getResources().getString(R.string.discover) .setText(getResources().getString(R.string.discover))
        tabButton.addTab(tabButton.newTab().setIcon(R.drawable.hud_main).setTag(0));
        tabButton.addTab(tabButton.newTab().setIcon(R.drawable.hud_sett).setTag(1));
        tabButton.addTab(tabButton.newTab().setIcon(R.drawable.hud_findss).setTag(2));
        tabButton.setTabMode(TabLayout.MODE_FIXED);
        tabButton.setSelectedTabIndicatorHeight(0);
        tabButton.setFitsSystemWindows(false);
        tabButton.setTabTextColors(Color.BLACK, Color.WHITE);
        tabButton.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    private void initDrawer() {
        nv = (NavigationView) findViewById(R.id.navigationview);
        layout = (DrawerLayout) findViewById(R.id.drawerlayout);
        nv.addView(View.inflate(this, R.layout.dr_main3, null));
        tb = (Toolbar) findViewById(R.id.toolbar);
        tb.setTitle("HUD");
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, layout, tb, R.string.open, R.string.close);
        // 确认，同步菜单打开状态
        //  mActionBarDrawerToggle.syncState();
        //设置监听
        //   layout.setDrawerListener(mActionBarDrawerToggle);
        //  roundImageView.setImageResource(R.drawable.girl);
        layout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            //  RelativeLayout  rl_mac,rl_app,rl_theme,rl_msg,rl_back,rl_min;
            @Override
            public void onDrawerOpened(View drawerView) {
                initDraws(drawerView);

                mShareChaName = SharedUtils.getString(MainActivity.this, ConstantUils.PERSON_ChaCTER_NAME, "YES");
                avatar = SharedUtils.getString(MainActivity.this, ConstantUils.USER_HEAD_PIC, "YES");
                realname = SharedUtils.getString(MainActivity.this, ConstantUils.USER_REALNAME, "YES");
                Log.i("JKS", avatar + " avatar " + avatar.length() + " " + !realname.equals("0"));
                Log.i("JKS", realname + " realname " + realname.length() + " " + realname.equals("YES"));
                Log.i("JKS", mShareChaName + " mShareChaName" + mShareChaName.length() + " " + mShareChaName.equals("YES"));
                //设置头像
                if (avatar.length() > 10) {
                    Picasso.with(MainActivity.this).load(avatar).placeholder(R.drawable.default_portrait).into(mImg);
                }
                //设置昵称
                if (realname.equals("YES") == true) {
                    mName.setText("昵称");
                } else {
                    mName.setText(realname);
                }
                if (mShareChaName.equals("YES") == true) {
                    mCharName.setText("有个性，有签名");
                } else {
                    mCharName.setText(mShareChaName);
                }
                initDrawLisenter();
                isOpen = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
    }

    private void initDrawLisenter() {
        re_back_login.setOnClickListener(this);
        rl_mac.setOnClickListener(MainActivity.this);
        rl_app.setOnClickListener(MainActivity.this);
        rl_theme.setOnClickListener(MainActivity.this);
        rl_msg.setOnClickListener(MainActivity.this);
        rl_back.setOnClickListener(MainActivity.this);
        rl_min.setOnClickListener(MainActivity.this);
        rl_hepe.setOnClickListener(MainActivity.this);
        mImg.setOnClickListener(MainActivity.this);
        mName.setOnClickListener(MainActivity.this);
        mCharName.setOnClickListener(MainActivity.this);
    }

    private void initDraws(View drawerView) {
        mName = (TextView) drawerView.findViewById(R.id.tv_main_name);
        mCharName = (TextView) drawerView.findViewById(R.id.tv_main_content);

        mImg = (RoundImageView) drawerView.findViewById(R.id.roundImage_one_border);
        re_back_login = (RelativeLayout) drawerView.findViewById(R.id.re_back_login);
        rl_mac = (RelativeLayout) drawerView.findViewById(R.id.rl_mac);
        rl_app = (RelativeLayout) drawerView.findViewById(R.id.rl_app);
        rl_theme = (RelativeLayout) drawerView.findViewById(R.id.rl_theme);
        rl_msg = (RelativeLayout) drawerView.findViewById(R.id.rl_msg);
        rl_back = (RelativeLayout) drawerView.findViewById(R.id.rl_back);
        rl_min = (RelativeLayout) drawerView.findViewById(R.id.rl_min);
        rl_hepe = (RelativeLayout) drawerView.findViewById(R.id.re_hepe);

    }

    /* @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
             finish();
             return;
         }
         super.onActivityResult(requestCode, resultCode, data);
     }*/
    private void startConn() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String s = BaseUtils.getMacStr(SharedUtils.getString(MainActivity.this, ConstantUils.CU_SPLASH, "0"))[2];
            /*    Log.i("KKK", "MAC " + s);*/
                MainActivity.mLeService.connect(BaseUtils.getMacStr(SharedUtils.getString(MainActivity.this, ConstantUils.CU_SPLASH, "0"))[2], false);
                connected = false;
            }
        }, 300);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent grayIntent = new Intent(getApplicationContext(), GrayService.class);
        startService(grayIntent);
        /**
         *     后台府区
         */
        Intent watchIntent = new Intent(getApplicationContext(), WatchService.class);
        startService(watchIntent);
        Intent localIntent = new Intent(getApplicationContext(), LocalService.class);
        startService(localIntent);
    }

    @Override
    protected void onDestroy() {
        if (mWakeLock != null) {
            this.releaseWakeLock();
        }
        unregisterReceiver(receiver);
        unbindService(mConnection);
        super.onDestroy();
    }

    // 释放设备电源锁
    private void releaseWakeLock() {
        if (null != mWakeLock) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void connect() {
        if (MainActivity.mLeService != null) {
            MainActivity.mLeService.connect(BaseUtils.getMacStr(SharedUtils.getString(MainActivity.this, ConstantUils.CU_SPLASH, "0"))[2], false);
            mTimerCounts++;
            Log.i("nnn", mTimerCounts + "开始连接中------------------------------------------------------");
            if (mTimerCounts == 30) {
                Log.i("nnn", "开始连接中-----------------------mTimerCounts-------------------------------" + mTimerCounts);
                mTimerCounts = 0;
            }
        }
    }

    // 对左侧窗体的处理
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //修改各项签名
            case R.id.tv_main_content:
                startActivity(new Intent(MainActivity.this, PersonInfoActivity.class));
                layout.closeDrawer(Gravity.LEFT);
                break;
            //昵称修改
            case R.id.tv_main_name:
                startActivity(new Intent(MainActivity.this, PersonInfoActivity.class));
                layout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.re_back_login:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                SharedUtils.saveString(MainActivity.this, "appback", "loginout");
                Log.i("ASP", "MainActivity 退出appback设为loginout");
                SharedUtils.saveString(MainActivity.this, ConstantUils.CU_LOGIN, "0");
                MainActivity.this.finish();
                break;
            case R.id.rl_mac:
                startActivity(new Intent(MainActivity.this, DeviceActivity.class));

                layout.closeDrawer(Gravity.LEFT);

                break;
            case R.id.rl_app:
                startActivity(new Intent(MainActivity.this, AppSettingActivity.class));
                layout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.rl_theme:
                Toast.makeText(MainActivity.this, "敬请期待", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_msg:
                Toast.makeText(MainActivity.this, "敬请期待", Toast.LENGTH_SHORT).show();
                break;
            //意见反馈
            case R.id.rl_back:
                startActivity(new Intent(MainActivity.this, OpinonActivity.class));
                layout.closeDrawer(Gravity.LEFT);
                break;
            //关于我们
            case R.id.rl_min:
                startActivity(new Intent(MainActivity.this, AboutActiivty.class));
                layout.closeDrawer(Gravity.LEFT);
                break;

            case R.id.roundImage_one_border:
                showPhotoDialog();
                break;
            case R.id.iv_hud:
                layout.openDrawer(Gravity.LEFT);
                break;
            //OBD 跳转界面
            case R.id.toor_iv_detection:
                startActivity(new Intent(MainActivity.this, ObdActivity.class));
                break;

            case R.id.re_hepe:

                Toast.makeText(MainActivity.this, "敬请期待", Toast.LENGTH_SHORT).show();
                break;


        }
    }

    /**
     * 弹出操作选择对话框
     */
    private void showPhotoDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setItems(mItems, (dialog, which) -> {
                    if (which == 0) operTakePhoto();
                    else if (which == 1) operChoosePic();
                }).create().show();
    }

    /**
     * 拍照操作
     */
    private void operTakePhoto() {
        isTakePhoto = true;
        isGetPic = false;
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA))
                showPhotoPerDialog();
            else
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_PHOTO);
        } else takePhoto();
    }

    /**
     * 选择图片操作
     */
    private void operChoosePic() {
        isTakePhoto = false;
        isGetPic = true;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                showFilePerDialog();
            else
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_FILE);
        } else getPictureFromLocal();
    }

    /**
     * 拍照权限提示
     */
    private void showPhotoPerDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("需要获取访问您的相机权限，以确保您可以正常拍照。")
                .setPositiveButton("确定", (dialog, which) -> ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_PHOTO)).create().show();
    }

    /**
     * 文件权限提示
     */
    private void showFilePerDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("需要获取存储文件权限，以确保可以正常保存拍摄或选取的图片。")
                .setPositiveButton("确定", (dialog, which) -> ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, PERMISSIONS_REQUEST_FILE)).create().show();
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        String mUUID = UUID.randomUUID().toString();
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        mPath = FileUtils.getStorageDirectory() + mUUID;
        File file = new File(mPath + ".jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, REQUEST_CODE_TAKING_PHOTO);
    }

    /**
     * 从本地选择图片
     */
    private void getPictureFromLocal() {
        Intent innerIntent =
                new Intent(Intent.ACTION_GET_CONTENT);
        innerIntent.setType("image/*");
        Intent wrapperIntent = Intent.createChooser(innerIntent, null);
        startActivityForResult(wrapperIntent, REQUEST_CODE_SELECT_PHOTO_FROM_LOCAL);
    }

    /**
     * 剪裁图片
     */
    private void startPhotoZoom(Uri uri, int size) {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            // crop为true是设置在开启的intent中设置显示的view可以剪裁
            intent.putExtra("crop", "true");
            // aspectX aspectY 是宽高的比例
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);

            // outputX,outputY 是剪裁图片的宽高
            intent.putExtra("outputX", size);
            intent.putExtra("outputY", size);
            //   intent.putExtra("return-data", true);
            mUri = Uri.parse("file:///" + IMAGE_SAVE_PATH);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            startActivityForResult(intent, REQUEST_CODE_CUT_PHOTO);
        } catch (ActivityNotFoundException e) {
            String errorMessage = "Your device doesn't support the crop action!";
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理拍照结果
     */
    private void dealTakePhotoWithoutZoom() {
        String finalPath = mPath + ".jpg";
        Bitmap bitmap = BitmapFactory.decodeFile(finalPath);
        if (bitmap != null) {
            boolean b = BitmapUtils.compressBitmap2file(bitmap, IMAGE_SAVE_PATH);
            if (b) {
                mImg.setImageBitmap(BitmapFactory.decodeFile(IMAGE_SAVE_PATH));
            }
        }

    }

    /**
     * 处理拍照并剪裁
     * <p>
     * 逻辑先服务器上传在处裁剪逻辑
     */
    private void dealTakePhotoThenZoom() {
    /*    Bundle data = new Bundle();
        data.putInt(HandlerUtil.Take_PHOTE_DATA,12);
        HandlerUtil.handleMsg(mHandler, HandlerUtil.Take_PHOTE, data);*/
        File file = new File(mPath + ".jpg");
        startPhotoZoom(Uri.fromFile(file), TARGET_HEAD_SIZE);
    }

    /**
     * 处理选择图片结果
     */
    private void dealChoosePhotoWithoutZoom(Intent data) {
        Uri uri = data.getData();
        if (uri != null) {
            Bitmap bitmap = BitmapUtils.uriToBitmap(this, uri);
            if (bitmap != null) {
                mImg.setImageBitmap(bitmap);
                boolean b = BitmapUtils.compressBitmap2file(bitmap, IMAGE_SAVE_PATH);

            }
        }
    }

    /**
     * 处理选择图片并剪裁
     */
    private void dealChoosePhotoThenZoom(Intent data) {

        Uri uri = data.getData();
        InputStream inputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                if (bitmap != null) BitmapUtils.compressBitmap2file(bitmap, IMAGE_SAVE_PATH);
            }
            startPhotoZoom(Uri.fromFile(new File(IMAGE_SAVE_PATH)), TARGET_HEAD_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 处理剪裁图片的结果
     */
    private void dealZoomPhoto() {
        try {
            if (mUri != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mUri));
                if (bitmap != null) {
                    //mImg.setImageBitmap(bitmap);
                    boolean b = BitmapUtils.compressBitmap2file(bitmap, IMAGE_SAVE_PATH);
                    Log.i("ASP", b + " " + IMAGE_SAVE_PATH);
                    if (b) {
                        OkGo.post(WeiYunURL.MAC_IMG_UPLOAD)//
                                .tag(this)//
                                .isMultipart(true)
                                .headers("Content-Type", "application/x-www-form-urlencoded")// 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                                .params("token", token)
                                .params("files", new File(IMAGE_SAVE_PATH))
                                .params("app_token", WeiYunURL.NOMAL_TOKEN)  // 这里可以上传参数
                                // 可以添加文件上传
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {

                                        try {
                                            JSONObject json = new JSONObject(s);
                                            String status = json.getString("status");
                                            if (status.equals("200")) {
                                                JSONObject data = json.getJSONObject("data");
                                                String avatar = data.getString("avatar");
                                                Log.i("NMN", avatar + " avatar");
                                                SharedUtils.saveString(MainActivity.this, ConstantUils.USER_HEAD_PIC, avatar);
                                                Picasso.with(MainActivity.this).load(avatar).placeholder(R.drawable.default_portrait).into(mImg);


                                            } else {
                                                Toast.makeText(MainActivity.this, "拍照上传失败", Toast.LENGTH_LONG).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                       /* if (s.contains("400")) {
                                            Toast.makeText(MainActivity.this, "拍照上传失败", Toast.LENGTH_LONG).show();
                                        } else if (s.contains("200")) {
                                            //在开始裁剪把


                                        }*/

                                });
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 权限申请回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_PHOTO: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (isTakePhoto) takePhoto();
                    if (isGetPic) getPictureFromLocal();
                }
            }
            case PERMISSIONS_REQUEST_FILE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    dealZoomPhoto();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     /*   if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            isOpenSystem = false;
            finish();
            return;
            //  getParent().setResult(Activity.RESULT_OK, data);
        }
*/
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_TAKING_PHOTO: // 拍照的结果
                    dealTakePhotoThenZoom();
                    break;
                case REQUEST_CODE_SELECT_PHOTO_FROM_LOCAL://选择图片的结果
                    dealChoosePhotoThenZoom(data);
                    break;
                case REQUEST_CODE_CUT_PHOTO: // 剪裁图片的结果
                    dealZoomPhoto();
                    break;
            }
        } else if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            isOpenSystem = false;
          /*  finish();
            return;*/
        }
        // super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 监听back键退出应用
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AppExit();
        } else if (keyCode == KeyEvent.KEYCODE_HOME) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }
        return false;
    }

    public void AppExit() {
        if (!isAppExit) {
            isAppExit = true;
            mHandler.sendEmptyMessageDelayed(111, 2000);
        } else {// 2s内再次按back时,isExit= true，执行以下操作，app退出
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }
    }

}
