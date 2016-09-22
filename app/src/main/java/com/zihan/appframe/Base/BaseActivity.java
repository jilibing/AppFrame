package com.zihan.appframe.Base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zihan.appframe.R;
import com.zihan.appframe.biz.loading.LoadingViewControl;
import com.zihan.appframe.event.LoginEvent;
import com.zihan.appframe.utils.EventBusUtils;

import butterknife.ButterKnife;

/**
 * Created by jilibing on 2016/5/24.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Dialog mDialog;
    private TextView tvContent;

    private LoadingViewControl mLoadingViewControl;


    public static void launch(Context context, Class clazz) {
        Intent intent = new Intent(context, clazz);
        context.startActivity(intent);
    }

    @LayoutRes
    protected abstract int getContentView();

    protected abstract void init(Bundle savedInstanceState);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
        mLoadingViewControl = new LoadingViewControl(decorView);

        ButterKnife.bind(this);
        EventBusUtils.register(this);
        init(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        EventBusUtils.unregister(this);
        hideLoading();
        super.onDestroy();
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


    public void showLoading(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mDialog == null || tvContent == null) {
                    View view = LayoutInflater.from(BaseActivity.this).inflate(R.layout.layout_loading, null);
                    tvContent = (TextView) view.findViewById(R.id.tv_content);
                    mDialog = new Dialog(BaseActivity.this, R.style.dialog_transparent);
                    mDialog.setCancelable(false);
                    mDialog.setContentView(view);
                    mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            if (keyCode == KeyEvent.KEYCODE_BACK) {
                                dialog.dismiss();
                                finish();
                            }
                            return false;
                        }
                    });
                }
                if (TextUtils.isEmpty(msg)) {
                    tvContent.setVisibility(View.GONE);
                } else {
                    tvContent.setVisibility(View.VISIBLE);
                    tvContent.setText(msg);
                }
                if (!mDialog.isShowing()) {
                    mDialog.show();
                }
            }
        });
    }

    public void hideLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        });
    }

    /**
     * 登录事件
     */
    public void onEventMainThread(LoginEvent event) {
    }
}
