package com.example.sydney.psov3;
import android.provider.BaseColumns;

/**
 * Created by sydney on 4/25/2017.
 */

interface Constants extends BaseColumns {
    String TABLE_ADMIN = "tbl_admin"; //Table name = tbl_admin
    String COLUMN_ADMIN_USERNAME = "Username"; //Field name = Firstname
    String COLUMN_ADMIN_PASSWORD = "Password"; //Field name = Password

    String TABLE_CASHIER = "tbl_cashier"; //Table name = tbl_cashier
    String COLUMN_CASHIER_NUMBER = "Cashiernum"; //Field name = Cashiernum
    String COLUMN_CASHIER_NAME = "Name"; //Field name = Name
    String COLUMN_CASHIER_POSITION = "Position"; //Field name = Position
    String COLUMN_CASHIER_PASSWORD = "Password"; //Field name = Password

    String TABLE_PRODUCT = "tbl_product";
    String COLUMN_PRODUCT_ID = "ProdId";
    String COLUMN_PRODUCT_NAME = "ProdName";
    String COLUMN_PRODUCT_DESCRIPTION = "ProdDesc";
    String COLUMN_PRODUCT_PRICE = "ProdPrice";
    String COLUMN_PRODUCT_QUANTITY = "ProdQuan";
    String COLUMN_PRODUCT_VATABLE = "Vatable";
    String COLUMN_PRODUCT_IMEI = "ProdIMEI";

    String TABLE_INVOICE = "tbl_invoice";
    String COLUMN_INVOICE_TRANSACTION_NUMBER = "InTrans";
    String COLUMN_INVOICE_DISCOUNT = "InDisc"; //DISCOUNT TYPE like ND,SENIOR/PWD/Diplomat
    String COLUMN_INVOICE_NORMALSALE = "InCustomer"; //Value from cash of customer - PHYSICAL SASHING or TOTAL PRICE
    String COLUMN_INVOICE_PRINT = "InPrint"; //ALL TEXT(JOURNAL) TO BE PRINTED
    String COLUMN_INVOICE_CASHIER_NUMBER = "InCashierNum"; //CASHIER IDENTIFICATION
    String COLUMN_INVOICE_ZREPORT_STATUS = "InZreport";
    String COLUMN_INVOICE_XREPORT_STATUS = "InXreport";
    String COLUMN_INVOICE_VATTABLE = "InVattable"; //FROM VATABLE IF CUSTOMER HAS NO DISCOUNT!
    //    String COLUMN_INVOICE_EXEMPT="InExempt";
    String COLUMN_INVOICE_VATTED = "InVatted"; //TAX OF VATABLE ITEMS
    String COLUMN_INVOICE_VAT_STATUS = "InVatStatus"; //V,X,Z
    String COLUMN_INVOICE_CREDITSALE = "InCreditSale";
    String COLUMN_INVOICE_DATEANDTIME = "InDateTime";
    String COLUMN_INVOICE_CREDITCARDNUMBER = "InCreditNumber";
    String COLUMN_INVOICE_CREDITDATEOFEXPIRATION = "InCreditExpiration";

    String TABLE_ITEM = "tbl_item";
    String COLUMN_ITEM_INVOICE = "ItemIn";
    String COLUMN_ITEM_NAME = "ItemName";
    String COLUMN_ITEM_DESC = "ItemDesc";
    String COLUMN_ITEM_PRICE = "ItemTotalPrice";
    String COLUMN_ITEM_PRODUCT = "ItemProd";
    String COLUMN_ITEM_QUANTITY = "ItemQuan";
    String COLUMN_ITEM_STATUS = "ItemStatus";
    String COLUMN_ITEM_DISCOUNT = "ItemDiscount";
    String COLUMN_ITEM_ZREPORT = "ItemZ";
    String COLUMN_ITEM_XREPORT = "ItemX";
    String COLUMN_ITEM_CASHIER = "ItemCashier";

    String TABLE_XREPORT = "tbl_xreport";
    String COLUMN_XREPORT_TRANSACTION_NUMBER = "xreportTransNum";
    String COLUMN_XREPORT_CASHSALES = "xreportCashsales";
    String COLUMN_XREPORT_CASHCOUNT = "xreportCashcount";
    String COLUMN_XREPORT_CASHSHORTOVER = "xreportCashShort";

