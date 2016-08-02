package com.zihan.appframe;

import android.app.Application;

import com.orhanobut.logger.Logger;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.zihan.appframe.utils.GloabalConfig;

/**
 * Created by jilibing on 2016/7/14/0014.
 */
public class FrameApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        GloabalConfig.init(this);

        FlowManager.init(new FlowConfig.Builder(getApplicationContext()).build());

        Logger.init("jlb");
    }
}
