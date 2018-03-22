package com.shdnxc.cn.activity.ui;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.shdnxc.cn.activity.R;
import com.shdnxc.cn.activity.Utils.ConstantUils;
import com.shdnxc.cn.activity.Utils.SharedUtils;
import com.shdnxc.cn.activity.Utils.WeiYunURL;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Zheng Jungen on 2017/3/22.
 */
public class OpinonActivity extends Activity implements View.OnClickListener {
    private Button btn_send;
    private EditText eidtcode, editcontent;
    String token = "";
    String appVersionName = "";
    private ImageView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinon);
        token = SharedUtils.getString(OpinonActivity.this, ConstantUils.CU_LOGIN, "0");
        Log.i("ASP", token + " ---------------------------------------  ");
        initviews();
        appVersionName = getAppVersionName(this);
        Log.i("ASP", appVersionName + " ---------------------------------------  ");
        iniLisenter();

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


    private void iniLisenter() {
        btn_send.setOnClickListener(this);
        mBack.setOnClickListener(this);
    }

    private void initviews() {
        mBack = (ImageView) findViewById(R.id.mBack);
        btn_send = (Button) findViewById(R.id.btn_send);
        eidtcode = (EditText) findViewById(R.id.eidtcode);
        editcontent = (EditText) findViewById(R.id.editcontent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mBack:

                OpinonActivity.this.finish();
                break;
            case R.id.btn_send:

                String contets = editcontent.getText().toString();
                String codes = eidtcode.getText().toString();
                if (contets != null && codes != null) {
                    OkGo.post(WeiYunURL.MAC_FANKUI)//
                            .tag(this)//
                            .isMultipart(true)
                            .headers("Content-Type", "application/x-www-form-urlencoded")// 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                            .params("token", token)
                            .params("content", contets)
                            .params("app_token", WeiYunURL.NOMAL_TOKEN)
                            .params("version", appVersionName)
                            .params("remark", codes)
                            // 这里可以上传参数
                            // 可以添加文件上传
                            .execute(
                                    new StringCallback() {
                                        @Override
                                        public void onSuccess(String s, Call call, Response response) {
                                            Toast.makeText(OpinonActivity.this, "提交成功", Toast.LENGTH_LONG).show();

                                            editcontent.setText("");
                                            eidtcode.setText("");
                                        }
                                    });
                }
                break;
        }
    }
}
