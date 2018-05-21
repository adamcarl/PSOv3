package com.example.sydney.psov3;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sydney.psov3.POJO.FunctionCall;
import com.example.sydney.psov3.POJO.Invoice;
import com.example.sydney.psov3.POJO.SetListener;
import com.example.sydney.psov3.adapter.AdapterOrder;
import com.google.android.flexbox.FlexboxLayout;
import com.jolimark.JmPrinter;
import com.jolimark.UsbPrinter;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import tw.com.prolific.driver.pl2303.PL2303Driver;

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
import static com.example.sydney.psov3.Constants.COLUMN_TEMP_DESCRIPTION;
import static com.example.sydney.psov3.Constants.COLUMN_TEMP_ID;
import static com.example.sydney.psov3.Constants.COLUMN_TEMP_NAME;
import static com.example.sydney.psov3.Constants.COLUMN_TEMP_PRICE;
import static com.example.sydney.psov3.Constants.COLUMN_TEMP_QUANTITY;
import static com.example.sydney.psov3.Constants.COLUMN_TEMP_TOTALPRICE;
import static com.example.sydney.psov3.Constants.COLUMN_TRANSACTION_TYPE;
import static com.example.sydney.psov3.Constants.HEADER;
import static com.example.sydney.psov3.Constants.TABLE_INVOICE;
import static com.example.sydney.psov3.Constants.TABLE_ITEM;
import static com.example.sydney.psov3.Constants.TABLE_PRODUCT;
import static com.example.sydney.psov3.Constants.TABLE_PRODUCT_TEMP;
import static com.example.sydney.psov3.Constants.TABLE_TEMP_INVOICING;
import static com.example.sydney.psov3.Constants.TABLE_TRANSACTION;
import static com.example.sydney.psov3.Constants._ID;
import static com.example.sydney.psov3.Constants.functionKeys;
import static com.example.sydney.psov3.Constants.functionKeysID;
import static com.example.sydney.psov3.POJO.FunctionCall.unLockCashBox;

public class Cashier extends AppCompatActivity implements View.OnKeyListener {
    private static final String ACTION_USB_PERMISSION =
            "com.prolific.pl2303hxdsimpletest.USB_PERMISSION";
    private static final boolean SHOW_DEBUG = true;

    //TO SUPPORT VECTOR DRAWABLES
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    ArrayList<String> itemCode123 = new ArrayList<>();
    ArrayList<String> itemQuan123 = new ArrayList<>();
    Cursor cursor;
    DB_Data db_data;
    SetListener setListener;
    BackUpDatabase backUpDatabase;
    EditText txt_search, txt_cash, txt_dummy;
    String itemnameCol, itemdescCol, formatted, vat2, vattable2, subTotal2, due2;
    //    ArrayList<Integer>itemQuantityList = new ArrayList<>();
    ArrayList<Double>itemPriceList = new ArrayList<>();
    ArrayList<String>itemNameList = new ArrayList<>();
    ArrayList<String>itemDescList = new ArrayList<>();
    ArrayList<Double> total = new ArrayList<>();
    RelativeLayout layout;

    ArrayList<String> productsHeader = new ArrayList<>();
    ArrayList<String> products = new ArrayList<>();
    ArrayList<String> productsFooter = new ArrayList<>();
    ArrayList<String> productsSpace = new ArrayList<>();

    SQLiteDatabase dbReader, dbWriter;
    TabHost tab_host;
    TextView lbl_sub,lbl_tax,lbl_total,lbl_due,lbl_dc,lbl_discount;
    Button btn_print, btn_cashier_confirmDelete, btn_cashier_cancelRefund, btnCreditCard, btnSetQuantity;
    ImageButton btn_cashier_delete, btn_cashier_refund;
    ImageView img_receipt;
    RadioButton rb_ndisc,rb_spdisc,rb_ddisc;
    RadioGroup rg_discount;
    List<List<String>> t2Rows = new ArrayList<>();
    ArrayList<Order> orderArrayList;
    AdapterOrder adapterOrder=null;
    String dateformatted, transType;
    ReportBaKamo reportBaKamo;
    boolean isOn, cashIn, refund_status;

    FlexboxLayout flexNiAdminPrivileges;
    CustomButtonForDrawableTop btn_customX, btn_customZ, btn_customCash, btn_customLog;

    //Dialog for Enter the Quantity
    AlertDialog.Builder builder, creditBuilder, debitBuilder, authenticateBuilder,
            cashinoutBuilder, zReportBuilder, xReportBuilder, productNotFoundBuilder,
            addProductNotFoundBuilder;

    AlertDialog alertQuantity, alertCredit, alertDebit, alertAuthenticate, alertCashinout,
            alertZreport, alertXreport, alertProductNotFound, alertAddProductNotFound;

    double mPriceTotal, mVattable, mTax, mTotalDiscount, mTotal, mDue, mTaxPercent, totalPayment,
            parsed, enteredCashDrawer;
    String userNum;
    String TAG = "PL2303HXD_APLog";
    PL2303Driver mSerial;
    //Variables for txt in Payment Info
    ZreportExportFunction zreportExportFunction;
    InvoiceAdapter invoiceAdapter = null;
    AppCompatEditText etQuan, etCreditBank, etCreditNumber, etCreditExpiry, etPassword, etUsername, etDebitBank, etDebitNumber, etDebitExpiry;
    String creditBank, creditNumber, creditExpiry, debitBank, debitNumber, debitExpiry;

    int transNumber, tenderCashStatus, tenderCreditStatus, tenderDebitStatus, tenderDiscountStatus,
            tenderGiftStatus, tenderRnEStatus;

    double tenderCashAmount, tenderCreditAmount, tenderDebitAmount, tenderDiscountAmount,
            tenderGiftAmount, tenderRnEAmount, vattable, vat, subTotal, itempriceCol,
            itempricetotalCol, discount, due;

    AppCompatEditText etTotalCashDrawerZ = null;
    AppCompatEditText etTotalCashDrawerX = null;
    DecimalFormat moneyDecimal = new DecimalFormat("0.00");
    private int quantityCount = 0, discType = 0, dialogVar;
    private String code = "";
    private List<InvoiceItem> invoiceItemList;
    private RecyclerView recyclerView;
    private GridLayoutManager mLayoutManager;
    private Double dCustomerCash, change;
    private Double inCustomer;
    private String inPrint = ""; //NULL FOR THE MEANTIME
    private String inZreport = ""; //NULL FOR THE MEANTIME
    private String inXreport = ""; //NULL FOR THE MEANTIME
    private Double inVattable;
    private Double inVatted;
    private String inVatStatus = "";
    private Double inSeniorDiscount;
    private Double inVatExempt;
    private Double inZeroRated;
    private Double inCreditCardNum;
    private Double inCreditExpiration;
    //JMPRINTER VARIABLES
    private JmPrinter mPrinter;
    private UsbPrinter marksPrinter = new UsbPrinter();
    private PL2303Driver.BaudRate mBaudrate = PL2303Driver.BaudRate.B9600;
    private FunctionCall fuc = new FunctionCall();

    private Button[] functionKeysBtn = new Button[12];
    private int functionKeysMode;

    protected void onCreate(Bundle savedInstanceState) {

        db_data = new DB_Data(this);
        setListener =  new SetListener();
        backUpDatabase = new BackUpDatabase();

        dbReader = db_data.getReadableDatabase();
        dbWriter = db_data.getWritableDatabase();
        reportBaKamo = new ReportBaKamo();

        printerDetection();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier);
        init(); //INITALIZATION OF VIEWS

        mPrinter = new JmPrinter();//Create a 78M printer object

        Intent intent = getIntent();
        userNum = intent.getExtras().getString("CashNum");

        mSerial = new PL2303Driver((UsbManager) getSystemService(Context.USB_SERVICE),
                this, ACTION_USB_PERMISSION);
        mBaudrate =PL2303Driver.BaudRate.B19200;
        if (!mSerial.PL2303USBFeatureSupported()) {
            Toast.makeText(this, "No Support USB host API", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "No Support USB host API");
            mSerial = null;
        }

        if( !mSerial.enumerate() ) {
            Toast.makeText(this, "no more devices found", Toast.LENGTH_SHORT).show();
        }

        try {
//            Thread.sleep(1500);
            openUsbSerial();
        } catch (Exception e) {
            e.printStackTrace();
        }
        functionKeysMode = 2;
        refreshFunctionKeys();

        for (int a = 0; a < HEADER.length; a++) {
            productsHeader.add(HEADER[a]);
        }
        for (int a = 0; a < 6; a++) {
            productsSpace.add(" ");
        }

        writeDataToSerial("ABZTRAK INC.", "Tinda-PoS Android", "");
        discount = 0.0;
        mTaxPercent = .12;

        tab_host.setup();
        orderArrayList = new ArrayList<>();
        adapterOrder = new AdapterOrder(this, R.layout.single_order, orderArrayList);

        //RECYCLERVIEW INITIALIZATION
        refreshRecyclerView();

        //Tab 1
        TabHost.TabSpec spec = tab_host.newTabSpec("Invoice");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Invoice");
        tab_host.addTab(spec);

        //Tab 2
        spec = tab_host.newTabSpec("Payment");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Payment");
        tab_host.addTab(spec);

        //Tab 3
        spec = tab_host.newTabSpec("Shift");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Shift");
        tab_host.addTab(spec);

