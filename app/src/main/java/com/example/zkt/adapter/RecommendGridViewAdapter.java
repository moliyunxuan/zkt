package com.example.zkt.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zkt.R;
import com.example.zkt.bean.Goods;
import com.example.zkt.util.GlideUtils;

import java.util.List;

public class RecommendGridViewAdapter extends BaseAdapter {

    private Context context;
    private List<Goods> listItemRecommend;

    public RecommendGridViewAdapter(Context context, List<Goods> listItemRecommend) {
        this.context = context;
        this.listItemRecommend = listItemRecommend;
    }

    @Override
    public int getCount() {
        if (listItemRecommend.size() > 4) {
            return 4;
        } else {
            return listItemRecommend.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return listItemRecommend.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GoodsViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_goods_for_homefragment, null);
            //布局文件中所有组件的对象封装到ViewHolder对象中
            holder = new GoodsViewHolder();
            holder.goodsName = convertView.findViewById(R.id.home_item_goodname);
            holder.goodsSale = convertView.findViewById(R.id.home_item_goodSale);
            holder.goodsPrice = convertView.findViewById(R.id.home_item_goodPrice);
            holder.goodsValue = convertView.findViewById(R.id.home_item_goodValue);
            holder.goodsimg = convertView.findViewById(R.id.home_item_goodimg);
            //把ViewHolder对象封装到View对象中
            convertView.setTag(holder);

        }else {
            holder = (GoodsViewHolder) convertView.getTag();
        }
        //获取点击的子菜单的View
        Goods goods = listItemRecommend.get(position);
        String name = goods.getGname();
        Integer sale = goods.getGsale();
        Integer price = goods.getGprice();
        Integer value = goods.getGvalue();


        holder.goodsName.setText(name);
        holder.goodsSale.setText(sale+"条交易记录");
        holder.goodsPrice.setText("￥ " + price);
        holder.goodsValue.setText("￥ "+value);
        holder.goodsValue.setPaintFlags(holder.goodsValue.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);



        GlideUtils.load(context,goods.getPhoto().getUrl(),holder.goodsimg);


        return convertView;
    }

    private class GoodsViewHolder {


        public TextView goodsName, goodsValue,goodsPrice,goodsSale;
        public ImageView goodsimg;



    }
}
