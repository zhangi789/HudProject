package com.shdnxc.cn.activity.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.shdnxc.cn.activity.R;
import com.shdnxc.cn.activity.Utils.ConstantUils;
import com.shdnxc.cn.activity.Utils.SharedUtils;
import com.shdnxc.cn.activity.Utils.WeiYunURL;
import com.shdnxc.cn.activity.app.IAPI;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Zheng Jungen on 2017/3/27.
 */
public class DeviceInfoActivity extends Activity implements View.OnClickListener {
    IAPI apiInfo;
    private ImageView iv_find_back;
    private TextView tv_title;
    private TextView tv_deviceid, tv_device_alter, tv_car_seceres;
    private RelativeLayout re_car_name;
    private RelativeLayout re_car_sale;
    private RelativeLayout re_fwversion;
    private String deviceAddre;
    private RelativeLayout re_pop_show;
    public static DeviceInfoActivity instance = null;
    private EditText tv_pop_content;

    private TextView btn_cancle, btn_name;

    private static final int REQUEST_ID = 200;
    private static final int REQUEST_NAME = 300;
    private static final int REQUEST_CAR_BAND = 400;

    String carName;
    String carSeries;
    String carlist;
    private String mCarInfo = "";
    private String token;
    String remark;
    private String seriesName, listNames;
    //将获得的数据接收  在updateReceivedData更新数据
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("JKS", "进入广播");
            final String action = intent.getAction();
            //成功的回掉
            if (ConstantUils.DEVIES_INFO_MSG.equals(action)) {

                Bundle bundle = intent.getExtras();
                String string = bundle.getString(ConstantUils.DEVIES_INFO);
                String stringExtra = intent.getStringExtra(ConstantUils.DEVIES_INFO);
                tv_car_seceres.setText(string);
            } else if (ConstantUils.DEVIES_INFO_MSG2.equals(action)) {
                String stringExtra = intent.getStringExtra(ConstantUils.DEVIES_INFO2);
                tv_car_seceres.setText(stringExtra);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deviceAddre = SharedUtils.getString(DeviceInfoActivity.this, ConstantUils.CU_SPLASH, "0");
        Intent intent = getIntent();
        token = SharedUtils.getString(DeviceInfoActivity.this, ConstantUils.CU_LOGIN, "0");
        remark = SharedUtils.getString(DeviceInfoActivity.this, ConstantUils.USER_DEVICE_NAME, "YES");

        Log.i("JKS", "出资按的");
        // String mCae = SharedUtils.getString(DeviceInfoActivity.this, ConstantUils.USER_DEVICE_CAR_NAME, "YES");

        carName = SharedUtils.getString(DeviceInfoActivity.this, "carName", "0");
        seriesName = SharedUtils.getString(DeviceInfoActivity.this, "seriesName", "0");
        listNames = SharedUtils.getString(DeviceInfoActivity.this, "listNames", "0");
        mCarInfo = carName + seriesName + listNames;
        setContentView(R.layout.activity_device_info);
        instance = this;
        //  SharedUtils.saveString(DeviceInfoActivity.this, ConstantUils.CU_CAR_DEVICE_INFO, "YES");
        initviews();
        boolean net = apiInfo.isNet(DeviceInfoActivity.this);
        this.registerReceiver(mReceiver, mbleIntentFilter());
        apiInfo = new IAPI();
        tv_title.setText("设备详情");
        initLienter();
        tv_deviceid.setText(deviceAddre);
        if (remark.equals("YES") == true) {
            tv_device_alter.setText("请输入设备昵称");

        } else {
            tv_device_alter.setText(remark);
        }
    }

    @Override
    protected void onResume() {
        String mCae = SharedUtils.getString(DeviceInfoActivity.this, ConstantUils.USER_DEVICE_CAR_NAME, "YES");

        if (mCae.equals("YES") == true) {
            tv_car_seceres.setText("点击选择车型");

        } else {
            if (mCae.length() > 28) {
                mCae = mCae.substring(0, 28) + "..";
                tv_car_seceres.setText(mCae);
            } else {
                tv_car_seceres.setText(mCae);
            }
        }
        super.onResume();
    }

