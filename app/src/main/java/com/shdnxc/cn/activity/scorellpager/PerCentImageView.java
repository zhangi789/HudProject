package com.shdnxc.cn.activity.scorellpager;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by 张海洋 on 10.04.2017.
 */

public class PerCentImageView extends ImageView {


    public PerCentImageView(Context context) {
        super(context);
    }

    public PerCentImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PerCentImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        int width = getContext().getResources().getDisplayMetrics().widthPixels;
        int percentWidth = (int) (width * 0.75);
        setMeasuredDimension(percentWidth, (int) (percentWidth / 2));
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
//        params.width = width;
//        params.height = (int) (width / scale);
//        setLayoutParams(params);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PerCentImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