    String TABLE_ZREPORT = "tbl_zreport";
    String COLUMN_ZREPORT_TRANSACTION_NUMBER = "zreportTransNum";
    String COLUMN_ZREPORT_CASHSALES = "zreportCashsales";
    String COLUMN_ZREPORT_CASHCOUNT = "zreportCashcount";
    String COLUMN_ZREPORT_PRINT = "zreportPrint";

    String TABLE_TRANSACTION = "tbl_trans";
    String COLUMN_TRANSACTION_TYPE = "TransType";
    String COLUMN_TRANSACTION_DATETIME = "TransDateTime";
    String COLUMN_TRANSACTION_CASHIER = "TransCashier";
    String COLUMN_TRANSACTION_ZREPORT = "TransZreport";
    String COLUMN_TRANSACTION_XREPORT = "TransXreport";
    String COLUMN_TRANSACTION_PRINT = "TransPrint";

    String TABLE_TEMP_INVOICING = "tbl_temp";
    String COLUMN_TEMP_NAME = "temp_name";
    String COLUMN_TEMP_DESCRIPTION = "temp_description";
    String COLUMN_TEMP_PRICE = "temp_price";
    String COLUMN_TEMP_QUANTITY = "temp_quantity";
    String COLUMN_TEMP_ID = "temp_id";
    String COLUMN_TEMP_TOTALPRICE = "temp_totalPrice";

    String TABLE_TOTAL= "tbl_total";
    String COLUMN_TOTAL_GRAND= "total_grand";

    String TABLE_NONSALE = "tbl_non_sale";
    String COLUMN_NONSALE_CASH = "non_sale_cash";
    String COLUMN_NONSALE_TYPE = "non_sale_type";
    String COLUMN_NONSALE_XREPORT = "non_sale_x";
    String COLUMN_NONSALE_ZREPORT = "non_sale_z";
    String COLUMN_NONSALE_PRINT = "non_sale_print";
    String COLUMN_NONSALE_DATETIME = "none_sale_date";

    String TABLE_PRODUCT_TEMP = "tbl_product_temp";
    String COLUMN_PRODUCT_ID_TEMP = "ProdId_temp";
    String COLUMN_PRODUCT_NAME_TEMP = "ProdName_temp";
    String COLUMN_PRODUCT_DESCRIPTION_TEMP = "ProdDesc_temp";
    String COLUMN_PRODUCT_PRICE_TEMP = "ProdPrice_temp";
    String COLUMN_PRODUCT_QUANTITY_TEMP = "ProdQuan_temp";
    String COLUMN_PRODUCT_VATABLE_TEMP = "Vatable_temp";
    String COLUMN_PRODUCT_IMEI_TEMP = "ProdIMEI_temp";

    String TABLE_RETRIEVED_JOINTABLE = "tbl_retrieved";
    String COLUMN_RETRIEVED_BARCODE = "total_ret_barcode";
    String COLUMN_RETRIEVED_NAME = "total_ret_name";
    String COLUMN_RETRIEVED_QUANTITY = "total_ret_quantity";
    String COLUMN_RETRIEVED_SOLDITEM = "total_ret_solditem";
    String COLUMN_RETRIEVED_NEWQUANTITY = "total_ret_newquantity";

    String TABLE_PRODUCTLOGS = "tbl_productLogs";
    String COLUMN_PRODUCTLOGS_BARCODE = "CODE";
    String COLUMN_PRODUCTLOGS_TYPE = "TYPE";
    String COLUMN_PRODUCTLOGS_VALUEADDED = "ADDED";
    String COLUMN_PRODUCTLOGS_VALUEMINUS = "SUBTRACT";
    String COLUMN_PRODUCTLOGS_REMARKS = "REMARKS";
    String COLUMN_PRODUCTLOGS_DATE = "DATE";

    String TABLE_CREDIT_CARD = "tbl_credit";
    String COLUMN_CREDIT_TRANS = "creditTrans";
    String COLUMN_CREDIT_CASHIER = "creditCashier";
    String COLUMN_CREDIT_DATE = "creditDate";
    String COLUMN_CREDIT_PAYMENT = "creditPayment";
    String COLUMN_CREDIT_BANK = "creditBank";
    String COLUMN_CREDITT_NUMBER = "creditNumber";
    String COLUMN_CREDIT_EXPIRY = "creditExpiry";

