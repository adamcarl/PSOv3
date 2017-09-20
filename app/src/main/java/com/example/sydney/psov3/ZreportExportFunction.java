package com.example.sydney.psov3;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * Created by PROGRAMMER2 on 8/1/2017.
 */

public class ZreportExportFunction extends AppCompatActivity{
    Context receivedCtx = null;
    ProgressDialog progressDialog = null;
    DB_Data db_data = null;
//    Cursor cursor = null;
//    public static final int requestcode = 1;
    public void showDialogLoading(Context ctx, Cursor cursorZreport, Cursor cursorLog){
        db_data = new DB_Data(ctx);
        receivedCtx = ctx;

        //START
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setMessage("File is Exporting. . .");
        progressDialog.show();

        if(cursorZreport!= null){
            writeToStorage(cursorZreport,"Zreport");
        }
        else if(cursorLog != null){
            writeToStorage(cursorLog,"ProductLog");
        }
    }

    void writeToStorage(Cursor cursor, String fileName){
        //WRITE FILE TO EXTERNAL STORAGE
        try {
//            cursor = db_data.queryDataRead("SELECT * FROM " + TABLE_PRODUCT_TEMP);
            int rowcount = 0;
            int colcount = 0;
            File sdCardDir = Environment.getExternalStorageDirectory();
            String filename = "ExportedFile"+fileName+".txt"; // the name of the file to export with
            File saveFile = new File(sdCardDir, filename);
            FileWriter fw = new FileWriter(saveFile);
            BufferedWriter bw = new BufferedWriter(fw);
            rowcount = cursor.getCount();
            colcount = cursor.getColumnCount();
            try{
                if (rowcount > 0) {
                    cursor.moveToFirst();
                    for (int i = 1; i < colcount; i++) {
                        if (i != colcount - 1) {
                            bw.write(cursor.getColumnName(i) + ",");
                        } else {
                            bw.write(cursor.getColumnName(i));
                        }
                    }
                    bw.newLine();
                    for (int i = 0; i < rowcount; i++) {
                        cursor.moveToPosition(i);
                        for (int j = 1; j < colcount; j++) { //I'VE CHANGED THE VALUE OF VARIABLE int j to 1. (original value 0)
                            if (j != colcount - 1)
                                bw.write(cursor.getString(j) + ",");
                            else
                                bw.write(cursor.getString(j));
                        }
                        bw.newLine();
                        bw.flush();
                    }
                }
                Toast.makeText(receivedCtx, "Successfully Exported CSV File", Toast.LENGTH_SHORT).show();
                delayDialogClose();
            } catch (NullPointerException ex){
                ex.printStackTrace();
                delayDialogClose();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            progressDialog.dismiss();
            delayDialogClose();
        } finally{
            cursor.close();
        }
        //END
        db_data.copyToProductTemp();

    }

    void delayDialogClose(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
            }
        },2000);
        progressDialog.dismiss();
    }
}