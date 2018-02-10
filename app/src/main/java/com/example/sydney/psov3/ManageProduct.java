package com.example.sydney.psov3;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.sydney.psov3.Constants.COLUMN_PRODUCT_DESCRIPTION;
import static com.example.sydney.psov3.Constants.COLUMN_PRODUCT_ID;
import static com.example.sydney.psov3.Constants.COLUMN_PRODUCT_NAME;
import static com.example.sydney.psov3.Constants.COLUMN_PRODUCT_PRICE;
import static com.example.sydney.psov3.Constants.COLUMN_PRODUCT_QUANTITY;
import static com.example.sydney.psov3.Constants.TABLE_PRODUCT;
import static com.example.sydney.psov3.Constants.TABLE_PRODUCTLOGS;

/**
 * Created by Marky on 6/2/2017.
 */

public class ManageProduct extends AppCompatActivity  implements ProductAdapter.OnRecyclerItemClickListener{
    public static final int requestcode = 1;
    AlertDialog.Builder builder = null;
    AlertDialog alertDialog = null;
    DB_Data db_data;
    ProductAdapter productAdapter = null;
    Spinner spinner;
    String colWhere;
    List<Product> productsList;
    int spinnerSelected;
    RecyclerView recyclerView;
    SearchView searchView;
    SQLiteDatabase dbReader;
    private ZreportExportFunction zreportExportFunction = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_product);
        db_data = new DB_Data(this);

        zreportExportFunction = new ZreportExportFunction();
        dbReader = db_data.getReadableDatabase();

        spinner = (Spinner) findViewById(R.id.spinnerProductSearch);
        spinner.setSelection(0); //initially Product ID

        //INITIALIZE DATA SET
        productsList = listGo();
        productAdapter = new ProductAdapter(getApplication(),productsList,this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1,GridLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(productAdapter);//recyclerView.setItemAnimator(new DefaultItemAnimator());

        //CREATE DIALOG
        createMyDialog();
        alertDialog = builder.create();

        //ALL ONCLICKLISTENERS
        allOnListeners();
    }

    private void allOnListeners() {
        //SEARCHVIEW LISTENER
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerSelected = spinner.getSelectedItemPosition();
                processSelectedSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void processSelectedSpinner() {
        if(spinnerSelected == 0){
            colWhere = COLUMN_PRODUCT_ID;
            searchProd();
        }
        else if(spinnerSelected == 1){
            colWhere = COLUMN_PRODUCT_NAME;
            searchProd();
        }
        else if(spinnerSelected == 2){
            colWhere = COLUMN_PRODUCT_DESCRIPTION;
            searchProd();
        }
        else if(spinnerSelected == 4){
            colWhere = COLUMN_PRODUCT_PRICE;
            searchProd();
        }
        else if(spinnerSelected == 3){
            colWhere = COLUMN_PRODUCT_QUANTITY;
            searchProd();
        }
        if(searchView.getQuery().toString().trim().toLowerCase().equals("")){
            productsList = listGo();
            productAdapter = new ProductAdapter(getApplication(),productsList,ManageProduct.this);
            recyclerView.setLayoutManager(new GridLayoutManager(this,1,GridLayoutManager.VERTICAL,false));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(productAdapter);

        }
    }

    private void importProduct() {
        Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        fileIntent.setType("gagt/sdf");
        try {
            startActivityForResult(fileIntent, requestcode);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Failed to import", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private List<Product> searchProd(){
        productsList.clear();
        String searchItem = searchView.getQuery().toString();
        if(searchItem.equals("")){
            listGo();
        }
        else if(!searchItem.equals("")) {
            db_data.getReadableDatabase();
            Cursor c = db_data.searchProductBaKamo(searchItem,colWhere);

            while (c.moveToNext()) {
                String pid = c.getString(1);
                String pname = c.getString(2);
                String pdesc = c.getString(3);
                double pprice = c.getDouble(4);
                int pdquan = c.getInt(5);

                productsList.add(new Product(pid, pname, pdesc, pprice, pdquan));
            }
            productAdapter.notifyDataSetChanged();
            c.close();
        }
        return productsList;
    }


    public List<Product> listGo(){
        productsList = new ArrayList<>();
        productsList.clear();
        String[] ALL = {
                COLUMN_PRODUCT_ID,
                COLUMN_PRODUCT_NAME,
                COLUMN_PRODUCT_DESCRIPTION,
                COLUMN_PRODUCT_PRICE,
                COLUMN_PRODUCT_QUANTITY};
        SQLiteDatabase db = db_data.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCT, ALL, null, null, null, null, null);
        while (cursor.moveToNext()){
            String pid = cursor.getString(0);
            String pname = cursor.getString(1);
            String pdesc = cursor.getString(2);
            double pprice = cursor.getDouble(3);
            int pdquan = cursor.getInt(4);

            productsList.add(new Product(pid,pname,pdesc,pprice,pdquan));
        }
        cursor.close();

        return productsList;
    }

    //MENU//MENU//MENU//MENU//MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_priveleges_main_menu,menu);
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

                    if(spinnerSelected == 0 || spinnerSelected == 1 || spinnerSelected == 2 || spinnerSelected == 3 || spinnerSelected == 4 && searchView.getQuery().toString().trim().toLowerCase().equals("")){
                        productsList = listGo();
                        productAdapter = new ProductAdapter(getApplication(),productsList,ManageProduct.this);
                        recyclerView.setLayoutManager(new GridLayoutManager(getApplication(),1,GridLayoutManager.VERTICAL,false));
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(productAdapter);
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
        switch (id) {
            case R.id.menu_logout:
                Intent intent = new Intent(ManageProduct.this, MainActivity.class);
                startActivity(intent);

            case R.id.menu_add_product:
                alertDialog.show();
                return true;

            case R.id.menu_import_product:
                //IMPORTING PRODUCT
                importProduct();

            case R.id.menu_show_log:
                //PRINT PRODUCT LOGS
                printProductLog();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void printProductLog() {
        Cursor retrievedCursorFromProductLog;
        Cursor cursorDummy = null;
        String logProductQuery = "SELECT * FROM " + TABLE_PRODUCTLOGS;
        retrievedCursorFromProductLog = dbReader.rawQuery(logProductQuery,null);

        zreportExportFunction.showDialogLoading(ManageProduct.this, null, retrievedCursorFromProductLog);
    }

    private void createMyDialog(){
        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_addproduct, null);
        builder.setView(alertLayout);
        final EditText productName =(EditText)alertLayout.findViewById(R.id.etProductName);
        final EditText productId =(EditText) alertLayout.findViewById(R.id.etProductId);
        final EditText productDes =(EditText) alertLayout.findViewById(R.id.etProductDes);
        final EditText productQuantity =(EditText) alertLayout.findViewById(R.id.etProductQuantity);
        final EditText productPrice =(EditText) alertLayout.findViewById(R.id.etProductPrice);
        final Button btnSave = (Button) alertLayout.findViewById(R.id.btnSaveAddProduct);
        final Button btnCancel = (Button) alertLayout.findViewById(R.id.btnCancelAddProduct);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mProductName = productName.getText().toString();
                String mProductPrice = productPrice.getText().toString();
                String mProductDes = productDes.getText().toString();
                String mProductQuantity = productQuantity.getText().toString();
                String mProductId = productId.getText().toString();
                int result = db_data.searchDuplicateProduct(mProductId);

                if(mProductName.isEmpty() || mProductId.isEmpty() || mProductDes.isEmpty() || mProductPrice.isEmpty() || mProductQuantity.isEmpty()){
                    Toast.makeText(ManageProduct.this, "Fill all fields!", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(result > 0){
                        productId.setText("");
                        Toast.makeText(ManageProduct.this, "Duplicate PRODUCT ID!", Toast.LENGTH_SHORT).show();
                    } else {
                        db_data.addProduct(mProductId,mProductName,mProductDes,mProductPrice,mProductQuantity);
                        Toast.makeText(ManageProduct.this, "Added Successfully!", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;
        switch (requestCode) {
            case requestcode:
                String filepath = data.getData().getPath();
                SQLiteDatabase db = db_data.getWritableDatabase();
                String tableName = TABLE_PRODUCT;
                db.execSQL("delete from " + tableName);
                try {
                    if (resultCode == RESULT_OK) {
                        try {
                            FileReader file = new FileReader(filepath);
                            BufferedReader buffer = new BufferedReader(file);
                            ContentValues contentValues = new ContentValues();
                            db.beginTransaction();
                            while (buffer.readLine() != null) {
                                String[] str = buffer.readLine().split(",", 5);  // defining 3 columns with null or blank field //values acceptance
                                //Id, Company,Name,Price
                                String pId = str[0];
                                String pName = str[1];
                                String pDesc = str[2];
                                String pPrice = str[3];
                                String pQuan = str[4];
                                contentValues.put(COLUMN_PRODUCT_ID, pId);
                                contentValues.put(COLUMN_PRODUCT_NAME, pName);
                                contentValues.put(COLUMN_PRODUCT_DESCRIPTION, pDesc);
                                contentValues.put(COLUMN_PRODUCT_PRICE, pPrice);
                                contentValues.put(COLUMN_PRODUCT_QUANTITY, pQuan);
                                db.insert(tableName, null, contentValues);
                            }
                            //INITIALIZE DATA SET
                            productsList = listGo();
                            productAdapter = new ProductAdapter(getApplication(),productsList,ManageProduct.this);
                            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                            recyclerView.setLayoutManager(new GridLayoutManager(this,1,GridLayoutManager.VERTICAL,false));
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(productAdapter);//recyclerView.setItemAnimator(new DefaultItemAnimator());

                            Toast.makeText(this, "Successfully Updated Database", Toast.LENGTH_LONG).show();
                            db.setTransactionSuccessful();
                            db.endTransaction();
                            int quer = db_data.getProdTempCount();
                            if (quer<=0){
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
        listGo();
    }

    @Override
    public void onRecyclerItemClick(View view, final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_edit_products, null);
        alertDialogBuilder.setView(alertLayout);

        Button btnAddQuan = (Button) alertLayout.findViewById(R.id.btnAddQuan);
        Button btnEdit = (Button) alertLayout.findViewById(R.id.btnEdit);
        Button btnMinus = (Button) alertLayout.findViewById(R.id.btnMinus);

        final AlertDialog alertDialogModify = alertDialogBuilder.create();
        alertDialogModify.show();

        //ONLY QUANTITY CAN BE EDITED!
        btnAddQuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageProduct.this,ModifyAddQuantity.class);
                intent.putExtra("POS",position);
                intent.putExtra("ID",productsList.get(position).getP_id());
                intent.putExtra("NAME",productsList.get(position).getP_name());
                intent.putExtra("DESCRIPTION",productsList.get(position).getP_desc());
                intent.putExtra("PRICE",productsList.get(position).getP_price());
                intent.putExtra("QUANTITY",productsList.get(position).getP_quan());
                startActivity(intent);
                alertDialogModify.dismiss();
            }
        });

        //ONLY NAME,DESCRIPTION,PRICE CAN BE EDITED!
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageProduct.this,ModifyEdit.class);
                intent.putExtra("ID",productsList.get(position).getP_id());
                intent.putExtra("NAME",productsList.get(position).getP_name());
                intent.putExtra("DESCRIPTION",productsList.get(position).getP_desc());
                intent.putExtra("PRICE",productsList.get(position).getP_price());
                intent.putExtra("QUANTITY",productsList.get(position).getP_quan());
                startActivity(intent);
                alertDialogModify.dismiss();
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageProduct.this,ModifyMinus.class);
                intent.putExtra("ID",productsList.get(position).getP_id());
                intent.putExtra("NAME",productsList.get(position).getP_name());
                intent.putExtra("DESCRIPTION",productsList.get(position).getP_desc());
                intent.putExtra("PRICE",productsList.get(position).getP_price());
                intent.putExtra("QUANTITY",productsList.get(position).getP_quan());
                startActivity(intent);
                alertDialogModify.dismiss();
            }
        });
    }
}