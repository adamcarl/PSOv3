package com.example.sydney.psov3;

/**
 * Created by Jen on 9/21/2017.
 */

public class CashTransaction {
    private String ctTransnum;
    private String ctCashNum;
    private String ctDateTime;
    private double ctCashAdd;
    private double ctCashMinus;
    private String ctReason;
    private String ctRemarks1;
    private String ctRemarks2;
    private String ctRemarks3;
    private String ctRemarks4;

    public CashTransaction() {
    }

    public String getCtTransnum() {
        return ctTransnum;
    }

    public void setCtTransnum(String ctTransnum) {
        this.ctTransnum = ctTransnum;
    }

    public String getCtCashNum() {
        return ctCashNum;
    }

    public void setCtCashNum(String ctCashNum) {
        this.ctCashNum = ctCashNum;
    }

    public String getCtDateTime() {
        return ctDateTime;
    }

    public void setCtDateTime(String ctDateTime) {
        this.ctDateTime = ctDateTime;
    }

    public double getCtCashAdd() {
        return ctCashAdd;
    }

    public void setCtCashAdd(double ctCashAdd) {
        this.ctCashAdd = ctCashAdd;
    }

    public double getCtCashMinus() {
        return ctCashMinus;
    }

    public void setCtCashMinus(double ctCashMinus) {
        this.ctCashMinus = ctCashMinus;
    }

    public String getCtReason() {
        return ctReason;
    }

    public void setCtReason(String ctReason) {
        this.ctReason = ctReason;
    }

    public String getCtRemarks1() {
        return ctRemarks1;
    }

    public void setCtRemarks1(String ctRemarks1) {
        this.ctRemarks1 = ctRemarks1;
    }

    public String getCtRemarks2() {
        return ctRemarks2;
    }

    public void setCtRemarks2(String ctRemarks2) {
        this.ctRemarks2 = ctRemarks2;
    }

    public String getCtRemarks3() {
        return ctRemarks3;
    }

    public void setCtRemarks3(String ctRemarks3) {
        this.ctRemarks3 = ctRemarks3;
    }

    public String getCtRemarks4() {
        return ctRemarks4;
    }

    public void setCtRemarks4(String ctRemarks4) {
        this.ctRemarks4 = ctRemarks4;
    }
}
