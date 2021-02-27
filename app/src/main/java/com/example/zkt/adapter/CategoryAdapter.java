package com.example.zkt.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.zkt.R;
import com.example.zkt.bean.Category;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CategoryAdapter extends BaseQuickAdapter<Category, BaseViewHolder> {

    public CategoryAdapter(List<Category> datas) {
        super(R.layout.item_category, datas);
    }
    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Category category) {
        baseViewHolder.setText(R.id.tv_category,category.getCname());

    }
}
