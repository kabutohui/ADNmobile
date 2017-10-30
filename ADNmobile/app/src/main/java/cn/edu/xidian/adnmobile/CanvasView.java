package cn.edu.xidian.adnmobile;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import cn.edu.xidian.adnmobile.view.CDrawable;
import cn.edu.xidian.adnmobile.view.FabricView;


/**
 * @author wangyao
 * @package com.example.administrator.myapplication
 * @date 10017/6/27  14:05
 * @describe 实现一个画布的放置控件、缩放画布、控件之间的连线、画布的手势识别、缩放后手势滑动的识别
 * @project
 */

public class CanvasView extends FabricView {

    private final Paint mPaint;
    private Context mContext;
    // Scale and zoom in/out factor.
    private static final int INIT_ZOOM_SCALES_INDEX = 0;
    private int mCurrentZoomScaleIndex = INIT_ZOOM_SCALES_INDEX;
    private static final float[] ZOOM_SCALES = new float[]{1.0f, 1.25f, 1.5f, 1.75f, 2.0f};
    private float mViewScale = ZOOM_SCALES[INIT_ZOOM_SCALES_INDEX];
    private static final int SCREEN_SLICE = 3;                      //把屏幕分成若干块
    private static final int WIDGET_GAP = 110;                      //控件排列的后的间距

    protected boolean mScrollable = true;
    private ScaleGestureDetector mScaleGestureDetector;                 //缩放手势
//    private GestureDetector mTapGestureDetector;                        //手势监听类
    private int mPanningPointerId = MotionEvent.INVALID_POINTER_ID;
    private Point mPanningStart = new Point();
    private int mOriginalScrollX;
    private int mOriginalScrollY;
    private float mOffSetViewScroll;

    // Default desired width of the view in pixels.
    private static final int DESIRED_WIDTH = 10048;
    // Default desired height of the view in pixels.
    private static final int DESIRED_HEIGHT = 10048;
    // Interactive Modes
    public static final int DRAW_MODE = 0;              //可以绘制线段的模式
    public static final int SELECT_MODE = 1; // TODO Support Object Selection.
    public static final int ROTATE_MODE = 2; // TODO Support Object ROtation.
    public static final int LOCKED_MODE = 3;              //空模式

    // Default Background Styles         背景颜色
    public static final int BACKGROUND_STYLE_BLANK = 0;
    public static final int BACKGROUND_STYLE_NOTEBOOK_PAPER = 1;
    public static final int BACKGROUND_STYLE_GRAPH_PAPER = 2;

    private float DownX;
    private float DownY;

    public void setDownX(float downX) {
        DownX = downX;
    }

    public void setDownY(float downY) {
        DownY = downY;
    }

    private float moveX;
    private float moveY;
    long currentMS;
    private int mDown2Widget;
    private float moveX1;
    private float moveY1;
    private ActionWidget mActionWidget;
    private Canvas canvas = new Canvas();
    private RectF dirtyRect = new RectF();
    private int mWidth;                         //获取屏幕宽高
    private int mHeight;
    private long mDownTime;
    private long mUpTime;
    private long mMoveTime;


    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GREEN);
    }

    /**
     * The method onFinishInflate() will be called after all children have been added.
     * 这个方法是所有的子view被添加之后调用
     */
    @Override
    public void onFinishInflate() {
        super.onFinishInflate();

        // Setting the child view's pivot point to (0,0) means scaling leaves top-left corner in
        // place means there is no need to adjust view translation.
        this.setPivotX(0);
        this.setPivotY(0);

        setWillNotDraw(false);
        setHorizontalScrollBarEnabled(mScrollable);                 //水平滑动滚动条的设置
        setVerticalScrollBarEnabled(mScrollable);                   //竖直滑动滚动条的设置
        setBackgroundMode(BACKGROUND_STYLE_GRAPH_PAPER);
        mScaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureListener());

