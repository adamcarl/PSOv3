package com.example.sydney.psov3;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import static com.example.sydney.psov3.Constants.*;

/**
 * Created by PROGRAMMER2 on 6/2/2017.
 */

public class ManageJournal extends AppCompatActivity {

    private TransactionAdapter transAdapter;
    private List<Transactions> transactionList;

    private DB_Data db_data;
    private Cursor cursor;

    private SearchView searchView;
    private Spinner spinner;
    private int spinnerSelected;
    private RecyclerView recyclerView;

    private String colWhere;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_journal);

        db_data = new DB_Data(this);

        spinner = (Spinner) findViewById(R.id.spinnerTransactionSearch);
        spinner.setSelection(0); //initially Product ID


        allOnClickLiteners();
        transactionList = fill_with_data();
//        cursor = db_data.getData();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        transAdapter = new TransactionAdapter(getApplication(),transactionList);

        recyclerView.setLayoutManager(new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(transAdapter);


//        prepareTransactions();
    }

    private void allOnClickLiteners() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerSelected = spinner.getSelectedItemPosition();
                processSelectedSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
    }

    private void processSelectedSpinner() {
        if(spinnerSelected == 0){
            colWhere = COLUMN_INVOICE_TRANSACTION_NUMBER;
            searchTransactions();
        }
        else if(spinnerSelected == 1){
            colWhere = COLUMN_INVOICE_DATETIME;
            searchTransactions();
        }
        if(searchView.getQuery().toString().trim().toLowerCase().equals("")){
            transactionList = fill_with_data();
            transAdapter = new TransactionAdapter(getApplication(),transactionList);
            recyclerView.setLayoutManager(new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(transAdapter);

        }
    }

    private List<Transactions> searchTransactions(){
        transactionList.clear();
        String searchItem = searchView.getQuery().toString().trim().toLowerCase();
        if(searchItem.equals("")){
            fill_with_data();
        }
        else if(!searchItem.equals("")) {
            db_data.getReadableDatabase();
            Cursor c = db_data.searchTransactions(searchItem,colWhere);

            while (c.moveToNext()) {
                int tid = c.getInt(1);
                String tdateTime = c.getString(5);


                transactionList.add(new Transactions(tid,tdateTime));
            }
            transAdapter.notifyDataSetChanged();
            c.close();
        }
        return transactionList;
    }

    private List<Transactions> fill_with_data() {
        transactionList = new ArrayList<>();
        transactionList.clear();

        SQLiteDatabase db = db_data.getReadableDatabase();
        String SELECT_QUERY = "SELECT " + TABLE_INVOICE +"."+ COLUMN_INVOICE_TRANSACTION_NUMBER + "," +TABLE_INVOICE+"."+COLUMN_INVOICE_DATETIME +
                              " FROM " + TABLE_INVOICE +
                              " INNER JOIN " + TABLE_TRANSACTION + " ON " + TABLE_TRANSACTION + "." + _ID + "=" + TABLE_INVOICE + "." + COLUMN_INVOICE_TRANSACTION_NUMBER;

        Cursor cursor = db.rawQuery(SELECT_QUERY,null);
        while (cursor.moveToNext()){
            int invoiceNum = cursor.getInt(0);
            String invoiceDateTime = cursor.getString(1);

            transactionList.add(new Transactions(invoiceNum,invoiceDateTime));
        }
        cursor.close();

        return  transactionList;
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

            SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            searchView = (SearchView) menu.findItem(R.id.productSearch).getActionView();
            searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

            //SEARCH MENU
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    spinnerSelected = spinner.getSelectedItemPosition();

                    if(spinnerSelected == 0 || spinnerSelected == 1 && searchView.getQuery().toString().trim().toLowerCase().equals("")){
                        transactionList = fill_with_data();
                        transAdapter = new TransactionAdapter(getApplication(),transactionList);
                        recyclerView.setLayoutManager(new GridLayoutManager(getApplication(),3,GridLayoutManager.VERTICAL,false));
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(transAdapter);
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    spinnerSelected = spinner.getSelectedItemPosition();
                    processSelectedSpinner();
                    return false;
                }
            });
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
