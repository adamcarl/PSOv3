package com.example.sydney.psov3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.hardware.usb.UsbManager;
import android.support.annotation.IdRes;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
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
import	android.content.pm.ActivityInfo;

import tw.com.prolific.driver.pl2303.PL2303Driver;
import android.hardware.usb.UsbManager;
import android.util.Log;

import static com.example.sydney.psov3.Constants.*;

public class Cashier extends AppCompatActivity {
    private static final String ACTION_USB_PERMISSION = "com.prolific.pl2303hxdsimpletest.USB_PERMISSION";

    ArrayList<String> itemCode123 = new ArrayList<>();
    ArrayList<String> itemQuan123 = new ArrayList<>();
    private int quantityCount = 0,itemcodeCol,discType=0,dialogVar;
    private long code=0;
    String userNum;
    Double vattable,vat,subTotal,itempriceCol,itempricetotalCol,discount=0.0,discounted,totalPrice;
    Double due,payment;
    Cursor cursor;
    DB_Data db_data;
    ContentValues cv;
//    ArrayList<String> items;
    private List<InvoiceItem> invoiceItemList;
    private RecyclerView recyclerView;
    private InvoiceAdapter invoiceAdapter;

    EditText txt_search,txt_cash;
    String itemnameCol,itemdescCol,formatted,vat2,vattable2,subTotal2,due2,totalPrice2,itempricetotalCol2,discount2,discounted2;
    ArrayList<Integer>itemQuantityList = new ArrayList<>();
    ArrayList<Double>itemPriceList = new ArrayList<>();
    ArrayList<String>itemNameList = new ArrayList<>();
    ArrayList<String>itemDescList = new ArrayList<>();
    ArrayList<Integer>itemCodeList  = new ArrayList<>();
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
    String[] forLog;
    String transType;
    Date currentDateTime;
    private GridLayoutManager mLayoutManager;
    ReportBaKamo reportBaKamo;

    private String customerCash;
    private Double dCustomerCash,change;

    private AlertDialog.Builder builder = null;
    private AlertDialog alertDialogXreport = null;
    private AlertDialog alertDialogZreport = null;
    private AlertDialog alertDialogCredit = null;
    private boolean xreportButtonStatus = false;
    private boolean zreportButtonStatus = false;

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

    //FOR BUTTON CLICK LISTENER X & Z REPORT DIALOG
    private String whatButton = "";
    private String whatDialog = "";


    //JMPRINTER VARIABLES
    private JmPrinter mPrinter;
    private UsbPrinter marksPrinter = new UsbPrinter();

    double enteredCashDrawer;
    int transNumber;

    static {AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);}    //TO SUPPORT VECTOR DRAWABLES

    private static final boolean SHOW_DEBUG = true;
    String TAG = "PL2303HXD_APLog";
    PL2303Driver mSerial;
    private PL2303Driver.BaudRate mBaudrate = PL2303Driver.BaudRate.B9600;

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

        writeDataToSerial();

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
cancelna();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy");
        dateformatted = dateformat.format(c.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        currentTime = sdf.format(new Date());
        dbWriter.execSQL("INSERT INTO sessions(time,date,username) VALUES(time('now'),date('now'),'"+ userNum +"') ");
        //forLog = db_data.searchStaff(userNum+"");
        //String log = dateformatted+" "+currentTime+". "+forLog[2]+" "+forLog[1]+" with staff number "+forLog[0]+" logged in.";
        //db_data.addLog(log);
//        invoiceItemList = new ArrayList<>();
//        items.add("Quantity");
//        items.add("Name");
//        items.add("Price");
//        items.add("Total Price");

//        final GridView grid = (GridView) findViewById(R.id.grd_sell);
//        grid.setAdapter(new ArrayAdapter<>(this, R.layout.invoice_item_card, items));
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

                        double parsed = Double.parseDouble(cleanString);
                        formatted = NumberFormat.getCurrencyInstance().format((parsed / 100));
                        current = formatted;
                        txt_cash.setText(formatted.replace("$", "P"));
                        txt_cash.setSelection(formatted.length());

                        txt_cash.addTextChangedListener(this);
                        customerCash = txt_cash.getText().toString().replace(",", "");
                        dCustomerCash = Double.parseDouble(customerCash.replace("P", ""));
                        due = totalPrice - dCustomerCash;
                        formatted = NumberFormat.getCurrencyInstance().format((due / 1));
                        formatted = formatted.replace("$", "");

//                        double convertedCash = Double.parseDouble(txt_cash.getText().toString());
                        if (due > 0) {
                            btn_print.setEnabled(false);
                            lbl_dc.setText("" + "Due" + "");
                            lbl_due.setText(formatted);
                        } else if (due <= 0) {
                            change = dCustomerCash - totalPrice;
                            btn_print.setEnabled(true);
                            btn_print.setText("" + "Print Receipt" + "");
                            lbl_dc.setText("" + "Change" + "");
                            formatted = NumberFormat.getCurrencyInstance().format((change / 1));
                            payment = Double.parseDouble(formatted.replaceAll("[$-,]", ""));
                            formatted = formatted.replaceAll("[$-]", "P");
                            lbl_due.setText(formatted);
                        }
                    }
                }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
        });
