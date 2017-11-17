package cn.edu.xidian.adnmobile;

/**
 * Created by lenovo on 2017/11/6.
 */
public class ItemBase {
    public String Name = "";
    public String Details = "";
    public int icon = 0;
    public String EngName = "";
    public int itemFlag = 0;

    public ItemBase(String Name,String Details,int icon,String EngName,int itemFlag)
    {
        this.Name = Name;
        this.Details = Details;
        this.icon = icon;
        this.EngName = EngName;
        this.itemFlag = itemFlag;
    }
}
