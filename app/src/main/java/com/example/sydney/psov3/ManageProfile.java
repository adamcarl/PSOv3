package com.example.sydney.psov3;

import android.content.Intent;
import android.database.Cursor;
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

import static com.example.sydney.psov3.Constants.TABLE_ADMIN;


/**
 * Created by PROGRAMMER2 on 6/3/2017.
 */

public class ManageProfile  extends AppCompatActivity{
    AlertDialog.Builder builder = null;
    AlertDialog alertDialog = null;

    private EditText name,password;
    private Button update;

    DB_Data db_data = null;

    String adminName = "";
    String adminPassword = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_profile);
        init();

        db_data = new DB_Data(this);
        SQLiteDatabase db = db_data.getReadableDatabase();
        String SELECT_QUERY = "SELECT * FROM " + TABLE_ADMIN;
        Cursor cursor = db.rawQuery(SELECT_QUERY,null);

        while (cursor.move(1)){
            adminName = cursor.getString(1);
            adminPassword = cursor.getString(2);

            name.setText(adminName);
            password.setText(adminPassword);
        }

        cursor.close();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });

        //CREATE DIALOG
        createMyDialog();
        alertDialog = builder.create();
    }

    private void createMyDialog() {

        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_updateadmin, null);
        builder.setView(alertLayout);

        final EditText dialogAdminName =(EditText)alertLayout.findViewById(R.id.etUpdateAdminName);
        final EditText dialogAdminPassword =(EditText) alertLayout.findViewById(R.id.etUpdateAdminPassword);

        final Button btnSave = (Button) alertLayout.findViewById(R.id.btnAdminUpdateSave);
        final Button btnCancel = (Button) alertLayout.findViewById(R.id.btnAdminUpdateCancel);

        dialogAdminName.setText(adminName);
        dialogAdminPassword.setText(adminPassword);

        dialogAdminName.setEnabled(false);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String mAdminName = dialogAdminName.getText().toString();
                String mAdminPassword = dialogAdminPassword.getText().toString();


                if(mAdminPassword.isEmpty()){
                    Toast.makeText(ManageProfile.this, "Fill Password!", Toast.LENGTH_SHORT).show();
                }
                else{
                    db_data.updateAdmin(mAdminPassword);

                    dialogAdminPassword.setText("");

                    alertDialog.dismiss();

                    password.setText(mAdminPassword);

                    Toast.makeText(ManageProfile.this, "Password updated !", Toast.LENGTH_SHORT).show();
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

    private void init() {
        name = (EditText) findViewById(R.id.etAdminName);
        password = (EditText) findViewById(R.id.etAdminPassword);
        update = (Button) findViewById(R.id.btnAdminUpdate);
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
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.menu_logout:
                Intent intent = new Intent(ManageProfile.this, MainActivity.class);
                startActivity(intent);
                //Todo Function Here for saving Transaction
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}