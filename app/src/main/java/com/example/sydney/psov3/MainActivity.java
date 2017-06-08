package com.example.sydney.psov3;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;

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
    EditText et_regName,et_regUsernum,et_regPass;
    Spinner spn_regPosition;
    Button btn_cancel,btn_register;
    FlexboxLayout flexNiLogin,flexNiSignUp;

    //For Orientation
    int o=0;
    int or;
    String ori;
    CharSequence userText;

    protected void onSaveInstanceState(Bundle savedInstanceState){
        userText = o+"";
        savedInstanceState.putCharSequence(ori,userText);
        super.onSaveInstanceState(savedInstanceState);
    }
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        userText = savedInstanceState.getCharSequence(ori);
        or= Integer.parseInt(userText.toString());
        if(or==0){
            layout_signup.setVisibility(View.GONE);
            flexNiLogin.setVisibility(View.VISIBLE);
            o=or;
        }else if(or==1) {
            flexNiLogin.setVisibility(View.GONE);
            layout_signup.setVisibility(View.VISIBLE);
            o=or;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        getSupportActionBar().hide();
//        db_data.addAdmin("1","1");
//        For ActivityLogin
        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flexNiLogin.setVisibility(View.GONE);
                layout_signup.setVisibility(View.VISIBLE);
                o=1;
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
                        Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Incorrect ID number/Password!", Toast.LENGTH_SHORT).show();
                        et_pass.setText("");
                    }
                }
                else{
                    int result=db_data.cashierLogin(muser,mpass);
                    if(result > 0){
                        et_usernum.setText("");
                        et_pass.setText("");
                        Intent myIntent = new Intent(getApplicationContext(),Cashier.class);

                        //PASS INDEX
                        myIntent.putExtra("CashNum",muser);
                        Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        startActivity(myIntent);
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Incorrect ID number/Password!", Toast.LENGTH_SHORT).show();
                        et_pass.setText("");
                    }
                }
            }
        });

        //For SignUp
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_signup.setVisibility(View.GONE);
                flexNiLogin.setVisibility(View.VISIBLE);
                et_regName.setText("");
                et_regUsernum.setText("");
                et_regPass.setText("");
                spn_regPosition.setSelection(0);
                et_usernum.setText("");
                et_pass.setText("");
                o=0;
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mname = et_regName.getText().toString().trim();
                String mnum = et_regUsernum.getText().toString().trim();
                String mpass = et_regPass.getText().toString().trim();
                String mpos = spn_regPosition.getSelectedItem().toString();
                try {
                    et_regName.setText("");
                    et_regUsernum.setText("");
                    et_regPass.setText("");
                    spn_regPosition.setSelection(0);

                    db_data.addCashier(mname,mnum,mpass,mpos);
                    Toast.makeText(MainActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
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
        chk_admin=(CheckBox)findViewById(R.id.spinnerAdmin);

        //For SignUp
        layout_signup=(LinearLayout)findViewById(R.id.ll_signup);
        btn_cancel=(Button)findViewById(R.id.btnSignupCancel);
        btn_register=(Button)findViewById(R.id.btnRegister);

        //For Register
        et_regName=(EditText)findViewById(R.id.etRegName);
        et_regUsernum=(EditText)findViewById(R.id.etRegUsernum);
        et_regPass=(EditText)findViewById(R.id.etRegPassword);
        spn_regPosition=(Spinner)findViewById(R.id.spinnerRegPosition);

        flexNiLogin = (FlexboxLayout)findViewById(R.id.flexNiLogin);
        flexNiSignUp = (FlexboxLayout)findViewById(R.id.flexNiSignUp);
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