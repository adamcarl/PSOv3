package com.example.sydney.psov3;

/**
 * Created by sydney on 6/29/2016.
 */

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.hdx.lib.serial.SerialParam;
import com.hdx.lib.serial.SerialPortOperaion;
import hdx.HdxUtil;


public class Printer extends AppCompatActivity {

    SerialPrinter mSerialPrinter = SerialPrinter.GetSerialPrinter();
    PowerManager.WakeLock lock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            mSerialPrinter.OpenPrinter(new SerialParam(9600, "/dev/ttyS3", 0), new SerialDataHandler());
            HdxUtil.SetPrinterPower(1);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void print(View view) {
        try {
            mSerialPrinter.sydneyDotMatrix7by7();
//            mSerialPrinter.printString("ABZTRAK INC CONVENIENCE STORE");
//
//            mSerialPrinter.printString("\n");
//
//            mSerialPrinter.printString("ABZTRAK INC CONVENIENCE STORE");
//
//            mSerialPrinter.printString("\n");
//
//            mSerialPrinter.printString("ABZTRAK INC CONVENIENCE STORE");
//            mSerialPrinter.printString("\n");
//            mSerialPrinter.printString("ABZTRAK INC CONVENIENCE STORE");
//            mSerialPrinter.printString("\n");
//
//
//            mSerialPrinter.printString("ABZTRAK INC CONVENIENCE STORE");
//            mSerialPrinter.printString("\n");
//            mSerialPrinter.printString("ABZTRAK INC CONVENIENCE STORE");
//            mSerialPrinter.printString("\n");
//
//            mSerialPrinter.printString("ABZTRAK INC CONVENIENCE STORE");
//            mSerialPrinter.printString("\n");
            mSerialPrinter.walkPaper(50);
            mSerialPrinter.sendLineFeed();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void sleep(int ms) {
        try {
            java.lang.Thread.sleep(ms);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private class SerialDataHandler extends Handler {
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