//        lv_cashier_order.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                txt_admin_prod_id_edit.setText(productArrayList.get(position).getP_id());
//                txt_admin_prod_name_edit.setText(productArrayList.get(position).getP_name());
//                txt_admin_prod_desc_edit.setText(productArrayList.get(position).getP_desc());
//                txt_admin_prod_price_edit.setText(""+productArrayList.get(position).getP_price()+"");
//                txt_admin_prod_quan_edit.setText(""+productArrayList.get(position).getP_quan()+"");
//                ll_admin_product.setVisibility(View.GONE);
//                ll_admin_product_edit.setVisibility(View.VISIBLE);
//            }
//        });
//       grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//           @Override
//           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//               String str = grid.getItemAtPosition(i).toString();
//               System.out.print(str);
//           }
//       });
        rg_discount.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(rb_spdisc.isChecked()){
                    //SENIOR DISCOUNT
                    discType = 1;
                    vat = 0.0;
                    vattable = subTotal / 1.12;
                    discount = vattable * 0.20;
                    discounted = vattable - discount;
                    discount2 = NumberFormat.getCurrencyInstance().format((discount/1));
                    discount2 = discount2.replace("$","");
                    lbl_discount.setText("" + discount2 + "");

                    discounted2 = NumberFormat.getCurrencyInstance().format((discounted/1));
                    discounted2 = discounted2.replace("$","");
                    lbl_total.setText(""+discounted2+"");

                    vat2 = NumberFormat.getCurrencyInstance().format((vat/1));
                    vat2 = vat2.replace("$","");
                    lbl_tax.setText(""+vat2+"");

                    totalPrice = discounted;
                    totalPrice2 = NumberFormat.getCurrencyInstance().format((totalPrice/1));
                    totalPrice2 = totalPrice2.replace("$","");
                    lbl_due.setText(totalPrice2);

                    //SUPPLIER FOR addInvoice(. . .)
                    inVattable = 0.0;
                    inVatted = 0.0;
                    inVatStatus = "x";
                    inSeniorDiscount = discount;
                    inVatExempt = 0.0;
                    inZeroRated = vattable;
                }
                else if(rb_ddisc.isChecked()){
                    //DIPLOMAT DISCOUNT
                    discType=2;
                    vattable = subTotal / 1.12;
                    vat = vattable * 0.0;

                    vat2 = NumberFormat.getCurrencyInstance().format((vat/1));
                    vat2 = vat2.replace("$","");
                    lbl_tax.setText(""+vat2+"");

                    vattable2 = NumberFormat.getCurrencyInstance().format((vattable/1));
                    vattable2 = vattable2.replace("$","");
                    lbl_total.setText(""+vattable2+"");
                    lbl_discount.setText(""+"0.00"+"");
                    totalPrice = vattable;
                    totalPrice2 = NumberFormat.getCurrencyInstance().format((totalPrice/1));
                    totalPrice2 = totalPrice2.replace("$","");
                    lbl_due.setText(totalPrice2);

                    //SUPPLIER FOR addInvoice(. . .)
                    inVattable = 0.0;
                    inVatted = 0.0;
                    inVatStatus = "z";
                    inSeniorDiscount = 0.0;
                    inVatExempt = 0.0;
                    inZeroRated = vattable;
                }
                else{
                    //NO DISCOUNT
                    vattable = subTotal / 1.12;
                    vat = vattable * 0.12;
                    vat2 = NumberFormat.getCurrencyInstance().format((vat/1));
                    vat2 = vat2.replace("$","");
                    lbl_tax.setText(""+vat2+"");
                    discount=0.0;
                    lbl_discount.setText(""+"0.00"+"");
                    subTotal2 = NumberFormat.getCurrencyInstance().format((subTotal/1));
                    subTotal2 = subTotal2.replace("$","");
                    totalPrice = subTotal;
                    totalPrice2 = NumberFormat.getCurrencyInstance().format((totalPrice/1));
                    totalPrice2 = totalPrice2.replace("$","");
                    lbl_due.setText(totalPrice2);
                    lbl_total.setText(""+totalPrice2+"");

                    //SUPPLIER FOR addInvoice(. . .)
                    inVattable = vattable;
                    inVatted = vat;
                    inVatStatus = "v";
                    inSeniorDiscount = 0.0;
                    inVatExempt = 0.0;
                    inZeroRated = 0.0;
                }
            }
        });
        //tab_host.setOnTabChangedListener(new AnimatedTabHostListener(this,tab_host));
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
                        db_data.deleteItemInvoice(item.getInvoiceProductDescription());

                        btn_cashier_confirmDelete.setVisibility(View.INVISIBLE);
                        btn_cashier_delete.setVisibility(View.VISIBLE);

                        refreshRecyclerView();
                        Toast.makeText(Cashier.this, "ITEM DELETED!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

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

    private void createMyDialog() {
        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        final EditText reportTotalCashDrawerX,reportTotalCashDrawerZ;
        final Button btnSubmitX,btnCancelX,btnSubmitZ,btnCancelZ;

        final View alertLayoutXreport = inflater.inflate(R.layout.custom_alertdialog_xreport,null);
        final View alertLayoutZreport = inflater.inflate(R.layout.custom_alertdialog_xreport,null);

        builder.setView(alertLayoutXreport);
        builder.setView(alertLayoutZreport);

        reportTotalCashDrawerX = (EditText) alertLayoutXreport.findViewById(R.id.etTotalCashDrawerX);
        btnSubmitX = (Button) alertLayoutXreport.findViewById(R.id.btnCashDrawerSubmitX);
        btnCancelX = (Button) alertLayoutXreport.findViewById(R.id.btnCashDrawerCancelX);

        reportTotalCashDrawerZ = (EditText) alertLayoutXreport.findViewById(R.id.etTotalCashDrawerZ);
        btnSubmitZ = (Button) alertLayoutXreport.findViewById(R.id.btnCashDrawerSubmitZ);
        btnCancelZ= (Button) alertLayoutXreport.findViewById(R.id.btnCashDrawerCancelZ);

        String[] itemID = new String[]{_ID, COLUMN_TRANSACTION_TYPE};
        Cursor cursor1 = dbReader.query(TABLE_TRANSACTION, itemID, null, null, null, null, null);
        cursor1.moveToLast();
        transNumber = cursor1.getInt(cursor1.getColumnIndex(_ID)); //COLUMN _ID of TABLE_TRANSACTION
        cursor1.close();

        btnSubmitX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String convertedTransNum = Integer.toString(transNumber);
//                double cashSale = db_data.getCashSales(userNum);
                enteredCashDrawer = Double.parseDouble(reportTotalCashDrawerX.getText().toString().trim());
//                double cashShortOver = enteredCashDrawer - cashSale;
//                db_data.addXreport(convertedTransNum,cashSale,enteredCashDrawer,cashShortOver);


            }
        });

        btnSubmitZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String convertedTransNum = Integer.toString(transNumber);
