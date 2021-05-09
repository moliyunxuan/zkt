package com.example.zkt.adapter;

import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.zkt.R;
import com.example.zkt.bean.OrderBean;
import com.example.zkt.mvp.model.ShoppingCartBean;
import com.example.zkt.util.GlideUtils;

import java.util.List;

public class GoodsOrderAdapter extends BaseQuickAdapter<OrderBean, BaseViewHolder> {

    private List<OrderBean> mDatas;

    public GoodsOrderAdapter( @Nullable List<OrderBean> data) {
        super(R.layout.template_order_goods, data);
        this.mDatas = data;
    }



    @Override
    protected void convert(BaseViewHolder helper, OrderBean item) {

        GlideUtils.load(mContext, item.getGoodsImages(), (ImageView) helper
                .getView(R.id.iv_view));

    }


}
