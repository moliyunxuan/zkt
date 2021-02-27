package com.example.zkt.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zkt.bean.DataBean;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;

import java.util.List;

/**
 * 轮播图Adapter
 */
public class ImageAdapter extends  BannerImageAdapter<DataBean>{
    public ImageAdapter(List<DataBean> mData) {
        super(mData);
    }



    @Override
    public void onBindView(BannerImageHolder holder, DataBean data, int position, int size) {
        holder.imageView.setImageResource(data.imageRes);


        class BannerViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public BannerViewHolder(@NonNull ImageView view) {
                super(view);
                this.imageView = view;
            }
        }


    }
}
