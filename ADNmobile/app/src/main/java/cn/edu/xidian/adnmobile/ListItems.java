package cn.edu.xidian.adnmobile;

import android.content.Context;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017/10/30.
 */
public class ListItems {

    private Context context = null;
    private ListView listView;
    private int flag;
//    public  ItemBase[][] datasave = {
//            //datacollect
//            {new ItemBase("时钟","获取当前的时间",R.drawable.datacollect_clock),
//            new ItemBase( "环境湿度","获取环境当前的湿度",R.drawable.datacollect_humidity),
//            new ItemBase("光敏","获取环境当前的光线强度",R.drawable.datacollect_lightsensor),
//            new ItemBase("环境温度","获取环境当前的温度",R.drawable.datacollect_temperature),
//            new ItemBase("土壤湿度","获取土壤湿度",R.drawable.datacollect_soilwet),
//            new ItemBase("水深","获取当前水深",R.drawable.datacollect_waterdeep)},
//
//            //switch
//            {new ItemBase("开关","设置阈值以控制通断",R.drawable.switch_relay)},
//
//            //function
//            {new ItemBase("灯泡"," ",R.drawable.function_bulb),
//            new ItemBase("风扇"," ",R.drawable.function_fan),
//            new ItemBase("空调"," ",R.drawable.function_aircondition),
//            new ItemBase("家庭音响"," ",R.drawable.function_sound)}};

    public  ItemBase[][] datasave = {
            //datacollect
            {new ItemBase("时钟","获取当前的时间",R.drawable.datacollect_clock,"Clock"),
                    new ItemBase( "环境湿度","获取环境当前的湿度",R.drawable.datacollect_humidity,"Humidity"),
                    new ItemBase("光敏传感器","获取环境当前的光线强度",R.drawable.datacollect_lightsensor,"Light_sensitive"),
                    new ItemBase("环境温度","获取环境当前的温度",R.drawable.datacollect_temperature,"Temperature"),
                    new ItemBase("土壤湿度","获取土壤湿度",R.drawable.datacollect_soilwet,"Soilwet"),
                    new ItemBase("水深传感器","获取当前水深",R.drawable.datacollect_waterdeep,"Waterdeep")},

            //switch
            {new ItemBase("switch","设置阈值以控制通断",R.drawable.switch_relay,"switch")},

            //function
            {new ItemBase("灯泡"," ",R.drawable.function_bulb,"Bulb"),
                    new ItemBase("风扇"," ",R.drawable.function_fan,"Fan"),
                    new ItemBase("空调"," ",R.drawable.function_aircondition,"Aircondition"),
                    new ItemBase("家庭音响"," ",R.drawable.function_sound,"Sound")}};



    public ListItems(Context context,ListView listView,int flag){
        this.context = context;
        this.listView = listView;
        this.flag  = flag;
    }

    public void listshow()
    {
        SimpleAdapter adapter = new SimpleAdapter(context,getData(),R.layout.list_cell,
                new String[]{"img","Name","Details"},
                new int[]{R.id.iv_iconcell,R.id.tv_itemName,R.id.tv_itemDetails});
        listView.setAdapter(adapter);
    }

    private List<Map<String,Object>> getData()
    {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        for (int i = 0; i < datasave[flag-1].length;i++)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("Name", datasave[flag-1][i].Name);
            map.put("Details", datasave[flag-1][i].Details);
            map.put("img", datasave[flag-1][i].icon);
            list.add(map);
        }

        return list;
    }

    public ItemBase[][] getDatasave() {
        return datasave;
    }
}
