package com.example.sydney.psov3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.example.sydney.psov3.Constants.*;

public class DB_Data extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pos_db.db";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase dbr = this.getReadableDatabase();
    private SQLiteDatabase dbw = this.getWritableDatabase();
    private ContentValues cv = new ContentValues();

    public DB_Data(Context ctx){
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase arg0) {
        // TODO Auto-generated method stub
        arg0.execSQL("CREATE TABLE "+TABLE_NAME_ADMIN+" ("+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+USERNAME_ADMIN+" TEXT NOT NULL UNIQUE, "+PASSWORD_ADMIN+" TEXT NOT NULL );");
        arg0.execSQL("CREATE TABLE "+TABLE_NAME_CASHIER+" ("+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+NUMBER_CASHIER+" INTEGER NOT NULL UNIQUE, "+NAME_CASHIER+" TEXT NOT NULL, "+POSITION_CASHIER+" TEXT NOT NULL,"+PASSWORD_CASHIER+" TEXT NOT NULL );");
        arg0.execSQL("CREATE TABLE "+TABLE_NAME_PRODUCT+" ("+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+ID_PRODUCT+" INTEGER NOT NULL UNIQUE, "+NAME_PRODUCT+" TEXT NOT NULL, "+DESC_PRODUCT+" TEXT NOT NULL, "+PRICE_PRODUCT+" DOUBLE NOT NULL,"+QUAN_PRODUCT+" INTEGER NOT NULL, "+VATABLE+" INTEGER );");
        arg0.execSQL("CREATE TABLE "+TABLE_NAME_INVOICE+" ("+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+CUSTOMER_DISCOUNT_INVOICE+" INTEGER NOT NULL, "+DATE_INVOICE+" INTEGER NOT NULL, "+TIME_INVOICE+" DATETIME);");
        arg0.execSQL("CREATE TABLE "+TABLE_NAME_ITEM+" ("+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+INVOICE_NUM_ITEM+" INTEGER NOT NULL, "+PRODUCT_ID_ITEM+" INTEGER NOT NULL, "+PRODUCT_QUANTITY_ITEM+" INTEGER NOT NULL );");
        arg0.execSQL("CREATE TABLE IF NOT EXISTS cashierlog(date TEXT, time TEXT,userNum TEXT,lastname TEXT,username TEXT,transactionnumber INTEGER PRIMARY KEY AUTOINCREMENT);");
        arg0.execSQL("CREATE TABLE IF NOT EXISTS sessions(time TEXT,date TEXT, username TEXT ); ");
        arg0.execSQL("CREATE TABLE IF NOT EXISTS receipts(invoicenumber INTEGER,transactionnumber INTEGER);");
        arg0.execSQL("CREATE TABLE IF NOT EXISTS departments(department TEXT,category TEXT,subcategory TEXT);");
        arg0.execSQL("CREATE TABLE "+ TABLE_NAME_XREPORT + "("+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                + XREPORT_TRANSACTION_NUMBER+" INTEGER NOT NULL,"
                                                + XREPORT_DATE + " INTEGER NOT NULL,"
                                                + XREPORT_TIME + " INTEGER NOT NULL,"
                                                + XREPORT_CASHIER + " INTEGER NOT NULL);");
        arg0.execSQL("CREATE TABLE "+ TABLE_NAME_TRANSACTION+ "("+ _ID+ " INTEGER PRIMARY KEY AUTOINCREMENT," + TRANSACTION_TYPE + " TEXT NOT NULL);");

    }
    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
        arg0.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_ADMIN);
        onCreate(arg0);
        arg0.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_CASHIER);
        onCreate(arg0);
        arg0.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_PRODUCT);
        onCreate(arg0);
        arg0.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_INVOICE);
        onCreate(arg0);
        arg0.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_ITEM);
        onCreate(arg0);
    }
    private static String[] FROM_ADMIN = {USERNAME_ADMIN,PASSWORD_ADMIN};

    public int adminLogin(String usernum,String Pass) {
        String WHERE_ADMIN = "Username = ? and Password = ?";
        String[] WHERE_ARGS_ADMIN = new String[]{usernum,Pass};
        try {
            int i;
            Cursor curse = dbr.query(TABLE_NAME_ADMIN,FROM_ADMIN,WHERE_ADMIN,WHERE_ARGS_ADMIN,null,null,null);
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
    public void addCashier(String Name,String UserNum, String Pass, String Pos){
        cv.clear();
        cv.put(NAME_CASHIER, Name);
        cv.put(NUMBER_CASHIER, UserNum);
        cv.put(POSITION_CASHIER, Pos);
        cv.put(PASSWORD_CASHIER, Pass);
        dbw.insertOrThrow(TABLE_NAME_CASHIER, null, cv);
        
    }
    public void addProduct(String ProdId,String ProdName,String ProdDesc, String ProdPrice, String ProdQuan){
        cv.clear();
        cv.put(ID_PRODUCT, ProdId);
        cv.put(NAME_PRODUCT, ProdName);
        cv.put(DESC_PRODUCT, ProdDesc);
        cv.put(PRICE_PRODUCT, ProdPrice);
        cv.put(QUAN_PRODUCT, ProdQuan);
        dbw.insertOrThrow(TABLE_NAME_PRODUCT, null, cv);
        
    }
    private static String[] FROM_CASH = {PASSWORD_CASHIER};
    public int cashierLogin(String cname, String cpass) {
        String WHERE_CASH = "Cashiernum LIKE ? and Password = ?";
        String[] WHERE_ARGS_CASH = new String[]{cname.toLowerCase().trim(),cpass};
        try {
            Cursor curse = dbr.query(TABLE_NAME_CASHIER,FROM_CASH,WHERE_CASH,WHERE_ARGS_CASH,null,null,null);
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
    public void addAdmin(String mName,String mPass){
        cv.clear();
        cv.put(USERNAME_ADMIN, mName);
        cv.put(PASSWORD_ADMIN, mPass);
        dbw.insertOrThrow(TABLE_NAME_ADMIN, null, cv);
        
    }
    public  void addInvoice( String inCash, String inDisc, String inCustomer, String inDate, String inTime){
        cv.clear();
        cv.put(CASHIER_INVOICE,inCash);
        cv.put(CUSTOMER_DISCOUNT_INVOICE,inDisc);
        cv.put(CUSTOMER_CASH_INVOICE, inCustomer);
        cv.put(DATE_INVOICE,inDate);
        cv.put(TIME_INVOICE,inTime);
        dbw.insertOrThrow(TABLE_NAME_INVOICE, null, cv);
        
    }
    public  void addItem(String itemIn, String itemProd, String itemQuan){
        cv.clear();
        cv.put(INVOICE_NUM_ITEM,itemIn);
        cv.put(PRODUCT_ID_ITEM,itemProd);
        cv.put(PRODUCT_QUANTITY_ITEM,itemQuan);
        dbw.insertOrThrow(TABLE_NAME_ITEM, null, cv);
        
    }
    public List<String> getAllLabels(){
        String[] all = {NUMBER_CASHIER};
        Cursor cursor = dbr.query(TABLE_NAME_CASHIER,all,null,null,null,null,null);
        ArrayList<String> cashnum = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            do {
                cashnum.add(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        
        return cashnum;
    }
    private static String[] ALL = {NUMBER_CASHIER,NAME_CASHIER,POSITION_CASHIER,PASSWORD_CASHIER};
    public String[] selectStaff(String cnum) {
        String WHERE_CASH = "Cashiernum = ?";
        String[] WHERE_ARGS_CASH = new String[]{cnum};
        String[] res_staff= new String[4];
        Cursor cursor = dbr.query(TABLE_NAME_CASHIER,ALL,WHERE_CASH,WHERE_ARGS_CASH,null,null,null);
        cursor.moveToFirst();
        res_staff[0]=cursor.getString(1);
        res_staff[1]=cursor.getString(2);
        res_staff[2]=cursor.getString(4);
        res_staff[3]=cursor.getString(3);
        cursor.close();
        
        return res_staff;
    }
    public void updateStaff(String Name,String UserNum, String Pass, String Pos){
        cv.clear();
        String WHERE_CASH = "Cashiernum = ?";
        String[] WHERE_ARGS_CASH = new String[]{UserNum};
        cv.put(NAME_CASHIER, Name);
        cv.put(POSITION_CASHIER, Pos);
        cv.put(PASSWORD_CASHIER, Pass);
        dbw.update(TABLE_NAME_CASHIER, cv, WHERE_CASH,WHERE_ARGS_CASH);
        
    }
    public void updateProd(String P_id,String P_name,String P_desc, String P_price, String P_quan){
        cv.clear();
        String WHERE_CASH = "ProdId = ?";
        String[] WHERE_ARGS_CASH = new String[]{P_id};
        cv.put(ID_PRODUCT, P_id);
        cv.put(NAME_PRODUCT, P_name);
        cv.put(DESC_PRODUCT, P_desc);
        cv.put(PRICE_PRODUCT, P_price);
        cv.put(QUAN_PRODUCT, P_quan);
        dbw.update(TABLE_NAME_PRODUCT, cv, WHERE_CASH,WHERE_ARGS_CASH);
    }
    public void updateAdmin(String A_name,String A_pass){
        cv.clear();
        String WHERE = "ID = ?";
        String[] WHERE_ARGS = new String[]{"1"};
        cv.put(USERNAME_ADMIN,A_name);
        cv.put(PASSWORD_ADMIN,A_pass);
        dbw.update(TABLE_NAME_ADMIN, cv, WHERE,WHERE_ARGS);
    }
    //For Invoice
    public String[] searchProduct(String id){
        String[] mALL = {ID_PRODUCT,NAME_PRODUCT,DESC_PRODUCT,PRICE_PRODUCT,QUAN_PRODUCT,VATABLE};
        String mWHERE = ID_PRODUCT+" = ?";
        String[] mWHERE_ARGS = new String[]{id};
        String[] itemResult = new String[5];
        Cursor cursor = dbr.query(TABLE_NAME_PRODUCT, mALL, mWHERE, mWHERE_ARGS, null, null, null);
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
}