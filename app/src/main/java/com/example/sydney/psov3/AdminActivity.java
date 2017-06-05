package com.example.sydney.psov3;

import android.app.Dialog;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.DrawableUtils;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sydney.psov3.adapter.AdapterOrder;
import com.example.sydney.psov3.adapter.AdapterProd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static com.example.sydney.psov3.Constants.*;

public class AdminActivity extends AppCompatActivity {
    //For Database
    DB_Data db_data;

    //For Admin Privilege
    Button btn_adminManStaff,btn_adminManProd,btn_adminManJournal,btn_dummyA, btn_adminManProfile;

    //TO SUPPORT VECTOR DRAWABLES
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

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
                Intent intent = new Intent(AdminActivity.this,ManageStaff.class);
                startActivity(intent);
            }
        });

        btn_adminManProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this,ManageProduct.class);
                startActivity(intent);
            }
        });

        btn_adminManJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this,ManageJournal.class);
                startActivity(intent);
            }
        });

        btn_dummyA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminActivity.this, "Manage A", Toast.LENGTH_SHORT).show();
            }
        });

        btn_adminManProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this,ManageProfile.class);
                startActivity(intent);
            }
        });
    }

    private void init(){
        //For Database
        db_data=new DB_Data(this);

        //For Admin Privilege Screen
        btn_adminManStaff=(Button)findViewById(R.id.btnStaff);
        btn_adminManProd=(Button)findViewById(R.id.btnProduct);
        btn_adminManJournal=(Button)findViewById(R.id.btnJournal);
        btn_dummyA=(Button)findViewById(R.id.btnD);
        btn_adminManProfile =(Button)findViewById(R.id.btnProfile);
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
                Intent intent = new Intent(AdminActivity.this, MainActivity.class);
                startActivity(intent);

                //Todo Function Here for saving Transaction

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}