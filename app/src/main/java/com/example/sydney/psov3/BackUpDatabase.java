package com.example.sydney.psov3;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.example.sydney.psov3.Constants.TABLE_ADMIN;
import static com.example.sydney.psov3.Constants.TABLE_CASH;
import static com.example.sydney.psov3.Constants.TABLE_CASHIER;
import static com.example.sydney.psov3.Constants.TABLE_CASHTRANS;
import static com.example.sydney.psov3.Constants.TABLE_CREDIT_CARD;
import static com.example.sydney.psov3.Constants.TABLE_INVOICE;
import static com.example.sydney.psov3.Constants.TABLE_ITEM;
import static com.example.sydney.psov3.Constants.TABLE_LOG;
import static com.example.sydney.psov3.Constants.TABLE_PRODUCT;
import static com.example.sydney.psov3.Constants.TABLE_PRODUCTLOGS;
import static com.example.sydney.psov3.Constants.TABLE_PRODUCT_TEMP;
import static com.example.sydney.psov3.Constants.TABLE_RETRIEVED_JOINTABLE;
import static com.example.sydney.psov3.Constants.TABLE_TEMP_INVOICING;
import static com.example.sydney.psov3.Constants.TABLE_TERMINAL;
import static com.example.sydney.psov3.Constants.TABLE_TOTAL;
import static com.example.sydney.psov3.Constants.TABLE_TRANSACTION;
import static com.example.sydney.psov3.Constants.TABLE_XREPORT;
import static com.example.sydney.psov3.Constants.TABLE_ZREPORT;

/**
 * Created by Marky on 2/3/2018.
 * ABZTRAK INC.
 */

class BackUpDatabase {
    private DB_Data db_data;
    private File exportDir = new File(Environment.getExternalStorageDirectory() + "/Backup", "");

    void main(Context ctx) {

        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        SQLiteDatabase dbReader = db_data.getReadableDatabase();

        String[] tables = {TABLE_TERMINAL, TABLE_ADMIN, TABLE_CASHIER, TABLE_PRODUCT, TABLE_PRODUCT_TEMP, TABLE_INVOICE, TABLE_ITEM, TABLE_PRODUCTLOGS, TABLE_CREDIT_CARD, TABLE_XREPORT, TABLE_ZREPORT, TABLE_TRANSACTION, TABLE_LOG, TABLE_TEMP_INVOICING, TABLE_TOTAL, TABLE_RETRIEVED_JOINTABLE, TABLE_CASH, TABLE_CASHTRANS};

        for (String table : tables) {
            String tableQuery = "SELECT * FROM " + table + ";";
            Cursor cursorTable = dbReader.rawQuery(tableQuery, null);
            exportBaKamo(table, cursorTable);
        }
        Toast.makeText(ctx, "Back up successful.", Toast.LENGTH_SHORT).show();
    }

    private void exportBaKamo(String tableName, Cursor curSV) {

        File file = new File(exportDir, tableName + "_" + getCurrentDay() + ".csv");
        try {
            if (file.createNewFile()) {
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                int cursorColumnCount = curSV.getColumnCount();
                curSV.moveToFirst();
                while (!curSV.isAfterLast()) {
                    ArrayList<String> listRows = new ArrayList<>();
                    for (int i = 1; i < cursorColumnCount; i++) {
                        listRows.add(curSV.getString(i));
                    }
                    String[] arrStr = listRows.toArray(new String[listRows.size()]);
                    csvWrite.writeNext(arrStr);
                    listRows.clear();
                    curSV.moveToNext();
                }
                csvWrite.close();
                curSV.close();
            }

        } catch (Exception sqlEx) {
            Log.e("BackUpDatabase", sqlEx.getMessage(), sqlEx);
        }
    }

    void setDb_data(DB_Data db_data) {
        this.db_data = db_data;
    }

    private String getCurrentDay() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yy", Locale.ENGLISH);
        return dateFormat.format(c.getTime());
    }
}
