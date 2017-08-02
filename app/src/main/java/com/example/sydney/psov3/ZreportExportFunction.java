package com.example.sydney.psov3;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static com.example.sydney.psov3.Constants.TABLE_PRODUCT_TEMP;

/**
 * Created by PROGRAMMER2 on 8/1/2017.
 */

public class ZreportExportFunction extends AppCompatActivity{
    Context receivedCtx = null;
    ProgressDialog progressDialog = null;
//    Cursor cursor = null;
//    public static final int requestcode = 1;

    boolean showDialogLoading(Context ctx, Cursor cursor){
        DB_Data db_data = new DB_Data(ctx);
        receivedCtx = ctx;
        boolean isSent = false;

        //START
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setMessage("File is Exporting. . .");
        progressDialog.show();

        //WRITE FILE TO EXTERNAL STORAGE
        try {
//            cursor = db_data.queryDataRead("SELECT * FROM " + TABLE_PRODUCT_TEMP);
            int rowcount = 0;
            int colcount = 0;
            File sdCardDir = Environment.getExternalStorageDirectory();
            String filename = "ExportedFile.txt"; // the name of the file to export with
            File saveFile = new File(sdCardDir, filename);
            FileWriter fw = new FileWriter(saveFile);
            BufferedWriter bw = new BufferedWriter(fw);
            rowcount = cursor.getCount();
            colcount = cursor.getColumnCount();
            try{
                for (int i = 0; i < rowcount; i++) {
                    cursor.moveToPosition(i);
                    for (int j = 0; j < colcount; j++) { //I'VE CHANGED THE VALUE OF VARIABLE int j to 1. (original value 0)
                        if (j != colcount - 1)
                            bw.write(cursor.getString(j) + "\t");
                        else
                            bw.write(cursor.getString(j));
                    }
                    bw.newLine();
                    bw.flush();
                }
                isSent = true;
                Log.e("Mark's Filter", "OCCURED sql TRY!");
            } catch (NullPointerException ex){
                ex.printStackTrace();
                Log.e("Mark's Filter", "OCCURED SQLException!");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(ctx, "Failed to Export file!", Toast.LENGTH_SHORT).show();
            isSent = false;
        }
        //END
        return isSent;
    }

//    void closeDialog(){
//        progressDialog.dismiss();
//        Toast.makeText(receivedCtx, "Successfully Exported CSV File", Toast.LENGTH_SHORT).show();
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (data == null){
//            return;
//        }
//        switch (requestCode) {
//            case requestcode:
//                String filepath = data.getData().getPath();
//                db_data = new DB_Data(this);
//                SQLiteDatabase db = db_data.getWritableDatabase();
//                String tableName = TABLE_PRODUCT_TEMP;
//                db.execSQL("delete from " + tableName);
//                try {
//                    if (resultCode == RESULT_OK) {
//                        try {
//                            FileReader file = new FileReader(filepath);
//                            BufferedReader buffer = new BufferedReader(file);
//                            ContentValues contentValues = new ContentValues();
//                            String line = "";
//                            db.beginTransaction();
//                            while ((line = buffer.readLine()) != null) {
//                                String[] str = line.split("\t",6);  // defining 3 columns with null or blank field //values acceptance
//                                //id, barcode,description,quantity
//                                String barcode = str[0].toString();
//                                String description = str[1].toString();
//                                String quantity = str[2].toString();
//
//                                contentValues.put("barcode", barcode);
//                                contentValues.put("description", description);
//                                contentValues.put("quantity", quantity);
//                                db.insert(tableName, null, contentValues);
//                            }
//
////                            String COLUMN_PRODUCT_ID_TEMP = "ProdId_temp";
////                            String COLUMN_PRODUCT_NAME_TEMP = "ProdName_temp";
////                            String COLUMN_PRODUCT_DESCRIPTION_TEMP = "ProdDesc_temp";
////                            String COLUMN_PRODUCT_PRICE_TEMP = "ProdPrice_temp";
////                            String COLUMN_PRODUCT_QUANTITY_TEMP = "ProdQuan_temp";
////                            String COLUMN_PRODUCT_VATABLE_TEMP = "Vatable_temp";
////                            String COLUMN_PRODUCT_IMEI_TEMP = "ProdIMEI_temp";
//
//                            db.setTransactionSuccessful();
//                            cursor = db.rawQuery("select * from "+ TABLE_PRODUCT_TEMP, null);
//                            db.endTransaction();
//
//
//                        } catch (SQLException e) {
//                            Log.e("Error",e.getMessage().toString());
//
//                        } catch (IOException e) {
//                            if (db.inTransaction()){
//                                db.endTransaction();
//                                Toast.makeText(this, "Failed to Export file!", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    } else {
//                        if (db.inTransaction()){
//                            db.endTransaction();
//                            Toast.makeText(this, "Failed to Export file!", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                } catch (Exception ex) {
//                    if (db.inTransaction()) {
//                        db.endTransaction();
//                        Toast.makeText(this, "Failed to Export file!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//        }
//    }

}
