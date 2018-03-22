package com.shdnxc.cn.activity.ui;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.shdnxc.cn.activity.MainActivity;
import com.shdnxc.cn.activity.R;
import com.shdnxc.cn.activity.Utils.ConstantUils;
import com.shdnxc.cn.activity.Utils.LeProxy;
import com.shdnxc.cn.activity.Utils.SharedUtils;
import com.shdnxc.cn.activity.Utils.WeiYunURL;
import com.shdnxc.cn.activity.app.IAPI;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Zheng Jungen on 2017/3/16.
 */
public class DeviceActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView mBack;
    private TextView mTitle;
    private Button btn_unbid;
    private RelativeLayout mBindDevice;
    private TextView mBindDeviceName;

    String deviceAddre = "";
    String token = "";
    BluetoothAdapter mBluetoothAdapter;
    private boolean isUnBindDevices = false;
    private String mDeviceNames;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 300:
                    MainActivity.instance.finish();
                    break;
                case 400:
                    MainActivity.instance.finish();
                    break;
            }
        }
    };
    private boolean isOrNotNet;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_device);


        mBluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        token = SharedUtils.getString(DeviceActivity.this, ConstantUils.CU_LOGIN, "0");
        deviceAddre = SharedUtils.getString(DeviceActivity.this, ConstantUils.CU_SPLASH, "0");
        initviews();
        initLisenter();

    }

    @Override
    protected void onResume() {
        mDeviceNames = SharedUtils.getString(DeviceActivity.this, ConstantUils.USER_DEVICE_NAME, "YES");
        mTitle.setText("我的设备");
        if (!mDeviceNames.equals("YES")) {
            mBindDeviceName.setText(mDeviceNames + " " + "_ " + deviceAddre);
        } else {
            mBindDeviceName.setText(deviceAddre);
        }
        super.onResume();
    }

    private void initLisenter() {

        mBack.setOnClickListener(this);
        btn_unbid.setOnClickListener(this);
        mBindDevice.setOnClickListener(this);

    }

    private void initviews() {
        mBindDeviceName = (TextView) findViewById(R.id.tv_bind_names);
        btn_unbid = (Button) findViewById(R.id.btn_unbid);
        mTitle = (TextView) findViewById(R.id.tv_title);
        mBack = (ImageView) findViewById(R.id.iv_find_back);
        mBindDevice = (RelativeLayout) findViewById(R.id.re_bind_device);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.re_bind_device:
                startActivity(new Intent(DeviceActivity.this, DeviceInfoActivity.class));
                break;
            case R.id.btn_unbid:
                isOrNotNet = IAPI.isNet(DeviceActivity.this);
                if (isOrNotNet == false) {
                    Toast.makeText(DeviceActivity.this, "网络没有打开,不能解绑", Toast.LENGTH_LONG).show();
                } else {
                    btn_unbid.setText("解除绑定中请稍等。。。");
                    OkGo.post(WeiYunURL.MAC_UNBIND)//
                            .tag(this)//
                            .isMultipart(true)
                            .headers("Content-Type", "application/x-www-form-urlencoded")// 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                            .params("token", token)
                            .params("sn", deviceAddre)
                            .params("app_token", WeiYunURL.NOMAL_TOKEN)  // 这里可以上传参数
                            // 可以添加文件上传
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    if (s.contains("400")) {
                                        if (isUnBindDevices) {
                                            Message msg = Message.obtain();
                                            msg.what = 300;
                                            handler.sendMessage(msg);
                                            startActivity(new Intent(DeviceActivity.this, CameraActivity.class));
                                            btn_unbid.setText("更改设备");
                                            DeviceActivity.this.finish();
                                        } else {
                                            isUnBindDevices = true;
                                            btn_unbid.setText("解绑失败再点击一次");
                                        }
                                    } else if (s.contains("200")) {
                                        //准备跳转到登录界面
                                        Message msg = Message.obtain();
                                        msg.what = 400;
                                        handler.sendMessage(msg);
                                        startActivity(new Intent(DeviceActivity.this, CameraActivity.class));
                                        btn_unbid.setText("更改设备");
                                        //SharedUtils.saveString(DeviceActivity.this, "devicebindstate", "bindout");
                                        SharedUtils.saveString(DeviceActivity.this, ConstantUils.CU_SPLASH, "0");
                                        LeProxy.mBleService.disconnect(deviceAddre);
                                        MainActivity.instance.finish();
                                        DeviceActivity.this.finish();
                                    }
                                }
                            });
                }
                break;
            case R.id.iv_find_back:
                DeviceActivity.this.finish();
                break;
        }
    }
}
