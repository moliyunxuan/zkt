package com.example.zkt.base;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Trace;

import com.example.zkt.bean.Category;

import com.example.zkt.bean.Goods;
import com.example.zkt.bean.Land;
import com.example.zkt.data.DBHelper;
import com.example.zkt.data.dao.DaoMaster;
import com.example.zkt.data.dao.DaoSession;
import com.example.zkt.data.dao.User;
import com.example.zkt.data.http.VollyHelperNew;
import com.example.zkt.util.ToastHelper;
import com.example.zkt.util.UserLocalData;
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



    private User user;
    private static BaseApplication mInstance;

    private        DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private        DaoMaster               mDaoMaster;
    private static DaoSession mDaoSession;

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

        setDatabase();

    }

    /**
     * 设置greenDao
     */
    private void setDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        mHelper = new DaoMaster.DevOpenHelper(this, "shop-db", null);
        db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    public static DaoSession getDaoSession() {
        return mDaoSession;
    }


    private void initUser() {
        this.user = UserLocalData.getUser(this);
    }


    public User getUser() {
        return user;
    }

    public void putUser(User user, String token) {
        this.user = user;
        UserLocalData.putUser(this, user);
        UserLocalData.putToken(this, token);
    }

    public void clearUser() {
        this.user = null;
        UserLocalData.clearUser(this);
        UserLocalData.clearToken(this);
    }

    public static BaseApplication getApplication() {
        return mInstance;
    }

    public static BaseApplication getInstance() {
        return mInstance;
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
