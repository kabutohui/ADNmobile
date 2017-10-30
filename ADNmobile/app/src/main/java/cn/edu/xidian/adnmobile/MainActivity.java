package cn.edu.xidian.adnmobile;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import cn.edu.xidian.adnmobile.view.CDrawable;

public class MainActivity extends AppCompatActivity implements OnClickListener{

    private Button btn_dataCollect,btn_switch,btn_function;
    private ImageView iv_trash,iv_zoomin,iv_zoomout,iv_reset;
    private CanvasView mCanvasView,mCanvasDataCollect,mCanvasSwitch,mCanvasFunction;
    private ListView lv_datacollect,lv_switch,lv_function;
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
        lv_datacollect = (ListView) findViewById(R.id.list_data);
        lv_switch = (ListView) findViewById(R.id.list_switch);
        lv_function = (ListView) findViewById(R.id.list_function);
        iv_trash = (ImageView) findViewById(R.id.iv_trash);
        iv_reset = (ImageView) findViewById(R.id.iv_reset);
        iv_zoomin = (ImageView) findViewById(R.id.iv_zoomin);
        iv_zoomout = (ImageView) findViewById(R.id.iv_zoomout);
    }

    private void initData()
    {
        mCanvasView.setBackgroundMode(CanvasView.BACKGROUND_STYLE_BLANK);
        mCanvasView.setBackgroundColor(Color.WHITE);

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
            public void onCanvasClick(int x, int y) {
                if (x > 130) {
                    Toast.makeText(MainActivity.this, "点击了画布", Toast.LENGTH_SHORT).show();
                    lv_function.setVisibility(View.GONE);
                    lv_switch.setVisibility(View.GONE);
                    lv_datacollect.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_dataCollect:
                lv_function.setVisibility(View.GONE);
                lv_switch.setVisibility(View.GONE);
                setIcon2List(datacollect, lv_datacollect);
                lv_datacollect.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(MainActivity.this, "you clicked the"+position, Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                break;
            case R.id.btn_switch:
                lv_datacollect.setVisibility(View.GONE);
                lv_function.setVisibility(View.GONE);
                setIcon2List(switchchoose, lv_switch);
                break;
            case R.id.btn_function:
                lv_datacollect.setVisibility(View.GONE);
                lv_switch.setVisibility(View.GONE);
                setIcon2List(function, lv_function);
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

    private void setIcon2List(int[] icon,ListView listView)
    {
        listView.setVisibility(View.VISIBLE);
        ListItems listItems = new ListItems(this,listView,icon);
        listItems.listshow();
    }

    //获取屏幕的大小
    private void GetWidgetWidthAndHeight()
    {
        WindowManager wm = this.getWindowManager();
        Widget_Width = wm.getDefaultDisplay().getWidth();
        Widget_heigh = wm.getDefaultDisplay().getHeight();
    }
}
