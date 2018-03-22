package com.shdnxc.cn.activity.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.shdnxc.cn.activity.MainActivity;
import com.shdnxc.cn.activity.R;
import com.shdnxc.cn.activity.Utils.ConstantUils;
import com.shdnxc.cn.activity.Utils.SharedUtils;
import com.shdnxc.cn.activity.Utils.WeiYunURL;
import com.shdnxc.cn.activity.app.IAPI;
import com.shdnxc.cn.activity.bean.CarDevBean;
import com.shdnxc.cn.activity.okgo.JsonCallbackABC;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Zheng Jungen on 2017/3/13.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edit_phone, edit_pwd;
    private TextView tv_register, tv_rem_pwd;
    private String token;
    private String sn;
    IAPI mAPi;
    private boolean mShow;  //mShow: true: login; false: logout
    private boolean isBindshow; //isBindshow: true: Bindin; false: bindOut
    long mExitTime;
    boolean isOrNotNet;

    private Timer timer, timer2;


    String mSharePhone;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String tokenName = (String) msg.obj;
            Log.i("nnn", tokenName + " tokenName");
            OkGo.post(WeiYunURL.MAC_DEVIDE_LISTS)
                    .tag(this)//
                    .isMultipart(true)
                    .headers("Content-Type", "application/x-www-form-urlencoded")// 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                    .params("token", tokenName)
                    .params("app_token", WeiYunURL.NOMAL_TOKEN)
                    .execute(new JsonCallbackABC<CarDevBean>() {
                        @Override
                        public void onSuccess(CarDevBean carDevBean, Call call, Response response) {
                            List<CarDevBean.DataBean> data1 = carDevBean.getData();
                            if (data1.size() == 0) {
                                startActivity(new Intent(LoginActivity.this, CameraActivity.class));
                                progressDialog.dismiss();
                                btn_login.setEnabled(true);
                                LoginActivity.this.finish();
                            } else {
                                Log.i("nnn", data1.size() + " 2222222");
                                CarDevBean.DataBean dataBean = data1.get(0);
                                String sn = dataBean.getSn();
                                SharedUtils.saveString(LoginActivity.this, ConstantUils.CU_SPLASH, sn);
                                String remark = dataBean.getRemark();
                                String name = dataBean.getName();
                                Log.i("nnn", sn + " sn2" + remark + " remark2" + name);
                                if (remark.length() > 0) {
                                    SharedUtils.saveString(LoginActivity.this, "mark", remark);
                                    SharedUtils.saveString(LoginActivity.this, ConstantUils.USER_DEVICE_NAME, remark);
                                }
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                progressDialog.dismiss();
                                btn_login.setEnabled(true);
                                LoginActivity.this.finish();


                            }
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            Log.i("ASP", e.getMessage());
                        }
                    });
        }
    };
    private Button btn_login;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        mAPi = new IAPI();
        String mAppBackState = SharedUtils.getString(LoginActivity.this, "appback", "loginout");
        String mDeviceBindState = SharedUtils.getString(LoginActivity.this, "devicebindstate", "bindout");
        if (mAppBackState.equals("loginout")) {
            mAPi.setBackApp(false);
        } else {
            mAPi.setBackApp(true);
        }
        mShow = mAPi.isBackApp();
        Log.i("ASP", "SplashActivity mShow " + mShow);
        if (mDeviceBindState.equals("bindout")) {
            mAPi.setScann(false);
        } else {
            mAPi.setScann(true);
        }
        isBindshow = mAPi.isScann();
        token = SharedUtils.getString(LoginActivity.this, ConstantUils.CU_LOGIN, "0");
        sn = SharedUtils.getString(LoginActivity.this, ConstantUils.CU_SPLASH, "0");
        initViews();
        initLisenter();
    }

    private void initLisenter() {
        tv_register.setOnClickListener(this);
        tv_rem_pwd.setOnClickListener(this);
        btn_login.setOnClickListener(this);

    }

    private void initViews() {
        edit_phone = (EditText) findViewById(R.id.edit_phone);
        edit_pwd = (EditText) findViewById(R.id.edit_pwd);
        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_rem_pwd = (TextView) findViewById(R.id.tv_rem_pwd);
        btn_login = (Button) findViewById(R.id.btn_login);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSharePhone = SharedUtils.getString(LoginActivity.this, ConstantUils.USER_MOBLIE, "false");

        Log.i("====", mSharePhone + "----------------------------------------------------------------->");
        if (!mSharePhone.equals("false")) {
            edit_phone.setText(mSharePhone);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        String editPhone = edit_phone.getText().toString();
        String editPwd = edit_pwd.getText().toString();
        switch (v.getId()) {
            //登录
            case R.id.btn_login:
                isOrNotNet = IAPI.isNet(LoginActivity.this);
                if (isOrNotNet == false) {
                    Toast.makeText(LoginActivity.this, "网络没有打开请打开网络", Toast.LENGTH_SHORT).show();
                } else {
                    int mPLen = editPhone.length();
                    int mELen = editPwd.length();
                    Log.i("TYU", mPLen + " " + mELen);
                    if (mPLen == 0 && mELen == 0) {
                        Toast.makeText(LoginActivity.this, "手机号和密码不能为空", Toast.LENGTH_SHORT).show();
                    } else if (mPLen == 0 && mELen != 0) {
                        Toast.makeText(LoginActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    } else if (mPLen > 0 && mPLen < 11 && mELen != 0) {
                        Toast.makeText(LoginActivity.this, "手机号的错误", Toast.LENGTH_SHORT).show();
                    } else if (mPLen > 0 && mPLen < 11 && mELen == 0) {
                        Toast.makeText(LoginActivity.this, "手机号错误密码不能为空", Toast.LENGTH_SHORT).show();
                    } else if (mPLen == 11 && mELen == 0) {
                        Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    } else if (mPLen == 11 && mELen != 0) {
                        btn_login.setEnabled(false);
                        progressDialog = new ProgressDialog(LoginActivity.this,
                                R.style.AppTheme_Dark_Dialog);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("登录中请稍等...");
                        progressDialog.show();
                        SharedUtils.saveString(LoginActivity.this, ConstantUils.PERSON_PHONE, editPhone);
                        SharedUtils.saveString(LoginActivity.this, ConstantUils.USER_MOBLIE, editPhone);

                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        // On complete call either onLoginSuccess or onLoginFailed
                                        OkGo.post(WeiYunURL.BLE_LOGIN_URL)//
                                                .tag(this)//
                                                .isMultipart(true)
                                                .headers("Content-Type", "application/x-www-form-urlencoded")// 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                                                .params("account", editPhone)
                                                .params("password", editPwd)
                                                .params("app_token", WeiYunURL.NOMAL_TOKEN)  // 这里可以上传参数
                                                // 可以添加文件上传
                                                .execute(new StringCallback() {
                                                    @Override
                                                    public void onError(Call call, Response response, Exception e) {
                                                        Log.i("nnn", "onError");
                                                        progressDialog.dismiss();
                                                        btn_login.setEnabled(true);
                                                    }

                                                    @Override
                                                    public void onSuccess(String s, Call call, Response response) {
                                                        if (s.contains("400")) {
                                                            JSONObject jsonObject = null;     //返回的数据形式是一个Object类型，所以可以直接转换成一个Object
                                                            try {
                                                                jsonObject = new JSONObject(s);
                                                                int status = jsonObject.getInt("status");
                                                                String error_message = jsonObject.getString("error_message");

                                                                Toast.makeText(LoginActivity.this, error_message + " ", Toast.LENGTH_LONG).show();
                                                                progressDialog.dismiss();
                                                                btn_login.setEnabled(true);

                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        } else if (s.contains("200")) {
                                                            SharedUtils.saveString(LoginActivity.this, "appback", "login");
                                                            Log.i("ASP", "LoginActivity 设置appback为login");
                                                            //准备挑战逻辑
                                                            JSONObject jsonObject = null;     //返回的数据形式是一个Object类型，所以可以直接转换成一个Object
                                                            try {
                                                                jsonObject = new JSONObject(s);
                                                                int status = jsonObject.getInt("status");
                                                                JSONObject data = jsonObject.getJSONObject("data");
                                                                String token = data.getString("token");
                                                                Log.i("nnn", token + "   ");
                                                                //保存Token
                                                                SharedUtils.saveString(LoginActivity.this, ConstantUils.CU_LOGIN, token);
                                                                //            String isJumpDis = SharedUtils.getString(LoginActivity.this, ConstantUils.CU_SPLASH, "0");
                                                                Message msg = Message.obtain();
                                                                msg.obj = token;
                                                                handler.sendMessage(msg);
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }
                                                });
                                    }
                                }, 3000);
                    }

                }
                break;
            //注册
            case R.id.tv_register:
                startActivity(new Intent(LoginActivity.this, RegistrActivity.class));
                break;
            //忘了密码
            case R.id.tv_rem_pwd:
                startActivity(new Intent(LoginActivity.this, ForgetActivity.class));
                break;

        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
