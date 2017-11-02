package com.example.sydney.psov3;

import android.app.Dialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static com.example.sydney.psov3.Constants.COLUMN_PRODUCT_DESCRIPTION;
import static com.example.sydney.psov3.Constants.COLUMN_PRODUCT_ID;
import static com.example.sydney.psov3.Constants.COLUMN_PRODUCT_NAME;
import static com.example.sydney.psov3.Constants.COLUMN_PRODUCT_PRICE;
import static com.example.sydney.psov3.Constants.COLUMN_PRODUCT_QUANTITY;
import static com.example.sydney.psov3.Constants.TABLE_PRODUCT;

public class MainActivity extends AppCompatActivity {
    //For Update
    public static final int requestcode = 1;
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
    AlertDialog.Builder builder;
    AlertDialog alertTerminal;

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

        //Todo DIALOG LOADING
        getSupportActionBar().hide();

        if (db_data.checkSerial() > 0) {
            try {
                db_data.addAdmin("admin", "admin");
                db_data.addGrandTotal(0.0);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            importProduct();

//        For ActivityLogin
            tv_signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flexNiLogin.setVisibility(View.GONE);
                    layout_signup.setVisibility(View.VISIBLE);
                    o = 1;
                }
            });
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String muser = et_usernum.getText().toString();
                    String mpass = et_pass.getText().toString();
                    if (chk_admin.isChecked()) {
                        int result = db_data.adminLogin(muser, mpass);
                        if (result > 0) {
                            et_usernum.setText("");
                            et_pass.setText("");
                            Intent myIntent = new Intent(MainActivity.this, AdminActivity.class);
                            startActivity(myIntent);
                            Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Incorrect ID number/Password!", Toast.LENGTH_SHORT).show();
                            et_pass.setText("");
                        }
                    } else {
                        int result = db_data.cashierLogin(muser, mpass);
                        if (result > 0) {
                            Intent myIntent = new Intent(getApplicationContext(), Cashier.class);
                            //PASS INDEX
                            myIntent.putExtra("CashNum", muser);
                            et_usernum.setText("");
                            et_pass.setText("");
                            Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                            startActivity(myIntent);
                        } else {
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
                    o = 0;
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

                        db_data.addCashier(mname, mnum, mpass, mpos);
                        Toast.makeText(MainActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            terminalBaKamo();
        }

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

    private void importProduct() {
        try {
            String filepath = "/storage/emulated/0/download/updatepos.csv";
            SQLiteDatabase dbWrite = db_data.getWritableDatabase();
            String tableName = TABLE_PRODUCT;
//            dbWrite.execSQL("delete from " + tableName);
            try {
                FileReader file = new FileReader(filepath);
                BufferedReader buffer = new BufferedReader(file);
                ContentValues contentValues = new ContentValues();
                String line = "";
                dbWrite.beginTransaction();
                while ((line = buffer.readLine()) != null) {
                    String[] str = line.split(",", 5);  // defining 3 columns with null or blank field //values acceptance

                    if (db_data.searchProdForUpdate(str[0]) > 0) {
                        String mWHERE = COLUMN_PRODUCT_ID + " = ?";
                        String[] mWHERE_ARGS = new String[]{str[0]};
//                        String pId = str[0];
                        String pName = str[1];
                        String pDesc = str[2];
                        String pPrice = str[3];
//                        String pQuan = str[4];
//                        contentValues.put(COLUMN_PRODUCT_ID, pId);
                        contentValues.put(COLUMN_PRODUCT_NAME, pName);
                        contentValues.put(COLUMN_PRODUCT_DESCRIPTION, pDesc);
                        contentValues.put(COLUMN_PRODUCT_PRICE, pPrice);
//                        contentValues.put(COLUMN_PRODUCT_QUANTITY, pQuan);
                        dbWrite.update(tableName, contentValues, mWHERE, mWHERE_ARGS);
                    } else {
                        //Id, Company,Name,Price
                        String pId = str[0];
                        String pName = str[1];
                        String pDesc = str[2];
                        String pPrice = str[3];
                        String pQuan = str[4];
                        contentValues.put(COLUMN_PRODUCT_ID, pId);
                        contentValues.put(COLUMN_PRODUCT_NAME, pName);
                        contentValues.put(COLUMN_PRODUCT_DESCRIPTION, pDesc);
                        contentValues.put(COLUMN_PRODUCT_PRICE, pPrice);
                        contentValues.put(COLUMN_PRODUCT_QUANTITY, pQuan);
                        dbWrite.insert(tableName, null, contentValues);
                    }

                    }
                Toast.makeText(this, "Update Successful", Toast.LENGTH_LONG).show();
                dbWrite.setTransactionSuccessful();
                dbWrite.endTransaction();
            } catch (IOException e) {
                if (dbWrite.inTransaction())
                    dbWrite.endTransaction();
                Dialog d = new Dialog(this);
                d.setTitle(e.getMessage() + "first");
                d.show();
                // db.endTransaction();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Failed to import", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    void terminalBaKamo() {
        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_terminal, null);
        builder.setView(alertLayout);
        final AppCompatButton btnEnter = (AppCompatButton) alertLayout.findViewById(R.id.btnTerminalSubmit);
        final EditText etName = (AppCompatEditText) alertLayout.findViewById(R.id.etTerminalName);
        final EditText etSerial = (AppCompatEditText) alertLayout.findViewById(R.id.etTerminalSerial);

        alertTerminal = builder.create();
        alertTerminal.show();

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etName.getText().toString().equals("") || etSerial.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please fill all fields.", Toast.LENGTH_SHORT).show();
                } else {
                    db_data.addTerminal(etName.getText().toString(), etSerial.getText().toString());
                    alertTerminal.dismiss();
                }
            }
        });
    }

    //Fragments
    public static class FragmentSignUp extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_signup, container, false);
        }
    }
}