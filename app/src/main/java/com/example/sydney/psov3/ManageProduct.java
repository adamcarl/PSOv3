package com.example.sydney.psov3;

import android.content.Intent;
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

/**
 * Created by PROGRAMMER2 on 6/2/2017.
 */

public class ManageProduct extends AppCompatActivity {

    AlertDialog.Builder builder = null;
    AlertDialog alertDialog = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_product);

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

        switch (id){
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

        final EditText productName =(EditText) findViewById(R.id.etProductName);
        final EditText productId =(EditText) findViewById(R.id.etProductId);
        final EditText productPrice =(EditText) findViewById(R.id.etProductPrice);
        final EditText productQuantity =(EditText) findViewById(R.id.etProductQuantity);
        final Button btnSave = (Button) findViewById(R.id.btnSaveAddProduct);
        final Button btnCancel = (Button) findViewById(R.id.btnCancelAddProduct);

//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String mProductName = productName.getText().toString();
//                String mProductPrice = productPrice.getText().toString();
//                String mProductQuantity = productQuantity.getText().toString();
//                String mProductId = productId.getText().toString();
//
//                if(mProductName.isEmpty() || mProductId.isEmpty() || mProductPrice.isEmpty() || mProductQuantity.isEmpty()){
//                    Toast.makeText(ManageProduct.this, "Fill all fields!", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    Toast.makeText(ManageProduct.this, "Success! Must implement method for adding!", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
//
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                alertDialog.dismiss();
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        alertDialog.dismiss();
        super.onDestroy();
    }
}
