package com.shdnxc.cn.activity.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Checkable;
import android.widget.ImageView;

import com.shdnxc.cn.activity.R;

/**
 * Created by Zheng Jungen on 2017/3/21.
 */
public class MediaImageView extends ImageView implements Checkable {
    private static final String TAG = "MediaImageView";
    private boolean isChecked;


    public MediaImageView(Context  context){
        super(context);

    }
    public MediaImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setChecked(boolean check){
        isChecked = check;
        refreshDrawableState();
    }

    public boolean isChecked(){
        return isChecked;
    }

    public void toggle() {
        setChecked(!isChecked);
    }

    @Override
    public boolean performClick() {
        Log.d(TAG,"performClick()");
        toggle();
        return super.performClick();
    }
// int resId = isChecked() ? R.drawable.ic_detection_communication_normal : R.drawable.ic_detection_communication_press;
    public void refreshDrawableState(){
       // int resId = isChecked() ? R.drawable.ic_detection_communication_normal : R.drawable.ic_detection_communication_press;
        int resId = isChecked() ? R.drawable.ic_detection_power_normal : R.drawable.ic_detection_power_press;
        this.setImageResource(resId);
    }
}
