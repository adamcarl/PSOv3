package com.example.sydney.psov3;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static com.example.sydney.psov3.Constants.*;

/**
 * Created by PROGRAMMER2 on 6/2/2017.
 */

public class ManageProduct extends AppCompatActivity {
    AlertDialog.Builder builder = null;
    AlertDialog alertDialog = null;
    DB_Data db_data;
    public static final int requestcode = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_product);
        db_data = new DB_Data(this);

        //CREATE DIALOG
        createMyDialog();
        alertDialog = builder.create();
    }
    //MENU//MENU//MENU//MENU//MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_priveleges_main_menu,menu);
        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
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
                //Todo Function Here for saving Transaction
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

                if(mProductName.isEmpty() || mProductId.isEmpty() || mProductDes.isEmpty() || mProductPrice.isEmpty() || mProductQuantity.isEmpty()){
                    Toast.makeText(ManageProduct.this, "Fill all fields!", Toast.LENGTH_SHORT).show();
                }
                else{
                    db_data.addProduct(mProductId,mProductName,mProductDes,mProductPrice,mProductQuantity);
                    Toast.makeText(ManageProduct.this, "Success! Must implement method for adding!", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
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
//    @Override
//    protected void onDestroy() {
//        alertDialog.dismiss();
//        super.onDestroy();
//    }
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
                            String line = "";
                            db.beginTransaction();
                            while ((line = buffer.readLine()) != null) {
                                String[] str = line.split(",", 5);  // defining 3 columns with null or blank field //values acceptance
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
                                Toast.makeText(this, "Successfully Updated Database", Toast.LENGTH_LONG).show();
                            }
                            db.setTransactionSuccessful();
                            db.endTransaction();
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
}