package com.shdnxc.cn.activity.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shdnxc.cn.activity.R;

/**
 * Created by Zheng Jungen on 2017/3/23.
 */
public class NetActivity extends Activity implements View.OnClickListener {

    private ImageView mBack;
    private TextView mTitle;
    private ProgressBar mPbNet;
    private WebView mWebView;
    String mtitle = "";
    String webUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net);


        Intent intent = getIntent();
        mtitle = intent.getStringExtra("mtitle");
        webUrl = intent.getStringExtra("weburl");
        initviews();
        iniLienter();
        initData();

    }
    private void initData() {
        mTitle.setText(mtitle);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true); //支持javascript代码
        mWebView.loadUrl(webUrl);
        mWebView.setWebViewClient(new WebViewClient() {
            //如果想在自己的app中，通过webview打开网页的话，需要覆写这个方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
//                Log.e("tag", "onPageStarted");
//                mPbNet.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                Log.e("tag", "onPageFinished");
//                Javascript
//                mPbNet.setVisibility(View.GONE);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress > 0) {
                    mPbNet.setVisibility(View.VISIBLE);
                }

                if (newProgress == 100) {
                    mPbNet.setVisibility(View.GONE);
                }
                Log.i("newProgress", "progress:" + newProgress);
                mPbNet.setProgress(newProgress);
            }
        });

    }

    private void iniLienter() {
        mBack.setOnClickListener(this);
    }

    private void initviews() {
        mBack = (ImageView) findViewById(R.id.iv_find_back);
        mTitle = (TextView) findViewById(R.id.tv_title);
        mPbNet = (ProgressBar) findViewById(R.id.pb_net);
        mWebView = (WebView) findViewById(R.id.web_view);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_find_back:
                NetActivity.this.finish();
                break;
        }
    }
}
