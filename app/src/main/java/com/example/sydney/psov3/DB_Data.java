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

    DB_Data(Context ctx){
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase arg0) {
        // TODO Auto-generated method stub
        arg0.execSQL("CREATE TABLE "+TABLE_NAME_ADMIN+" ("+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+FIRSTNAME_ADMIN+" TEXT NOT NULL, "+LASTNAME_ADMIN+" TEXT NOT NULL, "+PASSWORD_ADMIN+" TEXT NOT NULL );");
        arg0.execSQL("CREATE TABLE "+TABLE_NAME_CASHIER+" ("+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+NUMBER_CASHIER+" INTEGER NOT NULL, "+FIRSTNAME_CASHIER+" TEXT NOT NULL, "+LASTNAME_CASHIER+" TEXT NOT NULL, "+POSITION_CASHIER+" TEXT NOT NULL,"+PASSWORD_CASHIER+" TEXT NOT NULL );");
        arg0.execSQL("CREATE TABLE "+TABLE_NAME_PRODUCT+" ("+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+ID_PRODUCT+" INTEGER NOT NULL, "+NAME_PRODUCT+" TEXT NOT NULL, "+DESC_PRODUCT+" TEXT NOT NULL, "+PRICE_PRODUCT+" DOUBLE NOT NULL,"+QUAN_PRODUCT+" INTEGER NOT NULL ,"+VATABLE+" INTEGER NOT NULL );");
        arg0.execSQL("CREATE TABLE "+TABLE_NAME_ORDER+" ("+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, itemname TEXT, itemprice REAL,itemcode INTEGER,orderquantity INTEGER,consecutivenumber INTEGER,invoicenumber INTEGER,transactionnumber INTEGER DEFAULT 1);");
        arg0.execSQL("CREATE TABLE IF NOT EXISTS cashierlog(date TEXT, time TEXT,firstname TEXT,lastname TEXT,username TEXT,transactionnumber INTEGER PRIMARY KEY AUTOINCREMENT);");
        arg0.execSQL("CREATE TABLE IF NOT EXISTS sessions(time TEXT,date TEXT, username TEXT ); ");
        arg0.execSQL("CREATE TABLE IF NOT EXISTS receipts(invoicenumber INTEGER,transactionnumber INTEGER);");
        arg0.execSQL("CREATE TABLE IF NOT EXISTS departments(department TEXT,category TEXT,subcategory TEXT);");
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
        arg0.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_ORDER);
        onCreate(arg0);
    }
    private static String[] FROM_ADMIN = {PASSWORD_ADMIN};

    public int adminLogin(String vpass) {
        SQLiteDatabase db = this.getReadableDatabase();
        String WHERE_ADMIN = "Password = ?";
        String[] WHERE_ARGS_ADMIN = new String[]{vpass};
        try {
            int i;
            Cursor curse = db.query(TABLE_NAME_ADMIN,FROM_ADMIN,WHERE_ADMIN,WHERE_ARGS_ADMIN,null,null,null);
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
    public void addCashier(String LName,String FName,String EmpId, String pass, String Pos){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FIRSTNAME_CASHIER, FName);
        cv.put(LASTNAME_CASHIER, LName);
        cv.put(NUMBER_CASHIER, EmpId);
        cv.put(POSITION_CASHIER, Pos);
        cv.put(PASSWORD_CASHIER, pass);
        db.insertOrThrow(TABLE_NAME_CASHIER, null, cv);
    }
    public void addProduct(String ProdId,String ProdName,String ProdDesc, String ProdPrice, String ProdQuan){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ID_PRODUCT, ProdId);
        cv.put(NAME_PRODUCT, ProdName);
        cv.put(DESC_PRODUCT, ProdDesc);
        cv.put(PRICE_PRODUCT, ProdPrice);
        cv.put(QUAN_PRODUCT, ProdQuan);
        db.insertOrThrow(TABLE_NAME_PRODUCT, null, cv);
    }
    private static String[] FROM_CASH = {PASSWORD_CASHIER};
    public int cashierLogin(String cname, String cpass) {
        SQLiteDatabase db = this.getReadableDatabase();
        String WHERE_CASH = "Cashiernum LIKE ? and Password = ?";
        String[] WHERE_ARGS_CASH = new String[]{cname.toLowerCase().trim(),cpass};
        try {
            Cursor curse = db.query(TABLE_NAME_CASHIER,FROM_CASH,WHERE_CASH,WHERE_ARGS_CASH,null,null,null);
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
        public void addStudent(String name1,String name2,String pass){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FIRSTNAME_ADMIN, name1);
        cv.put(LASTNAME_ADMIN, name2);
        cv.put(PASSWORD_ADMIN, pass);
        db.insertOrThrow(TABLE_NAME_ADMIN, null, cv);
    }
    public List<String> getAllLabels(){
        String[] all = {NUMBER_CASHIER};
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_CASHIER,all,null,null,null,null,null);
        ArrayList<String> cashnum = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            do {
                cashnum.add(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return cashnum;
    }
    private static String[] ALL = {NUMBER_CASHIER,LASTNAME_CASHIER,FIRSTNAME_CASHIER,POSITION_CASHIER,PASSWORD_CASHIER};
    public String[] selectStaff(String cnum) {
        SQLiteDatabase db = this.getReadableDatabase();
        String WHERE_CASH = "Cashiernum = ?";
        String[] WHERE_ARGS_CASH = new String[]{cnum};
        String[] res_staff= new String[4];
        Cursor cursor = db.query(TABLE_NAME_CASHIER,ALL,WHERE_CASH,WHERE_ARGS_CASH,null,null,null);
        cursor.moveToFirst();
        res_staff[0]=cursor.getString(1);
        res_staff[1]=cursor.getString(2);
        res_staff[2]=cursor.getString(4);
        res_staff[3]=cursor.getString(3);
        cursor.close();
        return res_staff;
    }
    public void updateStaff(String LName,String FName,String EmpId, String pass, String Pos){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String WHERE_CASH = "Cashiernum = ?";
        String[] WHERE_ARGS_CASH = new String[]{EmpId};
        cv.put(FIRSTNAME_CASHIER, FName);
        cv.put(LASTNAME_CASHIER, LName);
        cv.put(POSITION_CASHIER, Pos);
        cv.put(PASSWORD_CASHIER, pass);
        db.update(TABLE_NAME_CASHIER, cv, WHERE_CASH,WHERE_ARGS_CASH);
    }
    public void updateProd(String P_id,String P_name,String P_desc, String P_price, String P_quan){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String WHERE_CASH = "ProdId = ?";
        String[] WHERE_ARGS_CASH = new String[]{P_id};
        cv.put(ID_PRODUCT, P_id);
        cv.put(NAME_PRODUCT, P_name);
        cv.put(DESC_PRODUCT, P_desc);
        cv.put(PRICE_PRODUCT, P_price);
        cv.put(QUAN_PRODUCT, P_quan);
        db.update(TABLE_NAME_PRODUCT, cv, WHERE_CASH,WHERE_ARGS_CASH);
    }
}