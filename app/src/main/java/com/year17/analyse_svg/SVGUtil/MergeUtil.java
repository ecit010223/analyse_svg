package com.year17.analyse_svg.SVGUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.year17.analyse_svg.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * 作者：张玉辉
 * 时间：2017/9/13.
 */

public class MergeUtil {

    /**
     * 根据图片路径读取图片，以Bitmap格式返回
     * @param context
     * @return
     */
    public static Bitmap readToBitmap(Context context){
//        Bitmap bitmap =BitmapFactory.decodeResource(context.getApplicationContext().getResources(), R.raw.img_back);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getApplicationContext().getResources(), R.raw.img_back_3);
//        Bitmap bitmap =BitmapFactory.decodeResource(context.getApplicationContext().getResources(), R.raw.img_back)
//                .copy(Bitmap.Config.ARGB_8888, true);
//        Bitmap bitmap= BitmapFactory.decodeStream(context.getApplicationContext()
//                .getClass().getResourceAsStream("/res/raw/img_back.jpg"));
        return bitmap;
    }

    public static Drawable readFromZipToBitmap(Context context){
        Drawable drawable = null;
        File file = copyRawZip(context);
        try{
            ZipFile zf = new ZipFile(file);
            InputStream in = new BufferedInputStream(new FileInputStream(file));
            ZipInputStream zin = new ZipInputStream(in);
            ZipEntry ze;
            while ((ze = zin.getNextEntry()) != null) {
                if (ze.isDirectory()) {
                    //Do nothing
                } else {
                    Log.i("tag", "file - " + ze.getName() + " : " + ze.getSize() + " bytes");
                    InputStream is = zf.getInputStream(ze);
//                    drawable = VectorDrawableCompat.createFromStream(is, ze.getName());
//                    drawable = ContextCompat.getDrawable(context, R.raw.svg0);
                    SVG svg = new SVGBuilder()
                            .readFromInputStream(is)
                            .build();
                    drawable = svg.getDrawable();
                    return drawable;

//                    if (drawable instanceof BitmapDrawable) {
//                        return ((BitmapDrawable) drawable).getBitmap();
//                    } else if (drawable instanceof VectorDrawable || drawable instanceof VectorDrawableCompat) {
//                        bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
//                                Bitmap.Config.ARGB_8888);
//                        Canvas canvas = new Canvas(bitmap);
//                        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
//                        drawable.draw(canvas);
//
//                        return bitmap;
//                    } else {
//                        throw new IllegalArgumentException("unsupported drawable type");
//                    }
                }
            }
            zin.closeEntry();
            return null;
        }catch (Exception e){
            e.printStackTrace();
        }
        return drawable;
    }

    public static Bitmap readFromZipToBitmap2(Context context, String file){
//        String fileName = file.substring(file.length() - 9, file.length() - 4);
        ZipFile zf = null;
        try {
            Uri uri = Uri.parse(context.getApplicationContext().getPackageName()+ R.raw.img_front);
            zf = new ZipFile(new File(String.valueOf(uri)));

            InputStream in = new BufferedInputStream(context.getApplicationContext().getResources()
                    .openRawResource(R.raw.img_front));
            ZipInputStream zin = new ZipInputStream(in);
            ZipEntry ze;
            while ((ze = zin.getNextEntry()) != null) {
                if (ze.isDirectory()) {
                    //Do nothing
                } else {
                    Log.i("tag", "file - " + ze.getName() + " : " + ze.getSize() + " bytes");
                    if (ze.getName().equals("/pic/haha.png")) {
                        InputStream is = zf.getInputStream(ze);
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        return bitmap;
                    }
                }
            }
            zin.closeEntry();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将两张图片合成一张
     * @param firstBitmap 第一张图片
     * @param secondBitmap 第二张图片
     * @return
     */
    public static Bitmap mergeTwoBitmap(Bitmap firstBitmap, Bitmap secondBitmap) {
        //以其中一张图片的大小作为画布的大小，或者也可以自己自定义
        Bitmap bitmap = Bitmap.createBitmap(firstBitmap.getWidth(), firstBitmap
                .getHeight(), firstBitmap.getConfig());
        //生成画布
        Canvas canvas = new Canvas(bitmap);
        //因为我传入的secondBitmap的大小是不固定的，所以我要将传来的secondBitmap调整到和画布一样的大小
        float w = firstBitmap.getWidth();
        float h = firstBitmap.getHeight();
        Matrix m = new Matrix();
        //确定secondBitmap大小比例
        m.setScale(w / secondBitmap.getWidth(), h / secondBitmap.getHeight());
        Paint paint = new Paint();
        //给画笔设定透明值，想将哪个图片进行透明化，就将画笔用到那张图片上
        paint.setAlpha(150);
        canvas.drawBitmap(firstBitmap, 0, 0, null);
        canvas.drawBitmap(secondBitmap, m, paint);

        return bitmap;
    }

    public static File copyRawZip(Context context){
        Context appContext = context.getApplicationContext();
        File file = new File(Environment.getExternalStorageDirectory(), "res.zip");
        try {
            //().openRawResource
            //InputStream is = getAssets().open("address.db");
//            InputStream is = appContext.getResources().openRawResource(R.raw.img_front);
            InputStream is = appContext.getResources().openRawResource(R.raw.img_front_3);
            FileOutputStream fos = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            is.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
