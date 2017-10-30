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
    private int[] icon;


    public ListItems(Context context,ListView listView,int[] icon){
        this.context = context;
        this.listView = listView;
        this.icon = icon;
    }

    public void listshow()
    {
        SimpleAdapter adapter = new SimpleAdapter(context,getData(),R.layout.list_cell,
                new String[]{"img"},
                new int[]{R.id.iv_iconcell});
        listView.setAdapter(adapter);
    }

    private List<Map<String,Object>> getData()
    {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < icon.length;i++)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("img", icon[i]);
            list.add(map);
        }

        return list;
    }
}
