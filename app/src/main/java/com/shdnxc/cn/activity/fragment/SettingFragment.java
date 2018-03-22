package com.shdnxc.cn.activity.fragment;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.shdnxc.cn.activity.MainActivity;
import com.shdnxc.cn.activity.R;
import com.shdnxc.cn.activity.Utils.BaseUtils;
import com.shdnxc.cn.activity.Utils.CRC64;
import com.shdnxc.cn.activity.Utils.ConstantUils;
import com.shdnxc.cn.activity.Utils.SendDefPool;
import com.shdnxc.cn.activity.Utils.SendUtils;
import com.shdnxc.cn.activity.Utils.SharedUtils;
import com.shdnxc.cn.activity.app.IAPI;
import com.shdnxc.cn.activity.base.BaseFragment;
import com.shdnxc.cn.activity.bean.LightBean;
import com.shdnxc.cn.activity.scorellpager.DiscreteScrollView;
import com.shdnxc.cn.activity.scorellpager.ImageModel;
import com.shdnxc.cn.activity.view.Image3DSwitchView;
import com.shdnxc.cn.activity.view.SwitchButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * Created by Zheng Jungen on 2017/1/16.
 */
public class SettingFragment extends BaseFragment {
    private String macAddress;
    SwitchButton nav_switch, light_switch;
    DiscreteScrollView scrollView;
    List<ImageModel> tempDatas;
    LightBean lightBeandata;
    private Image3DSwitchView imageSwitchView;
    private ImageView iv_show_true;
    RadioButton rb_engry, rb_balanle, rb_auto, rb_super;
    Timer timer;
    /**
     *
     */
    int[] images = new int[]{R.mipmap.theme_two, R.mipmap.theme_one};
    private static int TOTAL_COUNT = 2;
    private RelativeLayout viewPagerContainer;
    private ViewPager viewPager;
    String demoss = "";
    RadioGroup radioGroup;
    private SeekBar mSeerBar;
    //点击主题设置控制show
    private RelativeLayout mTpisShow;
    private TextView mTvShowTips;

    private RelativeLayout re_tips_show2;


