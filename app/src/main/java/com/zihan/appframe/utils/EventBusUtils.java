package com.zihan.appframe.utils;

import de.greenrobot.event.EventBus;

/**
 * Created by jilibing on 2016/5/26.
 */
public class EventBusUtils {

    public static void register(Object object) {
        EventBus.getDefault().register(object);
    }

    public static void unregister(Object object) {
        EventBus.getDefault().unregister(object);
    }

    public static void post(Object object) {
        EventBus.getDefault().post(object);
    }
}
