package com.example.sydney.psov3.drawer;

import android.printservice.PrintService;

/**
 * Created by sydney on 6/7/2017.
 */

public class cashdrawerOpen {
    public void cashdrawerOpen() {

        byte[] open = {27, 112, 48, 55, 121};
// byte[] cutter = {29, 86,49};
        String printer = PrinterName;
        PrintServiceAttributeSet printserviceattributeset = new HashPrintServiceAttributeSet();
        printserviceattributeset.add(new PrinterName(printer,null));
        PrintService[] printservice = PrintServiceLookup.lookupPrintServices(null, printserviceattributeset);
        if(printservice.length!=1){
            System.out.println("Printer not found");
        }
        PrintService pservice = printservice[0];
        DocPrintJob job = pservice.createPrintJob();
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        Doc doc = new SimpleDoc(open,flavor,null);
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        try {
            job.print(doc, aset);
        } catch (PrintException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
