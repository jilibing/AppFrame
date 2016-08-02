package com.zihan.appframe.biz.main;

import android.support.v7.widget.RecyclerView;

import com.zihan.appframe.Base.BaseRecyclerView.BaseRecyclerAdapter;
import com.zihan.appframe.Base.BaseRecyclerView.RecyclerHolder;
import com.zihan.appframe.R;

/**
 * Created by jilibing on 2016/7/18/0018.
 */
public class FunctionAdapter extends BaseRecyclerAdapter<String> {

    public FunctionAdapter(RecyclerView recyclerView) {
        super(recyclerView, null, R.layout.adapter_function);
    }

    @Override
    public void convert(RecyclerHolder holder, String item, int position, boolean isScrolling) {
        holder.setText(R.id.tv_content, item);
    }
}
