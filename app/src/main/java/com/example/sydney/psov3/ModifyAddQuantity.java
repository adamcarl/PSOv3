package com.example.sydney.psov3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by PROGRAMMER2 on 8/7/2017.
 */

public class ModifyAddQuantity  extends AppCompatActivity{
    private EditText etAddQuantityRemarks,etQuantity;
    private Button btnSave, btnCancel;
    private CoordinatorLayout mCl;
    private RadioButton rbDelivery,rbTransfer,rbOtherAddQuantity;
    private RadioGroup rgAddQuantity;
    private String logType ="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_addquantity_item);
        init();

        Intent fetchIntent = getIntent();
        final String id = fetchIntent.getExtras().getString("ID");
        final int quantity = fetchIntent.getExtras().getInt("QUANTITY");

        rgAddQuantity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if(rbTransfer.isChecked()){
                    logType = "Transfer In";
                }
                else if(rbOtherAddQuantity.isChecked()){
                    logType = "Other";
                }
                else{
                    logType = "Delivery";
                }
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date currDate = new Date();
                final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MMM-dd-yyyy hh:mm a");
                String dateConvertion = dateTimeFormat.format(currDate);
//                Date strToDate = dateTimeFormat.parse(dateConvertion);
//                String dateToString = strToDate.toString();

                String strQuantity = etQuantity.getText().toString().trim().replaceAll("[.]","");
                if(strQuantity.isEmpty()){
                    strQuantity = "0";
                }
                String strRemarks = etAddQuantityRemarks.getText().toString();
                int cQuantity = Integer.parseInt(strQuantity);

                DB_Data db_data = new DB_Data(ModifyAddQuantity.this);
                if(!TextUtils.isEmpty(strQuantity) || !strQuantity.equals("0") && !TextUtils.isEmpty(strRemarks) || strRemarks.equals("")
                        && rbTransfer.isChecked() || rbOtherAddQuantity.isChecked() || rbDelivery.isChecked()){
                    db_data.updateAddQuantity(id,quantity+cQuantity);
                    db_data.addProductLogs(id,logType,cQuantity,0,strRemarks,dateConvertion);
                    Intent intent = new Intent(ModifyAddQuantity.this,ManageProduct.class);
                    Snackbar.make(mCl,"Added Successfully!",Snackbar.LENGTH_SHORT).show();
                    startActivity(intent);
                }
                else{
                    Snackbar.make(mCl,"Complete all fields!",Snackbar.LENGTH_SHORT).show();
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
        etAddQuantityRemarks = (EditText) findViewById(R.id.etAddQuantityRemarks);
        btnSave = (Button) findViewById(R.id.btnSaveAddQuantity);
        btnCancel = (Button) findViewById(R.id.btnCancelAddQuantity);
        mCl = (CoordinatorLayout) findViewById(R.id.addQuantityCl);
        rbDelivery = (RadioButton) findViewById(R.id.rbDelivery);
        rbTransfer = (RadioButton) findViewById(R.id.rbTransfer);
        rbOtherAddQuantity = (RadioButton) findViewById(R.id.rbOtherAddQuantity);
        rgAddQuantity = (RadioGroup) findViewById(R.id.rgAddQuantity);
    }
}
