package com.example.zkt.bean;

import android.os.Parcelable;

import cn.leancloud.AVFile;
import cn.leancloud.AVObject;
import cn.leancloud.AVParcelableObject;
import cn.leancloud.AVRelation;
import cn.leancloud.annotation.AVClassName;

@AVClassName("Category")
public class Category extends AVObject {

    public static final Parcelable.Creator CREATOR = AVParcelableObject.AVObjectCreator.instance;

    public static final String CID ="cid";//商品分类ID

    public static final String CNAME="cname";//商品分类名称



    /** 设置商品分类id */
    public void setCid(long cid){
        put(CID, cid);
    }

    /** 获取商品分类id**/
    public long getcid(){
        return getLong(CID);
    }

    /** 获取商品分类名称**/
    public String getCname() {
        return getString(CNAME);
    }

    /** 设置商品分类名称*/
    public void setCname(String cname) {
        put(CNAME,cname);
    }












}
