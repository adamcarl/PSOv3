package com.example.sydney.psov3;

import android.provider.BaseColumns;

/**
 * Created by Poging Adam on 4/25/2017.
 */

interface Constants extends BaseColumns {
    String TABLE_NAME_ADMIN = "tbl_admin"; //Table name = tbl_admin
    String USERNAME_ADMIN = "Username"; //Field name = Firstname
    String PASSWORD_ADMIN = "Password"; //Field name = Password

    String TABLE_NAME_CASHIER = "tbl_cashier"; //Table name = tbl_cashier
    String NUMBER_CASHIER = "Cashiernum"; //Field name = Cashiernum
    String NAME_CASHIER = "Name"; //Field name = Name
    String POSITION_CASHIER = "Position"; //Field name = Position
    String PASSWORD_CASHIER = "Password"; //Field name = Password

    String TABLE_NAME_PRODUCT = "tbl_product";
    String ID_PRODUCT = "ProdId";
    String NAME_PRODUCT = "ProdName";
    String DESC_PRODUCT = "ProdDesc";
    String PRICE_PRODUCT = "ProdPrice";
    String QUAN_PRODUCT = "ProdQuan";
    String VATABLE = "Vatable";

    String TABLE_NAME_INVOICE = "tbl_invoice";
    String CASHIER_INVOICE = "InCash";
    String CUSTOMER_DISCOUNT_INVOICE = "InDisc";
    String CUSTOMER_CASH_INVOICE = "InCustomer";
    String DATE_INVOICE = "InDate";
    String TIME_INVOICE = "InTime";

    String TABLE_NAME_ITEM = "tbl_item";
    String INVOICE_NUM_ITEM = "ItemIn";
    String PRODUCT_ID_ITEM = "ItemProd";
    String PRODUCT_QUANTITY_ITEM = "ItemQuan";
    String STATUS_ITEM = "ItemStatus";

    String TABLE_NAME_XREPORT = "tbl_xreport";
    String XREPORT_TRANSACTION_NUMBER = "xreportNumber";
    String XREPORT_DATE= "xreportDate";
    String XREPORT_TIME= "xreportTime";
    String XREPORT_CASHIER = "xreportCashier";

    String TABLE_NAME_TRANSACTION = "tbl_trans";
    String TRANSACTION_TYPE = "TransType";

    String LOG_TABLE_NAME = "tbl_log";
    String LOG_FIELD = "logField";
}