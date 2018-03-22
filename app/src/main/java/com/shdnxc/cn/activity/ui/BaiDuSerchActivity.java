package com.shdnxc.cn.activity.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.shdnxc.cn.activity.R;
import com.shdnxc.cn.activity.Utils.ConstantUils;
import com.shdnxc.cn.activity.Utils.SharedUtils;
import com.shdnxc.cn.activity.adapter.BaiduAdapter;
import com.shdnxc.cn.activity.app.IAPI;
import com.shdnxc.cn.activity.bean.BaiDuBean;
import com.shdnxc.cn.activity.dbDao.RecordsDao;
import com.shdnxc.cn.activity.view.SearchEditText;
import com.shdnxc.cn.activity.view.SpinnerLoading;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zheng Jungen on 2017/4/17.
 */
public class BaiDuSerchActivity extends Activity implements View.OnClickListener, SearchEditText.OnSearchClickListener, Inputtips.InputtipsListener {

    private SearchEditText edit_serect;

    private ListView lv_baidu;
    private SuggestionSearch mSuggestionSearch;

    private ArrayList<BaiDuBean> mBaiduInfo = new ArrayList<>();

    private BaiduAdapter adapter;

    private ImageView iv_find_back;

    private String disNO;

    private String nomalCurrent;
    private String nomalCurren2;
    private String nomalAni;
    private String nomalAni2;
    private SpinnerLoading mSpain;

    private Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 300:
                    BaiDuIndexActivity.instance.finish();
                    break;
            }
        }
    };
    RecordsDao mDbDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidu_serch);

        disNO = SharedUtils.getString(BaiDuSerchActivity.this, ConstantUils.CU_CAR, "NO");
        mDbDao = new RecordsDao(this);
        nomalCurrent = SharedUtils.getString(BaiDuSerchActivity.this, ConstantUils.BAIDU_CURRENT, "NO");
        nomalCurren2 = SharedUtils.getString(BaiDuSerchActivity.this, ConstantUils.BAIDU_SECORD_CURRENT, "NO");
        nomalAni = SharedUtils.getString(BaiDuSerchActivity.this, ConstantUils.BAIDU_END, "NO");
        nomalAni2 = SharedUtils.getString(BaiDuSerchActivity.this, ConstantUils.BAIDU_SECORD_END, "NO");
        initviews();
        mSpain.setPaintMode(1);
        mSpain.setCircleRadius(20);
        mSpain.setItemCount(8);
//        initBaiduSerchData();
        edit_serect.setOnSearchClickListener(this);
        iv_find_back.setOnClickListener(this);
        lv_baidu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BaiDuBean baiDuBean = mBaiduInfo.get(position);
                String addresName = baiDuBean.getAddresName();
                String mLati = String.valueOf(baiDuBean.getmLatitude());
                String mLongitude = String.valueOf(baiDuBean.getmLongitude());
                String mMessgeInfo = addresName + "," + mLati + "," + mLongitude;
                boolean hasRecord = mDbDao.isHasRecord(mMessgeInfo);
                Log.i("====", hasRecord + "-----------------------------------------------------------");
                if (!hasRecord) {
                    mDbDao.addRecords(mMessgeInfo);
                }
                if (nomalAni.equals("YES")) {
                    Intent intent = new Intent();
                    intent.setAction(ConstantUils.BAIDU_END);
                    intent.putExtra("mLatude", baiDuBean.getmLatitude());
                    intent.putExtra("mLongtude", baiDuBean.getmLongitude());
                    intent.putExtra("mAddName", baiDuBean.getAddresName());
                    sendBroadcast(intent);
                    SharedUtils.saveString(BaiDuSerchActivity.this, ConstantUils.BAIDU_END, "NO");
                }

                if (nomalAni2.equals("YES")) {
                    Intent intent = new Intent();
                    intent.setAction(ConstantUils.BAIDU_SECORD_END);
                    intent.putExtra("mLatude", baiDuBean.getmLatitude());
                    intent.putExtra("mLongtude", baiDuBean.getmLongitude());
                    intent.putExtra("mAddName", baiDuBean.getAddresName());
                    sendBroadcast(intent);
                    SharedUtils.saveString(BaiDuSerchActivity.this, ConstantUils.BAIDU_SECORD_END, "NO");
                }
                if (nomalCurrent.equals("YES")) {
                    Intent intent = new Intent();
                    intent.setAction(ConstantUils.BAIDU_CURRENT);
                    intent.putExtra("mLatude", baiDuBean.getmLatitude());
                    intent.putExtra("mLongtude", baiDuBean.getmLongitude());
                    intent.putExtra("mAddName", baiDuBean.getAddresName());
                    sendBroadcast(intent);
                    SharedUtils.saveString(BaiDuSerchActivity.this, ConstantUils.BAIDU_CURRENT, "NO");
                }

                if (nomalCurren2.equals("YES")) {
                    Intent intent = new Intent();
                    intent.setAction(ConstantUils.BAIDU_SECORD_CURRENT);
                    intent.putExtra("mLatude", baiDuBean.getmLatitude());
                    intent.putExtra("mLongtude", baiDuBean.getmLongitude());
                    intent.putExtra("mAddName", baiDuBean.getAddresName());
                    sendBroadcast(intent);
                    SharedUtils.saveString(BaiDuSerchActivity.this, ConstantUils.BAIDU_SECORD_CURRENT, "NO");
                }
                BaiDuSerchActivity.this.finish();
            }
        });

     /*   // 实现TextWatcher监听即可
        edit_serect.setOnSearchClickListener(new SearchEditText.OnSearchClickListener() {
            @Override
            public void onSearchClick(View view) {
                mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                        .keyword("上海")
                        .city(""));
            }
        });*/
    }

    private void initviews() {
        iv_find_back = (ImageView) findViewById(R.id.iv_find_back);
        edit_serect = (SearchEditText) findViewById(R.id.edit_serect);
        lv_baidu = (ListView) findViewById(R.id.lv_baidu);
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSpain = (SpinnerLoading) findViewById(R.id.spinner_loading);
        edit_serect.setFocusable(true);

    }

