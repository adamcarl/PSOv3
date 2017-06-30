package com.example.sydney.psov3;

import android.accessibilityservice.GestureDescription;
import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;

import com.hdx.lib.serial.SerialParam;
import com.hdx.lib.serial.SerialPortOperaion;

import java.util.ArrayList;

import hdx.HdxUtil;


/**
 * Created by Poging Adam on 6/28/2017.
 */

class ReportBaKamo {
    private DB_Data db_data;
    private ArrayList<String> products = new ArrayList<>();

    void main(String userNum){

        SerialPrinter mSerialPrinter = SerialPrinter.GetSerialPrinter();
        try {
            mSerialPrinter.OpenPrinter(new SerialParam(9600, "/dev/ttyS3", 0), new SerialDataHandler());
            HdxUtil.SetPrinterPower(1);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
            Cursor cursor = db_data.searchInvoiceTransactions(userNum);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Cursor curseInvoice = db_data.searchInvoice(cursor.getInt(0));
                curseInvoice.moveToFirst();
                products.add(curseInvoice.getString(0));
                try{
                    mSerialPrinter.sydneyDotMatrix7by7();
                    mSerialPrinter.printString(products);
                    mSerialPrinter.walkPaper(40);
                    mSerialPrinter.sendLineFeed();
                    products.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                db_data.updateTransactions(cursor.getInt(0),userNum);

                curseInvoice.close();
                cursor.moveToNext();
            }

        int c = db_data.searchCancel();
        products.add("Number of Cancel: "+c);
        try{
            mSerialPrinter.sydneyDotMatrix7by7();
            mSerialPrinter.printString(products);
            mSerialPrinter.walkPaper(40);
            mSerialPrinter.sendLineFeed();
            products.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setDb_data(DB_Data db_data) {
        this.db_data = db_data;
    }

    private static class SerialDataHandler extends Handler {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SerialPortOperaion.SERIAL_RECEIVED_DATA_MSG:
                    SerialPortOperaion.SerialReadData data = (SerialPortOperaion.SerialReadData)msg.obj;
                    StringBuilder sb=new StringBuilder();
                    for(int x=0;x<data.size;x++)
                        sb.append(String.format("%02x", data.data[x]));
            }
        }
    }
}