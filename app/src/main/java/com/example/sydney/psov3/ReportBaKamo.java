package com.example.sydney.psov3;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.nio.DoubleBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import static com.example.sydney.psov3.Constants.COLUMN_ITEM_CASHIER;
import static com.example.sydney.psov3.Constants.COLUMN_ITEM_DESC;
import static com.example.sydney.psov3.Constants.COLUMN_ITEM_DISCOUNT;
import static com.example.sydney.psov3.Constants.COLUMN_ITEM_NAME;
import static com.example.sydney.psov3.Constants.COLUMN_ITEM_PRICE;
import static com.example.sydney.psov3.Constants.COLUMN_ITEM_QUANTITY;
import static com.example.sydney.psov3.Constants.COLUMN_ITEM_XREPORT;
import static com.example.sydney.psov3.Constants.COLUMN_ITEM_ZREPORT;
import static com.example.sydney.psov3.Constants.TABLE_ITEM;


/**
 * Created by Poging Adam on 6/28/2017.
 */

class ReportBaKamo {
    private DB_Data db_data;
    private ArrayList<String> toBePrinted = new ArrayList<>();
    SQLiteDatabase dbReader;
    String gross, discount, vsale, xsale, zsale, vtax, xtax, ztax, zf, t1, t2, or1, or2, ogt, ngt, trans;
    Double net_gross, net_discount, net, dogt, dngt, over, totalGross=0.0, totalDeduction=0.0, totalItemSales=0.0;
    int z, t3,totalQty=0;
    int[] or = new int[1], transArray = new int[1];
    List<List<String>> items = new ArrayList<>();

