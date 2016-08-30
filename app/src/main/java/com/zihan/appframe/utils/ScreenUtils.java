package com.zihan.appframe.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

/**
 * Created by jilibing on 2016/8/30/0030.
 */
public class ScreenUtils {

    public static void showScreenInfo() {
        // 获取屏幕密度（方法3）
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) GloabalConfig.getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);

        float density  = dm.density;      // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
        int densityDPI = dm.densityDpi;     // 屏幕密度（每寸像素：120/160/240/320）
        float xdpi = dm.xdpi;
        float ydpi = dm.ydpi;

        int screenWidthDip = dm.widthPixels;        // 屏幕宽（dip，如：320dip）
        int screenHeightDip = dm.heightPixels;      // 屏幕宽（dip，如：533dip）

        float w2 = screenWidthDip / xdpi;
        float h2 = screenHeightDip / xdpi;
        double inch = Math.sqrt(w2*w2 + h2*h2);

        ActivityManager am = (ActivityManager) GloabalConfig.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi); //mi.availMem; 当前系统的可用内存
        String availMem = Formatter.formatFileSize(GloabalConfig.getContext(), mi.availMem);// 将获取的内存大小规格化
        String totalMem = Formatter.formatFileSize(GloabalConfig.getContext(), mi.totalMem);

        Log.e("jlb", "density:" + density +
                    "\ndensityDPI:" + densityDPI +
                    "\nxdpi:"+xdpi +
                    "\nydpi:"+ydpi +
                    "\nscreenWidthDip:"+screenWidthDip +
                    "\nscreenHeightDip:"+screenHeightDip +
                    "\ninch:" + inch +
                    "\navailMem:" + availMem +
                    "\ntotalMem:" + totalMem
                    );
    }
}
