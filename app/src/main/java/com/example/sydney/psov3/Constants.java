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

    String TABLE_INVOICE = "tbl_invoice";
    String COLUMN_INVOICE_TRANSACTION_NUMBER = "InTrans";
    String COLUMN_INVOICE_DISCOUNT = "InDisc";
    String COLUMN_INVOICE_CUSTOMER = "InCustomer"; //Value from cash of customer - PHYSICAL SASHING
    String COLUMN_INVOICE_PRINT = "InPrint";
    String COLUMN_INVOICE_CASHIER_NUMBER = "InCashierNum";
    String COLUMN_INVOICE_ZREPORT_STATUS = "InZreport";
    String COLUMN_INVOICE_XREPORT_STATUS = "InXreport";
    String COLUMN_INVOICE_VATTABLE = "InVattable"; //TO BE SUMMED!
    String COLUMN_INVOICE_VATTED = "InVatted"; //TAX OF VATTABLED ITEMS
    String COLUMN_INVOICE_VAT_STATUS = "InVatStatus"; //ON/OFF
    String COLUMN_INVOICE_SENIOR_DISCOUNT = "InSeniorDiscount";
    String COLUMN_INVOICE_VAT_EXEMPT = "InExemptDiscount";
    String COLUMN_INVOICE_ZERORATED = "InExemptDiscount";
    String COLUMN_INVOICE_CC_BDO = "InCCBDO";
    String COLUMN_INVOICE_CC_BPI = "InCCBPI";
    String COLUMN_INVOICE_TOTAL_DISCOUNT = "InAllDiscount";

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

    String TABLE_ZREPORT = "tbl_zreport";
    String COLUMN_ZREPORT_TRANSACTION_NUMBER = "zreportTransNum";

    String TABLE_TRANSACTION = "tbl_trans";
    String COLUMN_TRANSACTION_TYPE = "TransType";
    String COLUMN_TRANSACTION_DATETIME = "TransDateTime";
    String COLUMN_TRANSACTION_CASHIER = "TransCashier";
    String COLUMN_TRANSACTION_ZREPORT = "TransZreport";
    String COLUMN_TRANSACTION_XREPORT = "TransXreport";

    String TABLE_LOG = "tbl_log";
    String COLUMN_LOG_STRING= "LogString";

    String TABLE_TEMP_INVOICING = "tbl_temp";
    String COLUMN_TEMP_DESCRIPTION = "temp_description";
    String COLUMN_TEMP_PRICE = "temp_price";
    String COLUMN_TEMP_QUANTITY = "temp_quantity";
    String COLUMN_TEMP_ID = "temp_id";


}