    private void initLienter() {
        iv_find_back.setOnClickListener(this);
        re_car_name.setOnClickListener(this);
        re_car_sale.setOnClickListener(this);
        re_fwversion.setOnClickListener(this);
        btn_name.setOnClickListener(this);
        btn_cancle.setOnClickListener(this);
    }

    private void initviews() {
        btn_name = (TextView) findViewById(R.id.btn_name);
        btn_cancle = (TextView) findViewById(R.id.btn_cancle);
        tv_pop_content = (EditText) findViewById(R.id.tv_pop_content);
        re_pop_show = (RelativeLayout) findViewById(R.id.re_pop_show);
        iv_find_back = (ImageView) findViewById(R.id.iv_find_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_deviceid = (TextView) findViewById(R.id.tv_deviceid);
        tv_device_alter = (TextView) findViewById(R.id.tv_ipnoe_name);
        tv_car_seceres = (TextView) findViewById(R.id.tv_car_seceres);
        re_car_name = (RelativeLayout) findViewById(R.id.re_car_name);
        re_car_sale = (RelativeLayout) findViewById(R.id.re_car_sale);
        re_fwversion = (RelativeLayout) findViewById(R.id.re_fwversion);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //取消按钮
            case R.id.btn_cancle:
                re_pop_show.setVisibility(View.INVISIBLE);

                break;
            //确定按钮
            case R.id.btn_name:


                String beiNames = tv_pop_content.getText().toString();
                if (beiNames.length() > 1) {
                    OkGo.post(WeiYunURL.MAC_BIND)//
                            .tag(this)//
                            .isMultipart(true)
                            .headers("Content-Type", "application/x-www-form-urlencoded")// 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                            .params("token", token)
                            .params("sn", deviceAddre)
                            .params("remark", beiNames)
                            .params("app_token", WeiYunURL.NOMAL_TOKEN)
                            // 这里可以上传参数
                            // 可以添加文件上传
                            .execute(
                                    new StringCallback() {

                                        @Override
                                        public void onError(Call call, Response response, Exception e) {
                                            Log.i("PPP", "car error        ");
                                        }

                                        @Override
                                        public void onSuccess(String s, Call call, Response response) {

                                            try {
                                                JSONObject json = new JSONObject(s);
                                                int status = json.getInt("status");
                                                if (status == 200) {
                                                    JSONObject data = json.getJSONObject("data");
                                                    String mremark = data.getString("remark");
                                                    Log.i("JKS", mremark + " mremark   成功");
                                                    SharedUtils.saveString(DeviceInfoActivity.this, "mark", mremark);
                                                    if (mremark != null) {
                                                        SharedUtils.saveString(DeviceInfoActivity.this, ConstantUils.USER_DEVICE_NAME, mremark);
                                                        tv_device_alter.setText(beiNames);
                                                    }
                                                } else {
                                                    Toast.makeText(DeviceInfoActivity.this, "错误码" + status, Toast.LENGTH_SHORT).show();
                                                }

                                                re_pop_show.setVisibility(View.INVISIBLE);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });


                    //   SharedUtils.saveString(DeviceInfoActivity.this, "mark", beiNames);
                    // tv_device_alter.setText(beiNames);
                }
                re_pop_show.setVisibility(View.INVISIBLE);

                break;
            case R.id.iv_find_back:
                Intent intent = new Intent();
                intent.setAction(ConstantUils.BLE_ADDRESS_NAME);
                sendBroadcast(intent);
                DeviceInfoActivity.this.finish();
                break;
            case R.id.re_car_name:
                re_pop_show.setVisibility(View.VISIBLE);

                break;
            case R.id.re_car_sale:
                Log.i("JKS", "车品牌");
                startActivity(new Intent(DeviceInfoActivity.this, CarActivity.class));
                //DeviceInfoActivity.this.finish();
                break;
            case R.id.re_fwversion:
                startActivity(new Intent(DeviceInfoActivity.this, UpdateFrieActivity.class));
                break;
        }
    }

    //对蓝牙服务的支持
    private static IntentFilter mbleIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantUils.DEVIES_INFO_MSG);
        intentFilter.addAction(ConstantUils.DEVIES_INFO_MSG2);
        return intentFilter;
    }


    @Override
    public void onBackPressed() {
        DeviceInfoActivity.this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
