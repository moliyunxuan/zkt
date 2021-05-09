package com.example.zkt.bean;

import java.util.ArrayList;

public class OrderBean {


    private String TotalMoney;//总价
    private String goodsImages;//商品图片

    public String getTotalMoney() {
        return TotalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        TotalMoney = totalMoney;
    }

    public String getGoodsImages() {
        return goodsImages;
    }

    public void setGoodsImages(String goodsImages) {
        this.goodsImages = goodsImages;
    }
}
