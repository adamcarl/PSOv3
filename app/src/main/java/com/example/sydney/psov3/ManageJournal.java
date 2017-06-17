package com.example.sydney.psov3;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import static com.example.sydney.psov3.Constants.*;

/**
 * Created by PROGRAMMER2 on 6/2/2017.
 */

public class ManageJournal extends AppCompatActivity {

//    private TransactionAdapter adapter;
//    private List<Transactions> transactionsList;

    DB_Data db_data;
    Cursor cursor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_journal);

        db_data = new DB_Data(this);

        List<Transactions> transactionsList = fill_with_data();
//        cursor = db_data.getData();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        TransactionAdapter transAdapter = new TransactionAdapter(getApplication(),transactionsList);

//        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(transAdapter);

//        prepareTransactions();
    }

    private List<Transactions> fill_with_data() {
        List<Transactions> transactions = new ArrayList<>();
//        String[] ALL = {
//                COLUMN_INVOICE_TRANSACTION_NUMBER,
//                COLUMN_INVOICE_DATETIME};
        SQLiteDatabase db = db_data.getReadableDatabase();
//        Cursor cursor = db.query(TABLE_INVOICE, ALL, null, null, null, null, null);
//        String SELECT_QUERY = "SELECT * FROM "+ TABLE_TRANSACTION + "," + TABLE_INVOICE +
//                " WHERE "+TABLE_TRANSACTION+ "." +_ID + "=" +
//                        TABLE_INVOICE + "." + COLUMN_INVOICE_TRANSACTION_NUMBER +
//                " GROUP BY "+ TABLE_TRANSACTION + "." + _ID + " AND " + TABLE_INVOICE + "." + COLUMN_INVOICE_DATETIME;
        String SELECT_QUERY = "SELECT " + COLUMN_INVOICE_TRANSACTION_NUMBER + "," + COLUMN_INVOICE_DATETIME +
                              " FROM " + TABLE_INVOICE +
                              " INNER JOIN " + TABLE_TRANSACTION + " ON " + TABLE_TRANSACTION + "." + _ID + "=" + TABLE_INVOICE + "." + COLUMN_INVOICE_TRANSACTION_NUMBER;
        Cursor cursor = db.rawQuery(SELECT_QUERY,null);
        while (cursor.moveToNext()){
            int invoiceNum = cursor.getInt(1);
            String invoiceDateTime = cursor.getString(5);

            transactions.add(new Transactions(invoiceNum,invoiceDateTime));
        }
        cursor.close();

        return  transactions;
    }

    //MENU//MENU//MENU//MENU//MENU

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_priveleges_main_menu,menu);

        MenuItem importCSV = menu.findItem(R.id.menu_import_product);
        MenuItem addProduct = menu.findItem(R.id.menu_add_product);
        importCSV.setVisible(false);
        addProduct.setVisible(false);

        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.menu_logout:
                Intent intent = new Intent(ManageJournal.this, MainActivity.class);
                startActivity(intent);

                //Todo Function Here for saving Transaction

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
