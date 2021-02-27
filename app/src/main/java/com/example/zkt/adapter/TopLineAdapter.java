package com.example.zkt.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zkt.R;
import com.example.zkt.bean.DataBean;
import com.youth.banner.adapter.BannerAdapter;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.util.BannerUtils;

import java.util.List;

public class TopLineAdapter extends BannerAdapter<DataBean,TopLineAdapter.TopLineHolder> {


    public TopLineAdapter(List<DataBean> datas) {
        super(datas);
    }

    @Override
    public TopLineHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new TopLineHolder(BannerUtils.getView(parent, R.layout.top_line_item2));
    }

    @Override
    public void onBindView(TopLineHolder holder, DataBean data, int position, int size) {

        holder.message.setText(data.title);
        if (data.viewType==1) {
            holder.source.setText("新闻");
        }else if (data.viewType==2) {
            holder.source.setText("动态");
        }
    }

    public class TopLineHolder extends RecyclerView.ViewHolder {
        public TextView message;
        public TextView source;

        public TopLineHolder(@NonNull View view) {
            super(view);
            message=view.findViewById(R.id.message);
            source=view.findViewById(R.id.source);
        }
    }

}