//    private void initBaiduSerchData() {
//        mSuggestionSearch.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
//            public void onGetSuggestionResult(SuggestionResult res) {
//                Log.i("====", "===信息");
//                if (res == null || res.getAllSuggestions() == null) {
//                    Toast.makeText(BaiDuSerchActivity.this, "没有查询到结果", Toast.LENGTH_SHORT).show();
//                    mSpain.setVisibility(View.INVISIBLE);
//                    return;
//                    //未找到相关结果
//                } else {
//                    //获取在线建议检索结果
//                    List<SuggestionResult.SuggestionInfo> allSuggestions = res.getAllSuggestions();
//                    mBaiduInfo.clear();
//                    for (SuggestionResult.SuggestionInfo pi : allSuggestions) {
//                        //latitude   维度
//                        if (pi.pt != null) {
//                            Log.i("====", "===信息：" + pi.key + " 维度" + pi.pt.latitude + "  经度" + pi.pt.longitude);
//
//                            mBaiduInfo.add(new BaiDuBean(pi.key, pi.pt.latitude, pi.pt.longitude));
//                            mSpain.setVisibility(View.INVISIBLE);
//                        }
//                    }
//                    adapter = new BaiduAdapter(BaiDuSerchActivity.this, mBaiduInfo);
//                    lv_baidu.setAdapter(adapter);
//
//                }
//
//            }
//        });
//    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_find_back:
                BaiDuSerchActivity.this.finish();
                break;
        }

    }

    @Override
    public void onSearchClick(View view) {
        switch (view.getId()) {
            case R.id.edit_serect:

                boolean net = IAPI.isNet(BaiDuSerchActivity.this);
                if (net) {
                    String editInfo = edit_serect.getText().toString();
                    if (editInfo.length() > 0) {
                        mSpain.setVisibility(View.VISIBLE);

                        //第二个参数传入null或者“”代表在全国进行检索，否则按照传入的city进行检索
                        InputtipsQuery inputquery = new InputtipsQuery(editInfo, "");
                        inputquery.setCityLimit(true);//限制在当前城市

                        Inputtips inputTips = new Inputtips(BaiDuSerchActivity.this, inputquery);
                        inputTips.setInputtipsListener(this);
                        inputTips.requestInputtipsAsyn();
                        //execute the task
//                                mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
//                                        .keyword(editInfo)
//                                        .city(""));
                    }
                } else {
                    Toast.makeText(BaiDuSerchActivity.this, "网络没有打开请打开网络", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    //对蓝牙服务的支持
    private static IntentFilter bleIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantUils.BLE_CONN_FALSE);
        intentFilter.addAction(ConstantUils.BLE_CONN_SUCCESS);
        intentFilter.addAction(ConstantUils.BLE_CONN_BREAK);
        intentFilter.addAction(ConstantUils.BLE_HUD_TO_APP);
        intentFilter.addAction(ConstantUils.BLE_ADDRESS_NAME);
        intentFilter.addAction(ConstantUils.BEL_SYSTEM_CLOSE);
        intentFilter.addAction(ConstantUils.BLE_CRC_ERROR);
        intentFilter.addAction(ConstantUils.BEL_SYSTEM_NO_SUPPORT);
        //20170414  添加的代码
        intentFilter.addAction(ConstantUils.BLE_START_NO_SUPPORT_BLE);
        intentFilter.addAction(ConstantUils.BLE_START_NO_SUPPORT_LANYA);
        intentFilter.addAction(ConstantUils.BLE_START_NO_START);

        return intentFilter;
    }


    @Override
    public void onGetInputtips(List<Tip> list, int i) {
        if (list.size() != 0) {
            mBaiduInfo.clear();
            for (Tip t : list) {
                if (t.getPoint() != null && !t.getAddress().equals("")) {
                    mBaiduInfo.add(new BaiDuBean(t.getAddress(), t.getName(), t.getPoint().getLatitude(), t.getPoint().getLongitude()));
                    mSpain.setVisibility(View.INVISIBLE);
                }
//                Log.i(TAG, "onGetInputtips: "+t.getName()+"  "+t.getAddress()+" "+t.getPoiID());
            }
            adapter = new BaiduAdapter(BaiDuSerchActivity.this, mBaiduInfo);
            lv_baidu.setAdapter(adapter);
        } else {
            mSpain.setVisibility(View.INVISIBLE);
            Toast.makeText(BaiDuSerchActivity.this, "没有查询到结果", Toast.LENGTH_SHORT).show();
        }
    }
}
