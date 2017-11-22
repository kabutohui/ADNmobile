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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.xidian.adnmobile.view.CBitmap;

public class MainActivity extends AppCompatActivity implements OnClickListener{

    private Toolbar toolbar;

    private ImageView iv_trash,iv_zoomin,iv_zoomout,iv_reset,iv_function,iv_switch,iv_datacollect,iv_rectangle1,iv_rectangle2;
    private CanvasView mCanvasView;
    private ListView listdata;
    private TextView tv_datacollectValue,tv_switchValue,tv_functionValue,tv_datacollectQos,tv_switchQos,tv_functionQos;
    private Bitmap mBitmap;
    private int Widget_Width,Widget_heigh;

    //更新数据
    private DataUpdate dataUpdate = null;
    //是否进入删除区标志
    private boolean IsDeleteFlag = false;

    private CBitmap mGamePadBitmap;//临时变量


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//实现全屏

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("西安电子科技大学-ADN演示平台");
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
                Toast.makeText(this, "清除", Toast.LENGTH_SHORT).show();
                if(dataUpdate != null){
                    dataUpdate.setThreadFlag(false);
                }
                mCanvasView.cleanPager();
                break;
            case R.id.create:
                if(mCanvasView.mDrawableList.size() >= 4)
                    Toast.makeText(this, "存在多余控件，生成模块失败！", Toast.LENGTH_LONG).show();
                else if(mCanvasView.mDrawableList.size() < 2){
                    Toast.makeText(this, "控件不足，生成模块失败！", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(this, "生成模块成功！", Toast.LENGTH_LONG).show();
                    //发送数据
                    JsonPakageandSendMsg(ActionWidget.SERVER_URL_POST,"data");
                }
                break;
            case R.id.upload:
                Toast.makeText(this, "停止运行", Toast.LENGTH_SHORT).show();
                if(dataUpdate != null) {
                    dataUpdate.setThreadFlag(false);
                }
                JsonPakageandSendMsg(ActionWidget.SERVER_URL_STOP,"stop");
                break;
            case R.id.about:
                Toast.makeText(this, "西安电子科技大学-ADN团队", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

    private void initView()
    {
        //文字显示部分
        tv_datacollectQos = (TextView) findViewById(R.id.tv_dataCollectQos);
        tv_datacollectValue = (TextView) findViewById(R.id.tv_dataCollectValue);
        tv_switchQos = (TextView) findViewById(R.id.tv_switchQos);
        tv_switchValue = (TextView) findViewById(R.id.tv_switchValue);
        tv_functionQos = (TextView) findViewById(R.id.tv_functionQos);
        tv_functionValue = (TextView) findViewById(R.id.tv_functionValue);
        //列表显示
        listdata = (ListView) findViewById(R.id.list);
        //画布
        mCanvasView = (CanvasView) findViewById(R.id.action_editor_canvas_test);
        //四个画布控件：放大/缩小/还原/删除
        iv_trash = (ImageView) findViewById(R.id.iv_trash);
        iv_reset = (ImageView) findViewById(R.id.iv_reset);
        iv_zoomin = (ImageView) findViewById(R.id.iv_zoomin);
        iv_zoomout = (ImageView) findViewById(R.id.iv_zoomout);
        //中心功能区5个图标显示
        iv_function = (ImageView) findViewById(R.id.iv_functionrectangle);
        iv_rectangle1 = (ImageView) findViewById(R.id.iv_direction1);
        iv_switch = (ImageView) findViewById(R.id.iv_switchrectangle);
        iv_rectangle2 = (ImageView) findViewById(R.id.iv_direction2);
        iv_datacollect = (ImageView) findViewById(R.id.iv_datacollectrectangle);

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
        iv_function.setImageResource(R.drawable.function_rectangle);
        iv_rectangle1.setImageResource(R.drawable.direction_right);
        iv_switch.setImageResource(R.drawable.switch_rectangle);
        iv_rectangle2.setImageResource(R.drawable.direction_right);
        iv_datacollect.setImageResource(R.drawable.datacollect_rectangle);
    }

    private void initEvent()
    {
        setIcon2List(listdata);
        iv_reset.setOnClickListener(this);
        iv_zoomin.setOnClickListener(this);
        iv_zoomout.setOnClickListener(this);


        mCanvasView.setOnWidgetMoveListener(new CanvasView.onWidgetMoveListener() {
            @Override
            public void onWidgetMove(int index, int x, int y) {
                Toast.makeText(MainActivity.this, "控件为：" + index + "拖动的X坐标为：" + x + "拖动的Y坐标为：" + y + "[" + Widget_Width + "," + Widget_heigh + "]", Toast.LENGTH_SHORT).show();
                mGamePadBitmap = (CBitmap) (mCanvasView.mDrawableList.get(index));
                //判断是否进入删除区
                if ((Widget_Width - 400 <= x && x <= Widget_Width) && (Widget_heigh - 400 <= y && y <= Widget_heigh)) {
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

            }
        });

        mCanvasView.setOnWidgetClickListener(new CanvasView.onWidgetClickListener() {
            @Override
            public void onWidgetClick(int index, int x, int y) {

                mGamePadBitmap = (CBitmap) (mCanvasView.mDrawableList.get(index));
                if (mGamePadBitmap.Item_Attributes == ActionWidget.switch_flag) {
                    if (ActionWidget.clickCount == 0) {
                        ActionWidget.firstTime = System.currentTimeMillis();
                        ActionWidget.clickCount++;
                    } else {
                        ActionWidget.secondTime = System.currentTimeMillis();
                        System.out.println("second:" + ActionWidget.secondTime + "\n" +
                                "first:" + ActionWidget.firstTime + "\n" +
                                "second-first=" + Math.abs(ActionWidget.secondTime - ActionWidget.firstTime) + "点击了开关！！！");
                        if (Math.abs(ActionWidget.secondTime - ActionWidget.firstTime) < 1500) {
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
                                    tv_switchValue.setText("当前阈值:" + mGamePadBitmap.setThresholdValue);
                                    //关闭对话框
                                    dialog.dismiss();
                                }
                            });
                            view.findViewById(R.id.btn_openNow).setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //立即启动+关闭对话框
                                    tv_switchValue.setText("开关状态：开");
                                    tv_switchValue.setTextColor(Color.parseColor("#66ff00"));
                                    if (dataUpdate != null) {
                                        dataUpdate.setThreadFlag(false);
                                    }
                                    JsonPakageandSendMsg(ActionWidget.SERVER_URL_POST, "immediate");
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
                        ActionWidget.clickCount = 0;
                    }
                }

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
                        double abs = Math.sqrt((y - ActionWidget.functionX) * (y - ActionWidget.functionX) + (x - ActionWidget.functionY3) * (x - ActionWidget.functionY3));
                        if (abs < ActionWidget.function_reius) {
                            mGamePadBitmap.setXcoords(ActionWidget.functionY3);
                            mGamePadBitmap.setYcoords(ActionWidget.functionX);
                        }
                    }
                    if ((mGamePadBitmap.Item_Attributes == ActionWidget.switch_flag)) {
                        double abs = Math.sqrt((y - ActionWidget.functionX) * (y - ActionWidget.functionX) + (x - ActionWidget.functionY2) * (x - ActionWidget.functionY2));
                        if (abs < ActionWidget.function_reius) {
                            mGamePadBitmap.setXcoords(ActionWidget.functionY2);
                            mGamePadBitmap.setYcoords(ActionWidget.functionX);
                        }
                    }
                    if ((mGamePadBitmap.Item_Attributes == ActionWidget.datacollect_flag)) {
                        double abs = Math.sqrt((y - ActionWidget.functionX) * (y - ActionWidget.functionX) + (x - ActionWidget.functionY1) * (x - ActionWidget.functionY1));
                        if (abs < ActionWidget.function_reius) {
                            mGamePadBitmap.setXcoords(ActionWidget.functionY1);
                            mGamePadBitmap.setYcoords(ActionWidget.functionX);
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

    private void setIcon2List(final ListView listView)
    {
        listView.setVisibility(View.VISIBLE);
        ListItems listItems = new ListItems(this,listView);
        listItems.listshow();
        final ItemBase[] datasave = listItems.getDatasave();

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "you clicked the" + position, Toast.LENGTH_SHORT).show();
                mBitmap = BitmapFactory.decodeResource(getResources(), datasave[position].icon);
                mGamePadBitmap = new CBitmap(mBitmap, 100, 130);
                mGamePadBitmap.Item_Attributes = datasave[position].itemFlag;
                mGamePadBitmap.itemName = datasave[position].EngName;
                mGamePadBitmap.itemCNName = datasave[position].Name;
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

    //打包和发送数据
    private void JsonPakageandSendMsg(String Url, final String dataCategory){
        JsonDataPakage jsonDataPakage = new JsonDataPakage(mCanvasView.mDrawableList);

        new NetConnection(Url, HttpMethod.POST, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                System.out.println("<<<<<<<<<<<<数据传输成功！Requrst = "+result);
                if(dataCategory == "stop") {
                    System.out.println(">>>>>>>>>>>>>>>>STOP RUN NOW!!!>>>>>>>>>>>>>");
                }else if(dataCategory == "data")
                {
                    if (dataUpdate == null) {
                        dataUpdate = new DataUpdate(MainActivity.this, tv_datacollectValue, tv_functionValue, mCanvasView);
                    } else {
                        dataUpdate.setmCanvasView(mCanvasView);
                        dataUpdate.setThreadFlag(true);
                        dataUpdate.Threadstart();
                    }
                }else{
                    //立即启动成功响应代码

                }
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                System.out.println("<<<<<<<<<<<<数据传输失败！");
            }
        },dataCategory,jsonDataPakage.JsonPackage());
    }
}
