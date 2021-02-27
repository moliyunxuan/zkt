package com.example.zkt.bean;

import android.os.Parcelable;

import java.io.Serializable;

import cn.leancloud.AVFile;
import cn.leancloud.AVObject;
import cn.leancloud.AVParcelableObject;
import cn.leancloud.annotation.AVClassName;


@AVClassName("Land")
public class Land extends AVObject implements Serializable {

    public static final Parcelable.Creator CREATOR = AVParcelableObject.AVObjectCreator.instance;

    public static final String LID = "lid";
    public static final String LNAME = "lname";      //土地名称
    public static final String LPRICE = "lprice";    //土地价格
    public static final String LAREA = "larea";      //土地面积
    public static final String LCONTENT = "larea";   //土地内容
    public static final String LTYPE = "ltype";      //土地种类
    public static final String LKIND = "lkind";      //土地种类名称
    public static final String LPICTURE = "lpicture";//土地照片
    public static final String PLANT = "plant";      //适合种植的农作物


    /** 设置土地 id */
    public void setLid(long lid){
        put(LID, lid);
    }

    /** 获取土地 id**/
    public long getLid(){
        return getLong(LID);
    }


    /** 获取土地名称**/
    public String getLname() {
        return getString(LNAME);
    }

    /** 设置土地名称*/
    public void setLname(String lname) {
        put(LNAME,lname);
    }


    /** 获取土地内容**/
    public String getLcontent() {
        return getString(LCONTENT);
    }

    /** 设置土地内容**/
    public void setLcontent(String lcontent) {
        put(LCONTENT, lcontent);
    }


    /** 获取土地种类名称**/
    public String getLkind() {
        return getString(LKIND);
    }

    /** 设置土地种类名称**/
    public void setLkind(String lkind) {
        put(LKIND, lkind);
    }

    /** 获得土地图片**/
    public AVFile getLpicture() {
        return getAVFile(LPICTURE);
    }

    /** 设置土地图片**/
    public void setLpicture(AVFile lpicture) {
        put(LPICTURE, lpicture);
    }

    /** 获得土地类型**/
    public Long getLtype() {
        return getLong(LTYPE);
    }

    /** 设置土地类型**/
    public void setLtype(long ltype) {
        put(LTYPE, ltype);
    }

    /** 获得土地价格**/
    public int getLprice() {
        return getInt(LPRICE);
    }

    /** 设置土地价格**/
    public void setLprice(int lprice) {
        put(LPRICE,lprice);
    }

    /** 获得土地面积**/
    public int getLarea() {
        return getInt(LAREA);
    }

    /** 设置土地面积**/
    public void setLarea(int larea) {
        put(LAREA,larea);
    }

    /** 获取土地适合种植的农作物**/
    public String getPlant() {
        return getString(PLANT);
    }

    /** 设置土地适合种植的农作物**/
    public void setPlant(String plant) {
        put(PLANT, plant);
    }










}
