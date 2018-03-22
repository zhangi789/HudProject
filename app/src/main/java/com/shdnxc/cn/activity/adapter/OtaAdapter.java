package com.shdnxc.cn.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shdnxc.cn.activity.R;

import java.util.ArrayList;

/**
 * Created by Zheng Jungen on 2017/3/30.
 */
public class OtaAdapter extends BaseAdapter {


    private ArrayList<String> data;

    private LayoutInflater layoutInflater;
    private Context context;

    public OtaAdapter(Context context, ArrayList<String> data) {
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
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder horder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.ota_item3,
                    null);
            horder = new ViewHolder();
            horder.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(horder);//绑定ViewHolder对象
        } else {
            horder = (ViewHolder) convertView.getTag();
        }
        horder.title.setText(data.get(position));
        return convertView;
    }

    public final class ViewHolder {
        public TextView title;
    }


}