    void main(String x, String date, int transNum, double moneyCount){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy");
        String dateformatted = dateformat.format(calendar.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String currentTime = sdf.format(new Date());
//        SerialPrinter mSerialPrinter = SerialPrinter.GetSerialPrinter();
//        try {
//            mSerialPrinter.OpenPrinter(new SerialParam(9600, "/dev/ttyS3", 0), new SerialDataHandler());
//            HdxUtil.SetPrinterPower(1);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
        String report;
        if(x.equals("no")){
            report = "Z";
        }
        else {
            report = "X";
        }
        gross = db_data.getGrossSales(x);
        discount = db_data.getDiscountSales(x);
        vsale = db_data.sales("0");
        xsale = db_data.sales("1");
        zsale = db_data.sales("2");
        vtax = db_data.tax("0");
        xtax = db_data.tax("1");
        ztax = db_data.tax("2");
        z = db_data.pleaseGiveMeTheZCount();
        transArray = db_data.pleaseGiveMeTheFirstAndLastOfTheTransactions();
        or = db_data.pleaseGiveMeTheFirstAndLastOfTheOfficialReceipt();
        ogt = db_data.getMyOldGross();

        Double exemptDiscount = 0.0;
        Double exemptDiscount1 = 0.0;
try{
    exemptDiscount = Double.parseDouble(zsale);
    exemptDiscount1 = exemptDiscount * .12;

}
catch (Exception e){
    exemptDiscount = 0.0;
    exemptDiscount1 = exemptDiscount * .12;
}
        net_gross = Double.parseDouble(gross);
        net_discount = Double.parseDouble(discount)+exemptDiscount1;
        net = net_gross-net_discount;
        or1 = String.format("%1$06d", or[0]);
        or2 = String.format("%1$06d", or[1]);
        t1 = String.format("%1$06d", transArray[0]);
        t2 = String.format("%1$06d", transArray[1]);
        t3 = transArray[1] - transArray[0];
        zf = String.format("%1$05d", z);
        trans = String.format("%1$06d", transNum);
        dogt = Double.parseDouble(ogt);
        dngt = dogt + net;
        over = moneyCount - net;
        // Pad with zeros and a width of 6 chars.

        toBePrinted.add("ABZTRACK DEMO STORE\n");
        toBePrinted.add("VAT REG TIN:000-111-111-001\n");
        toBePrinted.add("MIN:12345678901234567\n");
        toBePrinted.add("670 SGT BUMATAY STREET\n");
        toBePrinted.add("PLAINVIEW, MANDALUYONG\n");
        toBePrinted.add("SERIAL NO. ASDFG1234567890\n");
        toBePrinted.add("PTU No. FP121234-123-1234567-12345\n\n");
        toBePrinted.add("===============================\n");
        toBePrinted.add(report+"-READ\n");
        if(x.equals("no")) {
            toBePrinted.add("END-OF-DAY REPORT\n");
            toBePrinted.add("===============================\n");
            toBePrinted.add("BIZDATE : "+date+"\n");
            toBePrinted.add("BRANCH : HEAD OFFICE\n");
            toBePrinted.add("SHIFT : ALL\t\tTRANS#"+trans+"\n\n");
        }else {
            toBePrinted.add("CASHIER REPORT\n");
            toBePrinted.add("===============================\n");
            toBePrinted.add("BIZDATE : " + dateformatted + " " + currentTime+"\n");
            toBePrinted.add("CASHIER : "+x+"\n");
            toBePrinted.add("SHIFT : 1\t\tTRANS#"+trans+"\n\n");
        }
        toBePrinted.add("GROSS SALES\t\t"+gross+"\n");
        toBePrinted.add(" SALES DISCOUNT\t\t-"+net_discount+"\n");
        toBePrinted.add("------------------------------\n");
        toBePrinted.add("NET SALES\t\t"+ net +"\n\n");

        toBePrinted.add("TAX CODE\tSALES\tTAX\n");
        toBePrinted.add("------------------------------\n");
//        toBePrinted.add("[n] N-Sal\tX.XX\tX.XX");
        toBePrinted.add("[v] V-Sal\t"+vsale+"\t"+vtax+"\n");
        toBePrinted.add("[x] E-Sal\t"+xsale+"\t"+xtax+"\n");
        toBePrinted.add("[z] Z-Rat\t"+zsale+"\t"+ztax+"\n\n");

        if(x.equals("no")){
            toBePrinted.add("OLD GT\t000-"+ogt+"\n");
            toBePrinted.add("NEW GT\t000-"+dngt+"\n\n");

            toBePrinted.add("Z Count\t\t"+zf+"\n\n");

           toBePrinted.add("Trans #\t\t"+t1+" - "+t2+"\n");
            toBePrinted.add("\t\t\t"+t3+"\n\n");

            toBePrinted.add("OR #\t\t"+or1+ " - "+or2+"\n");
        }
        toBePrinted.add("CASH SALES\t\t"+net+"\n");
        toBePrinted.add("------------------------------\n");
        toBePrinted.add("CASH IN DRAWER\t\t"+net+"\n");

        toBePrinted.add("CASH COUNT\t\t"+moneyCount+"\n");
        toBePrinted.add("------------------------------\n");
        toBePrinted.add("CASH SHORT/OVER\t\t"+over+"\n\n");

//        toBePrinted.add("TRANSACTION\t\tAMOUNT");
//        toBePrinted.add("------------------------------");
//        toBePrinted.add("NORMAL SALES\t\tX,XXX.XX\n");

//        toBePrinted.add("TENDER\t\tAMOUNT");
//        toBePrinted.add("------------------------------");
//        toBePrinted.add("TOTAL CASH\t0\t0.00");
//        toBePrinted.add("  CC BDO\tX\t0.00");
//        toBePrinted.add("  CC BPI\tX\t0.00");
//        toBePrinted.add("TOTAL CREDIT CA\t\t0.00\n");

        toBePrinted.add("DISCOUNT\t\tAMOUNT");
        toBePrinted.add("------------------------------\n");
        toBePrinted.add("SCD 20%\t\t-"+discount+"\n");
        toBePrinted.add("Tax - Exempt\t\t-"+exemptDiscount1+"\n");
        toBePrinted.add("TOTAL DEDUCTION\t\t-"+net_discount+"\n\n");



        toBePrinted.add("ITEM SALES\t\tAMOUNT\n");
        toBePrinted.add("------------------------------\n");
            List<List<String>> items = new ArrayList<>();
            ArrayList<String> temp = new ArrayList<>();
            String mWHERE;
            String[] mWHERE_ARGS;
            String[] columns = {COLUMN_ITEM_NAME,COLUMN_ITEM_DESC,COLUMN_ITEM_QUANTITY,COLUMN_ITEM_DISCOUNT,COLUMN_ITEM_PRICE};
            if(x.equals("no")) {
                mWHERE = COLUMN_ITEM_XREPORT+" = ? AND "+COLUMN_ITEM_CASHIER+" = ?";
                mWHERE_ARGS = new String[]{"0"};
            }
            else {
                mWHERE = COLUMN_ITEM_ZREPORT+" = ?";
                mWHERE_ARGS = new String[]{"0"};
            }
            Cursor c = db_data.getAllItems(x);
            c.moveToFirst();

        while(!c.isAfterLast()){
            Double price = c.getDouble(c.getColumnIndex(COLUMN_ITEM_PRICE)) * c.getInt(c.getColumnIndex(COLUMN_ITEM_QUANTITY));
            Double total = price - c.getDouble(c.getColumnIndex(COLUMN_ITEM_DISCOUNT));
            toBePrinted.add(c.getString(c.getColumnIndex(COLUMN_ITEM_NAME))+"\n"+c.getString(c.getColumnIndex(COLUMN_ITEM_DESC))+"\t"+price+"\n");//example I don't know the order you need
            toBePrinted.add("x"+c.getString(c.getColumnIndex(COLUMN_ITEM_QUANTITY))+".0000\td-"+c.getString(c.getColumnIndex(COLUMN_ITEM_DISCOUNT))+"\t"+total+"\n");//example I don't know the order you need
            totalQty = totalQty + c.getInt(c.getColumnIndex(COLUMN_ITEM_QUANTITY));
            totalGross = totalGross + price;
            totalDeduction = totalDeduction + c.getDouble(c.getColumnIndex(COLUMN_ITEM_DISCOUNT));
            c.moveToNext();
        }
            c.close();

//        for(int i=0;i<items.size();i++){
//            String[] myString;
//            myString=items.get(i).toArray(new String[items.size()]);
//            Double price = Double.parseDouble(myString[4]);
//            Double discount = Double.parseDouble(myString[3]);
//            Double net = price-discount;
//            toBePrinted.add(myString[0]);
//            toBePrinted.add(myString[1]+"\t\t"+myString[4]);
//            toBePrinted.add("x"+myString[2]+".0000"+"\td-"+myString[3]+"\t"+net);
//            totalQty = totalQty + Integer.parseInt(myString[2]);
//            totalGross = totalGross + Double.parseDouble(myString[4]);
//            totalDeduction = totalDeduction + Double.parseDouble(myString[3]);
//    }
    totalItemSales = totalGross - totalDeduction;
        toBePrinted.add("------------------------------\n");
        toBePrinted.add("TOTAL QTY\t\t"+totalQty+".0000\n");
        toBePrinted.add("GROSS SALES\t\t"+totalGross+"\n");
        toBePrinted.add("TOTAL DEDUCTIONS\t\t-"+totalDeduction+"\n");
        Double totalNet = totalGross - totalDeduction;
        toBePrinted.add("NET SALES\t\t"+totalNet+"\n\n");

//        Cursor cursor = db_data.sales();
//        cursor.moveToFirst();
//        toBePrinted.add("Vattable"+cursor.getInt(0));
//        cursor.close();
//
//        Cursor cursor1 = db_data.tax();
//        cursor1.moveToFirst();
//        toBePrinted.add("Vat"+cursor1.getInt(0));
//        cursor1.close();
        toBePrinted.add("\n\n\n\n\n");
        toBePrinted.add("");

//        toBePrinted.add("DEPARTMENT SALES\t\tAMOUNT");
//        toBePrinted.add("------------------------------");
//        toBePrinted.add("NO DEPARTMENT");
//        toBePrinted.add("\t\t\tXX.XX");
//        toBePrinted.add("xX.XXXX\td-X.XX\tXX.XX");
//        toBePrinted.add("NO DEPARTMENT");
//        toBePrinted.add("\t\t\tXXX.XX");
//        toBePrinted.add("xX.XXXX\td-XX.XX\tXXX.XX");
//        toBePrinted.add("NO DEPARTMENT");
//        toBePrinted.add("\t\t\tX,XXX.XX");
//        toBePrinted.add("xXXXX.XXXX\td-X.XX\tX,XXX.XX");
//        toBePrinted.add("------------------------------");
//        toBePrinted.add("TOTAL QTY\t\tXXX.XXXX");
//        toBePrinted.add("GROSS SALES\t\tX,XXX.XX");
//        toBePrinted.add("TOTAL DEDUCTIONS\t\t-XX.XX");
//        toBePrinted.add("NET SALES\t\tX,XXX.XX\n");
//
//        toBePrinted.add("GROUP SALES\t\tAMOUNT");
//        toBePrinted.add("------------------------------");
//        toBePrinted.add("NO GROUP");
//        toBePrinted.add("\t\t\tXX.XX");
//        toBePrinted.add("xX.XXXX\td-X.XX\tXX.XX");
//        toBePrinted.add("NO GROUP");
//        toBePrinted.add("\t\t\tXXX.XX");
//        toBePrinted.add("xX.XXXX\td-XX.XX\tXXX.XX");
//        toBePrinted.add("NO GROUP");
//        toBePrinted.add("\t\t\tX,XXX.XX");
//        toBePrinted.add("xXXXX.XXXX\td-X.XX\tX,XXX.XX");
//        toBePrinted.add("------------------------------");
//        toBePrinted.add("TOTAL QTY\t\tXXX.XXXX");
//        toBePrinted.add("GROSS SALES\t\tX,XXX.XX");
//        toBePrinted.add("TOTAL DEDUCTIONS\t\t-XX.XX");
//        toBePrinted.add("NET SALES\t\tX,XXX.XX\n");
//
//        toBePrinted.add("BRAND SALES\t\tAMOUNT");
//        toBePrinted.add("------------------------------");
//        toBePrinted.add("NO BRAND");
//        toBePrinted.add("\t\t\tXX.XX");
//        toBePrinted.add("xX.XXXX\td-X.XX\tXX.XX");
//        toBePrinted.add("NO BRAND");
//        toBePrinted.add("\t\t\tXXX.XX");
//        toBePrinted.add("xX.XXXX\td-XX.XX\tXXX.XX");
//        toBePrinted.add("NO BRAND");
//        toBePrinted.add("\t\t\tX,XXX.XX");
//        toBePrinted.add("xXXXX.XXXX\td-X.XX\tX,XXX.XX");
//        toBePrinted.add("------------------------------");
//        toBePrinted.add("TOTAL QTY\t\tXXX.XXXX");
//        toBePrinted.add("GROSS SALES\t\tX,XXX.XX");
//        toBePrinted.add("TOTAL DEDUCTIONS\t\t-XX.XX");
//        toBePrinted.add("NET SALES\t\tX,XXX.XX\n");
//
//        toBePrinted.add("CATEGORY SALES\t\tAMOUNT");
//        toBePrinted.add("------------------------------");
//        toBePrinted.add("NO CATEGORY");
//        toBePrinted.add("\t\t\tXX.XX");
//        toBePrinted.add("xX.XXXX\td-X.XX\tXX.XX");
//        toBePrinted.add("NO CATEGORY");
//        toBePrinted.add("\t\t\tXXX.XX");
//        toBePrinted.add("xX.XXXX\td-XX.XX\tXXX.XX");
//        toBePrinted.add("NO CATEGORY");
//        toBePrinted.add("\t\t\tX,XXX.XX");
//        toBePrinted.add("xXXXX.XXXX\td-X.XX\tX,XXX.XX");
//        toBePrinted.add("------------------------------");
//        toBePrinted.add("TOTAL QTY\t\tXXX.XXXX");
//        toBePrinted.add("GROSS SALES\t\tX,XXX.XX");
//        toBePrinted.add("TOTAL DEDUCTIONS\t\t-XX.XX");
//        toBePrinted.add("NET SALES\t\tX,XXX.XX\n");

//        try{
//            mSerialPrinter.sydneyDotMatrix7by7();
//            mSerialPrinter.printString(toBePrinted);
//            mSerialPrinter.walkPaper(40);
//            mSerialPrinter.sendLineFeed();
//            toBePrinted.clear();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Cursor cursor = db_data.searchInvoiceTransactions(x);
//            cursor.moveToFirst();
//            while (!cursor.isAfterLast()) {
//                Cursor curseInvoice = db_data.searchInvoice(cursor.getInt(0));
//                curseInvoice.moveToFirst();
//                toBePrinted.add(curseInvoice.getString(0));
//                try{
//                    mSerialPrinter.sydneyDotMatrix7by7();
//                    mSerialPrinter.printString(toBePrinted);
//                    mSerialPrinter.walkPaper(40);
//                    mSerialPrinter.sendLineFeed();
//                    toBePrinted.clear();
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
//        toBePrinted.add("Number of Cancel: "+c);
//        try{
//            mSerialPrinter.sydneyDotMatrix7by7();
//            mSerialPrinter.printString(toBePrinted);
//            mSerialPrinter.walkPaper(40);
//            mSerialPrinter.sendLineFeed();
//            toBePrinted.clear();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
    void setDb_data(DB_Data db_data) {
        this.db_data = db_data;
    }

//    private static class SerialDataHandler extends Handler {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case SerialPortOperaion.SERIAL_RECEIVED_DATA_MSG:
//                    SerialPortOperaion.SerialReadData data = (SerialPortOperaion.SerialReadData)msg.obj;
//                    StringBuilder sb=new StringBuilder();
//                    for(int x=0;x<data.size;x++)
//                        sb.append(String.format("%02x", data.data[x]));
//            }
//        }
//    }
    ArrayList<String> getToBePrinted(){
        return toBePrinted;
    }
}