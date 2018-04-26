package com.example.sydney.psov3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.sydney.psov3.POJO.FunctionCall;
import com.example.sydney.psov3.POJO.Invoice;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.provider.BaseColumns._ID;
import static com.example.sydney.psov3.Constants.*;

class DB_Data extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pos_db.db";
    private static final int DATABASE_VERSION = 1;
    private static String[] FROM_ADMIN = {COLUMN_ADMIN_USERNAME, COLUMN_ADMIN_PASSWORD};
    private static String[] FROM_CASH = {COLUMN_CASHIER_PASSWORD};
    private static String[] ALL = {COLUMN_CASHIER_NUMBER, COLUMN_CASHIER_NAME, COLUMN_CASHIER_POSITION};
    private SQLiteDatabase dbr = this.getReadableDatabase();
    private SQLiteDatabase dbw = this.getWritableDatabase();
    private ContentValues cv = new ContentValues();
    private FunctionCall fuc = new FunctionCall();

    DB_Data(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase arg0) {

        arg0.execSQL("CREATE TABLE " + TABLE_TERMINAL + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TERMINAL_NAME + " TEXT NOT NULL UNIQUE, "
                + COLUMN_TERMINAL_SERIAL + " TEXT NOT NULL UNIQUE );");

        arg0.execSQL("CREATE TABLE "+TABLE_ADMIN+" ("
                +_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +COLUMN_ADMIN_USERNAME+" TEXT NOT NULL UNIQUE, "
                +COLUMN_ADMIN_PASSWORD+" TEXT NOT NULL );");

        arg0.execSQL("CREATE TABLE "+TABLE_CASHIER+" ("
                +_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +COLUMN_CASHIER_NUMBER+" INTEGER NOT NULL UNIQUE, "
                +COLUMN_CASHIER_NAME+" TEXT NOT NULL, "
                +COLUMN_CASHIER_POSITION+" TEXT NOT NULL,"
                +COLUMN_CASHIER_PASSWORD+" TEXT NOT NULL );");

        arg0.execSQL("CREATE TABLE "+TABLE_PRODUCT+" ("
                +_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +COLUMN_PRODUCT_ID+" TEXT NOT NULL UNIQUE, "
                +COLUMN_PRODUCT_NAME+" TEXT NOT NULL, "
                +COLUMN_PRODUCT_DESCRIPTION+" TEXT NOT NULL, "
                +COLUMN_PRODUCT_PRICE+" REAL NOT NULL,"
                +COLUMN_PRODUCT_QUANTITY+" INTEGER NOT NULL, "
                +COLUMN_PRODUCT_VATABLE+" INTEGER, "
                +COLUMN_PRODUCT_IMEI+" INTEGER );");

        arg0.execSQL("CREATE TABLE "+TABLE_PRODUCT_TEMP+" ("
                +_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +COLUMN_PRODUCT_ID_TEMP+" TEXT NOT NULL UNIQUE, "
                +COLUMN_PRODUCT_NAME_TEMP+" TEXT NOT NULL, "
                +COLUMN_PRODUCT_DESCRIPTION_TEMP+" TEXT NOT NULL, "
                +COLUMN_PRODUCT_PRICE_TEMP+" REAL NOT NULL,"
                +COLUMN_PRODUCT_QUANTITY_TEMP+" INTEGER NOT NULL, "
                +COLUMN_PRODUCT_VATABLE_TEMP+" REAL, "
                + COLUMN_PRODUCT_IMEI_TEMP + " STRING );");

        arg0.execSQL("CREATE TABLE "+TABLE_INVOICE+" ("
                +_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +COLUMN_INVOICE_TRANSACTION_NUMBER +" INTEGER NOT NULL ,"
                +COLUMN_INVOICE_DISCOUNT+" REAL,"
                +COLUMN_INVOICE_NORMALSALE +" REAL NOT NULL,"
                +COLUMN_INVOICE_PRINT+" TEXT,"
                +COLUMN_INVOICE_CASHIER_NUMBER+" TEXT,"
                +COLUMN_INVOICE_ZREPORT_STATUS +" TEXT,"
                +COLUMN_INVOICE_XREPORT_STATUS +" TEXT,"
                +COLUMN_INVOICE_VATTABLE+ " REAL,"
                +COLUMN_INVOICE_VATTED+ " REAL,"
                +COLUMN_INVOICE_VAT_STATUS+ " TEXT,"
                +COLUMN_INVOICE_CREDITSALE+ " REAL,"
                +COLUMN_INVOICE_DATEANDTIME+ " TEXT,"
                +COLUMN_INVOICE_CREDITCARDNUMBER+ " TEXT,"
                +COLUMN_INVOICE_CREDITDATEOFEXPIRATION+ " TEXT);");

        arg0.execSQL("CREATE TABLE "+TABLE_ITEM+" ("
                +_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +COLUMN_ITEM_INVOICE+" INTEGER NOT NULL, "
                +COLUMN_ITEM_NAME+" TEXT NOT NULL, "
                +COLUMN_ITEM_DESC+" TEXT NOT NULL, "
                +COLUMN_ITEM_PRICE+" REAL NOT NULL, "
                +COLUMN_ITEM_PRODUCT+" TEXT NOT NULL, "
                +COLUMN_ITEM_QUANTITY+" INTEGER NOT NULL, "
                +COLUMN_ITEM_DISCOUNT+" REAL NOT NULL, "
                +COLUMN_ITEM_STATUS+" INTEGER NOT NULL,"
                +COLUMN_ITEM_XREPORT+" TEXT NOT NULL,"
                +COLUMN_ITEM_ZREPORT+" TEXT NOT NULL,"
                +COLUMN_ITEM_CASHIER+" TEXT NOT NULL );");

        arg0.execSQL("CREATE TABLE "+TABLE_PRODUCTLOGS+" ("
                +_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +COLUMN_PRODUCTLOGS_BARCODE+" TEXT NOT NULL, "
                +COLUMN_PRODUCTLOGS_TYPE+" TEXT NOT NULL, " //transfer out/in, return to manufacturer
                +COLUMN_PRODUCTLOGS_VALUEADDED+" INTEGER, "
                +COLUMN_PRODUCTLOGS_VALUEMINUS+" INTEGER, "
                +COLUMN_PRODUCTLOGS_REMARKS+" TEXT NOT NULL,"
                +COLUMN_PRODUCTLOGS_DATE+" TEXT NOT NULL);");

        arg0.execSQL("CREATE TABLE " + TABLE_CREDIT_CARD + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_CREDIT_TRANS + " INTEGER NOT NULL, "
                + COLUMN_CREDIT_CASHIER + " TEXT NOT NULL, "
                + COLUMN_CREDIT_DATE + " TEXT NOT NULL, "
                + COLUMN_CREDIT_PAYMENT + " REAL NOT NULL,"
                + COLUMN_CREDIT_BANK + " TEXT NOT NULL, "
                + COLUMN_CREDITT_NUMBER + " TEXT NOT NULL, "
                + COLUMN_CREDIT_EXPIRY + " TEXT NOT NULL);");

        arg0.execSQL("CREATE TABLE IF NOT EXISTS cashierlog(date TEXT, time TEXT,userNum TEXT,lastname TEXT,username TEXT,transactionnumber INTEGER PRIMARY KEY AUTOINCREMENT);");
        arg0.execSQL("CREATE TABLE IF NOT EXISTS sessions(time TEXT,date TEXT, username TEXT ); ");
        arg0.execSQL("CREATE TABLE IF NOT EXISTS receipts(invoicenumber INTEGER,transactionnumber INTEGER);");
        arg0.execSQL("CREATE TABLE IF NOT EXISTS departments(department TEXT,category TEXT,subcategory TEXT);");

        arg0.execSQL("CREATE TABLE "+TABLE_XREPORT+ " ("
                +_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +COLUMN_XREPORT_TRANSACTION_NUMBER+" INTEGER NOT NULL, "
                +COLUMN_XREPORT_CASHSALES + " REAL,"
                +COLUMN_XREPORT_CASHCOUNT + " REAL,"
                +COLUMN_XREPORT_CASHSHORTOVER + " REAL);");

        arg0.execSQL("CREATE TABLE "+TABLE_ZREPORT+ " ("
                +_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +COLUMN_ZREPORT_TRANSACTION_NUMBER+" INTEGER NOT NULL, "
                +COLUMN_ZREPORT_CASHSALES+" REAL, "
                + COLUMN_ZREPORT_CASHCOUNT + " REAL, "
                + COLUMN_ZREPORT_PRINT + " STRING);");

        arg0.execSQL("CREATE TABLE "+ TABLE_TRANSACTION+ "("
                +_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +COLUMN_TRANSACTION_TYPE+" TEXT NOT NULL, "
                +COLUMN_TRANSACTION_DATETIME+" TEXT NOT NULL, "
                +COLUMN_TRANSACTION_CASHIER+" TEXT NOT NULL, "
                +COLUMN_TRANSACTION_XREPORT+" INTEGER NOT NULL, "
                + COLUMN_TRANSACTION_ZREPORT + " INTEGER NOT NULL, "
                + COLUMN_TRANSACTION_PRINT + " STRING);");

        arg0.execSQL("CREATE TABLE " + TABLE_LOG + "("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_LOG_TYPE + " TEXT NOT NULL, "
                + COLUMN_LOG_DATETIME + " TEXT NOT NULL, "
                + COLUMN_LOG_CASHIER + " TEXT NOT NULL, "
                + COLUMN_LOG_PRINT + " STRING);");

        arg0.execSQL("CREATE TABLE "+ TABLE_TEMP_INVOICING + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TEMP_NAME + " TEXT NOT NULL,"
                + COLUMN_TEMP_DESCRIPTION + " TEXT NOT NULL,"
                + COLUMN_TEMP_PRICE + " TEXT NOT NULL,"
                + COLUMN_TEMP_QUANTITY + " TEXT NOT NULL,"
                + COLUMN_TEMP_ID + " TEXT NOT NULL,"
                + COLUMN_TEMP_TOTALPRICE + " INTEGER);");

        arg0.execSQL("CREATE TABLE "+ TABLE_TOTAL + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TOTAL_GRAND + " REAL);");

        arg0.execSQL("CREATE TABLE "+ TABLE_RETRIEVED_JOINTABLE + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_RETRIEVED_BARCODE + " TEXT,"
                + COLUMN_RETRIEVED_NAME + " TEXT,"
                + COLUMN_RETRIEVED_QUANTITY + " INTEGER,"
                + COLUMN_RETRIEVED_SOLDITEM + " INTEGER,"
                + COLUMN_RETRIEVED_NEWQUANTITY + " INTEGER);");

        arg0.execSQL("CREATE TABLE "+ TABLE_CASH + " ("
                +_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CASH_TRANSNUM + " TEXT NOT NULL,"
                + COLUMN_CASH_CASHIERNUM + " TEXT NOT NULL,"
                + COLUMN_CASH_DATEANDTIME + " TEXT,"
                + COLUMN_CASH_ADDCASH + " REAL,"
                + COLUMN_CASH_MINNUSCASH + " REAL,"
                + COLUMN_CASH_CURRENTCASH + " REAL,"
                + COLUMN_CASH_ZREPORT + " TEXT,"
                + COLUMN_CASH_XREPORT + " TEXT);");

        arg0.execSQL("CREATE TABLE "+ TABLE_CASHTRANS + " ("
                +_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CASHTRANS_TRANSNUM + " TEXT NOT NULL,"
                + COLUMN_CASHTRANS_CASHIERNUM + " TEXT NOT NULL,"
                + COLUMN_CASHTRANS_DATETIME + " TEXT,"
                + COLUMN_CASHTRANS_ADDCASH + " REAL,"
                + COLUMN_CASHTRANS_MINNUSCASH + " REAL,"
                + COLUMN_CASHTRANS_REASON + " TEXT NOT NULL,"
                + COLUMN_CASHTRANS_REMARKS1 + " TEXT NOT NULL,"
                + COLUMN_CASHTRANS_REMARKS2 + " TEXT,"
                + COLUMN_CASHTRANS_REMARKS3 + " TEXT,"
                + COLUMN_CASHTRANS_REMARKS4 + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        arg0.execSQL("DROP TABLE IF EXISTS "+TABLE_ADMIN);
        onCreate(arg0);
        arg0.execSQL("DROP TABLE IF EXISTS "+TABLE_CASHIER);
        onCreate(arg0);
        arg0.execSQL("DROP TABLE IF EXISTS "+TABLE_PRODUCT);
        onCreate(arg0);
        arg0.execSQL("DROP TABLE IF EXISTS "+TABLE_PRODUCT_TEMP);
        onCreate(arg0);
        arg0.execSQL("DROP TABLE IF EXISTS "+TABLE_INVOICE);
        onCreate(arg0);
        arg0.execSQL("DROP TABLE IF EXISTS "+TABLE_ITEM);
        onCreate(arg0);
        arg0.execSQL("DROP TABLE IF EXISTS "+TABLE_XREPORT);
        onCreate(arg0);
        arg0.execSQL("DROP TABLE IF EXISTS "+TABLE_TRANSACTION);
        onCreate(arg0);
        arg0.execSQL("DROP TABLE IF EXISTS "+ TABLE_LOG);
        onCreate(arg0);
        arg0.execSQL("DROP TABLE IF EXISTS "+ TABLE_TEMP_INVOICING);
        onCreate(arg0);
        arg0.execSQL("DROP TABLE IF EXISTS "+ TABLE_TOTAL);
        onCreate(arg0);
        arg0.execSQL("DROP TABLE IF EXISTS "+ TABLE_RETRIEVED_JOINTABLE);
        onCreate(arg0);
        arg0.execSQL("DROP TABLE IF EXISTS "+ TABLE_PRODUCTLOGS);
        onCreate(arg0);
        arg0.execSQL("DROP TABLE IF EXISTS "+ TABLE_CASH);
        onCreate(arg0);
        arg0.execSQL("DROP TABLE IF EXISTS "+ TABLE_CASHTRANS);
        onCreate(arg0);
    }

    int adminLogin(String usernum, String Pass) {
        String WHERE_ADMIN = "Username = ? and Password = ?";
        String[] WHERE_ARGS_ADMIN = new String[]{usernum,Pass};
        try {
            int i;
            Cursor curse = dbr.query(TABLE_ADMIN,FROM_ADMIN,WHERE_ADMIN,WHERE_ARGS_ADMIN,null,null,null);
            curse.moveToFirst();
            i = curse.getCount();
            curse.close();
            return i;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    int cashierLogin(String cname, String cpass) {
        String WHERE_CASH = "Cashiernum LIKE ? and Password = ?";
        String[] WHERE_ARGS_CASH = new String[]{cname.toLowerCase().trim(),cpass};
        try {
            Cursor curse = dbr.query(TABLE_CASHIER,FROM_CASH,WHERE_CASH,WHERE_ARGS_CASH,null,null,null);
            curse.moveToFirst();
            int i = curse.getCount();
            curse.close();
            return i;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    void addCashInOut(String cashTransNum, String cashCashierNum, String cashDateTime,
                      double cashAdd, double cashMinus, double cashCurrent, String cashX,
                      String cashZ) {
        cv.clear();
        cv.put(COLUMN_CASH_TRANSNUM, cashTransNum);
        cv.put(COLUMN_CASH_CASHIERNUM, cashCashierNum);
        cv.put(COLUMN_CASH_DATEANDTIME, cashDateTime);
        cv.put(COLUMN_CASH_ADDCASH, cashAdd);
        cv.put(COLUMN_CASH_MINNUSCASH, cashMinus);
        cv.put(COLUMN_CASH_CURRENTCASH, cashCurrent);
        cv.put(COLUMN_CASH_XREPORT, cashX);
        cv.put(COLUMN_CASH_ZREPORT, cashZ);
        dbw.insertOrThrow(TABLE_CASH, null, cv);
    }

    void addCashTransaction(CashTransaction ct) {
        cv.clear();
        cv.put(COLUMN_CASHTRANS_TRANSNUM, ct.getCtTransnum());
        cv.put(COLUMN_CASHTRANS_CASHIERNUM, ct.getCtCashNum());
        cv.put(COLUMN_CASHTRANS_DATETIME, ct.getCtDateTime());
        cv.put(COLUMN_CASHTRANS_ADDCASH, ct.getCtCashAdd());
        cv.put(COLUMN_CASHTRANS_MINNUSCASH, ct.getCtCashMinus());
        cv.put(COLUMN_CASHTRANS_REASON, ct.getCtReason());
        cv.put(COLUMN_CASHTRANS_REMARKS1, ct.getCtRemarks1());
        cv.put(COLUMN_CASHTRANS_REMARKS2, ct.getCtRemarks2());
        cv.put(COLUMN_CASHTRANS_REMARKS3, ct.getCtRemarks3());
        cv.put(COLUMN_CASHTRANS_REMARKS4, ct.getCtRemarks4());

        dbw.insertOrThrow(TABLE_CASHTRANS, null, cv);
    }

    void addCashier(String Name,String UserNum, String Pass, String Pos){
        cv.clear();
        cv.put(COLUMN_CASHIER_NAME, Name);
        cv.put(COLUMN_CASHIER_NUMBER, UserNum);
        cv.put(COLUMN_CASHIER_POSITION, Pos);
        cv.put(COLUMN_CASHIER_PASSWORD, Pass);
        dbw.insertOrThrow(TABLE_CASHIER, null, cv);
    }

    void addProduct(String ProdId, String ProdName, String ProdDesc, String ProdPrice,
                    String ProdQuan) {
        cv.clear();
        cv.put(COLUMN_PRODUCT_ID, ProdId);
        cv.put(COLUMN_PRODUCT_NAME, ProdName);
        cv.put(COLUMN_PRODUCT_DESCRIPTION, ProdDesc);
        cv.put(COLUMN_PRODUCT_PRICE, ProdPrice);
        cv.put(COLUMN_PRODUCT_QUANTITY, ProdQuan);
        dbw.insertOrThrow(TABLE_PRODUCT, null, cv);
    }

    void addAdmin(String mName,String mPass){
        cv.clear();
        cv.put(COLUMN_ADMIN_USERNAME, mName);
        cv.put(COLUMN_ADMIN_PASSWORD, mPass);
        dbw.insertOrThrow(TABLE_ADMIN, null, cv);
    }

    void addTerminal(String mName, String mSerial) {
        cv.clear();
        cv.put(COLUMN_TERMINAL_NAME, mName);
        cv.put(COLUMN_TERMINAL_SERIAL, mSerial);
        dbw.insertOrThrow(TABLE_TERMINAL, null, cv);
    }

    void addGrandTotal(double gTotal) {
        cv.clear();
        cv.put(COLUMN_TOTAL_GRAND, gTotal);
        dbw.insertOrThrow(TABLE_TOTAL, null, cv);
    }

    void addInvoice(Invoice in) {
        cv.clear();
        cv.put(COLUMN_INVOICE_TRANSACTION_NUMBER, in.getInTrans());
        cv.put(COLUMN_INVOICE_DISCOUNT, in.getInDisc());
        cv.put(COLUMN_INVOICE_NORMALSALE, in.getInCustomer());
        cv.put(COLUMN_INVOICE_PRINT, in.getInPrint());
        cv.put(COLUMN_INVOICE_CASHIER_NUMBER, in.getInCashierNum());
        cv.put(COLUMN_INVOICE_ZREPORT_STATUS, in.getInZreport());
        cv.put(COLUMN_INVOICE_XREPORT_STATUS, in.getInXreport());
        cv.put(COLUMN_INVOICE_VATTABLE, in.getInVattable());
        cv.put(COLUMN_INVOICE_VATTED, in.getInVatted());
        cv.put(COLUMN_INVOICE_VAT_STATUS, in.getInVatStatus());
        cv.put(COLUMN_INVOICE_CREDITSALE, in.getInCreditSale());
        cv.put(COLUMN_INVOICE_DATEANDTIME, in.getInDateAndTime());
        cv.put(COLUMN_INVOICE_CREDITCARDNUMBER, in.getInCreditCardNum());
        cv.put(COLUMN_INVOICE_CREDITDATEOFEXPIRATION, in.getInCreditExp());
        dbw.insertOrThrow(TABLE_INVOICE, null, cv);
    }

    void addItem(String itemIn, String itemProd, String itemQuan, int itemStatus, String itemName,
                 String itemDesc, double itemPrice, String itemCashier) {
        cv.clear();
        cv.put(COLUMN_ITEM_INVOICE,itemIn);
        cv.put(COLUMN_ITEM_PRODUCT,itemProd);
        cv.put(COLUMN_ITEM_QUANTITY,itemQuan);
        cv.put(COLUMN_ITEM_DISCOUNT,0);
        cv.put(COLUMN_ITEM_STATUS,itemStatus);
        cv.put(COLUMN_ITEM_NAME,itemName);
        cv.put(COLUMN_ITEM_DESC,itemDesc);
        cv.put(COLUMN_ITEM_PRICE,itemPrice);
        cv.put(COLUMN_ITEM_CASHIER,itemCashier);
        cv.put(COLUMN_ITEM_XREPORT,"0");
        cv.put(COLUMN_ITEM_ZREPORT,"0");
        dbw.insertOrThrow(TABLE_ITEM, null, cv);
    }

    void addTransaction(String transactionType, String transactionDatetime,
                        String transactionCashier, int transactionXreport, int transactionZreport,
                        String transactionPrint) {
        cv.clear();
        cv.put(COLUMN_TRANSACTION_TYPE,transactionType);
        cv.put(COLUMN_TRANSACTION_DATETIME,transactionDatetime);
        cv.put(COLUMN_TRANSACTION_CASHIER,transactionCashier);
        cv.put(COLUMN_TRANSACTION_XREPORT,transactionXreport);
        cv.put(COLUMN_TRANSACTION_ZREPORT,transactionZreport);
        cv.put(COLUMN_TRANSACTION_PRINT, transactionPrint);
        dbw.insertOrThrow(TABLE_TRANSACTION, null, cv);
    }

    void addLog(String logType, String logDatetime, String logCashier, String logPrint) {
        cv.clear();
        cv.put(COLUMN_LOG_TYPE, logType);
        cv.put(COLUMN_LOG_DATETIME, logDatetime);
        cv.put(COLUMN_LOG_CASHIER, logCashier);
        cv.put(COLUMN_LOG_PRINT, logPrint);
        dbw.insertOrThrow(TABLE_LOG, null, cv);
    }

    List<String> getAllLabels(){
        String[] all = {COLUMN_CASHIER_NUMBER};
        Cursor cursor = dbr.query(TABLE_CASHIER,all,null,null,null,null,null);
        ArrayList<String> cashnum = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                cashnum.add(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return cashnum;
    }

    String[] searchStaff(String cnum) {
        String WHERE_CASH = COLUMN_CASHIER_NUMBER+" = ?";
        String[] WHERE_ARGS_CASH = new String[]{cnum};
        String[] res_staff= new String[4];
        Cursor cursor = dbr.query(TABLE_CASHIER,ALL,WHERE_CASH,WHERE_ARGS_CASH,null,null,null);
        cursor.moveToFirst();
        res_staff[0]=cursor.getString(1);
        res_staff[1]=cursor.getString(2);
        res_staff[2]=cursor.getString(3);
        cursor.close();
        return res_staff;
    }

    int searchStaffNumber(String staffNum) {
        SQLiteDatabase database = this.getReadableDatabase();
        String[] selectionArgs = new String[]{staffNum};
        try {
            int i;
            Cursor cursor;
            cursor = database.rawQuery("SELECT * FROM " + TABLE_CASHIER + " where " +
                    COLUMN_CASHIER_NUMBER + "=?", selectionArgs);
            cursor.moveToFirst();
            i = cursor.getCount();
            cursor.close();
            return i;
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    String[] searchProduct(String id){
        String[] mALL = {COLUMN_PRODUCT_ID, COLUMN_PRODUCT_NAME, COLUMN_PRODUCT_DESCRIPTION,
                COLUMN_PRODUCT_PRICE, COLUMN_PRODUCT_QUANTITY, COLUMN_PRODUCT_VATABLE};
        String mWHERE = COLUMN_PRODUCT_ID+" = ?";
        String[] mWHERE_ARGS = new String[]{id};
        String[] itemResult = new String[5];
        Cursor cursor = dbr.query(TABLE_PRODUCT, mALL, mWHERE, mWHERE_ARGS, null, null, null);
        cursor.moveToFirst();
        itemResult[0]=cursor.getString(1);
        itemResult[1]=cursor.getString(2);
        itemResult[2]=cursor.getString(3);
        itemResult[3]=cursor.getString(4);
        itemResult[4]=cursor.getString(5);
        itemResult[5]=cursor.getString(6);
        cursor.close();
        return itemResult;
    }

    Cursor queryDataRead(String query) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery(query,null);
    }

    void updateAdmin(String A_pass){
        cv.clear();
        String WHERE = _ID+" = ?";
        String[] WHERE_ARGS = new String[]{"1"};
        cv.put(COLUMN_ADMIN_PASSWORD,A_pass);
        dbw.update(TABLE_ADMIN, cv, WHERE,WHERE_ARGS);
    }

    void updateStaff(String Name,String UserNum, String Pass, String Pos){
        cv.clear();
        String WHERE_CASH = "Cashiernum = ?";
        String[] WHERE_ARGS_CASH = new String[]{UserNum};
        cv.put(COLUMN_CASHIER_NAME, Name);
        cv.put(COLUMN_CASHIER_POSITION, Pos);
        cv.put(COLUMN_CASHIER_PASSWORD, Pass);
        dbw.update(TABLE_CASHIER, cv, WHERE_CASH,WHERE_ARGS_CASH);
    }

    void updateProd(String P_id,String P_name,String P_desc, String P_price, String P_quan){
        cv.clear();
        String WHERE_CASH = COLUMN_PRODUCT_ID+" = ?";
        String[] WHERE_ARGS_CASH = new String[]{P_id};
        cv.put(COLUMN_PRODUCT_NAME, P_name);
        cv.put(COLUMN_PRODUCT_DESCRIPTION, P_desc);
        cv.put(COLUMN_PRODUCT_PRICE, P_price);
        cv.put(COLUMN_PRODUCT_QUANTITY, P_quan);
        dbw.update(TABLE_PRODUCT, cv, WHERE_CASH,WHERE_ARGS_CASH);
    }

    void updateInvoice(String id, String print){
        cv.clear();
        String WHERE_CASH = _ID + " = ?";
        String[] WHERE_ARGS_CASH = new String[]{id};
        cv.put(COLUMN_INVOICE_PRINT, print);
        dbw.update(TABLE_INVOICE, cv, WHERE_CASH,WHERE_ARGS_CASH);
    }

    void givePrintTransaction(String id, String print) {
        cv.clear();
        String WHERE_CASH = _ID + " = ?";
        String[] WHERE_ARGS_CASH = new String[]{id};
        cv.put(COLUMN_TRANSACTION_PRINT, print);
        dbw.update(TABLE_TRANSACTION, cv, WHERE_CASH, WHERE_ARGS_CASH);
    }

    void updateTransactions(String name){
        cv.clear();
        String WHERE_CASH;
        String[] mWHERE_ARGS;
        if(name.equals("no")){
            WHERE_CASH = COLUMN_TRANSACTION_ZREPORT + " = ?";
            mWHERE_ARGS = new String[]{"0"};

            cv.put(COLUMN_TRANSACTION_ZREPORT,"1");
            dbw.update(TABLE_TRANSACTION,cv,WHERE_CASH,mWHERE_ARGS);

            cv.clear();
            WHERE_CASH = COLUMN_INVOICE_ZREPORT_STATUS + " = ?";
            cv.put(COLUMN_INVOICE_ZREPORT_STATUS,"1");
            dbw.update(TABLE_INVOICE,cv,WHERE_CASH,mWHERE_ARGS);

            cv.clear();
            WHERE_CASH = COLUMN_ITEM_ZREPORT + " = ?";
            cv.put(COLUMN_ITEM_ZREPORT,"1");
            dbw.update(TABLE_ITEM,cv,WHERE_CASH,mWHERE_ARGS);
        }
        else {
            WHERE_CASH = COLUMN_TRANSACTION_XREPORT+" = ? AND "+COLUMN_TRANSACTION_CASHIER+" = ?";
            mWHERE_ARGS = new String[]{"0",name};
            cv.put(COLUMN_TRANSACTION_XREPORT,"1");
            dbw.update(TABLE_TRANSACTION,cv,WHERE_CASH,mWHERE_ARGS);

            WHERE_CASH = COLUMN_INVOICE_XREPORT_STATUS +" = ? AND "+COLUMN_INVOICE_CASHIER_NUMBER+" = ?";
            cv.clear();
            cv.put(COLUMN_INVOICE_XREPORT_STATUS,"1");
            dbw.update(TABLE_INVOICE,cv,WHERE_CASH,mWHERE_ARGS);

            WHERE_CASH = COLUMN_ITEM_XREPORT +" = ? AND "+COLUMN_ITEM_CASHIER+" = ?";
            cv.clear();
            cv.put(COLUMN_ITEM_XREPORT,"1");
            dbw.update(TABLE_ITEM,cv,WHERE_CASH,mWHERE_ARGS);
        }
    }
//OLD SEARCH FOR PRODUCTS!!!!
//    public Cursor searchViewProduct(String searchItem) {
//        String[] columns = {_ID,COLUMN_PRODUCT_ID,COLUMN_PRODUCT_NAME, COLUMN_PRODUCT_DESCRIPTION, COLUMN_PRODUCT_PRICE, COLUMN_PRODUCT_QUANTITY};
//        Cursor cursor = null;
//
//        if(searchItem != null && searchItem.length() > 0 ){
//            String sql = "SELECT * FROM "+ TABLE_PRODUCT + " WHERE "
//                    + COLUMN_PRODUCT_ID + " LIKE '%" + searchItem + "%' OR "
//                    + COLUMN_PRODUCT_NAME + " LIKE '%" + searchItem + "%' OR "
//                    + COLUMN_PRODUCT_DESCRIPTION + " LIKE '%" + searchItem + "%' OR "
//                    + COLUMN_PRODUCT_PRICE + " LIKE '%" + searchItem + "%' OR "
//                    + COLUMN_PRODUCT_QUANTITY + " LIKE '%" + searchItem +"%'";
//            cursor = dbr.rawQuery(sql,null);
//            return cursor;
//        }
//
//        cursor = dbr.query(TABLE_PRODUCT,columns,null,null,null,null,null,null);
//        return cursor;
//    }

    Cursor searchProductBaKamo(String searchItem, String WHERE) {
        String[] columns = {_ID,COLUMN_PRODUCT_ID,COLUMN_PRODUCT_NAME, COLUMN_PRODUCT_DESCRIPTION, COLUMN_PRODUCT_PRICE, COLUMN_PRODUCT_QUANTITY};
        if(searchItem != null && searchItem.length() > 0 ){
            String sql = "SELECT * FROM "+ TABLE_PRODUCT+" WHERE "+WHERE+" LIKE '%"+searchItem+"%'";
            return dbr.rawQuery(sql,null);
        }
        else {
            return dbr.query(TABLE_PRODUCT, columns, null, null, null, null, null, null);
        }
    }

    Cursor searchTransactions(String searchItem, String WHERE) {
        String[] columns = {_ID,COLUMN_TRANSACTION_TYPE,COLUMN_TRANSACTION_DATETIME};
        if(searchItem != null && searchItem.length() > 0 ){
            String sql = "SELECT * FROM "+ TABLE_TRANSACTION+" WHERE "+WHERE+" LIKE '%"+searchItem+"%'";
            return dbr.rawQuery(sql,null);
        }
        else {
            return dbr.query(TABLE_TRANSACTION, columns, null, null, null, null, null, null);
        }
    }

    Cursor searchInvoiceTransactions(String name){
        String[] columns = {_ID,COLUMN_TRANSACTION_TYPE};
        String mWHERE;
        String[] mWHERE_ARGS;
        if(name.equals("no")){
            mWHERE = COLUMN_TRANSACTION_TYPE+" = ? AND "+COLUMN_TRANSACTION_ZREPORT+" = ?";
            mWHERE_ARGS = new String[]{"invoice","0"};
        }else{
            mWHERE = COLUMN_TRANSACTION_TYPE+" = ? AND "+COLUMN_TRANSACTION_XREPORT+" = ? AND "+COLUMN_TRANSACTION_CASHIER+" = ?";
            mWHERE_ARGS = new String[]{"invoice","0",name};
        }

        return dbr.query(TABLE_TRANSACTION, columns, mWHERE, mWHERE_ARGS, null, null, null, null);
    }

    Cursor searchInvoice(int id){
        String[] columns = {COLUMN_INVOICE_PRINT};
        String mWHERE = COLUMN_INVOICE_TRANSACTION_NUMBER+" = ?";
        String[] mWHERE_ARGS = new String[]{Integer.toString(id)};
        return dbr.query(TABLE_INVOICE, columns, mWHERE, mWHERE_ARGS, null, null, null, null);
    }

    int searchCancel(){
        String[] columns = {_ID};
        String mWHERE = COLUMN_TRANSACTION_TYPE+" = ?";
        String[] mWHERE_ARGS = new String[]{"cancel"};
        Cursor curse = dbr.query(TABLE_TRANSACTION,columns,mWHERE,mWHERE_ARGS,null,null,null,null);
        int i = curse.getCount();
        curse.close();
        return i;
    }

    int searchProdForUpdate(String id) {
        String[] columns = {"COUNT(*)"};
        String mWHERE = COLUMN_PRODUCT_ID + " = ?";
        String[] mWHERE_ARGS = new String[]{id};
        Cursor curse = dbr.query(TABLE_PRODUCT, columns, mWHERE, mWHERE_ARGS, null, null, null, null);
        int i = curse.getCount();
        curse.close();
        return i;
    }

    //MARK'S FUNCTION
    void insertTempInvoice(InvoiceItem invoiceItem){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TEMP_NAME, invoiceItem.getInvoiceProductName());
        values.put(COLUMN_TEMP_DESCRIPTION, invoiceItem.getInvoiceProductDescription());
        values.put(COLUMN_TEMP_PRICE, invoiceItem.getInvoiceProductPrice());
        values.put(COLUMN_TEMP_QUANTITY, invoiceItem.getInvoiceProductQuantity());
        values.put(COLUMN_TEMP_ID, invoiceItem.getInvoiceProductID());
        values.put(COLUMN_TEMP_TOTALPRICE, invoiceItem.getInvoiceProductTotal());

        database.insert(TABLE_TEMP_INVOICING, null, values);
//        database.close();
    }

    void deleteTempItemInvoice(String itemDes){
        try {
            this.getWritableDatabase().execSQL("DELETE FROM " + TABLE_TEMP_INVOICING + " WHERE " + COLUMN_TEMP_ID +"='" + itemDes + "'");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

//    int searchDeletedTotal(String itemID) {
//        SQLiteDatabase database = this.getReadableDatabase();
//        String[] selectionArgs = new String[]{itemID};
//        int i;
//        Cursor cursor;
//        cursor = database.rawQuery("SELECT "+ _ID +" FROM " + TABLE_TEMP_INVOICING + " WHERE " + COLUMN_TEMP_ID + "=?",selectionArgs);
//        cursor.moveToFirst();
//        i = cursor.getPosition();
//        Log.e("GETPOSITION : ", i+"");
//        cursor.close();
//        return i;
//
//    }

    int searchSelectedItem(String isSelected) {
        SQLiteDatabase database = this.getReadableDatabase();
        String[] selectionArgs = new String[]{isSelected};
        int i;
        Cursor cursor;
        cursor = database.rawQuery("SELECT "+ _ID +" FROM " + TABLE_TEMP_INVOICING + " WHERE " + COLUMN_TEMP_TOTALPRICE + "=?",selectionArgs);
        cursor.moveToFirst();
        i = cursor.getPosition();
        Log.e("GETPOSITION : ", i+"");
        cursor.close();
        return i;
    }

    double totalPrice(){
        double total;
        String[] columns = {"SUM("+COLUMN_TEMP_TOTALPRICE+")"};
        Cursor cursor = dbr.query(TABLE_TEMP_INVOICING, columns, null, null, null, null, null, null);
        cursor.moveToFirst();
        total = cursor.getDouble(0);
        cursor.close();
        return total;
    }

    int getQuantityCount(){
        int quanCount;
        String[] columns = {"SUM("+COLUMN_TEMP_QUANTITY+")"};
        Cursor cursor = dbr.query(TABLE_TEMP_INVOICING, columns,null, null, null, null, null, null);
        cursor.moveToFirst();
        quanCount = cursor.getInt(0);
        cursor.close();
        Log.e("QuantityCount : ", quanCount+"");
        return quanCount;
    }

    void deleteAllTempItemInvoice(){
        try {
            this.getWritableDatabase().execSQL("DELETE FROM " + TABLE_TEMP_INVOICING);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    Cursor selectAllTempInvoice(){
        // TODO: 8/19/2017
        return dbr.rawQuery("SELECT * FROM "+ TABLE_TEMP_INVOICING,null);
    }

    int searchDuplicateInvoice(String itemID) {
        SQLiteDatabase database = this.getReadableDatabase();
        String[] selectionArgs = new String[]{itemID};
        try {
            int i;
            Cursor cursor;
            cursor = database.rawQuery("SELECT * FROM " + TABLE_TEMP_INVOICING + " WHERE " + COLUMN_TEMP_ID + "=?",selectionArgs);
            cursor.moveToFirst();
            i = cursor.getCount();
            cursor.close();
            return i;
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    int searchDuplicateProduct(String itemID) {
        SQLiteDatabase database = this.getReadableDatabase();
        String[] selectionArgs = new String[]{itemID};
        try {
            int i;
            Cursor cursor;
            cursor = database.rawQuery("SELECT * FROM " + TABLE_PRODUCT + " WHERE " + COLUMN_PRODUCT_ID + "=?", selectionArgs);
            cursor.moveToFirst();
            i = cursor.getCount();
            cursor.close();
            return i;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    void updateInvoiceItem(String code, int newQuantity, double newTotalPrice){
        this.getWritableDatabase().execSQL("UPDATE "+ TABLE_TEMP_INVOICING + " SET "
                + COLUMN_TEMP_TOTALPRICE +"='"+ newTotalPrice + "'," + COLUMN_TEMP_QUANTITY +"='"+ newQuantity + "' WHERE "+ COLUMN_TEMP_ID+"='" + code + "'");
    }
    double getGrossSales(String x){
        double gross;
        String mWHERE;
        String[] mWHERE_ARGS;
        String[] columns = {"SUM("+COLUMN_INVOICE_NORMALSALE+")"};
        if(x.equals("no")){
            mWHERE = COLUMN_INVOICE_ZREPORT_STATUS+" = ?";
            mWHERE_ARGS = new String[]{"0"};
        }
        else {
            mWHERE = COLUMN_INVOICE_XREPORT_STATUS+" = ? AND "+COLUMN_INVOICE_CASHIER_NUMBER+" = ?";
            mWHERE_ARGS = new String[]{"0",x};
        }
        Cursor cursor = dbr.query(TABLE_INVOICE, columns, mWHERE, mWHERE_ARGS, null, null, null, null);
        cursor.moveToFirst();
        gross = cursor.getDouble(0);
        cursor.close();
        Log.e("Gross getSale : ", gross + "");
        return  gross;
    }
    String getDiscountSales(String x){
        String discount;
        String mWHERE;
        String[] mWHERE_ARGS;
        String[] columns = {"SUM("+COLUMN_INVOICE_DISCOUNT+")"};
        if(x.equals("no")){
            mWHERE = COLUMN_INVOICE_ZREPORT_STATUS +" = ?";
            mWHERE_ARGS = new String[]{"0"};
        }
        else {
            mWHERE = COLUMN_INVOICE_XREPORT_STATUS+" = ? AND "+COLUMN_INVOICE_CASHIER_NUMBER+" = ?";
                mWHERE_ARGS = new String[]{"0",x};
            }
        Cursor cursor = dbr.query(TABLE_INVOICE, columns, mWHERE, mWHERE_ARGS, null, null, null, null);
            cursor.moveToFirst();
            discount = cursor.getString(0);
            cursor.close();
        if (discount == null)
            return "0";
        else
            return  discount;
        }
    String pleaseGetTheSalesForMe(String x, String status){
        String sales;
        String mWHERE;
        String[] mWHERE_ARGS;
        String[] columns = {"SUM(" + COLUMN_INVOICE_VATTABLE + ")"};
        if (x.equals("no")) {
            mWHERE = COLUMN_INVOICE_ZREPORT_STATUS + " = ? AND " + COLUMN_INVOICE_VAT_STATUS + " = ?";
            mWHERE_ARGS = new String[]{"0", status};
        } else {
            mWHERE = COLUMN_INVOICE_XREPORT_STATUS + " = ? AND " + COLUMN_INVOICE_CASHIER_NUMBER + " = ? AND " + COLUMN_INVOICE_VAT_STATUS + " = ?";
            mWHERE_ARGS = new String[]{"0", x, status};
        }
        Cursor cursor = dbr.query(TABLE_INVOICE, columns, mWHERE, mWHERE_ARGS, null, null, null, null);
        cursor.moveToFirst();
        sales = cursor.getString(0);
        cursor.close();
        return sales;
    }

    String pleaseGetTheTaxForMe(String x, String status){
        String tax;
        String mWHERE;
        String[] mWHERE_ARGS;
        String[] columns = {"SUM(" + COLUMN_INVOICE_VATTED + ")"};
        if (x.equals("no")) {
            mWHERE = COLUMN_INVOICE_ZREPORT_STATUS + " = ? AND " + COLUMN_INVOICE_VAT_STATUS + " = ?";
            mWHERE_ARGS = new String[]{"0", status};
        } else {
            mWHERE = COLUMN_INVOICE_XREPORT_STATUS + " = ? AND " + COLUMN_INVOICE_CASHIER_NUMBER + " = ? AND " + COLUMN_INVOICE_VAT_STATUS + " = ?";
            mWHERE_ARGS = new String[]{"0", x, status};
        }
        Cursor cursor = dbr.query(TABLE_INVOICE, columns, mWHERE, mWHERE_ARGS, null, null, null, null);
        cursor.moveToFirst();
        tax = cursor.getString(0);
        cursor.close();
        return tax;
    }

    int getZCount() {
        int z;
        String[] columns = {"COUNT(*)"};
        String mWHERE = COLUMN_TRANSACTION_TYPE+" = ?";
        String[] mWHERE_ARGS = new String[]{"zreport"};
        Cursor cursor = dbr.query(TABLE_TRANSACTION,columns,mWHERE,mWHERE_ARGS,null,null,null,null);
        cursor.moveToFirst();
        z = cursor.getInt(0);
        cursor.close();
        return z;
    }

    int getRefundCount(String x) {
        int refund;
        String mWHERE;
        String[] mWHERE_ARGS;
        String[] columns = {"COUNT(*)"};
        if (x.equals("no")) {
            mWHERE = COLUMN_INVOICE_NORMALSALE + " < ? AND " + COLUMN_INVOICE_ZREPORT_STATUS + " = ?";
            mWHERE_ARGS = new String[]{"0", "0"};
        } else {
            mWHERE = COLUMN_INVOICE_NORMALSALE + " < ? AND " + COLUMN_INVOICE_XREPORT_STATUS + " = ?";
            mWHERE_ARGS = new String[]{"0", "0"};
        }
        Cursor cursor = dbr.query(TABLE_INVOICE, columns, mWHERE, mWHERE_ARGS, null, null, null, null);
        cursor.moveToFirst();
        refund = cursor.getInt(0);
        cursor.close();
        return refund;
    }

    int getTotalRefund(String x) {
        int refund;
        String mWHERE;
        String[] mWHERE_ARGS;
        String[] columns = {"SUM(" + COLUMN_INVOICE_NORMALSALE + ")"};
        if (x.equals("no")) {
            mWHERE = COLUMN_INVOICE_NORMALSALE + " < ? AND " + COLUMN_INVOICE_ZREPORT_STATUS + " = ?";
            mWHERE_ARGS = new String[]{"0", "0"};
        } else {
            mWHERE = COLUMN_INVOICE_NORMALSALE + " < ? AND " + COLUMN_INVOICE_XREPORT_STATUS + " = ?";
            mWHERE_ARGS = new String[]{"0", "0"};
        }
        Cursor cursor = dbr.query(TABLE_INVOICE, columns, mWHERE, mWHERE_ARGS, null, null, null, null);
        cursor.moveToFirst();
        refund = cursor.getInt(0);
        cursor.close();
        return refund;
    }

    int[] getFirstAndLastTransaction() {
        int[] t = new int[2];
        String[] columns = {_ID};
        String mWHERE = COLUMN_TRANSACTION_ZREPORT + " = ?";
        String[] mWHERE_ARGS = new String[]{"0"};
        Cursor cursor = dbr.query(TABLE_TRANSACTION, columns, mWHERE, mWHERE_ARGS, null, null, null, null);
        cursor.moveToFirst();
        t[0] = cursor.getInt(0);
        cursor.moveToLast();
        t[1] = cursor.getInt(0);
        cursor.close();
        return t;
    }

    int[] getFirstAndLastReceipt() {
        try {
            int[] t = new int[2];
            String[] columns = {_ID};
            String mWHERE = COLUMN_INVOICE_ZREPORT_STATUS + " = ?";
            String[] mWHERE_ARGS = new String[]{"0"};
            Cursor cursor = dbr.query(TABLE_INVOICE, columns, mWHERE, mWHERE_ARGS, null, null, null, null);
            cursor.moveToFirst();
            t[0] = cursor.getInt(0);
            cursor.moveToLast();
            t[1] = cursor.getInt(0);
            cursor.close();
            return t;
        } catch (CursorIndexOutOfBoundsException ce) {
            return new int[]{0, 0};
        }
    }

    String pleaseGiveMeTheSumOfAll20PercentDiscountForXandZreport(String x, String status){
        String mWHERE;
        String[] mWHERE_ARGS;
        String[] columns = {"SUM(" + COLUMN_ITEM_DISCOUNT + ")"};
        if (x.equals("no")) {
            mWHERE = COLUMN_INVOICE_ZREPORT_STATUS + " = ? AND " + COLUMN_INVOICE_VAT_STATUS + " = ?";
            mWHERE_ARGS = new String[]{"0", status};
        } else {
            mWHERE = COLUMN_INVOICE_XREPORT_STATUS + " = ? AND " + COLUMN_INVOICE_CASHIER_NUMBER + " = ? AND " + COLUMN_INVOICE_VAT_STATUS + " = ?";
            mWHERE_ARGS = new String[]{"0", x, status};
        }
        Cursor cursor = dbr.query(TABLE_INVOICE, columns, mWHERE, mWHERE_ARGS, null, null, null, null);
        cursor.moveToFirst();
        String discount = cursor.getString(0);
        cursor.close();
        return discount;
    }

    //Grand Total is the sum of all accumulated NET SALES.
    double getGrandTotalOld() {
        double gross;
        String[] columns = {"SUM("+COLUMN_TOTAL_GRAND+")"};
        Cursor cursor = dbr.query(TABLE_TOTAL, columns, null, null, null, null, null, null);
        cursor.moveToFirst();
        try {
            gross = cursor.getDouble(0);
            cursor.close();
            double sale = getNormalSales("no") + getCreditSales("no");
            // TODO
            cv.clear();
            cv.put(COLUMN_TOTAL_GRAND,sale);
            dbw.insertOrThrow(TABLE_TOTAL, null, cv);
        }
        catch (Exception e){
            gross = 0.0;
        }
        cursor.close();
        return gross;
    }

    Cursor getItems(String x) {
        String mWHERE;
        String[] mWHERE_ARGS;
        String[] columns = {COLUMN_ITEM_NAME, COLUMN_ITEM_DESC, "SUM(" + COLUMN_ITEM_QUANTITY + ")", "SUM(" + COLUMN_ITEM_DISCOUNT + ")", COLUMN_ITEM_PRICE};

        if(x.equals("no")) {
            mWHERE = COLUMN_ITEM_ZREPORT+" = ?";
            mWHERE_ARGS = new String[]{"0"};
        }
        else {
            mWHERE = COLUMN_ITEM_XREPORT+" = ? AND "+COLUMN_ITEM_CASHIER+" = ?";
            mWHERE_ARGS = new String[]{"0",x};
        }
        return dbr.query(TABLE_ITEM, columns, mWHERE, mWHERE_ARGS, COLUMN_ITEM_NAME, null, null, null);
    }

    Cursor testTransaction() {
        String[] columns = {_ID, COLUMN_TRANSACTION_ZREPORT};

        return dbr.query(TABLE_TRANSACTION, columns, null, null, null, null, null);
    }

    String getSale(String status, String x) {
        String mWHERE;
        String[] mWHERE_ARGS;
        String[] columns = {"SUM("+COLUMN_INVOICE_VATTABLE+")"};
        if(x.equals("no")){
            mWHERE = COLUMN_INVOICE_ZREPORT_STATUS+" = ? AND "+ COLUMN_INVOICE_VAT_STATUS + " = ?";
            mWHERE_ARGS = new String[]{"0",status};
        }
        else {
            mWHERE = COLUMN_INVOICE_XREPORT_STATUS+" = ? AND "+COLUMN_INVOICE_CASHIER_NUMBER+" = ? AND "+ COLUMN_INVOICE_VAT_STATUS + " = ?";
            mWHERE_ARGS = new String[]{"0",x,status};
        }
        Cursor c = dbr.query(TABLE_INVOICE,columns,mWHERE,mWHERE_ARGS,null,null,null);
        c.moveToFirst();
        String sales = c.getString(0);
        c.close();
        if (sales == null)
            sales = "0";
        return sales;
    }

    String getTax(String status, String x) {
        String mWHERE;
        String[] mWHERE_ARGS;
        String[] COLUMNS = {"SUM(" + COLUMN_INVOICE_VATTED + ")"};
        if (x.equals("no")) {
            mWHERE = COLUMN_INVOICE_ZREPORT_STATUS + " = ? AND " + COLUMN_INVOICE_VAT_STATUS + " = ?";
            mWHERE_ARGS = new String[]{"0", status};
        } else {
            mWHERE = COLUMN_INVOICE_XREPORT_STATUS + " = ? AND " + COLUMN_INVOICE_CASHIER_NUMBER + " = ? AND " + COLUMN_INVOICE_VAT_STATUS + " = ?";
            mWHERE_ARGS = new String[]{"0", x, status};
        }

        Cursor c = dbr.query(TABLE_INVOICE, COLUMNS, mWHERE, mWHERE_ARGS, null, null, null);
        c.moveToFirst();
        String tax = c.getString(0);
        c.close();
        if (tax == null)
            tax = "0";
        return tax;
    }

    void copyToProductTemp() {
        String sql = "DELETE FROM " + TABLE_PRODUCT_TEMP;
        dbr.execSQL(sql);

        sql = "INSERT INTO " + TABLE_PRODUCT_TEMP + " SELECT * FROM " + TABLE_PRODUCT;
        dbr.execSQL(sql);
    }

    int getProdTempCount() {
        Cursor cursor = dbr.rawQuery("SELECT * FROM " + TABLE_PRODUCT_TEMP, null);
        cursor.moveToFirst();
        int a = cursor.getCount();
        cursor.close();
        return a;
    }

    int getQuantityofProducts(String prodID) {
        Cursor c;
        String mWHERE;
        String[] mWHERE_ARGS;
        String[] columns = {COLUMN_PRODUCT_QUANTITY};
        mWHERE = COLUMN_PRODUCT_ID + " = ?";
        mWHERE_ARGS = new String[]{prodID};
        c = dbr.query(TABLE_PRODUCT, columns, mWHERE, mWHERE_ARGS, null, null, null, null);
        c.moveToFirst();
        int quan = c.getInt(c.getColumnIndex(COLUMN_PRODUCT_QUANTITY));
        c.close();
        return quan;
    }

    void updateProductQuantity(String prodID, int newQuan) {
        cv.clear();
        String mWHERE = COLUMN_PRODUCT_ID + " = ?";
        String[] mWHERE_ARGS = new String[]{prodID};
        cv.put(COLUMN_PRODUCT_QUANTITY, newQuan);
        dbw.update(TABLE_PRODUCT, cv, mWHERE, mWHERE_ARGS);
    }

    void updateAddQuantity(String id, int quantity) {
        cv.clear();
        String WHERE_CASH = COLUMN_PRODUCT_ID + " = ?";
        String[] WHERE_ARGS_CASH = new String[]{id};
        cv.put(COLUMN_PRODUCT_QUANTITY, quantity);
        dbw.update(TABLE_PRODUCT, cv, WHERE_CASH, WHERE_ARGS_CASH);
    }

    void modifyItem(String id, String name, String description, double price) {
        cv.clear();
        String WHERE_CASH = COLUMN_PRODUCT_ID + " = ?";
        String[] WHERE_ARGS_CASH = new String[]{id};
        cv.put(COLUMN_PRODUCT_NAME, name);
        cv.put(COLUMN_PRODUCT_DESCRIPTION, description);
        cv.put(COLUMN_PRODUCT_QUANTITY, price);
        dbw.update(TABLE_PRODUCT, cv, WHERE_CASH, WHERE_ARGS_CASH);
    }

    void minusItemQuantity(String id, int newQuantity) {
        cv.clear();
        String WHERE_CASH = COLUMN_PRODUCT_ID + " = ?";
        String[] WHERE_ARGS_CASH = new String[]{id};
        cv.put(COLUMN_PRODUCT_QUANTITY, newQuantity);
        dbw.update(TABLE_PRODUCT, cv, WHERE_CASH, WHERE_ARGS_CASH);
    }

    void addProductLogs(String prodLogID, String prodLogType, int prodLogValueAdded, int prodLogValueMinus, String prodLogRemarks, String prodLogDate) {
        cv.clear();
        cv.put(COLUMN_PRODUCTLOGS_BARCODE, prodLogID);
        cv.put(COLUMN_PRODUCTLOGS_TYPE, prodLogType);
        cv.put(COLUMN_PRODUCTLOGS_VALUEADDED, prodLogValueAdded);
        cv.put(COLUMN_PRODUCTLOGS_VALUEMINUS, prodLogValueMinus);
        cv.put(COLUMN_PRODUCTLOGS_REMARKS, prodLogRemarks);
        cv.put(COLUMN_PRODUCTLOGS_DATE, prodLogDate);
        dbw.insertOrThrow(TABLE_PRODUCTLOGS, null, cv);
    }

    void addCredit(String inTrans, String inCashierNum, String inDateAndTime, double inCreditSale, String inCreditBank, String inCreditCardNum, String inCreditExp) {
        cv.clear();
        cv.put(COLUMN_CREDIT_TRANS, inTrans);
        cv.put(COLUMN_CREDIT_CASHIER, inCashierNum);
        cv.put(COLUMN_CREDIT_DATE, inDateAndTime);
        cv.put(COLUMN_CREDIT_PAYMENT, inCreditSale);
        cv.put(COLUMN_CREDIT_BANK, inDateAndTime);
        cv.put(COLUMN_CREDITT_NUMBER, inCreditCardNum);
        cv.put(COLUMN_CREDIT_EXPIRY, inCreditExp);
        dbw.insertOrThrow(TABLE_CREDIT_CARD, null, cv);
    }

    double[] getHourlyGross(String x) {
        double[] gross = new double[24];
        String mWHERE;
        String[] mWHERE_ARGS;
        String[] columns = {"SUM(" + COLUMN_INVOICE_NORMALSALE + ")", "SUM(" + COLUMN_INVOICE_CREDITSALE + ")"};
        for (int mHour = 0; mHour < 24; mHour++) {
            if (x.equals("no")) {
                mWHERE = COLUMN_INVOICE_ZREPORT_STATUS + " = ? AND " + COLUMN_INVOICE_DATEANDTIME + " = ?";
                String time = String.format(Locale.ENGLISH, "%1$02d", mHour);
                mWHERE_ARGS = new String[]{"0", fuc.getYesterDay() + " " + time + ":__"};
                Log.e("mWHERE_ARGS", mWHERE_ARGS[1] + " Z");
            } else {
                mWHERE = COLUMN_INVOICE_XREPORT_STATUS + " = ? AND " +
                        COLUMN_INVOICE_CASHIER_NUMBER + " = ? AND " +
                        COLUMN_INVOICE_DATEANDTIME + " LIKE ?";
                String time = String.format(Locale.ENGLISH, "%1$02d", mHour);
                mWHERE_ARGS = new String[]{"0", x, "'" + fuc.getYesterDay() + " " + time + ":_%_%'"};
                Log.e("mWHERE_ARGS : ", mWHERE_ARGS[0] + "" + mWHERE_ARGS[2]);
            }
            Cursor cursor = dbr.query(TABLE_INVOICE, columns, mWHERE, mWHERE_ARGS, null, null,
                    null, null);
                cursor.moveToFirst();
                gross[mHour] = cursor.getDouble(0) + cursor.getDouble(1);
                cursor.close();
            Log.e("Hourly Gross getSale", gross[mHour] + " | Date from INVOICE:" +
                    COLUMN_INVOICE_DATEANDTIME + "|date presses z" + fuc.getYesterDay());
        }
        return gross;
    }

    double getCreditSales(String x) {
        double credit;
        String mWHERE;
        String[] mWHERE_ARGS;
        String[] columns = {"SUM(" + COLUMN_INVOICE_CREDITSALE + ")"};
        if (x.equals("no")) {
            mWHERE = COLUMN_INVOICE_ZREPORT_STATUS + " = ?";
            mWHERE_ARGS = new String[]{"0"};
        } else {
            mWHERE = COLUMN_INVOICE_XREPORT_STATUS + " = ? AND " + COLUMN_INVOICE_CASHIER_NUMBER + " = ?";
            mWHERE_ARGS = new String[]{"0", x};
        }
        Cursor cursor = dbr.query(TABLE_INVOICE, columns, mWHERE, mWHERE_ARGS, null, null, null, null);
        cursor.moveToFirst();
        credit = cursor.getDouble(0);
        cursor.close();
        Log.e("Credit getSale : ", credit + "");
        return credit;
    }

    double getNormalSales(String x) {
        double normal;
        String mWHERE;
        String[] mWHERE_ARGS;
        String[] columns = {"SUM(" + COLUMN_INVOICE_NORMALSALE + ")"};
        if (x.equals("no")) {
            mWHERE = COLUMN_INVOICE_ZREPORT_STATUS + "=?";
            mWHERE_ARGS = new String[]{"0"};
        } else {
            mWHERE = COLUMN_INVOICE_XREPORT_STATUS + " = ? AND " + COLUMN_INVOICE_CASHIER_NUMBER + " = ?";
            mWHERE_ARGS = new String[]{"0", x};
        }
        Cursor cursor = dbr.query(TABLE_INVOICE, columns, mWHERE, mWHERE_ARGS, null, null, null);
        cursor.moveToFirst();
        normal = cursor.getDouble(0);
        cursor.close();
        Log.e("Normal getSale : ", normal + "");
        return normal;
    }

    String getTheCashierLevel(String userNum) {
        String[] columns = {COLUMN_CASHIER_POSITION};
        String mWHERE = COLUMN_CASHIER_NUMBER + " = ?";
        String[] mWHERE_ARGS = new String[]{userNum};
        Cursor cursor = dbr.query(TABLE_CASHIER, columns, mWHERE, mWHERE_ARGS, null, null, null, null);
        cursor.moveToFirst();
        String level = cursor.getString(0);
        cursor.close();
        Log.e("Cashier Level", level);
        return level;
    }

    String getPrintForTransactionNumber(int position) {
        String mWHERE = _ID + " = ?";
        String[] mWHERE_ARGS = new String[]{position + ""};
        String[] columns = {COLUMN_TRANSACTION_PRINT};
        Cursor cursor = dbr.query(TABLE_TRANSACTION, columns, mWHERE, mWHERE_ARGS, null, null, null, null);
        cursor.moveToFirst();
        String print = cursor.getString(0);
        cursor.close();
        Log.e("Print", print);
        return print;
    }

    double getCashFlow(String cashierNum, String date) {
        double currentCashinout, minuin, subtrahend;

        String WHERE;
        String[] IS_EQUAL;
        String[] COLUMNS = {COLUMN_CASHTRANS_ADDCASH, COLUMN_CASHTRANS_MINNUSCASH};

        WHERE = COLUMN_CASHTRANS_CASHIERNUM + " = ? AND " + COLUMN_CASHTRANS_DATETIME + " = ?";

        IS_EQUAL = new String[]{"'" + cashierNum + "'", "'" + date + "%'"};
        Log.e("DB_Data.java ", "cashiernum =" + cashierNum + " date =" + date);
        Cursor cursor = dbr.query(TABLE_CASHTRANS, COLUMNS, WHERE, IS_EQUAL, null, null, null, null);
        cursor.moveToFirst();
        try {
            minuin = cursor.getDouble(0);
            subtrahend = cursor.getDouble(1);
        } catch (Exception e) {
            minuin = 0;
            subtrahend = 0;
            e.printStackTrace();
        }
        cursor.close();
        currentCashinout = minuin - subtrahend;
        return currentCashinout;
    }

    int checkSerial() {
        String[] mCOLUMNS = {"COUNT(*)"};
        Cursor cursor = dbr.query(TABLE_TERMINAL, mCOLUMNS, null, null, null, null, null, null);
        cursor.moveToFirst();
        int a = cursor.getInt(0);
        cursor.close();
        return a;
    }

    int checkForPendingZRead() {
        String[] mCOLUMNS = {"COUNT(*)"};
        String dayWHERE = COLUMN_TRANSACTION_ZREPORT + " = ? AND " + COLUMN_TRANSACTION_DATETIME + " NOT LIKE ?";
        String[] dayWHERE_ARGS = new String[]{"'0'", "'" + fuc.getYesterDay() + "%'"};

        Cursor cursor = dbr.query(TABLE_TRANSACTION, mCOLUMNS, dayWHERE, dayWHERE_ARGS, null, null, null, null);
        cursor.moveToFirst();
        int mCount = cursor.getInt(0);
        cursor.close();
        if (mCount != 0)
            return 1;
        else
            return 0;
    }
}