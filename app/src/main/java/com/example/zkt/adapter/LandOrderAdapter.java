package com.example.zkt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zkt.R;
import com.example.zkt.util.GlideUtils;

import java.util.List;

import cn.leancloud.AVObject;

public class LandOrderAdapter extends RecyclerView.Adapter<LandOrderAdapter.MyLandOrderHolder>{
    private Context mContext;
    private List<AVObject> mList;


    public LandOrderAdapter(Context context, List<AVObject> list) {
        mContext = context;
        mList = list;

    }


    @NonNull
    @Override
    public MyLandOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_order_land, parent, false);
        return new LandOrderAdapter.MyLandOrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyLandOrderHolder holder, int position) {

        holder.setData((AVObject) mList.get(position));

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    class MyLandOrderHolder extends RecyclerView.ViewHolder {

        private TextView landName;//土地名称
        private TextView time;//租赁时间
        private TextView area;//土地面积
        private TextView service;

        public MyLandOrderHolder(View itemView) {
            super(itemView);
            landName = (TextView) itemView.findViewById(R.id.tv_order_land_name);
            time = (TextView) itemView.findViewById(R.id.tv_land_time);
            area = (TextView) itemView.findViewById(R.id.tv_land_area);
            service = (TextView) itemView.findViewById(R.id.tv_land_service);

        }


        public void setData(AVObject item) {

            landName.setText(item.getString("landName"));
            time.setText("租赁周期"+item.getString("time"));
            area.setText("土地面积："+item.getInt("area")+"km²");
            service.setText("服务类型："+item.getString("service"));
            GlideUtils.load(mContext,item.getString("photo"),itemView.findViewById(R.id.iv_order_land));


        }


    }
}