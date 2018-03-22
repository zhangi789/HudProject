package com.shdnxc.cn.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shdnxc.cn.activity.R;

import java.util.ArrayList;

/**
 * Created by Zheng Jungen on 2017/4/13.
 */
public class BreakAdapter2 extends BaseAdapter {


    private ArrayList<String> lists = new ArrayList<>();

    private LayoutInflater layoutInflater;
    private Context context;
    String result = "0";

    public BreakAdapter2(Context context, ArrayList<String> lists) {
        this.context = context;
        this.lists = lists;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {

        return lists.size();
    }

    @Override
    public Object getItem(int position) {
       /* data.getData().get(position)*/
        return lists.get(position);
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
            convertView = layoutInflater.inflate(R.layout.lv_sigal3,
                    null);
            horder = new ViewHolder();
            horder.re_sigle = (RelativeLayout) convertView.findViewById(R.id.re_sigle);
            horder.tv_code = (TextView) convertView.findViewById(R.id.tv_code);
            horder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(horder);//绑定ViewHolder对象
        } else {
            horder = (ViewHolder) convertView.getTag();
        }

        horder.tv_code.setText(lists.get(position));
        horder.tv_content.setText("未查询到结果");
      /*  if (this.data.size()>0){
            horder.tv_code.setText(this.data.get(position).getDtc());
            horder.tv_content.setText(this.data.get(position).getDefinition());
        }else{
            horder.tv_code.setText("错误");
            horder.tv_content.setText("错误");
        }*/
        return convertView;
    }

    public final class ViewHolder {
        public RelativeLayout re_sigle;
        public TextView tv_code;
        public TextView tv_content;
    }

}
