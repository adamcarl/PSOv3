package com.example.sydney.psov3;

import android.provider.BaseColumns;

/**
 * Created by sydney on 4/25/2017.
 */

public interface Constants extends BaseColumns {
    String TABLE_NAME_ADMIN = "tbl_admin"; //Table name = tbl_admin
    String FIRSTNAME_ADMIN = "Firstname"; //Field name = Firstname
    String LASTNAME_ADMIN = "Lastname"; //Field  name = Lastname
    String PASSWORD_ADMIN = "Password"; //Field name = Password

    String TABLE_NAME_CASHIER = "tbl_cashier"; //Table name = tbl_cashier
    String NUMBER_CASHIER = "Cashiernum"; //Field name = Cashiernum
    String FIRSTNAME_CASHIER = "Firstname"; //Field name = Firstname
    String LASTNAME_CASHIER = "Lastname"; //Field name = Lastname
    String POSITION_CASHIER = "Position"; //Field name = Password
    String PASSWORD_CASHIER = "Password"; //Field name = Password

    String TABLE_NAME_PRODUCT = "tbl_product";
    String ID_PRODUCT = "ProdId";
    String NAME_PRODUCT = "ProdName";
    String DESC_PRODUCT = "ProdDesc";
    String PRICE_PRODUCT = "ProdPrice";
    String QUAN_PRODUCT = "ProdQuan";
    String VATABLE = "Vatable";

    String TABLE_NAME_ORDER = "tbl_order";
    String NUMBER_ORDER = "";
    String NAME_ORDER = "";
    String ITEM_ORDER = "";
    String TRANSACTION_ORDER = "";
    String QUAN_ORDER = "";
    String DATE_ORDER = "";

    String TABLE_NAME_RECEIPT = "";
    String NUMBER_RECEIPT = "";
    String DISCOUNT_RECEIPT = "";
    String TERMINAL_RECEIPT = "";
    String CASH_RECEIPT = "";
    String TRANSACTION_NUMBER_RECEIPT = "";
    String DATE_RECEIPT = "";
}