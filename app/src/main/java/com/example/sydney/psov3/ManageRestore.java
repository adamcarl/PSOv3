package com.example.sydney.psov3;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static com.example.sydney.psov3.Constants.columnArray;
import static com.example.sydney.psov3.Constants.tableArray;

public class ManageRestore extends AppCompatActivity {
    public static final int requestcode = 1;

    DB_Data db_data;
    SQLiteDatabase dbReader;

    String tableName;
    String[] tableColumns;

    FloatingActionButton[] fabArray = new FloatingActionButton[18];

    int[] fabInt = {R.id.fabPathTerminal, R.id.fabPathAdmin, R.id.fabPathCashier, R.id.fabPathProduct,
            R.id.fabPathProdTemp, R.id.fabPathInvoice, R.id.fabPathItem, R.id.fabPathProdLogs,
            R.id.fabPathCreditCard, R.id.fabPathXreport, R.id.fabPathZreport, R.id.fabPathTransaction,
            R.id.fabPathLog, R.id.fabPathTotal, R.id.fabPathCash, R.id.fabPathCashTrans,
            R.id.fabPathRetrievedJoin, R.id.fabPathTempInvoice};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_restore);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db_data = new DB_Data(this);
        dbReader = db_data.getReadableDatabase();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                importProduct();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        listeners();

    }

    private void importProduct() {
        Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        fileIntent.setType("gagt/sdf");
        try {
            startActivityForResult(fileIntent, requestcode);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Failed to restore backup. Try again.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;
        switch (requestCode) {
            case requestcode:
                String filepath = data.getData().getPath();
                SQLiteDatabase db = db_data.getWritableDatabase();
                String tableQuery = "SELECT * FROM " + tableName + ";";
                Cursor cursorTable = dbReader.rawQuery(tableQuery, null);
                int cursorColumnCount = cursorTable.getColumnCount();
                cursorTable.close();
                db.execSQL("delete from " + tableName);
                try {
                    if (resultCode == RESULT_OK) {
                        try {
                            FileReader file = new FileReader(filepath);
                            BufferedReader buffer = new BufferedReader(file);
                            ContentValues contentValues = new ContentValues();
                            db.beginTransaction();
                            while (buffer.readLine() != null) {
                                String[] str = buffer.readLine().split(",", cursorColumnCount - 1);  // defining 3 columns with null or blank field //values acceptance
                                for (int counter = 0; counter < cursorColumnCount; counter++) {
                                    contentValues.put(tableColumns[counter], str[counter]);
                                }
                                db.insert(tableName, null, contentValues);
                            }

                            Toast.makeText(this, "Successfully restored database.", Toast.LENGTH_LONG).show();
                            db.setTransactionSuccessful();
                            db.endTransaction();
                            int quer = db_data.getProdTempCount();
                            if (quer <= 0) {
                                db_data.copyToProductTemp();
                            }
                        } catch (IOException e) {
                            if (db.inTransaction())
                                db.endTransaction();
                            Dialog d = new Dialog(this);
                            d.setTitle(e.getMessage() + "first");
                            d.show();
                            // db.endTransaction();
                        }
                    } else {
                        if (db.inTransaction())
                            db.endTransaction();
                        Dialog d = new Dialog(this);
                        d.setTitle("Only CSV files allowed");
                        d.show();
                    }
                } catch (Exception ex) {
                    if (db.inTransaction())
                        db.endTransaction();
                    Dialog d = new Dialog(this);
                    d.setTitle(ex.getMessage() + "second");
                    d.show();
                    // db.endTransaction();
                }
        }
    }

    void listeners() {

        for (int index = 0; index < fabInt.length; index++) {
            fabArray[index] = (FloatingActionButton) findViewById(fabInt[index]);
            final int mIndex = index;
            fabArray[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tableName = tableArray[mIndex];
                    tableColumns = columnArray[mIndex];
                    importProduct();
                    Snackbar.make(view, "Table " + tableName + " has been retored.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }
    }
}
