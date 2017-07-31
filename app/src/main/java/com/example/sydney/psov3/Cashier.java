package com.example.sydney.psov3;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.annotation.IdRes;
import android.os.Bundle;
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

import static com.example.sydney.psov3.Constants.*;

public class Cashier extends AppCompatActivity {
    ArrayList<String> itemCode123 = new ArrayList<>();
    ArrayList<String> itemQuan123 = new ArrayList<>();
    private int quantityCount = 0,itemcodeCol,discType=0,dialogVar;
    private long code=0;
    String userNum;
    Double vattable,vat,subTotal=0.0,itempriceCol,itempricetotalCol,discount=0.0,discounted,totalPrice;
    Double due,payment;
    Cursor cursor;
    DB_Data db_data;
    ContentValues cv;
    private List<InvoiceItem> invoiceItemList;
    private RecyclerView recyclerView;

    EditText txt_search,txt_cash;
    String itemnameCol,itemdescCol,formatted,vat2,vattable2,subTotal2,due2,totalPrice2,itempricetotalCol2,discount2,discounted2;
    ArrayList<Integer>itemQuantityList = new ArrayList<>();
    ArrayList<Double>itemPriceList = new ArrayList<>();
    ArrayList<String>itemNameList = new ArrayList<>();
    ArrayList<String>itemDescList = new ArrayList<>();
    ArrayList<Integer>itemCodeList  = new ArrayList<>();
    ArrayList<Double> total = new ArrayList<>();
    RelativeLayout layout;
    ArrayList<String> products = new ArrayList<>();
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
    private GridLayoutManager mLayoutManager;
    ReportBaKamo reportBaKamo;

    private String customerCash;
    private Double dCustomerCash,change;

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

    double enteredCashDrawer;
    int transNumber;


    //Dialog for Enter the Quantity
    AlertDialog.Builder builder;
    AlertDialog alertQuantity;

    //Variables for txt in Payment Info

    double mPriceTotal;
    double mVattable;
    double mTax;
    double mTotalDiscount;
    double mTotal;
    String mcustomerCash;
    double mDue;
    double mTaxPercent;

    String mVattableConverted;
    String mTaxConverted;
    String mSubTotalConverted;
    String mTotalDiscountConverted;
    String mDueConverted;

