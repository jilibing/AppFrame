package com.zihan.appframe.biz.dbflow;

import android.support.v7.widget.RecyclerView;

import com.zihan.appframe.Base.BaseRecyclerView.BaseRecyclerAdapter;
import com.zihan.appframe.Base.BaseRecyclerView.RecyclerHolder;
import com.zihan.appframe.R;

/**
 * Created by jilibing on 2016/7/18/0018.
 */
public class DbAdapter extends BaseRecyclerAdapter<Card> {

    public DbAdapter(RecyclerView recyclerView) {
        super(recyclerView, null, R.layout.adapter_db);
    }

    @Override
    public void convert(RecyclerHolder holder, Card item, int position, boolean isScrolling) {
        holder.setText(R.id.tv_title, item.title);
    }
}
