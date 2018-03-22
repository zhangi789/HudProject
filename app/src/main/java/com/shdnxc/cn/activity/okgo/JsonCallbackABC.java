package com.shdnxc.cn.activity.okgo;

import com.google.gson.stream.JsonReader;
import com.lzy.okgo.callback.AbsCallback;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Response;

/**
 * ***********************************************************
 * author: andy
 * time: 2017/3/4 23:07
 * name:
 * overview:
 * usage:
 *
 * OkGo.get(Urls.URL_METHOD)//
 .tag(this)//
 .execute(new JsonCallback<Login>(this) {   //这里传递Login
 @Override
 public void onSuccess(Login login, Call call, Response response) {

 }
 });
 * *************************************************************
 */
public class JsonCallbackABC<T> extends AbsCallback<T> {
    @Override
    public void onSuccess(T t, Call call, Response response) {
    }

    @Override
    public T convertSuccess(Response response) throws Exception {
        //以下代码是通过泛型解析实际参数,泛型必须传

        //com.lzy.demo.callback.DialogCallback<com.lzy.demo.model.Login> 得到类的泛型，包括了泛型参数
        Type genType = getClass().getGenericSuperclass();
        //从上述的类中取出真实的泛型参数，有些类可能有多个泛型，所以是数值
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        //我们的示例代码中，只有一个泛型，所以取出第一个，得到如下结果
        //com.lzy.demo.model.Login
        Type type = params[0];

        //这里我们既然都已经拿到了泛型的真实类型，即对应的 class ，那么当然可以开始解析数据了，我们采用 Gson 解析
        //以下代码是根据泛型解析数据，返回对象，返回的对象自动以参数的形式传递到 onSuccess 中，可以直接使用
        JsonReader jsonReader = new JsonReader(response.body().charStream());
        //有数据类型，表示有data
        T data = Convert.fromJson(jsonReader, type);
        response.close();
        return data;
    }
}
