package com.zihan.appframe.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.zihan.appframe.R;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by jilibing on 2016/5/26.
 */
public class ImageLoaderUtils {

    private static ImageLoaderUtils instance = null;

    private ImageLoaderUtils() {
    }

    public static ImageLoaderUtils getInstance() {

        if (instance == null) {
            synchronized (ImageLoaderUtils.class) {
                if (instance == null) {
                    instance = new ImageLoaderUtils();
                }
            }
        }

        return instance;
    }

    public void display(final ImageView imageView, String url) {
        Glide
                .with(imageView.getContext())
                .load(url)
                .centerCrop()
                .error(getErrorHolder())
                .placeholder(getPlaceHolder())
                .into(imageView);
    }

    public void display(final ImageView imageView, String url, RequestListener requestListener) {
        Glide
                .with(imageView.getContext())
                .load(url)
                .centerCrop()
                .error(getErrorHolder())
                .placeholder(getPlaceHolder())
                .listener(requestListener)
                .into(imageView);
    }

    public void displayHead(ImageView imageView, String url) {
        Glide
                .with(imageView.getContext())
                .load(url)
                .error(getDefaultHolder())
                .placeholder(getDefaultHolder())
                .crossFade()
                .dontAnimate() // 解决第一次图片加载失败的问题
                .into(imageView);
    }

    // 圆形图片
    public void displayCircleImage(final ImageView imageView, String url) {
        Glide
                .with(imageView.getContext())
                .load(url)
                .error(getErrorHolder())
                .placeholder(getPlaceHolder())
                .crossFade()
                .bitmapTransform(new CropCircleTransformation(imageView.getContext()))
                .into(imageView);
    }

    private int getErrorHolder() {
        return R.drawable.ic_favorites;
    }

    private int getPlaceHolder() {
        return R.drawable.ic_favorites;
    }

    private int getDefaultHolder() {
        return R.drawable.ic_favorites;
    }
}
