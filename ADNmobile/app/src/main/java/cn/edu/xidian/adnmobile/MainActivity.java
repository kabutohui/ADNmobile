package cn.edu.xidian.adnmobile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.xidian.adnmobile.view.CBitmap;
import cn.edu.xidian.adnmobile.view.CDrawable;

public class MainActivity extends AppCompatActivity implements OnClickListener{

    private Toolbar toolbar;
    private Button btn_dataCollect,btn_switch,btn_function;
    private ImageView iv_trash,iv_zoomin,iv_zoomout,iv_reset,iv_function,iv_switch,iv_datacollect,iv_rectangle1,iv_rectangle2;
    private CanvasView mCanvasView;
    private ListView lv_datacollect,lv_switch,lv_function;
    private TextView tv_datacollect,tv_switch,tv_function;
    private Bitmap mBitmap;
    private CDrawable mPutTrash,mPutIcon;
    private int Widget_Width,Widget_heigh;

    private String dataCollectName;

    private boolean IsLongPress = false;
    //是否进入删除区标志
    private boolean IsDeleteFlag = false;

    private CBitmap mGamePadBitmap;//临时变量


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        GetWidgetWidthAndHeight();
        initView();
        initData();
        initEvent();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    //监听菜单栏事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_line:
                Toast.makeText(this, "添加连线", Toast.LENGTH_SHORT).show();
                break;
            case R.id.create:
                if(mCanvasView.mDrawableList.size() >= 4)
                    Toast.makeText(this, "存在多余控件，生成模块失败！", Toast.LENGTH_LONG).show();
                else
                {
                    Toast.makeText(this, "生成模块成功！", Toast.LENGTH_LONG).show();

                    //关闭前一个线程
                    ActionWidget.ThreadFlag = false;

                    JsonDataPakage jsonDataPakage = new JsonDataPakage(mCanvasView.mDrawableList);

                    new NetConnection(ActionWidget.SERVER_URL_POST, HttpMethod.POST, new NetConnection.SuccessCallback() {
                        @Override
                        public void onSuccess(String result) {
                            System.out.println("<<<<<<<<<<<<数据传输成功！Requrst = "+result);
                            //dataUpdate = new DataUpdate(MainActivity.this,tv_datacollect,tv_function,mCanvasView);
                            new DataUpdate(MainActivity.this,tv_datacollect,tv_function,mCanvasView);
                        }
                    }, new NetConnection.FailCallback() {
                        @Override
                        public void onFail() {
                            System.out.println("<<<<<<<<<<<<数据传输失败！");
                        }
                    },"test",jsonDataPakage.JsonPackage());

                }

