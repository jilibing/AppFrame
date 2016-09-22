package com.zihan.appframe.biz.loading;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.support.annotation.IntDef;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import com.zihan.appframe.R;
import com.zihan.appframe.utils.GloabalConfig;
import com.zihan.appframe.utils.HandlerUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.animation.ObjectAnimator.ofPropertyValuesHolder;

/**
 * Created by jilibing on 2016/9/20/0020.
 */
public class LoadingViewControl {

    public static final int STATE_NULL = -1;
    public static final int STATE_LOADING = 1;
    public static final int STATE_FAILURE = 2;
    public static final int STATE_SUCCESS = 3;

    private static final int ANIMATION_DURATION_SHORT = 300;
    private static final int ANIMATION_DURATION_LONG = 500;

    @IntDef({STATE_NULL, STATE_LOADING, STATE_FAILURE, STATE_SUCCESS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {

    }

    @State
    private int mCurrentState = STATE_NULL;

    private ViewGroup mRootView;
    private View mLoadingView, mLoadingFailureView, mLoadingSuccessView;
    private View mCurrentView;

    public LoadingViewControl(ViewGroup rootView) {
        mRootView = rootView;

        View.inflate(GloabalConfig.getContext(), R.layout.view_loading, mRootView);
        mLoadingView = mRootView.findViewById(R.id.vg_loading);
        hideView(mLoadingView);

        View.inflate(GloabalConfig.getContext(), R.layout.view_failure, mRootView);
        mLoadingFailureView = mRootView.findViewById(R.id.vg_loading_failure);
        hideView(mLoadingFailureView);

        View.inflate(GloabalConfig.getContext(), R.layout.view_succ, mRootView);
        mLoadingSuccessView = mRootView.findViewById(R.id.vg_loading_success);
        hideView(mLoadingSuccessView);
    }

    private void hideView(View view) {
        view.setScaleX(0);
        view.setScaleY(0);
        view.setAlpha(0);
    }

    private View getLoadingView() {
        return mLoadingView;
    }

    private View getLoadingFailureView() {
        return mLoadingFailureView;
    }

    private View getLoadingSuccessView() {
        return mLoadingSuccessView;
    }

    public void show(@State int state) {

        if(mCurrentState != state) {

            // 隐藏当前状态
            hideState(mCurrentState);

            // 显示当前状态
            showState(state);
        }

        mCurrentState = state;
    }

    public void hide() {
        hideState(mCurrentState);
    }

    private void hideState(@State int state) {
        if(state != STATE_NULL) {
            // 隐藏当前状态
            switch (state) {
                case STATE_LOADING:
                    hideProgress();
                    break;

                case STATE_FAILURE:
                    hideFailureView();
                    break;

                case STATE_SUCCESS:
                    hideSucc();
                    break;
            }
        }

        mCurrentState = STATE_NULL;
    }

    private void showState(@State int state) {
        if(state != STATE_NULL) {
            // 隐藏当前状态
            switch (state) {
                case STATE_LOADING:
                    showProgress();
                    break;

                case STATE_FAILURE:
                    showFailureView();
                    break;

                case STATE_SUCCESS:
                    showSucc();
                    break;
            }
        }
    }

    private void resetView() {
        if(mCurrentView != null) {
            PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0f);
            PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f);
            PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0f);

            ObjectAnimator objectAnimator = ofPropertyValuesHolder(mCurrentView, scaleX, scaleY, alpha);
            objectAnimator.setInterpolator(new AccelerateInterpolator());
            objectAnimator.setDuration(ANIMATION_DURATION_SHORT)
                    .start();
        }

        mCurrentView = null;
    }

    private void showCurrentView() {
        if(mCurrentView != null) {
            PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0, 1f);
            PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0, 1f);
            PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0, 1f);

            ObjectAnimator objectAnimator = ObjectAnimator
                    .ofPropertyValuesHolder(mCurrentView, scaleX, scaleY, alpha);

            objectAnimator.setInterpolator(new AccelerateInterpolator());
            objectAnimator.setDuration(ANIMATION_DURATION_SHORT)
                    .start();
        }
    }

    public void showProgress() {
        getLoadingView();
        mCurrentView = mLoadingView;
        showCurrentView();
    }

    public void hideProgress() {
        resetView();
    }

    public void showFailureView() {
        getLoadingFailureView();
        mCurrentView = mLoadingFailureView;
        showCurrentView();

        HandlerUtils.postDelayed(new Runnable() {
            @Override
            public void run() {
                hide();
            }
        }, ANIMATION_DURATION_LONG);
    }

    public void hideFailureView() {
        resetView();
    }

    public void showSucc() {
        getLoadingSuccessView();
        mCurrentView = mLoadingSuccessView;
        showCurrentView();

        HandlerUtils.postDelayed(new Runnable() {
            @Override
            public void run() {
                hide();
            }
        }, ANIMATION_DURATION_LONG);
    }

    public void hideSucc() {
        resetView();
    }
}
