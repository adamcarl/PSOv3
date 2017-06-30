package com.example.sydney.psov3;

/**
 * Created by PROGRAMMER2 on 6/9/2017.
 */

public class Transactions {
    private int transId;
    private String transType;
    private String transDateTime;

    public Transactions() {

    }

    public int getTransId() {
        return transId;
    }

    public void setTransId(int transId) {
        this.transId = transId;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getTransDateTime() {
        return transDateTime;
    }

    public void setTransDateTime(String transDateTime) {
        this.transDateTime = transDateTime;
    }

    public Transactions(int transId , String transType, String transDateTime) {
        this.transId = transId;
        this.transType = transType;
        this.transDateTime = transDateTime;
    }
}