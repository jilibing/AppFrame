package com.zihan.appframe.utils;

import android.widget.Toast;

/**
 * Created by jilibing on 2016/5/24.
 */
public class ToastUtils {
    
    private static Toast sToast;

    private static Toast getToast(String msg) {
        if (sToast == null) {
            sToast = Toast.makeText(GloabalConfig.getContext(), msg, Toast.LENGTH_SHORT);
        }else {
            sToast.setText(msg);
        }
        return sToast;
    }

    public static void show(final String msg) {
        HandlerUtils.post(new Runnable() {
            @Override
            public void run() {
                getToast(msg).show();
            }
        });
    }
}
