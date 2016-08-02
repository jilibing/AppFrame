package com.zihan.appframe.biz.main;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;

import com.zihan.appframe.Base.BaseActivity;
import com.zihan.appframe.Base.BaseRecyclerView.BaseRecyclerAdapter;
import com.zihan.appframe.Base.BaseRecyclerView.Divider;
import com.zihan.appframe.R;
import com.zihan.appframe.biz.dbflow.DbActivity;
import com.zihan.appframe.biz.restore.FragmentRetainDataActivity;
import com.zihan.appframe.biz.tab.TabActivity;
import com.zihan.appframe.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.rv_function)
    RecyclerView mRvFunction;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mRvFunction.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        List<String> functions = new ArrayList<>(10);
        functions.add("Fragment Tab Switch");
        functions.add("Dbflow demo");
        functions.add("Retained Fragment");

        FunctionAdapter adapter = new FunctionAdapter(mRvFunction);
        adapter.refresh(functions);

        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object data, int position) {
                switch (position) {
                    case 0:
                        launch(MainActivity.this, TabActivity.class);
                        break;

                    case 1:
                        launch(MainActivity.this, DbActivity.class);
                        break;

                    case 2:
                        launch(MainActivity.this, FragmentRetainDataActivity.class);
                        break;

                    case 3:
                        break;
                }
            }
        });

        Divider divider = new Divider(new ColorDrawable(0x00000000), OrientationHelper.VERTICAL);
        divider.setMargin(0, 0, 0, 0);
        divider.setHeight(20);
        mRvFunction.addItemDecoration(divider);

        mRvFunction.setAdapter(adapter);
    }

    private long firstTime = 0;

    //监测返回键，再按一次退出程序
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                ToastUtils.show("再按一次退出程序");
                firstTime = secondTime;
                return true;
            } else {
                finish();
                System.exit(0);
            }
        }
        return super.onKeyUp(keyCode, event);
    }
}
