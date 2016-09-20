package com.zihan.appframe.biz.loading;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;

import com.zihan.appframe.R;
import com.zihan.appframe.utils.GloabalConfig;

/**
 * Created by jilibing on 2016/9/20/0020.
 */
public class LoadingViewControl {

    ViewGroup mRootView;
    View mLoadingView, mLoadingFailureView, mLoadingSuccessView;
    View mCurrentView;

    public LoadingViewControl(ViewGroup rootView) {
        mRootView = rootView;
    }

    private View getLoadingView() {
        if(mLoadingView == null) {
            View.inflate(GloabalConfig.getContext(), R.layout.view_loading, mRootView);
            mLoadingView = mRootView.findViewById(R.id.vg_loading);
        }

        return mLoadingView;
    }

    private View getLoadingFailureView() {
        if(mLoadingFailureView == null) {
            View.inflate(GloabalConfig.getContext(), R.layout.view_failure, mRootView);
            mLoadingFailureView = mRootView.findViewById(R.id.vg_loading_failure);
        }

        return mLoadingFailureView;
    }

    private View getLoadingSuccessView() {
        if(mLoadingSuccessView == null) {
            View.inflate(GloabalConfig.getContext(), R.layout.view_succ, mRootView);
            mLoadingSuccessView = mRootView.findViewById(R.id.vg_loading_success);
        }

        return mLoadingSuccessView;
    }

    private void resetView() {
        if(mCurrentView != null) {
            //mCurrentView.setVisibility(View.GONE);

            ObjectAnimator//
                    .ofFloat(mCurrentView, "scaleX", 0F)//
                    .ofFloat(mCurrentView, "scaleY", 0F)//
                    .setDuration(500)//
                    .start();
        }
    }

    private void showCurrentView() {
        if(mCurrentView != null) {
            ObjectAnimator//
                    .ofFloat(mCurrentView, "scaleX", 1F)//
                    .ofFloat(mCurrentView, "scaleY", 1F)//
                    .setDuration(500)//
                    .start();
        }
    }

    public void showProgress() {
        resetView();

        getLoadingView();

        mCurrentView = mLoadingView;
        showCurrentView();
    }

    public void hideProgress() {
        resetView();

        if(mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);

            if(mCurrentView == mLoadingView) {
                mCurrentView = null;
            }
        }
    }

    public void showFailureView() {
        resetView();

        getLoadingFailureView();
        mCurrentView = mLoadingFailureView;
        showCurrentView();
    }

    public void hideFailureView() {
        resetView();

        if(mLoadingFailureView != null) {
            mLoadingFailureView.setVisibility(View.GONE);

            if(mLoadingView == mLoadingFailureView) {
                mCurrentView = null;
            }
        }
    }

    public void showSucc() {
        resetView();

        getLoadingSuccessView();
        mCurrentView = mLoadingSuccessView;
        showCurrentView();
    }

    public void hideSucc() {
        resetView();

        if(mLoadingSuccessView != null) {
            mLoadingSuccessView.setVisibility(View.GONE);

            if(mCurrentView == mLoadingSuccessView) {
                mCurrentView = null;
            }
        }
    }
}
