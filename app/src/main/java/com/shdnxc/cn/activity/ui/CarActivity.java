package com.shdnxc.cn.activity.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.shdnxc.cn.activity.R;
import com.shdnxc.cn.activity.Utils.ConstantUils;
import com.shdnxc.cn.activity.Utils.SharedUtils;
import com.shdnxc.cn.activity.Utils.WeiYunURL;
import com.shdnxc.cn.activity.adapter.SortAdapter;
import com.shdnxc.cn.activity.bean.CarBandBean;
import com.shdnxc.cn.activity.bean.CarBean;
import com.shdnxc.cn.activity.bean.CharacterParser;
import com.shdnxc.cn.activity.bean.PinyinComparator;
import com.shdnxc.cn.activity.bean.SortModel;
import com.shdnxc.cn.activity.view.ClearEditText;
import com.shdnxc.cn.activity.view.SideBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by Zheng Jungen on 2017/3/24.
 */
public class CarActivity extends Activity implements View.OnClickListener {
    private TextView mTitle;
    private ImageView mBack;
    private CarBandBean.DataBean data;
    ArrayList<String> listName = new ArrayList<>();
    ArrayList<String> listID = new ArrayList<>();
    ArrayList<String> listHasChild = new ArrayList<>();
    private HashMap<String, ArrayList<CarBean>> mMap = new HashMap<>();

    public static CarActivity instance = null;
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private SortAdapter adapter;
    private ClearEditText mClearEditText;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<SortModel> SourceDateList = new ArrayList<>();

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    private String token;
    private String deviceAddre;

    private FrameLayout car_show_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = SharedUtils.getString(CarActivity.this, ConstantUils.CU_LOGIN, "0");
        Log.i("PPP", token + " token");
        deviceAddre = SharedUtils.getString(CarActivity.this, ConstantUils.CU_SPLASH, "0");
        Log.i("PPP", deviceAddre + " deviceAddre");
        setContentView(R.layout.activity_car);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        initviews();
        instance = this;
        mTitle.setText("选择车品牌");
        initLenster();
        OkGo.post(WeiYunURL.CAR_BAND)//
                .tag(this)//
                .isMultipart(true)
                .headers("Content-Type", "application/x-www-form-urlencoded")// 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
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
                                        JSONArray jsonArray = data.getJSONArray(next);

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject dataBean = jsonArray.getJSONObject(i);

                                            String id = dataBean.getString("id");

                                            String name = dataBean.getString("name");

                                            String hasChild = dataBean.getString("hasChild");

                                            listName.add(name);
                                            listID.add(id);
                                            listHasChild.add(hasChild);
                                            SortModel sortModel = new SortModel();
                                            sortModel.setName(name);
                                            //汉字转换成拼音
                                            String pinyin = characterParser.getSelling(name);
                                            String sortString = pinyin.substring(0, 1).toUpperCase();

                                            // 正则表达式，判断首字母是否是英文字母
                                            if (sortString.matches("[A-Z]")) {
                                                sortModel.setSortLetters(sortString.toUpperCase());
                                            } else {
                                                sortModel.setSortLetters("#");
                                            }
                                            SourceDateList.add(sortModel);
                                        }
                                    }
                                    car_show_start.setVisibility(View.VISIBLE);
                                    mClearEditText.setVisibility(View.VISIBLE);

                                    // 根据a-z进行排序源数据
                                    Collections.sort(SourceDateList, pinyinComparator);
                                    adapter = new SortAdapter(CarActivity.this, SourceDateList);
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
                String name = ((SortModel) adapter.getItem(position)).getName();
                Log.i("PPP", "car  carBand  " + name);
                String Spotion = listID.get(position);
                Log.i("PPP", "car  postion  " + Spotion);
                SharedUtils.saveString(CarActivity.this, "carName", name);
                OkGo.post(WeiYunURL.MAC_BIND)//
                        .tag(this)//
                        .isMultipart(true)
                        .headers("Content-Type", "application/x-www-form-urlencoded")// 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                        .params("token", token)
                        .params("sn", deviceAddre)
                        .params("brand_id", Spotion)
                   /*     .params("remark", "我的设备")*/
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
                                            /*    String remark = data.getString("remark");
                                                SharedUtils.saveString(CarActivity.this, "mark", remark);*/
                                                String modelsName = data.getString("modelsName");
                                                Log.i("PPP", "car  modelsName           " + modelsName);
                                                Intent intent = new Intent(CarActivity.this, CarSeriesActivity.class);
                                                intent.putExtra("carId", listID.get(position));

                                                Log.i("JKS", listID.get(position)+"   carId");
                                                intent.putExtra("carName", ((SortModel) adapter.getItem(position)).getName());
                                                startActivity(intent);
                                                //   CarActivity.this.finish();


                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });


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

        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }
            }
        });
    }

    private void initLenster() {
        mBack.setOnClickListener(this);
    }

    private void initviews() {
        mTitle = (TextView) findViewById(R.id.tv_title);
        mBack = (ImageView) findViewById(R.id.iv_find_back);

//实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);
        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
        car_show_start = (FrameLayout) findViewById(R.id.car_show_start);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_find_back:
                //  startActivity(new Intent(CarActivity.this, DeviceInfoActivity.class));
                CarActivity.this.finish();
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
        super.onBackPressed();
    }
}