        tab_host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {
                int i = tab_host.getCurrentTab();
                Log.i("@@@@@@@@ ANN CLICK TAB NUMBER", "------" + i);

                if (i == 0) {
                    Log.i("@@@@@@@@@@ Inside onClick tab 0", "onClick tab");
                    functionKeysMode = 2;
                    refreshFunctionKeys();
                }
                else if (i ==1) {
                    Log.i("@@@@@@@@@@ Inside onClick tab 1", "onClick tab");
                    functionKeysMode = 3;
                    refreshFunctionKeys();
                }
                else{
                    functionKeysMode = 6;
                    refreshFunctionKeys();
                }

            }
        });

        dbWriter.execSQL(
                "INSERT INTO sessions(time,date,username) VALUES(time('now'),date('now'),'" +
                        userNum + "') ");

        txt_cash.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (!s.toString().equals(current)) {
                        txt_cash.removeTextChangedListener(this);

                        String cleanString = s.toString().replaceAll("[P,.]", "");

                        parsed = Double.parseDouble(cleanString);
                        formatted = moneyDecimal.format(parsed / 100);
                        current = formatted;
                        txt_cash.setText(formatted);
                        txt_cash.setSelection(formatted.length());

                        txt_cash.addTextChangedListener(this);

//                        if (mDue < 0 ) {
//                            btn_print.setEnabled(false);
//                        } else if (mDue >= 0) {
//                            btn_print.setEnabled(true);
//                            btn_print.setText("" + "Print Receipt" + "");
//                            lbl_dc.setText("" + "Change" + "");
//                        }
//
//                        refreshPaymentInformation();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        rg_discount.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(rb_spdisc.isChecked()){
                    //SENIOR DISCOUNT
                    discType = 1;

                    //SUPPLIER FOR addInvoice(. . .)
                    inVattable = vattable;
                    inVatted = 0.0;
                    inVatStatus = "x";
                    inSeniorDiscount = discount;
                    inVatExempt = 0.0;
                    inZeroRated = 0.0;

                    discount = 0.20;
                    mTaxPercent = 0;

                    refreshPaymentInformation();
                }
                else if(rb_ddisc.isChecked()){
                    //DIPLOMAT DISCOUNT
                    discType=2;

                    discount = 0.0;
                    mTaxPercent = 0;

                    //SUPPLIER FOR addInvoice(. . .)
                    inVattable = vattable;
                    inVatted = 0.0;
                    inVatStatus = "z";
                    inSeniorDiscount = 0.0;
                    inVatExempt = 0.0;
                    inZeroRated = 0.0;

                    mTaxPercent = 0;
                    discount = 0.0;
                    refreshPaymentInformation();

                }
                else{
                    //NO DISCOUNT

                    //SUPPLIER FOR addInvoice(. . .)
                    inVattable = vattable;
                    inVatted = vat;
                    inVatStatus = "v";
                    inSeniorDiscount = 0.0;
                    inVatExempt = 0.0;
                    inZeroRated = 0.0;

                    discount = 0.0;
                    mTaxPercent = .12;
                    refreshPaymentInformation();
                }
            }
        });

        //code 0 =
        for (int i = 0; i < functionKeysBtn.length; i++) {
            final int functionKeysCode = i;
            functionKeysBtn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    functionKeysDo(functionKeysCode);
                    functionKeysBtn[functionKeysCode].setOnKeyListener(Cashier.this);
                }
            });
        }

        btnCreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTenderCredit();
            }
        });

        //Mark's onClickListeners for Button reports
        btn_cashier_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDelete();
            }
        });

        btn_cashier_refund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRefund();
            }
        });

        btn_cashier_confirmDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDelete();
            }
        });

        btn_cashier_cancelRefund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unsetRefund();
            }
        });

        //START AUTO COMPUTE WHEN APP CLOSES
        if(invoiceAdapter.getItemCount() > 0){
            Log.e("InvoiceAdapter : ", invoiceAdapter.getItemCount()+"");
            refreshPaymentInformation();
        }
        //END AUTO COMPUTE WHEN APP CLOSE

        txt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                long mCode = txt_search.getText().length();
                if(mCode >= 13){
                    searchProduct();
                }
            }
        });

