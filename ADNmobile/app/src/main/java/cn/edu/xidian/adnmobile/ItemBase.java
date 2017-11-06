package cn.edu.xidian.adnmobile;

/**
 * Created by lenovo on 2017/11/6.
 */
public class ItemBase {
    public String Name = "";
    public String Details = "";
    public int icon = 0;

    public ItemBase(String Name,String Details,int icon)
    {
        this.Name = Name;
        this.Details = Details;
        this.icon = icon;
    }
}
