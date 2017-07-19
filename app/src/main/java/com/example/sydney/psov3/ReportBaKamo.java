package com.example.sydney.psov3;

import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;


/**
 * Created by Poging Adam on 6/28/2017.
 */

class ReportBaKamo {
    private DB_Data db_data;
    private ArrayList<String> toBePrinted = new ArrayList<>();
    SQLiteDatabase dbReader;
    String gross, discount, vsale, xsale, zsale, vtax, xtax, ztax, zf, t1, t2, or1, or2, ogt, ngt, trans;
    Double net_gross, net_discount, net, dogt, dngt, over, totalGross=0.0, totalDeduction=0.0, totalItemSales=0.0;
    int z, t3, totalQty=0;
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
        vsale = db_data.pleaseGetTheSalesForMe(x,"0");
        xsale = db_data.pleaseGetTheSalesForMe(x,"1");
        zsale = db_data.pleaseGetTheSalesForMe(x,"2");
        vtax = db_data.pleaseGetTheTaxForMe(x,"0");
        xtax = db_data.pleaseGetTheTaxForMe(x,"1");
        ztax = db_data.pleaseGetTheTaxForMe(x,"2");
        z = db_data.pleaseGiveMeTheZCount();
        transArray = db_data.pleaseGiveMeTheFirstAndLastOfTheTransactions();
        or = db_data.pleaseGiveMeTheFirstAndLastOfTheOfficialReceipt();
        ogt = db_data.getMyOldGross();
        items = db_data.getAllItems(x);

        net_gross = Double.parseDouble(gross);
        net_discount = Double.parseDouble(discount);
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

        toBePrinted.add("ABZTRACK DEMO STORE");
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
            toBePrinted.add("BIZDATE : " + dateformatted + " " + currentTime);
            toBePrinted.add("CASHIER : "+x);
            toBePrinted.add("SHIFT : 1\t\tTRANS#"+trans+"\n");
        }
        toBePrinted.add("GROSS SALES\t\t"+gross);
        toBePrinted.add(" SALES DISCOUNT\t\t-"+discount);
        toBePrinted.add("----------------------------------------------");
        toBePrinted.add("NET SALES\t\t"+ net +"\n");

        toBePrinted.add("TAX CODE\tSALES\tTAX");
        toBePrinted.add("----------------------------------------------");
//        toBePrinted.add("[n] N-Sal\tX.XX\tX.XX");
        toBePrinted.add("[v] V-Sal\t"+vsale+"\t"+vtax);
        toBePrinted.add("[x] E-Sal\t"+xsale+"\t"+xtax);
        toBePrinted.add("[z] Z-Rat\t"+zsale+"\t"+ztax+"\n");

        if(x.equals("no")){
            toBePrinted.add("OLD GT\t000-"+ogt);
            toBePrinted.add("NEW GT\t000-"+dngt+"\n");

            toBePrinted.add("Z Count\t\t"+zf+"\n");

           toBePrinted.add("Trans #\t\t"+t1+" - "+t2);
            toBePrinted.add("\t\t\t"+t3+"\n");

            toBePrinted.add("OR #\t\t"+or1+ " - "+or2);
        }
        toBePrinted.add("CASH SALES\t\t"+net);
        toBePrinted.add("----------------------------------------------");
        toBePrinted.add("CASH IN DRAWER\t\t"+net);

        toBePrinted.add("CASH COUNT\t\t"+moneyCount);
        toBePrinted.add("----------------------------------------------");
        toBePrinted.add("CASH SHORT/OVER\t\t"+over+"\n");

        toBePrinted.add("TRANSACTION\t\tAMOUNT");
        toBePrinted.add("----------------------------------------------");
        toBePrinted.add("NORMAL SALES\t\tX,XXX.XX\n");

        toBePrinted.add("TENDER\t\tAMOUNT");
        toBePrinted.add("----------------------------------------------");
        toBePrinted.add("TOTAL CASH\t0\t0.00");
        toBePrinted.add("  CC BDO\tX\t0.00");
        toBePrinted.add("  CC BPI\tX\t0.00");
        toBePrinted.add("TOTAL CREDIT CA\t\t0.00\n");

        toBePrinted.add("DISCOUNT\t\tAMOUNT");
        toBePrinted.add("----------------------------------------------");
        toBePrinted.add("SCD 20%\tX\t-0.00");
        toBePrinted.add("Tax - Exempt\tX\t-0.00");
        toBePrinted.add("TOTAL DEDUCTION\tX\t-0.00\n");

        toBePrinted.add("ITEM SALES\t\tAMOUNT");
        toBePrinted.add("----------------------------------------------");
        for(int i=0;i<items.size();i++){
            String[] myString;
            myString=items.get(i).toArray(new String[items.size()]);
            Double price = Double.parseDouble(myString[4]);
            Double discount = Double.parseDouble(myString[3]);
            Double net = price-discount;
            toBePrinted.add(myString[0]);
            toBePrinted.add(myString[1]+"\t\t"+myString[4]);
            toBePrinted.add("x"+myString[2]+".0000"+"\td-"+myString[3]+"\t"+net);
            totalQty = totalQty + Integer.parseInt(myString[2]);
            totalGross = totalGross + Double.parseDouble(myString[4]);
            totalDeduction = totalDeduction + Double.parseDouble(myString[3]);
    }
    totalItemSales = totalGross - totalDeduction;
        toBePrinted.add("----------------------------------------------");
        toBePrinted.add("TOTAL QTY\t\t"+totalQty+".0000");
        toBePrinted.add("GROSS SALES\t\t"+totalGross);
        toBePrinted.add("TOTAL DEDUCTIONS\t\t-"+totalDeduction);
        toBePrinted.add("NET SALES\t\t"+totalItemSales+"\n");
//
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