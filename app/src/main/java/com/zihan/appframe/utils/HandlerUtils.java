package com.zihan.appframe.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by jilibing on 2016/5/24.
 */
public class HandlerUtils {
    private static Handler sMainThreadHandler;

    private static Handler getMainThreadHandler() {
        if(sMainThreadHandler == null) {
            synchronized (HandlerUtils.class) {
                sMainThreadHandler = new Handler(Looper.getMainLooper());
            }
        }
        return sMainThreadHandler;
    }

    public static void post(Runnable runnable) {
        getMainThreadHandler().post(runnable);
    }

    public static void postDelayed(Runnable runnable, long delayMillis) {
        getMainThreadHandler().postDelayed(runnable, delayMillis);
    }
}
