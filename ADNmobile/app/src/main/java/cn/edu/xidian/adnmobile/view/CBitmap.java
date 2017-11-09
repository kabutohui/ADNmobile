package cn.edu.xidian.adnmobile.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;


public class CBitmap implements CDrawable {
    private int x, y, height, width;
    private Bitmap mBitmap;
    private Paint mPaint;
    private int mRotDegree;

    //设置控件属性,确定是那一模块
    public int Item_Attributes;

    //设置设备名称
    public String itemName;
    //设置switch控件阈值
    public String setThresholdValue  = "null";

    //设置控件左边沿连线坐标点
    public float beforeLX = 0;
    public float beforeLY = 0;
    public float nowLX = 0;
    public float nowLY = 0;

    //设置控件右边沿连线坐标点
    public float beforeRX = 0;
    public float beforeRY = 0;
    public float nowRX = 0;
    public float nowRY = 0;

    //与其相连的下一个控件的连线坐标点
    public float[] nextX_Y = {0,0};





    public CBitmap(Bitmap src, int x, int y) {
        this(src, x, y, null);
    }

    public CBitmap(Bitmap src, int x, int y, Paint p) {
        mBitmap = src;
        setXcoords(x);
        setYcoords(y);
        setPaint(p);
    }

    @Override
    public Paint getPaint() {
        return mPaint;
    }

    @Override
    public void setPaint(Paint p) {
        mPaint = p;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public int getXcoords() {
        return x;
    }

    @Override
    public int getYcoords() {
        return y;
    }

    @Override
    public void setXcoords(int x) {
        this.x = x;
    }

    @Override
    public void setYcoords(int y) {
        this.y = y;
    }

    @Override
    public void draw(Canvas canvas) {
        //以图片的正中心设置坐标
        canvas.drawBitmap(mBitmap, x - mBitmap.getWidth()/2, y - mBitmap.getHeight()/2, mPaint);
    }

    /*public void setAnimation(){
        mBitmap
    }*/
    @Override
    public int getRotation() {
        return mRotDegree;
    }

    @Override
    public void setRotation(int degree) {
        mRotDegree = degree;
    }
}
