package com.example.sydney.psov3;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btn_adminlogin,btn_admin,btn_cash,btn_cashregcancel,btn_cashlogin,btn_cashreg;
    TextView txt_signup;
    EditText txt_adminpass,txt_cashname,txt_cashpass,txt_emplname,txt_empfname,txt_empid,txt_emppass;
    LinearLayout ll_welcome,ll_log,ll_cashlog,ll_cashsignup;
    Spinner spn_staff_pos;
    DB_Data db_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db_data = new DB_Data(this);
        
        ll_log=(LinearLayout)findViewById(R.id.ll_log);
        ll_welcome=(LinearLayout)findViewById(R.id.ll_welcome);
        ll_cashlog=(LinearLayout)findViewById(R.id.ll_cashlog);
        ll_cashsignup=(LinearLayout)findViewById(R.id.ll_cashsignup);
        btn_adminlogin= (Button)findViewById(R.id.btn_adminlogin);
        btn_admin=(Button)findViewById(R.id.btn_admin);
        btn_cash=(Button)findViewById(R.id.btn_cash);
        btn_cashlogin=(Button)findViewById(R.id.btn_cashlogin);
        btn_cashregcancel=(Button)findViewById(R.id.btn_cashreg_cancel);
        btn_cashreg=(Button)findViewById(R.id.btn_cashreg);
        txt_signup=(TextView)findViewById(R.id.txt_signup);
        txt_adminpass=(EditText)findViewById(R.id.txt_adminpass);
        txt_cashname=(EditText)findViewById(R.id.txt_cashname);
        txt_cashpass=(EditText)findViewById(R.id.txt_cashpass);
        txt_empfname=(EditText)findViewById(R.id.txt_empfname);
        txt_emplname=(EditText)findViewById(R.id.txt_emplname);
        txt_empid=(EditText)findViewById(R.id.txt_empid);
        txt_emppass=(EditText)findViewById(R.id.txt_emppass);
        spn_staff_pos=(Spinner)findViewById(R.id.spn_staffpos);

        ll_welcome.setVisibility(View.VISIBLE);
        btn_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_welcome.setVisibility(View.GONE);
                ll_log.setVisibility(View.VISIBLE);
            }
        });
        btn_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_welcome.setVisibility(View.GONE);
                ll_cashlog.setVisibility(View.VISIBLE);
            }
        });
        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_cashsignup.setVisibility(View.VISIBLE);
                ll_cashlog.setVisibility(View.GONE);
            }
        });
        btn_cashregcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_cashsignup.setVisibility(View.GONE);
                ll_cashlog.setVisibility(View.VISIBLE);
            }
        });
//                String vpass = txt_adminpass.getText().toString().trim();
//                //db_data.addStudent("Mary","Parker",vpass);
//                int result= db_data.adminLogin(vpass);
//                if(result > 0){
//                    txt_adminpass.setText("");
//                    Intent myIntent = new Intent(MainActivity.this,AdminActivity.class);
//                    startActivity(myIntent);
//                    Toast.makeText(MainActivity.this, "Login Successfull!", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    Toast.makeText(MainActivity.this, "Incorrect Password!", Toast.LENGTH_SHORT).show();
//                }

                    Intent myIntent = new Intent(MainActivity.this,AdminActivity.class);
                    startActivity(myIntent);
                    Toast.makeText(MainActivity.this, "Login Successfull!", Toast.LENGTH_SHORT).show();
        // TODO: 5/30/2017 Log in for both admin and staff
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String muser = txt_user.getText().toString();
                String mpass = txt_pass.getText().toString();
                if(chk_admin.isChecked){
                    int result=db_data.adminLogin(muser,mpass);
                    if(result > 0){
                        txt_user.setText("");
                        txt_pass.setText("");
                        Intent myIntent = new Intent(MainActivity.this,AdminActivity.class);
                        startActivity(myIntent);
                        Toast.makeText(MainActivity.this, "Login Successfull!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Incorrect Password!", Toast.LENGTH_SHORT).show();
                        txt_pass.setText("");
                    }
                }
                else{
                    int result=db_data.cashierLogin(muser,mpass);
                    if(result > 0){
                        Intent myIntent = new Intent(MainActivity.this,Cashier.class);
                        startActivity(myIntent);
                        Toast.makeText(MainActivity.this, "Login Successfull!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Incorrect ID number/Password!", Toast.LENGTH_SHORT).show();
                        txt_pass.setText("");
                    }
                }
            }
        });
        btn_cashreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cfname = txt_empfname.getText().toString().trim();
                String clname = txt_emplname.getText().toString().trim();
                String cnum = txt_empid.getText().toString().trim();
                String cpass = txt_emppass.getText().toString().trim();
                String pos = spn_staff_pos.getSelectedItem().toString();
                try {
                    db_data.addCashier(cfname,clname,cnum,cpass,pos);
                    Toast.makeText(MainActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    public void onBackPressed(){
        txt_adminpass.setText("");
        txt_cashpass.setText("");
        txt_cashname.setText("");

        ll_log.setVisibility(View.GONE);
        ll_welcome.setVisibility(View.VISIBLE);
        ll_cashlog.setVisibility(View.GONE);
        ll_cashsignup.setVisibility(View.GONE);
    }
    public static class FirstFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_login, container, false);
        }
    }
    public static class SecondFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_cashier_login, container, false);
        }
    }
    public static class ThirdFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_cashier_register, container, false);
        }
    }
    public static class FourthFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_welcome, container, false);
        }
    }
}