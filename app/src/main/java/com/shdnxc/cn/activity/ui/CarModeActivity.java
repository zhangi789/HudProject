package com.shdnxc.cn.activity.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.shdnxc.cn.activity.R;
import com.shdnxc.cn.activity.Utils.ConstantUils;
import com.shdnxc.cn.activity.Utils.SharedUtils;
import com.shdnxc.cn.activity.Utils.WeiYunURL;
import com.shdnxc.cn.activity.adapter.SortTypeAdapter;
import com.shdnxc.cn.activity.bean.CharacterParser;
import com.shdnxc.cn.activity.bean.PinyinComparator;
import com.shdnxc.cn.activity.bean.SortModel;
import com.shdnxc.cn.activity.view.ClearEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Zheng Jungen on 2017/3/28.
 */
public class CarModeActivity extends Activity implements View.OnClickListener {
    private TextView mTitle;
    private ImageView mBack;
    private ClearEditText mClearEditText;
    private ListView sortListView;
    private CharacterParser characterParser;
    private PinyinComparator pinyinComparator;
    private String carName;
    private String carSeriesName;
    private String seriesId;
    private String brandId;
    private List<SortModel> SourceDateList = new ArrayList<>();
    private SortTypeAdapter adapter;
    private RelativeLayout tv_show;
    ArrayList<String> listID = new ArrayList<>();
    private String token;
    private String deviceAddre;
    String noState;
    private String modelsName;
    private FrameLayout mode_show_start;

    private Handler hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 400:
                 /*   DeviceInfoActivity.instance.finish();
                    CarActivity.instance.finish();
                    CarSeriesActivity.instance.finish();*/
                    Intent intent = new Intent(CarModeActivity.this, DeviceInfoActivity.class);
                    intent.setAction(ConstantUils.DEVIES_INFO_MSG);
                    Bundle mBundle = new Bundle();
                    mBundle.putString(ConstantUils.DEVIES_INFO, modelsName);
                    intent.putExtras(mBundle);
                    sendBroadcast(intent);
                    startActivity(intent);
                    CarModeActivity.this.finish();


