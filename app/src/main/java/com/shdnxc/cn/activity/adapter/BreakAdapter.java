package com.shdnxc.cn.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shdnxc.cn.activity.R;
import com.shdnxc.cn.activity.bean.BreakCodeBean;

import java.util.List;

/**
 * Created by Zheng Jungen on 2017/3/21.
 */
public class BreakAdapter extends BaseAdapter {

    List<BreakCodeBean.DataBean> data;

    private LayoutInflater layoutInflater;
    private Context context;
    String result = "0";

    public BreakAdapter(Context context,  List<BreakCodeBean.DataBean> data) {
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {

        return data.size();
    }

    @Override
    public Object getItem(int position) {
       /* data.getData().get(position)*/
        return data.get(position);
                /*data.get(position);*/
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder horder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.lv_sigal2,
                    null);
            horder = new ViewHolder();
            horder.re_sigle = (RelativeLayout) convertView.findViewById(R.id.re_sigle);
            horder.tv_code = (TextView) convertView.findViewById(R.id.tv_code);
            horder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(horder);//绑定ViewHolder对象
        } else {
            horder = (ViewHolder) convertView.getTag();
        }

        if (this.data.size()>0){
            horder.tv_code.setText(this.data.get(position).getDtc());
            horder.tv_content.setText(this.data.get(position).getDefinition());
        }else{
            horder.tv_code.setText("错误");
            horder.tv_content.setText("错误");
        }
        return convertView;
    }

    public final class ViewHolder {
        public RelativeLayout re_sigle;
        public TextView tv_code;
        public TextView tv_content;
    }

}