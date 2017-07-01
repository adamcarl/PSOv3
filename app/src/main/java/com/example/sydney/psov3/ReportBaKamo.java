package com.example.sydney.psov3;

import android.database.Cursor;
import android.os.Handler;
import android.os.Message;

import com.hdx.lib.serial.SerialParam;
import com.hdx.lib.serial.SerialPortOperaion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import hdx.HdxUtil;


/**
 * Created by Poging Adam on 6/28/2017.
 */

class ReportBaKamo {
    private DB_Data db_data;
    private ArrayList<String> products = new ArrayList<>();

    void main(String x, String userNum){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy");
        String dateformatted = dateformat.format(calendar.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String currentTime = sdf.format(new Date());
        SerialPrinter mSerialPrinter = SerialPrinter.GetSerialPrinter();
        try {
            mSerialPrinter.OpenPrinter(new SerialParam(9600, "/dev/ttyS3", 0), new SerialDataHandler());
            HdxUtil.SetPrinterPower(1);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        String report;
        if(x.equals("no")){
            report = "Z";
        }
        else {
            report = "X";
        }

        products.add("     ABZTRAK INC.");
        products.add("   CONVENIENCE STORE");
        products.add("  Vat Reg TIN:XXXXXXXXXXXX");
        products.add("   MIN:XXXXXXXXXXXXXXXXX");
        products.add("      2nd Floor, #670,");
        products.add("Sgt. Bumatay St, Mandaluyong");
        products.add("    NCR, Philippines");
        products.add("   Serial No. XXXXXXXX");
        products.add(report +"\t"+ dateformatted +"\t"+currentTime);
        products.add("CLK:"+userNum+"\t"+"");
        products.add("------------------------------");
        products.add(report +"\tDAY SALES");
        products.add("------------------------------");
        products.add(report +"\tFIX");
        products.add("\t\t\tXXXXXXX\n");
        products.add("GROSS\tX");
        products.add("\t\t\tXXX.XX");
        products.add("NET\tNo\tX");
        products.add("\t\t\tXXX.XX");
        products.add("------------------------------");
        products.add("RF\tNo\tX");
        products.add("\t\tXXX.XX");
        products.add("CANCEL\tNo\tX");
        products.add("\t\t\tXXX.XX");
        products.add("------------------------------");
        products.add("V-SALES\t\tXXX.XX");
        products.add("V-12%\t\tXXX.XX");
        products.add("EXEMPT-SALES\t\tXXX.XX");
        products.add("ZERO-RATED\t\tXXX.XX");
        if(x.equals("no")){
            products.add("------------------------------");
            products.add("NEW GT XXXXXXXXXXXXXX.XX");
            products.add("OLD GT XXXXXXXXXXXXXX.XX");
            products.add("Reset Count:\t\tX");
            products.add("CONSECUTIVE No.");
            products.add("\t\tXXXXXX - XXXXXX");
            products.add("\t\t\tXXXXXX");
            products.add("RECEIPT No.");
            products.add("\t\tXXXXXX - XXXXXX");
            products.add("\t\t\tXXXXXX");
        }

        products.add("------------------------------");
        products.add(report +"\tTRANS");
        products.add("\t\t\tXXXXXXX\n");
        products.add("CASH\tNo\tX");
        products.add("\t\t\tXXX.XX");
        products.add("PWD\tNo\tX");
        products.add("\t\t\tX.XX");
        products.add("SCD\tNo\tX");
        products.add("\t\t\tX.XX");
        products.add("Less Vat\tNo\tX");
        products.add("\t\t\tX.XX");
        products.add("------------------------------");
        products.add(report +"\tPLU");
        products.add("\t\t\tXXXXXXX\n");
        products.add("AMRICAN LOAF350G\tX");
        products.add("\t\t\tXX.XX");
        products.add("CHEEZY PAN DE SA\tX");
        products.add("\t\t\tXX.XX");
        products.add("AMRICAN LOAF350G\tX");
        products.add("\t\t\tXX.XX");
        products.add("CHEEZY PAN DE SA\tX");
        products.add("\t\t\tXX.XX");
        products.add("AMRICAN LOAF350G\tX");
        products.add("\t\t\tXX.XX");
        products.add("CHEEZY PAN DE SA\tX");
        products.add("\t\t\tXX.XX");
        products.add("AMRICAN LOAF350G\tX");
        products.add("\t\t\tXX.XX");
        products.add("CHEEZY PAN DE SA\tX");
        products.add("\t\t\tXX.XX");
        products.add("------------------------------");
        products.add("TL\t\t\tX");
        products.add("\t\t\tX\n");
        products.add("\t\t\tXX.XX");
        products.add("------------------------------");
        products.add(report +"\tCASHIER");
        products.add("\t\t\tXXXXXXX\n");
        products.add("CLK"+userNum+"\t.........X");
        products.add("GROSS\tX");
        products.add("\t\t\tXX.XX");
        products.add("NET\tNo\tX");
        products.add("\t\t\tXX.XX");
        products.add("CAID\t\tXX.XX");
        products.add("------------------------------");
        products.add(report +"\tHOURLY");
        products.add("\t\t\tXXXXXXX\n");
        products.add("XX:XX->XX:XX");
        products.add("\t\tCT\tX");
        products.add("GROSS\t\tXXX.XX");
        products.add("NET\tNo\tX\n");
        products.add("------------------------------");
        products.add("TL\tCT\tX");
        products.add("GROSS\tXXX.XX");
        products.add("\t\t\tXX.XX");
        products.add("NET\tNo\tX\n");
        products.add("\t\t\tXXX.XX");

        try{
            mSerialPrinter.sydneyDotMatrix7by7();
            mSerialPrinter.printString(products);
            mSerialPrinter.walkPaper(40);
            mSerialPrinter.sendLineFeed();
            products.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Cursor cursor = db_data.searchInvoiceTransactions(x);
//            cursor.moveToFirst();
//            while (!cursor.isAfterLast()) {
//                Cursor curseInvoice = db_data.searchInvoice(cursor.getInt(0));
//                curseInvoice.moveToFirst();
//                products.add(curseInvoice.getString(0));
//                try{
//                    mSerialPrinter.sydneyDotMatrix7by7();
//                    mSerialPrinter.printString(products);
//                    mSerialPrinter.walkPaper(40);
//                    mSerialPrinter.sendLineFeed();
//                    products.clear();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                db_data.updateTransactions(cursor.getInt(0),x);
//
//                curseInvoice.close();
//                cursor.moveToNext();
//            }
//
//        int c = db_data.searchCancel();
//        products.add("Number of Cancel: "+c);
//        try{
//            mSerialPrinter.sydneyDotMatrix7by7();
//            mSerialPrinter.printString(products);
//            mSerialPrinter.walkPaper(40);
//            mSerialPrinter.sendLineFeed();
//            products.clear();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
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