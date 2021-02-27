package com.example.zkt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zkt.R;
import com.example.zkt.base.BaseApplication;
import com.example.zkt.bean.Land;
import com.example.zkt.util.GlideUtils;

import java.util.List;

import cn.leancloud.AVObject;


/**
 * 首页热评土地Adapter
 */
public class HotGridViewAdapter extends BaseAdapter {

    private Context context;
    private List<Land> listItemHot;

    public HotGridViewAdapter(Context context, List<Land> listItemHot) {
        this.context = context;
        this.listItemHot = listItemHot;
        //实例化ImageLoader

    }

    @Override
    public int getCount() {

        if (listItemHot.size() > 2) {
            return 2;
        } else {
            return listItemHot.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return listItemHot.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LandViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_land_for_homefragment,null);
            //布局文件中所有组件的对象封装到ViewHolder对象中
            holder = new LandViewHolder();
            holder.landName = convertView.findViewById(R.id.home_item_landname);
            holder.landPlant = convertView.findViewById(R.id.home_item_landplant);
            holder.landPrice = convertView.findViewById(R.id.home_item_landprice);
            holder.landimg = convertView.findViewById(R.id.home_item_landimg);
            //把ViewHolder对象封装到View对象中
            convertView.setTag(holder);
        } else {
            holder = (LandViewHolder) convertView.getTag();

        }


        //获取点击的子菜单的View
        AVObject land = listItemHot.get(position);
        String name = land.getString("lname");
        String plant = land.getString("plant");
        Integer price = land.getInt("lprice");

        holder.landName.setText(name);
        holder.landPlant.setText(plant);
        holder.landPrice.setText("￥ " + price + "元起");

        GlideUtils.load(context,land.getAVFile("lpicture").getUrl(),holder.landimg);




        return convertView;

    }

    private class LandViewHolder {

        public TextView landName, landPlant, landPrice;
        public ImageView landimg;
    }
}
