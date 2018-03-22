package com.shdnxc.cn.activity.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;

/**
 * Created by Android on 2017/5/12.
 */

public class ActivityAnimation {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void explodeToSlide(Activity activity ,Class<? extends Activity> targetActivity){
//        Explode explode = new Explode();
//        explode.setDuration(200);
//
//        Slide slide = new Slide();
//        slide.setDuration(50);
//
//        Fade fade = new Fade();
//        fade.setDuration(50);
//
////        activity.getWindow().setExitTransition(explode);
//        activity.getWindow().setEnterTransition(slide);
//        activity.getWindow().setReturnTransition(fade);
////        activity.getWindow().setReenterTransition(fade);
//        ActivityOptionsCompat oc = ActivityOptionsCompat.makeSceneTransitionAnimation(activity);
        Intent i = new Intent(activity,targetActivity);
//        activity.startActivity(i, oc.toBundle());
        activity.startActivity(i);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void SliderToFade(Activity activity , Class<? extends Activity> targetActivity){
//        Slide slide = new Slide();
//        slide.setDuration(50);
//
//        Fade fade = new Fade();
//        fade.setDuration(50);
//
//        Explode explode = new Explode();
//        explode.setDuration(200);
//
////        activity.getWindow().setExitTransition(slide);
//        activity.getWindow().setEnterTransition(explode);
//        activity.getWindow().setReturnTransition(fade);
////        activity.getWindow().setReenterTransition(slide);
//        ActivityOptionsCompat oc = ActivityOptionsCompat.makeSceneTransitionAnimation(activity);
        Intent i = new Intent(activity,targetActivity);
//        activity.startActivity(i, oc.toBundle());
        activity.startActivity(i);
    }
}
