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
    public final static int NotExist_flag = 4;


//    //function area X, Y[手机]
//    public final static int functionX = 320;
//    public final static int functionY1 = 185;
//    public final static int functionY2 = 400;
//    public final static int functionY3 = 625;

//    //function area radius[手机]
//    public final static int function_reius = 100;

    //function area X, Y[平板]
    public final static int functionX = 305;
    public final static int functionY1 = 230;
    public final static int functionY2 = 535;
    public final static int functionY3 = 835;

    //function area radius[平板]
    public final static int function_reius = 130;

    //存储敲击开关的时间
    public  static long firstTime = 0;
    public  static long secondTime = 60000;
    public  static int clickCount = 0;

    public int actionType;

    //server url
    public static final String SERVER_URL_POST = "http://116.196.101.27:36666/todo/api/v1.0/run/";
    public static final String SERVER_URL_GET = "http://116.196.101.27:36666/todo/api/v1.0/value/";
    public static final String SERVER_URL_STOP = "http://116.196.101.27:36666/todo/api/v1.0/stop/";//停止运行
    public static final String SERVER_URL_IMMEDIATE = "http://116.196.101.27:36666/todo/api/v1.0/do/"; //立即触发

    //线程flag
    public static boolean ThreadFlag = false;


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