//        createMyDialog();
//        //CREATE DIALOG Z
//        alertDialogXreport = builder.create();
//        alertDialogZreport = builder.create();
//        alertDialogCredit = builder.create();
        zreportExportFunction = new ZreportExportFunction();
    }

    private List<InvoiceItem> fill_with_data() {
        invoiceItemList = new ArrayList<>();
        invoiceItemList.clear();

        SQLiteDatabase db = db_data.getReadableDatabase();
        String SELECT_QUERY = "SELECT * FROM " + TABLE_TEMP_INVOICING;

        Cursor cursor = db.rawQuery(SELECT_QUERY,null);

        String invoiceItemName,invoiceItemDescription,invoiceItemID;
        double invoiceItemPrice,invoiceItemTotal;
        int invoiceItemQuantity;

        while(cursor.moveToNext()){
            invoiceItemName = cursor.getString(1);
            invoiceItemDescription = cursor.getString(2);
            invoiceItemPrice = cursor.getDouble(3);
            String dummyVattable = "";
            invoiceItemQuantity = cursor.getInt(4);
            invoiceItemID = cursor.getString(5);
            invoiceItemTotal = cursor.getDouble(6);
            invoiceItemList.add(new InvoiceItem(invoiceItemName, invoiceItemDescription,
                    invoiceItemPrice, dummyVattable, invoiceItemQuantity, invoiceItemID,
                    invoiceItemTotal));
        }
        cursor.close();
        return invoiceItemList;
    }

    //BUTTON SET QUANTITY ONCLICK
    public void setRegisterQuantity(View view) {
        setQuantity();
    }

    private void setQuantity(){
        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_enterquantity, null);
        builder.setView(alertLayout);
        final AppCompatButton btnEnter = (AppCompatButton) alertLayout.findViewById(R.id.btnEnter);
        etQuan = (AppCompatEditText) alertLayout.findViewById(R.id.etEnterQuantity);

        alertQuantity = builder.create();
        alertQuantity.show();

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etQuan.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter Quantity.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    btnSetQuantity.setText(etQuan.getText().toString());
                    alertQuantity.dismiss();
                }
            }
        });
    }

    private void setRefund(){
        btn_cashier_refund.setVisibility(View.INVISIBLE);
        btn_cashier_cancelRefund.setVisibility(View.VISIBLE);
        refund_status = true;
    }
    private void unsetRefund(){
        btn_cashier_cancelRefund.setVisibility(View.INVISIBLE);
        btn_cashier_refund.setVisibility(View.VISIBLE);
        refund_status = false;
    }
    private void setDelete(){
        if(mLayoutManager.getItemCount() == 0){
            btn_cashier_delete.setVisibility(View.VISIBLE);
            btn_cashier_confirmDelete.setVisibility(View.INVISIBLE);
            Toast.makeText(Cashier.this, "NO ITEMS!", Toast.LENGTH_SHORT).show();
        } else {
            btn_cashier_delete.setVisibility(View.INVISIBLE);
            btn_cashier_confirmDelete.setVisibility(View.VISIBLE);
        }
    }
    private void setPrint(){
        try {
            if (refund_status) transType = "refund";
            else transType = "invoice";
            db_data.addTransaction(transType, fuc.getCurrentDate(), userNum, 0, 0, "");
            String[] itemID = new String[]{_ID};
            Cursor cursor1 = dbReader.query(TABLE_TRANSACTION, itemID, null, null, null, null, null);
            cursor1.moveToLast();
            transNumber = cursor1.getInt(0); //COLUMN _ID of TABLE_TRANSACTION
            cursor1.close();

            //MARK : I UPDATED THIS PART FOR addInvoice discounted and so on---
            try{

                Invoice in = new Invoice();

                in.setInTrans(transNumber + "");
                in.setInPrint(inPrint);
                in.setInCashierNum(userNum);
                in.setInZreport("0");
                in.setInXreport("0");
                in.setInVatStatus(discType + "");
                in.setInDateAndTime(fuc.getCurrentDate());
                in.setInCreditCardNum(creditNumber);
                in.setInCreditExp(creditExpiry);

                if (refund_status) {
                    in.setInDisc("-" + moneyDecimal.format(mTotalDiscount));
                    in.setInCustomer("-" + moneyDecimal.format(mTotal));
                    in.setInVattable("-" + moneyDecimal.format(mVattable));
                    in.setInVatted(-mTax);
                    in.setInCreditSale(-tenderCreditAmount);
                } else {
                    in.setInDisc(moneyDecimal.format(mTotalDiscount));
                    in.setInCustomer(moneyDecimal.format(mTotal));
                    in.setInVattable(moneyDecimal.format(mVattable));
                    in.setInVatted(mTax);
                    in.setInCreditSale(tenderCreditAmount);
                }
                if (tenderCreditStatus == 1) {
                    db_data.addCredit(transNumber + "", userNum, fuc.getCurrentDate(),
                            tenderCreditAmount, creditBank, creditNumber, creditExpiry);
                }

                db_data.addInvoice(in);

                Log.e("AddInvoice : ", "trans#:" + transNumber + "|disc:" + moneyDecimal.format(mTotalDiscount) + "|subtotal:" +
                        moneyDecimal.format(mTotal) + "|print:" + inPrint + "|cashier:" + userNum +
                        "|vattable: " + moneyDecimal.format(mVattable) + "|getTax: " + moneyDecimal.format(mTax) + "|disc type:" +
                        discType + "|Date:" + fuc.getCurrentDate() + "");
            } catch(Exception e){
                e.printStackTrace();
            }

            //db_data.addInvoice(transNumber+"",discount.toString(),totalPrice.toString().replace(",",""),inPrint,userNum,"0","0",vattable2.replaceAll("[$P,]",""),Double.parseDouble(vat2),discType+"", 0.0,"","","");
            String[] SELECT_QUERY = new String[]{_ID};
            Cursor cursor = dbReader.query(TABLE_INVOICE, SELECT_QUERY, null, null, null, null, null);
            cursor.moveToLast();
            String abc = cursor.getString(0);

            Cursor receivedCursorFromTemp = db_data.selectAllTempInvoice();
            receivedCursorFromTemp.moveToFirst();

            String consecutive = String.format("%1$06d", transNumber);
            if (refund_status) products.add("ITEM REFUND");
            else products.add("CASH INVOICE");
            products.add("Date:\t" + fuc.getCurrentDate());
            products.add("TRANS#" + consecutive);
            products.add("-------------------------------");
            products.add("Quantity \t\t" + "Name\t\t" + "Price");
            try{
                while (!receivedCursorFromTemp.isAfterLast()){
                    db_data.addItem(
                            abc,
                            receivedCursorFromTemp.getString(receivedCursorFromTemp.getColumnIndex(COLUMN_TEMP_ID)),
                            receivedCursorFromTemp.getString(receivedCursorFromTemp.getColumnIndex(COLUMN_TEMP_QUANTITY)),
                            0,
                            receivedCursorFromTemp.getString(receivedCursorFromTemp.getColumnIndex(COLUMN_TEMP_NAME )),
                            receivedCursorFromTemp.getString(receivedCursorFromTemp.getColumnIndex(COLUMN_TEMP_DESCRIPTION)),
                            receivedCursorFromTemp.getDouble(receivedCursorFromTemp.getColumnIndex(COLUMN_TEMP_PRICE)),
                            userNum);
                    products.add("" + receivedCursorFromTemp.getString(receivedCursorFromTemp.getColumnIndex(COLUMN_TEMP_QUANTITY)) +
                            "\t" + receivedCursorFromTemp.getString(receivedCursorFromTemp.getColumnIndex(COLUMN_TEMP_NAME)) +
                            "\t" + receivedCursorFromTemp.getDouble(receivedCursorFromTemp.getColumnIndex(COLUMN_TEMP_PRICE)) * Double.parseDouble(receivedCursorFromTemp.getString(receivedCursorFromTemp.getColumnIndex(COLUMN_TEMP_QUANTITY))) + "");
                    int quanBaKamo = db_data.getQuantityofProducts(receivedCursorFromTemp.getString(receivedCursorFromTemp.getColumnIndex(COLUMN_TEMP_ID))) - receivedCursorFromTemp.getInt(receivedCursorFromTemp.getColumnIndex(COLUMN_TEMP_QUANTITY));
                    db_data.updateProductQuantity(receivedCursorFromTemp.getString(receivedCursorFromTemp.getColumnIndex(COLUMN_TEMP_ID)) ,quanBaKamo);
                    receivedCursorFromTemp.moveToNext();
                }
            } catch (Exception e){
                e.printStackTrace();
            }

            itemCode123.clear();
            itemQuan123.clear();
            itemNameList.clear();
            itemDescList.clear();
            itemPriceList.clear();

            products.add("-------------------------------");
            if (!refund_status)
                products.add("Invoice Number " + abc + "");
            products.add(quantityCount + " item(s)");

            products.add("Subtotal" + "" + "\t\t " + mPriceTotal);
            if (rb_spdisc.isChecked()) {
                products.add("Vat Exempt" + "" + "\t\t " + moneyDecimal.format(mPriceTotal - (mPriceTotal / 1.12)));
                products.add("Discount" + "" + "\t\t-" + moneyDecimal.format(mTotalDiscount));
                products.add("Amount Due:" + "" + "\t\t " + moneyDecimal.format((mPriceTotal / 1.12) - mTotalDiscount) + "\n");
                products.add("VAT Sales" + "" + "\t\t " + "0.00");
                products.add("VAT Exempt Sales" + "" + "\t\t " + moneyDecimal.format((mPriceTotal / 1.12) - mTotalDiscount));
                products.add("VAT Zero-rated Sales" + "" + "\t\t " + "00.0");
                products.add("12% VAT" + "\t\t " + "00.0");
            } else if (rb_ddisc.isChecked()) {
                products.add("Vat Exempt" + "" + "\t\t-" + moneyDecimal.format(mPriceTotal - (mPriceTotal / 1.12)));
                products.add("Discount" + "" + "\t\t-" + "0.00");
                products.add("Amount Due:" + "" + "\t\t " + moneyDecimal.format(mVattable) + "\n");
                products.add("VAT Sales" + "" + "\t\t " + "0.00");
                products.add("VAT Exempt Sales" + "" + "\t\t " + "0.00");
                products.add("VAT Zero-rated Sales" + " " + "\t\t" + moneyDecimal.format(mVattable));
                products.add("12% VAT" + "\t\t " + "0.00");
            } else {
                products.add("Vat Exempt" + "" + "\t\t-" + "0.00");
                products.add("Discount" + "" + "\t\t " + "0.00");
                products.add("Amount Due:" + "" + "\t\t " + mPriceTotal + "\n");
                products.add("VAT Sales" + "" + "\t\t " + moneyDecimal.format(mVattable));
                products.add("VAT Exempt Sales" + "" + "\t\t " + "0.00");
                products.add("VAT Zero-rated Sales" + "" + "\t\t " + "0.00");
                products.add("12% VAT" + "\t\t " + moneyDecimal.format(mTax));
            }
            //customerCash = txt_cash.getText().toString().replace(",", "");
//            double change = dCustomerCash - totalPrice;

//            products.add("\t\t\t\tCash" + "\t\t\t\t" + txt_cash.getText().toString());
//            products.add("\t\t\t\tCredit" + "\t\t\t\t" + creditPayment);
//            products.add("\n\n\n\n\n\n");
            if (tenderDiscountStatus == 1)
                products.add("Discount\t" + tenderDiscountAmount);

            if (tenderCashStatus == 1) {
                products.add("Cash\t" + tenderCashAmount);
            }
            if (tenderCreditStatus == 1) {
                products.add("Credit\t" + tenderCreditAmount);
            }
            if (tenderDebitStatus == 1) {
                products.add("Debit\t" + tenderDebitAmount);
            }
            if (tenderDiscountStatus == 1) {
                products.add("Debit\t" + tenderDebitAmount);
            }
            if (tenderGiftStatus == 1) {
                products.add("Gift Check\t" + tenderGiftAmount);
            }
            if (tenderRnEStatus == 1) {
                products.add("Others\t" + tenderRnEAmount);
            }
            if (!refund_status)
                products.add("Change " + moneyDecimal.format(mDue));

            //CHECK IF PRINTERS ARE OPEN
//            boolean ret = marksPrinter.Open();

            String[] printHeadBaKamo = productsHeader.toArray(new String[productsHeader.size()]);
            String[] printBaKamo = products.toArray(new String[products.size()]);
            String printBaHanapMo="";
            String mLine = "-------------------------------";
            String mStoreCopy = "STORE COPY";
            for (int p=0; p<products.size();p++){
                printBaHanapMo = printBaHanapMo +"\n"+ printBaKamo[p];
            }
            db_data.updateInvoice(abc,printBaHanapMo);
            //TODO create journar trail 1/9/2018
            fuc.writeJournalTrail(mStoreCopy + "\n" + printBaHanapMo + "\n" + mStoreCopy + "\n" + mLine);
            //TODO create image version of the receipt 1/15/2018
            fuc.createReceiptImage(abc, printHeadBaKamo, printBaKamo);

            if (invoiceAdapter.getItemCount() > 0 && totalPayment >= mDue) {
                //JOLLIMARK PRINTER
                unLockCashBox();
                printFunction(productsHeader);
                printFunction(products);
                printFunction(productsSpace);
//                printFunction(productsFooter);
                btn_print.setEnabled(false);
                products.clear();
            }
            db_data.givePrintTransaction(transNumber + "", printBaHanapMo);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//                itemQuantityList.clear();
            itemPriceList.clear();
            itemNameList.clear();
            btn_cashier_cancelRefund.setVisibility(View.INVISIBLE);
            btn_cashier_refund.setVisibility(View.VISIBLE);
            refund_status = false;
            tab_host.setCurrentTab(0);
            functionKeysMode = 2;
            refreshFunctionKeys();
//                itemCodeList.clear();
        }
        cancelna();
        btn_print.setEnabled(false);
        writeDataToSerial("THANK YOU!", "COME AGAIN!", "");
    }
    private void confirmDelete(){
        for(InvoiceItem item : invoiceItemList){
            if(item.isSelected()){
//                        int result = db_data.searchSelectedItem(item.isSelected() + "");
                db_data.deleteTempItemInvoice(item.getInvoiceProductID());
                btn_cashier_confirmDelete.setVisibility(View.INVISIBLE);
                btn_cashier_delete.setVisibility(View.VISIBLE);

                refreshRecyclerView();

                //START
                if(invoiceAdapter.getItemCount() > 0){
                    //START OF COMPUTATION UPPER
                    refreshPaymentInformation();
                    Toast.makeText(Cashier.this, "ITEM DELETED!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    isOn = false;
                    total.clear();
                    cancelna();
                }
                //END
            }
        }
    }
    private void setCancel(){
        try {
            if (db_data.getTheCashierLevel(userNum).equals("Cashier")) {
                authenticateBuilder = new AlertDialog.Builder(this);
                LayoutInflater inflater = getLayoutInflater();
                final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_login, null);
                authenticateBuilder.setView(alertLayout);

                final AppCompatButton btnEnterAuthenticate = (AppCompatButton) alertLayout.findViewById(R.id.btnEnterAuthentication);
                etUsername = (AppCompatEditText) alertLayout.findViewById(R.id.etUsername);
                etPassword = (AppCompatEditText) alertLayout.findViewById(R.id.etPassword);

                alertAuthenticate = authenticateBuilder.create();
                alertAuthenticate.show();
                btnEnterAuthenticate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            int authen = db_data.cashierLogin(etUsername.getText().toString(), etPassword.getText().toString());
                            if (authen >= 1) {
                                if (db_data.getTheCashierLevel(etUsername.getText().toString()).equals("Manager") || db_data.getTheCashierLevel(etUsername.getText().toString()).equals("Supervisor")) {
                                    alertAuthenticate.dismiss();
                                    transType = "cancel";
                                    db_data.addTransaction(transType, fuc.getCurrentDate(), userNum, 0, 0, "");

                                    cancelna();
                                    db_data.deleteAllTempItemInvoice(); //DELETE ALL TEMP ITEMS
                                    refreshRecyclerView();
                                    t2Rows.clear();
                                    products.clear();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Unauthorized account.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                                alertAuthenticate.dismiss();
                            }
                        } catch (Exception e) {

                        }
                    }
                });
            } else {
                transType = "cancel";
                db_data.addTransaction(transType, fuc.getCurrentDate(), userNum, 0, 0, "");
                db_data.deleteAllTempItemInvoice(); //DELETE ALL TEMP ITEMS
                refreshRecyclerView();
                cancelna();
                t2Rows.clear();
                products.clear();
                productsHeader.clear();
//                        productsFooter.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setTenderCash(){
        if (fuc.getTxtCashDouble(txt_cash) == 0.0) {
            Toast.makeText(getApplicationContext(), "Please enter amount", Toast.LENGTH_SHORT).show();
        } else {
            tenderCashAmount = fuc.getTxtCashDouble(txt_cash);
            tenderCashStatus = 1;
            refreshPaymentInformation();
            txt_cash.setText("" + "P0.00" + "");

        }
    }
    private void setTenderDebit(){
        try {
            debitBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_debitcard, null);
            debitBuilder.setView(alertLayout);
            final AppCompatButton btnEnterDebit = (AppCompatButton) alertLayout.findViewById(R.id.btnEnterDebit);
            etDebitBank = (AppCompatEditText) alertLayout.findViewById(R.id.etDebitBank);
            etDebitNumber = (AppCompatEditText) alertLayout.findViewById(R.id.etDebitNumber);
            etDebitExpiry = (AppCompatEditText) alertLayout.findViewById(R.id.etDebitExpiry);

            alertDebit = debitBuilder.create();
            alertDebit.show();

            btnEnterDebit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (etDebitBank.getText().toString().equals("") || etDebitNumber.getText().toString().equals("") || etDebitExpiry.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Please Fill all Fields.", Toast.LENGTH_SHORT).show();
                    } else if (fuc.getTxtCashDouble(txt_cash) == 0.0) {
                        Toast.makeText(getApplicationContext(), "Please enter amount", Toast.LENGTH_SHORT).show();
                    } else {
                        tenderDebitAmount = fuc.getTxtCashDouble(txt_cash);
                        debitBank = etDebitBank.getText().toString();
                        debitNumber = etDebitNumber.getText().toString();
                        debitExpiry = etDebitExpiry.getText().toString();
                        tenderDebitStatus = 1;
                        txt_cash.setText("" + "P0.00" + "");
                        refreshPaymentInformation();
                        alertDebit.dismiss();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setTenderDiscount(){
        if (fuc.getTxtCashDouble(txt_cash) == 0.0) {
            Toast.makeText(getApplicationContext(), "Please enter amount", Toast.LENGTH_SHORT).show();
        } else {
            tenderDiscountAmount = fuc.getTxtCashDouble(txt_cash);
            tenderDiscountStatus = 1;
            refreshPaymentInformation();
            txt_cash.setText("" + "P0.00" + "");
        }
    }
    private void setTenderGift(){
        if (fuc.getTxtCashDouble(txt_cash) == 0.0) {
            Toast.makeText(getApplicationContext(), "Please enter amount", Toast.LENGTH_SHORT).show();
        } else {
            tenderGiftAmount = fuc.getTxtCashDouble(txt_cash);
            tenderGiftStatus = 1;
            refreshPaymentInformation();
            txt_cash.setText("" + "P0.00" + "");
        }
    }
    private void setXReport(){
        xReportBaKamo();
        unLockCashBox();
    }
    private void setZReport(){
        backUpDatabase.setDb_data(db_data);
        try {
            if (db_data.getTheCashierLevel(userNum).equals("Cashier")) {
                userAuthZreport();

            } else {
                zReportBaKamo();
                unLockCashBox();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setCashInOut(){
        if (db_data.getTheCashierLevel(userNum).equals("Cashier")) {
            //AUTHENTICATE FIRST
            authenticateBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_login, null);
            authenticateBuilder.setView(alertLayout);

            final AppCompatButton btnEnterAuthenticate = (AppCompatButton) alertLayout.findViewById(R.id.btnEnterAuthentication);
            etUsername = (AppCompatEditText) alertLayout.findViewById(R.id.etUsername);
            etPassword = (AppCompatEditText) alertLayout.findViewById(R.id.etPassword);

            alertAuthenticate = authenticateBuilder.create();
            alertAuthenticate.show();
            btnEnterAuthenticate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int authen = db_data.cashierLogin(etUsername.getText().toString(), etPassword.getText().toString());
                    if (authen >= 1) {
                        if (db_data.getTheCashierLevel(etUsername.getText().toString()).equals("Manager") || db_data.getTheCashierLevel(etUsername.getText().toString()).equals("Supervisor")) {
                            alertAuthenticate.dismiss();
                            showDialogCashInOut();
                        } else {
                            Toast.makeText(getApplicationContext(), "Unauthorized account.", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        alertAuthenticate.dismiss();
                    }
                }
            });
        } else {
            showDialogCashInOut();
        }
    }
    private void setLogOut(){
        cancelna();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("MMM-dd-yyyy HH:mm", Locale.SIMPLIFIED_CHINESE);
        dateformatted = dateformat.format(c.getTime());
        dbWriter.execSQL("INSERT INTO sessions(time,date,username) VALUES(time('now'),date('now'),'" + userNum + "') ");
        finish();
    }

    public void searchProduct() {
        try {
            code = txt_search.getText().toString();
            final String[] itemcode = {code};
            itemCode123.add(code);
            String[] WHERE = {COLUMN_PRODUCT_ID, COLUMN_PRODUCT_NAME, COLUMN_PRODUCT_DESCRIPTION,
                    COLUMN_PRODUCT_QUANTITY, COLUMN_PRODUCT_PRICE};
            cursor = dbReader.query(TABLE_PRODUCT, WHERE, COLUMN_PRODUCT_ID + " LIKE ?", itemcode,
                    null, null, null);
            cursor.moveToFirst();
            int rows = cursor.getCount();
            if (rows > 0) {
//                alertQuantity = builder.create();
//                alertQuantity.show();

//                btnEnter.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
                try {
                    if (btnSetQuantity.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Please Enter Quantity.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        dialogVar = Integer.parseInt(btnSetQuantity.getText().toString());

                        //END OF SUPPLIER FOR ADD PRODUCTS

                        if (invoiceItemList != null) {
                            //MARK'S SOLUTION FOR TEMPORARY INVOICING ITEMS
                            //INSERT TEMP INVOICE ITEMS INTO TABLE
                            try {
                                InvoiceItem invoiceItem = new InvoiceItem();

                                String convertedCode = code.trim();
                                Double prodPrice = cursor.getDouble(cursor.getColumnIndex(
                                        COLUMN_PRODUCT_PRICE));
                                String prodName = cursor.getString(cursor.getColumnIndex(
                                        COLUMN_PRODUCT_NAME));
                                String prodDes = cursor.getString(cursor.getColumnIndex(
                                        COLUMN_PRODUCT_DESCRIPTION));
                                String prodId = cursor.getString(cursor.getColumnIndex(
                                        COLUMN_PRODUCT_ID));

                                int result = db_data.searchDuplicateInvoice(convertedCode);
                                if (result > 0) {
                                    SQLiteDatabase db = db_data.getReadableDatabase();
                                    String SELECT_QUERY = "SELECT " + COLUMN_TEMP_QUANTITY +
                                            " , " + COLUMN_TEMP_TOTALPRICE + " , " +
                                            COLUMN_TEMP_PRICE + " FROM " + TABLE_TEMP_INVOICING +
                                            " WHERE " + COLUMN_TEMP_ID + "='" + convertedCode + "'";
                                    Cursor cursor = db.rawQuery(SELECT_QUERY, null);
                                    cursor.moveToFirst();
                                    int retrievedQuantity = cursor.getInt(0);
                                    double retrievedTotalPrice = cursor.getDouble(1);
                                    double retrievedProce = cursor.getDouble(2);
                                    double totalNaTalaga = retrievedTotalPrice + (retrievedProce *
                                            dialogVar);
                                    cursor.close();

//                                            double marksTotal = db_data.totalPr   ice();
                                    db_data.updateInvoiceItem(convertedCode, retrievedQuantity +
                                            dialogVar, totalNaTalaga);
                                    Toast.makeText(Cashier.this, "Quantity Updated!",
                                            Toast.LENGTH_SHORT).show();
                                } else if (result == 0) { //IF DOESN'T HAVE DUPLICATE
                                    invoiceItem.setInvoiceProductName(prodName);
                                    invoiceItem.setInvoiceProductDescription(prodDes);
                                    invoiceItem.setInvoiceProductPrice(prodPrice);//toAdd item
                                    invoiceItem.setInvoiceProductQuantity(dialogVar);//toAdd item
                                    invoiceItem.setInvoiceProductID(prodId);//toAdd item
                                    invoiceItem.setInvoiceProductTotal(prodPrice * dialogVar);

                                    db_data.insertTempInvoice(invoiceItem);
                                }
                                isOn = true;
                                refreshPaymentInformation();

                                String itemPriceConverted = String.valueOf(itempriceCol);
                                int sizeOfChar = itemPriceConverted.trim().length();
                                int lengthOfCharToBeAdded = 20 - sizeOfChar;
                                writeDataToSerial(itemdescCol, itemPriceConverted,
                                        String.valueOf(lengthOfCharToBeAdded));
                                refreshRecyclerView();
                                txt_search.requestFocus();
                            } catch (SQLiteException e) {
                                e.printStackTrace();
                            }
                        } else {
                            cancelna();
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
//                            lbl_due.setText(subTotal2);
                }
                txt_search.setText("");
                btnSetQuantity.setText("1");
            } else {
                Toast.makeText(this, "Product can't be found", Toast.LENGTH_LONG).show();
                txt_search.setText("");
                productNotFound();
                txt_dummy.requestFocus();
                txt_search.requestFocus();
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    void setTenderCredit() {
        try {
            creditBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_creditcard, null);
            creditBuilder.setView(alertLayout);
            final AppCompatButton btnEnterCredit = (AppCompatButton)
                    alertLayout.findViewById(R.id.btnEnterCredit);
            etCreditBank = (AppCompatEditText) alertLayout.findViewById(R.id.etCreditBank);
            etCreditNumber = (AppCompatEditText) alertLayout.findViewById(R.id.etCreditNumber);
            etCreditExpiry = (AppCompatEditText) alertLayout.findViewById(R.id.etCreditExpiry);

            alertCredit = creditBuilder.create();
            alertCredit.show();

            btnEnterCredit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (etCreditBank.getText().toString().equals("") ||
                                etCreditNumber.getText().toString().equals("") ||
                                etCreditExpiry.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), "Please Fill all Fields.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            tenderCreditAmount = fuc.getTxtCashDouble(txt_cash);
                            creditBank = etCreditBank.getText().toString();
                            creditNumber = etCreditNumber.getText().toString();
                            creditExpiry = etCreditExpiry.getText().toString();
                            tenderCreditStatus = 1;
                            txt_cash.setText("" + "P0.00" + "");
                            refreshPaymentInformation();
                            alertCredit.dismiss();
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshPaymentInformation() {

        quantityCount = db_data.getQuantityCount(); //GET ITEM QUANTITY TOTAL COUNT
        double mDoubleCustomerCash = 0.0;
        mPriceTotal = db_data.totalPrice();

        if (tenderCashStatus == 1) {
            mDoubleCustomerCash += tenderCashAmount;
        }
        if (tenderCreditStatus == 1) {
            mDoubleCustomerCash += tenderCreditAmount;
        }
        if (tenderDebitStatus == 1) {
            mDoubleCustomerCash += tenderDebitAmount;
        }
        if (tenderGiftStatus == 1) {
            mDoubleCustomerCash += tenderGiftAmount;
        }
        if (tenderRnEStatus == 1) {
            mDoubleCustomerCash += tenderRnEAmount;
        }
        if (tenderDiscountStatus == 1) {
            mPriceTotal -= tenderDiscountAmount;
        }

        //HERE HERE HERE HERE
        mVattable = mPriceTotal / 1.12;
        mTax = mVattable * mTaxPercent; //.12
        mTotalDiscount = mVattable * discount;
        mTotal = mVattable + mTax - mTotalDiscount;
        mDue = mDoubleCustomerCash - mTotal;

//            mVattableConverted = NumberFormat.getCurrencyInstance().format(mVattable).replace("$","P");
//            mTaxConverted = NumberFormat.getCurrencyInstance().format(mTax).replace("$","P");
//            mSubTotalConverted = NumberFormat.getCurrencyInstance().format(mTotal).replace("$","P");
//            mTotalDiscountConverted = NumberFormat.getCurrencyInstance().format(mTotalDiscount).replace("$","P");
//            mDueConverted = NumberFormat.getCurrencyInstance().format(mDue).replace("$","P");

        lbl_sub.setText("" + moneyDecimal.format(mVattable) + "");
        lbl_tax.setText("" + moneyDecimal.format(mTax) + "");
        lbl_total.setText("" + moneyDecimal.format(mTotal) + "");
        lbl_discount.setText("-" + moneyDecimal.format(mTotalDiscount) + "");
        lbl_due.setText("" + moneyDecimal.format(mDue) + "");

        if (mDue < 0) {
            btn_print.setEnabled(false);
            lbl_dc.setText("" + "Due" + "");
        } else if (mDue >= 0 && invoiceAdapter.getItemCount() > 0) {
            btn_print.setEnabled(true);
            btn_print.setText("" + "Print Receipt" + "");
            lbl_dc.setText("" + "Change" + "");
        }
        writeDataToSerial("Total  : " + lbl_total.getText().toString(), "Change : " + lbl_due.getText().toString(), "");
        totalPayment = mDoubleCustomerCash;
    }

    //BUTTON PRINT
    public void print(View view){
        setPrint();
    }

    private void printFunction(ArrayList<String> list) {
        StringBuilder sb = new StringBuilder();
        for(String s : list){
            sb.append(s);
            sb.append("\n");
        }
        String convertedArray = sb.toString();

        byte[] SData;
        try {
            SData = convertedArray.getBytes("UTF-8");
            boolean retnVale = mPrinter.PrintText(SData);
            db_data.deleteAllTempItemInvoice(); //DELETE ALL TEMP ITEMS
            refreshRecyclerView();

            if(!retnVale){
                Toast.makeText(Cashier.this, mPrinter.GetLastPrintErr() , Toast.LENGTH_SHORT).show();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.my_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_cancel:
                setCancel();
                return true;
            case R.id.action_vieworder:
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onBackPressed(){
        tab_host.setCurrentTab(0);
        functionKeysMode = 2;
        refreshFunctionKeys();
        txt_search.requestFocus();
    }
    //On Options Menu Cancel Item
    public void cancelna(){
        txt_cash.setText(""+"P0.00"+"");
        txt_search.setText("");

        txt_search.requestFocus();
        lbl_due.setText(""+"P0.00"+"");
        lbl_total.setText(""+"P0.00"+"");
        lbl_sub.setText(""+"P0.00"+"");
        lbl_tax.setText(""+"P0.00"+"");
        lbl_discount.setText(""+"P0.00"+"");
        dialogVar=0;
        vat=0.00;
        vat2="";
        vattable=0.00;
        vattable2="";
        itempriceCol=0.00;
        itempricetotalCol=0.00;
//        itemCodeList.clear();
        itemnameCol="";
        itemNameList.clear();
//        itemQuantityList.clear();
        itemPriceList.clear();
        due=0.00;
        due2="";
        subTotal=0.00;
        subTotal2="";
        creditBank = "";
        creditNumber = "";
        creditExpiry = "";

        tenderCreditAmount = 0.0;
        tenderDebitAmount = 0.0;
        tenderDiscountAmount = 0.0;
        tenderGiftAmount = 0.0;
        tenderRnEAmount = 0.0;

        tenderCreditStatus = 0;
        tenderDebitStatus = 0;
        tenderDiscountStatus = 0;
        tenderGiftStatus = 0;
        tenderRnEStatus = 0;

        db_data.deleteAllTempItemInvoice();
    }

//    public void cashierLogOut(View view){
//        cancelna();
//        dbWriter.execSQL("INSERT INTO sessions(time,date,username) VALUES(time('now'),date('now'),'"+ userNum +"') ");
//        finish();
//        sleep(1000);
//    }

    public void tenderCash(View view) {
        setTenderCash();
    }

    public void tenderDebit(View view) {
        setTenderDebit();
    }

    public void tenderDiscount(View view) {
        setTenderDiscount();
    }

    //TODO 1/3/2018 Create this for product not found exception
    public void productNotFound() {
        productNotFoundBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_not_found, null);
        productNotFoundBuilder.setView(alertLayout);

        final AppCompatButton btnProductNotFoundAdd = (AppCompatButton) alertLayout.findViewById(R.id.btnProductNotFoundAdd);
        final AppCompatButton btnProductNotFoundIgnore = (AppCompatButton) alertLayout.findViewById(R.id.btnProductNotFoundIgnore);

        alertProductNotFound = productNotFoundBuilder.create();
        alertProductNotFound.show();
        btnProductNotFoundAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 1/4/2018 Add AlertDialog for AddProduct
                addProductNotFound();
                alertProductNotFound.dismiss();
            }
        });
        btnProductNotFoundIgnore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertProductNotFound.dismiss();
            }
        });
    }

    private void addProductNotFound() {
        addProductNotFoundBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_addproduct, null);
        addProductNotFoundBuilder.setView(alertLayout);
        final EditText productName = (EditText) alertLayout.findViewById(R.id.etProductName);
        final EditText productId = (EditText) alertLayout.findViewById(R.id.etProductId);
        final EditText productDes = (EditText) alertLayout.findViewById(R.id.etProductDes);
        final EditText productQuantity = (EditText) alertLayout.findViewById(R.id.etProductQuantity);
        final EditText productPrice = (EditText) alertLayout.findViewById(R.id.etProductPrice);
        final Button btnSaveAddProductNotFound = (Button) alertLayout.findViewById(R.id.btnSaveAddProduct);
        final Button btnCancelAddProductNotFound = (Button) alertLayout.findViewById(R.id.btnCancelAddProduct);

        alertAddProductNotFound = addProductNotFoundBuilder.create();
        alertAddProductNotFound.show();

        btnSaveAddProductNotFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mProductName = productName.getText().toString();
                String mProductPrice = productPrice.getText().toString();
                String mProductDes = productDes.getText().toString();
                String mProductQuantity = productQuantity.getText().toString();
                String mProductId = productId.getText().toString();
                int result = db_data.searchDuplicateProduct(mProductId);

                if (mProductName.isEmpty() || mProductId.isEmpty() || mProductDes.isEmpty() || mProductPrice.isEmpty() || mProductQuantity.isEmpty()) {
                    Toast.makeText(Cashier.this, "Fill all fields!", Toast.LENGTH_SHORT).show();
                } else {
                    if (result > 0) {
                        productId.setText("");
                        Toast.makeText(Cashier.this, "Duplicate PRODUCT ID!", Toast.LENGTH_SHORT).show();
                    } else {
                        db_data.addProduct(mProductId, mProductName, mProductDes, mProductPrice, mProductQuantity);
                        Toast.makeText(Cashier.this, "Added Successfully!", Toast.LENGTH_SHORT).show();
                        alertAddProductNotFound.dismiss();
                        txt_search.requestFocus();
                    }
                }
            }
        });

        btnCancelAddProductNotFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertAddProductNotFound.dismiss();
            }
        });
    }

