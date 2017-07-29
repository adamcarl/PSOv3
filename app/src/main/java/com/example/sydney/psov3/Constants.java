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
    String COLUMN_INVOICE_VATTABLE = "InVattable"; //FROM VATTABLE IF CUSTOMER HAS NO DISCOUNT!
    String COLUMN_INVOICE_VATTED = "InVatted"; //TAX OF VATTABLED ITEMS
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
    String COLUMN_TEMP_TOTALPRICE = "temp_totalPrice";

    String TABLE_TOTAL= "tbl_total";
    String COLUMN_TOTAL_GRAND= "total_grand";

    String TABLE_PRODUCT_TEMP = "tbl_product_temp";
    String COLUMN_PRODUCT_ID_TEMP = "ProdId_temp";
    String COLUMN_PRODUCT_NAME_TEMP = "ProdName_temp";
    String COLUMN_PRODUCT_DESCRIPTION_TEMP = "ProdDesc_temp";
    String COLUMN_PRODUCT_PRICE_TEMP = "ProdPrice_temp";
    String COLUMN_PRODUCT_QUANTITY_TEMP = "ProdQuan_temp";
    String COLUMN_PRODUCT_VATABLE_TEMP = "Vatable_temp";
    String COLUMN_PRODUCT_IMEI_TEMP = "ProdIMEI_temp";
}