package com.shdnxc.cn.activity.ui;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ble.api.DataUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.shdnxc.cn.activity.MainActivity;
import com.shdnxc.cn.activity.R;
import com.shdnxc.cn.activity.Utils.BaseUtils;
import com.shdnxc.cn.activity.Utils.CRC64;
import com.shdnxc.cn.activity.Utils.ConstantUils;
import com.shdnxc.cn.activity.Utils.LeProxy;
import com.shdnxc.cn.activity.Utils.SDCardScanner;
import com.shdnxc.cn.activity.Utils.SendDefPool;
import com.shdnxc.cn.activity.Utils.SendOTAPool;
import com.shdnxc.cn.activity.Utils.SendUtils;
import com.shdnxc.cn.activity.Utils.SharedUtils;
import com.shdnxc.cn.activity.Utils.WeiYunURL;
import com.shdnxc.cn.activity.adapter.OtaAdapter;
import com.shdnxc.cn.activity.app.IAPI;
import com.shdnxc.cn.activity.bean.FrameMode;
import com.shdnxc.cn.activity.servce.GrayService;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Zheng Jungen on 2017/5/7.
 */
public class UpdateFrieActivity extends Activity implements View.OnClickListener {
    String binUrl = "http://dnxc.waywings.com/files/fireware/file/20170214/HUD_APP_V-1.3.bin";
    private static final int REQUEST_CODE_PERMISSION_OTHER = 101;
    // 用户取消后重新提醒
    private static final int REQUEST_CODE_SETTING = 300;
    private Button btn_file_date;
    private String binUrls = "";
    private Button btn_local;
    String appVersionName = "";
    private ImageView mBack;
    private TextView mTitle;
    private boolean needPerssion;
    private String dirName;
    private String fderName = "HUD_APP_V-1.3.bin";
    SendOTAPool sendOtaPool;
    String macAddress;
    String filestr = "";
    int counts = 0;
    int sexDage = 256;
    int flag = 0;
    int value = 0;
    private ListView lv_content;
    private RelativeLayout re_update_show;
    private RelativeLayout re_down_show;
    private RelativeLayout mReShowProgress;

    private SeekBar mProgressBar;
    private TextView mProgress;
    private OtaAdapter otaAdapter;
    private RelativeLayout re_show_or_not;
    private boolean isSetMax;

