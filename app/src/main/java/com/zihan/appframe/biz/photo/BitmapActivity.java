package com.zihan.appframe.biz.photo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.zihan.appframe.Base.BaseActivity;
import com.zihan.appframe.R;
import com.zihan.appframe.utils.BitmapUtils;
import com.zihan.appframe.utils.LogUtils;

import butterknife.BindView;

public class BitmapActivity extends BaseActivity {

    @BindView(R.id.iv_bitmap1)
    ImageView iv_bitmap1;

    @BindView(R.id.iv_bitmap2)
    ImageView iv_bitmap2;

    @BindView(R.id.iv_bitmap3)
    ImageView iv_bitmap3;

    @Override
    protected int getContentView() {
        return R.layout.activity_bitmap;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Bitmap bitmap = BitmapUtils.getBitmap(R.drawable.icon_head_default); // 48 48
        LogUtils.e("bitmap size:"+bitmap.getByteCount());
        iv_bitmap1.setImageBitmap(bitmap);
        LogUtils.e("iv_bitmap1.getWidth():"+bitmap.getWidth()+" iv_bitmap1.getHeight():"+bitmap.getHeight());

        BitmapUtils.display(iv_bitmap2, R.drawable.icon_head_default);

        Bitmap bitmap3 = BitmapUtils.getBitmap(R.drawable.ic_gf_camera); // 96 96
        LogUtils.e("bitmap3 size:"+bitmap3.getByteCount());
        iv_bitmap3.setImageBitmap(bitmap3);
    }

}