    String TABLE_CASH = "tbl_cash";
    String COLUMN_CASH_TRANSNUM = "cash_num";
    String COLUMN_CASH_CASHIERNUM = "cash_transnum";
    String COLUMN_CASH_DATEANDTIME = "cash_datetime";
    String COLUMN_CASH_ADDCASH = "cash_added";
    String COLUMN_CASH_MINNUSCASH = "cash_subtract";
    String COLUMN_CASH_CURRENTCASH = "cash_current";
    String COLUMN_CASH_ZREPORT = "cash_z";
    String COLUMN_CASH_XREPORT = "cash_x";

    String TABLE_CASHTRANS = "tbl_cashtrans";
    String COLUMN_CASHTRANS_TRANSNUM = "cashtrans_transnum";
    String COLUMN_CASHTRANS_CASHIERNUM = "cashtrans_num";
    String COLUMN_CASHTRANS_DATETIME = "cashtrans_datetime";
    String COLUMN_CASHTRANS_ADDCASH = "cash_added";
    String COLUMN_CASHTRANS_MINNUSCASH = "cash_subtract";
    String COLUMN_CASHTRANS_REASON = "cashtrans_reason";
    String COLUMN_CASHTRANS_REMARKS1 = "cashtrans_remarks1";
    String COLUMN_CASHTRANS_REMARKS2 = "cashtrans_remarks2";
    String COLUMN_CASHTRANS_REMARKS3 = "cashtrans_remarks3";
    String COLUMN_CASHTRANS_REMARKS4 = "cashtrans_remarks4";

    String TABLE_LOG = "tbl_Log";
    String COLUMN_LOG_TYPE = "LogType";
    String COLUMN_LOG_DATETIME = "LogDateTime";
    String COLUMN_LOG_CASHIER = "LogCashier";
    String COLUMN_LOG_PRINT = "LogPrint";

    String TABLE_TERMINAL = "tbl_Terminal";
    String COLUMN_TERMINAL_NAME = "terminal_name";
    String COLUMN_TERMINAL_SERIAL = "terminal_serial";

    String[] HEADER = {"ABZTRAK DEMO STORE",
            "VAT REG TIN:000-111-111-001",
            "MIN:12345678901234567",
            "2/F 670 SGT BUMATAY ST.",
            "PLAINVIEW, MANDALUYONG",
            "SERIAL NO. ASDFG1234567890",
            "PTU No. FP121234-123-1234567-12345",
            "=============================================="};

    String[] TAB_TERMINAL = {"terminal_name", "terminal_serial"};
    String[] TAB_ADMIN = {"Username", "Password"};
    String[] TAB_CASHIER = {"Cashiernum", "Name", "Position", "Password"};
    String[] TAB_PRODUCT = {"ProdId", "ProdName", "ProdDesc", "ProdPrice", "ProdQuan", "Vatable",
            "ProdIMEI"};
    String[] TAB_PRODUCT_TEMP = {"ProdId_temp", "ProdName_temp", "ProdDesc_temp", "ProdPrice_temp",
            "ProdQuan_temp", "Vatable_temp", "ProdIMEI_temp"};
    String[] TAB_INVOICE = {"InTrans", "InDisc", "InCustomer", "InPrint", "InCashierNum", "InZreport",
            "InXreport", "InVattable", "InVatted", "InVatStatus", "InCreditSale", "InDateTime",
            "InCreditNumber", "InCreditExpiration"};
    String[] TAB_ITEM = {"ItemIn", "ItemName", "ItemDesc", "ItemTotalPrice", "ItemProd", "ItemQuan",
            "ItemStatus", "ItemDiscount", "ItemZ", "ItemX", "ItemCashier"};
    String[] TAB_PRODUCTLOGS = {"CODE", "TYPE", "ADDED", "SUBTRACT", "REMARKS", "DATE"};
    String[] TAB_CREDIT_CARD = {"creditTrans", "creditCashier", "creditDate", "creditPayment",
            "creditBank", "creditNumber", "creditExpiry"};
    String[] TAB_XREPORT = {"xreportTransNum", "xreportCashsales", "xreportCashcount",
            "xreportCashShort"};
    String[] TAB_ZREPORT = {"zreportTransNum", "zreportCashsales", "zreportCashcount",
            "zreportPrint"};
    String[] TAB_TRANSACTION = {"TransType", "TransDateTime", "TransCashier", "TransZreport",
            "TransXreport", "TransPrint"};
    String[] TAB_LOG = {"LogType", "LogDateTime", "LogCashier", "LogPrint"};
    String[] TAB_TOTAL = {"total_grand"};
    String[] TAB_CASH = {"cash_num", "cash_transnum", "cash_datetime", "cash_added", "cash_subtract",
            "cash_current", "cash_z", "cash_x"};
    String[] TAB_CASHTRANS = {"cashtrans_transnum", "cashtrans_num", "cashtrans_datetime",
            "cash_added", "cash_subtract", "cashtrans_reason", "cashtrans_remarks1",
            "cashtrans_remarks2", "cashtrans_remarks3", "cashtrans_remarks4"};
    String[] TAB_RETRIEVED_JOINTAB = {};
    String[] TAB_TEMP_INVOICING = {};

