package com.shdnxc.cn.activity.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Zheng Jungen on 2017/3/13.
 */
public class ForgetActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edit_for_phone, edit_for_yan, edit_for_pwd;
    private ImageView iv_phone_delete, iv_yan_delete, iv_pwd_delete, toor_iv_back;

    private Button tv_send_yan;

    private Button btn_reset;

    private TimeCount time;


    private RelativeLayout re_btn_enent;

    private RelativeLayout re_input_iphone;
    private RelativeLayout re_input_yan;

    private RelativeLayout re_input_new_pwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actiivty_forget);

        time = new TimeCount(60000, 1000);//构造CountDownTimer对象
        initviews();
        initLisenter();
    }

    private void initLisenter() {
        re_input_new_pwd.setOnClickListener(this);
        re_input_yan.setOnClickListener(this);
        iv_phone_delete.setOnClickListener(this);
        iv_yan_delete.setOnClickListener(this);
        iv_pwd_delete.setOnClickListener(this);
        tv_send_yan.setOnClickListener(this);
        toor_iv_back.setOnClickListener(this);
        btn_reset.setOnClickListener(this);
        re_btn_enent.setOnClickListener(this);
        re_input_iphone.setOnClickListener(this);
    }

    private void initviews() {
        re_input_new_pwd = (RelativeLayout) findViewById(R.id.re_input_new_pwd);
        re_input_yan = (RelativeLayout) findViewById(R.id.re_input_yan);
        re_input_iphone = (RelativeLayout) findViewById(R.id.re_input_iphone);
        re_btn_enent = (RelativeLayout) findViewById(R.id.re_btn_enent);
        btn_reset = (Button) findViewById(R.id.btn_reset);
        edit_for_phone = (EditText) findViewById(R.id.edit_for_phone);
        edit_for_yan = (EditText) findViewById(R.id.edit_for_yan);
        edit_for_pwd = (EditText) findViewById(R.id.edit_for_pwd);
        iv_phone_delete = (ImageView) findViewById(R.id.iv_phone_delete);
        iv_yan_delete = (ImageView) findViewById(R.id.iv_yan_delete);
        iv_pwd_delete = (ImageView) findViewById(R.id.iv_pwd_delete);
        toor_iv_back = (ImageView) findViewById(R.id.iv_find_back);
        tv_send_yan = (Button) findViewById(R.id.tv_send_yan);
    }

    @Override
    public void onClick(View v) {
        String editForPhone = edit_for_phone.getText().toString();
        String editForPwd = edit_for_pwd.getText().toString();
        switch (v.getId()) {

            case R.id.re_input_new_pwd:
                edit_for_pwd.setText("");
                break;
            case R.id.re_input_yan:
                edit_for_yan.setText("");
                break;
            case R.id.re_input_iphone:
                edit_for_phone.setText("");
                break;
            case R.id.re_btn_enent:
                startActivity(new Intent(ForgetActivity.this, LoginActivity.class));
                ForgetActivity.this.finish();
                break;
            case R.id.btn_reset:
                String editSerect = edit_for_yan.getText().toString();
                if (editForPhone != null && editForPwd != null && editSerect != null) {
                    OkGo.post(WeiYunURL.RE_SECRET_REGISTER)//
                            .tag(this)//
                            .isMultipart(true)
                            .headers("Content-Type", "application/x-www-form-urlencoded")// 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                            .params("mobile", editForPhone)
                            .params("code", editSerect)  // 这里可以上传参数
                            .params("newpwd", editForPwd)  // 这里可以上传参数
                            .params("app_token", WeiYunURL.NOMAL_TOKEN)  // 这里可以上传参数
                            // 可以添加文件上传
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    if (s.contains("400") && s.contains("10104")) {
                                        Toast.makeText(ForgetActivity.this, "手机号错误", Toast.LENGTH_SHORT).show();
                                    } else if (s.contains("200")) {
                                        startActivity(new Intent(ForgetActivity.this, LoginActivity.class));
                                    }
                                }

                                @Override
                                public void onError(Call call, Response response, Exception e) {
                                    Toast.makeText(ForgetActivity.this, "失败" + e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });

                } else {
                    Toast.makeText(ForgetActivity.this, "手机号验证码密码都不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_send_yan:

                if (editForPhone != null && editForPhone.length() == 11) {
                    time.start();//开始计时
                    OkGo.post(WeiYunURL.RE_PHONE_MUBER)//
                            .tag(this)//
                            .isMultipart(true)
                            .headers("Content-Type", "application/x-www-form-urlencoded")// 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                            .params("mobile", editForPhone)
                            .params("type", "1")  // 这里可以上传参数
                            .params("app_token", WeiYunURL.NOMAL_TOKEN)  // 这里可以上传参数
                            // 可以添加文件上传
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    if (s.contains("400") && s.contains("10104")) {
                                        Toast.makeText(ForgetActivity.this, "手机号错误", Toast.LENGTH_SHORT).show();
                                    } else if (s.contains("200")) {
                                        Toast.makeText(ForgetActivity.this, "验证成功请输入验证码", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(ForgetActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_phone_delete:
                edit_for_phone.setText("");
                break;
            case R.id.iv_pwd_delete:
                edit_for_pwd.setText("");
                break;
            case R.id.iv_yan_delete:
                edit_for_yan.setText("");
                break;
            case R.id.iv_find_back:
                startActivity(new Intent(ForgetActivity.this, LoginActivity.class));
                ForgetActivity.this.finish();
                break;
        }
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            tv_send_yan.setText("重发验证码");
            tv_send_yan.setClickable(true);
            //FEFFF5
            tv_send_yan.setTextColor(Color.rgb(255,255,255));
            tv_send_yan.setBackgroundColor(Color.rgb(87,116,152));
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            tv_send_yan.setClickable(false);
            tv_send_yan.setText(millisUntilFinished / 1000 + "秒");
            tv_send_yan.setTextColor(Color.rgb(190,190,190));
            tv_send_yan.setBackgroundColor(Color.rgb(246,246,246));
        }
    }
}
