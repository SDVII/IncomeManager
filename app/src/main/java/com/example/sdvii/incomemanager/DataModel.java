package com.example.sdvii.incomemanager;

/**
 * Created by houmam on 17-Dec-17.
 */

public class DataModel {

    int dId;
    String date;
    String money;
    String type;
    String title;

    public DataModel(int dId ,String date, String money, String type, String title){
        this.dId = dId;
        this.date = date;
        this.money = money;
        this.type = type;
        this.title = title;
    }

    public int getdId() {
        return dId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
