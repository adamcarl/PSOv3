package com.example.sydney.psov3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by PROGRAMMER2 on 8/7/2017.
 */

public class ModifyAddQuantity  extends AppCompatActivity{
    private EditText etName,etQuantity;
    private Button btnSave, btnCancel;
    private CoordinatorLayout mCl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_addquantity_item);
        init();

        Intent fetchIntent = getIntent();
        final String id = fetchIntent.getExtras().getString("ID");
        final int quantity = fetchIntent.getExtras().getInt("QUANTITY");


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cQuantity = Integer.parseInt(etQuantity.getText().toString().trim().replaceAll("[.]",""));
                DB_Data db_data = new DB_Data(ModifyAddQuantity.this);
                if(!TextUtils.isEmpty(etQuantity.getText().toString())){
                    db_data.updateAddQuantity(id,quantity+cQuantity);
                    Intent intent = new Intent(ModifyAddQuantity.this,ManageProduct.class);
                    Snackbar.make(mCl,"Added Successfully!",Snackbar.LENGTH_SHORT).show();
                    startActivity(intent);
                }
                else{
                    Snackbar.make(mCl,"Fill all fields!",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ModifyAddQuantity.this,ManageProduct.class);
                startActivity(intent);
            }
        });
    }

    private void init() {
        etQuantity = (EditText) findViewById(R.id.etUpdateAddQuantity);
        btnSave = (Button) findViewById(R.id.btnSaveAddQuantity);
        btnCancel = (Button) findViewById(R.id.btnCancelAddQuantity);
        mCl = (CoordinatorLayout) findViewById(R.id.addQuantityCl);
    }
}
