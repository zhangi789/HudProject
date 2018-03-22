package com.shdnxc.cn.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shdnxc.cn.activity.R;
import com.shdnxc.cn.activity.bean.BaiDuBean;

import java.util.ArrayList;

/**
 * Created by Zheng Jungen on 2017/4/17.
 */
public class BaiduAdapter extends BaseAdapter {
    private ArrayList<BaiDuBean> mBaiduInfo;
    private LayoutInflater layoutInflater;
    private Context context;

    public BaiduAdapter(Context context, ArrayList<BaiDuBean> mBaiduInfo) {
        this.context = context;
        this.mBaiduInfo = mBaiduInfo;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mBaiduInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return mBaiduInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder horder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_baidu_info,
                    null);
            horder = new ViewHolder();
            horder.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(horder);//绑定ViewHolder对象
        } else {
            horder = (ViewHolder) convertView.getTag();
        }
        horder.title.setText(mBaiduInfo.get(position).getAddresName());
        return convertView;
    }
    public final class ViewHolder {
        public TextView title;
    }

}
