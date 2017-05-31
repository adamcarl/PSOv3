package com.example.sydney.psov3;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //For Database
    DB_Data db_data;
    
    //For ActivityLogin
    CheckBox chk_admin;
    EditText et_usernum,et_pass;
    Button btn_login;
    TextView tv_signup;
    
    //For FragmentSignUp
    LinearLayout layout_signup;
    EditText et_regFname,et_regLname,et_regUsernum,et_regPass;
    Spinner spn_regPosition;
    Button btn_cancel,btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        init();
        getSupportActionBar().hide();

        //For ActivityLogin
        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_signup.setVisibility(View.VISIBLE);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String muser = et_usernum.getText().toString();
                String mpass = et_pass.getText().toString();
                if(chk_admin.isChecked()){
                    int result=db_data.adminLogin(muser,mpass);
                    if(result > 0){
                        et_usernum.setText("");
                        et_pass.setText("");
                        Intent myIntent = new Intent(MainActivity.this,AdminActivity.class);
                        startActivity(myIntent);
                        Toast.makeText(MainActivity.this, "Login Successfull!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Incorrect Password!", Toast.LENGTH_SHORT).show();
                        et_pass.setText("");
                    }
                }
                else{
                    int result=db_data.cashierLogin(muser,mpass);
                    if(result > 0){
                        et_usernum.setText("");
                        et_pass.setText("");
                        Intent myIntent = new Intent(MainActivity.this,Cashier.class);
                        startActivity(myIntent);
                        Toast.makeText(MainActivity.this, "Login Successfull!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Incorrect ID number/Password!", Toast.LENGTH_SHORT).show();
                        et_pass.setText("");
                    }
                }
            }
        });

        //For SignUp
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                layout_signup.setVisibility(View.GONE);
//                et_regFname.setText("");
//                et_regLname.setText("");
//                et_regUsernum.setText("");
//                et_regPass.setText("");
//                spn_regPosition.setSelection(0);
//                et_usernum.setText("");
//                et_pass.setText("");
//            }
//        });
//        btn_register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String mfname = et_regFname.getText().toString().trim();
//                String mlname = et_regLname.getText().toString().trim();
//                String mnum = et_regUsernum.getText().toString().trim();
//                String mpass = et_regPass.getText().toString().trim();
//                String mpos = spn_regPosition.getSelectedItem().toString();
//                try {
//                    db_data.addCashier(mfname,mlname,mnum,mpass,mpos);
//                    et_regFname.setText("");
//                    et_regLname.setText("");
//                    et_regUsernum.setText("");
//                    et_regPass.setText("");
//                    spn_regPosition.setSelection(0);
//                    Toast.makeText(MainActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
//                }
//                catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    //Initialization
    private void init() {
        //Database
        db_data = new DB_Data(this);
        
        //For LogIn
        btn_login=(Button)findViewById(R.id.btnLogin);
        et_usernum=(EditText)findViewById(R.id.etUsernum);
        et_pass=(EditText)findViewById(R.id.etPassword);
        tv_signup=(TextView)findViewById(R.id.txtSignup);
        
        //For SignUp
//        layout_signup=(LinearLayout)findViewById(R.id.ll_signup);
//        btn_cancel=(Button)findViewById(R.id.btn_cancel);
//        btn_register=(Button)findViewById(R.id.btn_signup);
//
//        //For Register
//        et_regFname=(EditText)findViewById(R.id.et_regFname);
//        et_regLname=(EditText)findViewById(R.id.et_regLname);
//        et_regUsernum=(EditText)findViewById(R.id.et_regUsernum);
//        et_regPass=(EditText)findViewById(R.id.et_regPass);
//        spn_regPosition=(Spinner)findViewById(R.id.spn_regPosition);
    }
    //BackButton Holder
    public void onBackPressed(){

    }
    //Fragments
    public static class FragmentSignUp extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_signup, container, false);
        }
    }
}