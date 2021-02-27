package com.example.zkt.bean;

import android.os.Parcelable;

import java.io.Serializable;

import cn.leancloud.AVFile;
import cn.leancloud.AVObject;
import cn.leancloud.AVParcelableObject;
import cn.leancloud.annotation.AVClassName;

@AVClassName("Goods")
public class Goods extends AVObject implements Serializable {

    public static final Parcelable.Creator CREATOR = AVParcelableObject.AVObjectCreator.instance;

    public static final String GID = "gid";
    public static final String GNAME = "gname";        //商品名称
    public static final String GCONTENT = "gcontent";  //商品描述
    public static final String TYPE = "type";          //商品类型
    public static final String COMMENTS = "comments";  //商品评论
    public static final String GPHOTO = "gphoto";      //商品照片
    public static final String GPRICE= "gprice";       //商品现价
    public static final String GVALUE = "gvalue";      //商品原价
    public static final String GSTOCK = "gstock";      //商品库存
    public static final String GSALE = "gsale";        //商品销量



    /** 设置商品 id */
    public void setGid(long gid){
        put(GID, gid);
    }

    /** 获取商品 id**/
    public long getGid(){
        return getLong(GID);
    }


    /** 获取商品名称**/
    public String getGname() {
        return getString(GNAME);
    }

    /** 设置商品名称*/
    public void setGname(String gname) {
        put(GNAME,gname);
    }


    /** 获取商品内容**/
    public String getGcontent() {
        return getString(GCONTENT);
    }

    /** 设置商品内容**/
    public void setGcontent(String content) {
        put(GCONTENT, content);
    }

    /** 获得商品图片**/
    public AVFile getPhoto() {
        return getAVFile(GPHOTO);
    }

    /** 设置商品图片**/
    public void setPhoto(AVFile gphoto) {
        put(GPHOTO, gphoto);
    }

    /** 获得商品类型**/
    public Long getType() {
        return getLong(TYPE);
    }

    /** 设置商品类型**/
    public void setType(long type) {
        put(TYPE, type);
    }

    /** 获得商品价格**/
    public int getGprice() {
        return getInt(GPRICE);
    }

    /** 设置商品价格**/
    public void setGprice(int gprice) {
        put(GPRICE,gprice);
    }

    /** 获得商品价值**/
    public int getGvalue() {
        return getInt(GVALUE);
    }

    /** 设置商品价值**/
    public void setGvalue(int gvalue) {
        put(GVALUE,gvalue);
    }

    /** 获得商品库存**/
    public int getGstock() {
        return getInt(GSTOCK);
    }

    /** 设置商品库存**/
    public void setGstock(int gstock) {
        put(GSTOCK,gstock);
    }

    /** 获得商品销量**/
    public int getGsale() {
        return getInt(GSALE);
    }

    /** 设置商品销量**/
    public void setGsale(int gsale) {
        put(GSALE,gsale);
    }


}
