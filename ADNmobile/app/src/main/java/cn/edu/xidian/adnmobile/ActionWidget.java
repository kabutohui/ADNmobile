package cn.edu.xidian.adnmobile;

import android.graphics.Canvas;
import android.graphics.Paint;

import cn.edu.xidian.adnmobile.view.CDrawable;


/**
 * @author wangyao
 * @package com.lejurobot.aelos.aelosmini.view
 * @date 2017/7/12  9:45
 * @describe TODO
 * @project
 */

public class ActionWidget  implements CDrawable {

    public static final float RADIUS = 50;            //半径
    private int  coordinateX, coordinateY;              //控件的横纵坐标
    private Paint mPaint;                               //画笔


    public final static int ACTION_FRIST=0;
    public final static int ACTION_SCEND=1;
    public final static int ACTION_THRIST=2;

    //listView Falg
    public final static int datacollect_flag = 1;
    public final static int switch_flag = 2;
    public final static int function_flag = 3;

    public int actionType;



    public ActionWidget(int x,int y,Paint paint){
        setXcoords(x);
        setYcoords(y);
        setPaint(paint);
    }

    @Override
    public Paint getPaint() {
        return null;
    }

    @Override
    public int getXcoords() {
        return coordinateX;
    }

    @Override
    public int getYcoords() {
        return coordinateY;
    }

    @Override
    public void setXcoords(int x) {
        this.coordinateX=x;
    }

    @Override
    public void setYcoords(int y) {
        this.coordinateY=y;
    }

    @Override
    public void setPaint(Paint p) {
        this.mPaint=p;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(coordinateX,coordinateY,RADIUS,mPaint);
    }

    @Override
    public int getRotation() {
        return 0;
    }

    @Override
    public void setRotation(int degree) {

    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public int getActionType() {
        return actionType;
    }

}
