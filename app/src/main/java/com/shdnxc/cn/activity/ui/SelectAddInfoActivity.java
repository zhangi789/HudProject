package com.shdnxc.cn.activity.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.shdnxc.cn.activity.R;
import com.shdnxc.cn.activity.Utils.ConstantUils;
import com.shdnxc.cn.activity.Utils.SharedUtils;
import com.shdnxc.cn.activity.Utils.WeiYunURL;
import com.shdnxc.cn.activity.adapter.AddressInfoAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Zheng Jungen on 2017/4/1.
 */
public class SelectAddInfoActivity extends Activity implements View.OnClickListener {
    private ImageView mBack;
    private TextView tv_title;
    String ids = "";
    private String addressName;
    private ListView lv_address_info;
    private ArrayList<String> addressInfos;


    private ArrayList<String> address_regionId;

    private AddressInfoAdapter adapter;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = SharedUtils.getString(SelectAddInfoActivity.this, ConstantUils.CU_LOGIN, "0");
        Intent intent =
                getIntent();
        ids = intent.getStringExtra("addressId");
        addressName = intent.getStringExtra("addressName");
        Log.i("PPP", "PPP " + ids);
        setContentView(R.layout.actiivty_add_info);
        initviews();
        initLenster();
        initdata();
        lv_address_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String mRegionId = address_regionId.get(position);
                OkGo.post(WeiYunURL.UPDATE_USER_INFO).isMultipart(true)
                        .headers("Content-Type", "application/x-www-form-urlencoded")// 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                        .params("token", token)
                        .params("app_token", WeiYunURL.NOMAL_TOKEN)  // 这里可以上传参数
                        .params("region_id", mRegionId)
                        // 可以添加文件上传
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                try {
                                    JSONObject json = new JSONObject(s);
                                    String status = json.getString("status");
                                    if (status.equals("200")) {
                                        String addressInfoss = addressName + addressInfos.get(position);
                                        SharedUtils.saveString(SelectAddInfoActivity.this, ConstantUils.USER_ADDRESS_CITY, addressInfoss);
                                        Intent intent = new Intent(SelectAddInfoActivity.this, PersonInfoActivity.class);
                                        startActivity(intent);
                                        SelectAddInfoActivity.this.finish();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });


            }
        });
    }

    private void initdata() {
        OkGo.post(WeiYunURL.MAC_ADDRESS_AREA)
                .tag(this)//
                .isMultipart(true)
                .headers("Content-Type", "application/x-www-form-urlencoded")// 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                .params("id", ids)
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
                                    String status = json.getString("status");
                                    JSONObject data = json.getJSONObject("data");
                                    String s1 = data.toString();
                                    Log.i("PPP", "PPP " + s1);

                                    String[] split = s1.replace("{", "").replace("}", "").split(",");

                                    for (int i = 0; i < split.length; i++) {

                                        Log.i("JKS", split[i].split(":")[0] + " " + split[i].split(":")[1]);

                                        String replace = split[i].split(":")[1];
                                        addressInfos.add(replace.substring(1, replace.length() - 1));
                                        address_regionId.add(split[i].split(":")[0]);
                                    }
                                    adapter = new AddressInfoAdapter(SelectAddInfoActivity.this, addressInfos);
                                    lv_address_info.setAdapter(adapter);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });


    }

    private void initLenster() {
        mBack.setOnClickListener(this);
    }

    private void initviews() {
        address_regionId = new ArrayList<>();
        addressInfos = new ArrayList<>();
        lv_address_info = (ListView) findViewById(R.id.lv_address_info);
        mBack = (ImageView) findViewById(R.id.iv_find_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("地址详情");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_find_back:
                startActivity(new Intent(SelectAddInfoActivity.this, SelectAddActivity.class));
                SelectAddInfoActivity.this.finish();
                break;
        }
    }
}
