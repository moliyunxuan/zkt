package com.example.zkt.bean;

import android.os.Parcelable;

import cn.leancloud.AVObject;
import cn.leancloud.AVParcelableObject;
import cn.leancloud.annotation.AVClassName;

@AVClassName("Type")
public class Type extends AVObject {

    public static final Parcelable.Creator CREATOR = AVParcelableObject.AVObjectCreator.instance;


    public static final String TID ="tid";//土地分类ID

    public static final String TNAME="tname";//土地分类名称




    /** 设置土地分类id */
    public void setTid(long tid){
        put(TID, tid);
    }

    /** 获取土地分类id**/
    public long getTid(){
        return getLong(TID);
    }

    /** 获取土地分类名称**/
    public String getTname() {
        return getString(TNAME);
    }

    /** 设置土地分类名称*/
    public void setTname(String tname) {
        put(TNAME,tname);
    }




}
