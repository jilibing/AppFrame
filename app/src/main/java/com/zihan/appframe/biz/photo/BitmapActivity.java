package com.zihan.appframe.biz.photo;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.zihan.appframe.Base.BaseActivity;
import com.zihan.appframe.R;
import com.zihan.appframe.utils.BitmapUtils;
import com.zihan.appframe.utils.LogUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class BitmapActivity extends BaseActivity {

    @BindView(R.id.iv_bitmap1)
    ImageView iv_bitmap1;

    @BindView(R.id.iv_bitmap2)
    ImageView iv_bitmap2;

    @BindView(R.id.iv_bitmap3)
    ImageView iv_bitmap3;

    @OnClick(R.id.iv_bitmap1)
    void addShort() {
        addShortcut();
    }

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

    /**
     * 为程序创建桌面快捷方式
     * 带参数
     */
    private void addShortcut(){
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

        //快捷方式的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
        shortcut.putExtra("duplicate", false); //不允许重复创建

        //指定当前的Activity为快捷方式启动的对象: 如 com.everest.video.VideoPlayer
        //注意: ComponentName的第二个参数必须加上点号(.)，否则快捷方式无法启动相应程序
        ComponentName comp = new ComponentName(this.getPackageName(), "."+this.getLocalClassName());
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));

        //快捷方式的图标
        Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(this, R.drawable.icon_head_default);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
        //添加要做的事情
        Intent todo = new Intent(Intent.ACTION_CALL, Uri.parse("tel:10000"));
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, todo);
        sendBroadcast(shortcut);
    }
}
