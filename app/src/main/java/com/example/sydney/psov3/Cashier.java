package com.example.sydney.psov3;

import android.app.Fragment;
import android.content.ContentValues;
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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sydney.psov3.adapter.AdapterOrder;
import com.jolimark.JmPrinter;
import com.jolimark.UsbPrinter;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
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
import static com.example.sydney.psov3.Constants.TABLE_INVOICE;
import static com.example.sydney.psov3.Constants.TABLE_ITEM;
import static com.example.sydney.psov3.Constants.TABLE_PRODUCT;
import static com.example.sydney.psov3.Constants.TABLE_PRODUCT_TEMP;
import static com.example.sydney.psov3.Constants.TABLE_TEMP_INVOICING;
import static com.example.sydney.psov3.Constants.TABLE_TRANSACTION;
import static com.example.sydney.psov3.Constants._ID;

public class Cashier extends AppCompatActivity {
    private static final String ACTION_USB_PERMISSION = "com.prolific.pl2303hxdsimpletest.USB_PERMISSION";
    private static final boolean SHOW_DEBUG = true;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }    //TO SUPPORT VECTOR DRAWABLES

    ArrayList<String> itemCode123 = new ArrayList<>();
    ArrayList<String> itemQuan123 = new ArrayList<>();
    String userNum;
    Double vattable,vat,subTotal=0.0,itempriceCol,itempricetotalCol=0.0,discount=0.0,discounted,totalPrice;
    Double due,payment;
    Cursor cursor;
    DB_Data db_data;
    ContentValues cv;
    EditText txt_search,txt_cash;
    String itemnameCol,itemdescCol,formatted,vat2,vattable2,subTotal2,due2,totalPrice2,itempricetotalCol2,discount2,discounted2;
//    ArrayList<Integer>itemQuantityList = new ArrayList<>();
    ArrayList<Double>itemPriceList = new ArrayList<>();
    ArrayList<Integer>itemQuantityList = new ArrayList<>();
    ArrayList<Integer>itemCodeList = new ArrayList<>();
    ArrayList<String>itemNameList = new ArrayList<>();
    ArrayList<String>itemDescList = new ArrayList<>();
    ArrayList<Double> total = new ArrayList<>();
    RelativeLayout layout;
    ArrayList<String> products = new ArrayList<>();
    ArrayList<String> reprint = new ArrayList<>(); //Use to duplicate last print
    SQLiteDatabase dbReader;
    SQLiteDatabase dbWriter;
    TabHost tab_host;
    TextView lbl_sub,lbl_tax,lbl_total,lbl_due,lbl_dc,lbl_discount;
    Button btn_print,btn_cashier_confirmDelete,btnCreditCard;
    ImageButton btn_cashier_delete;
    RadioButton rb_ndisc,rb_spdisc,rb_ddisc;
    RadioGroup rg_discount;
    List<List<String>> t2Rows = new ArrayList<>();
    ArrayList<Order> orderArrayList;
    AdapterOrder adapterOrder=null;
    String currentTime;
    String dateformatted;
    String transType;
    ReportBaKamo reportBaKamo;
    double enteredCashDrawer;
    int transNumber;
    boolean isOn = false;
    //Dialog for Enter the Quantity
    AlertDialog.Builder builder;
    AlertDialog alertQuantity;

    //Dialog for Enter the Credit Card
    AlertDialog.Builder creditBuilder;
    AlertDialog alertCredit;

    double mPriceTotal;
    double mVattable;
    double mTax;
    double mTotalDiscount;
    double mTotal;
    String mcustomerCash;
    double mDue;
    double mTaxPercent;
    String mVattableConverted = "";
    String mTaxConverted = "";
    String mSubTotalConverted = "";
    String mTotalDiscountConverted = "";
    String mDueConverted = "";
    String cleanSubtotal = "";
    String cleanVattable = "";
    String cleanTax = "";
    double parsed = 0.0;
    String TAG = "PL2303HXD_APLog";
    PL2303Driver mSerial;

    //Variables for txt in Payment Info
    ZreportExportFunction zreportExportFunction;
    InvoiceAdapter invoiceAdapter = null;
    AppCompatEditText etQuan = null;

    AppCompatEditText etCreditPayment = null;
    AppCompatEditText etCreditBank = null;
    AppCompatEditText etCreditNumber = null;
    AppCompatEditText etCreditExpiry = null;
    String creditBank, creditNumber, creditExpiry;
    double creditPayment = 0.0;
    int creditStatus = 0;

    private int quantityCount = 0, itemcodeCol, discType = 0, dialogVar;
    private String code = "";
    private List<InvoiceItem> invoiceItemList;
    private RecyclerView recyclerView;
    private GridLayoutManager mLayoutManager;
    private String customerCash;
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

    public static boolean unLockCashBox() {
        boolean retnValue = false;
        UsbPrinter tmpUsbDev = new UsbPrinter();
        retnValue = tmpUsbDev.UnLockOfCashBox();

        return retnValue;
    }

