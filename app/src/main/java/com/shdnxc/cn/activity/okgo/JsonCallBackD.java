package com.shdnxc.cn.activity.okgo;

import com.lzy.okgo.callback.AbsCallback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * ***********************************************************
 * author: Dove
 * time: 2017/3/4 23:38
 * name:
 * overview:
 * usage:
 * *************************************************************
 */
public class JsonCallBackD <T> extends AbsCallback<T> {
    @Override
    public void onSuccess(T t, Call call, Response response) {
    }

    @Override
    public T convertSuccess(Response response) throws Exception {
        return null;
    }
}
