package com.zihan.appframe.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

/**
 * Created by jilibing on 2016/9/7/0007.
 */
public class BitmapUtils {

    public static Bitmap getBitmap(final int id) {
        Bitmap bitmap = BitmapFactory.decodeResource(GloabalConfig.getContext().getResources(), id);
        return bitmap;
    }

    // 采样率压缩，分辨率会变化
    public static Bitmap getBitmap(int id, int width, int height) {
        LogUtils.e("req width:" + width + " height:" + height);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(GloabalConfig.getContext().getResources(), id, options);

        int inSampleSize = calculateInSampleSize(options, width, height);
        LogUtils.e("inSampleSize:" + inSampleSize);

        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = inSampleSize;
        Bitmap bitmap = BitmapFactory.decodeResource(GloabalConfig.getContext().getResources(), id, options);
        return bitmap;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        //先根据宽度进行缩小
        while (width / inSampleSize > reqWidth) {
            inSampleSize++;
        }
        //然后根据高度进行缩小
        while (height / inSampleSize > reqHeight) {
            inSampleSize++;
        }
        return inSampleSize;
    }

    public static void display(ImageView imageView, int id) {
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        int width = ScreenUtils.getScreenWidth();
        int height = ScreenUtils.getScreenHeight();
        if (layoutParams != null) {
            if (layoutParams.width > 0) {
                width = layoutParams.width;
            }

            if (layoutParams.height > 0) {
                height = layoutParams.height;
            }
        }

        Bitmap bitmap = getBitmap(id, width, height);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
    }

    // 质量压缩，分辨率不变，压缩有极限
    // 这种压缩可能是没有必要的，因为分辨率不变，图片在内存中占的空间不变
    private static byte[] compressBitmapFileSize(Bitmap image, int size) {
        int quality = 100;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, quality, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中

        while (baos.toByteArray().length / 1024 > size) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            quality -= 10;//每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, quality, baos);//这里压缩options%，把压缩后的数据存放到baos中

            if (quality <= 0) { //
                break;
            }
        }

        return baos.toByteArray();
    }
}
