package com.example.sydney.psov3;

import android.database.Cursor;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Adam on 6/28/2017.
 */

class ReportBaKamo {
    private double cashFlow;
    private double grossTotal;
    private double deductionTotal;
    private int quantityTotal;
    private DB_Data db_data;
    private ArrayList<String> printArray = new ArrayList<>();
    private DecimalFormat moneyDecimal = new DecimalFormat("0.00");

    void main(String reportType, String date, int transactionNumber,
              double moneyCount) throws ParseException {
        Date currDate = new Date();
        final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MMM-dd-yy");
        String dateToStr = dateTimeFormat.format(currDate);
//        Date strToDate = null;
//        strToDate = dateTimeFormat.parse(dateToStr);
//        String dateToString = strToDate.toString();
//        Log.e("main in ReportBakamo ", dateToStr);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentTime = sdf.format(new Date());

//        SerialPrinter mSerialPrinter = SerialPrinter.GetSerialPrinter();
//        try {
//            mSerialPrinter.OpenPrinter(new SerialParam(9600, "/dev/ttyS3", 0), new SerialDataHandler());
//            HdxUtil.SetPrinterPower(1);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
        String reportLetter;
        if (reportType.equals("no")) {
            reportLetter = "Z";
        }
        else {
            reportLetter = "X";
        }
        double[] grossHourly = db_data.getHourlyGross(reportType);
        double grandTotalOld = db_data.getGrandTotalOld();
        double grossNormal = db_data.getNormalSales(reportType);
        double creditSales = db_data.getCreditSales(reportType);
        String discountSales = db_data.getDiscountSales(reportType);
        String saleVatted = db_data.getSale("0", reportType);
        String saleExempt = db_data.getSale("1", reportType);
        String saleZero = db_data.getSale("2", reportType);
        String tax = db_data.getTax("0", reportType);
        int zCount = db_data.getZCount();
        int refundCount = db_data.getRefundCount(reportType);
        double refundTotal = db_data.getTotalRefund(reportType);
        int[] transactionExtent = db_data.getFirstAndLastTransaction();
        int[] invoiceExtent = db_data.getFirstAndLastReceipt();
        if (!reportType.equals("no")) {
            cashFlow = db_data.getCashFlow(reportType, dateToStr);
        }

        double lessVat;
        double exemptDiscount;
        double exemptDiscountFinal;

        try {
            exemptDiscount = Double.parseDouble(saleZero);
            lessVat = (Double.parseDouble(saleExempt) / 1.12) * .20;
            exemptDiscountFinal = exemptDiscount * .12;
        } catch (Exception e) {
            exemptDiscount = 0.0;
            lessVat = 0.0;
            exemptDiscountFinal = exemptDiscount * .12;
        }

        double discountTotal = Double.parseDouble(discountSales) + exemptDiscountFinal;
        double saleNet = (grossNormal + creditSales) - discountTotal;
        String invoiceStart = String.format(Locale.ENGLISH, "%1$06d", invoiceExtent[0]);
        String invoiceEnd = String.format(Locale.ENGLISH, "%1$06d", invoiceExtent[1]);
        String transactionString = String.format(Locale.ENGLISH, "%1$06d", transactionNumber);
        double cashDifference = moneyCount - (grossNormal + cashFlow);
        // Pad with zeros and a width of 6 chars.

        printArray.add("ABZTRAK DEMO STORE");
        printArray.add("VAT REG TIN:000-111-111-001");
        printArray.add("MIN:12345678901234567");
        printArray.add("2/F 670 SGT BUMATAY ST.");
        printArray.add("PLAINVIEW, MANDALUYONG");
        printArray.add("SERIAL NO. ASDFG1234567890");
        printArray.add("PTU No. FP121234-123-1234567-12345\n");
        printArray.add("==============================================");
        printArray.add(reportLetter + "-READ");
        if (reportType.equals("no")) {
            printArray.add("END-OF-DAY REPORT");
            printArray.add("==============================================");
            printArray.add("BIZDATE : " + date);
            printArray.add("BRANCH : HEAD OFFICE");
            printArray.add("SHIFT : ALL\t\tTRANS#" + transactionString + "\n");
        }else {
            printArray.add("CASHIER REPORT");
            printArray.add("==============================================");
            printArray.add("BIZDATE : " + dateToStr + " " + currentTime);
            printArray.add("CASHIER : " + reportType);
            printArray.add("TRANS#" + transactionString + "\n");
        }
        printArray.add("GROSS SALES\t\t" + moneyDecimal.format(grossNormal +
                creditSales));
        printArray.add(" SALES DISCOUNT\t\t-" +
                moneyDecimal.format(discountTotal));
        printArray.add("----------------------------------------------");
        printArray.add("NET SALES\t\t" +
                moneyDecimal.format(saleNet) + "\n");

        printArray.add("REFUND\tNo.\t" + refundCount);
        printArray.add(moneyDecimal.format(refundTotal) + "\n");

        printArray.add("TAX CODE\tSALES\tTAX");
        printArray.add("----------------------------------------------");
//        toBePrinted.add("[n] N-Sal\tX.XX\tX.XX");
        printArray.add("[v] V-Sale\t" + saleVatted);
        printArray.add("[t] V-12%\t" + tax);
        printArray.add("[x] E-Sale\t" + saleExempt);
        printArray.add("[z] Z-Rate\t" + saleZero + "\n");

        if (reportType.equals("no")) {
            printArray.add("OLD GT\t000-" + grandTotalOld);
            printArray.add("NEW GT\t000-" + (grandTotalOld + saleNet) + "\n");
            printArray.add("Z Count\t\t" + String.format(Locale.ENGLISH, "%1$05d", zCount) + "\n");
            printArray.add("Trans #\t\t" + String.format(Locale.ENGLISH, "%1$06d",
                    transactionExtent[0]) + " - " + String.format(Locale.ENGLISH, "%1$06d",
                    transactionExtent[1]));
            printArray.add("\t\t\t" + (transactionExtent[1] - transactionExtent[0]) + "\n");

            printArray.add("OR #\t\t" + invoiceStart + " - " + invoiceEnd);
        }
        printArray.add("CASH SALES\t\t" + moneyDecimal.format(grossNormal));
        printArray.add("----------------------------------------------");
        printArray.add("CASH IN DRAWER\t\t" + moneyDecimal.format(grossNormal + cashFlow));

        printArray.add("CASH COUNT\t\t" + moneyDecimal.format(moneyCount));
        printArray.add("----------------------------------------------");
        printArray.add("CASH SHORT/OVER\t\t" + moneyDecimal.format(cashDifference) + "\n");
//        printArray.add("TRANSACTION\t\tAMOUNT");
        printArray.add("----------------------------------------------");
//        printArray.add("NORMAL SALES\t\t"+grossNormal+"\n");

        printArray.add("TENDER\t\tAMOUNT");
        printArray.add("----------------------------------------------");
        printArray.add("TOTAL CASH\t" + moneyDecimal.format(grossNormal));
        printArray.add("TOTAL CREDIT CA\t" + moneyDecimal.format(creditSales) + "\n");

        printArray.add("DISCOUNT\t\tAMOUNT");
        printArray.add("----------------------------------------------");
        printArray.add("SCD 20%\t\t-" + discountSales);
        printArray.add("Less VAT\t\t-" + moneyDecimal.format(lessVat));
        printArray.add("TOTAL DEDUCTION\t\t-" + moneyDecimal.format(discountTotal) + "\n");

        printArray.add("ITEM SALES\t\tAMOUNT");
        printArray.add("----------------------------------------------");
        Cursor cursorItem = db_data.getItems(reportType);
        cursorItem.moveToFirst();

        while (!cursorItem.isAfterLast()) {
            double mItemPrice = cursorItem.getDouble(4) * cursorItem.getInt(2);
            printArray.add(cursorItem.getString(0) + "\n" + cursorItem.getString(1) + "\t" + cursorItem.getDouble(4));
            printArray.add("x" + cursorItem.getString(2) +
                    ".0000\td-" + cursorItem.getString(3) + "\t" +
                    (mItemPrice - cursorItem.getDouble(3)) + "\n");
            quantityTotal = quantityTotal + cursorItem.getInt(2);
            grossTotal = grossTotal + mItemPrice;
            deductionTotal = deductionTotal + cursorItem.getDouble(3);
            cursorItem.moveToNext();
        }
        cursorItem.close();

        printArray.add("----------------------------------------------");
        printArray.add("TOTAL QTY\t\t" + quantityTotal + ".0000");
        printArray.add("GROSS SALES\t\t" + moneyDecimal.format(grossTotal));
        printArray.add("TOTAL DEDUCTIONS\t\t-" + moneyDecimal.format(deductionTotal));
        printArray.add("NET SALES\t\t" + moneyDecimal.format(grossTotal - deductionTotal) + "\n\n\n");
        printArray.add("----------------------------------------------");
        printArray.add("HOURLY SALES\t\t\t\t\tAMOUNT");
        for(int mHour=0;mHour<24;mHour++){
            String mTime = String.format(Locale.ENGLISH, "%1$02d", mHour);
            if (grossHourly[mHour] > 0)
                printArray.add(mTime + ":00 - " + mTime + ":59\t\t" +
                        moneyDecimal.format(grossHourly[mHour]));
        }
        quantityTotal = 0;
        grossTotal = 0;
        deductionTotal = 0;

//        Cursor testCursor = db_data.testTransaction();
//        testCursor.moveToFirst();
//        while(!testCursor.isAfterLast()){
//            toBePrinted.add(testCursor.getString(0));
//            toBePrinted.add(testCursor.getString(1));
//            testCursor.moveToNext();
//        }
//        testCursor.close();
//        Cursor cursor = db_data.getSale();
//        cursor.moveToFirst();
//        toBePrinted.add("Vattable"+cursor.getInt(0));
//        cursor.close();
//
//        Cursor cursor1 = db_data.getTax();
//        cursor1.moveToFirst();
//        toBePrinted.add("Vat"+cursor1.getInt(0));
//        cursor1.close();
        printArray.add("\n\n\n\n\n");
        printArray.add("");

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
//        Cursor cursor = db_data.searchInvoiceTransactions(reportType);
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


//        Cursor cProd = db_data.getAllProductsSample();
//        cProd.moveToFirst();

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

    ArrayList<String> getPrintArray() {
        return printArray;
    }
}