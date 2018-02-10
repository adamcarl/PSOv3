package com.example.sydney.psov3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.menu.MenuBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class AdminActivity extends AppCompatActivity{
    //For Database

    //For Admin Privilege

    //TO SUPPORT VECTOR DRAWABLES
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    Button btn_adminManStaff, btn_adminManProd, btn_adminManJournal, btn_adminManReport, btn_adminManProfile, btn_adminManRestore;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_priveleges);

        init(); //cast views to java class variables
        allButtonClickListener(); //al button click listener
    }

    private void allButtonClickListener() {
        btn_adminManStaff.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, ManageStaff.class);
                startActivity(intent);
            }

        });

        btn_adminManProd.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, ManageProduct.class);
                startActivity(intent);
            }

        });

        btn_adminManJournal.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, ManageJournal.class);
                startActivity(intent);
            }

        });

        btn_adminManReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, ManageReport.class);
                startActivity(intent);
            }
        });

        btn_adminManProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, ManageProfile.class);
                startActivity(intent);
            }

        });
        btn_adminManRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, ManageRestore.class);
                startActivity(intent);
            }
        });
    }

    private void init(){
        //For Admin Privilege Screen
        btn_adminManStaff=(Button)findViewById(R.id.btnStaff);
        btn_adminManProd=(Button)findViewById(R.id.btnProduct);
        btn_adminManJournal=(Button)findViewById(R.id.btnJournal);
        btn_adminManReport = (Button) findViewById(R.id.btnReport);
        btn_adminManProfile =(Button)findViewById(R.id.btnProfile);
        btn_adminManRestore = (Button) findViewById(R.id.btnRestore);
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
                this.finish();
                Intent intent = new Intent(AdminActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}