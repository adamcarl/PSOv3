package com.example.sydney.psov3;

import android.provider.BaseColumns;

/**
 * Created by sydney on 4/25/2017.
 */

public interface Constants extends BaseColumns {
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

    String TABLE_INVOICE = "tbl_invoice";
    String COLUMN_INVOICE_TRANSACTION_NUMBER = "InTrans";
    String COLUMN_INVOICE_CASHIER_NUMBER = "InCash";
    String COLUMN_INVOICE_DISCOUNT = "InDisc";
    String COLUMN_INVOICE_CUSTOMER = "InCustomer";
    String COLUMN_INVOICE_DATETIME = "InDateTime";
    String COLUMN_INVOICE_XREPORT = "InXreport";
    String COLUMN_INVOICE_ZREPORT = "InZreport";

    String TABLE_ITEM = "tbl_item";
    String COLUMN_ITEM_INVOICE = "ItemIn";
    String COLUMN_ITEM_PRODUCT = "ItemProd";
    String COLUMN_ITEM_QUANTITY = "ItemQuan";
    String COLUMN_ITEM_STATUS = "ItemStatus";

    String TABLE_XREPORT = "tbl_xreport";
    String COLUMN_XREPORT_TRANSACTION_NUMBER = "xreportTransNum";
    String COLUMN_XREPORT_DATETIME = "xreportDateTime";
    String COLUMN_XREPORT_CASHIER = "xreportCashier";

    String TABLE_ZREPORT = "tbl_zreport";
    String COLUMN_ZREPORT_TRANSACTION_NUMBER = "zreportTransNum";
    String COLUMN_ZREPORT_DATETIME = "zreportDateTime";
    String COLUMN_ZREPORT_CASHIER = "zreportCashier";

    String TABLE_TRANSACTION = "tbl_trans";
    String COLUMN_TRANSACTION_TYPE = "TransType";

    String TABLE_LOG = "tbl_log";
    String COLUMN_LOG_STRING= "LogString";

    String TABLE_CANCEL = "tbl_cancel";
    String COLUMN_CANCEL_TRANSACTION_NUMBER = "CancelTransNum";
    String COLUMN_CANCEL_DATETIME = "CancelDateTime";
    String COLUMN_CANCEL_CASHIER = "CancelCashier";
    String COLUMN_CANCEL_XREPORT = "CancelXreport";
    String COLUMN_CANCEL_ZREPORT = "CancelZreport";
}