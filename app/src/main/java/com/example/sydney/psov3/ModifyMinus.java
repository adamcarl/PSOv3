package com.example.sydney.psov3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

/**
 * Created by PROGRAMMER2 on 8/7/2017.
 */

class ModifyMinus extends AppCompatActivity {
    private Button btnSave,btnCancel;
    private EditText etValueToMinus,etMinusRemarks;
    private CoordinatorLayout minusCl;
    private RadioButton rbReturnToManufacter,rbTransferOut;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_modify_item_minus);
        init();

        Intent fetchIntent = getIntent();
        final String id = fetchIntent.getExtras().getString("ID");
        final int quantity = fetchIntent.getExtras().getInt("QUANTITY");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int intValueToMinus = Integer.parseInt(etValueToMinus.getText().toString());
                DB_Data db_data = new DB_Data(ModifyMinus.this);

                if(intValueToMinus > 0 && intValueToMinus < quantity ){
                    db_data.minusItemQuantity(id,quantity - intValueToMinus);
                    db_data.addProductLogs(id,logType,null,intValueToMinus,etMinusRemarks.getText().toString(),etMinusOtherRemark.getText().toString());
                    Intent intent = new Intent(ModifyMinus.this,ManageProduct.class);
                    Snackbar.make(minusCl,"Updated Successfully!",Snackbar.LENGTH_SHORT).show();
                    startActivity(intent);
                }
                else{
                    Snackbar.make(minusCl,"Fill all fields!",Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ModifyMinus.this,ManageProduct.class);
                startActivity(intent);
            }
        });

    }

    private void init() {
        etValueToMinus = (EditText) findViewById(R.id.etMinusQuantity);
        etMinusRemarks = (EditText) findViewById(R.id.etMinusRemarks);
        btnSave = (Button) findViewById(R.id.btnSaveMinus);
        btnCancel = (Button) findViewById(R.id.btnCancelMinus);
        minusCl = (CoordinatorLayout) findViewById(R.id.minusQuantityCl);
        rbReturnToManufacter = (RadioButton) findViewById(R.id.rbReturnToManufacturer);
        rbTransferOut = (RadioButton) findViewById(R.id.rbTransferOut);
    }
}