//                double cashSale = db_data.getCashSales(userNum);
                double enteredCashDrawer = Double.parseDouble(reportTotalCashDrawerZ.getText().toString().trim());
//                double cashShortOver = enteredCashDrawer - cashSale;
//                db_data.addXreport(convertedTransNum, cashSale, enteredCashDrawer, cashShortOver);


        }
        });


        btnCancelX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogXreport.dismiss();
            }
        });

        btnCancelZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogZreport.dismiss();
            }
        });
    }

    private List<InvoiceItem> fill_with_data() {
        invoiceItemList = new ArrayList<>();
        invoiceItemList.clear();

        SQLiteDatabase db = db_data.getReadableDatabase();
//        String SELECT_QUERY = "SELECT " + COLUMN_PRODUCT_DESCRIPTION + "," + COLUMN_PRODUCT_PRICE + "," + COLUMN_PRODUCT_VATABLE +
//                " FROM " + TABLE_PRODUCT + " WHERE " + code + "=" + COLUMN_PRODUCT_ID;
        String SELECT_QUERY = "SELECT * FROM " + TABLE_TEMP_INVOICING;

        Cursor cursor = db.rawQuery(SELECT_QUERY,null);

        String invoiceItemDescription,invoiceItemID;
        Double invoiceItemPrice;
        int invoiceItemQuantity;

        while(cursor.moveToNext()){
            invoiceItemDescription = cursor.getString(1);
            invoiceItemPrice = cursor.getDouble(2);
            String dummyVattable = "";
            invoiceItemQuantity = cursor.getInt(3);
            invoiceItemID = cursor.getString(4);

//            invoiceItemList.add(new InvoiceItem(invoiceItemDescription,invoiceItemPrice,dummyVattable,invoiceItemQuantity,invoiceItemID));
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
            code = Long.parseLong(txt_search.getText().toString());
            final String[] itemcode = {Long.toString(code)};
            itemCode123.add(Long.toString(code));
            String[] WHERE = {COLUMN_PRODUCT_ID, COLUMN_PRODUCT_NAME, COLUMN_PRODUCT_DESCRIPTION,COLUMN_PRODUCT_QUANTITY, COLUMN_PRODUCT_PRICE};
            cursor = dbReader.query(TABLE_PRODUCT, WHERE, COLUMN_PRODUCT_ID+ " LIKE ?", itemcode, null, null, null);
            cursor.moveToFirst();
            int rows = cursor.getCount();
            if (rows >= 1) {
                // 1. Instantiate an AlertDialog.Builder with its constructor
                final EditText dialogText = new EditText(this);
                dialogText.setInputType(InputType.TYPE_CLASS_NUMBER);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Enter Quantity");
                builder.setView(dialogText);
                txt_search.setText("");
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        try {
                                if (dialogText.getText().toString().equals("")){
                                    Toast.makeText(getApplicationContext(), "Please Enter Quantity.", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    dialogVar = Integer.parseInt(dialogText.getText().toString());
                                    itemQuan123.add(dialogText.getText().toString());
                                    itempriceCol = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRODUCT_PRICE));
                                    itemnameCol = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_NAME));
                                    itemdescCol = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_DESCRIPTION));
                                    itemcodeCol = cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_ID));
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
                                    vattable = subTotal / 1.12;
                                    vat = vattable * 0.12;
                                    vattable2 = NumberFormat.getCurrencyInstance().format((vattable / 1));
                                    vat2 = NumberFormat.getCurrencyInstance().format((vat / 1));
                                    subTotal2 = NumberFormat.getCurrencyInstance().format((subTotal / 1));
                                    vat2 = vat2.replace("$", "");
                                    vattable2 = vattable2.replace("$", "");
                                    subTotal2 = subTotal2.replace("$", "");
                                    //products.add("" + itemnameCol + "\t " + dialogVar + "\t \t \t" + itempriceCol + "");
                                    lbl_sub.setText("" + vattable2 + "");
                                    lbl_tax.setText("" + vat2 + "");
                                    lbl_total.setText("" + subTotal2 + "");
                                    ArrayList<String> temp = new ArrayList<>();
                                    temp.add(itemnameCol);//example I don't know the order you need
                                    temp.add(itempriceCol + "");//example I don't know the order you need
                                    temp.add(dialogVar + "");//example I don't know the order you need
                                    temp.add(itempricetotalCol2);//example I don't know the order you ne
                                    t2Rows.add(temp);

                                    totalPrice = subTotal;
                                    due = totalPrice;
                                    due2 = NumberFormat.getCurrencyInstance().format((subTotal / 1));
                                    due2 = due2.replace("$", "");
                                    lbl_due.setText("" + due2 + "");

                                    //MARK'S SOLUTION FOR TEMPORARY INVOICING ITEMS
                                    //INSERT TEMP INVOICE ITEMS INTO TABLE
                                    try {
                                        String convertedCode = Long.toString(code).trim();
                                        int result = db_data.searchDuplicateInvoice(convertedCode);
                                        if(result > 0){
                                            SQLiteDatabase db = db_data.getReadableDatabase();
                                            String SELECT_QUERY = "SELECT * FROM " + TABLE_TEMP_INVOICING + " WHERE " + COLUMN_TEMP_ID + "='"+ convertedCode +"'";
                                            Cursor cursor = db.rawQuery(SELECT_QUERY,null);
                                            cursor.moveToFirst();
                                            String retrievedQuantity = cursor.getString(3);
                                            cursor.close();
                                            int convertedQuantity = Integer.parseInt(retrievedQuantity);

                                            db_data.updateInvoiceItem(convertedCode,convertedQuantity + dialogVar);
                                            Toast.makeText(Cashier.this, "Quantity Updated!", Toast.LENGTH_SHORT).show();
                                        }
                                        else if(result == 0){
                                            InvoiceItem invoiceItem = new InvoiceItem();
                                            invoiceItem.setInvoiceProductDescription(itemnameCol);
                                            invoiceItem.setInvoiceProductPrice(itempriceCol);
                                            invoiceItem.setInvoiceProductQuantity(dialogVar);
                                            invoiceItem.setInvoiceProductID(convertedCode);

                                            db_data.insertTempInvoice(invoiceItem);
                                        }
                                    } catch (SQLiteException e){
                                        e.printStackTrace();
                                    }
                                    refreshRecyclerView();
                                }
                        }catch (Exception ex){
                            ex.printStackTrace();
                            lbl_due.setText(subTotal2);
                        }
                    }
                });
                Dialog dialog = builder.create();
                dialog.show();
            } else {
                Toast.makeText(this, "Product cant be found", Toast.LENGTH_LONG).show();
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    //BUTTON PRINT
    public void print(View view) throws ParseException {
//        bill.main();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy");
        dateformatted = dateformat.format(c.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        currentTime = sdf.format(new Date());
        products.add("ABZTRACK DEMO STORE\n");
        products.add("VAT REG TIN:000-111-111-001\n");
        products.add("MIN:12345678901234567\n");
        products.add("670 SGT BUMATAY STREET\n");
        products.add("PLAINVIEW, MANDALUYONG\n");
        products.add("SERIAL NO. ASDFG1234567890\n");
        products.add("PTU No. FP121234-123-1234567-12345\n");
        products.add("===============================\n");
        products.add("CASH INVOICE\n");
        products.add("Date: \t\t\t\t "+dateformatted+" \t "+currentTime+"\n");
        products.add("-------------------------------\n");
        products.add("Name \t\t"+"Quantity \t\t"+"Price\n");
        transType = "invoice";
//        Calendar c = Calendar.getInstance();
        Date currDate = new Date();
        final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MMM-dd-yyyy hh:mm a");
        String dateToStr = dateTimeFormat.format(currDate);
        Date strToDate = dateTimeFormat.parse(dateToStr);
//        currentDateTime = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
//        currentTime = sdf.format(new Date());
        String customerCash = txt_cash.getText().toString().replaceAll("[P,]", "");
        String rDisc = discType + "";
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
            db_data.addInvoice(transNumber+"",discount.toString(),totalPrice.toString().replace(",",""),inPrint,userNum,"0","0",vattable2.replaceAll("[$P,]",""),Double.parseDouble(vat2),discType+"", 0.0,"","","");
            String[] SELECT_QUERY = new String[]{_ID};
            Cursor cursor = dbReader.query(TABLE_INVOICE, SELECT_QUERY, null, null, null, null, null);
            cursor.moveToLast();
            String abc=cursor.getString(0);
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
                int quanBaKamo = db_data.getQuantityofProducts(itemCode12345[a]) - Integer.parseInt(itemQuan12345[a]);
                db_data.updateProductQuantity(itemCode12345[a],quanBaKamo);
            }
            products.add("-------------------------------\n");
            products.add("Invoice Number " + abc + "\n");
            products.add(quantityCount + " item(s)" + "Subtotal\t" + subTotal + "\n");
            products.add("Vatable" + "" + "\t\t" + vattable2+"\n");
            products.add("Vat" + "" + "\t\t" + vat2+"\n");
            products.add("Total" + "\t\t" + subTotal + "\n");

            //customerCash = txt_cash.getText().toString().replace(",", "");
//            double change = dCustomerCash - totalPrice;

            products.add("\t\t\t\tCash" + "\t\t\t\t" + txt_cash.getText().toString() + "\n");
            products.add("\t\t\t\tChange" + "\t\t\t" + formatted + "\n");
            products.add("\n\n\n\n\n\n");

            //CHECK IF PRINTERS ARE OPEN
            boolean ret = marksPrinter.Open();

            String[] printBaKamo = products.toArray(new String[products.size()]);
            String printBaHanapMo="";
            for (int p=0; p<products.size();p++){
                printBaHanapMo = printBaHanapMo +"\n"+ printBaKamo[p];
            }
            db_data.updateInvoice(abc,printBaHanapMo);


            double doubleCustomerCash = Double.parseDouble(customerCash);

            if( invoiceItemList != null && doubleCustomerCash >= due ) {
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
    }

    private void printFunction(ArrayList<String> list) {
        StringBuilder sb = new StringBuilder();
        for(String s : list){
            sb.append(s);
//            sb.append("\t");
            sb.append("\n");
//            sb.insert(sb.length(),"\t");
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
//                Calendar c = Calendar.getInstance();
//                SimpleDateFormat dateformat = new SimpleDateFormat("MM.dd.yyyy");
//                dateformatted = dateformat.format(c.getTime());
//                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
//                currentTime = sdf.format(new Date());

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

    public void cancelna(){
        txt_cash.setText(""+"0.00"+"");
        txt_search.setText("");
//        items.clear();
//        items.add("Quantity");
//        items.add("Name");
//        items.add("Price");
//        items.add("Total Price");
        txt_search.requestFocus();
        lbl_due.setText(""+"0.00"+"");
        lbl_total.setText(""+"0.00"+"");
        lbl_sub.setText(""+"0.00"+"");
        lbl_tax.setText(""+"0.00"+"");
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


    }
    public void cashierLogOut(View view){
        cancelna();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy hh:mm a",Locale.SIMPLIFIED_CHINESE);
        dateformatted = dateformat.format(c.getTime());
        dbWriter.execSQL("INSERT INTO sessions(time,date,username) VALUES(time('now'),date('now'),'"+ userNum +"') ");
//        String log = dateformatted+" "+currentTime+". "+forLog[2]+" "+forLog[1]+" with staff number "+forLog[0]+" logged out.";
//        db_data.addLog(log);
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






    //    public void listGo(){
//        orderArrayList.clear();
//        String min = "min("+NUMBER_ORDER+")";
//        String[] ALL = {NAME_ORDER, min, DATE_ORDER};
//        String ASC = "ASC";
//        SQLiteDatabase db = db_data.getReadableDatabase();
////        String table = "goal";
////        String[] columns = new String[] { "distinct id", "date", "sum(goal)" };
////        String selection = "id=? and date=?";
////        String[] arguments = new String[] { "0", "Sep 15, 2015" };
////        String groupBy = "id, date";
////        String having = null;
////        String orderBy = null;
////        db.query(table, column    s, selection, arguments, groupBy, having, orderBy);
//        Cursor curse = db.query(TABLE_NAME_ORDER, ALL, null, null, NUMBER_ORDER, null, ASC);
//        while (curse.moveToNext()) {
//            String oname = curse.getString(0);
//            int onumber = curse.getInt(1);
//            long pdate = curse.getLong(2);
//            orderArrayList.add(new Order(oname, onumber, pdate));
//        }
//        adapterOrder.notifyDataSetChanged();
//        curse.close();
//    }
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

    private void writeDataToSerial() {

        Log.d(TAG, "Enter writeDataToSerial");

        if(null==mSerial)
            return;

        if(!mSerial.isConnected())
            return;

        String strWrite;
        strWrite = String.format("%1$-40" + "s", "Loves ni Mark si     Keidel <3.");
        if (SHOW_DEBUG) {
            Log.d(TAG, "PL2303Driver Write 2(" + strWrite.length() + ") : " + strWrite);
        }
        int res = mSerial.write(strWrite.getBytes(), strWrite.length());
        if( res<0 ) {
            Log.d(TAG, "setup2: fail to controlTransfer: "+ res);
            return;
        }

        Toast.makeText(this, "Write length: "+strWrite.length()+" bytes", Toast.LENGTH_SHORT).show();

        Log.d(TAG, "Leave writeDataToSerial");
    }//writeDataToSerial
}