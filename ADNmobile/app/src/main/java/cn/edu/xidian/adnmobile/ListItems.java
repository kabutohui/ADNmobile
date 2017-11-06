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

    public ItemBase[] datacollect = {new ItemBase("时钟","获取当前的时间",R.drawable.datacollect_clock),
                                     new ItemBase( "环境湿度","获取环境当前的湿度",R.drawable.datacollect_humidity),
                                     new ItemBase("光敏","获取环境当前的光线强度",R.drawable.datacollect_lightsensor),
                                     new ItemBase("环境温度","获取环境当前的温度",R.drawable.datacollect_temperature),
                                     new ItemBase("土壤湿度","获取土壤湿度",R.drawable.datacollect_soilwet),
                                     new ItemBase("水深","获取当前水深",R.drawable.datacollect_waterdeep)};

    public ItemBase[] switchchoose = {new ItemBase("开关","设置阈值以控制通断",R.drawable.switch_relay)};

    public ItemBase[] functions = {new ItemBase("灯泡"," ",R.drawable.function_bulb),
                                    new ItemBase("风扇"," ",R.drawable.function_fan),
                                    new ItemBase("空调"," ",R.drawable.function_aircondition),
                                    new ItemBase("家庭音响"," ",R.drawable.function_sound),};


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
        ItemBase[] itemBases = null;
        switch(flag)
        {
            case ActionWidget.datacollect_flag:
                itemBases = datacollect;
                break;
            case ActionWidget.switch_flag:
                itemBases = switchchoose;
                break;
            case ActionWidget.function_flag:
                itemBases = functions;
                break;
        }
        for (int i = 0; i < itemBases.length;i++)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("Name", itemBases[i].Name);
            map.put("Details", itemBases[i].Details);
            map.put("img", itemBases[i].icon);
            list.add(map);
        }

        return list;
    }
}
