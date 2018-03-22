package com.shdnxc.cn.activity.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.shdnxc.cn.activity.R;
import com.shdnxc.cn.activity.Utils.WeiYunURL;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Zheng Jungen on 2017/3/13.
 */
public class RegistrActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edit_phone, edit_crc, edit_serect;
    private Button btn_accout;
    private ImageView toor_iv_back, iv_phone_delete, iv_yan_delete, iv_pwd_delete;
    private Button btn_send;
    private TimeCount time;

    private RelativeLayout re_btn_res;

    private RelativeLayout re_input_new_pwd;

    private RelativeLayout re_input_yan;
    private RelativeLayout re_input_iphone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actiivty_register);
        time = new TimeCount(60000, 1000);//构造CountDownTimer对象
        initViews();
        initLisenter();
    }

    private void initLisenter() {
        re_input_iphone.setOnClickListener(this);
        re_input_yan.setOnClickListener(this);
        re_btn_res.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        btn_accout.setOnClickListener(this);
        toor_iv_back.setOnClickListener(this);
        iv_phone_delete.setOnClickListener(this);
        iv_yan_delete.setOnClickListener(this);
        iv_pwd_delete.setOnClickListener(this);
        re_input_new_pwd.setOnClickListener(this);
    }

    private void initViews() {
        re_input_iphone = (RelativeLayout) findViewById(R.id.re_input_iphone);
        re_input_yan = (RelativeLayout) findViewById(R.id.re_input_yan);
        re_input_new_pwd = (RelativeLayout) findViewById(R.id.re_input_new_pwd);
        re_btn_res = (RelativeLayout) findViewById(R.id.re_btn_res);
        edit_phone = (EditText) findViewById(R.id.edit_phone);
        edit_crc = (EditText) findViewById(R.id.edit_crc);
        edit_serect = (EditText) findViewById(R.id.edit_serect);
        btn_accout = (Button) findViewById(R.id.btn_accout);
        btn_send = (Button) findViewById(R.id.btn_send);
        toor_iv_back = (ImageView) findViewById(R.id.iv_find_back);
        iv_phone_delete = (ImageView) findViewById(R.id.iv_phone_delete);
        iv_yan_delete = (ImageView) findViewById(R.id.iv_yan_delete);
        iv_pwd_delete = (ImageView) findViewById(R.id.iv_pwd_delete);
    }


    @Override
    public void onClick(View v) {
        String editPhone = edit_phone.getText().toString();
        String editSerect = edit_serect.getText().toString();
        switch (v.getId()) {
            case R.id.re_input_iphone:
                edit_phone.setText("");
                break;
            case R.id.re_input_yan:
                edit_crc.setText("");
                break;
            case R.id.re_input_new_pwd:
                edit_serect.setText("");
                break;

            case R.id.re_btn_res:
                startActivity(new Intent(RegistrActivity.this, LoginActivity.class));
                RegistrActivity.this.finish();
                break;
            //验证码
            case R.id.btn_send:
                if (editPhone != null && editPhone.length() == 11) {
                    time.start();//开始计时
                    OkGo.post(WeiYunURL.RE_PHONE_MUBER)//
                            .tag(this)//
                            .isMultipart(true)
                            .headers("Content-Type", "application/x-www-form-urlencoded")// 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                            .params("mobile", editPhone)
                            .params("app_token", WeiYunURL.NOMAL_TOKEN)  // 这里可以上传参数
                            // 可以添加文件上传
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    if (s.contains("400") && s.contains("10106")) {
                                        Toast.makeText(RegistrActivity.this, "此手机号已经注册", Toast.LENGTH_SHORT).show();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                startActivity(new Intent(RegistrActivity.this, LoginActivity.class));
                                                RegistrActivity.this.finish();
                                            }
                                        }, 1000);
                                    } else if (s.contains("400") && s.contains("10104")) {
                                        Toast.makeText(RegistrActivity.this, "手机号错误重新输入手机号", Toast.LENGTH_SHORT).show();
                                    } else if (s.contains("200")) {
                                        Toast.makeText(RegistrActivity.this, "验证成功请输入验证码", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(RegistrActivity.this, "手机号错误或者不能为空", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_accout:
                String editCrc = edit_crc.getText().toString();
                if (editPhone != null && editCrc.length() > 4 && editSerect != null) {
                    OkGo.post(WeiYunURL.RE_PHONE_REGISTER)//
                            .tag(this)//
                            .isMultipart(true)
                            .headers("Content-Type", "application/x-www-form-urlencoded")// 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                            .params("account", editPhone)
                            .params("password", editSerect)
                            .params("code", editCrc)
                            .params("app_token", WeiYunURL.NOMAL_TOKEN) // 这里可以上传参数
                            // 可以添加文件上传
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    JSONObject jsonObject = null;
                                    try {
                                        jsonObject = new JSONObject(s);
                                        int status = jsonObject.getInt("status");
                                        if (status == 400) {
                                            Log.i("nnn", s + " 400");
                                            Toast.makeText(RegistrActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                                        } else if (status == 200) {
                                            Log.i("nnn", s + " 200");
                                            startActivity(new Intent(RegistrActivity.this, LoginActivity.class));
                                            RegistrActivity.this.finish();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                } else {
                    Toast.makeText(RegistrActivity.this, "手机号验证码密码都不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
            //返回
            case R.id.iv_find_back:
                startActivity(new Intent(RegistrActivity.this, LoginActivity.class));
                RegistrActivity.this.finish();
                break;


            case R.id.iv_phone_delete:
                edit_phone.setText("");
                break;
            case R.id.iv_yan_delete:
                edit_crc.setText("");
                break;
            case R.id.iv_pwd_delete:
                edit_serect.setText("");
                break;
        }
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发

            btn_send.setText("重发验证码");
            btn_send.setClickable(true);
            //FEFFF5
            btn_send.setTextColor(Color.rgb(255, 255, 255));
            btn_send.setBackgroundColor(Color.rgb(87, 116, 152));


        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            btn_send.setClickable(false);
            btn_send.setText(millisUntilFinished / 1000 + "秒");
            btn_send.setTextColor(Color.rgb(190, 190, 190));
            btn_send.setBackgroundColor(Color.rgb(246, 246, 246));
        }
    }
}
