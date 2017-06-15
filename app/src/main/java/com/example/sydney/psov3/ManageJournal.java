package com.example.sydney.psov3;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
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
        String[] ALL = {
                COLUMN_PRODUCT_ID,
                COLUMN_PRODUCT_NAME,
                COLUMN_PRODUCT_DESCRIPTION,
                COLUMN_PRODUCT_PRICE,
                COLUMN_PRODUCT_QUANTITY};
        SQLiteDatabase db = db_data.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCT, ALL, null, null, null, null, null);
        while (cursor.moveToNext()){
            String pid = cursor.getInt(0) + "";
            String pname = cursor.getString(1);
            String pdesc = cursor.getString(2);
            double pprice = cursor.getDouble(3);
            int pdquan = cursor.getInt(4);

            transactions.add(new Transactions());
        }
        cursor.close();

//        transactions.add(new Transactions(1,"VOID","6-10-2017 11:01AM"));
//        transactions.add(new Transactions(2,"INVOICE","6-10-2017 11:10AM"));
//        transactions.add(new Transactions(3,"CANCEL","6-10-2017 1:30PM"));
//        transactions.add(new Transactions(4,"VOID","6-10-2017 11:01AM"));
//        transactions.add(new Transactions(5,"INVOICE","6-10-2017 11:10AM"));
//        transactions.add(new Transactions(6,"CANCEL","6-10-2017 1:30PM"));
//        transactions.add(new Transactions(7,"VOID","6-10-2017 11:01AM"));
//        transactions.add(new Transactions(8,"INVOICE","6-10-2017 11:10AM"));
//        transactions.add(new Transactions(10,"CANCEL","6-10-2017 1:30PM"));
//        transactions.add(new Transactions(11,"VOID","6-10-2017 11:01AM"));
//        transactions.add(new Transactions(12,"INVOICE","6-10-2017 11:10AM"));
//        transactions.add(new Transactions(13,"CANCEL","6-10-2017 1:30PM"));

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
