package com.shdnxc.cn.activity.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lzy.okgo.OkGo;
import com.shdnxc.cn.activity.R;
import com.shdnxc.cn.activity.Utils.ConstantUils;
import com.shdnxc.cn.activity.Utils.SharedUtils;
import com.shdnxc.cn.activity.Utils.WeiYunURL;
import com.shdnxc.cn.activity.adapter.BreakAdapter;
import com.shdnxc.cn.activity.adapter.BreakAdapter2;
import com.shdnxc.cn.activity.bean.BreakCodeBean;
import com.shdnxc.cn.activity.okgo.JsonCallbackABC;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Zheng Jungen on 2017/3/21.
 */
public class BreakActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mBack;
    private ListView lv;
    BreakAdapter adapter;
    BreakAdapter2 adapter2;
    String deviceAddre = "";
    String token = "";
    List<BreakCodeBean.DataBean> data;
    List<BreakCodeBean.DataBean> data2 = new ArrayList<>();
    private RelativeLayout re_sigle3;
    String pdata = "";

    private ListView break_lv_data;

    private ArrayList<String> lists = new ArrayList<>();
    Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    re_sigle3.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    re_sigle3.setVisibility(View.INVISIBLE);
                    break;
            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = SharedUtils.getString(BreakActivity.this, ConstantUils.CU_LOGIN, "0");
        Log.i("LLL", token + "   ");
        deviceAddre = SharedUtils.getString(BreakActivity.this, ConstantUils.CU_SPLASH, "0");
        Log.i("LLL", deviceAddre + "   ");
        setContentView(R.layout.activity_braek);
        initviews();
        Intent intent = getIntent();
        pdata = intent.getStringExtra("pdata");
        Log.i("LLL", pdata + "   ");
        iniLisenter();
        initData();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BreakCodeBean.DataBean dataBean = data.get(position);
                Intent intent1 = new Intent();
                String dtc = dataBean.getDtc();
                String definition = dataBean.getDefinition();
                String meaning = dataBean.getMeaning();
                intent1.putExtra("codedet", dtc);
                intent1.putExtra("codedef", definition);
                intent1.putExtra("codemean", meaning);
                intent1.setClass(BreakActivity.this, BreakInfoActivity.class);
                startActivity(intent1);
            }
        });
    }

    private void iniLisenter() {
        mBack.setOnClickListener(this);
    }

    private void initData() {
        if (pdata != null) {
            Log.i("LLL", pdata + " " + "------------------------------");
            OkGo.post(WeiYunURL.CODE_URL)//
                    .tag(this)//
                    .isMultipart(true)
                    .headers("Content-Type", "application/x-www-form-urlencoded")// 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                    .params("app_token", WeiYunURL.NOMAL_TOKEN)
                    .params("code", pdata)
                    .params("sn", deviceAddre)
                    .params("token", token)
                    // 这里可以上传参数
                    // 可以添加文件上传
                    .execute(
                            new JsonCallbackABC<BreakCodeBean>() {
                                @Override
                                public void onSuccess(BreakCodeBean breakCodeBean, Call call, Response response) {
                                    data = breakCodeBean.getData();
                                    List<BreakCodeBean.DataBean> data = breakCodeBean.getData();
                                    int size = data.size();
                                    Message msg = Message.obtain();
                                    if (size == 0) {
                                        msg.what = 1;
                                        mHander.sendMessage(msg);
                                        Log.i("LLL", data.size() + " " + "------------------------------");
                                    } else {
                                        for (int i = 0; i < data.size(); i++) {
                                            boolean contains = pdata.contains(data.get(i).getDtc());
                                            Log.i("ASP", data.size() + " ");
                                            if (contains && i == data.size() - 1) {
                                                pdata = pdata.replace(data.get(i).getDtc(), "");
                                            } else if (contains && i < data.size() - 1) {
                                                pdata = pdata.replace(data.get(i).getDtc() + ",", "");
                                            }
                                        }
                                        String datas = pdata.replace(",", "");
                                        for (int i = 0; i < datas.length(); i += 5) {
                                            lists.add(datas.substring(i, i + 5));
                                            Log.i("LLL", datas.substring(i, i + 5));
                                        }
                                        msg.what = 2;
                                        mHander.sendMessage(msg);
                                        adapter = new BreakAdapter(BreakActivity.this, BreakActivity.this.data);
                                        lv.setAdapter(adapter);
                                        if (lists.size() > 0) {
                                            adapter2 = new BreakAdapter2(BreakActivity.this, lists);
                                            break_lv_data.setAdapter(adapter2);
                                        }
                                    }

                                }
                            });
        }
    }


    private void initviews() {
        mBack = (ImageView) findViewById(R.id.iv_find_back);
        lv = (ListView) findViewById(R.id.break_lv);
        re_sigle3 = (RelativeLayout) findViewById(R.id.re_sigle3);
        break_lv_data = (ListView) findViewById(R.id.break_lv_data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_find_back:
                BreakActivity.this.finish();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
