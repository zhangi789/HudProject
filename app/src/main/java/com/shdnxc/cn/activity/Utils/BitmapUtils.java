package com.shdnxc.cn.activity.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Zheng Jungen on 2017/3/24.
 */
public class BitmapUtils {
    /***
     * 根据Uri转换成Bitmap
     * @param context
     * @param uri
     * @return
     */
    public static Bitmap uriToBitmap(Context context, Uri uri){
        InputStream inputStream = null;
        try {
            inputStream = context.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if(inputStream != null) inputStream.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 压缩图片到指定文件
     * @param bmp
     * @param path
     * @return
     */
    public static boolean compressBitmap2file(Bitmap bmp, String path){
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(path);
            return bmp.compress(format, quality, stream);
        }catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if(stream != null) stream.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public  static Bitmap getRoundRectBitmap(Bitmap bitmap, int radius, int imageSize) {

        if (bitmap == null) {
            return null;
        }
        // bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        // 生成画布图像
        Bitmap resultBitmap = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(Color.YELLOW);

        int bmWidth = bitmap.getWidth();
        int bmHeight = bitmap.getHeight();

        final Rect rect = new Rect(0, 0, bmWidth, bmHeight);
        final RectF rectF = new RectF(0, 0, bmWidth - 10, bmHeight - 10);

        Canvas canvas = new Canvas(resultBitmap);

        paint.setXfermode(null);
        canvas.drawRoundRect(rectF, radius, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rectF, paint);

        // return bitmap;
        return resultBitmap;
    }
}
