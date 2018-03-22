package com.shdnxc.cn.activity.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shdnxc.cn.activity.R;

/**
 * Created by Zheng Jungen on 2017/3/22.
 */
public class BreakInfoActivity extends Activity implements View.OnClickListener {
    private TextView tv_mean, tv_dtc, tv_def;
    String codeDef, codeDet, codeMean;
    private ImageView toorivback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        codeDet = intent.getStringExtra("codedet");
        codeDef = intent.getStringExtra("codedef");
        codeMean = intent.getStringExtra("codemean");
        setContentView(R.layout.activity_break_info);
        initviews();
        tv_dtc.setText(codeDet);
        tv_def.setText(codeDef);
        tv_mean.setText(codeMean);
        initLisenter();
    }

    private void initLisenter() {
        toorivback.setOnClickListener(this);
    }

    private void initviews() {
        toorivback = (ImageView) findViewById(R.id.toorivback);
        tv_mean = (TextView) findViewById(R.id.tv_mean);
        tv_dtc = (TextView) findViewById(R.id.tv_dtc);
        tv_def = (TextView) findViewById(R.id.tv_def);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toorivback:
                BreakInfoActivity.this.finish();
                break;
        }
    }
}
