package com.example.sydney.psov3;

import android.app.Dialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sydney.psov3.POJO.SetListener;
import com.google.android.flexbox.FlexboxLayout;
import com.jolimark.JmPrinter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static android.provider.BaseColumns._ID;
import static com.example.sydney.psov3.Constants.COLUMN_ITEM_PRODUCT;
import static com.example.sydney.psov3.Constants.COLUMN_ITEM_QUANTITY;
import static com.example.sydney.psov3.Constants.COLUMN_ITEM_ZREPORT;
import static com.example.sydney.psov3.Constants.COLUMN_PRODUCT_DESCRIPTION;
import static com.example.sydney.psov3.Constants.COLUMN_PRODUCT_ID;
import static com.example.sydney.psov3.Constants.COLUMN_PRODUCT_ID_TEMP;
import static com.example.sydney.psov3.Constants.COLUMN_PRODUCT_NAME;
import static com.example.sydney.psov3.Constants.COLUMN_PRODUCT_PRICE;
import static com.example.sydney.psov3.Constants.COLUMN_PRODUCT_QUANTITY;
import static com.example.sydney.psov3.Constants.COLUMN_PRODUCT_QUANTITY_TEMP;
import static com.example.sydney.psov3.Constants.COLUMN_TRANSACTION_TYPE;
import static com.example.sydney.psov3.Constants.TABLE_ITEM;
import static com.example.sydney.psov3.Constants.TABLE_PRODUCT;
import static com.example.sydney.psov3.Constants.TABLE_PRODUCT_TEMP;
import static com.example.sydney.psov3.Constants.TABLE_TRANSACTION;
import static com.example.sydney.psov3.Constants.functionKeys;
import static com.example.sydney.psov3.Constants.functionKeysID;
import static com.example.sydney.psov3.POJO.FunctionCall.unLockCashBox;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener {
    //For Database
    DB_Data db_data;
    //For ActivityLogin
    CheckBox chk_admin;
    EditText et_usernum,et_pass;
    Button btn_login;
    TextView tv_signup;
    //For FragmentSignUp
    LinearLayout layout_signup, layout_keys;
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

    private SetListener setListener;

    private Button[] functionKeysBtn = new Button[12];
    private int functionKeysMode;

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
            functionKeysMode = o;
            refreshFunctionKeys();

        }else if(or==1) {
            flexNiLogin.setVisibility(View.GONE);
            layout_signup.setVisibility(View.VISIBLE);
            o=or;
            functionKeysMode = o;
            refreshFunctionKeys();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        getSupportActionBar().hide();

        layout_keys.setVisibility(View.VISIBLE);
        layout_signup.setVisibility(View.GONE);
        functionKeysMode = o;
        refreshFunctionKeys();

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
                    setCallRegister();
                }
            });
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (chk_admin.isChecked())
                        loginBaKamo();
//                    else
//                    checkZReadPending();
                }
            });
            //For SignUp
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setCancel();
                }
            });
            btn_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else {
            terminalBaKamo();
        }

        for (int i = 0; i < functionKeysBtn.length; i++) {
            final int functionKeysCode = i;
            functionKeysBtn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    functionKeysDo(functionKeysCode);
                    functionKeysBtn[functionKeysCode].setOnKeyListener(MainActivity.this);
                }
            });
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

        //For Keys
        layout_keys = (LinearLayout) findViewById(R.id.ll_keys);

        //For Register
        et_regName=(EditText)findViewById(R.id.etRegName);
        et_regUsernum=(EditText)findViewById(R.id.etRegUsernum);
        et_regPass=(EditText)findViewById(R.id.etRegPassword);
        spn_regPosition=(Spinner)findViewById(R.id.spinnerRegPosition);

        flexNiLogin = (FlexboxLayout)findViewById(R.id.flexNiLogin);
        flexNiSignUp = (FlexboxLayout)findViewById(R.id.flexNiSignUp);

        for (int a = 0; a < functionKeysBtn.length; a++)
            functionKeysBtn[a] = (Button) findViewById(functionKeysID[a]);
