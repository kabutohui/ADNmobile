package cn.edu.xidian.adnmobile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.xidian.adnmobile.view.CBitmap;
import cn.edu.xidian.adnmobile.view.CDrawable;

/**
 * Created by lenovo on 2017/11/9.
 */
public class JsonDataPakage {

    private ArrayList<CDrawable> mDrawableList = new ArrayList<>();
    private String[] equipAttr = {"DataCollect","Switch","Function"};

    public JsonDataPakage(ArrayList<CDrawable> mDrawableList)
    {
        this.mDrawableList = mDrawableList;
    }

    public JsonDataPakage()
    {

    }

    public String JsonPackage()
    {
        JSONObject jsonObject = new JSONObject();

        for (int i = 0; i < mDrawableList.size(); i++) {
            CBitmap cBitmap = (CBitmap)(mDrawableList.get(i));
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("equip", cBitmap.itemName);
            map.put("value", cBitmap.setThresholdValue);
            list.add(map);
            try {
                jsonObject.put(equipAttr[cBitmap.Item_Attributes - 1], list);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        System.out.println(jsonObject.toString());
        return jsonObject.toString();
    }




    public String JsonParse(String string)
    {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(string);
            //JSONArray jsonArray =  jsonObject.getJSONArray("value");
            //return jsonArray.toString();
            String value = jsonObject.getString("value");
            return value;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "ERROR";
    }

}
