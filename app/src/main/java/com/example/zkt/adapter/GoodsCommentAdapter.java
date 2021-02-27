package com.example.zkt.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zkt.R;
import com.example.zkt.databinding.ItemGoodsCommentsBinding;
import com.example.zkt.util.BaseViewHolder;
import com.lihang.nbadapter.BaseAdapter;

public class GoodsCommentAdapter extends BaseAdapter<String> {
    @Override
    public RecyclerView.ViewHolder getViewHolder(ViewGroup viewGroup, int viewType) {
        ItemGoodsCommentsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.item_goods_comments, viewGroup, false);
        return new BaseViewHolder(binding);

    }

    @Override
    public void onBindMyViewHolder(RecyclerView.ViewHolder viewHolder, int i) {



    }
}
