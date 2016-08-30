package com.zihan.appframe.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by jilibing on 2016/7/14/0014.
 */
public class GloabalConfig {

    private static Context sContext;

    public static void init(Application application) {
        sContext = application.getApplicationContext();
    }

    public static Context getContext() {
        if(sContext == null) {
            throw new NullPointerException("must be init in application");
        }
        return sContext;
    }
}