    private Handler mhander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String nomal = SendDefPool.stu.getmBootHudInfo();
            String hud_to_app = (String) msg.obj;
            String hexTo8Byte = CRC64.getHexTo8Byte(hud_to_app);
            String data = hexTo8Byte.substring(3, 6);
            String data1 = Integer.valueOf(data, 2).toString();
            SharedUtils.saveString(getActivity(), "selectTheme", data1);
            Log.i("TARS", data1);
            if (hud_to_app.equals("7")) {


            } else if (data1.equals("0")) {
                rb_engry.setChecked(true);
                SharedUtils.saveString(getActivity(), ConstantUils.GAO_BAIDU_LIGHT, "0");
                Log.i("ASP", "  revever 0->");

            } else if (data1.equals("1")) {
                rb_balanle.setChecked(true);
                SharedUtils.saveString(getActivity(), ConstantUils.GAO_BAIDU_LIGHT, "1");
                Log.i("ASP", "  revever 1->");

            } else if (data1.equals("2")) {
                rb_auto.setChecked(true);
                SharedUtils.saveString(getActivity(), ConstantUils.GAO_BAIDU_LIGHT, "2");
                Log.i("ASP", "  revever 2->");
            } else if (data1.equals("3")) {
                rb_super.setChecked(true);
                SharedUtils.saveString(getActivity(), ConstantUils.GAO_BAIDU_LIGHT, "3");
                Log.i("ASP", "  revever 3->");
            }
        }
    };
    //将获得的数据接收  在updateReceivedData更新数据
    private final BroadcastReceiver bleIntentReceiver = new BroadcastReceiver() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (ConstantUils.BLE_HUD_TO_APP.equals(action)) {
                String stringExtra = intent.getStringExtra(ConstantUils.BLE_HUD_TO_APP_KEY);
                String[] defInformation = BaseUtils.getArrayFromHudToApp(BaseUtils.removeSpace(stringExtra));
                //获得数据段信息   数据段信息处理
                String[] dataSegment = BaseUtils.getDataSegment(defInformation[4]);
                String hud_to_app = dataSegment[1];
                Message msg = Message.obtain();
                msg.obj = hud_to_app;
                mhander.sendMessage(msg);
                String voiceSett = dataSegment[12];
                String replace = voiceSett.replace("0", "");
                if (!voiceSett.equals("00")) {
                    Log.i("RRR", "hud_to_app " + replace);
                    int progress2 = Integer.parseInt(replace);
                    Log.i("RRR", "hud_to_app--> " + progress2);
                    if (progress2 < 9) {
                        mSeerBar.setProgress(progress2 - 1);
                        String mStoreProgress = String.valueOf(progress2);
                        SharedUtils.saveString(getActivity(), ConstantUils.GAO_BAIDU_VOICE, mStoreProgress);
                        Log.i("RRR", "hud_to_app----> " + progress2 + " " + mStoreProgress);
                    }
                }
            }
        }
    };
    private String mStoreLight;
    private String mStoreVoice;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View loadXml(LayoutInflater inflater) {
        return inflater.inflate(R.layout.frag_sett, null);
    }

    @Override
    protected void initView(View view) {
        getActivity().registerReceiver(bleIntentReceiver, bleIntentFilter());
        mTpisShow = (RelativeLayout) view.findViewById(R.id.re_tips_show);
        lightBeandata = new LightBean();
        re_tips_show2 = (RelativeLayout) view.findViewById(R.id.re_tips_show2);
        mTvShowTips = (TextView) view.findViewById(R.id.tv_tips_sure);
        mSeerBar = (SeekBar) view.findViewById(R.id.mSeerBar);
        rb_engry = (RadioButton) view.findViewById(R.id.rb_engry);
        rb_balanle = (RadioButton) view.findViewById(R.id.rb_balanle);
        rb_auto = (RadioButton) view.findViewById(R.id.rb_auto);
        rb_super = (RadioButton) view.findViewById(R.id.rb_super);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        nav_switch = (SwitchButton) view.findViewById(R.id.nav_switch);
        light_switch = (SwitchButton) view.findViewById(R.id.light_switch);
        scrollView = (DiscreteScrollView) view.findViewById(R.id.discreteScrollView);
    }

    @Override
    protected void initData() {
        timer = new Timer();
        tempDatas = new ArrayList<>();
        ImageModel item0 = new ImageModel(R.mipmap.theme_two, true);
        tempDatas.add(item0);
        ImageModel item1 = new ImageModel(R.mipmap.theme_one, false);
        tempDatas.add(item1);
        /**
         * 设备主题跟随数据联动
         */
        String nomalTheme = SharedUtils.getString(getActivity(), "localTheme", "0");
        if (nomalTheme.equals("0")) {
            tempDatas.get(0).setChecked(true);
            tempDatas.get(1).setChecked(false);
        } else {
            tempDatas.get(1).setChecked(true);
            tempDatas.get(0).setChecked(false);
        }
        /**
         * 同步亮度模式
         *
         */
        mStoreLight = SharedUtils.getString(getActivity(), ConstantUils.GAO_BAIDU_LIGHT, "7");
        if (mStoreLight.equals("0")) {
            rb_engry.setChecked(true);
        } else if (mStoreLight.equals("1")) {
            rb_balanle.setChecked(true);
        } else if (mStoreLight.equals("2")) {
            rb_auto.setChecked(true);
        } else if (mStoreLight.equals("3")) {
            rb_super.setChecked(true);
        }
        mStoreVoice = SharedUtils.getString(getActivity(), ConstantUils.GAO_BAIDU_VOICE, "10");

        if (!mStoreVoice.equals("10")) {
            Log.i("RRR", "hud_to_app-----存储的数据-------> " + mStoreVoice);
            int i = Integer.parseInt(mStoreVoice);
            mSeerBar.setProgress(i - 1);


        }

       /* int progress = mSeerBar.getProgress();
        Log.i("RRR", "hud_to_app-----存储的数据2-------> " + progress);*/
        macAddress = BaseUtils.getMacStr(SharedUtils.getString(getActivity(), ConstantUils.CU_SPLASH, "0"))[2];
        mSeerBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress + 1;
                Log.i("RRR", "hud_to_app-----setOnSeekBarChangeListener-------> " + progress);
                String hexData = SendUtils.hexAdd0Oprate(Integer.toHexString(progress));
                String mStoreProgress = String.valueOf(progress);
                Log.i("RRR", "hud_to_app-----setOnSeekBarChangeListener-------> " + progress + " " + mStoreProgress);
                SharedUtils.saveString(getActivity(), ConstantUils.GAO_BAIDU_VOICE, mStoreProgress);
                if (IAPI.WHICH_MAP.equals("1")) {
                    SendDefPool.stu.setmVoiceSett(hexData);
                    MainActivity.mt.setSta(CRC64.getAll2(SendDefPool.stu.toString()));
                } else {
                    SendDefPool.stu2.setmVoiceSett(hexData);
                    MainActivity.mt.setSta(CRC64.getAll2(SendDefPool.stu2.toString()));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //亮度设置
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            /* String nomal = SharedUtils.getString(getActivity(), "sed", "0E");
 */
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_engry:
                        //百度
                        if (IAPI.WHICH_MAP.equals("1")) {
                            String nomal1 = SendDefPool.stu.getmBootHudInfo();
                            Log.i("nnn", nomal1 + "  初始化话rb_engry  " + "-------------------------------------------------------------------------- ");
                            String engry = SendUtils.setHudLight(0, nomal1);
                            SendDefPool.stu.setChanged(false);
                            SendDefPool.stu.setmBootHudInfo(engry);
                            Log.i("nnn", engry + "           只是进入一次rb_engry-----00--------------------------------------------------------");
                            MainActivity.mt.setSta(CRC64.getAll2(SendDefPool.stu.toString()));
                            SharedUtils.saveString(getActivity(), ConstantUils.GAO_BAIDU_LIGHT, "0");
                            Log.i("ASP", engry + " 0->");

                        } else {
                            //高德
                            String nomal1 = SendDefPool.stu2.getmBootHudInfo();
                            Log.i("nnn", nomal1 + "  初始化话rb_engry  " + "-------------------------------------------------------------------------- ");
                            String engry = SendUtils.setHudLight(0, nomal1);
                            SendDefPool.stu2.setChanged(false);
                            SendDefPool.stu2.setmBootHudInfo(engry);
                            Log.i("nnn", engry + "           只是进入一次rb_engry-----00--------------------------------------------------------");
                            MainActivity.mt.setSta(CRC64.getAll2(SendDefPool.stu2.toString()));
                            SharedUtils.saveString(getActivity(), ConstantUils.GAO_BAIDU_LIGHT, "0");
                            Log.i("ASP", engry + " 0->");

                        }
                        break;
                    case R.id.rb_balanle:
                        if (IAPI.WHICH_MAP.equals("1")) {
                            String nomal2 = SendDefPool.stu.getmBootHudInfo();
                            Log.i("nnn", nomal2 + "  初始化话rb_balanle  " + "-------------------------------------------------------------------------- ");
                            String balance = SendUtils.setHudLight(1, nomal2);
                            SendDefPool.stu.setChanged(false);
                            SendDefPool.stu.setmBootHudInfo(balance);
                            Log.i("nnn", balance + "           只是进入一次rb_balanle-----02--------------------------------------------------------");
                            MainActivity.mt.setSta(CRC64.getAll2(SendDefPool.stu.toString()));
                            SharedUtils.saveString(getActivity(), ConstantUils.GAO_BAIDU_LIGHT, "1");
                            Log.i("ASP", balance + " 1->");
                        } else {
                            String nomal2 = SendDefPool.stu2.getmBootHudInfo();
                            Log.i("nnn", nomal2 + "  初始化话rb_balanle  " + "-------------------------------------------------------------------------- ");
                            String balance = SendUtils.setHudLight(1, nomal2);
                            SendDefPool.stu2.setChanged(false);
                            SendDefPool.stu2.setmBootHudInfo(balance);
                            Log.i("nnn", balance + "           只是进入一次rb_balanle-----02--------------------------------------------------------");
                            MainActivity.mt.setSta(CRC64.getAll2(SendDefPool.stu2.toString()));
                            SharedUtils.saveString(getActivity(), ConstantUils.GAO_BAIDU_LIGHT, "1");
                            Log.i("ASP", balance + " 1->");
                        }
                        break;
                    case R.id.rb_super:
                        if (IAPI.WHICH_MAP.equals("1")) {
                            String nomal3 = SendDefPool.stu.getmBootHudInfo();
                            Log.i("nnn", nomal3 + "  初始化话rb_super " + "-------------------------------------------------------------------------- ");
                            String super2 = SendUtils.setHudLight(3, nomal3);
                            SendDefPool.stu.setChanged(false);
                            SendDefPool.stu.setmBootHudInfo(super2);
                            Log.i("nnn", super2 + "            只是进入一次rb_super---06--------------------------------------------------------");
                            MainActivity.mt.setSta(CRC64.getAll2(SendDefPool.stu.toString()));
                            SharedUtils.saveString(getActivity(), ConstantUils.GAO_BAIDU_LIGHT, "3");
                            Log.i("ASP", super2 + " 3->");
                        } else {
                            String nomal3 = SendDefPool.stu2.getmBootHudInfo();
                            Log.i("nnn", nomal3 + "  初始化话rb_super " + "-------------------------------------------------------------------------- ");
                            String super2 = SendUtils.setHudLight(3, nomal3);
                            SendDefPool.stu2.setChanged(false);
                            SendDefPool.stu2.setmBootHudInfo(super2);
                            Log.i("nnn", super2 + "            只是进入一次rb_super---06--------------------------------------------------------");
                            MainActivity.mt.setSta(CRC64.getAll2(SendDefPool.stu2.toString()));
                            SharedUtils.saveString(getActivity(), ConstantUils.GAO_BAIDU_LIGHT, "3");
                            Log.i("ASP", super2 + " 3->");
                        }
                        break;
                    case R.id.rb_auto:
                        if (IAPI.WHICH_MAP.equals("1")) {
                            String nomal4 = SendDefPool.stu.getmBootHudInfo();
                            Log.i("nnn", nomal4 + "  初始化话rb_auto " + "-------------------------------------------------------------------------- ");
                            String auto = SendUtils.setHudLight(2, nomal4);
                            SendDefPool.stu.setChanged(false);
                            SendDefPool.stu.setmBootHudInfo(auto);
                            Log.i("nnn", auto + "           只是进入一次rb_auto----04--------------------------------------------------------");
                            MainActivity.mt.setSta(CRC64.getAll2(SendDefPool.stu.toString()));
                            SharedUtils.saveString(getActivity(), ConstantUils.GAO_BAIDU_LIGHT, "2");
                            Log.i("ASP", auto + " 2->");
                        } else {
                            String nomal4 = SendDefPool.stu2.getmBootHudInfo();
                            Log.i("nnn", nomal4 + "  初始化话rb_auto " + "-------------------------------------------------------------------------- ");
                            String auto = SendUtils.setHudLight(2, nomal4);
                            SendDefPool.stu2.setChanged(false);
                            SendDefPool.stu2.setmBootHudInfo(auto);
                            Log.i("nnn", auto + "           只是进入一次rb_auto----04--------------------------------------------------------");
                            MainActivity.mt.setSta(CRC64.getAll2(SendDefPool.stu2.toString()));
                            SharedUtils.saveString(getActivity(), ConstantUils.GAO_BAIDU_LIGHT, "2");
                            Log.i("ASP", auto + " 2->");
                        }
                        break;
                }

            }
        });
        //  对导航语言开关机设置
        nav_switch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked == true) {
                    mSeerBar.setEnabled(true);
                    if (IAPI.WHICH_MAP.equals("1")) {
                        String nomal5 = SendDefPool.stu.getmBootHudInfo();
                        Log.i("nnn", nomal5 + "     只是进入一次isChecked == true-----------------------------------------------------");
                        String navopen = SendUtils.setHudNavVoice(0, nomal5);
                        SendDefPool.stu.setmBootHudInfo(navopen);
                        Log.i("nnn", navopen + "     只是进入一次isChecked == true---+navopen+-----------------------------------------------------");
                        MainActivity.mt.setSta(CRC64.getAll2(SendDefPool.stu.toString()));
                    } else {
                        String nomal5 = SendDefPool.stu2.getmBootHudInfo();
                        Log.i("nnn", nomal5 + "     只是进入一次isChecked == true-----------------------------------------------------");
                        String navopen = SendUtils.setHudNavVoice(0, nomal5);
                        SendDefPool.stu2.setmBootHudInfo(navopen);
                        Log.i("nnn", navopen + "     只是进入一次isChecked == true---+navopen+-----------------------------------------------------");
                        MainActivity.mt.setSta(CRC64.getAll2(SendDefPool.stu2.toString()));
                    }
                } else if (isChecked == false) {
                    mSeerBar.setEnabled(false);
                    if (IAPI.WHICH_MAP.equals("1")) {
                        String nomal6 = SendDefPool.stu.getmBootHudInfo();
                        Log.i("nnn", nomal6 + "     只是进入一次isChecked == false-----------------------------------------------------");
                        String navClose = SendUtils.setHudNavVoice(1, nomal6);
                        SendDefPool.stu.setmBootHudInfo(navClose);
                        Log.i("nnn", navClose + "     只是进入一次isChecked == false---+navClose+-----------------------------------------------------");
                        MainActivity.mt.setSta(CRC64.getAll2(SendDefPool.stu.toString()));
                    } else {
                        String nomal6 = SendDefPool.stu2.getmBootHudInfo();
                        Log.i("nnn", nomal6 + "     只是进入一次isChecked == false-----------------------------------------------------");
                        String navClose = SendUtils.setHudNavVoice(1, nomal6);
                        SendDefPool.stu2.setmBootHudInfo(navClose);
                        Log.i("nnn", navClose + "     只是进入一次isChecked == false---+navClose+-----------------------------------------------------");
                        MainActivity.mt.setSta(CRC64.getAll2(SendDefPool.stu2.toString()));
                    }
                }
            }
        });
        //对开关机控制
        light_switch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            String mBaiDuNomal = SendDefPool.stu.getmBootHudInfo();
            String mGaoDeNomal = SendDefPool.stu2.getmBootHudInfo();

            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked == true) {
                    if (IAPI.WHICH_MAP.equals("1")) {
                        String mHudopen = SendUtils.setHudOpenClose(0, mBaiDuNomal);
                        SendDefPool.stu.setmBootHudInfo(mHudopen);
                        MainActivity.mt.setSta(CRC64.getAll2(SendDefPool.stu.toString()));
                    } else {
                        String mHudopen = SendUtils.setHudOpenClose(0, mGaoDeNomal);
                        SendDefPool.stu2.setmBootHudInfo(mHudopen);
                        MainActivity.mt.setSta(CRC64.getAll2(SendDefPool.stu2.toString()));
                    }

                } else if (isChecked == false) {
                    if (IAPI.WHICH_MAP.equals("1")) {
                        String mHudClose = SendUtils.setHudOpenClose(1, mBaiDuNomal);
                        SendDefPool.stu.setmBootHudInfo(mHudClose);
                        MainActivity.mt.setSta(CRC64.getAll2(SendDefPool.stu.toString()));
                    } else {
                        String mHudClose = SendUtils.setHudOpenClose(1, mGaoDeNomal);
                        SendDefPool.stu2.setmBootHudInfo(mHudClose);
                        MainActivity.mt.setSta(CRC64.getAll2(SendDefPool.stu2.toString()));
                    }

                }
            }
        });
        //对主题的控制
        scrollView.init(tempDatas, new DiscreteScrollView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (IAPI.WHICH_MAP.equals("1")) {
                    String nomal = SendDefPool.stu.getmThemeSett();
                    String currentTheme = String.valueOf(index);
                    SendDefPool.stu.setmThemeSett(SendUtils.setTheme(index, nomal));
                    MainActivity.mt.setSta(CRC64.getAll2(SendDefPool.stu.toString()));
                    mTpisShow.setVisibility(View.VISIBLE);
                    re_tips_show2.setVisibility(View.VISIBLE);
                    SharedUtils.saveString(getActivity(), "localTheme", currentTheme);
                } else {
                    String nomal = SendDefPool.stu2.getmThemeSett();
                    String currentTheme = String.valueOf(index);
                    SendDefPool.stu2.setmThemeSett(SendUtils.setTheme(index, nomal));
                    MainActivity.mt.setSta(CRC64.getAll2(SendDefPool.stu2.toString()));
                    mTpisShow.setVisibility(View.VISIBLE);
                    re_tips_show2.setVisibility(View.VISIBLE);
                    SharedUtils.saveString(getActivity(), "localTheme", currentTheme);
                }
            }
        });
        mTvShowTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTpisShow.setVisibility(View.INVISIBLE);
                re_tips_show2.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //对蓝牙服务的支持
    private static IntentFilter bleIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantUils.BLE_CONN_FALSE);
        intentFilter.addAction(ConstantUils.BLE_CONN_SUCCESS);
        intentFilter.addAction(ConstantUils.BLE_CONN_BREAK);
        intentFilter.addAction(ConstantUils.BLE_HUD_TO_APP);
        return intentFilter;
    }
}
