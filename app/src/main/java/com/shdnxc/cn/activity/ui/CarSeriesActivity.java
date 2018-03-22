package com.shdnxc.cn.activity.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Zheng Jungen on 2017/3/28.
 */
public class CarSeriesActivity extends Activity implements View.OnClickListener {

    private TextView mTitle;
    private ImageView mBack;
    String carId;
    String carName;
    ArrayList<String> listName = new ArrayList<>();
    ArrayList<String> listID = new ArrayList<>();
    ArrayList<String> listHasChild = new ArrayList<>();

    private HashMap<String, String> mMap = new HashMap<>();
    private List<SortModel> SourceDateList = new ArrayList<>();
    private SortTypeAdapter adapter;
    private CharacterParser characterParser;
    private PinyinComparator pinyinComparator;
    private ClearEditText mClearEditText;
    private ListView sortListView;
    private String token;
    private String deviceAddre;
    public static CarSeriesActivity instance = null;

    private FrameLayout series_show_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = SharedUtils.getString(CarSeriesActivity.this, ConstantUils.CU_LOGIN, "0");
        deviceAddre = SharedUtils.getString(CarSeriesActivity.this, ConstantUils.CU_SPLASH, "0");
        Intent intent = getIntent();
        carId = intent.getStringExtra("carId");
        carName = intent.getStringExtra("carName");
        setContentView(R.layout.activity_car_series);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initviews();
        instance = this;

        mTitle.setText("选择车系");
        initLenter();
        OkGo.post(WeiYunURL.CAR_SERES)//
                .tag(this)//
                .isMultipart(true)
                .headers("Content-Type", "application/x-www-form-urlencoded")// 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                .params("brand_id", carId)
                .params("app_token", WeiYunURL.NOMAL_TOKEN)
                // 这里可以上传参数
                // 可以添加文件上传
                .execute(
                        new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                try {
                                    JSONObject json = new JSONObject(s);
                                    JSONObject data = json.getJSONObject("data");
                                    Iterator<String> keys = data.keys();
                                    while (keys.hasNext()) {
                                        String next = keys.next();
                                        Log.i("ASS", "--------------" + next + "--------------");
                                        JSONArray jsonArray = data.getJSONArray(next);
                                        Log.i("ASS", "------" + jsonArray.length() + "----");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject dataBean = jsonArray.getJSONObject(i);
                                            Log.i("ASS", "-----第" + i + "--次-");
                                            String id = dataBean.getString("id");
                                            Log.i("ASS", "id " + id);
                                            String name = dataBean.getString("name");
                                            Log.i("ASS", "name " + name);
                                            String hasChild = dataBean.getString("hasChild");
                                            Log.i("ASS", "hasChild " + hasChild);
                                            listID.add(id);
                                            listName.add(next);
                                            //    mMap.put(next, name);
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
                                    series_show_start.setVisibility(View.VISIBLE);

                                    // 根据a-z进行排序源数据
                                    adapter = new SortTypeAdapter(CarSeriesActivity.this, SourceDateList);
                                    sortListView.setAdapter(adapter);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //这里要利用adapter.getItem(position)来获取当前position所对应的对象
                String seriesName = ((SortModel) adapter.getItem(position)).getName();
                Log.i("PPP", "car  series  " + seriesName);
                Log.i("PPP", "car  series potion " + listID.get(position));
                SharedUtils.saveString(CarSeriesActivity.this, "seriesName", seriesName);
                String carModeId = listID.get(position);
                OkGo.post(WeiYunURL.MAC_BIND)//
                        .tag(this)//
                        .isMultipart(true)
                        .headers("Content-Type", "application/x-www-form-urlencoded")// 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                        .params("token", token)
                        .params("sn", deviceAddre)
                        .params("series_id", carModeId)
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

                                            if (status.equals("200")) {
                                                JSONObject data = json.getJSONObject("data");
                                                String modelsName = data.getString("modelsName");

                                                Log.i("JKS", carModeId+"   carModeId");
                                                Log.i("PPP", "car  modelsName                  " + modelsName);
                                                Intent intent = new Intent(CarSeriesActivity.this, CarModeActivity.class);
                                                intent.putExtra("carSeries", carModeId);
                                                intent.putExtra("carSeriesName", ((SortModel) adapter.getItem(position)).getName());
                                                intent.putExtra("carReName", carName);
                                                intent.putExtra("carBandId", carId);
                                                startActivity(intent);
                                                //    CarSeriesActivity.this.finish();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

            /*    String carModeId = listID.get(position);
                Log.i("ASS", carModeId+" "+name);
                Intent intent = new Intent(CarSeriesActivity.this, CarModeActivity.class);
                intent.putExtra("carSeries",carModeId);
                intent.putExtra("carSeriesName", ((SortModel) adapter.getItem(position)).getName());
                intent.putExtra("carReName",carName);
                intent.putExtra("carBandId",carId);
                startActivity(intent);*/
            }
        });

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

        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
        series_show_start = (FrameLayout) findViewById(R.id.series_show_start);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_find_back:
                //    startActivity(new Intent(CarSeriesActivity.this,CarActivity.class));
                CarSeriesActivity.this.finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }


    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