                break;
            case R.id.upload:
                Toast.makeText(this, "上传模块", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    private void initView()
    {
        tv_datacollect = (TextView) findViewById(R.id.tv_dataCollect);
        tv_switch = (TextView) findViewById(R.id.tv_switch);
        tv_function = (TextView) findViewById(R.id.tv_funciton);

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


        iv_function = (ImageView) findViewById(R.id.iv_function);
        iv_rectangle1 = (ImageView) findViewById(R.id.iv_rectangle1);
        iv_switch = (ImageView) findViewById(R.id.iv_switch);
        iv_rectangle2 = (ImageView) findViewById(R.id.iv_rectangle2);
        iv_datacollect = (ImageView) findViewById(R.id.iv_datacollect);

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


        //放置工作区
        iv_function.setImageResource(R.drawable.rectangle);
        iv_rectangle1.setImageResource(R.drawable.direction_up);
        iv_switch.setImageResource(R.drawable.rectangle);
        iv_rectangle2.setImageResource(R.drawable.direction_up);
        iv_datacollect.setImageResource(R.drawable.rectangle);
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
                Toast.makeText(MainActivity.this, "控件为：" + index + "拖动的X坐标为：" + x + "拖动的X坐标为：" + y + "[" + Widget_Width + "," + Widget_heigh + "]", Toast.LENGTH_SHORT).show();
                mGamePadBitmap = (CBitmap) (mCanvasView.mDrawableList.get(index));
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
                mGamePadBitmap = (CBitmap) (mCanvasView.mDrawableList.get(index));
                if (mGamePadBitmap.Item_Attributes == ActionWidget.switch_flag) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    // 通过LayoutInflater来加载一个xml的布局文件作为一个View对象
                    View view = LayoutInflater.from(MainActivity.this).inflate(
                            R.layout.popup_view, null);
                    // 设置我们自己定义的布局文件作为弹出框的Content
                    builder.setView(view);

                    //这个位置十分重要，只有位于这个位置逻辑才是正确的
                    final AlertDialog dialog = builder.show();

                    final EditText et_Threshold = view.findViewById(R.id.edThreshold);
                    et_Threshold.setText(mGamePadBitmap.setThresholdValue);

                    view.findViewById(R.id.btn_confirm).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //确认
                            //获取输入的阈值
                            mGamePadBitmap.setThresholdValue = et_Threshold.getText().toString();
                            //当前阈值;

                            tv_switch.setText("当前阈值:" + mGamePadBitmap.setThresholdValue);
                            //关闭对话框
                            dialog.dismiss();
                        }
                    });
                    view.findViewById(R.id.btn_openNow).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //立即启动+关闭对话框
                            tv_switch.setText("开关状态：开");
                            tv_switch.setTextColor(Color.parseColor("#66ff00"));
                            dialog.dismiss();
                        }
                    });
                    view.findViewById(R.id.btn_cancle).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //取消+关闭对话框
                            dialog.dismiss();
                        }
                    });
                }
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

                //判断图标是否出现在指定的功能区
                if (index >= 0) {
                    if ((mGamePadBitmap.Item_Attributes == ActionWidget.function_flag)) {
                        double abs = Math.sqrt((x - ActionWidget.functionX) * (x - ActionWidget.functionX) + (y - ActionWidget.functionY1) * (y - ActionWidget.functionY1));
                        if (abs < ActionWidget.function_reius) {
                            mGamePadBitmap.setXcoords(ActionWidget.functionX);
                            mGamePadBitmap.setYcoords(ActionWidget.functionY1);
                        }
                    }
                    if ((mGamePadBitmap.Item_Attributes == ActionWidget.switch_flag)) {
                        double abs = Math.sqrt((x - ActionWidget.functionX) * (x - ActionWidget.functionX) + (y - ActionWidget.functionY2) * (y - ActionWidget.functionY2));
                        if (abs < ActionWidget.function_reius) {
                            mGamePadBitmap.setXcoords(ActionWidget.functionX);
                            mGamePadBitmap.setYcoords(ActionWidget.functionY2);
                        }
                    }
                    if ((mGamePadBitmap.Item_Attributes == ActionWidget.datacollect_flag)) {
                        double abs = Math.sqrt((x - ActionWidget.functionX) * (x - ActionWidget.functionX) + (y - ActionWidget.functionY3) * (y - ActionWidget.functionY3));
                        if (abs < ActionWidget.function_reius) {
                            mGamePadBitmap.setXcoords(ActionWidget.functionX);
                            mGamePadBitmap.setYcoords(ActionWidget.functionY3);
                        }
                    }

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
                setIcon2List(ActionWidget.datacollect_flag,lv_datacollect);
                break;
            case R.id.btn_switch:
                lv_datacollect.setVisibility(View.GONE);
                lv_function.setVisibility(View.GONE);
                setIcon2List(ActionWidget.switch_flag,lv_switch);
                break;
            case R.id.btn_function:
                lv_datacollect.setVisibility(View.GONE);
                lv_switch.setVisibility(View.GONE);
                setIcon2List(ActionWidget.function_flag,lv_function);
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

    private void setIcon2List(final int flag, final ListView listView)
    {
        listView.setVisibility(View.VISIBLE);
        ListItems listItems = new ListItems(this,listView,flag);
        listItems.listshow();
        final ItemBase[][] datasave = listItems.getDatasave();

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "you clicked the" + position, Toast.LENGTH_SHORT).show();
                mCanvasView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                mBitmap = BitmapFactory.decodeResource(getResources(), datasave[flag-1][position].icon);
                mGamePadBitmap = new CBitmap(mBitmap, 200, 200);
                mGamePadBitmap.Item_Attributes = flag;
                mGamePadBitmap.itemName = datasave[flag-1][position].EngName;
                mGamePadBitmap.itemCNName = datasave[flag-1][position].Name;
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
