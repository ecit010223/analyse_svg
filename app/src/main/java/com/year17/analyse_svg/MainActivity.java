package com.year17.analyse_svg;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.year17.analyse_svg.SVGUtil.MergeUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_CODE = 1;

    String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    List<String> mPermissionList = new ArrayList<>();
    /** 用户是否禁止权限 **/
    boolean mShowRequestPermission = true;

    /** 两个Drawable叠加的展示控件 **/
    private ImageView imgMerge;
    /** 两个控件展示的背景控件 **/
    private ImageView imgBack;
    /** 两个控件展示的上部控件 **/
    private ImageView imgSVG;
    /** 背景图片 **/
    private Bitmap bitmapBack;
    /** SVG图片 **/
    private Drawable drawableSVG;
    private Bitmap bitmapBackPNG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
        initView();

        mergeByLayerDrawable();
//        mergeByDrawable();
//        mergeByView();
    }

    /** 权限申请 **/
    private void requestPermission(){
        mPermissionList.clear();
        //判断哪些权限未授予
        for(int i=0;i<permissions.length;i++){
            if(ContextCompat.checkSelfPermission(this,permissions[i])!= PackageManager.PERMISSION_GRANTED){
                mPermissionList.add(permissions[i]);
            }
        }
        if(!mPermissionList.isEmpty()){
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);
            ActivityCompat.requestPermissions(this,permissions,REQUEST_PERMISSION_CODE);
        }
    }

    /** 控件初始化 **/
    private void initView(){
        imgMerge = (ImageView)findViewById(R.id.img_merge);
        imgBack = (ImageView) findViewById(R.id.img_back);
        imgSVG = (ImageView)findViewById(R.id.img_svg);

        bitmapBack = MergeUtil.readToBitmap(this);
        drawableSVG = MergeUtil.readFromZipToBitmap(this);
//        bitmapBackPNG = ImageUtil.readToBitmap2(this);
    }

    /** 通过叠加Drawable至LayerDrawable的方式 **/
    private void mergeByLayerDrawable(){
        Drawable[] array = new Drawable[2];
        Drawable drawableBack = new BitmapDrawable(getResources(),bitmapBack);
        //背景图片的宽度
//        int widthBack = drawableBack.getIntrinsicWidth();
        //背景图片的高度
//        int heightBack = drawableBack.getIntrinsicHeight();
        array[0] = drawableBack;

//        Bitmap newBitmapBack = drawableToBitmap2(drawableBack);
//        Drawable newDrawableBack = new BitmapDrawable(null,newBitmapBack);
//        array[0] = newDrawableBack;

//        int widthSVG = 2280;
//        int heightSVG = 1536;
//        Matrix matrix = new Matrix();
//        float scaleWidth = ((float) widthBack / widthSVG);
//        float scaleHeight = ((float) heightBack / heightSVG);
//        matrix.postScale(scaleWidth, scaleHeight);
//        Bitmap newBitmapBackPNG = Bitmap.createBitmap(bitmapBackPNG, 0, 0, widthSVG, heightSVG,
//                matrix, true);
//        Drawable drawablePNG = new BitmapDrawable(null,newBitmapBackPNG);
//        array[1] = drawablePNG;

        //svg图片的宽度
//        int widthSVG = drawableSVG.getIntrinsicWidth();
        //svg图片的高度
//        int heightSVG = drawableSVG.getIntrinsicHeight();
        Bitmap bitmapSVG = drawableToBitmap(drawableSVG);
//        Matrix matrix = new Matrix();
//
//        float scaleWidth = ((float) widthBack / widthSVG);
//        float scaleHeight = ((float) heightBack / heightSVG);
//        matrix.postScale(scaleWidth, scaleHeight);
//        Bitmap newbitmapSVG = Bitmap.createBitmap(bitmapSVG, 0, 0, widthSVG, heightSVG,
//                matrix, true);
        Drawable newDrawableSVG = new BitmapDrawable(null,bitmapSVG);
//        Drawable newDrawableSVG = new BitmapDrawable(null,bitmapSVG);
        array[1] = newDrawableSVG;
//        array[1] = drawableSVG;

        LayerDrawable layerDrawable = new LayerDrawable(array);
        //第一个参数表示放在第几层，其它四个参数表示到left、top、right、bottom的距离
        layerDrawable.setLayerInset(0, 0, 0, 0, 0);
//        layerDrawable.setLayerInset(1, -340, 0, 0, 0);
        layerDrawable.setLayerInset(0, 0, 0, 0, 0);

        //注意：一定要加这行代码,不然会加载不出来
        imgMerge.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        imgMerge.setImageDrawable(layerDrawable);
    }

    /** 通过在控件上设置Background和ImageDrawable的方式实现 **/
    private void mergeByDrawable(){
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        imgMerge.measure(w, h);
        int height =imgMerge.getMeasuredHeight();
        int width =imgMerge.getMeasuredWidth();
        imgMerge.setBackground(new BitmapDrawable(getResources(),bitmapBack));
        //注意：一定要加这行代码,不然会加载不出来
        imgMerge.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        imgMerge.setImageDrawable(drawableSVG);
        height =imgMerge.getMeasuredHeight();
        width =imgMerge.getMeasuredWidth();
    }

    /** 通过控件叠加的方式实现 **/
    private void mergeByView(){
        imgBack.setImageBitmap(bitmapBack);
        //注意：一定要加这行代码,不然会加载不出来
        imgSVG.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        imgSVG.setImageDrawable(drawableSVG);
    }

    /** Drawable转Bitmap **/
    private Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    /** Drawable转Bitmap **/
    private Bitmap drawableToBitmap2(Drawable drawable) {
//        int width = drawable.getIntrinsicWidth();
//        int width = (int)((1024.0f/1366.0f)*2732.0f);
        int width = 2732;
//        int height = drawable.getIntrinsicHeight();
        int height = 1536;
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_PERMISSION_CODE:
                for(int i=0;i<grantResults.length;i++){
                    if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                        //判断是否勾选禁止后不再询问
                        boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(this,permissions[i]);
                        if(showRequestPermission){
                            requestPermission();  //重新申请
                            return;
                        }else {
                            mShowRequestPermission = false;  //禁止申请
                        }
                    }
                }
                break;
        }
    }
}
