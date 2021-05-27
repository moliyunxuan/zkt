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


import java.util.List;

import cn.leancloud.AVObject;

public class GoodsPayOrderAdapter extends RecyclerView.Adapter<GoodsPayOrderAdapter.MyGoodsOrderHolder>  {

    private Context mContext;
    private List<AVObject> mList;
    private String mtitle;

    public GoodsPayOrderAdapter(Context context, List<AVObject> list,String title) {
        mContext = context;
        mList = list;
        mtitle = title;
    }


    @NonNull
    @Override
    public MyGoodsOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_order_goods, parent, false);
        return new GoodsPayOrderAdapter.MyGoodsOrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyGoodsOrderHolder holder, int position) {
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

    public class MyGoodsOrderHolder extends RecyclerView.ViewHolder {


        private TextView orderId;
        private TextView orderTime;
        private TextView goodsNumber;
        private TextView orderPrice;




        public MyGoodsOrderHolder(@NonNull View itemView) {
            super(itemView);

            orderId = (TextView) itemView.findViewById(R.id.tv_goods_order_id);
            orderTime = (TextView) itemView.findViewById(R.id.tv_goods_order_time);
            goodsNumber = (TextView) itemView.findViewById(R.id.tv_goods_order_number);
            orderPrice = (TextView) itemView.findViewById(R.id.tv_goods_order_price);

        }

        public void setData(AVObject item ) {

            orderId .setText(item.getObjectId());
            orderTime.setText(item.getCreatedAtString().substring(0,10));
            goodsNumber.setText(item.getInt("goodsNumber")+"件");
            orderPrice.setText("￥"+item.getString("orderPrice"));





        }

    }
}
