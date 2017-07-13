package com.example.sydney.psov3;

import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Poging Adam on 6/28/2017.
 */

class ReportBaKamo {
    private DB_Data db_data;
    private ArrayList<String> toBePrinted = new ArrayList<>();
    SQLiteDatabase dbReader;

    void main(String x, String date, int transNum){
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
            // Pad with zeros and a width of 6 chars.
            String trans = String.format("%1$06d", transNum);

        toBePrinted.add("NOVA RESTAURANT & SERVICE INC.");
        toBePrinted.add("MILKY WAY");
        toBePrinted.add("VAT REG TIN:000-482-511-001");
        toBePrinted.add("MIN:15040610033393902");
        toBePrinted.add("PLANT 25 LOPEZ DRIVE");
        toBePrinted.add("POBLACION, MAKATI CITY");
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
        String gross = db_data.getGrossSales(x);
        toBePrinted.add("GROSS SALES\t\t"+gross);
        String discount = db_data.getDiscountSales(x);

        toBePrinted.add(" SALES DISCOUNT\t\t-"+discount);
        toBePrinted.add("-----------------------------------------------");
        double net_gross = Double.parseDouble(gross);
        double net_discount = Double.parseDouble(discount);
        double net = net_gross-net_discount;
        toBePrinted.add("NET SALES\t\t"+net+"\n");

        toBePrinted.add("TAX CODE\tSALES\tTAX");
        toBePrinted.add("-----------------------------------------------");
//        toBePrinted.add("[n] N-Sal\tX.XX\tX.XX");
        String vsale =  db_data.pleaseGetTheSalesForMe(x,"v");
        String vtax =  db_data.pleaseGetTheTaxForMe(x,"v");
        toBePrinted.add("[v] V-Sal\t"+vsale+"\t"+vtax);
        String xsale =  db_data.pleaseGetTheSalesForMe(x,"v");
        String xtax =  db_data.pleaseGetTheTaxForMe(x,"v");
        toBePrinted.add("[x] E-Sal\t"+xsale+"\t"+xtax);
        String zsale =  db_data.pleaseGetTheSalesForMe(x,"v");
        String ztax =  db_data.pleaseGetTheTaxForMe(x,"v");
        toBePrinted.add("[z] Z-Rat\t"+zsale+"\t"+ztax+"\n");

        if(x.equals("no")){
            toBePrinted.add("OLD GT\tXXX-XXXXXXXXXXXX.XX");
            toBePrinted.add("NEW GT\tXXX-XXXXXXXXXXXX.XX\n");

            int z = db_data.pleaseGiveMeTheZCount();
            String zf = String.format("%$05d", z);
            toBePrinted.add("Z Count\t\t"+zf+"\n");

            int transArray[] = new int[1];
            transArray = db_data.pleaseGiveMeTheFirstAndLastOfTheTransactions();
            String t1 = String.format("%$06d", transArray[0]);
            String t2 = String.format("%$06d", transArray[1]);
            toBePrinted.add("Trans #\t\t"+t1+" - "+t2);
            int t3 = transArray[1] - transArray[0];
            toBePrinted.add("\t\t\t"+t3+"\n");

            int or[];
            or = db_data.pleaseGiveMeTheFirstAndLastOfTheOfficialReceipt();
            String or1 = String.format("%$06d", or[0]);
            String or2 = String.format("%$06d", or[1]);
            toBePrinted.add("OR #\t\t"+or1+ " - "+or2);
            int or3 = or[1] - or[0];
            toBePrinted.add("\t\t\t"+or3+"\n");
        }
        toBePrinted.add("CASH SALES\t\t"+net);
        toBePrinted.add("-----------------------------------------------");
        toBePrinted.add("CASH IN DRAWER\t\tXXX.XX");

//        toBePrinted.add("CASH COUNT\t\tXXX.XX");
//        toBePrinted.add("-----------------------------------------------");
//        toBePrinted.add("CASH SHORT/OVER\t\tX.XX\n");
//
//        toBePrinted.add("TRANSACTION\t\tAMOUNT");
//        toBePrinted.add("-----------------------------------------------");
//        toBePrinted.add("NORMAL SALES\t\tX,XXX.XX\n");
//
//        toBePrinted.add("TENDER\t\tAMOUNT");
//        toBePrinted.add("-----------------------------------------------");
//        toBePrinted.add("TOTAL CASH\tX\tXXX.XX");
//        toBePrinted.add("  CC BDO\tX\tX,XXX.XX");
//        toBePrinted.add("  CC BPI\tX\tX,XXX.XX");
//        toBePrinted.add("TOTAL CREDIT CA\t\tXX,XXX.XX\n");
//
//        toBePrinted.add("DISCOUNT\t\tAMOUNT");
//        toBePrinted.add("-----------------------------------------------");
//        toBePrinted.add("SCD 20%\tX\t-XX.XX");
//        toBePrinted.add("Tax - Exempt\tX\t-XX.XX");
//        toBePrinted.add("TOTAL DEDUCTION\tX\t-XXX.XX\n");
//
//        toBePrinted.add("ITEM SALES\t\tAMOUNT");
//        toBePrinted.add("-----------------------------------------------");
//        toBePrinted.add("MILO ACTIV-GO 22GMS.");
//        toBePrinted.add("BEV-001\t\tX,XXX.XX");
//        toBePrinted.add("xXXXX.XXXX\td-X.XX\tX,XXX.XX");
//        toBePrinted.add("QUAKER OAT CEREAL DRINK 29g");
//        toBePrinted.add("BEV-002\t\tXX.XX");
//        toBePrinted.add("xX.XXXX\td-X.XX\tXX.XX");
//        toBePrinted.add("TAPSILOG");
//        toBePrinted.add("FOOD-004\t\tXXX.XX");
//        toBePrinted.add("xX.XXXX\td-XX.XX\tXXX.XX");
//        toBePrinted.add("-----------------------------------------------");
//        toBePrinted.add("TOTAL QTY\t\tXXX.XXXX");
//        toBePrinted.add("GROSS SALES\t\tX,XXX.XX");
//        toBePrinted.add("TOTAL DEDUCTIONS\t\t-XX.XX");
//        toBePrinted.add("NET SALES\t\tX,XXX.XX\n");
//
//        toBePrinted.add("DEPARTMENT SALES\t\tAMOUNT");
//        toBePrinted.add("-----------------------------------------------");
//        toBePrinted.add("NO DEPARTMENT");
//        toBePrinted.add("\t\t\tXX.XX");
//        toBePrinted.add("xX.XXXX\td-X.XX\tXX.XX");
//        toBePrinted.add("NO DEPARTMENT");
//        toBePrinted.add("\t\t\tXXX.XX");
//        toBePrinted.add("xX.XXXX\td-XX.XX\tXXX.XX");
//        toBePrinted.add("NO DEPARTMENT");
//        toBePrinted.add("\t\t\tX,XXX.XX");
//        toBePrinted.add("xXXXX.XXXX\td-X.XX\tX,XXX.XX");
//        toBePrinted.add("-----------------------------------------------");
//        toBePrinted.add("TOTAL QTY\t\tXXX.XXXX");
//        toBePrinted.add("GROSS SALES\t\tX,XXX.XX");
//        toBePrinted.add("TOTAL DEDUCTIONS\t\t-XX.XX");
//        toBePrinted.add("NET SALES\t\tX,XXX.XX\n");
//
//        toBePrinted.add("GROUP SALES\t\tAMOUNT");
//        toBePrinted.add("-----------------------------------------------");
//        toBePrinted.add("NO GROUP");
//        toBePrinted.add("\t\t\tXX.XX");
//        toBePrinted.add("xX.XXXX\td-X.XX\tXX.XX");
//        toBePrinted.add("NO GROUP");
//        toBePrinted.add("\t\t\tXXX.XX");
//        toBePrinted.add("xX.XXXX\td-XX.XX\tXXX.XX");
//        toBePrinted.add("NO GROUP");
//        toBePrinted.add("\t\t\tX,XXX.XX");
//        toBePrinted.add("xXXXX.XXXX\td-X.XX\tX,XXX.XX");
//        toBePrinted.add("-----------------------------------------------");
//        toBePrinted.add("TOTAL QTY\t\tXXX.XXXX");
//        toBePrinted.add("GROSS SALES\t\tX,XXX.XX");
//        toBePrinted.add("TOTAL DEDUCTIONS\t\t-XX.XX");
//        toBePrinted.add("NET SALES\t\tX,XXX.XX\n");
//
//        toBePrinted.add("BRAND SALES\t\tAMOUNT");
//        toBePrinted.add("-----------------------------------------------");
//        toBePrinted.add("NO BRAND");
//        toBePrinted.add("\t\t\tXX.XX");
//        toBePrinted.add("xX.XXXX\td-X.XX\tXX.XX");
//        toBePrinted.add("NO BRAND");
//        toBePrinted.add("\t\t\tXXX.XX");
//        toBePrinted.add("xX.XXXX\td-XX.XX\tXXX.XX");
//        toBePrinted.add("NO BRAND");
//        toBePrinted.add("\t\t\tX,XXX.XX");
//        toBePrinted.add("xXXXX.XXXX\td-X.XX\tX,XXX.XX");
//        toBePrinted.add("-----------------------------------------------");
//        toBePrinted.add("TOTAL QTY\t\tXXX.XXXX");
//        toBePrinted.add("GROSS SALES\t\tX,XXX.XX");
//        toBePrinted.add("TOTAL DEDUCTIONS\t\t-XX.XX");
//        toBePrinted.add("NET SALES\t\tX,XXX.XX\n");
//
//        toBePrinted.add("CATEGORY SALES\t\tAMOUNT");
//        toBePrinted.add("-----------------------------------------------");
//        toBePrinted.add("NO CATEGORY");
//        toBePrinted.add("\t\t\tXX.XX");
//        toBePrinted.add("xX.XXXX\td-X.XX\tXX.XX");
//        toBePrinted.add("NO CATEGORY");
//        toBePrinted.add("\t\t\tXXX.XX");
//        toBePrinted.add("xX.XXXX\td-XX.XX\tXXX.XX");
//        toBePrinted.add("NO CATEGORY");
//        toBePrinted.add("\t\t\tX,XXX.XX");
//        toBePrinted.add("xXXXX.XXXX\td-X.XX\tX,XXX.XX");
//        toBePrinted.add("-----------------------------------------------");
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