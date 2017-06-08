package com.example.sydney.psov3;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import static com.example.sydney.psov3.Constants.*;

/**
 * Created by PROGRAMMER2 on 6/2/2017.
 */


public class ManageStaff extends AppCompatActivity {
    private Button btn_update,btn_save, btn_cancel;
    private EditText etSearchStaff,et_Name, et_Password;
    private Spinner spinner_Position;

    DB_Data db_data = new DB_Data(this);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_staff);

        init();
        allButtonOnclickListener();

        spinner_Position.setEnabled(false);//SET SPINNER TO DISABLE INITIALLY
        etSearchStaff.addTextChangedListener(myTextWatcher);//Automatic Search for staff number
    }

    private void allButtonOnclickListener() {

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Enable all edittext
                et_Name.setEnabled(true);
                et_Password.setEnabled(true);
                spinner_Position.setEnabled(true);

                //Set button SAVE & CANCEL visible while UPDATE invisible
                btn_save.setVisibility(View.VISIBLE);
                btn_cancel.setVisibility(View.VISIBLE);
                btn_update.setVisibility(View.GONE);

                //Disable Spinner when editting
                etSearchStaff.setText("");
                etSearchStaff.setEnabled(false);
                spinner_Position.setEnabled(false);

            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Disable all edittext
                et_Name.setEnabled(false);
                et_Password.setEnabled(false);
                spinner_Position.setEnabled(false);

                //Set button SAVE & CANCEL invisible while UPDATE visible
                btn_save.setVisibility(View.GONE);
                btn_cancel.setVisibility(View.GONE);
                btn_update.setVisibility(View.VISIBLE);

                //Enable Spinner when editting
                etSearchStaff.setEnabled(true);
                spinner_Position.setEnabled(true);

                // TODO: 6/2/2017 FUNCTION FOR SAVE BELOW

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Disable all edittext
                et_Name.setEnabled(false);
                et_Password.setEnabled(false);
                spinner_Position.setEnabled(false);

                //Set button SAVE & CANCEL invisible while UPDATE visible
                btn_save.setVisibility(View.GONE);
                btn_cancel.setVisibility(View.GONE);
                btn_update.setVisibility(View.VISIBLE);

                //Enable Spinner when editting
                etSearchStaff.setEnabled(true);
                spinner_Position.setEnabled(true);

                // TODO: 6/2/2017 FUNCTION FOR SAVE BELOW
            }
        });
    }

    private void init() {
        btn_update = (Button) findViewById(R.id.btnUpdate);
        btn_save = (Button) findViewById(R.id.btnSave);
        btn_cancel = (Button) findViewById(R.id.btnCancel);
        etSearchStaff = (EditText) findViewById(R.id.etSearchNumber);
        et_Name = (EditText) findViewById(R.id.etName);
        et_Password = (EditText) findViewById(R.id.etPass);
        spinner_Position = (Spinner) findViewById(R.id.spinnerUpdPos);
    }

    //MENU

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
                Intent intent = new Intent(ManageStaff.this, MainActivity.class);
                startActivity(intent);

                //Todo Function Here for saving Transaction

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private TextWatcher myTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            try{
                String mStaffCode = etSearchStaff.getText().toString().trim();
                int result = db_data.searchStaffNumber(mStaffCode);

                if(result > 0 ){
                    printCashierDetails(mStaffCode);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    private void printCashierDetails(String mStaffCode) {
        String query = "SELECT * FROM "+ TABLE_CASHIER + "WHERE " + COLUMN_CASHIER_NUMBER + "=" + mStaffCode;
        Cursor cursor = db_data.queryDataRead(query);

        String mStaffName = "", mStaffPass = "", mStaffPos = "";

        if(cursor != null){
            cursor.moveToFirst();
            mStaffName = cursor.getString(2);
            mStaffPos = cursor.getString(3);
            mStaffPass = cursor.getString(4);
        }

        et_Name.setText(mStaffName);
            int mStaffPosConverted = Integer.parseInt(mStaffPos);//STRING POSITION CONVERTED TO INT
        spinner_Position.setSelection(mStaffPosConverted);
        et_Password.setText(mStaffPass);
    }
}