//    public void tenderRedeem(View view) {
//        if (getTxtCashDouble() == 0.0) {
//            Toast.makeText(getApplicationContext(), "Please enter quantity", Toast.LENGTH_SHORT).show();
//        } else {
//            tenderRedeemQuantity += getTxtCashDouble();
//            tenderRedeemStatus = 1;
//            refreshPaymentInformation();
//                txt_cash.setText("" + "P0.00" + "");
//        }
//
//    }

    public void tenderGift(View view) {
        setTenderGift();
    }

//    public void tenderRnE(View view) {
//
//        tenderRnEAmount = getTxtCashDouble();
//        tenderRnEStatus = 1;
//            refreshPaymentInformation();
//            txt_cash.setText("" + "P0.00" + "");
//    }

    public void cashierLogOut(View view) {
        setLogOut();
    }

    public void cashierCashInOut(View view) {
        setCashInOut();
    }

    public void xreport(View view) {
        //FOR SAVING X REPORT
        setXReport();
    }

    public void zreport(View view) {
        //FOR SAVING Z REPORT
        setZReport();
    }

    void userAuthZreport() {
        authenticateBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_login, null);
        authenticateBuilder.setView(alertLayout);

        final AppCompatButton btnEnterAuthenticate = (AppCompatButton) alertLayout.findViewById(R.id.btnEnterAuthentication);
        etUsername = (AppCompatEditText) alertLayout.findViewById(R.id.etUsername);
        etPassword = (AppCompatEditText) alertLayout.findViewById(R.id.etPassword);

        alertAuthenticate = authenticateBuilder.create();
        alertAuthenticate.show();
        btnEnterAuthenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int authen = db_data.cashierLogin(etUsername.getText().toString(), etPassword.getText().toString());
                    if (authen >= 1) {
                        if (db_data.getTheCashierLevel(etUsername.getText().toString()).equals("Manager") || db_data.getTheCashierLevel(etUsername.getText().toString()).equals("Supervisor")) {
                            alertAuthenticate.dismiss();
                            zReportBaKamo();
                            unLockCashBox();
                        } else {
                            Toast.makeText(getApplicationContext(), "Unauthorized account.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        alertAuthenticate.dismiss();
                    }
                } catch (Exception e) {

                }
            }
        });
    }

    void showDialogCashInOut() {
        cashinoutBuilder = new AlertDialog.Builder(Cashier.this);
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_cashierinout, null);
        RadioGroup rgINorOUT = (RadioGroup) alertLayout.findViewById(R.id.rg_inout);
        final AppCompatRadioButton rbIN = (AppCompatRadioButton) alertLayout.findViewById(R.id.rgIN);
        final AppCompatRadioButton rbOUT = (AppCompatRadioButton) alertLayout.findViewById(R.id.rgOUT);
        final AppCompatEditText cashValue = (AppCompatEditText) alertLayout.findViewById(R.id.etValueCashinOut);
        final AppCompatEditText referenceNumber = (AppCompatEditText) alertLayout.findViewById(R.id.etReferenceNumber);
        final AppCompatEditText remarks1 = (AppCompatEditText) alertLayout.findViewById(R.id.etRemarks1);
        final AppCompatEditText remarks2 = (AppCompatEditText) alertLayout.findViewById(R.id.etRemarks2);
        final AppCompatEditText remarks3 = (AppCompatEditText) alertLayout.findViewById(R.id.etRemarks3);
        final Spinner cashinoutSpinner = (Spinner) alertLayout.findViewById(R.id.spinnerCashinout);
        AppCompatButton btnSubmitCashinout = (AppCompatButton) alertLayout.findViewById(R.id.btnSubmitCashinout);
        AppCompatButton btnCancelCashinout = (AppCompatButton) alertLayout.findViewById(R.id.btnCancelCashinout);
        final AppCompatButton btnAddremarksCashinout = (AppCompatButton) alertLayout.findViewById(R.id.btnAddRemarksCashinout);

        cashinoutBuilder.setView(alertLayout);
        alertCashinout = cashinoutBuilder.create();
        alertCashinout.show();

        rgINorOUT.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (rbIN.isChecked()) {
                    cashIn = true;
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Cashier.this, R.array.cashin, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    cashinoutSpinner.setAdapter(adapter);
                } else if (rbOUT.isChecked()) {
                    cashIn = false;
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Cashier.this, R.array.cashout, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    cashinoutSpinner.setAdapter(adapter);
                }
            }
        });

        btnAddremarksCashinout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = 0;

                if (count == 1 && remarks3.getVisibility() != View.VISIBLE) {
                    remarks3.setVisibility(View.VISIBLE);
                    count = 0;
                    btnAddremarksCashinout.setVisibility(View.GONE);

                }

                if (remarks2.getVisibility() != View.VISIBLE) {
                    remarks2.setVisibility(View.VISIBLE);
                    count = 1;
                }

            }
        });

        btnSubmitCashinout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                double ctCashAdd = 0.0, ctCashMinus = 0.0;

                Date currDate = new Date();
                final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MMM-dd-yyyy");
                String dateToStr = dateTimeFormat.format(currDate);
