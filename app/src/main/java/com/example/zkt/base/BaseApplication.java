package com.example.zkt.base;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Trace;

import com.example.zkt.bean.Category;
import com.example.zkt.bean.Goods;
import com.example.zkt.bean.Land;
import com.example.zkt.bean.User;
import com.example.zkt.data.DBHelper;
import com.example.zkt.data.http.VollyHelperNew;
import com.example.zkt.util.ToastHelper;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.File;

import cn.leancloud.AVObject;
import io.reactivex.plugins.RxJavaPlugins;

public class BaseApplication extends Application {

    private static BaseApplication context;

    private static Context mContext;
    // 默认存放图片的路径
    public final static String DEFAULT_SAVE_IMAGE_PATH = Environment.getExternalStorageDirectory() + File.separator + "CircleDemo" + File.separator + "Images" + File.separator;



    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        mContext = getApplicationContext();

        registSubClass();
        initImageLoader();

        initVollyHelper();
//        initSharePreferenceUtil();
        initToastHelper();
        initDBHelper();

    }

    private void initDBHelper() {
        DBHelper.init(getApplicationContext());
    }



    private void initVollyHelper() {
        VollyHelperNew.getInstance().initVollyHelper(getApplicationContext());
    }

//    private void initSharePreferenceUtil() {
//        SharePreferenceUtilNew.getInstance().init(getApplicationContext());
//    }

    private void initToastHelper() {
        ToastHelper.getInstance().init(getApplicationContext());
    }



    public static Context getContext() {
        return context;
    }


    private void registSubClass() {
        AVObject.registerSubclass(Category.class);
        AVObject.registerSubclass(Goods.class);
        AVObject.registerSubclass(Land.class);

    }
    private void initImageLoader() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory(true)
                //.cacheOnDisk(true)默认不缓存硬盘，以免本地图片加载也被缓存
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCache(new WeakMemoryCache())
                .threadPoolSize(4)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheFileCount(10)
                .diskCache(new UnlimitedDiskCache(new File(DEFAULT_SAVE_IMAGE_PATH)))
                .defaultDisplayImageOptions(options)
                .build();
        ImageLoader.getInstance().init(config);
    }
}
