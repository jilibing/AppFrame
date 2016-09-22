package com.zihan.appframe.Base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zihan.appframe.R;
import com.zihan.appframe.biz.loading.LoadingViewControl;
import com.zihan.appframe.biz.tab.FragmentNavigation;
import com.zihan.appframe.event.LoginEvent;
import com.zihan.appframe.utils.EventBusUtils;
import com.zihan.appframe.utils.LogUtils;

import butterknife.ButterKnife;

/**
 *
 * Created by jilibing on 2016/5/25.
 */
public abstract class BaseFragment extends Fragment {

    private Dialog mDialog;
    private TextView tvContent;
    protected Activity mActivity;
    protected FragmentNavigation mFragmentNavigation;
    private LoadingViewControl mLoadingViewControl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBusUtils.register(this);

        LogUtils.e("BaseFragment onCreateView "+container);
        return inflater.inflate(getContentView(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(view instanceof ViewGroup) {
            mLoadingViewControl = new LoadingViewControl(((ViewGroup) view));
        }else {
            throw new RuntimeException("the fragment view is not a viewgroup");
        }

        ButterKnife.bind(this, view);

        init(view);
    }

    @Override
    public void onDestroyView() {
        EventBusUtils.unregister(this);
        //ApiManager.getInstance().cancelRequestByTag(this);
        super.onDestroyView();
    }

    public void showProgress() {
        mLoadingViewControl.show(LoadingViewControl.STATE_LOADING);
    }

    public void hideProgress() {
        mLoadingViewControl.hide();
    }

    public void showFailure() {
        mLoadingViewControl.show(LoadingViewControl.STATE_FAILURE);
    }

    public void hideFailure() {
        mLoadingViewControl.hide();
    }

    public void showSuccess() {
        mLoadingViewControl.show(LoadingViewControl.STATE_SUCCESS);
    }

    public void hideSuccess() {
        mLoadingViewControl.hide();
    }

    @Override
    public void onAttach(Context context) {
        mActivity = getActivity();
        super.onAttach(context);
        if (context instanceof FragmentNavigation) {
            mFragmentNavigation = (FragmentNavigation) context;
        }
    }

    @LayoutRes
    protected abstract int getContentView();

    protected abstract void init(View view);

    protected void showLoading(final String msg) {
        if(mActivity == null) {
            return;
        }

        if (mActivity instanceof BaseActivity) {
            ((BaseActivity) mActivity).showLoading(msg);
        } else {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mDialog == null || tvContent == null) {
                        View view = LayoutInflater.from(mActivity).inflate(R.layout.layout_loading, null);
                        tvContent = (TextView) view.findViewById(R.id.tv_content);
                        mDialog = new Dialog(mActivity, R.style.dialog_transparent);
                        mDialog.setCancelable(false);
                        mDialog.setContentView(view);
                        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                            @Override
                            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                if (keyCode == KeyEvent.KEYCODE_BACK) {
                                    dialog.dismiss();
                                    mActivity.finish();
                                }
                                return false;
                            }
                        });
                    }
                    if (msg == null) {
                        tvContent.setVisibility(View.GONE);
                    } else {
                        tvContent.setVisibility(View.VISIBLE);
                        tvContent.setText(msg);
                    }
                    mDialog.show();
                }
            });
        }
    }

    protected void hideLoading() {
        if(mActivity == null) {
            return;
        }

        if (mActivity instanceof BaseActivity) {
            ((BaseActivity) mActivity).hideLoading();
        } else {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                }
            });
        }
    }

    /**
     * 登录事件
     */
    public void onEventMainThread(LoginEvent event) {
    }

    @Override
    public void onDestroy() {
        mActivity = null;
        super.onDestroy();
    }
}