//                Date strToDate = null;
//                try {
//                    strToDate = dateTimeFormat.parse(dateToStr);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                String dateToString = strToDate.toString();

                if (!rbIN.isChecked() && !rbOUT.isChecked()) {
                    Toast.makeText(Cashier.this, "Select IN or OUT!", Toast.LENGTH_SHORT).show();
                }

                if (cashValue.equals("") || remarks1.equals("") || remarks2.equals("")) {
                    Toast.makeText(Cashier.this, "Fill all fields!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (!cashIn && cashValue.getText().toString() != "") {
                        if (cashValue.getText().toString().equals("")) {
                            ctCashMinus = 0.0;
                        } else {
                            ctCashAdd = 0.0;
                            ctCashMinus = Double.parseDouble(cashValue.getText().toString());
                        }
                    } else if (cashIn && cashValue.getText().toString() != "") {
                        if (cashValue.getText().toString().equals("")) {
                            ctCashAdd = 0.0;
                        } else {
                            ctCashAdd = Double.parseDouble(cashValue.getText().toString());
                            ctCashMinus = 0.0;
                        }
                    }

                    CashTransaction ct = new CashTransaction();

                    ct.setCtTransnum(transNumber + "");
                    ct.setCtCashNum(userNum);
                    ct.setCtDateTime(dateToStr);
                    ct.setCtCashAdd(ctCashAdd);
                    ct.setCtCashMinus(ctCashMinus);
                    ct.setCtReason(cashinoutSpinner.getSelectedItem().toString());
                    ct.setCtRemarks1(referenceNumber.getText().toString());
                    ct.setCtRemarks2(remarks1.getText().toString());
                    ct.setCtRemarks3(remarks2.getText().toString());
                    ct.setCtRemarks4(remarks3.getText().toString());
                    ct.setCtX("0");
                    ct.setCtZ("0");

                    db_data.addCashTransaction(ct);
                }
                alertCashinout.dismiss();
            }
        });

        btnCancelCashinout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertCashinout.dismiss();
            }
        });
    }

    //    void authenticationUser(){
