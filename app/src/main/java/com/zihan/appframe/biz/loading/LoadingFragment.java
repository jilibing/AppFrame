package com.zihan.appframe.biz.loading;

import android.os.Bundle;
import android.view.View;

import com.zihan.appframe.Base.BaseFragment;
import com.zihan.appframe.R;
import com.zihan.appframe.utils.LogUtils;

import butterknife.OnClick;

public class LoadingFragment extends BaseFragment {

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

    public LoadingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoadingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoadingFragment newInstance(String param1, String param2) {
        LoadingFragment fragment = new LoadingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        LogUtils.e("LoadingFragment onCreate");
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_loading;
    }

    @Override
    protected void init(View view) {

    }
}
