package com.example.sdvii.incomemanager;

/**
 * Created by houma on 19-Dec-17.
 */

public class Payment {
    int pId,pIncome,pDate,pType;
    Double pAmount;
    String pTitle;

    public Payment(){

    }

    public Payment(int pId, int pType, String pTitle, Double pAmount, int pDate, int pIncome) {
        this.pId = pId;
        this.pType = pType;
        this.pTitle = pTitle;
        this.pAmount = pAmount;
        this.pDate = pDate;
        this.pIncome = pIncome;
    }

    public Payment(int pType, String pTitle, Double pAmount, int pDate, int pIncome) {
        this.pType = pType;
        this.pTitle = pTitle;
        this.pAmount = pAmount;
        this.pDate = pDate;
        this.pIncome = pIncome;
    }

    public int getpIncome() {
        return pIncome;
    }

    public void setpIncome(int pIncome) {
        this.pIncome = pIncome;
    }

    public int getpId() {

        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public int getpType() {
        return pType;
    }

    public void setpType(int pType) {
        this.pType = pType;
    }

    public String getpTitle() {
        return pTitle;
    }

    public void setpTitle(String pTitle) {
        this.pTitle = pTitle;
    }

    public Double getpAmount() {
        return pAmount;
    }

    public void setpAmount(Double pAmount) {
        this.pAmount = pAmount;
    }

    public int getpDate() {
        return pDate;
    }

    public void setpDate(int pDate) {
        this.pDate = pDate;
    }
}
