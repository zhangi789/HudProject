package com.shdnxc.cn.activity.animation;

import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by Zheng Jungen on 2017/5/8.
 */
public class CallbackAnimation extends Animation {
    public interface TransformationListener {
        void onApplyTrans(float interpolatedTime);
    }

    private TransformationListener mListener;

    public CallbackAnimation(TransformationListener listener) {
        mListener = listener;
        if (listener == null) {
            mListener = listener;
        }
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        mListener.onApplyTrans(interpolatedTime);
    }

    public void setListener(TransformationListener listener) {
        if (listener == null) {
            return;
        }
        mListener = listener;
    }
}

