package com.example.zkt.bean;

import android.os.Parcelable;

import cn.leancloud.AVFile;
import cn.leancloud.AVObject;
import cn.leancloud.AVParcelableObject;
import cn.leancloud.annotation.AVClassName;

@AVClassName("Seed")
public class Seed extends AVObject {


    public static final Parcelable.Creator CREATOR = AVParcelableObject.AVObjectCreator.instance;


    public static final String SID = "sid";
    public static final String SNAME = "sname";              //种子名称
    public static final String SCONTENT = "scontent";        //种子描述
    public static final String GROWTHCYCLE = "growthcycle";  //生长周期
    public static final String HARVEST = "harvest";          //产量
    public static final String SPICTURE = "spicture";        //种子照片
    public static final String SEASON= "season";             //播种季节
    public static final String SPRICE= "sprice";             //种子价格



    /** 设置种子 id */
    public void setSid(long sid){
        put(SID, sid);
    }

    /** 获取种子 id**/
    public long getSid(){
        return getLong(SID);
    }


    /** 获取种子名称**/
    public String getSname() {
        return getString(SNAME);
    }

    /** 设置种子名称*/
    public void setSname(String sname) {
        put(SNAME,sname);
    }


    /** 获取种子内容**/
    public String getScontent() {
        return getString(SCONTENT);
    }

    /** 设置种子内容**/
    public void setScontent(String scontent) {
        put(SCONTENT, scontent);
    }

    /** 获得种子图片**/
    public AVFile getSpicture() {
        return getAVFile(SPICTURE);
    }

    /** 设置种子图片**/
    public void setSpicture(AVFile spicture) {
        put(SPICTURE, spicture);
    }


    /** 获得土地价格**/
    public int getSprice() {
        return getInt(SPRICE);
    }

    /** 设置土地价格**/
    public void setSprice(int sprice) {
        put(SPRICE,sprice);
    }



    /** 获取产量**/
    public String getHarvest() {
        return getString(HARVEST);
    }

    /** 设置产量**/
    public void setHarvest(String harvest) {
        put(HARVEST, harvest);
    }


    /** 获取生长周期**/
    public String getGrowthcycle() {
        return getString(GROWTHCYCLE);
    }

    /** 设置生长周期**/
    public void setGrowthcycle(String growthcycle) {
        put(GROWTHCYCLE, growthcycle);
    }



}
