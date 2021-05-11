package com.example.zkt.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zkt.R;
import com.example.zkt.bean.ServiceBean;

import java.util.List;

public class ServiceAdapter extends BaseAdapter {

    private List<ServiceBean> mData;
    private LayoutInflater mInflater;
    private int selectionPosition = -1;

    public ServiceAdapter(List<ServiceBean> data, Context mContext) {
        this.mData = data;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_service_listview, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_name.setText(mData.get(position).getName());
        holder.tv_price.setText(mData.get(position).getPrice());
        holder.tv_content.setText(mData.get(position).getContent());

        if (selectionPosition == position)
        {
            holder.relativeLayout.setBackgroundColor(Color.GREEN);

        }
        else
        {
            holder.relativeLayout.setBackgroundColor(Color.WHITE);

        }

        return convertView;
    }

    public void setSelectedPosition(int position)
    {
        this.selectionPosition = position;
    }

    public class ViewHolder {


        public TextView tv_name;
        public TextView tv_price;
        public TextView tv_content;
        private RelativeLayout relativeLayout;

        public ViewHolder(View rootView) {

            this.relativeLayout = rootView.findViewById(R.id.rl_service);
        this.tv_content = (TextView) rootView.findViewById(R.id.service_content_list);
            this.tv_name = (TextView) rootView.findViewById(R.id.service_name_list);
            this.tv_price = (TextView)rootView.findViewById(R.id.service_price_list);
        }

    }


}
