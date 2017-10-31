package cn.edu.xidian.adnmobile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import cn.edu.xidian.adnmobile.view.CBitmap;
import cn.edu.xidian.adnmobile.view.CDrawable;

public class MainActivity extends AppCompatActivity implements OnClickListener{

    private Button btn_dataCollect,btn_switch,btn_function;
    private ImageView iv_trash,iv_zoomin,iv_zoomout,iv_reset;
    private CanvasView mCanvasView;
    private ListView lv_datacollect,lv_switch,lv_function;
    private Bitmap mBitmap;
    private CDrawable mPutTrash,mPutIcon;
    private int Widget_Width,Widget_heigh;

    private boolean IsLongPress = false;
    //是否进入删除区标志
    private boolean IsDeleteFlag = false;

    private CBitmap mGamePadBitmap;//临时变量

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


        mCanvasView.setOnCanvasClickListener(new CanvasView.onCanvasClickListener() {
            @Override
            public void onCanvasClick(int x, int y) {
                //点击控件列表外的部分以关闭控件列表
                if (x > 130) {
                    lv_function.setVisibility(View.GONE);
                    lv_switch.setVisibility(View.GONE);
                    lv_datacollect.setVisibility(View.GONE);
                }
            }
        });

        mCanvasView.setOnWidgetMoveListener(new CanvasView.onWidgetMoveListener() {
            @Override
            public void onWidgetMove(int index, int x, int y) {
                Toast.makeText(MainActivity.this, "控件为：" + index + "拖动的X坐标为：" + x + "拖动的X坐标为：" + y+"["+Widget_Width+","+Widget_heigh+"]", Toast.LENGTH_SHORT).show();
                //判断是否进入删除区
                if ((Widget_Width - 200 <= x && x <= Widget_Width) && (Widget_heigh - 200 <= y && y <= Widget_heigh)) {
                    //更换trash图标,trash图标切换为打开状态
                    iv_trash.setImageResource(R.drawable.blockly_trash_open);
                    //将标志位设置为true
                    IsDeleteFlag = true;
                } else {
                    //trash图标切回正常状态
                    iv_trash.setImageResource(R.drawable.blockly_trash);
                    IsDeleteFlag = false;
                }
            }
        });

        mCanvasView.setOnWidgetLongPressListener(new CanvasView.onWidgetLongPressListener() {
            @Override
            public void onWidgetLongPress(int index, int x, int y) {
                Toast.makeText(MainActivity.this, "长按了控件:" + index, Toast.LENGTH_SHORT).show();

            }
        });

        mCanvasView.setOnWidgetClickListener(new CanvasView.onWidgetClickListener() {
            @Override
            public void onWidgetClick(int index, int x, int y) {
                Toast.makeText(MainActivity.this, "点击了控件:" + index, Toast.LENGTH_SHORT).show();


            }
        });

        mCanvasView.setOnWidgetUpListener(new CanvasView.onWidgetUpListener() {
            @Override
            public void onWidgetUp(int index, int x, int y) {
                if (IsDeleteFlag) {
                    IsDeleteFlag = false;
                    mCanvasView.mDrawableList.remove(index);
                    //trash图标切回正常状态
                    iv_trash.setImageResource(R.drawable.blockly_trash);
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
                        System.out.println("click zoomin>>>>>>> >>>>");
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

    private void setIcon2List(final int[] icon, final ListView listView)
    {
        listView.setVisibility(View.VISIBLE);
        ListItems listItems = new ListItems(this,listView,icon);
        listItems.listshow();
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "you clicked the" + position, Toast.LENGTH_SHORT).show();
                mCanvasView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                mBitmap = BitmapFactory.decodeResource(getResources(), icon[position]);
                mGamePadBitmap = new CBitmap(mBitmap, 200, 200);
                mCanvasView.addCanvasDrawable(mGamePadBitmap);
                return true;
            }
        });
    }

    //获取屏幕的大小
    private void GetWidgetWidthAndHeight()
    {
        WindowManager wm = this.getWindowManager();
        Widget_Width = wm.getDefaultDisplay().getWidth();
        Widget_heigh = wm.getDefaultDisplay().getHeight();
    }
}