//
//        et_usernum.setOnKeyListener(this);
//        et_pass.setOnKeyListener(this);
//        btn_login.setOn

        setListener = new SetListener();

        setListener.setListener(new EditText[]{et_usernum,et_pass,et_regName,et_regPass},
                new ImageButton[]{},
                new Button[]{btn_cancel,btn_login,btn_register},
                new RadioButton[]{},
                new CheckBox[]{chk_admin},
                new Spinner[]{spn_regPosition},
                new CustomButtonForDrawableTop[]{},
                this);
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
        final AppCompatButton btnEnter = (AppCompatButton)
                alertLayout.findViewById(R.id.btnTerminalSubmit);
        final EditText etName = (AppCompatEditText)
                alertLayout.findViewById(R.id.etTerminalName);
        final EditText etSerial = (AppCompatEditText)
                alertLayout.findViewById(R.id.etTerminalSerial);

        alertTerminal = builder.create();
        alertTerminal.show();

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etName.getText().toString().equals("") ||
                        etSerial.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please fill all fields.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    db_data.addTerminal(etName.getText().toString(), etSerial.getText().toString());
                    alertTerminal.dismiss();
                    finish();
                }
            }
        });
    }

    int checkZReadPending(String muser) {
        int mStatus = db_data.checkForPendingZRead();
        switch (mStatus) {
            case 0:
                Intent myIntent = new Intent(getApplicationContext(), Cashier.class);
                myIntent.putExtra("CashNum", muser);
                et_usernum.setText("");
                et_pass.setText("");
                startActivity(myIntent);
                Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                userAuthZreport(muser);
                break;
//                loginBaKamo();
        }
        return 0;
    }

    void loginBaKamo() {
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
                Toast.makeText(MainActivity.this, "Incorrect ID number/Password!",
                        Toast.LENGTH_SHORT).show();
                et_pass.setText("");
            }
        } else {
            int result = db_data.cashierLogin(muser, mpass);
            if (result > 0) {
//                Intent myIntent = new Intent(getApplicationContext(), Cashier.class);
                //PASS INDEX
                checkZReadPending(muser);
//                myIntent.putExtra("CashNum", muser);
//                et_usernum.setText("");
//                et_pass.setText("");
//                Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
//                startActivity(myIntent);
            } else {
                Toast.makeText(this, "Incorrect ID number/Password!", Toast.LENGTH_SHORT).show();
                et_pass.setText("");
            }
        }
    }

    void userAuthZreport(final String muser) {
        AlertDialog.Builder authenticateBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_login, null);
        authenticateBuilder.setView(alertLayout);

        final AppCompatButton btnEnterAuthenticate = (AppCompatButton)
                alertLayout.findViewById(R.id.btnEnterAuthentication);
        final AppCompatEditText etUsername = (AppCompatEditText)
                alertLayout.findViewById(R.id.etUsername);
        final AppCompatEditText etPassword = (AppCompatEditText)
                alertLayout.findViewById(R.id.etPassword);

        final AlertDialog alertAuthenticate = authenticateBuilder.create();
        alertAuthenticate.show();
        btnEnterAuthenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int authen = db_data.cashierLogin(etUsername.getText().toString(),
                            etPassword.getText().toString());
                    if (authen >= 1) {
                        if (db_data.getTheCashierLevel(
                                etUsername.getText().toString()).equals("Manager") ||
                                db_data.getTheCashierLevel(
                                        etUsername.getText().toString()).equals("Supervisor")) {
                            alertAuthenticate.dismiss();
                            zReportBaKamo(etUsername.getText().toString(), muser);
                            unLockCashBox();
                        } else {
                            Toast.makeText(getApplicationContext(), "Unauthorized account.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        alertAuthenticate.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void zReportBaKamo(final String userNum, final String muser) {
        final SQLiteDatabase dbReader = db_data.getReadableDatabase();
        final SQLiteDatabase dbWriter = db_data.getWritableDatabase();
        final ReportBaKamo reportBaKamo = new ReportBaKamo();

        AlertDialog.Builder zReportBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_zreport, null);
        zReportBuilder.setView(alertLayout);

        final AppCompatButton btnEnterZreport = (AppCompatButton)
                alertLayout.findViewById(R.id.btnCashDrawerSubmitZ);
        final AppCompatEditText etTotalCashDrawerZ = (AppCompatEditText)
                alertLayout.findViewById(R.id.etTotalCashDrawerZ);

        final AlertDialog alertZreport = zReportBuilder.create();
        alertZreport.show();

        btnEnterZreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Double enteredCashDrawer = 0.0;
                    if (etTotalCashDrawerZ.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Please Fill all Fields.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        enteredCashDrawer = Double.parseDouble(etTotalCashDrawerZ.getText()
                                .toString());
                        alertZreport.dismiss();
                    }

                    String mTransType = "zreport";

                    int bcd;
                    db_data.addTransaction(mTransType, getCurrentDate(), userNum, 0, 0, "");
                    String[] itemID = new String[]{_ID, COLUMN_TRANSACTION_TYPE};
                    Cursor cursor1 = dbReader.query(TABLE_TRANSACTION, itemID, null, null, null,
                            null, null);
                    cursor1.moveToLast();
                    bcd = cursor1.getInt(0); //COLUMN _ID of TABLE_TRANSACTION
                    cursor1.close();
                    reportBaKamo.setDb_data(db_data);
                    reportBaKamo.main("no", getCurrentDate(), bcd, enteredCashDrawer);
                    ArrayList<String> paPrintNaman;
                    paPrintNaman = reportBaKamo.getPrintArray();
                    printFunction(paPrintNaman);
                    String[] printBaKamo = paPrintNaman.toArray(new String[paPrintNaman.size()]);
                    String printBaHanapMo = "";
                    for (int p = 0; p < paPrintNaman.size(); p++) {
                        printBaHanapMo = printBaHanapMo + "\n" + printBaKamo[p];
                    }
                    db_data.givePrintTransaction(bcd + "", printBaHanapMo);

//            retrievedCursorFromJoinTable.close();

                    //EXPORT TO CSV
                    //Cursor retrievedCursorFromReportBaKaMo = reportBaKamo.getCursorInReportBaKamo();
                    Cursor retrievedCursorFromJoinTable;
                    Cursor cursorDummy = null;

                    String joinTableQuery = "SELECT c1." + _ID + " as ID, " +
                            "c1." + COLUMN_PRODUCT_ID + " as CODE," +
                            "c1." + COLUMN_PRODUCT_NAME + " as ITEM," +
                            "c2." + COLUMN_PRODUCT_QUANTITY_TEMP + " as BEGINNING," +
                            "SUM(c3." + COLUMN_ITEM_QUANTITY + ") as SALES," +
                            "c1." + COLUMN_PRODUCT_QUANTITY +
                            " as ENDING FROM " + TABLE_PRODUCT + " c1 " +
                            " INNER JOIN " + TABLE_PRODUCT_TEMP + " c2 " +
                            " ON " + "c1." + COLUMN_PRODUCT_ID + "= c2." + COLUMN_PRODUCT_ID_TEMP +
                            " LEFT JOIN " + TABLE_ITEM + " c3 " +
                            " ON " + "c1." + COLUMN_PRODUCT_ID + "= c3." + COLUMN_ITEM_PRODUCT +
                            " AND c3." + COLUMN_ITEM_ZREPORT + "= 0" +
                            " GROUP BY " + COLUMN_PRODUCT_ID + ";";
                    retrievedCursorFromJoinTable = dbReader.rawQuery(joinTableQuery, null);
                    ZreportExportFunction zreportExportFunction = new ZreportExportFunction();
                    zreportExportFunction.showDialogLoading(MainActivity.this,
                            retrievedCursorFromJoinTable, cursorDummy);
                    BackUpDatabase backUpDatabase = new BackUpDatabase();
                    backUpDatabase.setDb_data(db_data);
                    backUpDatabase.main(MainActivity.this);
//            boolean sent =
//            if(sent){
//                zreportExportFunction.closeDialog();
//            }
                    //END OF EXPORT CSV

                    db_data.updateTransactions("no");

                    //GETTING QUANTITY SALES
                    ContentValues cv = new ContentValues();
                    cv.put(COLUMN_ITEM_ZREPORT, 1);
                    String whereBaKamo = COLUMN_ITEM_ZREPORT + "= ?";
                    String[] WhereArgBaKamo = {"0"};
                    dbWriter.update(TABLE_ITEM, cv, whereBaKamo, WhereArgBaKamo);
                    //END OF QUANTITY SALES
                    paPrintNaman.clear();
                    Intent myIntent = new Intent(getApplicationContext(), Cashier.class);
                    myIntent.putExtra("CashNum", muser);
                    et_usernum.setText("");
                    et_pass.setText("");
                    startActivity(myIntent);
                    Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void printFunction(ArrayList<String> list) {
        JmPrinter mPrinter = new JmPrinter();
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s);
            sb.append("\n");
        }
        String convertedArray = sb.toString();

        byte[] SData;
        try {
            SData = convertedArray.getBytes("UTF-8");
            boolean retnVale = mPrinter.PrintText(SData);

            if (!retnVale) {
                Toast.makeText(MainActivity.this, mPrinter.GetLastPrintErr(),
                        Toast.LENGTH_SHORT).show();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MMM-dd-yyyy HH:mm", Locale.ENGLISH);
        c.add(Calendar.DATE, -1);
        return dateTimeFormat.format(c.getTime());
    }

    private void setCallRegister(){
        flexNiLogin.setVisibility(View.GONE);
        layout_signup.setVisibility(View.VISIBLE);
        o = 1;
        functionKeysMode = o;
        refreshFunctionKeys();
    }
    private void setCancel(){
        layout_signup.setVisibility(View.GONE);
        flexNiLogin.setVisibility(View.VISIBLE);
        et_regName.setText("");
        et_regUsernum.setText("");
        et_regPass.setText("");
        spn_regPosition.setSelection(0);
        et_usernum.setText("");
        et_pass.setText("");
        o = 0;
        functionKeysMode = o;
        refreshFunctionKeys();
    }
    private void setRegister(){
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
            Toast.makeText(MainActivity.this, "Registration successful!",
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshFunctionKeys() {
        for (int i = 0; i < functionKeysBtn.length; i++) {
            functionKeysBtn[i].setText(functionKeys[functionKeysMode][i]);
        }
    }

    private void functionKeysDo(int functionKeysCode){
        switch(functionKeysMode){
            case 0:
                switch (functionKeysCode){
                    case 0:
                        loginBaKamo();
                        Toast.makeText(this,"LogIn",Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        if (chk_admin.isChecked()) {
                            chk_admin.setChecked(false);
                            break;
                        }
                        else {
                            chk_admin.setChecked(true);
                            break;
                        }
//                        Toast.makeText(this,"Admin",Toast.LENGTH_LONG).show();
                    case 2:
                        setCallRegister();
                        functionKeysMode = 1;
                        refreshFunctionKeys();
                        Toast.makeText(this,"SignUp",Toast.LENGTH_LONG).show();
                        break;
                }
                break;
            case 1:
                switch (functionKeysCode){
                    case 0:
                        setRegister();
                        Toast.makeText(this,"Register",Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        functionKeysMode = 0;
                        refreshFunctionKeys();
                        setCancel();
                        Toast.makeText(this,"Cancel",Toast.LENGTH_LONG).show();
                        break;
                }
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_F1:
                functionKeysDo(0);
                return true;
            case KeyEvent.KEYCODE_F2:
                functionKeysDo(1);
                return true;
            case KeyEvent.KEYCODE_F3:
                functionKeysDo(2);
                return true;
            case KeyEvent.KEYCODE_F4:
                functionKeysDo(3);
                return true;
            case KeyEvent.KEYCODE_F5:
                functionKeysDo(4);
                return true;
            case KeyEvent.KEYCODE_F6:
                functionKeysDo(5);
                return true;
            case KeyEvent.KEYCODE_F7:
                functionKeysDo(6);
                return true;
            case KeyEvent.KEYCODE_F8:
                functionKeysDo(7);
                return true;
            case KeyEvent.KEYCODE_F9:
                functionKeysDo(8);
                return true;
            case KeyEvent.KEYCODE_F10:
                functionKeysDo(9);
                return true;
            case KeyEvent.KEYCODE_F11:
                functionKeysDo(10);
                return true;
            case KeyEvent.KEYCODE_F12:
                functionKeysDo(11);
                return true;
        }
//        switch(functionKeysMode){
//            case 0:
//                switch (keyCode){
//                    case KeyEvent.KEYCODE_F1:
//                        loginBaKamo();
//                        Toast.makeText(this,"LogIn",Toast.LENGTH_LONG).show();
//                        break;
//                    case KeyEvent.KEYCODE_F2:
//                        if (chk_admin.isChecked()){
//                            chk_admin.setChecked(false);
//                            break;
//                        }
//                        else {
//                            chk_admin.setChecked(true);
//                            break;
//                        }
////                        Toast.makeText(this,"Admin",Toast.LENGTH_LONG).show();
//                    case KeyEvent.KEYCODE_F3:
//                        setCallRegister();
//                        functionKeysMode = 1;
//                        refreshFunctionKeys();
//                        Toast.makeText(this,"SignUp",Toast.LENGTH_LONG).show();
//                        break;
//                }
//                break;
//            case 1:
//                switch (keyCode){
//                    case KeyEvent.KEYCODE_F1:
//                        setRegister();
//                        Toast.makeText(this,"Register",Toast.LENGTH_LONG).show();
//                        break;
//                    case KeyEvent.KEYCODE_F2:
//                        functionKeysMode = 0;
//                        refreshFunctionKeys();
//                        setCancel();
//                        Toast.makeText(this,"Cancel",Toast.LENGTH_LONG).show();
//                        break;
//                }
//                break;
//        }
        Toast.makeText(this,keyCode+" is pressed.",Toast.LENGTH_SHORT).show();
        return false;
    }

    //Fragments
    public static class FragmentSignUp extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_signup, container, false);
        }
    }
    public static class FragmentFunctionKeys extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_function, container, false);
        }
    }
}