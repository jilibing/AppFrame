package com.zihan.appframe.biz.loading;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.zihan.appframe.Base.BaseActivity;
import com.zihan.appframe.R;

import butterknife.OnClick;

public class LoadingActivity extends BaseActivity {

    @OnClick(R.id.bt_1)
    void click1() {
        showProgress();
    }
    @OnClick(R.id.bt_2)
    void click2() {
        hideProgress();
    }
    @OnClick(R.id.bt_3)
    void click3() {
        showFailure();
    }
    @OnClick(R.id.bt_4)
    void click4() {
        hideFailure();
    }
    @OnClick(R.id.bt_5)
    void click5() {
        showSuccess();
    }
    @OnClick(R.id.bt_6)
    void click6() {
        hideSuccess();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_loading;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new LoadingFragment();
        ft.add(R.id.container, fragment, "");
        ft.commit();
    }
}
