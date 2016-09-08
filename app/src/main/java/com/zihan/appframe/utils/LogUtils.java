package com.zihan.appframe.utils;

import android.util.Log;

import com.orhanobut.logger.Logger;

/**
 * 日志工具
 * Created by liyingfeng on 2015/8/18.
 */
public class LogUtils {
    private static String tag = "jlb";
    private static boolean isLogger = false;
    public static boolean debug = true;//BuildConfig.DEBUG;


    public static void d(String msg) {
        if (debug) {
            if (isLogger) {
                Logger.d(msg);
            } else {
                Log.d(tag, msg);
            }
        }
    }

    public static void i(String msg) {
        if (debug) {
            if (isLogger) {
                Logger.i(msg);
            } else {
                Log.i(tag, msg);
            }
        }
    }

    public static void w(String msg) {
        if (debug) {
            if (isLogger) {
                Logger.w(msg);
            } else {
                Log.w(tag, msg);
            }
        }
    }

    public static void e(String msg) {
        if (debug) {
            if (isLogger) {
                Logger.e(msg);
            } else {
                Log.e(tag, msg);
            }
        }
    }

    public static void e(Throwable e) {
        if (debug) {
            if (isLogger) {
                Logger.e(e.getMessage());
            } else {
                Log.e(tag, e.getMessage());
            }
        }
    }
}