    private boolean isOrNotUpdata = false;
    private boolean isControlOTA = false;
    private ArrayList<String> items = new ArrayList<>();
    private FrameMode mFrameData;
    Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int valueData = msg.what;
            switch (valueData) {
                case 10:
                    if (needPerssion) {
                        AndPermission.with(UpdateFrieActivity.this)
                                .requestCode(REQUEST_CODE_PERMISSION_OTHER)
                                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                                .rationale((requestCode, rationale) ->
                                        // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                                        AndPermission.rationaleDialog(UpdateFrieActivity.this, rationale).show()
                                )
                                .send();
                    }
                    if (!needPerssion) {
                        setNetRequest();
                    }
                    break;
                //表示进去MEMU 界面
                case 1:
                    //关闭线程
                    MainActivity.mt.stopThread(true);
                    Log.i("gbk", "gbk 关闭线程");
                  /*  if (MainActivity.mLeService != null) {
                        MainActivity.mLeService.send(macAddress, CRC64.stringToAscii("Y"), false);
                    }*/
                    if (MainActivity.mLeProxy != null) {
                        MainActivity.mLeProxy.send(macAddress, CRC64.stringToAscii("Y"), false);

                    }
                    isControlOTA = true;
                    break;
                //接收到了C 发送第一帧数据
                case 2:
                    String s0 = CRC64.sendOTAData(flag, value, sexDage, filestr, fderName);
                    Log.i("gbk", s0);
                    sendOtaPool = new SendOTAPool(macAddress, s0);
                    new Thread(sendOtaPool).start();
                    mProgress.setText("0%");
                    mProgressBar.setProgress(1);
                    break;
                //发送失败的数据
                case 3:
                    String s3 = CRC64.sendOTAData(flag, value, sexDage, filestr, fderName);
                    Log.i("gbk", s3);
                    sendOtaPool = new SendOTAPool(macAddress, s3);
                    new Thread(sendOtaPool).start();

                    break;
                //正确发送的数据
                case 4:

                    Log.i("flag", "flag" + flag);
                    String s4 = CRC64.sendOTAData(flag, value, sexDage, filestr, fderName);
                    Log.i("gbk", s4);
                    sendOtaPool = new SendOTAPool(macAddress, s4);
                    new Thread(sendOtaPool).start();
                    double v = (double) flag / (double) (counts + 1);
                    double v1 = v * 100;
                    long round2 = Math.round(v1);
                    mProgress.setText(round2 + "%");
                    mProgressBar.setProgress(flag);
                    break;

                case 5:
                  /*  if (MainActivity.mLeService != null) {
                        MainActivity.mLeService.send(macAddress, "04", false);
                    }*/
                    if (MainActivity.mLeProxy != null) {
                        MainActivity.mLeProxy.send(macAddress, "04", false);

                    }

                    Log.i("TYU", "  case  5   发送04");
                    break;
                case 6:

               /*     if (!isControlOTA) {*/

                    Log.i("TYU", "  case  6 ");
                  /*  if (MainActivity.mLeService != null) {
                        MainActivity.mLeService.send(macAddress, CRC64.stringToAscii("E"), false);
                    }*/
                    if (MainActivity.mLeProxy != null) {
                        MainActivity.mLeProxy.send(macAddress, CRC64.stringToAscii("E"), false);
                    }
                    mProgress.setText("100%");
                    mProgressBar.setProgress(flag);
                    re_show_or_not.setVisibility(View.VISIBLE);
                    mReShowProgress.setVisibility(View.INVISIBLE);
                    sendOtaPool.stopThread(true);
                    Log.i("TYU", "结束");

                    /*
                    OTA结束标志
                     */

                    if (IAPI.WHICH_MAP.equals("1")) {
                        String nomal = SendDefPool.stu.getmBootHudInfo();
                        String bootHex = SendUtils.setBoot(0, nomal);
                        SendDefPool.stu.setmBootHudInfo(bootHex);
                        MainActivity.mt.setSta(CRC64.getAll2(SendDefPool.stu.toString()));
                        //开启线程
                        MainActivity.mt.stopThread(false);
                        btn_file_date.setVisibility(View.INVISIBLE);
                        isOrNotUpdata = false;

                    }

                  /*  }*/
                    break;
            }
        }
    };

    private final BroadcastReceiver mLocalReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String address = intent.getStringExtra(LeProxy.EXTRA_ADDRESS);
            String stringExtra = intent.getStringExtra(LeProxy.EXTRA_ADDRESS);
            String hexData = DataUtil.byteArrayToHex(intent.getByteArrayExtra(LeProxy.EXTRA_DATA));
