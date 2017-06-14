package com.example.sydney.psov3;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.provider.BaseColumns._ID;
import static com.example.sydney.psov3.Constants.TABLE_INVOICE;

public class temp extends AppCompatActivity {
    DB_Data db_data;
    SQLiteDatabase dbWriter;
    String currentTime;
    String dateformatted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        db_data = new DB_Data(this);
        dbWriter = db_data.getWritableDatabase();
    }
    public void invoiceBaKamo(View view) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("MM.dd.yyyy", Locale.CHINESE);
        dateformatted = dateformat.format(c.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a",Locale.CHINESE);
        currentTime = sdf.format(new Date());
        try {
            db_data.addInvoice("1", "1", "1000", dateformatted, currentTime);
            db_data.addItem("1", "1", "50");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void cancelBaKamo(View view){

    }
}
