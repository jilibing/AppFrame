package com.zihan.appframe.biz.tab;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zihan.appframe.Base.BaseFragment;
import com.zihan.appframe.biz.main.MainActivity;
import com.zihan.appframe.R;
import com.zihan.appframe.utils.LogUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TestFragment extends BaseFragment {

    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.et_name)
    EditText et_name;

    @OnClick(R.id.bt_add)
    void addFragment() {
        if(mFragmentNavigation != null) {
            mFragmentNavigation.pushFragment(TestFragment.newInstance("new Test"));
            LogUtils.e("add new Fragment");
        }
    }

    @OnClick(R.id.bt_test)
    void test() {
        MainActivity.launch(getContext(), MainActivity.class);
    }

    private String mName;

    public TestFragment() {
        // Required empty public constructor
    }

    public static TestFragment newInstance(String name) {
        TestFragment fragment = new TestFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mName = getArguments().getString("name");
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_test;
    }

    @Override
    protected void init(View view) {
        tv_name.setText(mName);
    }

    @Override
    public void onPause() {
        if(et_name != null) {
            et_name.clearFocus();
        }

        super.onPause();
        LogUtils.e("onPause " + mName);
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.e("onResume " + mName);
    }
}
