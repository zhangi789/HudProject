package com.shdnxc.cn.activity.scorellpager.util;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.shdnxc.cn.activity.scorellpager.DiscreteScrollView;


/**
 * Created by 张海洋 on 10.04.2017.
 */
public class ScrollListenerAdapter<T extends RecyclerView.ViewHolder> implements DiscreteScrollView.ScrollStateChangeListener<T> {

    private DiscreteScrollView.ScrollListener<T> adaptee;

    public ScrollListenerAdapter(DiscreteScrollView.ScrollListener<T> adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void onScrollStart(@NonNull T currentItemHolder, int adapterPosition) {

    }

    @Override
    public void onScrollEnd(@NonNull T currentItemHolder, int adapterPosition) {

    }

    @Override
    public void onScroll(float scrollPosition, @NonNull T currentHolder, @NonNull T newCurrentHolder) {
        adaptee.onScroll(scrollPosition, currentHolder, newCurrentHolder);
    }
}
