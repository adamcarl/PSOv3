package com.example.sydney.psov3;

import android.database.Cursor;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.sydney.psov3.Constants.COLUMN_ITEM_CASHIER;
import static com.example.sydney.psov3.Constants.COLUMN_ITEM_DESC;
import static com.example.sydney.psov3.Constants.COLUMN_ITEM_DISCOUNT;
import static com.example.sydney.psov3.Constants.COLUMN_ITEM_NAME;
import static com.example.sydney.psov3.Constants.COLUMN_ITEM_PRICE;
import static com.example.sydney.psov3.Constants.COLUMN_ITEM_QUANTITY;
import static com.example.sydney.psov3.Constants.COLUMN_ITEM_XREPORT;
import static com.example.sydney.psov3.Constants.COLUMN_ITEM_ZREPORT;

/**
 * Created by Poging Adam on 6/28/2017.
 */

class ReportBaKamo {
    private String zsale;
    private String vtax;
    private String xtax;
    private String ztax;
    private String t1;
    private String t2;
    private String or1;
    private String or2;
    private String trans;
    private double net_discount;
    private double net;
    private double dogt;
    private double dngt;
    private double over;
    private double currentCashInOutEveryShift;
    private double totalGross = 0.0;
    private double totalDeduction = 0.0;
    private double totalItemSales = 0.0;
    private int z, t3, totalQty = 0;
    private int[] or = new int[1], transArray = new int[1];
    private double[] hourlyGrossSale = new double[24];
    private DB_Data db_data;
    private ArrayList<String> toBePrinted = new ArrayList<>();
    private Cursor cProd = null;

