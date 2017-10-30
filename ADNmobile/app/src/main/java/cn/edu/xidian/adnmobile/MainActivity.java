package cn.edu.xidian.adnmobile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import cn.edu.xidian.adnmobile.view.CBitmap;
import cn.edu.xidian.adnmobile.view.CDrawable;

public class MainActivity extends AppCompatActivity implements OnClickListener{

    private Button btn_dataCollect,btn_switch,btn_function;
    private ImageView iv_trash,iv_zoomin,iv_zoomout,iv_reset;
    private CanvasView mCanvasView,mCanvasDataCollect,mCanvasSwitch,mCanvasFunction;
    private Bitmap mBitmap;
    private CDrawable mPutTrash,mPutIcon;
    private int Widget_Width,Widget_heigh;

    private boolean IsLongPress = false;

    private CDrawable mGamePadBitmap;//临时变量

    //datacollect
    private int datacollect[] = {R.drawable.datacollect_1,R.drawable.datacollect_2,R.drawable.datacollect_3,R.drawable.datacollect_1,R.drawable.datacollect_2,R.drawable.datacollect_3,R.drawable.datacollect_1,R.drawable.datacollect_2,R.drawable.datacollect_3,R.drawable.datacollect_1,R.drawable.datacollect_2,R.drawable.datacollect_3,};
    //switch
    private int switchchoose[] = {R.drawable.switch_1,R.drawable.switch_2,R.drawable.function_1,R.drawable.function_1,};
    //function
    private int function[] = {R.drawable.function_1,R.drawable.function_1,R.drawable.function_1,};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GetWidgetWidthAndHeight();
        initView();
        initData();
        initEvent();
    }

    private void initView()
    {
        btn_dataCollect = (Button) findViewById(R.id.btn_dataCollect);
        btn_switch = (Button) findViewById(R.id.btn_switch);
        btn_function = (Button) findViewById(R.id.btn_function);
        mCanvasView = (CanvasView) findViewById(R.id.action_editor_canvas_test);
        mCanvasDataCollect = (CanvasView) findViewById(R.id.canvas_data);
        mCanvasSwitch = (CanvasView) findViewById(R.id.canvas_switch);
        mCanvasFunction = (CanvasView) findViewById(R.id.canvas_function);
        iv_trash = (ImageView) findViewById(R.id.iv_trash);
        iv_reset = (ImageView) findViewById(R.id.iv_reset);
        iv_zoomin = (ImageView) findViewById(R.id.iv_zoomin);
        iv_zoomout = (ImageView) findViewById(R.id.iv_zoomout);

    }

    private void initData()
    {
        mCanvasView.setBackgroundMode(CanvasView.BACKGROUND_STYLE_BLANK);
        mCanvasView.setBackgroundColor(Color.WHITE);

        mCanvasDataCollect.setBackgroundMode(CanvasView.BACKGROUND_STYLE_BLANK);
        mCanvasDataCollect.setBackgroundColor(Color.WHITE);

        mCanvasSwitch.setBackgroundMode(CanvasView.BACKGROUND_STYLE_BLANK);
        mCanvasSwitch.setBackgroundColor(Color.WHITE);

        mCanvasFunction.setBackgroundMode(CanvasView.BACKGROUND_STYLE_BLANK);
        mCanvasFunction.setBackgroundColor(Color.WHITE);

        //放置trash
        iv_trash.setImageResource(R.drawable.blockly_trash);
        //放置reset
        iv_reset.setImageResource(R.drawable.reset_view);
        //放置zoomin
        iv_zoomin.setImageResource(R.drawable.zoom_in);
        //放置zoomout
        iv_zoomout.setImageResource(R.drawable.zoom_out);
    }

    private void initEvent()
    {
        btn_dataCollect.setOnClickListener(this);
        btn_switch.setOnClickListener(this);
        btn_function.setOnClickListener(this);
        iv_reset.setOnClickListener(this);
        iv_zoomin.setOnClickListener(this);
        iv_zoomout.setOnClickListener(this);

//        mCanvasView.setOnCanvasClickListener(new CanvasView.onCanvasClickListener() {
//            @Override
//            public void onCanvasClick(int x, int y) {
//                if (x > 130) {
//                    Toast.makeText(MainActivity.this, "点击了画布", Toast.LENGTH_SHORT).show();
//                    mCanvasDataCollect.setVisibility(View.GONE);
//                    mCanvasSwitch.setVisibility(View.GONE);
//                    mCanvasFunction.setVisibility(View.GONE);
//                }
//            }
//        });

        mCanvasDataCollect.setOnWidgetMoveListener(new CanvasView.onWidgetMoveListener() {
            @Override
            public void onWidgetMove(int index, int x, int y) {
                Toast.makeText(MainActivity.this, "控件为" + index, Toast.LENGTH_SHORT).show();

            }
        });
        mCanvasDataCollect.setOnWidgetLongPressListener(new CanvasView.onWidgetLongPressListener() {
            @Override
            public void onWidgetLongPress(int index, int x, int y) {
                Toast.makeText(MainActivity.this, "长按了控件:" + index, Toast.LENGTH_SHORT).show();

                mCanvasDataCollect.setVisibility(View.GONE);//模块界面隐藏
                mBitmap = BitmapFactory.decodeResource(getResources(), datacollect[index]);
                mGamePadBitmap = new CBitmap(mBitmap, x, y);
                mCanvasView.addCanvasDrawable(mGamePadBitmap);//添加到绘制界面
                IsLongPress = true;
            }
        });
        mCanvasDataCollect.setOnWidgetClickListener(new CanvasView.onWidgetClickListener() {
            @Override
            public void onWidgetClick(int index, int x, int y) {
                Toast.makeText(MainActivity.this, "点击了控件:" + index, Toast.LENGTH_SHORT).show();
            }
        });

        mCanvasDataCollect.setOnWidgetUpListener(new CanvasView.onWidgetUpListener() {
            @Override
            public void onWidgetUp(int index, int x, int y) {
                if (IsLongPress) {
                    //手指移开时
                    mCanvasView.setVisibility(View.VISIBLE);    //设置第一层的画布为显示
                    mCanvasDataCollect.setVisibility(View.GONE);      //设置第二层的画布为隐藏

//                    //若控件在删除区，则删除第一层画布上的次控件
//                    if ((x > mXcoords && x < mXcoords + 250) && (y > mYcoords && y < mYcoords + 250)) {
//                        mCanvasView.mDrawableList.remove(index);
//                    }

                    IsLongPress = false;
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_dataCollect:
                mCanvasSwitch.setVisibility(View.GONE);
                mCanvasFunction.setVisibility(View.GONE);
                setIcon2Canvas(datacollect, mCanvasDataCollect);
                break;
            case R.id.btn_switch:
                mCanvasDataCollect.setVisibility(View.GONE);
                mCanvasFunction.setVisibility(View.GONE);
                setIcon2Canvas(switchchoose,mCanvasSwitch);
                break;
            case R.id.btn_function:
                mCanvasSwitch.setVisibility(View.GONE);
                mCanvasDataCollect.setVisibility(View.GONE);
                setIcon2Canvas(function,mCanvasFunction);
                break;
            case R.id.iv_reset:
                iv_reset.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("click reset>>>>>>>>>>>");
                        mCanvasView.resetView();
                    }
                });
                break;
            case R.id.iv_zoomin:
                iv_zoomin.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("click zoomin>>>>>>>>>>>");
                        mCanvasView.zoomIn();
                    }
                });
                break;
            case R.id.iv_zoomout:
                iv_zoomout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("click zoomout>>>>>>>>>>>");
                        mCanvasView.zoomOut();
                    }
                });
                break;
            default:
                break;
        }
    }

    private void setIcon2Canvas(int[] icon,CanvasView canvasView)
    {
        canvasView.setBackgroundMode(CanvasView.BACKGROUND_STYLE_BLANK);
        canvasView.setBackgroundColor(Color.LTGRAY);
        canvasView.setVisibility(View.VISIBLE);
        for (int i = 0;i < icon.length;i++)
        {
            mBitmap = BitmapFactory.decodeResource(getResources(), icon[i]);
            mPutIcon = new CBitmap(mBitmap, 0,i*210 );
            canvasView.addCanvasDrawable(mPutIcon);
        }
    }

    //获取屏幕的大小
    private void GetWidgetWidthAndHeight()
    {
        WindowManager wm = this.getWindowManager();
        Widget_Width = wm.getDefaultDisplay().getWidth();
        Widget_heigh = wm.getDefaultDisplay().getHeight();
    }
}