//        authenticationBuilder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = getLayoutInflater();
//        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_authenticate, null);
//        authenticationBuilder.setView(alertLayout);
//
//        final AppCompatButton btnProceed = (AppCompatButton) alertLayout.findViewById(R.id.btnProceed);
//        final AppCompatEditText etUsername = (AppCompatEditText) alertLayout.findViewById(R.id.etUsernameAuth);
//        final AppCompatEditText etPass = (AppCompatEditText) alertLayout.findViewById(R.id.etPasswordAuth);
//
//        alertZreport = zReportBuilder.create();
//        alertZreport.show();
//    }
    void xReportBaKamo() {
        xReportBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_xreport, null);
        xReportBuilder.setView(alertLayout);

        final AppCompatButton btnEnterXreport = (AppCompatButton) alertLayout.findViewById(R.id.btnCashDrawerSubmitX);
        etTotalCashDrawerX = (AppCompatEditText) alertLayout.findViewById(R.id.etTotalCashDrawerX);

        alertXreport = xReportBuilder.create();
        alertXreport.show();

        btnEnterXreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    if (etTotalCashDrawerX.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Please Fill all Fields.", Toast.LENGTH_SHORT).show();
                    } else {
                        enteredCashDrawer = Double.parseDouble(etTotalCashDrawerX.getText().toString());
                        alertXreport.dismiss();
                    }

                    transType = "xreport";

                    int bcd;
                    db_data.addTransaction(transType, fuc.getCurrentDate(), userNum, 0, 0, "");
                    String[] itemID = new String[]{_ID, COLUMN_TRANSACTION_TYPE};
                    Cursor cursor1 = dbReader.query(TABLE_TRANSACTION, itemID, null, null, null, null, null);
                    cursor1.moveToLast();
                    bcd = cursor1.getInt(0); //COLUMN _ID of TABLE_TRANSACTION
                    cursor1.close();

                    reportBaKamo.setDb_data(db_data);

                    reportBaKamo.main(userNum, fuc.getCurrentDate(), bcd, enteredCashDrawer);
                    ArrayList<String> paPrintNaman;
                    paPrintNaman = reportBaKamo.getPrintArray();
                    printFunction(paPrintNaman);
                    paPrintNaman.clear();

                    db_data.updateTransactions(userNum);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    void zReportBaKamo() {
        zReportBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_zreport, null);
        zReportBuilder.setView(alertLayout);

        final AppCompatButton btnEnterZreport = (AppCompatButton) alertLayout.findViewById(R.id.btnCashDrawerSubmitZ);
        etTotalCashDrawerZ = (AppCompatEditText) alertLayout.findViewById(R.id.etTotalCashDrawerZ);

        alertZreport = zReportBuilder.create();
        alertZreport.show();

        btnEnterZreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (etTotalCashDrawerZ.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Please Fill all Fields.", Toast.LENGTH_SHORT).show();
                    } else {
                        enteredCashDrawer = Double.parseDouble(etTotalCashDrawerZ.getText().toString());
                        alertZreport.dismiss();
                    }

                    transType = "zreport";

                    int bcd;
                    db_data.addTransaction(transType, fuc.getCurrentDate(), userNum, 0, 0, "");
                    String[] itemID = new String[]{_ID, COLUMN_TRANSACTION_TYPE};
                    Cursor cursor1 = dbReader.query(TABLE_TRANSACTION, itemID, null, null, null, null, null);
                    cursor1.moveToLast();
                    bcd = cursor1.getInt(0); //COLUMN _ID of TABLE_TRANSACTION
                    cursor1.close();
                    reportBaKamo.setDb_data(db_data);
                    reportBaKamo.main("no", fuc.getCurrentDate(), bcd, enteredCashDrawer);
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

                    zreportExportFunction.showDialogLoading(Cashier.this, retrievedCursorFromJoinTable, cursorDummy);
                    backUpDatabase.main(Cashier.this);
