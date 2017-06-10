package com.example.sydney.psov3;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import static com.example.sydney.psov3.Constants.*;

/**
 * Created by PROGRAMMER2 on 6/2/2017.
 */


public class ManageStaff extends AppCompatActivity {
    AlertDialog.Builder builder = null;
    AlertDialog alertDialog = null;
    private Button btn_update;
    private EditText etSearchStaff,et_Name, et_Password;
    private Spinner spinner_Position;

    DB_Data db_data;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_staff);

        db_data = new DB_Data(this);
        init();
        allButtonOnclickListener();

        spinner_Position.setEnabled(false);//SET SPINNER TO DISABLE INITIALLY
        etSearchStaff.addTextChangedListener(myTextWatcher);//Automatic Search for staff number

        //CREATE DIALOG
        createMyDialog();
        alertDialog = builder.create();
    }

    private void init() {
        btn_update = (Button) findViewById(R.id.btnUpdate);
        etSearchStaff = (EditText) findViewById(R.id.etSearchNumber);
        et_Name = (EditText) findViewById(R.id.etName);
        et_Password = (EditText) findViewById(R.id.etPass);
        spinner_Position = (Spinner) findViewById(R.id.spinnerUpdPos);
    }

    private void createMyDialog() {

        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_updatestaff, null);
        builder.setView(alertLayout);

        final EditText staffName =(EditText)alertLayout.findViewById(R.id.etUpdateName);
        final EditText staffPassword =(EditText) alertLayout.findViewById(R.id.etUpdatePassword);
        final Spinner staffPosition =(Spinner) alertLayout.findViewById(R.id.spinnerUpdPos);

        final Button btnSave = (Button) alertLayout.findViewById(R.id.btnSaveUpdateStaff);
        final Button btnCancel = (Button) alertLayout.findViewById(R.id.btnCancelUpdateStaff);

        staffPosition.setSelection(0); //initially Manager

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mStaffName = staffName.getText().toString();
                String mStaffPassword = staffPassword.getText().toString();
                String mStaffPosition = Integer.toString(staffPosition.getSelectedItemPosition());


                int mCashierNumber = Integer.parseInt(etSearchStaff.getText().toString().trim());

                if(mStaffName.isEmpty() || mStaffPassword.isEmpty()){
                    Toast.makeText(ManageStaff.this, "Fill all fields!", Toast.LENGTH_SHORT).show();
                }
                else{
                    db_data.updateStaff(mCashierNumber,mStaffName,mStaffPassword,mStaffPosition);
                    etSearchStaff.getText();
                    alertDialog.dismiss();

                    et_Name.setText(mStaffName);
                    et_Password.setText(mStaffPassword);
                    spinner_Position.setSelection(Integer.parseInt(mStaffPosition));

                    Toast.makeText(ManageStaff.this, "Updated!", Toast.LENGTH_SHORT).show();
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

    private void allButtonOnclickListener() {

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Disable Spinner when editting
                alertDialog.show();
            }
        });
    }



    //MENU

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
        String query = "SELECT * FROM "+ TABLE_CASHIER + " WHERE " + COLUMN_CASHIER_NUMBER + " = " + mStaffCode;
        Cursor cursor = db_data.queryDataRead(query);

        String mStaffName = "", mStaffPass = "", mStaffPos = "";

        if(cursor != null){
            cursor.moveToFirst();
            mStaffName = cursor.getString(2);
            mStaffPos = cursor.getString(3);
            mStaffPass = cursor.getString(4);
        }

        et_Name.setText(mStaffName);
        et_Password.setText(mStaffPass);

        if(mStaffPos.equals("Manager")){
            spinner_Position.setSelection(0);
        }
        if(mStaffPos.equals("Supervisor")){
            spinner_Position.setSelection(1);
        }
        if(mStaffPos.equals("Cashier")){
            spinner_Position.setSelection(2);
        }

        Toast.makeText(this, spinner_Position.getSelectedItemPosition(), Toast.LENGTH_SHORT).show();

    }
}
