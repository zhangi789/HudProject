package com.shdnxc.cn.activity.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Zheng Jungen on 2017/4/7.
 */
public class RatioLayout extends FrameLayout {

    private float mPicRitio = 2.0f; //一个固定的宽高比，后面创建属性自定义来设置宽高比
    public RatioLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RatioLayout(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //父控件是否是固定值或者是match_parent
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY){
            //得到父容器的宽度
            int parentWidth = (int) (MeasureSpec.getSize(widthMeasureSpec)*0.8);
            //得到子控件的宽度
            int childWidth = parentWidth - getPaddingLeft() - getPaddingRight();
            //计算子控件的高度
            int childHeight = (int) (childWidth / mPicRitio );
            //计算父控件的高度
            int parentHeight = childHeight + getPaddingBottom() + getPaddingTop();

            //测量子控件,固定孩子的大小
            int childWidthMeasureSpec = (int) (MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY));
            int childHeightMeasureSpec = (int) (MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY));
            measureChildren(childWidthMeasureSpec,childHeightMeasureSpec);
            //测量
            setMeasuredDimension(parentWidth,parentHeight);

        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }
}
