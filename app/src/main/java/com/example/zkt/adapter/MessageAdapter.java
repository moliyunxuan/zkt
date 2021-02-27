package com.example.zkt.adapter;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.example.zkt.R;
import com.example.zkt.databinding.ItemMessageBinding;
import com.example.zkt.util.BaseViewHolder;
import com.lihang.nbadapter.BaseAdapter;



public class MessageAdapter extends BaseAdapter<Integer> {

    @Override
    public RecyclerView.ViewHolder getViewHolder(ViewGroup viewGroup, int viewType) {


        ItemMessageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.item_message, viewGroup, false);
        return new BaseViewHolder(binding);
}

    @Override
    public void onBindMyViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

        BaseViewHolder baseViewHolder = (BaseViewHolder) viewHolder;
        if (baseViewHolder.binding instanceof ItemMessageBinding) {
            ItemMessageBinding binding = (ItemMessageBinding) baseViewHolder.binding;
            Integer itemBean = dataList.get(i);
            binding.imageMessage.setImageResource(itemBean);

        }
    }
}