    String[] tableArray = {TABLE_TERMINAL, TABLE_ADMIN, TABLE_CASHIER, TABLE_PRODUCT,
            TABLE_PRODUCT_TEMP, TABLE_INVOICE, TABLE_ITEM, TABLE_PRODUCTLOGS, TABLE_CREDIT_CARD,
            TABLE_XREPORT, TABLE_ZREPORT, TABLE_TRANSACTION, TABLE_LOG, TABLE_TOTAL, TABLE_CASH,
            TABLE_CASHTRANS, TABLE_RETRIEVED_JOINTABLE, TABLE_TEMP_INVOICING};

    String[][] columnArray = {TAB_TERMINAL, TAB_ADMIN, TAB_CASHIER, TAB_PRODUCT,
            TAB_PRODUCT_TEMP, TAB_INVOICE, TAB_ITEM, TAB_PRODUCTLOGS, TAB_CREDIT_CARD,
            TAB_XREPORT, TAB_ZREPORT, TAB_TRANSACTION, TAB_LOG, TAB_TOTAL, TAB_CASH,
            TAB_CASHTRANS, TAB_RETRIEVED_JOINTAB, TAB_TEMP_INVOICING};

    int[] functionKeysID = new int[]{R.id.btn_f01, R.id.btn_f02, R.id.btn_f03, R.id.btn_f04,
            R.id.btn_f05, R.id.btn_f06, R.id.btn_f07, R.id.btn_f08, R.id.btn_f09, R.id.btn_f10,
            R.id.btn_f11, R.id.btn_f12};

    String[][] functionKeys = new String[][]{{"LogIn", "Admin", "SignUp", " ", " ", " ", " ", " ", " ", " ", " ", " "},    //0 LogIn            //MainActivity
            {"Register", "Cancel", "", "", "", "", "", "", "", "", "", ""},                                                //1 SignUp           //MainActivity
            {"Quantity", "Refund", "Delete", "", "", "", "", "", "", "Cancel", "Payment", "Shift"},                        //2 Invoice          //Cashier
            {"Complete", "Payment", "Discount", "", "", "", "", "", "", "Cancel", "Invoice", "Shift"},                     //3 Payment          //Cashier
            {"Cash", "Credit", "Debit", "Gift", "Discount", "Other", "", "", "", "", "", "Back"},                          //4 Payment Method   //Cashier_Payment
            {"Normal", "Senior", "Diplomat", "", "", "", "", "", "", "", "", "Back"},                                      //5 Discount         //Cashier_Payment
            {"X-Read", "Z-Read", "Cash", "LogOut", "", "", "", "", "", "", "Invoice", "Payment"},                          //6 Shift            //Cashier
            {"", "", "", "", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "", "", "", "", ""}};
}

//"SELECT c1."+COLUMN_PRODUCT_ID + "," +
//       "c1."+COLUMN_PRODUCT_NAME + "," +
//       "c2."+COLUMN_PRODUCT_QUANTITY_TEMP + "," +
//       "c3."+COLUMN_ITEM_QUANTITY + "," +
//       "c1."+COLUMN_PRODUCT_QUANTITY +
//       " FROM " + TABLE_PRODUCT + " c1 " +
//       " INNER JOIN " + TABLE_PRODUCT_TEMP + " c2 " +
//          " ON " + "c1."+COLUMN_PRODUCT_ID + "= c2."+COLUMN_PRODUCT_ID_TEMP +
//       " INNER JOIN " + TABLE_ITEM c3 +
//          " ON " + "c1."+COLUMN_PRODUCT_ID + "= c3."+COLUMN_ITEM_PRODUCT+";"