package com.example.zkt.data.source;

import com.example.zkt.bean.Goods;
import com.example.zkt.bean.User;
import com.example.zkt.data.i.Callback;

import java.util.List;

public interface GoodsDataSource {

    /**查询所有商品**/
    void queryAllGoods(Callback callback);

    /**查询某一件商品**/
    void queryOneGood(long gid);

    /**查询指定类型下的商品**/
    void queryTypeGoods(int type);

    /**查询指定用户下的所有商品**/
    void queryGoods(User user);




}
