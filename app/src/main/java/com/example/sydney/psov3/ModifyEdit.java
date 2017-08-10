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

/**
 * Created by PROGRAMMER2 on 8/7/2017.
 */

public class ModifyEdit extends AppCompatActivity{
    private Button btnSave,btnCancel;
    private EditText etID,etName,etDescription,etPrice,etQuantity;
    private CoordinatorLayout modifyCl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_modify_item);
        init();

        Intent fetchIntent = getIntent();
        final String id = fetchIntent.getExtras().getString("ID");
        final String name = fetchIntent.getExtras().getString("NAME");
        final String description = fetchIntent.getExtras().getString("DESCRIPTION");
        final double price = fetchIntent.getExtras().getDouble("PRICE");
        final int quantity = fetchIntent.getExtras().getInt("QUANTITY");

        etID.setText(id);
        etName.setText(name);
        etDescription.setText(description);
        etPrice.setText(Double.toString(price));
        etQuantity.setText(Integer.toString(quantity));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strName = etName.getText().toString();
                String strDescription = etDescription.getText().toString();
                Double doublePrice = Double.parseDouble(etPrice.getText().toString());
                DB_Data db_data = new DB_Data(ModifyEdit.this);
                if(!TextUtils.isEmpty(strName)){
                    db_data.modifyItem(id,strName,strDescription,doublePrice);
                    Intent intent = new Intent(ModifyEdit.this,ManageProduct.class);
                    Snackbar.make(modifyCl,"Updated Successfully!",Snackbar.LENGTH_SHORT).show();
                    startActivity(intent);
                }
                else{
                    Snackbar.make(modifyCl,"Fill all fields!",Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ModifyEdit.this,ManageProduct.class);
                startActivity(intent);
            }
        });

    }

    private void init() {
        etID = (EditText) findViewById(R.id.etID);
        etName = (EditText) findViewById(R.id.etUpdateName);
        etDescription = (EditText) findViewById(R.id.etUpdateDescription);
        etPrice = (EditText) findViewById(R.id.etUpdatePrice);
        btnSave = (Button) findViewById(R.id.btnSaveModifyItem);
        btnCancel = (Button) findViewById(R.id.btnCancelModifyItem);
        modifyCl = (CoordinatorLayout) findViewById(R.id.modifyItemCl);
    }
}
