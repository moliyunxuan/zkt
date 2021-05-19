package com.example.zkt.util;


import com.orhanobut.logger.BuildConfig;
import com.orhanobut.logger.Logger;

/**
 * 日志打印工具类
 */
public class LogUtils {
    /**
     * 是否开启debug
     */
    public static boolean isDebug = BuildConfig.DEBUG;

    /**
     * 错误
     */
    public static void e(String tag, String msg) {
        if (isDebug) {
            Logger.t(tag).e(msg + "");
        }
    }

    /**
     * 调试
     */
    public static void d(String tag, String msg) {
        if (isDebug) {
            Logger.t(tag).d(msg + "");
        }
    }

    /**
     * 信息
     */
    public static void i(String tag, String msg) {
        if (isDebug) {
            Logger.t(tag).i(msg + "");
        }
    }
}
