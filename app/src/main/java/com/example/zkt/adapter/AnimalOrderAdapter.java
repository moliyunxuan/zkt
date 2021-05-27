package com.example.zkt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zkt.R;
import com.example.zkt.util.GlideUtils;

import java.util.List;

import cn.leancloud.AVObject;

public class AnimalOrderAdapter extends RecyclerView.Adapter<AnimalOrderAdapter.MyAnimalOrderHolder>  {
    private Context mContext;
    private List<AVObject> mList;
    private String mtitle;

    public AnimalOrderAdapter(Context context, List<AVObject> list,String title) {
        mContext = context;
        mList = list;
        mtitle = title;
    }


    @NonNull
    @Override
    public MyAnimalOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_order_animal, parent, false);
        return new MyAnimalOrderHolder(view);
    }

    @Override
    public void onBindViewHolder(MyAnimalOrderHolder holder, int position) {
        holder.setData((AVObject) mList.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }



    class MyAnimalOrderHolder extends RecyclerView.ViewHolder {

        private TextView shopName;
        private TextView orderTime;
        private TextView animalName;
        private TextView growthCycle;
        private TextView harvest;
        private TextView orderId;
        private TextView orderPrice;
        private ImageView animalPhoto;





        public MyAnimalOrderHolder(View itemView) {
            super(itemView);
            shopName = (TextView) itemView.findViewById(R.id.animal_order_shop_name);
            orderTime = (TextView) itemView.findViewById(R.id.animal_order_time);
            animalName = (TextView) itemView.findViewById(R.id.tv_order_animal_name);
            growthCycle = (TextView) itemView.findViewById(R.id.tv_order_animal_cycle);
            harvest = (TextView) itemView.findViewById(R.id. tv_order_animal_harvest);
            orderId = (TextView) itemView.findViewById(R.id.tv_order_animal_id);
            orderPrice = (TextView) itemView.findViewById(R.id.tv_order_animal_price);
            animalPhoto = itemView.findViewById(R.id.iv_order_animal);
        }


        public void setData(AVObject item ) {

            shopName.setText(item.getString("shopName"));
            orderTime.setText(item.getCreatedAtString().substring(0,10));
            animalName.setText(item.getString("name"));
            growthCycle.setText("生长周期："+item.getString("growCycle"));
            harvest.setText("收获："+item.getString("harvest"));
            orderId.setText(item.getObjectId());
            orderPrice.setText("总价："+item.getInt("price"));
            GlideUtils.load(mContext,item.getString("photo"),itemView.findViewById(R.id.iv_order_animal));
        }


    }


}
