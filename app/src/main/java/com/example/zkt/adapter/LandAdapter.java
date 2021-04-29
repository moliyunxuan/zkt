package com.example.zkt.adapter;

import android.widget.ImageView;
import androidx.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.zkt.R;
import com.example.zkt.bean.Land;
import com.example.zkt.util.GlideUtils;

import java.util.List;

import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;


public class LandAdapter extends BaseQuickAdapter<AVObject, BaseViewHolder>{


    public LandAdapter(@Nullable List<AVObject> datas) {
        super(R.layout.item_land,datas);
    }



    @Override
    protected void convert(BaseViewHolder helper, AVObject item) {
        // ImageView imageView = helper.getView(R.id.item_land_photo);

        AVQuery avQuery = new AVQuery("_USer");


        GlideUtils.load(mContext,item.getAVFile("lpicture").getUrl(),(ImageView) helper
                .getView(R.id.item_land_photo));
        helper.setText(R.id.item_land_price, "￥"+item.getInt("lprice"))
                .setText(R.id.item_land_name, item.getString("lname"))
                .setText(R.id.item_land_tv1,"价格：")
                .setText(R.id.item_land_tv2,"  元起");

    }
}