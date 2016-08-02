package com.zihan.appframe.utils;

import com.orhanobut.logger.Logger;
import com.zihan.appframe.BuildConfig;

/**
 * 日志工具
 * Created by liyingfeng on 2015/8/18.
 */
public class LogUtils {
    private static String tag = "jlb";
    public static boolean debug = BuildConfig.DEBUG;


    public static void d(String msg) {
        if (debug) {
            //Log.d(tag, msg);
            Logger.d(msg);
        }
    }

    public static void i(String msg) {
        if (debug) {
            //Log.i(tag, msg);
            Logger.i(msg);
        }
    }

    public static void w(String msg) {
        if (debug) {
            //Log.w(tag, msg);
            Logger.w(msg);
        }
    }

    public static void e(String msg) {
        if (debug) {
            //Log.e(tag, msg);
            Logger.e(msg);
        }
    }

    public static void e(Throwable e) {
        if (debug) {
            //Log.e(tag, e.getMessage());
            Logger.e(e.getMessage());
        }
    }
}