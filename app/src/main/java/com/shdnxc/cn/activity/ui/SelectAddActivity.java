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

import com.shdnxc.cn.activity.R;
import com.shdnxc.cn.activity.adapter.AddressAdapter;
import com.shdnxc.cn.activity.bean.AddressBean;

import java.util.ArrayList;

/**
 * Created by Zheng Jungen on 2017/4/1.
 */
public class SelectAddActivity extends Activity implements View.OnClickListener {

    private ImageView mBack;
    private TextView tv_title;
    //数据集合
    private ArrayList<AddressBean> addressColletions;

    private AddressAdapter adapter;

    private ListView lv_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actiivty_address_select);
        initviews();
        initLenster();
        addData();
        inidata();
    }

    private void addData() {
        addressColletions.add(new AddressBean("110000", "北京"));
        addressColletions.add(new AddressBean("120000", "天津"));
        addressColletions.add(new AddressBean("130000", "河北"));
        addressColletions.add(new AddressBean("140000", "山西"));
        addressColletions.add(new AddressBean("150000", "内蒙古"));
        addressColletions.add(new AddressBean("210000", "辽宁"));
        addressColletions.add(new AddressBean("220000", "吉林"));
        addressColletions.add(new AddressBean("230000", "黑龙江"));
        addressColletions.add(new AddressBean("310000", "上海"));
        addressColletions.add(new AddressBean("320000", "江苏"));
        addressColletions.add(new AddressBean("330000", "浙江"));
        addressColletions.add(new AddressBean("340000", "安徽"));
        addressColletions.add(new AddressBean("350000", "福建"));
        addressColletions.add(new AddressBean("360000", "江西"));
        addressColletions.add(new AddressBean("370000", "山东"));
        addressColletions.add(new AddressBean("410000", "河南"));
        addressColletions.add(new AddressBean("420000", "湖北"));
        addressColletions.add(new AddressBean("430000", "湖南"));
        addressColletions.add(new AddressBean("440000", "广东"));
        addressColletions.add(new AddressBean("450000", "广西"));
        addressColletions.add(new AddressBean("460000", "海南"));
        addressColletions.add(new AddressBean("500000", "重庆"));
        addressColletions.add(new AddressBean("510000", "四川"));
        addressColletions.add(new AddressBean("520000", "贵州"));
        addressColletions.add(new AddressBean("530000", "云南"));
        addressColletions.add(new AddressBean("610000", "陕西"));
        addressColletions.add(new AddressBean("620000", "甘肃"));
        addressColletions.add(new AddressBean("630000", "青海"));
        addressColletions.add(new AddressBean("640000", "宁夏"));
        addressColletions.add(new AddressBean("650000", "新疆"));
        addressColletions.add(new AddressBean("710000", "台湾"));
        addressColletions.add(new AddressBean("810000", "香港"));
        addressColletions.add(new AddressBean("820000", "澳门"));
    }

    private void inidata() {
        adapter = new AddressAdapter(SelectAddActivity.this, addressColletions);
        lv_address.setAdapter(adapter);
        lv_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i("JKS", addressColletions.get(position).getAddressId()+" addressId");
                Intent intent = new Intent(SelectAddActivity.this, SelectAddInfoActivity.class);
                intent.putExtra("addressId", addressColletions.get(position).getAddressId());
                intent.putExtra("addressName", addressColletions.get(position).getAddressName());
                startActivity(intent);
                SelectAddActivity.this.finish();
              /*  SelectAddActivity.this.finish();*/
            }
        });
    }

    private void initLenster() {
        mBack.setOnClickListener(this);
    }

    private void initviews() {
        lv_address = (ListView) findViewById(R.id.lv_address);
        mBack = (ImageView) findViewById(R.id.iv_find_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("选择地址");
        addressColletions = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_find_back:
                startActivity(new Intent(SelectAddActivity.this, PersonInfoActivity.class));
                SelectAddActivity.this.finish();
                break;
        }
    }
}