//成功接收的数据
            Log.i("gbk", "hexData" + hexData + " ");
            String gbk = CRC64.decodeString(hexData.replace(" ", ""), "GBK");
            Log.i("gbk", "gbk" + gbk + "  ");
            filestr = SharedUtils.getString(UpdateFrieActivity.this, ConstantUils.OTA_DOCUMENT, "0");
            counts = CRC64.getCounnts(filestr.length(), sexDage);
            mProgressBar.setMax(counts + 1);
            if (gbk.contains("MENU") && flag == 0) {
                Message msg = Message.obtain();
                msg.what = 1;
                mHander.sendMessage(msg);
            }
            if (gbk.equals("C") && flag == 0) {
                Message msg = Message.obtain();
                msg.what = 2;
                mHander.sendMessage(msg);
            }
            if (hexData.contains("06")) {
                if (flag < counts + 1) {
                    flag++;
                    value++;
                    Log.i("flag", "flag " + flag);
                    if (value > 255) {
                        value = 0;
                    }
                    if (flag == counts + 1) {
                        Log.i("TYU", "进入ROT 5  " + flag + " hexData " + hexData);
                        Message msg = Message.obtain();
                        msg.what = 5;
                        mHander.sendMessage(msg);

                    } else if (flag <= counts) {
                        Log.i("TYU", "进入OOU 4 -> " + flag);
                        Message msg = Message.obtain();
                        msg.what = 4;
                        mHander.sendMessage(msg);
                    }
                } else if (hexData.contains("15")) {
                    value++;
                    if (value > 255) {
                        value = 0;
                    }
                    Message msg = Message.obtain();
                    msg.what = 3;
                    mHander.sendMessage(msg);
                }
            }
            if (gbk.contains("MENU") && flag == (counts + 1)) {
                if (isControlOTA) {
                    Log.i("TYU", "OK  次数  6 " + flag);
                    Log.i("TYU", "OK");
                    Log.i("TYU", "OK  " + isControlOTA);
                    Message msg = Message.obtain();
                    msg.what = 6;
                    mHander.sendMessage(msg);
                    isControlOTA = false;
                    Log.i("TYU", "进入次数   ------------------------------------------");
                }
            }
        }
    };

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        LocalBroadcastManager.getInstance(this).registerReceiver(mLocalReceiver, mMikeFilter());
        macAddress = BaseUtils.getMacStr(SharedUtils.getString(UpdateFrieActivity.this, ConstantUils.CU_SPLASH, "0"))[2];
        initviews();
        appVersionName = getAppVersionName(this);
        initLisenter();
        needPerssion = BaseUtils.isNeedPerssion();
        File SDDir = SDCardScanner.getStorgePath(UpdateFrieActivity.this);// 获取跟目录
        String filePath = SDDir.getPath() + "/shdnxc/doucument/";
        dirName = filePath;
        File f = new File(dirName);
        if (!f.exists()) {
            f.mkdir();
        }
        initdata();
    }

    private void initLisenter() {
        btn_file_date.setOnClickListener(this);
        btn_local.setOnClickListener(this);
        mBack.setOnClickListener(this);
    }

    private void initviews() {
        re_show_or_not = (RelativeLayout) findViewById(R.id.re_show_or_not);
        re_down_show = (RelativeLayout) findViewById(R.id.re_down_show);
        re_update_show = (RelativeLayout) findViewById(R.id.re_update_show);
        re_update_show = (RelativeLayout) findViewById(R.id.re_update_show);
        mProgress = (TextView) findViewById(R.id.tv_progress);
        mProgressBar = (SeekBar) findViewById(R.id.p_net_bar);
        mReShowProgress = (RelativeLayout) findViewById(R.id.re_ota_cen);
        btn_file_date = (Button) findViewById(R.id.btn_file_date);
        btn_local = (Button) findViewById(R.id.btn_local);
        lv_content = (ListView) findViewById(R.id.lv_content);
        mBack = (ImageView) findViewById(R.id.iv_find_back);
        mTitle = (TextView) findViewById(R.id.tv_title);
        mTitle.setText("OTA");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i("TYU", "出现次数--------------启动后台进程服务保证线程不被杀死------------");
        Intent grayIntent = new Intent(getApplicationContext(), GrayService.class);
        startService(grayIntent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_file_date:
                if (IAPI.WHICH_MAP.equals("1")) {
                    mReShowProgress.setVisibility(View.VISIBLE);
                    flag = 0;
                    mProgress.setText("0%");
                    mProgressBar.setProgress(flag);
                    isOrNotUpdata = true;
                    String nomal = SendDefPool.stu.getmBootHudInfo();
                    String bootHex = SendUtils.setBoot(1, nomal);
                    SendDefPool.stu.setmBootHudInfo(bootHex);
                    MainActivity.mt.setSta(CRC64.getAll2(SendDefPool.stu.toString()));
                    filestr = SharedUtils.getString(UpdateFrieActivity.this, ConstantUils.OTA_DOCUMENT, "0");
                    counts = CRC64.getCounnts(filestr.length(), sexDage);
                    re_show_or_not.setVisibility(View.INVISIBLE);
                } else {
                /*    String nomal = SendDefPool.stu2.getmBootHudInfo();
                    String bootHex = SendUtils.setBoot(1, nomal);
                    SendDefPool.stu2.setmBootHudInfo(bootHex);
                    MainActivity.mt.setSta(CRC64.getAll2(SendDefPool.stu2.toString()));*/
                    Toast.makeText(UpdateFrieActivity.this, "高德暂时不支持OTA升级", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_local:
                String storeData = SharedUtils.getString(UpdateFrieActivity.this, ConstantUils.OTA_DOCUMENT, "0");
                if ("0".equals(storeData)) {
                    String otaFile = CRC64.getDoucment(dirName + fderName);
                    String replace = otaFile.replace(" ", "");
                    if (replace != null) {
                        SharedUtils.saveString(UpdateFrieActivity.this, ConstantUils.OTA_DOCUMENT, replace);
                        btn_local.setVisibility(View.INVISIBLE);
                        btn_file_date.setVisibility(View.VISIBLE);
                    }
                } else {
                    btn_local.setVisibility(View.INVISIBLE);
                    btn_file_date.setVisibility(View.VISIBLE);
                    // Toast.makeText(UpdateFrieActivity.this, "文件已经下载转化完毕", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.iv_find_back:
                UpdateFrieActivity.this.finish();
                break;
        }
    }

    private void initdata() {

        OkGo.post(WeiYunURL.BLE_UPDATA_URL)//
                .tag(this)//
              /*  .isMultipart(true)
                .headers("Content-Type", "application/x-www-form-urlencoded")*/// 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                .params("version", appVersionName)
                .params("app_token", WeiYunURL.NOMAL_TOKEN)  // 这里可以上传参数
                .params("model", "AA12")   // 可以添加文件上传
                // 可以添加文件上传
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        //上传成功

                        try {
                            JSONObject json = new JSONObject(s);
                            String status = json.getString("status");
                            if (status.equals("200")) {
                                JSONObject data = json.getJSONObject("data");
                                String version = data.getString("version");
                                String url = data.getString("url");
                                binUrls = url;
                                String[] split1 = url.split("/");
                                String s1 = split1[split1.length - 1];
                                fderName = s1;
                                Log.i("binUrl", "onSuccess  getMultiYes ");
                                String length = data.getString("length");
                                String md5 = data.getString("md5");
                                String description = data.getString("description");
                                String flag = data.getString("flag");
                                String[] split = description.split("\r\n");
                                for (int i = 0; i < split.length; i++) {
                                    items.add(split[i]);
                                }
                            }
                            otaAdapter = new OtaAdapter(UpdateFrieActivity.this, items);
                            lv_content.setAdapter(otaAdapter);
                            Message msg = Message.obtain();
                            msg.what = 10;
                            mHander.sendMessage(msg);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Log.i("ota", e.getMessage());
                    }

                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        //这里回调上传进度(该回调在主线程,可以直接更新ui)
                    }
                });
    }

    @PermissionYes(REQUEST_CODE_PERMISSION_OTHER)
    private void getMultiYes(List<String> grantedPermissions) {
        Log.i("binUrl", "onSuccess  getMultiYes ");
        setNetRequest();

    }

    @PermissionNo(REQUEST_CODE_PERMISSION_OTHER)
    private void getMultiNo(List<String> deniedPermissions) {
        Log.i("binUrl", "onSuccess  getMultiNo ");
        Toast.makeText(this, "申请权限失败", Toast.LENGTH_SHORT).show();
        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING)
                    .setTitle("权限申请被您拒绝")
                    .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用")
                    .setPositiveButton("YES")
                    .setNegativeButton("NO", null)
                    .show();

            // 更多自定dialog，请看上面。
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
         * @param object     要接受结果的Activity、Fragment。
         * @param requestCode  请求码。
         * @param permissions  权限数组，一个或者多个。
         * @param grantResults 请求结果。
         */
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_SETTING:
                Toast.makeText(this, "重新请求权限成功可以正常使用", Toast.LENGTH_LONG).show();
                setNetRequest();
                break;
        }
    }

    private void setNetRequest() {
        OkGo.get(binUrls)//
                .tag(this)//
                .execute(new FileCallback(dirName, fderName) {
                    @Override
                    public void onSuccess(File file, Call call, Response response) {
                        Log.i("binUrl", "onSuccess  one " + file.getAbsolutePath() + " " + file.getName());

                        btn_local.setVisibility(View.VISIBLE);

                        //  btn_fireware.setEnabled(true);
                    }

                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        Log.i("binUrl", "下载进度" + currentSize + " " + totalSize + " " + progress + " " + networkSpeed);
                    }

                    @Override
                    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        Log.i("binUrl", "下载进度" + currentSize + " " + totalSize + " " + progress + " " + networkSpeed);
                    }
                });
    }

    private String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo("com.shdnxc.cn.activity", 0);
            versionName = packageInfo.versionName;
            if (TextUtils.isEmpty(versionName)) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 点击返回键返回后台
     *
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isOrNotUpdata) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
            if (!isOrNotUpdata) {
                return super.onKeyDown(keyCode, event);
            }
        } else if (keyCode == KeyEvent.KEYCODE_HOME) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }
        return false;
    }
}
