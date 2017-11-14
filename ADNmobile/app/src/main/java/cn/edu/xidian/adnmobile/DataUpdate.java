package cn.edu.xidian.adnmobile;

import android.content.Context;
import android.widget.TextView;

/**
 * Created by lenovo on 2017/11/14.
 */
public class DataUpdate {

    private Context context;
    private MyThread myThread;
    private TextView textView;
    private String itemName;
    private int itemID;


    public DataUpdate (Context context,TextView textView,String itemName){
        this.textView = textView;
        this.context = context;
        this.itemName = itemName;
        this.myThread = new MyThread();
        this.myThread.start();
    }

    private class MyThread extends Thread {
        @Override
        public void run() {
            while (true) {
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
                        textView.setText(itemName+": "+jsonDataPakage.JsonParse(result));

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

