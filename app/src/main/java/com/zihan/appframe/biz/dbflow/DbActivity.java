package com.zihan.appframe.biz.dbflow;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.zihan.appframe.Base.BaseActivity;
import com.zihan.appframe.Base.BaseRecyclerView.Divider;
import com.zihan.appframe.R;
import com.zihan.appframe.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DbActivity extends BaseActivity {

    @BindView(R.id.rv_data)
    RecyclerView rv_data;

    private List<Card> mDatas = new ArrayList<>(10);
    private DbAdapter mAdapter;

    @OnClick(R.id.bt_insert)
    void insert() {

        Card card = new Card();
        card.title = "hello";
        card.save();

        query();
    }

    @OnClick(R.id.bt_query)
    void query() {

        List<Card> cardList = SQLite.select().from(Card.class)
                .orderBy(Card_Table.id, false) // 排序
                .queryList();

        mAdapter.refresh(cardList);
    }

    @OnClick(R.id.bt_delete)
    void delete() {
        Card card = SQLite.select().from(Card.class).querySingle();
        if(card == null) {
            ToastUtils.show("没有数据了");
            return;
        }
        card.delete();

        //card.title = "update";
        //card.update();

        query();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_db;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        rv_data.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mAdapter = new DbAdapter(rv_data);
        mAdapter.refresh(mDatas);

        Divider divider = new Divider(new ColorDrawable(0x00000000), OrientationHelper.VERTICAL);
        divider.setMargin(0, 0, 0, 0);
        divider.setHeight(20);
        rv_data.addItemDecoration(divider);

        rv_data.setAdapter(mAdapter);
    }

}
