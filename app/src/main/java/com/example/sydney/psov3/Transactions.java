package com.example.sydney.psov3;

/**
 * Created by PROGRAMMER2 on 6/9/2017.
 */

public class Transactions {
    private int transId;
    private String transactionType;
    private String transDateTime;

    public Transactions() {
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public int getTransId() {
        return transId;
    }

    public void setTransId(int transId) {
        this.transId = transId;
    }

    public String getTransDateTime() {
        return transDateTime;
    }

    public void setTransDateTime(String transDateTime) {
        this.transDateTime = transDateTime;
    }


    public Transactions(int transId, String transactionType , String transDateTime) {
        this.transactionType = transactionType;
        this.transId = transId;
        this.transDateTime = transDateTime;
    }
}
