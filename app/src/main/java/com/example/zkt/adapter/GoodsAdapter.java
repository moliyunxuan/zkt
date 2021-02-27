package com.example.zkt.adapter;

import android.widget.ImageView;
import androidx.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.zkt.R;
import com.example.zkt.bean.Goods;
import com.example.zkt.util.GlideUtils;

import java.util.List;


/**
 * 右侧主界面商品ListView的适配器
 */
public class GoodsAdapter extends BaseQuickAdapter<Goods, BaseViewHolder> {





    public GoodsAdapter(@Nullable List<Goods> datas) {

        super(R.layout.item_goods,datas);
    }


    @Override
    protected void convert(BaseViewHolder holder, Goods item) {
        holder.setText(R.id.item_goods_name,item.getGname())
              .setText(R.id.item_goods_content,item.getGcontent())
              .setText(R.id.item_goods_price,"￥"+item.getGprice())
              .setText(R.id.item_goods_value,"["+item.getGvalue()+"]")//删除线
              .setText(R.id.item_goods_sale,item.getGsale()+"人付款") ;
        GlideUtils.load(mContext,item.getPhoto().getUrl(),(ImageView) holder
                .getView(R.id.item_goods_img));


    }

}
