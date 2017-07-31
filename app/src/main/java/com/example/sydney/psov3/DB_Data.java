package com.example.sydney.psov3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.example.sydney.psov3.Constants.*;

    class DB_Data extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pos_db.db";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase dbr = this.getReadableDatabase();
    private SQLiteDatabase dbw = this.getWritableDatabase();
    private ContentValues cv = new ContentValues();
        private ContentValues cv1 = new ContentValues();

        DB_Data(Context ctx){
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase arg0) {
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
                +COLUMN_PRODUCT_ID+" INTEGER NOT NULL UNIQUE, "
                +COLUMN_PRODUCT_NAME+" TEXT NOT NULL, "
                +COLUMN_PRODUCT_DESCRIPTION+" TEXT NOT NULL, "
                +COLUMN_PRODUCT_PRICE+" DOUBLE NOT NULL,"
                +COLUMN_PRODUCT_QUANTITY+" INTEGER NOT NULL, "
                +COLUMN_PRODUCT_VATABLE+" INTEGER );");
        arg0.execSQL("CREATE TABLE "+TABLE_INVOICE+" ("
                +_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +COLUMN_INVOICE_TRANSACTION_NUMBER +" INTEGER NOT NULL ,"
                +COLUMN_INVOICE_DISCOUNT+" INTEGER,"
                +COLUMN_INVOICE_NORMALSALE +" INTEGER NOT NULL,"
                +COLUMN_INVOICE_PRINT+" TEXT,"
                +COLUMN_INVOICE_CASHIER_NUMBER+" TEXT,"
                +COLUMN_INVOICE_ZREPORT_STATUS +" TEXT,"
                +COLUMN_INVOICE_XREPORT_STATUS +" TEXT,"
                +COLUMN_INVOICE_VATTABLE+ " INTEGER,"
                +COLUMN_INVOICE_VATTED+ " INTEGER,"
                +COLUMN_INVOICE_VAT_STATUS+ " TEXT,"
                +COLUMN_INVOICE_CREDITSALE+ " FLOAT,"
                +COLUMN_INVOICE_DATEANDTIME+ " TEXT,"
                +COLUMN_INVOICE_CREDITCARDNUMBER+ " TEXT,"
                +COLUMN_INVOICE_CREDITDATEOFEXPIRATION+ " TEXT);");


        arg0.execSQL("CREATE TABLE "+TABLE_ITEM+" ("
                +_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +COLUMN_ITEM_INVOICE+" INTEGER NOT NULL, "
                +COLUMN_ITEM_NAME+" TEXT NOT NULL, "
                +COLUMN_ITEM_DESC+" TEXT NOT NULL, "
                +COLUMN_ITEM_PRICE+" TEXT NOT NULL, "
                +COLUMN_ITEM_PRODUCT+" INTEGER NOT NULL, "
                +COLUMN_ITEM_QUANTITY+" INTEGER NOT NULL, "
                +COLUMN_ITEM_DISCOUNT+" TEXT NOT NULL, "
                +COLUMN_ITEM_STATUS+" INTEGER NOT NULL,"
                +COLUMN_ITEM_XREPORT+" TEXT NOT NULL,"
                +COLUMN_ITEM_ZREPORT+" TEXT NOT NULL,"
                +COLUMN_ITEM_CASHIER+" TEXT NOT NULL );");
        arg0.execSQL("CREATE TABLE IF NOT EXISTS cashierlog(date TEXT, time TEXT,userNum TEXT,lastname TEXT,username TEXT,transactionnumber INTEGER PRIMARY KEY AUTOINCREMENT);");
        arg0.execSQL("CREATE TABLE IF NOT EXISTS sessions(time TEXT,date TEXT, username TEXT ); ");
        arg0.execSQL("CREATE TABLE IF NOT EXISTS receipts(invoicenumber INTEGER,transactionnumber INTEGER);");
        arg0.execSQL("CREATE TABLE IF NOT EXISTS departments(department TEXT,category TEXT,subcategory TEXT);");
        arg0.execSQL("CREATE TABLE "+TABLE_XREPORT+ " ("
                +_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +COLUMN_XREPORT_TRANSACTION_NUMBER+" INTEGER NOT NULL, "
                +COLUMN_XREPORT_CASHSALES + " FLOAT,"
                +COLUMN_XREPORT_CASHCOUNT + " FLOAT,"
                +COLUMN_XREPORT_CASHSHORTOVER + " FLOAT);");
        arg0.execSQL("CREATE TABLE "+TABLE_ZREPORT+ " ("
                +_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +COLUMN_ZREPORT_TRANSACTION_NUMBER+" INTEGER NOT NULL, "
                +COLUMN_ZREPORT_CASHSALES+" FLOAT, "
                +COLUMN_ZREPORT_CASHCOUNT+" FLOAT);");
        arg0.execSQL("CREATE TABLE "+ TABLE_TRANSACTION+ "("
                +_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +COLUMN_TRANSACTION_TYPE+" TEXT NOT NULL, "
                +COLUMN_TRANSACTION_DATETIME+" TEXT NOT NULL, "
                +COLUMN_TRANSACTION_CASHIER+" TEXT NOT NULL, "
                +COLUMN_TRANSACTION_XREPORT+" INTEGER NOT NULL, "
                +COLUMN_TRANSACTION_ZREPORT+" INTEGER NOT NULL);");
        arg0.execSQL("CREATE TABLE "+ TABLE_LOG + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_LOG_STRING + " TEXT NOT NULL);");
        arg0.execSQL("CREATE TABLE "+ TABLE_TEMP_INVOICING + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TEMP_DESCRIPTION + " TEXT NOT NULL,"
                + COLUMN_TEMP_PRICE + " TEXT NOT NULL,"
                + COLUMN_TEMP_QUANTITY + " TEXT NOT NULL,"
                + COLUMN_TEMP_ID + " TEXT NOT NULL,"
                + COLUMN_TEMP_TOTALPRICE + " INTEGER);");

        arg0.execSQL("CREATE TABLE "+ TABLE_TOTAL + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TOTAL_GRAND + " DOUBLE);");

    }
    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        arg0.execSQL("DROP TABLE IF EXISTS "+TABLE_ADMIN);
        onCreate(arg0);
        arg0.execSQL("DROP TABLE IF EXISTS "+TABLE_CASHIER);
        onCreate(arg0);
        arg0.execSQL("DROP TABLE IF EXISTS "+TABLE_PRODUCT);
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
    }

    private static String[] FROM_ADMIN = {COLUMN_ADMIN_USERNAME,COLUMN_ADMIN_PASSWORD};
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

    private static String[] FROM_CASH = {COLUMN_CASHIER_PASSWORD};
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

    void addCashier(String Name,String UserNum, String Pass, String Pos){
        cv.clear();
        cv.put(COLUMN_CASHIER_NAME, Name);
        cv.put(COLUMN_CASHIER_NUMBER, UserNum);
        cv.put(COLUMN_CASHIER_POSITION, Pos);
        cv.put(COLUMN_CASHIER_PASSWORD, Pass);
        dbw.insertOrThrow(TABLE_CASHIER, null, cv);
    }

    void addProduct(String ProdId,String ProdName,String ProdDesc, String ProdPrice, String ProdQuan){
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

        void addGrandTotal(){
            cv.clear();
            cv.put(COLUMN_TOTAL_GRAND, 0.0);
            dbw.insertOrThrow(TABLE_TOTAL, null, cv);
        }


        void addInvoice(String inTrans, String inDisc, String inCustomer, String inPrint, String inCashierNum, String inZreport, String inXreport, String inVattable, Double inVatted, String inVatStatus, Double inCreditSale, String inDateAndTime, String inCreditCardNum, String inCreditExp){
        cv.clear();
        cv.put(COLUMN_INVOICE_TRANSACTION_NUMBER,inTrans);
        cv.put(COLUMN_INVOICE_DISCOUNT,inDisc);
        cv.put(COLUMN_INVOICE_NORMALSALE,inCustomer);
        cv.put(COLUMN_INVOICE_PRINT,inPrint);
        cv.put(COLUMN_INVOICE_CASHIER_NUMBER,inCashierNum);
        cv.put(COLUMN_INVOICE_ZREPORT_STATUS,inZreport);
        cv.put(COLUMN_INVOICE_XREPORT_STATUS,inXreport);
        cv.put(COLUMN_INVOICE_VATTABLE,inVattable);
        cv.put(COLUMN_INVOICE_VATTED,inVatted);
        cv.put(COLUMN_INVOICE_VAT_STATUS,inVatStatus);
        cv.put(COLUMN_INVOICE_CREDITSALE, inCreditSale);
        cv.put(COLUMN_INVOICE_DATEANDTIME, inDateAndTime);
        cv.put(COLUMN_INVOICE_CREDITCARDNUMBER, inCreditCardNum);
        cv.put(COLUMN_INVOICE_CREDITDATEOFEXPIRATION,inCreditExp);
        dbw.insertOrThrow(TABLE_INVOICE, null, cv);
    }

    void addItem(String itemIn, String itemProd, String itemQuan, int itemStatus, String itemName, String itemDesc, Double itemPrice, String itemCashier){
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

    void addTransaction(String transactionType, String transactionDatetime, String transactionCashier, int transactionXreport, int transactionZreport){
        cv.clear();
        cv.put(COLUMN_TRANSACTION_TYPE,transactionType);
        cv.put(COLUMN_TRANSACTION_DATETIME,transactionDatetime);
        cv.put(COLUMN_TRANSACTION_CASHIER,transactionCashier);
        cv.put(COLUMN_TRANSACTION_XREPORT,transactionXreport);
        cv.put(COLUMN_TRANSACTION_ZREPORT,transactionZreport);
        dbw.insertOrThrow(TABLE_TRANSACTION, null, cv);
    }

    void addLog(String log){
        cv.clear();
        cv.put(COLUMN_LOG_STRING,log);
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

    private static String[] ALL = {COLUMN_CASHIER_NUMBER, COLUMN_CASHIER_NAME, COLUMN_CASHIER_POSITION};
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
            cursor = database.rawQuery("SELECT * FROM "+TABLE_CASHIER+" where "+COLUMN_CASHIER_NUMBER+"=?", selectionArgs );
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
        String[] mALL = {COLUMN_PRODUCT_ID,COLUMN_PRODUCT_NAME,COLUMN_PRODUCT_DESCRIPTION,COLUMN_PRODUCT_PRICE,COLUMN_PRODUCT_QUANTITY,COLUMN_PRODUCT_VATABLE};
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

    void updateTransactions(int id,String name){
        cv.clear();
        String WHERE_CASH;
        String[] mWHERE_ARGS;
        if(name.equals("no")){
            WHERE_CASH = _ID + " = ?";
            mWHERE_ARGS = new String[]{Integer.toString(id)};
            cv.put(COLUMN_TRANSACTION_ZREPORT,"1");
        }
        else {
            WHERE_CASH = _ID +" = ? AND "+COLUMN_TRANSACTION_CASHIER+" = ?";
            mWHERE_ARGS = new String[]{Integer.toString(id),name};
            cv.put(COLUMN_TRANSACTION_XREPORT,"1");
        }
        dbw.update(TABLE_TRANSACTION,cv,WHERE_CASH,mWHERE_ARGS);
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

    //MARK'S FUNCTION
    void insertTempInvoice(InvoiceItem invoiceItem){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

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
        Double total;

        String[] columns = {"SUM("+COLUMN_TEMP_TOTALPRICE+")"};

        Cursor cursor = dbr.query(TABLE_TEMP_INVOICING, columns, null, null, null, null, null, null);
        cursor.moveToFirst();
        total = cursor.getDouble(0);
        cursor.close();
        return total;
    }

    void deleteAllTempItemInvoice(){
        try {
            this.getWritableDatabase().execSQL("DELETE FROM " + TABLE_TEMP_INVOICING);
        }catch (SQLException e){
            e.printStackTrace();
        }
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

    public int searchDuplicateProduct(String itemID) {
            SQLiteDatabase database = this.getReadableDatabase();
            String[] selectionArgs = new String[]{itemID};
            try {
                int i;
                Cursor cursor;
                cursor = database.rawQuery("SELECT * FROM " + TABLE_PRODUCT + " WHERE " + COLUMN_PRODUCT_ID + "=?",selectionArgs);
                cursor.moveToFirst();
                i = cursor.getCount();
                cursor.close();
                return i;
            }catch (Exception e){
                e.printStackTrace();
            }
            return 0;
        }



    void updateInvoiceItem(String code, int newQuantity, double newTotalPrice){
        this.getWritableDatabase().execSQL("UPDATE "+ TABLE_TEMP_INVOICING + " SET "
                + COLUMN_TEMP_TOTALPRICE +"='"+ newTotalPrice + "'," + COLUMN_TEMP_QUANTITY +"='"+ newQuantity + "' WHERE "+ COLUMN_TEMP_ID+"='" + code + "'");
    }
    String getGrossSales(String x){
        String gross;
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
        Cursor cursor = dbr.query(TABLE_INVOICE, columns, null, null, null, null, null, null);
        cursor.moveToFirst();
        gross = cursor.getString(0);
        cursor.close();
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
        Cursor cursor = dbr.query(TABLE_INVOICE, columns, null, null, null, null, null, null);
            cursor.moveToFirst();
            discount = cursor.getString(0);
            cursor.close();
            return  discount;
        }
    String pleaseGetTheSalesForMe(String x, String status){
            String sales;
            String mWHERE;
            String[] mWHERE_ARGS;
            String[] columns = {"SUM("+COLUMN_INVOICE_VATTABLE+")"};
            if(x.equals("no")){
                mWHERE = COLUMN_INVOICE_ZREPORT_STATUS+" = ? AND "+COLUMN_INVOICE_VAT_STATUS+" = ?";
                mWHERE_ARGS = new String[]{"0",status};
            }
            else {
                mWHERE = COLUMN_INVOICE_XREPORT_STATUS+" = ? AND "+COLUMN_INVOICE_CASHIER_NUMBER+" = ? AND "+COLUMN_INVOICE_VAT_STATUS+" = ?";
                mWHERE_ARGS = new String[]{"0",x,status};
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
            String[] columns = {"SUM("+COLUMN_INVOICE_VATTED+")"};
            if(x.equals("no")){
                mWHERE = COLUMN_INVOICE_VAT_STATUS+" = ?";
//                mWHERE = COLUMN_INVOICE_ZREPORT_STATUS+" = ? AND "+COLUMN_INVOICE_VAT_STATUS+" = ?";
//                mWHERE_ARGS = new String[]{"0",status};
                mWHERE_ARGS = new String[]{status};
            }
            else {
                mWHERE = COLUMN_INVOICE_VAT_STATUS+" = ?";
//                mWHERE = COLUMN_INVOICE_XREPORT_STATUS+" = ? AND "+COLUMN_INVOICE_CASHIER_NUMBER+" = ? AND "+COLUMN_INVOICE_VAT_STATUS+" = ?";
//                mWHERE_ARGS = new String[]{"0",x,status};
                mWHERE_ARGS = new String[]{status};
            }
            Cursor cursor = dbr.query(TABLE_INVOICE, columns, mWHERE, mWHERE_ARGS, null, null, null, null);
            cursor.moveToFirst();
            tax = cursor.getString(0);
            cursor.close();
            return tax;
        }
    int pleaseGiveMeTheZCount(){
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
    int[] pleaseGiveMeTheFirstAndLastOfTheTransactions(){
        int[] t = new int[2];
        String[] columns = {_ID};
        String mWHERE = COLUMN_TRANSACTION_ZREPORT+" = ?";
        String[] mWHERE_ARGS = new String[]{"0"};
        Cursor cursor = dbr.query(TABLE_TRANSACTION,columns,null,null,null,null,null,null);
        cursor.moveToFirst();
        t[0] = cursor.getInt(0);
        cursor.moveToLast();
        t[1] = cursor.getInt(0);
        cursor.close();
        return t;
    }
    int[] pleaseGiveMeTheFirstAndLastOfTheOfficialReceipt(){
            int[] t = new int[2];
            String[] columns = {_ID};
            String mWHERE = COLUMN_INVOICE_ZREPORT_STATUS+" = ?";
            String[] mWHERE_ARGS = new String[]{"0"};
            Cursor cursor = dbr.query(TABLE_INVOICE,columns,null,null,null,null,null,null);
            cursor.moveToFirst();
            t[0] = cursor.getInt(0);
            cursor.moveToLast();
            t[1] = cursor.getInt(0);
            cursor.close();
            return t;
        }
    String pleaseGiveMeTheSumOfAll20PercentDiscountForXandZreport(String x, String status){
        String discount="";
                String mWHERE;
                String[] mWHERE_ARGS;
                String[] columns = {"SUM("+COLUMN_ITEM_DISCOUNT+")"};
                if(x.equals("no")){
                    mWHERE = COLUMN_INVOICE_ZREPORT_STATUS+" = ? AND "+COLUMN_INVOICE_VAT_STATUS+" = ?";
                    mWHERE_ARGS = new String[]{"0",status};
                }
                else {
                    mWHERE = COLUMN_INVOICE_CASHIER_NUMBER+" = ?";
                    mWHERE = COLUMN_INVOICE_XREPORT_STATUS+" = ? AND "+COLUMN_INVOICE_CASHIER_NUMBER+" = ? AND "+COLUMN_INVOICE_VAT_STATUS+" = ?";
                    mWHERE_ARGS = new String[]{"0",x,status};
                }
                Cursor cursor = dbr.query(TABLE_INVOICE, columns, mWHERE, mWHERE_ARGS, null, null, null, null);
                cursor.moveToFirst();
                discount = cursor.getString(0);
                cursor.close();
            return discount;
        }
        //Grand Total is the sum of all accumulated NET SALES.
    String getMyOldGross(){
        String gross;
        String[] columns = {"SUM("+COLUMN_TOTAL_GRAND+")"};
        Cursor cursor = dbr.query(TABLE_TOTAL, columns, null, null, null, null, null, null);
        cursor.moveToFirst();
        try {
            gross = cursor.getString(0);
            String sale = getGrossSales("no");

            float s = Float.parseFloat(sale);
            // TODO
            cv.clear();
            cv.put(COLUMN_TOTAL_GRAND,s);
            dbw.insertOrThrow(TABLE_TOTAL, null, cv);
        }
        catch (Exception e){
            gross = "0.0";
        }
        cursor.close();
        return gross;
    }
    Cursor getAllItems(String x){
        String mWHERE;
        String[] mWHERE_ARGS;
        String[] columns = {COLUMN_ITEM_NAME,COLUMN_ITEM_DESC,COLUMN_ITEM_QUANTITY,COLUMN_ITEM_DISCOUNT,COLUMN_ITEM_PRICE};
        String groupBy = COLUMN_ITEM_NAME;

        if(x.equals("no")) {
            mWHERE = COLUMN_ITEM_XREPORT+" = ? AND "+COLUMN_ITEM_CASHIER+" = ?";
            mWHERE_ARGS = new String[]{"0"};
        }
        else {
            mWHERE = COLUMN_ITEM_ZREPORT+" = ?";
            mWHERE_ARGS = new String[]{"0"};
        }
         return dbr.query(TABLE_ITEM, columns, null, null, groupBy, null, null, null);
    }

    void addXreport(String transNum,double cashSales, double cashCount, double cashShortOver){
        cv.clear();
        cv.put(COLUMN_XREPORT_TRANSACTION_NUMBER, transNum);
        cv.put(COLUMN_XREPORT_CASHSALES, cashSales);
        cv.put(COLUMN_XREPORT_CASHCOUNT, cashCount);
        cv.put(COLUMN_XREPORT_CASHSHORTOVER, cashShortOver);
        dbw.insertOrThrow(TABLE_XREPORT, null, cv);
    }

String sales(String status){
            String sql = "SELECT SUM("+COLUMN_INVOICE_VATTABLE+") FROM "+ TABLE_INVOICE+" WHERE "+COLUMN_INVOICE_VAT_STATUS+" LIKE "+status;
            Cursor c = dbr.rawQuery(sql,null);
        c.moveToFirst();
        String sales = c.getString(0);
        c.close();
    return sales;
}
        String tax(String status){
            String sql = "SELECT SUM("+COLUMN_INVOICE_VATTED+") FROM "+ TABLE_INVOICE+" WHERE "+COLUMN_INVOICE_VAT_STATUS+" LIKE "+status;
            Cursor c = dbr.rawQuery(sql,null);
            c.moveToFirst();
            String tax = c.getString(0);
            c.close();
            return tax;
        }


}