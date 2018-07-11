package com.example.sydney.psov3.POJO;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Environment;
import android.widget.EditText;

import com.jolimark.UsbPrinter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by PROGRAMMER2 on 4/26/2018.
 * ABZTRAK INC.
 */

public class FunctionCall {
    private static FunctionCall singleton = null;
    DecimalFormat moneyDecimal = new DecimalFormat("0.00");

    private FunctionCall() {
    }

    public static FunctionCall getinstance() {
        if (singleton == null) {
            singleton = new FunctionCall();
        }
        return singleton;
    }

    public static boolean unLockCashBox() {
        boolean retnValue = false;
        UsbPrinter tmpUsbDev = new UsbPrinter();
        retnValue = tmpUsbDev.UnLockOfCashBox();

        return retnValue;
    }

    public String getYesterDay() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-dd-yyyy", Locale.ENGLISH);
        return dateFormat.format(c.getTime());
    }

    public String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MMM-dd-yyyy HH:mm", Locale.ENGLISH);
        return dateTimeFormat.format(c.getTime());
    }

    private String getCurrentDay() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yy", Locale.ENGLISH);
        return dateFormat.format(c.getTime());
    }

    public void createReceiptImage(String orNum, String[] orCopyHead, String[] orCopy) {
        File mSdCardDir = new File(Environment.getExternalStorageDirectory() + "/Receipt");
        if (!mSdCardDir.exists()) {
            mSdCardDir.mkdirs();
        }
        String mFilename = orNum + "_" + getCurrentDay() + ".jpg";
        File mSaveFile = new File(mSdCardDir, mFilename);

        try {
            FileOutputStream out = new FileOutputStream(mSaveFile);

            // NEWLY ADDED CODE STARTS HERE [
            Bitmap mBitmap = Bitmap.createBitmap(480, orCopyHead.length * 35 + orCopy.length * 35, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(mBitmap);

            Paint paint = new Paint();
            paint.setColor(Color.BLACK); // Text Color
            paint.setStrokeWidth(30); // Text Size
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)); // Text Overlapping Pattern
            // some more settings...

            canvas.drawColor(0xFFFFFFFF);
            for (int a = 1; a <= orCopyHead.length; a++) {
                canvas.drawText(orCopyHead[a - 1], 10, a * 35, paint);
            }
            for (int a = 1 + orCopyHead.length; a <= orCopy.length + orCopyHead.length; a++) {
                canvas.drawText(orCopy[(a - orCopyHead.length) - 1], 10, a * 35, paint);
            }
            // NEWLY ADDED CODE ENDS HERE ]

            mBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeJournalTrail(String journalEntry) {
        //WRITE JOURNAL ENTRY TO EXTERNAL STORAGE
        try {
            File sdCardDir = new File(Environment.getExternalStorageDirectory() + "/Journals");

            if (!sdCardDir.exists()) {
                sdCardDir.mkdirs();
            }

            String filename = getCurrentDay() + ".jnl"; // the name of the file to export with
            File saveFile = new File(sdCardDir, filename);
            FileWriter fw = new FileWriter(saveFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            try {
                bw.append(journalEntry);
                bw.newLine();
                bw.flush();
                bw.close();
//                    Toast.makeText(receivedCtx, "Successfully Exported CSV File", Toast.LENGTH_SHORT).show();
//                    delayDialogClose();
            } catch (NullPointerException ex) {
                ex.printStackTrace();
//                    delayDialogClose();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
//                progressDialog.dismiss();
//                delayDialogClose();
        }
        //END WRITE JOURNAL ENTRY TO EXTERNAL STORAGE
    }

    public double getTxtCashDouble(EditText cash) {
        String mCash = cash.getText().toString().replaceAll("[P,$]", "");
        if (mCash == null || mCash.equals("")) return 0.0;
        else return Double.parseDouble(mCash);
    }

    public String removeLast(String str) {
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
}