    void main(String x, String date, int transNum, double moneyCount) throws ParseException {
        Date currDate = new Date();
        final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MMM-dd-yyyy");
        String dateToStr = dateTimeFormat.format(currDate);
//        Date strToDate = null;
//        strToDate = dateTimeFormat.parse(dateToStr);
//        String dateToString = strToDate.toString();
//        Log.e("main in ReportBakamo ", dateToStr);

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
        hourlyGrossSale = db_data.getHourlyGrossSale(x);
        double ogt = db_data.getMyOldGross();
        double gross = db_data.getNormalSales(x);
        double credit = db_data.getCreditSales(x);
        String discount = db_data.getDiscountSales(x);
        String vsale = db_data.sales("0", x);
        String xsale = db_data.sales("1", x);
        zsale = db_data.sales("2",x);
        vtax = db_data.tax("0",x);
        xtax = db_data.tax("1",x);
        ztax = db_data.tax("2",x);
        z = db_data.pleaseGiveMeTheZCount();
        transArray = db_data.pleaseGiveMeTheFirstAndLastOfTheTransactions();
        or = db_data.pleaseGiveMeTheFirstAndLastOfTheOfficialReceipt();
        if(!x.equals("no")){
            currentCashInOutEveryShift = db_data.getCashinoutForShift(x,dateToStr);
        }

        double exemptDiscount;
        double exemptDiscount1;
try{
    exemptDiscount = Double.parseDouble(zsale);
    exemptDiscount1 = exemptDiscount * .12;
}
catch (Exception e){
    exemptDiscount = 0.0;
    exemptDiscount1 = exemptDiscount * .12;
}
        double net_gross = gross + credit;
        net_discount = Double.parseDouble(discount)+exemptDiscount1;
        net = net_gross - net_discount;
        or1 = String.format("%1$06d", or[0]);
        or2 = String.format("%1$06d", or[1]);
        trans = String.format("%1$06d", transNum);
        over = moneyCount - gross;
        // Pad with zeros and a width of 6 chars.

        toBePrinted.add("ABZTRAK DEMO STORE");
        toBePrinted.add("VAT REG TIN:000-111-111-001");
        toBePrinted.add("MIN:12345678901234567");
        toBePrinted.add("670 SGT BUMATAY STREET");
        toBePrinted.add("PLAINVIEW, MANDALUYONG");
        toBePrinted.add("SERIAL NO. ASDFG1234567890");
        toBePrinted.add("PTU No. FP121234-123-1234567-12345\n");
        toBePrinted.add("==============================================");
        toBePrinted.add(report+"-READ");
        if(x.equals("no")) {
            toBePrinted.add("END-OF-DAY REPORT");
            toBePrinted.add("==============================================");
            toBePrinted.add("BIZDATE : "+date);
            toBePrinted.add("BRANCH : HEAD OFFICE");
            toBePrinted.add("SHIFT : ALL\t\tTRANS#"+trans+"\n");
        }else {
            toBePrinted.add("CASHIER REPORT");
            toBePrinted.add("==============================================");
            toBePrinted.add("BIZDATE : " + dateToStr + " " + currentTime);
            toBePrinted.add("CASHIER : "+x);
            toBePrinted.add("SHIFT : 1\t\tTRANS#"+trans+"\n");
        }
        double mGrossBaKamo = gross + credit;
        toBePrinted.add("GROSS SALES\t\t" + mGrossBaKamo);
        toBePrinted.add(" SALES DISCOUNT\t\t-"+net_discount);
        toBePrinted.add("----------------------------------------------");
        toBePrinted.add("NET SALES\t\t"+ net +"\n");

        toBePrinted.add("TAX CODE\tSALES\tTAX");
        toBePrinted.add("----------------------------------------------");
//        toBePrinted.add("[n] N-Sal\tX.XX\tX.XX");
        toBePrinted.add("[v] V-Sal\t" + vsale + "\t" + vtax);
        toBePrinted.add("[x] E-Sal\t" + xsale + "\t" + xtax);
        toBePrinted.add("[z] Z-Rat\t"+zsale+"\t"+ztax+"\n");

        if(x.equals("no")){
            dogt = ogt;
            dngt = dogt + net;
            String zf = String.format("%1$05d", z);

            t1 = String.format("%1$06d", transArray[0]);
            t2 = String.format("%1$06d", transArray[1]);
            t3 = transArray[1] - transArray[0];

            toBePrinted.add("OLD GT\t000-"+dogt);
            toBePrinted.add("NEW GT\t000-"+dngt+"\n");

            toBePrinted.add("Z Count\t\t" + zf + "\n");
            toBePrinted.add("Trans #\t\t" + t1 + " - " + t2);
            toBePrinted.add("\t\t\t"+t3+"\n");

            toBePrinted.add("OR #\t\t"+or1+ " - "+or2);
        }
        toBePrinted.add("CASH SALES\t\t" + gross);
        toBePrinted.add("----------------------------------------------");
        toBePrinted.add("CASH IN DRAWER\t\t" + gross);

        toBePrinted.add("CASH COUNT\t\t"+moneyCount + currentCashInOutEveryShift);
        toBePrinted.add("----------------------------------------------");
        toBePrinted.add("CASH SHORT/OVER\t\t"+over+"\n");
        toBePrinted.add("TRANSACTION\t\tAMOUNT");
        toBePrinted.add("----------------------------------------------");
        toBePrinted.add("NORMAL SALES\t\tX,XXX.XX\n");

        toBePrinted.add("TENDER\t\tAMOUNT");
        toBePrinted.add("----------------------------------------------");
        toBePrinted.add("TOTAL CASH\t" + gross);
        toBePrinted.add("TOTAL CREDIT CA\t" + credit + "\n");

        toBePrinted.add("DISCOUNT\t\tAMOUNT");
        toBePrinted.add("----------------------------------------------");
        toBePrinted.add("SCD 20%\t\t-" + discount);
        toBePrinted.add("Tax - Exempt\t\t-"+exemptDiscount1);
        toBePrinted.add("TOTAL DEDUCTION\t\t-"+net_discount+"\n");

        toBePrinted.add("ITEM SALES\t\tAMOUNT");
        toBePrinted.add("----------------------------------------------");
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
            Cursor c = db_data.getAllItems(x);//// TODO: 8/2/2017  
            c.moveToFirst();

        while(!c.isAfterLast()){
            Double price = c.getDouble(c.getColumnIndex(COLUMN_ITEM_PRICE)) * c.getInt(c.getColumnIndex(COLUMN_ITEM_QUANTITY));
            Double total = price - c.getDouble(c.getColumnIndex(COLUMN_ITEM_DISCOUNT));
            toBePrinted.add(c.getString(c.getColumnIndex(COLUMN_ITEM_NAME))+"\n"+c.getString(c.getColumnIndex(COLUMN_ITEM_DESC))+"\t"+price);//example I don't know the order you need
            toBePrinted.add("x"+c.getString(c.getColumnIndex(COLUMN_ITEM_QUANTITY))+".0000\td-"+c.getString(c.getColumnIndex(COLUMN_ITEM_DISCOUNT))+"\t"+total+"\n");//example I don't know the order you need
            totalQty = totalQty + c.getInt(c.getColumnIndex(COLUMN_ITEM_QUANTITY));
            totalGross = totalGross + price;
            totalDeduction = totalDeduction + c.getDouble(c.getColumnIndex(COLUMN_ITEM_DISCOUNT));
            c.moveToNext();
        }
            c.close();

    totalItemSales = totalGross - totalDeduction;
        toBePrinted.add("----------------------------------------------");
        toBePrinted.add("TOTAL QTY\t\t"+totalQty+".0000");
        toBePrinted.add("GROSS SALES\t\t"+totalGross);
        toBePrinted.add("TOTAL DEDUCTIONS\t\t-"+totalDeduction);
        Double totalNet = totalGross - totalDeduction;
        toBePrinted.add("NET SALES\t\t"+totalNet+"\n\n\n");
        toBePrinted.add("----------------------------------------------");
        toBePrinted.add("HOURLY SALES\t\t\t\t\tAMOUNT");
        for(int mHour=0;mHour<24;mHour++){
            if(mHour<12) {
                String time = String.format("%1$02d", mHour);
                toBePrinted.add(time+":00AM - "+time+":59AM\t\t"+hourlyGrossSale[mHour]);
            }
            else {
                String time = String.format("%1$02d", mHour-12);
                toBePrinted.add(time+":00PM - "+time+":59PM\t\t"+hourlyGrossSale[mHour]);
            }
        }

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
//        toBePrinted.add("----------------------------------------------");
//        toBePrinted.add("NO DEPARTMENT");
//        toBePrinted.add("\t\t\tXX.XX");
//        toBePrinted.add("xX.XXXX\td-X.XX\tXX.XX");
//        toBePrinted.add("NO DEPARTMENT");
//        toBePrinted.add("\t\t\tXXX.XX");
//        toBePrinted.add("xX.XXXX\td-XX.XX\tXXX.XX");
//        toBePrinted.add("NO DEPARTMENT");
//        toBePrinted.add("\t\t\tX,XXX.XX");
//        toBePrinted.add("xXXXX.XXXX\td-X.XX\tX,XXX.XX");
//        toBePrinted.add("----------------------------------------------");
//        toBePrinted.add("TOTAL QTY\t\tXXX.XXXX");
//        toBePrinted.add("GROSS SALES\t\tX,XXX.XX");
//        toBePrinted.add("TOTAL DEDUCTIONS\t\t-XX.XX");
//        toBePrinted.add("NET SALES\t\tX,XXX.XX\n");
//
//        toBePrinted.add("GROUP SALES\t\tAMOUNT");
//        toBePrinted.add("----------------------------------------------");
//        toBePrinted.add("NO GROUP");
//        toBePrinted.add("\t\t\tXX.XX");
//        toBePrinted.add("xX.XXXX\td-X.XX\tXX.XX");
//        toBePrinted.add("NO GROUP");
//        toBePrinted.add("\t\t\tXXX.XX");
//        toBePrinted.add("xX.XXXX\td-XX.XX\tXXX.XX");
//        toBePrinted.add("NO GROUP");
//        toBePrinted.add("\t\t\tX,XXX.XX");
//        toBePrinted.add("xXXXX.XXXX\td-X.XX\tX,XXX.XX");
//        toBePrinted.add("----------------------------------------------");
//        toBePrinted.add("TOTAL QTY\t\tXXX.XXXX");
//        toBePrinted.add("GROSS SALES\t\tX,XXX.XX");
//        toBePrinted.add("TOTAL DEDUCTIONS\t\t-XX.XX");
//        toBePrinted.add("NET SALES\t\tX,XXX.XX\n");
//
//        toBePrinted.add("BRAND SALES\t\tAMOUNT");
//        toBePrinted.add("----------------------------------------------");
//        toBePrinted.add("NO BRAND");
//        toBePrinted.add("\t\t\tXX.XX");
//        toBePrinted.add("xX.XXXX\td-X.XX\tXX.XX");
//        toBePrinted.add("NO BRAND");
//        toBePrinted.add("\t\t\tXXX.XX");
//        toBePrinted.add("xX.XXXX\td-XX.XX\tXXX.XX");
//        toBePrinted.add("NO BRAND");
//        toBePrinted.add("\t\t\tX,XXX.XX");
//        toBePrinted.add("xXXXX.XXXX\td-X.XX\tX,XXX.XX");
//        toBePrinted.add("----------------------------------------------");
//        toBePrinted.add("TOTAL QTY\t\tXXX.XXXX");
//        toBePrinted.add("GROSS SALES\t\tX,XXX.XX");
//        toBePrinted.add("TOTAL DEDUCTIONS\t\t-XX.XX");
//        toBePrinted.add("NET SALES\t\tX,XXX.XX\n");
//
//        toBePrinted.add("CATEGORY SALES\t\tAMOUNT");
//        toBePrinted.add("----------------------------------------------");
//        toBePrinted.add("NO CATEGORY");
//        toBePrinted.add("\t\t\tXX.XX");
//        toBePrinted.add("xX.XXXX\td-X.XX\tXX.XX");
//        toBePrinted.add("NO CATEGORY");
//        toBePrinted.add("\t\t\tXXX.XX");
//        toBePrinted.add("xX.XXXX\td-XX.XX\tXXX.XX");
//        toBePrinted.add("NO CATEGORY");
//        toBePrinted.add("\t\t\tX,XXX.XX");
//        toBePrinted.add("xXXXX.XXXX\td-X.XX\tX,XXX.XX");
//        toBePrinted.add("----------------------------------------------");
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
//             while (!cursor.isAfterLast()) {
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


        cProd = db_data.getAllProductsSample();
        cProd.moveToFirst();

//        while(cProd.moveToNext()){
//            toBePrinted.add(cProd.getString(cProd.getColumnIndex(COLUMN_PRODUCT_NAME_TEMP))+"\n"+cProd.getString(cProd.getColumnIndex(COLUMN_PRODUCT_DESCRIPTION_TEMP)));//example I don't know the order you need
//            toBePrinted.add("\n\n");
//        }
//        cProd.close();
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
    Cursor getCursorInReportBaKamo(){
        return cProd;
    }

    ArrayList<String> getToBePrinted(){
        return toBePrinted;
    }
}