//            boolean sent =
//            if(sent){
//                zreportExportFunction.closeDialog();
//            }
                    //END OF EXPORT CSV

                    db_data.updateTransactions("no");

//                    //GETTING QUANTITY SALES
//                    ContentValues cv = new ContentValues();
//                    cv.put(COLUMN_ITEM_ZREPORT, 1);
//                    String whereBaKamo = COLUMN_ITEM_ZREPORT + "= ?";
//                    String[] WhereArgBaKamo = {"0"};
//                    dbWriter.update(TABLE_ITEM, cv, whereBaKamo, WhereArgBaKamo);
//                    //END OF QUANTITY SALES

                    paPrintNaman.clear();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }


    private void sleep(int ms) {
        try {
            java.lang.Thread.sleep(ms);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        rb_ndisc = (RadioButton)findViewById(R.id.rb_ndisc);
        rb_spdisc = (RadioButton)findViewById(R.id.rb_rpdisc);
        rb_ddisc = (RadioButton)findViewById(R.id.rb_ddisc);
        txt_search =(EditText) findViewById(R.id.txt_cashier_search);
        txt_cash = (EditText)findViewById(R.id.txt_cash);
        txt_dummy = (EditText) findViewById(R.id.dummy_ba_kamo);
        layout = (RelativeLayout)findViewById(R.id.rl_cashier);
        tab_host = (TabHost)findViewById(R.id.tabHost);
        lbl_sub=(TextView)findViewById(R.id.lbl_subtotal);
        lbl_tax=(TextView)findViewById(R.id.lbl_tax);
        lbl_total=(TextView)findViewById(R.id.lbl_total);
        lbl_due=(TextView)findViewById(R.id.lbl_due);
        lbl_dc=(TextView)findViewById(R.id.lbl_dc);
        rg_discount = (RadioGroup)findViewById(R.id.rg_discount);
        lbl_discount = (TextView)findViewById(R.id.lbl_discount);
        btn_print = (Button)findViewById(R.id.btn_printBaKamo);
        img_receipt = (ImageView) findViewById(R.id.img_receipt);
        btnSetQuantity = (Button) findViewById(R.id.btnRegisterQuantity);

        //Mark's Initialization
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_invoice);

        btn_cashier_delete = (ImageButton) findViewById(R.id.btn_cashier_delete);
        btn_cashier_refund = (ImageButton) findViewById(R.id.btn_cashier_refund);
        btn_cashier_cancelRefund = (Button) findViewById(R.id.btn_cashier_cancelRefund);
        btn_cashier_confirmDelete = (Button) findViewById(R.id.btn_cashier_confirmDelete);

        btn_customX = (CustomButtonForDrawableTop) findViewById(R.id.btn_cashier_x_report);
        btn_customZ = (CustomButtonForDrawableTop) findViewById(R.id.btn_cashier_z_report);
        btn_customCash = (CustomButtonForDrawableTop) findViewById(R.id.btn_cashier_cashinout);
        btn_customLog = (CustomButtonForDrawableTop) findViewById(R.id.btn_cashier_logout);

        flexNiAdminPrivileges = (FlexboxLayout) findViewById(R.id.flexNiAdminPriveleges);
        flexNiAdminPrivileges.setOnKeyListener(this);

//        LinearLayout ln_shift = (LinearLayout)findViewById(R.id.ln_shift);
//        ln_shift.setOnKeyListener(this);

//        TabWidget tabBa = (TabWidget)findViewById(R.id.tabs);
//        tabBa.setOnKeyListener(this);

        btnCreditCard = (Button)findViewById(R.id.btnCredit);

        for (int a = 0; a < functionKeysBtn.length; a++)
            functionKeysBtn[a] = (Button) findViewById(functionKeysID[a]);

        setListener.setListener(new EditText[]{},
                new ImageButton[]{btn_cashier_refund,btn_cashier_delete},
                new Button[]{btn_cashier_cancelRefund,btn_cashier_confirmDelete,btn_print,btnCreditCard,btnSetQuantity},
                new RadioButton[]{rb_ddisc,rb_spdisc,rb_ndisc},new CheckBox[]{},new Spinner[]{},
                new CustomButtonForDrawableTop[]{btn_customX,btn_customZ,btn_customCash,btn_customLog},
                this);
        tab_host.setOnKeyListener(this);
    }

    private void printerDetection() {
        try {
            //RUN CODE IF JOLLICARL IS PRESENT
            try {
//                mSerialPrinter.OpenPrinter(new SerialParam(9600, "/dev/ttyS3", 0), new SerialDataHandler());
//                HdxUtil.SetPrinterPower(1);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            //RUN CODE IF JOLLIMARK IS PRESENT
            try {
                marksPrinter.Open();
            } catch (Exception e) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshRecyclerView() {
        //REFRESHING THE RECYCLER
        invoiceItemList = fill_with_data();
        invoiceAdapter = new InvoiceAdapter(getApplication(), invoiceItemList);
        mLayoutManager = new GridLayoutManager(Cashier.this,2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(invoiceAdapter);
        invoiceAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "Enter onDestroy");

        if(mSerial!=null) {
            mSerial.end();
            mSerial = null;
        }

        super.onDestroy();
        Log.d(TAG, "Leave onDestroy");
    }

    public void onResume() {
        Log.d(TAG, "Enter onResume");
        super.onResume();
        String action =  getIntent().getAction();
        Log.d(TAG, "onResume:"+action);

        //if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action))
        if(!mSerial.isConnected()) {
            if (SHOW_DEBUG) {
                Log.d(TAG, "New instance : " + mSerial);
            }

            if( !mSerial.enumerate() ) {

                Toast.makeText(this, "no more devices found", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Log.d(TAG, "onResume:enumerate succeeded!");
            }
            try {
                Thread.sleep(1500);
                openUsbSerial();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }//if isConnected

        Log.d(TAG, "Leave onResume");
    }

    private void openUsbSerial() {
        Log.d(TAG, "Enter  openUsbSerial");

        if(mSerial==null) {

            Log.d(TAG, "No mSerial");
            return;

        }

        if (mSerial.isConnected()) {
            if (SHOW_DEBUG) {
                Log.d(TAG, "openUsbSerial : isConnected ");
            }

            // if (!mSerial.InitByBaudRate(mBaudrate)) {
            if (!mSerial.InitByBaudRate(mBaudrate,700)) {
                if(!mSerial.PL2303Device_IsHasPermission()) {
                    Toast.makeText(this, "cannot open, maybe no permission", Toast.LENGTH_SHORT).show();
                }

                if(mSerial.PL2303Device_IsHasPermission() && (!mSerial.PL2303Device_IsSupportChip())) {
                    Toast.makeText(this, "cannot open, maybe this chip has no support, please use PL2303HXD / RA / EA chip.", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "cannot open, maybe this chip has no support, please use PL2303HXD / RA / EA chip.");
                }
                Toast.makeText(this, "connected : OK", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "connected : OK");
                Log.d(TAG, "Exit  openUsbSerial");

            } else {


            }
        }//isConnected
        else {
            Toast.makeText(this, "Connected failed, Please plug in PL2303 cable again!" , Toast.LENGTH_SHORT).show();
            Log.d(TAG, "connected failed, Please plug in PL2303 cable again!");

        }
    }//openUsbSerial

    private void writeDataToSerial(String printMe1st,String printMe2nd,String lengthToLeft) {

        Log.d(TAG, "Enter writeDataToSerial");

        if(null==mSerial) return;

        if(!mSerial.isConnected()) return;

        String strWrite;
        //strWrite = String.format("%1$-40" + "s", "Mae Loves Mark <3."); //20 char per line x2 = 40 chars
        strWrite = String.format("%1$-20s" + "%2$-20s", printMe1st, printMe2nd);

        if (SHOW_DEBUG) {
            Log.d(TAG, "PL2303Driver Write 2(" + strWrite.length() + ") : " + strWrite);
        }
        int res = mSerial.write(strWrite.getBytes(), strWrite.length());
//        mSerial.write(strWrite2.getBytes(), strWrite2.length());
        if( res<0 ) {
            Log.d(TAG, "setup2: fail to controlTransfer: "+ res);
            return;
        }

        Toast.makeText(this, "Write length: "+strWrite.length()+" bytes", Toast.LENGTH_SHORT).show();

        Log.d(TAG, "Leave writeDataToSerial");
    }//writeDataToSerial

    public void refreshFunctionKeys() {
        for (int i = 0; i < functionKeysBtn.length; i++) {
            functionKeysBtn[i].setText(functionKeys[functionKeysMode][i]);
        }
    }

    private void functionKeysDo(int functionKeysCode){
        switch(functionKeysMode){
            case 2:
                switch (functionKeysCode){
                    case 0:
                        setQuantity();
                        Toast.makeText(this,"Quantity",Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        if(refund_status) unsetRefund();
                        else setRefund();
                        Toast.makeText(this,"Refund",Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        if(functionKeysBtn[2].getText().equals("Delete")){
                            setDelete();
                            functionKeysBtn[2].setText("Confirm");
                        }else{
                            confirmDelete();
                            functionKeysBtn[2].setText("Delete");
                        }
                        Toast.makeText(this,"Delete",Toast.LENGTH_LONG).show();
                        break;
                    case 9:
                        setCancel();
                        tab_host.setCurrentTab(0);
                        functionKeysMode = 2;
                        refreshFunctionKeys();
                        Toast.makeText(this,"Cancel",Toast.LENGTH_LONG).show();
                        break;
                    case 10:
                        tab_host.setCurrentTab(1);
                        functionKeysMode = 3;
                        refreshFunctionKeys();
                        Toast.makeText(this,"Payment",Toast.LENGTH_LONG).show();
                        break;
                    case 11:
                        tab_host.setCurrentTab(2);
                        functionKeysMode = 6;
                        refreshFunctionKeys();
                        Toast.makeText(this,"Shift",Toast.LENGTH_LONG).show();
                        break;
                }
                break;
            case 3:
                switch (functionKeysCode){
                    case 0:
                        setPrint();
                        Toast.makeText(this,"Complete",Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        functionKeysMode = 4;
                        refreshFunctionKeys();
                        Toast.makeText(this,"Payment",Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        functionKeysMode = 5;
                        refreshFunctionKeys();
                        Toast.makeText(this,"Discount",Toast.LENGTH_LONG).show();
                        break;
                    case 9:
                        setCancel();
                        tab_host.setCurrentTab(0);
                        functionKeysMode = 2;
                        refreshFunctionKeys();
                        Toast.makeText(this,"Cancel",Toast.LENGTH_LONG).show();
                        break;
                    case 10:
                        tab_host.setCurrentTab(0);
                        functionKeysMode = 2;
                        refreshFunctionKeys();
                        Toast.makeText(this,"Invoice",Toast.LENGTH_LONG).show();
                        break;
                    case 11:
                        tab_host.setCurrentTab(2);
                        functionKeysMode = 6;
                        refreshFunctionKeys();
                        Toast.makeText(this,"Shift",Toast.LENGTH_LONG).show();
                        break;
                }
                break;
            case 4:
                switch (functionKeysCode){
                    case 0:
                        setTenderCash();
                        Toast.makeText(this,"Cash",Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        setTenderCredit();
                        Toast.makeText(this,"Credit",Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        setTenderDebit();
                        Toast.makeText(this,"Debit",Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        setTenderGift();
                        Toast.makeText(this,"Gift",Toast.LENGTH_LONG).show();
                        break;
                    case 4:
                        setTenderDiscount();
                        Toast.makeText(this,"Discount",Toast.LENGTH_LONG).show();
                        break;
                    case 5:
                        Toast.makeText(this,"Other",Toast.LENGTH_LONG).show();
                        break;
                    case 11:
                        functionKeysMode = 3;
                        refreshFunctionKeys();
                        Toast.makeText(this,"Back",Toast.LENGTH_LONG).show();
                        break;
                }
                break;
            case 5:
                switch (functionKeysCode){
                    case 0:
                        rb_ndisc.setChecked(true);
                        Toast.makeText(this,"Normal",Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        rb_spdisc.setChecked(true);
                        Toast.makeText(this,"Senior",Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        rb_ddisc.setChecked(true);
                        Toast.makeText(this,"Diplomat",Toast.LENGTH_LONG).show();
                        break;
                    case 11:
                        functionKeysMode = 3;
                        refreshFunctionKeys();
                        Toast.makeText(this,"Back",Toast.LENGTH_LONG).show();
                        break;
                }
                break;
            case 6:
                switch (functionKeysCode){
                    case 0:
                        setXReport();
                        Toast.makeText(this,"X-Read",Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        setZReport();
                        Toast.makeText(this,"Z-Read",Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        setCashInOut();
                        Toast.makeText(this,"Cash",Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        setLogOut();
                        Toast.makeText(this,"LogOut",Toast.LENGTH_LONG).show();
                        break;
                    case 10:
                        tab_host.setCurrentTab(0);
                        functionKeysMode = 2;
                        refreshFunctionKeys();
                        Toast.makeText(this,"Invoice",Toast.LENGTH_LONG).show();
                        break;
                    case 11:
                        tab_host.setCurrentTab(1);
                        functionKeysMode = 3;
                        refreshFunctionKeys();
                        Toast.makeText(this,"Payment",Toast.LENGTH_LONG).show();
                        break;
                }

                break;
        }
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(event.getAction()!=KeyEvent.ACTION_DOWN)
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
            // TODO: 5/21/2018 Add action to every number for barcode and cash
//            case KeyEvent.KEYCODE_0:

//            case KeyEvent.KEYCODE_ENTER:
//                if(functionKeysMode==2){
//                    long mCode = txt_search.getText().length();
//                    if(mCode >= 13){
//                        searchProduct();
//                    }
//                }
//                return true;
        }
        return true;
    }

    public static class FirstFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_cashier_invoice, container, false);
        }
    }
    public static class SecondFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_cashier_payment, container, false);
        }
    }
    public static class ThirdFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_cashier_shift, container, false);
        }
    }
}
// SELECT TP.PN, TPT.Q, S.Q, TP.Q