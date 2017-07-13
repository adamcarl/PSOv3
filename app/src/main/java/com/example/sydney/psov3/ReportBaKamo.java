package com.example.sydney.psov3;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;

import com.hdx.lib.serial.SerialParam;
import com.hdx.lib.serial.SerialPortOperaion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import hdx.HdxUtil;

import static android.provider.BaseColumns._ID;
import static com.example.sydney.psov3.Constants.COLUMN_TRANSACTION_TYPE;
import static com.example.sydney.psov3.Constants.TABLE_TRANSACTION;


/**
 * Created by Poging Adam on 6/28/2017.
 */

class ReportBaKamo {
    private DB_Data db_data;
    private ArrayList<String> products = new ArrayList<>();
    SQLiteDatabase dbReader;

    void main(String x, String date, int transNum){
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
            // Pad with zeros and a width of 6 chars.
            String trans = String.format("%1$06d", transNum);

        products.add("NOVA RESTAURANT & SERVICE INC.");
        products.add("MILKY WAY");
        products.add("VAT REG TIN:000-482-511-001");
        products.add("MIN:15040610033393902");
        products.add("PLANT 25 LOPEZ DRIVE");
        products.add("POBLACION, MAKATI CITY");
        products.add("SERIAL NO. ASDFG1234567890");
        products.add("PTU No. FP121234-123-1234567-12345\n");
        products.add("=======================================");
        products.add(report+"-READ");
        if(x.equals("no")) {
            products.add("END-OF-DAY REPORT");
            products.add("=======================================");
            products.add("BIZDATE : "+date);
            products.add("BRANCH : HEAD OFFICE");
            products.add("SHIFT : ALL\t\tTRANS#"+trans+"\n");
        }else {
            products.add("CASHIER REPORT");
            products.add("=======================================");
            products.add("BIZDATE : " + dateformatted + " " + currentTime);
            products.add("CASHIER : "+x);
            products.add("SHIFT : 1\t\tTRANS#"+transNum+"\n");
        }
        String gross = db_data.getGrossSales(x);
        products.add("GROSS SALES\t\t"+gross);
//
//        products.add(" SALES DISCOUNT\t\t-XX.XX");
//        products.add("----------------------------------------");
//        products.add("NET SALES\t\tX,XXX.XX\n");
//
//        products.add("TAX CODE\tSALES\tTAX");
//        products.add("----------------------------------------");
//        products.add("[n] N-Sal\tX.XX\tX.XX");
//        products.add("[v] V-Sal\tX,XXX.XX\tXXX.XX");
//        products.add("[x] E-Sal\tXXX.XX\tX.XX");
//        products.add("[z] Z-Sal\tX.XX\tX.XX\n");
//
//        if(x.equals("no")){
//            products.add("OLD GT\tXXX-XXXXXXXXXXXX.XX");
//            products.add("NEW GT\tXXX-XXXXXXXXXXXX.XX\n");
//
//            products.add("Z Count\t\tXXXXX\n");
//
//            products.add("Trans #\t\tXXXXXX - XXXXXX");
//            products.add("\t\t\tX\n");
//
//            products.add("OR #\t\tXXXXXX - XXXXXX");
//            products.add("\t\t\tX\n");
//        }
//        products.add("CASH SALES\t\tXXX.XX");
//        products.add("----------------------------------------");
//        products.add("CASH IN DRAWER\t\tXXX.XX");
//
//        products.add("CASH COUNT\t\tXXX.XX");
//        products.add("----------------------------------------");
//        products.add("CASH SHORT/OVER\t\tX.XX\n");
//
//        products.add("TRANSACTION\t\tAMOUNT");
//        products.add("----------------------------------------");
//        products.add("NORMAL SALES\t\tX,XXX.XX\n");
//
//        products.add("TENDER\t\tAMOUNT");
//        products.add("----------------------------------------");
//        products.add("TOTAL CASH\tX\tXXX.XX");
//        products.add("  CC BDO\tX\tX,XXX.XX");
//        products.add("  CC BPI\tX\tX,XXX.XX");
//        products.add("TOTAL CREDIT CA\t\tXX,XXX.XX\n");
//
//        products.add("DISCOUNT\t\tAMOUNT");
//        products.add("----------------------------------------");
//        products.add("SCD 20%\tX\t-XX.XX");
//        products.add("Tax - Exempt\tX\t-XX.XX");
//        products.add("TOTAL DEDUCTION\tX\t-XXX.XX\n");
//
//        products.add("ITEM SALES\t\tAMOUNT");
//        products.add("----------------------------------------");
//        products.add("MILO ACTIV-GO 22GMS.");
//        products.add("BEV-001\t\tX,XXX.XX");
//        products.add("xXXXX.XXXX\td-X.XX\tX,XXX.XX");
//        products.add("QUAKER OAT CEREAL DRINK 29g");
//        products.add("BEV-002\t\tXX.XX");
//        products.add("xX.XXXX\td-X.XX\tXX.XX");
//        products.add("TAPSILOG");
//        products.add("FOOD-004\t\tXXX.XX");
//        products.add("xX.XXXX\td-XX.XX\tXXX.XX");
//        products.add("----------------------------------------");
//        products.add("TOTAL QTY\t\tXXX.XXXX");
//        products.add("GROSS SALES\t\tX,XXX.XX");
//        products.add("TOTAL DEDUCTIONS\t\t-XX.XX");
//        products.add("NET SALES\t\tX,XXX.XX\n");
//
//        products.add("DEPARTMENT SALES\t\tAMOUNT");
//        products.add("----------------------------------------");
//        products.add("NO DEPARTMENT");
//        products.add("\t\t\tXX.XX");
//        products.add("xX.XXXX\td-X.XX\tXX.XX");
//        products.add("NO DEPARTMENT");
//        products.add("\t\t\tXXX.XX");
//        products.add("xX.XXXX\td-XX.XX\tXXX.XX");
//        products.add("NO DEPARTMENT");
//        products.add("\t\t\tX,XXX.XX");
//        products.add("xXXXX.XXXX\td-X.XX\tX,XXX.XX");
//        products.add("----------------------------------------");
//        products.add("TOTAL QTY\t\tXXX.XXXX");
//        products.add("GROSS SALES\t\tX,XXX.XX");
//        products.add("TOTAL DEDUCTIONS\t\t-XX.XX");
//        products.add("NET SALES\t\tX,XXX.XX\n");
//
//        products.add("GROUP SALES\t\tAMOUNT");
//        products.add("----------------------------------------");
//        products.add("NO GROUP");
//        products.add("\t\t\tXX.XX");
//        products.add("xX.XXXX\td-X.XX\tXX.XX");
//        products.add("NO GROUP");
//        products.add("\t\t\tXXX.XX");
//        products.add("xX.XXXX\td-XX.XX\tXXX.XX");
//        products.add("NO GROUP");
//        products.add("\t\t\tX,XXX.XX");
//        products.add("xXXXX.XXXX\td-X.XX\tX,XXX.XX");
//        products.add("----------------------------------------");
//        products.add("TOTAL QTY\t\tXXX.XXXX");
//        products.add("GROSS SALES\t\tX,XXX.XX");
//        products.add("TOTAL DEDUCTIONS\t\t-XX.XX");
//        products.add("NET SALES\t\tX,XXX.XX\n");
//
//        products.add("BRAND SALES\t\tAMOUNT");
//        products.add("----------------------------------------");
//        products.add("NO BRAND");
//        products.add("\t\t\tXX.XX");
//        products.add("xX.XXXX\td-X.XX\tXX.XX");
//        products.add("NO BRAND");
//        products.add("\t\t\tXXX.XX");
//        products.add("xX.XXXX\td-XX.XX\tXXX.XX");
//        products.add("NO BRAND");
//        products.add("\t\t\tX,XXX.XX");
//        products.add("xXXXX.XXXX\td-X.XX\tX,XXX.XX");
//        products.add("----------------------------------------");
//        products.add("TOTAL QTY\t\tXXX.XXXX");
//        products.add("GROSS SALES\t\tX,XXX.XX");
//        products.add("TOTAL DEDUCTIONS\t\t-XX.XX");
//        products.add("NET SALES\t\tX,XXX.XX\n");
//
//        products.add("CATEGORY SALES\t\tAMOUNT");
//        products.add("----------------------------------------");
//        products.add("NO CATEGORY");
//        products.add("\t\t\tXX.XX");
//        products.add("xX.XXXX\td-X.XX\tXX.XX");
//        products.add("NO CATEGORY");
//        products.add("\t\t\tXXX.XX");
//        products.add("xX.XXXX\td-XX.XX\tXXX.XX");
//        products.add("NO CATEGORY");
//        products.add("\t\t\tX,XXX.XX");
//        products.add("xXXXX.XXXX\td-X.XX\tX,XXX.XX");
//        products.add("----------------------------------------");
//        products.add("TOTAL QTY\t\tXXX.XXXX");
//        products.add("GROSS SALES\t\tX,XXX.XX");
//        products.add("TOTAL DEDUCTIONS\t\t-XX.XX");
//        products.add("NET SALES\t\tX,XXX.XX\n");

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