                    break;
                case 500:
                    DeviceInfoActivity.instance.finish();
                    CarActivity.instance.finish();
                    CarSeriesActivity.instance.finish();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        token = SharedUtils.getString(CarModeActivity.this, ConstantUils.CU_LOGIN, "0");
        Log.i("ASS", token + " token");
        deviceAddre = SharedUtils.getString(CarModeActivity.this, ConstantUils.CU_SPLASH, "0");
        Log.i("NN", deviceAddre + " deviceAddre");
        brandId = intent.getStringExtra("carBandId");
        Log.i("NMN", brandId + " brandId");
        carName = intent.getStringExtra("carReName");
        carSeriesName = intent.getStringExtra("carSeriesName");
        seriesId = intent.getStringExtra("carSeries");
        Log.i("NMN", seriesId + " seriesId");
        Log.i("ASP", "--------------" + seriesId + "--------------");
        setContentView(R.layout.activity_car_mode);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initviews();
        mTitle.setText("选择车型");
        initLenter();
        OkGo.post(WeiYunURL.CAR_MODEL)//
                .tag(this)//
                .isMultipart(true)
                .headers("Content-Type", "application/x-www-form-urlencoded")// 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                .params("series_id", seriesId)
                .params("app_token", WeiYunURL.NOMAL_TOKEN)
                // 这里可以上传参数
                // 可以添加文件上传
                .execute(
                        new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {

                                if (s.contains("[]")) {
                                    tv_show.setVisibility(View.VISIBLE);
                                } else {
                                    try {

                                        JSONObject json = new JSONObject(s);
                                        JSONObject data = json.getJSONObject("data");

                                        Iterator<String> keys = data.keys();
                                        while (keys.hasNext()) {
                                            String next = keys.next();
                                            Log.i("ASP", "--------------" + next + "--------------");
                                            JSONArray jsonArray = data.getJSONArray(next);
                                            Log.i("ASP", "------" + jsonArray.length() + "----");
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject dataBean = jsonArray.getJSONObject(i);
                                                Log.i("ASP", "-----第" + i + "--次-");
                                                String id = dataBean.getString("id");
                                                Log.i("ASP", "id " + id);
                                                listID.add(id);
                                                String name = dataBean.getString("name");
                                                Log.i("ASP", "name " + name);
                                                SortModel sortModel = new SortModel();
                                                sortModel.setName(name);
                                                //汉字转换成拼音
                                                String pinyin = characterParser.getSelling(name);
                                                String sortString = pinyin.substring(0, 1).toUpperCase();
                                                if (next.equals("")) {
                                                    sortModel.setSortLetters("0");
                                                } else {
                                                    sortModel.setSortLetters(next);

                                                }
                                                SourceDateList.add(sortModel);
                                            }
                                        }

                                        mClearEditText.setVisibility(View.VISIBLE);
                                        mode_show_start.setVisibility(View.VISIBLE);
                                        // 根据a-z进行排序源数据
                                        adapter = new SortTypeAdapter(CarModeActivity.this, SourceDateList);
                                        sortListView.setAdapter(adapter);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                        });

        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //这里要利用adapter.getItem(position)来获取当前position所对应的对象
                Log.i("NMN", listID.get(position));

                String listNames = ((SortModel) adapter.getItem(position)).getName();
                Log.i("PPP", "car  list " + listNames);
                String Spoton = listID.get(position);
                Log.i("PPP", "car  list potion " + listID.get(position));
                SharedUtils.saveString(CarModeActivity.this, "listNames", listNames);


                Log.i("JKS", Spoton + "   Spoton");
/**
 * 绑定设备
 */
                OkGo.post(WeiYunURL.MAC_BIND)//
                        .tag(this)//
                        .isMultipart(true)
                        .headers("Content-Type", "application/x-www-form-urlencoded")// 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                        .params("token", token)
                        .params("sn", deviceAddre)
                        .params("models_id", Spoton)
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
                                            Log.i("NMN", "总数据3：" + brandId + " " + seriesId + " " + listID.get(position) + " " + s);
                                            JSONObject json = new JSONObject(s);
                                            JSONObject data = json.getJSONObject("data");
                                            modelsName = data.getString("modelsName");
                                            SharedUtils.saveString(CarModeActivity.this, ConstantUils.USER_DEVICE_CAR_NAME, modelsName);
                                            Log.i("PPP", " car modelsName          " + modelsName);
                                            String name = ((SortModel) adapter.getItem(position)).getName();
                                        /*    Message mess = Message.obtain();
                                            mess.what = 400;
                                            hander.sendMessage(mess);*/

                                            Intent intent = new Intent(CarModeActivity.this, DeviceInfoActivity.class);
                                         /*   intent.setAction(ConstantUils.DEVIES_INFO_MSG);
                                            Bundle mBundle = new Bundle();
                                            mBundle.putString(ConstantUils.DEVIES_INFO, modelsName);
                                            intent.putExtras(mBundle);
                                            sendBroadcast(intent);*/
                                            startActivity(intent);
                                            CarModeActivity.this.finish();




                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

            }


        });
        tv_show.setOnClickListener(this);

        //根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }

    private void initLenter() {
        mBack.setOnClickListener(this);
    }

    private void initviews() {
        mTitle = (TextView) findViewById(R.id.tv_title);
        mBack = (ImageView) findViewById(R.id.iv_find_back);

        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();
        tv_show = (RelativeLayout) findViewById(R.id.tv_show);
        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
        mode_show_start = (FrameLayout) findViewById(R.id.mode_show_start);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_find_back:
                //  startActivity(new Intent(CarModeActivity.this,CarSeriesActivity.class));
                CarModeActivity.this.finish();
                break;

            case R.id.tv_show:
                OkGo.post(WeiYunURL.MAC_BIND)//
                        .tag(this)//
                        .isMultipart(true)
                        .headers("Content-Type", "application/x-www-form-urlencoded")// 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                        .params("token", token)
                        .params("sn", deviceAddre)
                        .params("brandId", brandId)
                        .params("seriesId", seriesId)
                        .params("app_token", WeiYunURL.NOMAL_TOKEN)
                        // 这里可以上传参数
                        // 可以添加文件上传
                        .execute(
                                new StringCallback() {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {

                                        try {
                                            Log.i("NMN", "总数据2：" + brandId + " " + seriesId + " " + " " + s);
                                            JSONObject json = new JSONObject(s);
                                            JSONObject data = json.getJSONObject("data");
                                            String modelsName = data.getString("modelsName");
                                            Log.i("NMN", modelsName + " 2");
                                       //     SharedUtils.saveString(CarModeActivity.this, "carType", modelsName);

                                            SharedUtils.saveString(CarModeActivity.this, ConstantUils.USER_DEVICE_CAR_NAME, modelsName);

                                        /*    Message mess = Message.obtain();
                                            mess.what = 500;
                                            hander.sendMessage(mess);*/
                                            Intent intent = new Intent(CarModeActivity.this, DeviceInfoActivity.class);
                                          /*  intent.setAction(ConstantUils.DEVIES_INFO_MSG2);
                                            intent.putExtra(ConstantUils.DEVIES_INFO2, modelsName);
                                            sendBroadcast(intent);*/
                                            startActivity(intent);
                                            CarModeActivity.this.finish();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });


                break;
        }
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }

    @Override
    public void onBackPressed() {
        CarModeActivity.this.finish();
    }
}
