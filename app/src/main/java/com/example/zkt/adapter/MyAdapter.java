package com.example.zkt.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.zkt.R;
import com.example.zkt.ui.widget.SaleProgressView;
import com.example.zkt.util.GlideUtils;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.AVObject;

public class MyAdapter extends SwipeCardAdapter<MyAdapter.MyHolder> {
    private Context mContext;

    public MyAdapter(Context context, List<AVObject> list) {
        super(list);
        mContext = context;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.setData((AVObject) mList.get(position));
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvBatch;
        private TextView tvTime;
        private TextView tvkind;
        private TextView tvSpiece;
        private TextView tvCycle;
        private TextView tvOutput;
        private TextView tvPriece;
        private TextView tvSale;
        private ImageView imageView;
        private SaleProgressView saleProgressView;

        public MyHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvBatch = (TextView) itemView.findViewById(R.id.tv_batch);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvkind = (TextView) itemView.findViewById(R.id.tv_kind);
            tvSpiece = (TextView) itemView.findViewById(R.id.tv_specie);
            tvCycle = (TextView) itemView.findViewById(R.id.tv_cycle);
            tvOutput = (TextView) itemView.findViewById(R.id.tv_output);
            tvPriece = (TextView) itemView.findViewById(R.id.tv_price);
            tvSale = (TextView) itemView.findViewById(R.id.tv_sale);
            imageView = (ImageView) itemView.findViewById(R.id.iv_animal);
            saleProgressView = (SaleProgressView) itemView.findViewById(R.id.spv);

        }


        public void setData(AVObject item) {
            tvName.setText(item.getString("name"));
            tvkind.setText(item.getString("kind"));
            tvBatch.setText(item.getString("batch"));
            tvTime.setText(item.getString("time"));
            tvSpiece.setText(item.getString("specie"));
            tvCycle.setText(item.getString("cycle"));
            tvPriece.setText(""+item.getInt("price"));
            tvSale.setText("已有"+item.getInt("rate")+"人参与认养");
            GlideUtils.load(mContext,item.getString("image"),itemView.findViewById(R.id.iv_animal));

            saleProgressView.setTotalAndCurrentCount(100,item.getInt("rate"));





        }


    }
}