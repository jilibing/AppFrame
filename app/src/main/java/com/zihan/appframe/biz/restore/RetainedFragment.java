package com.zihan.appframe.biz.restore;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.zihan.appframe.utils.LogUtils;

/**
 * Created by jilibing on 2016/7/21/0021.
 */
public class RetainedFragment extends Fragment {
    // data object we want to retain
    private Bitmap data;

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        LogUtils.e("RetainedFragment onCreate");
        setRetainInstance(true);
    }

    public void setData(Bitmap data) {
        this.data = data;
    }

    public Bitmap getData() {
        return data;
    }

    @Override
    public void onDestroy() {
        LogUtils.e("RetainedFragment onDestroy");
        super.onDestroy();
    }
}


