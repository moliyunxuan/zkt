package com.example.zkt.base;

import android.app.Application;
import android.content.Context;

import com.example.zkt.bean.Category;
import com.example.zkt.bean.Goods;
import com.example.zkt.bean.Land;

import cn.leancloud.AVObject;

public class BaseApplication extends Application {

    private static BaseApplication context;




    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        registSubClass();


    }

    public static Context getContext() {
        return context;
    }

    private void registSubClass() {
        AVObject.registerSubclass(Category.class);
        AVObject.registerSubclass(Goods.class);
        AVObject.registerSubclass(Land.class);

    }
}