//        mTapGestureDetector = new GestureDetector(getContext(), new TapGestureListener());          //设置手势的监听

    }

    public void setBackgroundMode(int mBackgroundMode) {
        super.setBackgroundMode(mBackgroundMode);
    }

    /**
     * 设置画布所处在的模式,可以添加并设置其他的模式
     */
    public void setInteractionMode(int interactionMode) {
        if (interactionMode == DRAW_MODE) {
            super.setInteractionMode(DRAW_MODE);
        } else if (interactionMode == LOCKED_MODE) {
            super.setInteractionMode(LOCKED_MODE);
        } else if (interactionMode == SELECT_MODE) {
            super.setInteractionMode(SELECT_MODE);
        }

    }

    /**
     * 增加画布里面的控件或其他实现了CDrawable接口的类
     */
    public boolean addCanvasDrawable(CDrawable cDrawable) {
        super.mDrawableList.add(cDrawable);
        invalidate();
        return true;
    }

    public void sortCanvasDrawable() {
        getScreenWidthAndHight();

        int fristActionX = WIDGET_GAP;
        int fristActionY = WIDGET_GAP;
        int fristActionX1 = mWidth / SCREEN_SLICE + WIDGET_GAP;
        int fristActionY1 = WIDGET_GAP;
        int fristActionX2 = (mWidth / SCREEN_SLICE) * 2 + WIDGET_GAP;
        int fristActionY2 = WIDGET_GAP;
        for (int i = 0; i < mDrawableList.size(); i++) {
            if (mDrawableList.get(i) instanceof ActionWidget) {
                if (((ActionWidget) mDrawableList.get(i)).getActionType() == ActionWidget.ACTION_FRIST) {
                    if (fristActionX < mWidth / SCREEN_SLICE) {
                        mDrawableList.get(i).setXcoords(fristActionX);
                        mDrawableList.get(i).setYcoords(fristActionY);
                    } else {
                        fristActionX = WIDGET_GAP;
                        fristActionY = fristActionY + WIDGET_GAP;
                        mDrawableList.get(i).setXcoords(fristActionX);
                        mDrawableList.get(i).setYcoords(fristActionY);
                    }
                    fristActionX = fristActionX + WIDGET_GAP;
                } else if (((ActionWidget) mDrawableList.get(i)).getActionType() == ActionWidget.ACTION_SCEND) {
                    if (fristActionX1 < (mWidth / SCREEN_SLICE) * 2) {
                        mDrawableList.get(i).setXcoords(fristActionX1);
                        mDrawableList.get(i).setYcoords(fristActionY1);
                    } else {
                        fristActionX1 = mWidth / SCREEN_SLICE + WIDGET_GAP;
                        fristActionY1 = fristActionY1 + WIDGET_GAP;
                        mDrawableList.get(i).setXcoords(fristActionX1);
                        mDrawableList.get(i).setYcoords(fristActionY1);
                    }
                    fristActionX1 = fristActionX1 + WIDGET_GAP;
                } else if (((ActionWidget) mDrawableList.get(i)).getActionType() == ActionWidget.ACTION_THRIST) {
                    if (fristActionX2 < mWidth) {
                        mDrawableList.get(i).setXcoords(fristActionX2);
                        mDrawableList.get(i).setYcoords(fristActionY2);
                    } else {
                        fristActionX2 = (mWidth / SCREEN_SLICE) * 2 + WIDGET_GAP;
                        fristActionY2 = fristActionY2 + WIDGET_GAP;
                        mDrawableList.get(i).setXcoords(fristActionX2);
                        mDrawableList.get(i).setYcoords(fristActionY2);
                    }
                    fristActionX2 = fristActionX2 + WIDGET_GAP;
                }
            }

        }
        invalidate();
    }

    private void getScreenWidthAndHight() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        mWidth = wm.getDefaultDisplay().getWidth();
        mHeight = wm.getDefaultDisplay().getHeight();
    }

    /**
     * 清除画布里面的控件
     */
    public void cleanPager() {
        super.cleanPage();
    }

    /**
     * 重置画布的大小尺寸
     */
    public void resetView() {
        // Reset scrolling state.
        mPanningPointerId = MotionEvent.INVALID_POINTER_ID;
        mPanningStart.set(0, 0);
        mOriginalScrollX = 0;
        mOriginalScrollY = 0;
        updateScaleStep(INIT_ZOOM_SCALES_INDEX);
        scrollTo((int) this.getX(), (int) this.getY());
    }


    @Override
    public boolean onTouchDrawMode(MotionEvent event) {
        event.offsetLocation(getScrollX(), getScrollY());                //缩放后偏移的距离，保证缩放后触点跟缩放前对应
        return super.onTouchDrawMode(event);
    }

    @Override
    public boolean onTouchSelectMode(MotionEvent event) {
        event.offsetLocation(getScrollX(), getScrollY());                //缩放后偏移的距离，保证缩放后触点跟缩放前对应
        return super.onTouchSelectMode(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
//        mTapGestureDetector.onTouchEvent(event);
        event.offsetLocation(getScrollX(), getScrollY());                //缩放后偏移的距离，保证缩放后触点跟缩放前对应
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mDownTime = System.currentTimeMillis();
                DownX = event.getX();//float DownX
                DownY = event.getY();//float DownY
                //判断点击的坐标范围是否在控件上
//                Log.i("everb", "控件的list集合：" + mDrawableList);
                mDown2Widget = getDown2Widget();
                moveX = 0;
                moveY = 0;
                moveX1 = 0;
                moveY1 = 0;

                if (monCanvasClickListener != null) {
                    monCanvasClickListener.onCanvasClick((int) DownX, (int) DownY);
                }

            }
            break;
            case MotionEvent.ACTION_MOVE: {
                moveX = Math.abs(event.getX() - DownX);//X轴距离
                moveY = Math.abs(event.getY() - DownY);//y轴距离
                moveX1 = event.getX();
                moveY1 = event.getY();
                if (moveX == 0 && moveY == 0) {
                    mMoveTime = System.currentTimeMillis();
                    long DValueTime = mMoveTime - mDownTime;
                    System.out.println("long touch before "+DValueTime+">>>>>>>>>>>>>>>>>>>");
                    //if (DValueTime>200){
                    if(getDown2Widget() != -1) {
                        if (DValueTime > 20) {
                            if (mOnWidgetLongPressListener != null) {
                                System.out.println("long touch>>>>>>>>>>>>>>>>>>>"+getDown2Widget());
                                mOnWidgetLongPressListener.onWidgetLongPress(mDown2Widget, (int) moveX1, (int) moveY1);
                            }
                        }
                    }
                    return true;
                } else {
                    if (mDown2Widget > -1) {
                            if (mOnWidgetMoveListener!=null){
                                mOnWidgetMoveListener.onWidgetMove(mDown2Widget,(int)moveX1,(int)moveY1);
                            }
                            mDrawableList.get(mDown2Widget).setXcoords((int) moveX1);
                            mDrawableList.get(mDown2Widget).setYcoords((int) moveY1);
                            invalidate();
                    }
                }

                DownX = event.getX();
                DownY = event.getY();
            }
            break;
            case MotionEvent.ACTION_UP: {
                long moveTime = System.currentTimeMillis() - currentMS;//移动时间
                mUpTime = System.currentTimeMillis();
                long DValueTime = mUpTime - mDownTime;
                if (mDown2Widget > -1) {
                    //判断是否为拖动事件
                    if (!(moveTime > 1000 && (moveX > 100 || moveY > 100))) {
                        if (DValueTime < 200) {
                            if (mOnWidgetClickListener!=null){
                                mOnWidgetClickListener.onWidgetClick(mDown2Widget,(int)moveX1,(int)moveY1);
                            }
                        }
                    }
                }
                if (mOnWidgetUpListener!=null){
                    mOnWidgetUpListener.onWidgetUp(mDown2Widget,(int)moveX1,(int)moveY1);
                }
            }
            break;
        }
        return true;

    }

    public onWidgetUpListener mOnWidgetUpListener;

    public interface onWidgetUpListener{
        void onWidgetUp(int index, int x, int y);
    }

    public void  setOnWidgetUpListener(onWidgetUpListener mOnWidgetUpListener){
        this.mOnWidgetUpListener=mOnWidgetUpListener;
    }

    public onWidgetMoveListener mOnWidgetMoveListener;

    public interface onWidgetMoveListener{
         void onWidgetMove(int index, int x, int y);
    }

    public void  setOnWidgetMoveListener(onWidgetMoveListener moveListener){
        this.mOnWidgetMoveListener=moveListener;
    }

    public onWidgetLongPressListener mOnWidgetLongPressListener;

    public interface onWidgetLongPressListener{
        void onWidgetLongPress(int index, int x, int y);
    }

    public void setOnWidgetLongPressListener(onWidgetLongPressListener mOnWidgetLongPressListener){
        this.mOnWidgetLongPressListener=mOnWidgetLongPressListener;
    }



    public onCanvasClickListener monCanvasClickListener;
    public interface  onCanvasClickListener
    {
        void onCanvasClick(int x,int y);
    }
    public void setOnCanvasClickListener(onCanvasClickListener monCanvasClickListener)
    {
        this.monCanvasClickListener = monCanvasClickListener;
    }



    public onWidgetClickListener mOnWidgetClickListener;

    public interface onWidgetClickListener{
        void onWidgetClick(int index, int x, int y);
    }

    public void setOnWidgetClickListener(onWidgetClickListener mOnWidgetClickListener){
        this.mOnWidgetClickListener=mOnWidgetClickListener;
    }

    private int getDown2Widget() {
        for (int i = 0; i < mDrawableList.size(); i++) {
            int xcoords = mDrawableList.get(i).getXcoords();
            int ycoords = mDrawableList.get(i).getYcoords();
            double abs = Math.sqrt((DownX - xcoords) * (DownX - xcoords) + (DownY - ycoords) * (DownY - ycoords));
            //点落在控件内
            if (abs < ActionWidget.RADIUS) {
//                Log.i("everb","点下去的落在某点之上："+i);
                return i;
            }
        }
        return -1;
    }

    /**
     * 画布实现缩小的方法
     *
     * @return
     */
    public boolean zoomOut() {
//        if (mScrollable && mCurrentZoomScaleIndex > 0) {
        if (mCurrentZoomScaleIndex > 0) {
            updateScaleStep(mCurrentZoomScaleIndex - 1);
            return true;
        }
        return false;
    }


    /**
     * 画布实现放大的方法
     *
     * @return
     */
    public boolean zoomIn() {
        if (mCurrentZoomScaleIndex < ZOOM_SCALES.length - 1) {
            updateScaleStep(mCurrentZoomScaleIndex + 1);
            return true;
        }
        return false;
    }

    /**
     * 缩放的具体实现
     *
     * @param newScaleIndex
     */
    private void updateScaleStep(int newScaleIndex) {
        if (newScaleIndex != mCurrentZoomScaleIndex) {
            final float oldViewScale = mViewScale;

            mCurrentZoomScaleIndex = newScaleIndex;
            mViewScale = ZOOM_SCALES[mCurrentZoomScaleIndex];

            final float scaleDifference = mViewScale - oldViewScale;
            scrollBy((int) (scaleDifference * getMeasuredWidth() / 2),
                    (int) (scaleDifference * getMeasuredHeight() / 2));

            this.setScaleX(mViewScale);
            this.setScaleY(mViewScale);
            this.requestLayout();
        }
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
                getMeasuredSize(widthMeasureSpec, DESIRED_WIDTH),
                getMeasuredSize(heightMeasureSpec, DESIRED_HEIGHT));

    }

    private static int getMeasuredSize(int measureSpec, int desiredSize) {
        int mode = View.MeasureSpec.getMode(measureSpec);
        int size = View.MeasureSpec.getSize(measureSpec);

        if (mode == View.MeasureSpec.EXACTLY) {
            return size;
        } else if (mode == View.MeasureSpec.AT_MOST) {
            return Math.min(size, desiredSize);
        } else {
            return desiredSize;
        }

    }


    private class TapGestureListener implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
            Toast.makeText(mContext, "实现了长按手势", Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent motionEvent) {
            return false;
        }
    }

    private class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        // Focus point at the start of the pinch gesture. This is used for computing proper scroll
        // offsets during scaling, as well as for simultaneous panning.
        private float mStartFocusX;
        private float mStartFocusY;
        // View scale at the beginning of the gesture. This is used for computing proper scroll
        // offsets during scaling.
        private float mStartScale;
        // View scroll offsets at the beginning of the gesture. These provide the reference point
        // for adjusting scroll in response to scaling and panning.
        private int mStartScrollX;
        private int mStartScrollY;

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            mStartFocusX = detector.getFocusX();
            mStartFocusY = detector.getFocusY();
            mStartScrollX = getScrollX();
            mStartScrollY = getScrollY();

            mStartScale = mViewScale;
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            final float oldViewScale = mViewScale;

            final float scaleFactor = detector.getScaleFactor();
            mViewScale *= scaleFactor;

            if (mViewScale < ZOOM_SCALES[0]) {
                mCurrentZoomScaleIndex = 0;
                mViewScale = ZOOM_SCALES[mCurrentZoomScaleIndex];
            } else if (mViewScale > ZOOM_SCALES[ZOOM_SCALES.length - 1]) {
                mCurrentZoomScaleIndex = ZOOM_SCALES.length - 1;
                mViewScale = ZOOM_SCALES[mCurrentZoomScaleIndex];
            } else {
                // find nearest zoom scale
                float minDist = Float.MAX_VALUE;
                // If we reach the end the last one was the closest
                int index = ZOOM_SCALES.length - 1;
                for (int i = 0; i < ZOOM_SCALES.length; i++) {
                    float dist = Math.abs(mViewScale - ZOOM_SCALES[i]);
                    if (dist < minDist) {
                        minDist = dist;
                    } else {
                        // When it starts increasing again we've found the closest
                        index = i - 1;
                        break;
                    }
                }
                mCurrentZoomScaleIndex = index;
            }

          /*  if (shouldDrawGrid()) {
                mGridRenderer.updateGridBitmap(mViewScale);
            }*/

            CanvasView.this.setScaleX(mViewScale);
            CanvasView.this.setScaleY(mViewScale);

            // Compute scroll offsets based on difference between original and new scaling factor
            // and the focus point where the gesture started. This makes sure that the scroll offset
            // is adjusted to keep the focus point in place on the screen unless there is also a
            // focus point shift (see next scroll component below).
            final float scaleDifference = mViewScale - mStartScale;
            final int scrollScaleX = (int) (scaleDifference * mStartFocusX);
            final int scrollScaleY = (int) (scaleDifference * mStartFocusY);

            // Compute scroll offset based on shift of the focus point. This makes sure the view
            // pans along with the focus.
            final int scrollPanX = (int) (mStartFocusX - detector.getFocusX());
            final int scrollPanY = (int) (mStartFocusY - detector.getFocusY());

            // Apply the computed scroll components for scale and panning relative to the scroll
            // coordinates at the beginning of the gesture.
            scrollTo(mStartScrollX + scrollScaleX + scrollPanX,
                    mStartScrollY + scrollScaleY + scrollPanY);

            return true;
        }
    }
}
