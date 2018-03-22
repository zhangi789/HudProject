package com.shdnxc.cn.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.lzy.okgo.OkGo;
import com.shdnxc.cn.activity.R;
import com.shdnxc.cn.activity.Utils.WeiYunURL;
import com.shdnxc.cn.activity.bean.FindBean;
import com.shdnxc.cn.activity.bean.GlideImageLoader;
import com.shdnxc.cn.activity.okgo.JsonCallbackABC;
import com.shdnxc.cn.activity.ui.NetActivity;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Zheng Jungen on 2017/1/16.
 */
public class DiscoverFragment extends Fragment implements View.OnClickListener, OnBannerListener {
    private Banner mBbanner;
    private ArrayList<String> mImages = new ArrayList<String>();
    private ArrayList<String> mTitles = new ArrayList<>();
    private ArrayList<String> mUrls = new ArrayList<>();

    private ArrayList<String> mShopTile = new ArrayList<>();

    private ArrayList<String> mInformation = new ArrayList<>();

    private RelativeLayout mReShop, mReInformation;

    private boolean isAddDataSuceess;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_find, null);
        initviews(view);
        initLenster();
        initIamgesDate();
        mBbanner.setOnBannerListener(this);
        return view;
    }

    private void initLenster() {
        mReShop.setOnClickListener(this);
        mReInformation.setOnClickListener(this);
    }

    private void initviews(View view) {
        mBbanner = (Banner) view.findViewById(R.id.banner);
        mReShop = (RelativeLayout) view.findViewById(R.id.re_shop);
        mReInformation = (RelativeLayout) view.findViewById(R.id.re_information);

    }

    private void initIamgesDate() {
        OkGo.post(WeiYunURL.MAC_FIND)//
                .tag(this)//
                .isMultipart(true)
                .headers("Content-Type", "application/x-www-form-urlencoded")// 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                .params("app_token", WeiYunURL.NOMAL_TOKEN)  // 这里可以上传参数
                // 可以添加文件上传
                .execute(new JsonCallbackABC<FindBean>() {
                             @Override
                             public void onSuccess(FindBean findBean, Call call, Response response) {
                                 FindBean.DataBean data = findBean.getData();
                                 List<FindBean.DataBean.BannerBean> banner = data.getBanner();

                                 List<FindBean.DataBean.ChannelBean> channel = data.getChannel();
                                 for (int i = 0; i < banner.size(); i++) {
                                     if (i < 2) {
                                         mShopTile.add(channel.get(i).getTitle());
                                         mInformation.add(channel.get(i).getUrl());
                                     }
                                     FindBean.DataBean.BannerBean bannerBean = banner.get(i);
                                     mImages.add(bannerBean.getImageUrl());
                                     mUrls.add(bannerBean.getUrl());
                                     mTitles.add(bannerBean.getTitle());
                                 }
                                 mBbanner.setImages(mImages).setImageLoader(new GlideImageLoader()).start();
                                 isAddDataSuceess = true;

                             }
                         }
                );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.re_information:
                if (isAddDataSuceess) {
                    Intent intent3 = new Intent();
                    intent3.putExtra("mtitle", mShopTile.get(1));
                    intent3.putExtra("weburl", mInformation.get(1));
                    intent3.setClass(getActivity(), NetActivity.class);
                    startActivity(intent3);
                }
                break;

            case R.id.re_shop:
                if (isAddDataSuceess) {
                    Intent intent2 = new Intent();
                    intent2.putExtra("mtitle", mShopTile.get(0));
                    intent2.putExtra("weburl", mInformation.get(0));
                    intent2.setClass(getActivity(), NetActivity.class);
                    startActivity(intent2);
                }
                break;
        }
    }

    @Override
    public void OnBannerClick(int position) {
        Intent intent1 = new Intent();
        intent1.putExtra("mtitle", mTitles.get(position));
        intent1.putExtra("weburl", mUrls.get(position));
        intent1.setClass(getActivity(), NetActivity.class);
        startActivity(intent1);
    }
}

