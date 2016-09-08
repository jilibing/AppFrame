package com.zihan.appframe.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by jilibing on 2016/9/8/0008.
 */
public class FileUtils {

    private static final String PUBLIC_DIR = "AppFrame";

    public static boolean isSDCardWriteable() {
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageWriteable = false;
        } else {
            // Something else is wrong. It may be one of many other states, but all we need
            // to know is we can neither read nor write
            mExternalStorageWriteable = false;
        }

        return mExternalStorageWriteable;
    }

    public static String getExternalDirectory() {
        if(!isSDCardWriteable()) {
            throw new IllegalStateException("SD Card is not writeable");
        }

        File dirFile = Environment.getExternalStoragePublicDirectory(PUBLIC_DIR);
        if(dirFile == null) {
            throw new NullPointerException("Create public directory is null");
        }

        if(!dirFile.exists()) {
            dirFile.mkdirs();
        }

        return dirFile.getAbsolutePath();
    }

    public static void saveBitmap(Bitmap bitmap, String filename) {
        String path = getExternalDirectory();
        File file = new File(path, filename);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save(byte[] buffer, String filename) {
        String path = getExternalDirectory();
        File file = new File(path, filename);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(buffer);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveFile(InputStream inputStream, String filename) {
        String path = getExternalDirectory();

        try {
            OutputStream outputStream = new FileOutputStream(path + File.separator + filename);
            int byteCount;
            byte[] bytes = new byte[1024];
            while ((byteCount = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, byteCount); // 注意：第二个参数offset，偏移量是指数据源的起始偏移量
            }
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            LogUtils.e("exp:" + e.getMessage());
        }
    }
}