    double parsed = 0.0;
    static {AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);}    //TO SUPPORT VECTOR DRAWABLES



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

        discount = 0.0;
        mTaxPercent = .12;
        refreshPaymentInformation();

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
//        cancelna();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy");
        dateformatted = dateformat.format(c.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        currentTime = sdf.format(new Date());
        dbWriter.execSQL("INSERT INTO sessions(time,date,username) VALUES(time('now'),date('now'),'"+ userNum +"') ");

        txt_cash.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            private String current = "";
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
                        if(!invoiceItemList.isEmpty()){
                            //START OF COMPUTATION UPPER

                            double marksTotal = db_data.totalPrice();
                            totalPrice = marksTotal; //subTotal

                            vattable = totalPrice / 1.12;
                            vat = vattable * 0.12;
                            vattable2 = NumberFormat.getCurrencyInstance().format((vattable / 1));
                            vat2 = NumberFormat.getCurrencyInstance().format((vat / 1));
                            subTotal2 = NumberFormat.getCurrencyInstance().format((totalPrice / 1));
                            vat2 = vat2.replace("$", "");
                            vattable2 = vattable2.replace("$", "");
                            subTotal2 = subTotal2.replace("$", "");

                            lbl_sub.setText("" + vattable2 + "");
                            lbl_tax.setText("" + vat2 + "");
                            lbl_total.setText("" + subTotal2 + "");
                            ArrayList<String> temp = new ArrayList<>();
                            temp.add(itemnameCol);//example I don't know the order you need
                            temp.add(itempriceCol + "");//example I don't know the order you need
                            temp.add(dialogVar + "");//example I don't know the order you need
                            temp.add(itempricetotalCol2);//example I don't know the order you ne
                            t2Rows.add(temp);
                            //END OF COMPUTATION UPPER

                            due = totalPrice;
                            due2 = NumberFormat.getCurrencyInstance().format((marksTotal / 1));//subTotal
                            due2 = due2.replace("$", "");
                            lbl_due.setText("" + marksTotal + "");

                            Toast.makeText(Cashier.this, "ITEM DELETED!", Toast.LENGTH_SHORT).show();
                        } else {
                            total.clear();
                            cancelna();
                        }
                        //END
                    }
                }
            }
        });

        //START AUTO COMPUTE WHEN APP CLOSES
        if(invoiceItemList != null){
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

    private List<InvoiceItem> fill_with_data() {
        invoiceItemList = new ArrayList<>();
        invoiceItemList.clear();

        SQLiteDatabase db = db_data.getReadableDatabase();
        String SELECT_QUERY = "SELECT * FROM " + TABLE_TEMP_INVOICING;

        Cursor cursor = db.rawQuery(SELECT_QUERY,null);

        String invoiceItemDescription,invoiceItemID;
        Double invoiceItemPrice,invoiceItemTotal;
        int invoiceItemQuantity;

        while(cursor.moveToNext()){
            invoiceItemDescription = cursor.getString(1);
            invoiceItemPrice = cursor.getDouble(2);
            String dummyVattable = "";
            invoiceItemQuantity = cursor.getInt(3);
            invoiceItemID = cursor.getString(4);
            invoiceItemTotal = cursor.getDouble(5);

            invoiceItemList.add(new InvoiceItem(invoiceItemDescription,invoiceItemPrice,dummyVattable,invoiceItemQuantity,invoiceItemID,invoiceItemTotal));
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
            final AppCompatEditText etQuan = (AppCompatEditText) alertLayout.findViewById(R.id.etEnterQuantity);

            code = Long.parseLong(txt_search.getText().toString());
            final String[] itemcode = {Long.toString(code)};
            itemCode123.add(Long.toString(code));
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
                                itemQuan123.add(etQuan.getText().toString());
                                itempriceCol = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRODUCT_PRICE));
                                itemnameCol = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_NAME));
                                itemdescCol = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_DESCRIPTION));
                                itemcodeCol = cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_ID));

                                //START OF SUPPLIER FOR ADD PRODUCTS
                                //ADD TO ARRAYLIST FOR EACH FIELD IN PRODUCTS
                                itemQuantityList.add(dialogVar);
                                itemPriceList.add(itempriceCol);
                                itemNameList.add(itemnameCol);
                                itemCodeList.add(itemcodeCol);
                                itemDescList.add(itemdescCol);
                                itempricetotalCol = dialogVar * itempriceCol;
                                itempricetotalCol2 = NumberFormat.getCurrencyInstance().format((itempricetotalCol/1));
                                itempricetotalCol2 = itempricetotalCol2.replace("$","");
                                ArrayList<Double> total = new ArrayList<>();
                                total.add(itempricetotalCol);
                                layout.invalidate();
                                layout.requestLayout();
                                for (int x = 0; x < total.size(); x++) {
                                    subTotal = subTotal + total.get(x);
                                    quantityCount++;
                                }
                                //END OF SUPPLIER FOR ADD PRODUCTS

                                if(invoiceItemList != null){
                                    //MARK'S SOLUTION FOR TEMPORARY INVOICING ITEMS
                                    //INSERT TEMP INVOICE ITEMS INTO TABLE
                                    try {
                                        InvoiceItem invoiceItem = new InvoiceItem();

                                        String convertedCode = Long.toString(code).trim();
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
                                            invoiceItem.setInvoiceProductDescription(itemnameCol);
                                            invoiceItem.setInvoiceProductPrice(itempriceCol);
                                            invoiceItem.setInvoiceProductQuantity(dialogVar);
                                            invoiceItem.setInvoiceProductID(convertedCode);
                                            invoiceItem.setInvoiceProductTotal(itempriceCol*dialogVar);

                                            db_data.insertTempInvoice(invoiceItem);
                                        }

                                        refreshPaymentInformation();
                                        refreshRecyclerView();

//                                        double marksTotal = db_data.totalPrice();
//                                        Double mtotalPrice = marksTotal; //subTotal
//
//                                        //START OF COMPUTATION UPPER
//                                        vattable = mtotalPrice / 1.12;
//                                        vat = vattable * 0.12;
//                                        vattable2 = NumberFormat.getCurrencyInstance().format((vattable / 1));
//                                        vat2 = NumberFormat.getCurrencyInstance().format((vat / 1));
//                                        subTotal2 = NumberFormat.getCurrencyInstance().format((subTotal / 1));
//                                        vat2 = vat2.replace("$", "");
//                                        vattable2 = vattable2.replace("$", "");
//                                        subTotal2 = subTotal2.replace("$", "");
//
//                                        lbl_sub.setText("" + vattable2 + "");
//                                        lbl_tax.setText("" + vat2 + "");
//                                        lbl_total.setText("" + subTotal2 + "");
//                                        ArrayList<String> temp = new ArrayList<>();
//                                        temp.add(itemnameCol);//example I don't know the order you need
//                                        temp.add(itempriceCol + "");//example I don't know the order you need
//                                        temp.add(dialogVar + "");//example I don't know the order you need
//                                        temp.add(itempricetotalCol2);//example I don't know the order you ne
//                                        t2Rows.add(temp);
//                                        //END OF COMPUTATION UPPER
//
//                                        due = subTotal;
//                                        due2 = NumberFormat.getCurrencyInstance().format((marksTotal / 1));
//                                        due2 = due2.replace("$", "");
//                                        lbl_due.setText("" + marksTotal + "");

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
                            lbl_due.setText(subTotal2);
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

    private void refreshPaymentInformation() {
        //HERE HERE HERE HERE
        mPriceTotal = db_data.totalPrice();
        mVattable = mPriceTotal / 1.12;
        mTax = mVattable * mTaxPercent; //.12
        mTotalDiscount = mVattable * discount;
        mTotal = mVattable + mTax - mTotalDiscount;
        customerCash = txt_cash.getText().toString().replaceAll("[P,$]","");
        if (customerCash.equals(null) || customerCash.equals("")){customerCash = "0";}
        double mDoubleCustomerCash = 0.0;
        mDoubleCustomerCash = Double.parseDouble(customerCash);
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

        if (mDue < 0) {
            btn_print.setEnabled(false);
            lbl_dc.setText("" + "Due" + "");
        } else if (mDue >= 0) {
            btn_print.setEnabled(true);
            btn_print.setText("" + "Print Receipt" + "");
            lbl_dc.setText("" + "Change" + "");
//                            formatted = NumberFormat.getCurrencyInstance().format((change / 1));
//                            payment = Double.parseDouble(formatted.replaceAll("[$-,]", ""));
//                            formatted = formatted.replaceAll("[$-]", "P");
//                            lbl_due.setText(formatted);
        }

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
            db_data.addInvoice(transNumber+"",discount.toString(),mSubTotalConverted,inPrint,userNum,inZreport,inXreport,mVattableConverted,mTax,discType+"", 0.0,"","","");
            String[] SELECT_QUERY = new String[]{_ID};
            Cursor cursor = dbReader.query(TABLE_INVOICE, SELECT_QUERY, null, null, null, null, null);
            cursor.moveToLast();
            String abc = cursor.getString(0);
            String[] itemCode12345 = itemCode123.toArray(new String[itemCode123.size()]);
            String[] itemQuan12345 = itemQuan123.toArray(new String[itemQuan123.size()]);
            String[] itemName12345 = itemNameList.toArray(new String[itemNameList.size()]);
            String[] itemDesc12345 = itemNameList.toArray(new String[itemNameList.size()]);
            Double[] itemPrice12345 = itemPriceList.toArray(new Double[itemPriceList.size()]);
            cursor.close();
            itemCode123.clear();
            itemQuan123.clear();
            itemNameList.clear();
            itemDescList.clear();
            itemPriceList.clear();
            for (int a = 0; a < t2Rows.size(); a++){
                db_data.addItem(abc,itemCode12345[a],itemQuan12345[a],0,itemName12345[a],itemDesc12345[a],itemPrice12345[a],userNum);
                products.add("" + itemName12345[a] + "\t" + itemQuan12345[a] + "\t" + itemPrice12345[a] * Double.parseDouble(itemQuan12345[a]) + "");
            }
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

            double doubleCustomerCash = Double.parseDouble(customerCash);

            if( invoiceItemList != null && doubleCustomerCash >= mDue ) {
                    //JOLLIMARK PRINTER
                    unLockCashBox();
                    printFunction(products);
                    btn_print.setEnabled(false);
                    products.clear();
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                itemQuantityList.clear();
                itemPriceList.clear();
                itemNameList.clear();
                itemCodeList.clear();
        }
        cancelna();
        btn_print.setEnabled(false);

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
        itemCodeList.clear();
        itemnameCol="";
        itemNameList.clear();
        itemQuantityList.clear();
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
        reportBaKamo.setDb_data(db_data);
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
            int bcd;
            String dateToString = strToDate.toString();
            db_data.addTransaction(transType, dateToString, userNum, 0, 0);
            String[] itemID = new String[]{_ID, COLUMN_TRANSACTION_TYPE};
            Cursor cursor1 = dbReader.query(TABLE_TRANSACTION, itemID, null, null, null, null, null);
            cursor1.moveToLast();
            bcd = cursor1.getInt(0); //COLUMN _ID of TABLE_TRANSACTION
            cursor1.close();

            reportBaKamo.setDb_data(db_data);
            reportBaKamo.main("no", dateToString, bcd, enteredCashDrawer);
            ArrayList<String> paPrintNaman = new ArrayList<>();
            paPrintNaman = reportBaKamo.getToBePrinted();


            unLockCashBox();
            printFunction(paPrintNaman);
            paPrintNaman.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sleep(int ms) {
        try {
            java.lang.Thread.sleep(ms);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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
        InvoiceAdapter invoiceAdapter = new InvoiceAdapter(getApplication(), invoiceItemList);
        mLayoutManager = new GridLayoutManager(Cashier.this,2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(invoiceAdapter);
        invoiceAdapter.notifyDataSetChanged();
    }

    public static boolean unLockCashBox(){
        boolean retnValue = false;
        UsbPrinter tmpUsbDev = new UsbPrinter();
        retnValue = tmpUsbDev.UnLockOfCashBox();

        return retnValue;
    }
}