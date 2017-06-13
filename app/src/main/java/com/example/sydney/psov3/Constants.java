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
    String COLUMN_INVOICE_CASHIER = "InCash";
    String COLUMN_INVOICE_DISCOUNT = "InDisc";
    String COLUMN_INVOICE_CUSTOMER = "InCustomer";
    String COLUMN_INVOICE_DATE = "InDate";
    String COLUMN_INVOICE_TIME = "InTime";

    String TABLE_ITEM = "tbl_item";
    String COLUMN_ITEM_INVOICE = "ItemIn";
    String COLUMN_ITEM_PRODUCT = "ItemProd";
    String COLUMN_ITEM_QUANTITY = "ItemQuan";
    String COLUMN_ITEM_STATUS = "ItemStatus";

    String TABLE_XREPORT = "tbl_xreport";
    String COLUMN_XREPORT_REPORTNUMBER = "xreportNumber";
    String COLUMN_XREPORT_DATE = "xreportDate";
    String COLUMN_XREPORT_TIME = "xreportTime";
    String COLUMN_XREPORT_CASHIER = "xreportCashier";

    String TABLE_TRANSACTION = "tbl_trans";
    String COLUMN_TRANSACTION_TYPE = "TransType";

    String TABLE_LOG = "tbl_log";
    String COLUMN_LOG_STRING= "LogString";
}