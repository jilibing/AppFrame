package com.zihan.appframe.biz.restore;

import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.zihan.appframe.Base.BaseActivity;
import com.zihan.appframe.R;
import com.zihan.appframe.utils.ImageLoaderUtils;
import com.zihan.appframe.utils.LogUtils;

/**
 * Created by jilibing on 2016/7/21/0021.
 */
public class FragmentRetainDataActivity extends BaseActivity {

    private RetainedFragment dataFragment;
    private ImageView mImageView;
    private Bitmap mBitmap;

    @Override
    protected int getContentView() {
        return R.layout.activity_fragment_retain_data;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        // find the retained fragment on activity restarts
        FragmentManager fm = getFragmentManager();
        dataFragment = (RetainedFragment) fm.findFragmentByTag("data");
        // create the fragment and data the first time
        if (dataFragment == null) {
            // add the fragment
            dataFragment = new RetainedFragment();
            fm.beginTransaction().add(dataFragment, "data").commit();
        }
        mBitmap = collectMyLoadedData();
        initData();

        // the data is available in dataFragment.getData()
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mImageView = (ImageView) findViewById(R.id.id_imageView);
        if (mBitmap == null) {
            LogUtils.e("load bitmap from web");
            ImageLoaderUtils.getInstance().display(mImageView, "http://i1.ce.cn/ce/xwzx/photo/gdtp/201102/24/W020110224317053302967.jpg", new RequestListener() {
                @Override
                public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
                    GlideBitmapDrawable drawable = (GlideBitmapDrawable) resource;
                    mBitmap = drawable.getBitmap();
                    dataFragment.setData(mBitmap);
                    return false;
                }
            });
        } else {
            LogUtils.e("load bitmap from fragment");
            mImageView.setImageBitmap(mBitmap);
        }
    }

    @Override
    public void onDestroy() {
        LogUtils.e("FragmentRetainDataActivity onDestroy");
        super.onDestroy();
        // store the data in the fragment
        dataFragment.setData(mBitmap);
    }

    private Bitmap collectMyLoadedData() {
        return dataFragment.getData();
    }

}