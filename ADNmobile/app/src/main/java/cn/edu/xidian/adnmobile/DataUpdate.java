package cn.edu.xidian.adnmobile;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import cn.edu.xidian.adnmobile.view.CBitmap;

/**
 * Created by lenovo on 2017/11/14.
 */
public class DataUpdate {

    private Context context;
    private static MyThread myThread;
    private TextView tv_datacollect,tv_function;
    private String datacollectName,functionName;
    private String Threshold; //获取设置的阈值
    private int itemID;
    private CanvasView mCanvasView;
    private CBitmap mGamePadBitmap;//临时变量
    private boolean ThreadFlag = false;

    public DataUpdate (Context context,TextView tv_datacollect,TextView tv_function,CanvasView mCanvasView){
        this.context = context;
        this.tv_datacollect = tv_datacollect;
        this.tv_function = tv_function;
        this.mCanvasView = mCanvasView;

        this.myThread = new MyThread();
        ThreadFlag = true;
        this.myThread.start();

        for(int i = 0;i < mCanvasView.mDrawableList.size();i++)
        {
            mGamePadBitmap = (CBitmap) (mCanvasView.mDrawableList.get(i));
            switch (mGamePadBitmap.Item_Attributes)
            {
                case ActionWidget.datacollect_flag:
                    datacollectName = mGamePadBitmap.itemCNName;
                    break;
                case ActionWidget.function_flag:
                    functionName = mGamePadBitmap.itemCNName;
                    break;
                case ActionWidget.switch_flag:
                    Threshold = mGamePadBitmap.setThresholdValue;
                    break;
            }
        }
    }


    public void setmCanvasView(CanvasView mCanvasView) {
        this.mCanvasView = mCanvasView;
        for(int i = 0;i < mCanvasView.mDrawableList.size();i++)
        {
            mGamePadBitmap = (CBitmap) (mCanvasView.mDrawableList.get(i));
            switch (mGamePadBitmap.Item_Attributes)
            {
                case ActionWidget.datacollect_flag:
                    datacollectName = mGamePadBitmap.itemCNName;
                    break;
                case ActionWidget.function_flag:
                    functionName = mGamePadBitmap.itemCNName;
                    break;
                case ActionWidget.switch_flag:
                    Threshold = mGamePadBitmap.setThresholdValue;
                    break;
            }
        }
    }

    public void Threadstart(){
        this.myThread.start();
    }

    public void setThreadFlag(boolean threadFlag) {
        ThreadFlag = threadFlag;
    }

    private class MyThread extends Thread {
        @Override
        public void run() {
            while (ThreadFlag) {
                System.out.println("发送获取传感器数据请求");
                try {
                    // 每个500毫秒向服务器发送一次请求
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //下面写请求服务器的代码
                new NetConnection(ActionWidget.SERVER_URL_GET, HttpMethod.GET, new NetConnection.SuccessCallback() {
                    @Override
                    public void onSuccess(String result) {
                        JsonDataPakage jsonDataPakage = new JsonDataPakage();
                        System.out.println(result);
                        tv_datacollect.setText(datacollectName+":"+jsonDataPakage.JsonParse(result));
                        if (Float.parseFloat(Threshold) <=  Float.parseFloat(jsonDataPakage.JsonParse(result)))
                        {
                            tv_function.setText(functionName+":"+"开");
                            tv_function.setTextColor(Color.parseColor("#66ff00"));//打开为绿色
                        }else{
                            tv_function.setText(functionName+":"+"关");
                            tv_function.setTextColor(Color.parseColor("#ff3300"));//打开为红色
                        }

                    }
                }, new NetConnection.FailCallback() {
                    @Override
                    public void onFail() {

                    }
                });
            }
        }
    }
}