//    private void createMyDialog() {
//        builder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = getLayoutInflater();
//
//        final EditText reportTotalCashDrawerX,reportTotalCashDrawerZ;
//        final Button btnSubmitX,btnCancelX,btnSubmitZ,btnCancelZ;
//
//        final View alertLayoutXreport = inflater.inflate(R.layout.custom_alertdialog_xreport,null);
//        final View alertLayoutZreport = inflater.inflate(R.layout.custom_alertdialog_xreport,null);
//
//        builder.setView(alertLayoutXreport);
//        builder.setView(alertLayoutZreport);
//
//        reportTotalCashDrawerX = (EditText) alertLayoutXreport.findViewById(R.id.etTotalCashDrawerX);
//        btnSubmitX = (Button) alertLayoutXreport.findViewById(R.id.btnCashDrawerSubmitX);
//        btnCancelX = (Button) alertLayoutXreport.findViewById(R.id.btnCashDrawerCancelX);
//
//        reportTotalCashDrawerZ = (EditText) alertLayoutXreport.findViewById(R.id.etTotalCashDrawerZ);
//        btnSubmitZ = (Button) alertLayoutXreport.findViewById(R.id.btnCashDrawerSubmitZ);
//        btnCancelZ= (Button) alertLayoutXreport.findViewById(R.id.btnCashDrawerCancelZ);
//
//        String[] itemID = new String[]{_ID, COLUMN_TRANSACTION_TYPE};
//        Cursor cursor1 = dbReader.query(TABLE_TRANSACTION, itemID, null, null, null, null, null);
//        cursor1.moveToLast();
//        transNumber = cursor1.getInt(cursor1.getColumnIndex(_ID)); //COLUMN _ID of TABLE_TRANSACTION
//        cursor1.close();
//
//        btnSubmitX.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String convertedTransNum = Integer.toString(transNumber);
////                double cashSale = db_data.getCashSales(userNum);
//                enteredCashDrawer = Double.parseDouble(reportTotalCashDrawerX.getText().toString().trim());
////                double cashShortOver = enteredCashDrawer - cashSale;
////                db_data.addXreport(convertedTransNum,cashSale,enteredCashDrawer,cashShortOver);
//
//
//            }
//        });
//
//        btnSubmitZ.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String convertedTransNum = Integer.toString(transNumber);
////                double cashSale = db_data.getCashSales(userNum);
//                double enteredCashDrawer = Double.parseDouble(reportTotalCashDrawerZ.getText().toString().trim());
////                double cashShortOver = enteredCashDrawer - cashSale;
////                db_data.addXreport(convertedTransNum, cashSale, enteredCashDrawer, cashShortOver);
//
//
//        }
//        });
//
//
//        btnCancelX.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                alertDialogXreport.dismiss();
//            }
//        });
//
//        btnCancelZ.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                alertDialogZreport.dismiss();
//            }
//        });
//    }

    protected void onCreate(Bundle savedInstanceState) {

        db_data = new DB_Data(this);

        dbReader = db_data.getReadableDatabase();
        dbWriter = db_data.getWritableDatabase();
        reportBaKamo = new ReportBaKamo();
        //bill.main();
        cv = new ContentValues();

        printerDetection();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier);
        init(); //INITALIZATION OF VIEWS

        mPrinter = new JmPrinter();           //Create a 78M printer object

        Intent intent = getIntent();
        userNum = intent.getExtras().getString("CashNum");

        mSerial = new PL2303Driver((UsbManager) getSystemService(Context.USB_SERVICE),
                this, ACTION_USB_PERMISSION);
        mBaudrate =PL2303Driver.BaudRate.B19200;
        if (!mSerial.PL2303USBFeatureSupported()) {

            Toast.makeText(this, "No Support USB host API", Toast.LENGTH_SHORT)
                    .show();

            Log.d(TAG, "No Support USB host API");

            mSerial = null;

        }

        if( !mSerial.enumerate() ) {
            Toast.makeText(this, "no more devices found", Toast.LENGTH_SHORT).show();
        }

        try {
            Thread.sleep(1500);
            openUsbSerial();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        writeDataToSerial("ABZTRAK INC.","Tinda-PoS Android","");
//        writeDataToSerial("ABZTRAK INC.");
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
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy");
        dateformatted = dateformat.format(c.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        currentTime = sdf.format(new Date());
        dbWriter.execSQL("INSERT INTO sessions(time,date,username) VALUES(time('now'),date('now'),'"+ userNum +"') ");

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
                        formatted = NumberFormat.getCurrencyInstance().format((parsed / 100));
                        current = formatted;
                        txt_cash.setText(formatted.replace("$", "P"));
                        txt_cash.setSelection(formatted.length());

                        txt_cash.addTextChangedListener(this);

                        if (mDue < 0 ) {
                            btn_print.setEnabled(false);
                        } else if (mDue >= 0) {
                            btn_print.setEnabled(true);
                            btn_print.setText("" + "Print Receipt" + "");
                            lbl_dc.setText("" + "Change" + "");
                        }

                        refreshPaymentInformation();


                    }

                }
                    catch (Exception e){
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
                    inVattable = 0.0;
                    inVatted = 0.0;
                    inVatStatus = "x";
                    inSeniorDiscount = discount;
                    inVatExempt = 0.0;
                    inZeroRated = mVattable;

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
                    inVattable = 0.0;
                    inVatted = 0.0;
                    inVatStatus = "z";
                    inSeniorDiscount = 0.0;
                    inVatExempt = 0.0;
                    inZeroRated = vattable;

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

        btnCreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterCreditDetails();
            }
        });

        //Mark's onClickListeners for Button reports
        btn_cashier_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mLayoutManager.getItemCount() == 0){
                    btn_cashier_delete.setVisibility(View.VISIBLE);
                    btn_cashier_confirmDelete.setVisibility(View.INVISIBLE);
                    Toast.makeText(Cashier.this, "NO ITEMS!", Toast.LENGTH_SHORT).show();
                } else {
                    btn_cashier_delete.setVisibility(View.INVISIBLE);
                    btn_cashier_confirmDelete.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_cashier_confirmDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(InvoiceItem item : invoiceItemList){
                    if(item.isSelected()){
                        Double mSubTotal = 0.0;
//                        int result = db_data.searchSelectedItem(item.isSelected() + "");
                        db_data.deleteTempItemInvoice(item.getInvoiceProductID());
                        btn_cashier_confirmDelete.setVisibility(View.INVISIBLE);
                        btn_cashier_delete.setVisibility(View.VISIBLE);

                        refreshRecyclerView();

                        //START
                        if(invoiceAdapter.getItemCount() > 0){
                            //START OF COMPUTATION UPPER
                            refreshPaymentInformation();
                            Toast.makeText(Cashier.this, "ITEM DELETED!", Toast.LENGTH_SHORT).show();
                        } else {
                            isOn = false;
                            total.clear();
                            cancelna();
                        }
                        //END
                    }
                }
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

            invoiceItemList.add(new InvoiceItem(invoiceItemName,invoiceItemDescription,invoiceItemPrice,dummyVattable,invoiceItemQuantity,invoiceItemID,invoiceItemTotal));
        }
        cursor.close();

        return invoiceItemList;
    }

    public void priceClick(View view){
        txt_cash.requestFocus();
    }

    //BUTTON SEARCH ONCLICK
    public void searchProduct() {
        try {
            builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_enterquantity, null);
            builder.setView(alertLayout);
            final AppCompatButton btnEnter = (AppCompatButton) alertLayout.findViewById(R.id.btnEnter);
            etQuan = (AppCompatEditText) alertLayout.findViewById(R.id.etEnterQuantity);

            code = txt_search.getText().toString();
            final String[] itemcode = {code};
            itemCode123.add(code);
            String[] WHERE = {COLUMN_PRODUCT_ID, COLUMN_PRODUCT_NAME, COLUMN_PRODUCT_DESCRIPTION,COLUMN_PRODUCT_QUANTITY, COLUMN_PRODUCT_PRICE};
            cursor = dbReader.query(TABLE_PRODUCT, WHERE, COLUMN_PRODUCT_ID+ " LIKE ?", itemcode, null, null, null);
            cursor.moveToFirst();
            int rows = cursor.getCount();
            if (rows > 0) {
                alertQuantity = builder.create();
                alertQuantity.show();

                btnEnter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            if(etQuan.getText().toString().equals("")){
                                Toast.makeText(getApplicationContext(), "Please Enter Quantity.", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                dialogVar = Integer.parseInt(etQuan.getText().toString());

                                //END OF SUPPLIER FOR ADD PRODUCTS

                                if(invoiceItemList != null){
                                    //MARK'S SOLUTION FOR TEMPORARY INVOICING ITEMS
                                    //INSERT TEMP INVOICE ITEMS INTO TABLE
                                    try {
                                        InvoiceItem invoiceItem = new InvoiceItem();

                                        String convertedCode = code.trim();
                                        Double prodPrice = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRODUCT_PRICE));
                                        String prodName = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_NAME));
                                        String prodDes = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_DESCRIPTION));
                                        String prodId = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_ID));

                                        int result = db_data.searchDuplicateInvoice(convertedCode);
                                        if(result > 0){
                                            SQLiteDatabase db = db_data.getReadableDatabase();
                                            String SELECT_QUERY = "SELECT "+ COLUMN_TEMP_QUANTITY + " , "+ COLUMN_TEMP_TOTALPRICE + " , "+ COLUMN_TEMP_PRICE +" FROM " + TABLE_TEMP_INVOICING + " WHERE " + COLUMN_TEMP_ID + "='"+ convertedCode +"'";
                                            Cursor cursor = db.rawQuery(SELECT_QUERY,null);
                                            cursor.moveToFirst();
                                            int retrievedQuantity = cursor.getInt(0);
                                            double retrievedTotalPrice = cursor.getDouble(1);
                                            double retrievedProce = cursor.getDouble(2);
                                            double totalNaTalaga = retrievedTotalPrice + (retrievedProce * dialogVar);
                                            cursor.close();

//                                            double marksTotal = db_data.totalPrice();
                                            db_data.updateInvoiceItem(convertedCode,retrievedQuantity + dialogVar,totalNaTalaga);
                                            Toast.makeText(Cashier.this, "Quantity Updated!", Toast.LENGTH_SHORT).show();
                                        }
                                        else if(result == 0){ //IF DOESN'T HAVE DUPLICATE
                                            invoiceItem.setInvoiceProductName(prodName);
                                            invoiceItem.setInvoiceProductDescription(prodDes);
                                            invoiceItem.setInvoiceProductPrice(prodPrice);//toAdd item
                                            invoiceItem.setInvoiceProductQuantity(dialogVar);//toAdd item
                                            invoiceItem.setInvoiceProductID(prodId);//toAdd item
                                            invoiceItem.setInvoiceProductTotal(prodPrice*dialogVar);//toAdd item

                                            db_data.insertTempInvoice(invoiceItem);
                                        }
                                        isOn = true;
                                        refreshPaymentInformation();

                                        String itemPriceConverted = String.valueOf(itempriceCol);
                                        int sizeOfChar = itemPriceConverted.trim().length();
                                        int lengthOfCharToBeAdded = 20 - sizeOfChar;
                                        writeDataToSerial(itemdescCol,itemPriceConverted, String.valueOf(lengthOfCharToBeAdded));

                                        refreshRecyclerView();

                                        alertQuantity.dismiss();
                                    } catch (SQLiteException e){
                                        e.printStackTrace();
                                    }
                                } else {
                                    cancelna();
                                }
                            }
                        }catch (Exception ex){
                            ex.printStackTrace();
//                            lbl_due.setText(subTotal2);
                        }
                    }
                });
                txt_search.setText("");
            } else {
                Toast.makeText(this, "Product can't be found", Toast.LENGTH_LONG).show();
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    void enterCreditDetails() {
        try {
            creditBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_creditcard, null);
            creditBuilder.setView(alertLayout);
            final AppCompatButton btnEnterCredit = (AppCompatButton) alertLayout.findViewById(R.id.btnEnterCredit);
            etCreditPayment = (AppCompatEditText) alertLayout.findViewById(R.id.etCreditPayment);
            etCreditBank = (AppCompatEditText) alertLayout.findViewById(R.id.etCreditBank);
            etCreditNumber = (AppCompatEditText) alertLayout.findViewById(R.id.etCreditNumber);
            etCreditExpiry = (AppCompatEditText) alertLayout.findViewById(R.id.etCreditExpiry);

            alertCredit = creditBuilder.create();
            alertCredit.show();

            btnEnterCredit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (etCreditPayment.getText().toString().equals("") || etCreditBank.getText().toString().equals("") || etCreditNumber.getText().toString().equals("") || etCreditExpiry.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), "Please Fill all Fields.", Toast.LENGTH_SHORT).show();
                        } else {
                            creditPayment = Double.parseDouble(etCreditPayment.getText().toString());
                            creditBank = etCreditBank.getText().toString();
                            creditNumber = etCreditNumber.getText().toString();
                            creditExpiry = etCreditExpiry.getText().toString();
                            creditStatus = 1;
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

        //HERE HERE HERE HERE
        mPriceTotal = db_data.totalPrice();
        mVattable = mPriceTotal / 1.12;
        mTax = mVattable * mTaxPercent; //.12
        mTotalDiscount = mVattable * discount;
        mTotal = mVattable + mTax - mTotalDiscount;
        customerCash = txt_cash.getText().toString().replaceAll("[P,$]","");
        if (customerCash.equals(null) || customerCash.equals("")) customerCash = "0";
        double mDoubleCustomerCash = Double.parseDouble(customerCash) + creditPayment;
        mDue = mDoubleCustomerCash - mTotal;

        mVattableConverted = NumberFormat.getCurrencyInstance().format((mVattable / 1));
        mTaxConverted = NumberFormat.getCurrencyInstance().format((mTax / 1));
        mSubTotalConverted = NumberFormat.getCurrencyInstance().format((mTotal / 1));
        mTotalDiscountConverted = NumberFormat.getCurrencyInstance().format((mTotalDiscount / 1));
        mDueConverted = NumberFormat.getCurrencyInstance().format((mDue / 1));

        lbl_sub.setText("" + mVattableConverted.replace("$","P") + "");
        lbl_tax.setText("" + mTaxConverted.replace("$","P") + "");
        lbl_total.setText("" + mSubTotalConverted.replace("$","P") + "");
        lbl_discount.setText("" + mTotalDiscountConverted.replace("$","P") + "");
        lbl_due.setText("P" + mDueConverted.replaceAll("[$()]","") + "");

        cleanVattable = mVattableConverted.replaceAll("[$,]","");
        cleanSubtotal = mSubTotalConverted.replaceAll("[$,]","");
        cleanTax = mTotalDiscountConverted.replaceAll("[$,]","");

        if (mDue < 0) {
            btn_print.setEnabled(false);
            lbl_dc.setText("" + "Due" + "");
        } else if (mDue >= 0 && invoiceAdapter.getItemCount() > 0) {
            btn_print.setEnabled(true);
            btn_print.setText("" + "Print Receipt" + "");
            lbl_dc.setText("" + "Change" + "");
        }
        writeDataToSerial("Total : " + lbl_total.getText().toString(),"Change : " + lbl_due.getText().toString(),"");
    }

    //BUTTON PRINT
    public void print(View view) throws ParseException {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy");
        dateformatted = dateformat.format(c.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        currentTime = sdf.format(new Date());
        products.add("ABZTRAK DEMO STORE");
        products.add("VAT REG TIN:000-111-111-001");
        products.add("MIN:12345678901234567");
        products.add("670 SGT BUMATAY STREET");
        products.add("PLAINVIEW, MANDALUYONG");
        products.add("SERIAL NO. ASDFG1234567890");
        products.add("PTU No. FP121234-123-1234567-12345");
        products.add("===============================");
        products.add("CASH INVOICE");
        products.add("Date: \t\t\t\t "+dateformatted+" \t "+currentTime);
        products.add("-------------------------------");
        products.add("Name \t\t"+"Quantity \t\t"+"Price");
        transType = "invoice";

        Date currDate = new Date();
        final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MMM-dd-yyyy hh:mm a");
        String dateToStr = dateTimeFormat.format(currDate);
        Date strToDate = dateTimeFormat.parse(dateToStr);
        String customerCash = txt_cash.getText().toString().replaceAll("[P,]", "");
        try {
            String dateToString = strToDate.toString();
            db_data.addTransaction(transType,dateToString,userNum,0,0);
            String[] itemID = new String[]{_ID, COLUMN_TRANSACTION_TYPE};
            Cursor cursor1 = dbReader.query(TABLE_TRANSACTION, itemID, null, null, null, null, null);
            cursor1.moveToLast();
            transNumber = cursor1.getInt(0); //COLUMN _ID of TABLE_TRANSACTION
            cursor1.close();

            //MARK : I UPDATED THIS PART FOR addInvoice discounted and so on---
            String customerDiscount = lbl_discount.getText().toString().trim();
            //--END
            //// TODO: 7/15/2017
            try{
                db_data.addInvoice(transNumber + "", cleanTax, cleanSubtotal, inPrint, userNum, "0", "0", cleanVattable, mTax, discType + "", creditPayment, dateToStr, creditNumber, creditExpiry);
                if (creditStatus == 1) {
                    db_data.addCredit(transNumber + "", userNum, dateToString, creditPayment, creditBank, creditNumber, creditExpiry);
                }

                Log.e("AddInvoice : ", "trans#"+transNumber+ "|disc"+discount.toString()+ "|subtotyal"+cleanSubtotal+"|print"+inPrint+ "|cashier"+userNum+ "|clean vattable: "+cleanVattable+"|tax: "+mTax+"|disc typee"+discType+"|Date:"+dateToStr+"");
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

                    products.add("" + receivedCursorFromTemp.getString(receivedCursorFromTemp.getColumnIndex(COLUMN_TEMP_DESCRIPTION)) +
                                "\t" + receivedCursorFromTemp.getString(receivedCursorFromTemp.getColumnIndex(COLUMN_TEMP_QUANTITY)) +
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
            products.add("Invoice Number " + abc + "");
            products.add(quantityCount + " item(s)");
            products.add("Vatable" + "" + "\t\t" + mVattableConverted.replace("$","P"));
            products.add("Vat" + "" + "\t\t" + mTaxConverted.replace("$","P"));
            products.add("Total" + "\t\t" + mSubTotalConverted.replace("$","P"));

            //customerCash = txt_cash.getText().toString().replace(",", "");
//            double change = dCustomerCash - totalPrice;

            products.add("\t\t\t\tCash" + "\t\t\t\t" + txt_cash.getText().toString());
            products.add("\t\t\t\tChange" + "\t\t\t" + "P" + mDueConverted.replaceAll("[$()]",""));
            products.add("\n\n\n\n\n\n");

            //CHECK IF PRINTERS ARE OPEN
//            boolean ret = marksPrinter.Open();

            String[] printBaKamo = products.toArray(new String[products.size()]);
            String printBaHanapMo="";
            for (int p=0; p<products.size();p++){
                printBaHanapMo = printBaHanapMo +"\n"+ printBaKamo[p];
            }
            db_data.updateInvoice(abc,printBaHanapMo);
            double mCashBaKamo = 0.0;
            try {
                mCashBaKamo = Double.parseDouble(customerCash);
            } catch (Exception e) {
                mCashBaKamo = 0.0;
            }
            double doubleCustomer = mCashBaKamo + creditPayment;

            if (invoiceAdapter.getItemCount() > 0 && doubleCustomer >= mDue) {
                    //JOLLIMARK PRINTER
//                    unLockCashBox();
                printFunction(products);
                reprint = products;
                reprint.add(0,"DUPLICATE");
                btn_print.setEnabled(false);
                products.clear();
            }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
//                itemQuantityList.clear();
                itemPriceList.clear();
                itemNameList.clear();
//                itemCodeList.clear();
        }
        cancelna();
        btn_print.setEnabled(false);
        writeDataToSerial("SALAMAT PO!","PAWIL KINI!","");
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

                Date currDate = new Date();
                final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MMM-dd-yyyy hh:mm a");
                String dateToStr = dateTimeFormat.format(currDate);
                Date strToDate = null;
                try {
                    strToDate = dateTimeFormat.parse(dateToStr);
                    transType ="cancel";
                    String dateToString = strToDate.toString();
                    db_data.addTransaction(transType,dateToString,userNum,0,0);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                cancelna();
                t2Rows.clear();
                products.clear();
                return true;
            case R.id.action_vieworder:
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onBackPressed(){
        tab_host.setCurrentTab(0);
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
        itemcodeCol=0;
//        itemCodeList.clear();
        itemnameCol="";
        itemNameList.clear();
//        itemQuantityList.clear();
        itemPriceList.clear();
        due=0.00;
        due2="";
        subTotal=0.00;
        subTotal2="";

        db_data.deleteAllTempItemInvoice();

    }

    public void cashierLogOut(View view){
        cancelna();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy hh:mm a",Locale.SIMPLIFIED_CHINESE);
        dateformatted = dateformat.format(c.getTime());
        dbWriter.execSQL("INSERT INTO sessions(time,date,username) VALUES(time('now'),date('now'),'"+ userNum +"') ");
        finish();
        sleep(1000);
    }

    public void xreport(View view){
        //FOR SAVING X REPORT
        try{
            transType = "xreport";
            Date currDate = new Date();
            final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MMM-dd-yyyy hh:mm a");
            String dateToStr = dateTimeFormat.format(currDate);
            Date strToDate = dateTimeFormat.parse(dateToStr);
            int bcd;
            String dateToString = strToDate.toString();
            db_data.addTransaction(transType,dateToString,userNum,0,0);
            String[] itemID = new String[]{_ID, COLUMN_TRANSACTION_TYPE};
            Cursor cursor1 = dbReader.query(TABLE_TRANSACTION, itemID, null, null, null, null, null);
            cursor1.moveToLast();
            bcd = cursor1.getInt(0); //COLUMN _ID of TABLE_TRANSACTION
            cursor1.close();

            reportBaKamo.setDb_data(db_data);

            reportBaKamo.main(userNum,dateToString,bcd,enteredCashDrawer);
            ArrayList<String> paPrintNaman;
            paPrintNaman = reportBaKamo.getToBePrinted();

//            unLockCashBox();
            printFunction(paPrintNaman);
            paPrintNaman.clear();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void zreport(View view){
        //FOR SAVING Z REPORT
        try {
            transType = "zreport";
            Date currDate = new Date();
            final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MMM-dd-yyyy hh:mm a");
            String dateToStr = dateTimeFormat.format(currDate);
            Date strToDate = dateTimeFormat.parse(dateToStr);
            String dateToString = strToDate.toString();

            int bcd;
            db_data.addTransaction(transType, dateToString, userNum, 0, 0);
            String[] itemID = new String[]{_ID, COLUMN_TRANSACTION_TYPE};
            Cursor cursor1 = dbReader.query(TABLE_TRANSACTION, itemID, null, null, null, null, null);
            cursor1.moveToLast();
            bcd = cursor1.getInt(0); //COLUMN _ID of TABLE_TRANSACTION
            cursor1.close();


            reportBaKamo.setDb_data(db_data);
            reportBaKamo.main("no", dateToString, bcd, enteredCashDrawer);
            ArrayList<String> paPrintNaman;
            paPrintNaman = reportBaKamo.getToBePrinted();



            //unLockCashBox();
            printFunction(paPrintNaman);
//            retrievedCursorFromJoinTable.close();

            //EXPORT TO CSV
            //Cursor retrievedCursorFromReportBaKaMo = reportBaKamo.getCursorInReportBaKamo();
            Cursor retrievedCursorFromJoinTable;
            Cursor cursorDummy = null;

            String joinTableQuery = "SELECT c1."+ _ID+ " as ID, "+
                    "c1."+COLUMN_PRODUCT_ID + " as CODE," +
                    "c1."+COLUMN_PRODUCT_NAME + " as ITEM," +
                    "c2." + COLUMN_PRODUCT_QUANTITY_TEMP + " as BEGINNING," +
                    "SUM(c3."+COLUMN_ITEM_QUANTITY + ") as SALES," +
                    "c1."+COLUMN_PRODUCT_QUANTITY +
                    " as ENDING FROM " + TABLE_PRODUCT + " c1 " +
                    " INNER JOIN " + TABLE_PRODUCT_TEMP + " c2 " +
                    " ON " + "c1."+COLUMN_PRODUCT_ID + "= c2."+COLUMN_PRODUCT_ID_TEMP +
                    " LEFT JOIN " + TABLE_ITEM + " c3 " +
                    " ON " + "c1."+COLUMN_PRODUCT_ID + "= c3."+COLUMN_ITEM_PRODUCT +
                    " AND c3." + COLUMN_ITEM_ZREPORT + "= 0"+
                    " GROUP BY " + COLUMN_PRODUCT_ID + ";";
            retrievedCursorFromJoinTable = dbReader.rawQuery(joinTableQuery,null);


            zreportExportFunction.showDialogLoading(Cashier.this,retrievedCursorFromJoinTable,cursorDummy);
//            boolean sent =
//            if(sent){
//                zreportExportFunction.closeDialog();
//            }
            //END OF EXPORT CSV

            db_data.updateTransactions(userNum);

            //GETTING QUANTITY SALES
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_ITEM_ZREPORT, 1);
            String whereBaKamo = COLUMN_ITEM_ZREPORT + "= ?";
            String[] WhereArgBaKamo = {"0"};
            dbWriter.update(TABLE_ITEM, cv, whereBaKamo, WhereArgBaKamo);
            //END OF QUANTITY SALES

            paPrintNaman.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//94 3 97
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

        //Mark's Initialization
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_invoice);

        btn_cashier_delete = (ImageButton) findViewById(R.id.btn_cashier_delete);
        btn_cashier_confirmDelete = (Button) findViewById(R.id.btn_cashier_confirmDelete);

        btnCreditCard = (Button)findViewById(R.id.btnCredit);
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
            } else {

                Toast.makeText(this, "connected : OK" , Toast.LENGTH_SHORT).show();
                Log.d(TAG, "connected : OK");
                Log.d(TAG, "Exit  openUsbSerial");


            }
        }//isConnected
        else {
            Toast.makeText(this, "Connected failed, Please plug in PL2303 cable again!" , Toast.LENGTH_SHORT).show();
            Log.d(TAG, "connected failed, Please plug in PL2303 cable again!");

        }
    }//openUsbSerial

    private void writeDataToSerial(String printMe1st,String printMe2nd,String lengthToLeft) {

        Log.d(TAG, "Enter writeDataToSerial");

        if(null==mSerial)
            return;

        if(!mSerial.isConnected())
            return;

        String strWrite;
        //strWrite = String.format("%1$-40" + "s", "Mae Loves Mark <3."); //20 char per line x2 = 40 chars
        strWrite = String.format("%1$-20s" + "%2$-20s" , printMe1st, printMe2nd);

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

    public void reprintBakamo(View view){
        printFunction(reprint);

        reprint.clear();
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