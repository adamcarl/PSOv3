package com.example.sydney.psov3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import com.hdx.lib.serial.SerialParam;
import com.hdx.lib.serial.SerialPortOperaion;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import hdx.HdxUtil;
import static com.example.sydney.psov3.Constants.*;

public class Cashier extends AppCompatActivity {
    int temp = 0,temp2 = 1,quantityCount = 0,itemcodeCol;
    double vattable,vat,vattable2,vat2,due2,totalPrice,totalPrice2,itempriceCol,itempricetotalCol;
    Double due;
    int dialogVar;
    Cursor cursor;
    DB_Data db_data;
    ContentValues cv;
    ArrayList<String> items;
    EditText searchText,txt_cash;
    String itemnameCol;
    ArrayList<Integer>itemQuantityList = new ArrayList<Integer>();
    ArrayList<Double>itemPriceList = new ArrayList<Double>();
    ArrayList<String>itemNameList = new ArrayList<String>();
    ArrayList<Integer>itemCodeList  = new ArrayList<Integer>();
    String firstname;
    SerialPrinter mSerialPrinter = SerialPrinter.GetSerialPrinter();
    RelativeLayout layout;
    ArrayList<String> products = new ArrayList<String>();
    SQLiteDatabase dbReader;
    SQLiteDatabase dbWriter;
    TabHost host;
    TextView lbl_sub,lbl_tax,lbl_total,lbl_due,lbl_dc;
    Button btn_print;
    protected void onCreate(Bundle savedInstanceState) {
        db_data = new DB_Data(this);
        dbReader = db_data.getReadableDatabase();
        dbWriter = db_data.getWritableDatabase();
        cv = new ContentValues();
        try {
            mSerialPrinter.OpenPrinter(new SerialParam(9600, "/dev/ttyS3", 0), new SerialDataHandler());
            HdxUtil.SetPrinterPower(1);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier);
        searchText =(EditText) findViewById(R.id.txt_cashier_search);
        txt_cash = (EditText)findViewById(R.id.txt_cash);
        layout = (RelativeLayout)findViewById(R.id.rl_cashier);
        host = (TabHost)findViewById(R.id.tabHost);
        host.setup();
        lbl_sub=(TextView)findViewById(R.id.lbl_subtotal);
        lbl_tax=(TextView)findViewById(R.id.lbl_tax);
        lbl_total=(TextView)findViewById(R.id.lbl_total);
        lbl_due=(TextView)findViewById(R.id.lbl_due);
        lbl_dc=(TextView)findViewById(R.id.lbl_dc);
        btn_print=(Button)findViewById(R.id.btn_print);
        Toolbar mtoolBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mtoolBar);
        //Tab 1

        TabHost.TabSpec spec = host.newTabSpec("Invoice");

        spec.setContent(R.id.tab1);

        spec.setIndicator("Invoice");

        host.addTab(spec);



        //Tab 2

        spec = host.newTabSpec("Payment");

        spec.setContent(R.id.tab2);

        spec.setIndicator("Payment");

        host.addTab(spec);



        //Tab 3

        spec = host.newTabSpec("Shift");

        spec.setContent(R.id.tab3);

        spec.setIndicator("Shift");

        host.addTab(spec);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("MM.dd.yyyy");
        String dateformatted = dateformat.format(c.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String currentTime = sdf.format(new Date());
        dbWriter.execSQL("INSERT INTO sessions(time,date,username) VALUES(time('now'),date('now'),'"+firstname+"') ");
        items = new ArrayList<String>();
        items.add("Quantity");
        items.add("Name");
        items.add("Price");
        items.add("Total Price");
        products.add("     ABZTRAK INC CONVENIENCE STORE");
        products.add("2nd Floor, #670,");
        products.add("Sgt. Bumatay St, Mandaluyong");
        products.add("NCR, Philippines");
        products.add("             Cash Invoice");
        products.add("Date: \t "+dateformatted+" \t "+currentTime+"");
        products.add("--------------------------------------");
        products.add("Quantity          Name           Price");
        GridView grid = (GridView) findViewById(R.id.grd_sell);
        grid.setAdapter(new ArrayAdapter<String>(this,R.layout.single_cell,items));



        txt_cash.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            private String current = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    txt_cash.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[P,.]", "");

                    double parsed = Double.parseDouble(cleanString);
                    String formatted = NumberFormat.getCurrencyInstance().format((parsed / 100));
                    formatted = formatted.replace('$','P');
                    current = formatted;
                    txt_cash.setText(formatted);
                    txt_cash.setSelection(formatted.length());

                    txt_cash.addTextChangedListener(this);

                    String deym = txt_cash.getText().toString().replace(",", "");
                    double d = Double.parseDouble(deym.replace("P",""));
                    due = totalPrice - d;
                    if (due>0 || due==0) {
                        lbl_dc.setText("Due");
                        lbl_due.setText("" + due + "");
                        btn_print.setText("Print Tender");
                    }
                    else {
                        lbl_dc.setText("Change");
                        String change = due.toString().replace("-","");
                        lbl_due.setText(change);
                        btn_print.setText("Print Receipt");
                    }
                }
            }
        });
        //host.setOnTabChangedListener(new AnimatedTabHostListener(this,host));
    }
    public void priceClick(View view){
        txt_cash.requestFocus();
    }
    public void search(View view) {
        try {
            int code = Integer.parseInt(searchText.getText().toString());
            final String[] itemcode = {Integer.toString(code)};
            String[] WHERE = {ID_PRODUCT, NAME_PRODUCT, QUAN_PRODUCT, PRICE_PRODUCT};
            cursor = dbReader.query(TABLE_NAME_PRODUCT, WHERE, ID_PRODUCT + " LIKE ?", itemcode, null, null, null);
            cursor.moveToFirst();
            int rows = cursor.getCount();
            if (rows >= 1) {
                // 1. Instantiate an AlertDialog.Builder with its constructor
                final EditText dialogText = new EditText(this);
                dialogText.setInputType(InputType.TYPE_CLASS_NUMBER);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Enter Quantity");
                final DecimalFormat format = new DecimalFormat("#.##");
                builder.setView(dialogText);
                searchText.setText("");
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        try {
                                if (dialogText.getText().toString().equals("")){
                                    Toast.makeText(getApplicationContext(), "Please Enter Quantity.", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    dialogVar = Integer.parseInt(dialogText.getText().toString());
                                    itempriceCol = cursor.getDouble(cursor.getColumnIndex(PRICE_PRODUCT));
                                    itemnameCol = cursor.getString(cursor.getColumnIndex(NAME_PRODUCT));
                                    itemcodeCol = cursor.getInt(cursor.getColumnIndex(ID_PRODUCT));
                                    itemQuantityList.add(dialogVar);
                                    itemPriceList.add(itempriceCol);
                                    itemNameList.add(itemnameCol);
                                    itemCodeList.add(itemcodeCol);
                                    itempricetotalCol = dialogVar * itempriceCol;
                                    itempricetotalCol = Double.valueOf(format.format(itempricetotalCol));
                                    ArrayList<Double> total = new ArrayList<Double>();
                                    total.add(itempricetotalCol);
                                    items.add("" + dialogVar + "");
                                    items.add("" + itemnameCol + "");
                                    items.add("" + itempriceCol + "");
                                    items.add("" + itempricetotalCol + "");
                                    cursor.close();
                                    layout.invalidate();
                                    layout.requestLayout();
                                    for (int x = 0; x < total.size(); x++) {
                                        totalPrice = totalPrice += total.get(x);
                                        quantityCount++;
                                    }
                                    vattable = totalPrice / 1.12;
                                    vat = vattable * 0.12;
                                    vattable2 = Double.valueOf(format.format(vattable));
                                    vat2 = Double.valueOf(format.format(vat));
                                    totalPrice2 = Double.valueOf(format.format(totalPrice));
                                    products.add("" + itemnameCol + "\t " + dialogVar + "\t \t \t" + itempriceCol + "");
                                    lbl_sub.setText("" + vattable2 + "");
                                    lbl_tax.setText("" + vat2 + "");
                                    lbl_total.setText("" + totalPrice2 + "");
                                    String deym = txt_cash.getText().toString();
                                    double d = Double.parseDouble(deym.replaceAll("[P,.]",""));
                                    due = totalPrice - d;
                                    due2 = Double.valueOf(format.format(due));
                                    lbl_due.setText("" + due2 + "");
                                }
                        }catch (Exception ex){
                            ex.printStackTrace();
                            lbl_due.setText(""+totalPrice+"");
                        }
                    }
                });
                Dialog dialog = builder.create();
                dialog.show();
            } else Toast.makeText(this, "Product cant be found", Toast.LENGTH_LONG).show();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    public void print(View view) {
//            final EditText dialogText = new EditText(Cashier.this);
//            dialogText.setInputType(InputType.TYPE_CLASS_NUMBER);
//            AlertDialog.Builder builder = new AlertDialog.Builder(Cashier.this);
//            builder.setTitle("Enter Amount");
//            builder.setView(dialogText);
//            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int whichButton) {
//                    dialogVar = Integer.parseInt(dialogText.getText().toString());
//                    double a1 = dialogVar - itempriceCol;
//                    layout.invalidate();
//                    layout.requestLayout();
//                    DecimalFormat format = new DecimalFormat("#.##");
//                    cash = a1;
                    cursor = dbReader.rawQuery("SELECT MAX(transactionnumber) AS maxix from receipts", null);
                    cursor.moveToFirst();
                    temp = cursor.getInt(cursor.getColumnIndex("maxix"));
                    if (temp < 1) {
                        for (int x = 0; x < itemPriceList.size(); x++) {
                            cv.put("itemname", "" + itemNameList.get(x) + "");
                            cv.put("itemprice", "" + itemPriceList.get(x) + "");
                            cv.put("itemcode", "" + itemCodeList.get(x) + "");
                            cv.put("orderquantity", "" + itemQuantityList.get(x) + "");
                            temp2 = 1;
                            dbWriter.insert("orders", null, cv);
                        }
                        dbWriter.execSQL("INSERT INTO receipts(transactionnumber) values(1)");
                        itemQuantityList.clear();
                        itemPriceList.clear();
                        itemNameList.clear();
                        itemCodeList.clear();
                        cursor.close();
                        Toast.makeText(Cashier.this, "aw", Toast.LENGTH_LONG).show();
                    } else {
                        dbWriter.execSQL("UPDATE receipts set transactionnumber = transactionnumber + 1");
                        cursor = dbReader.rawQuery("SELECT MAX(transactionnumber) AS maxix from receipts", null);
                        cursor.moveToFirst();
                        temp2 = cursor.getInt(cursor.getColumnIndex("maxix"));
                        for (int x = 0; x < itemPriceList.size(); x++) {
                            cv.put("itemname", "" + itemNameList.get(x) + "");
                            cv.put("itemprice", "" + itemPriceList.get(x) + "");
                            cv.put("itemcode", "" + itemCodeList.get(x) + "");
                            cv.put("orderquantity", "" + itemQuantityList.get(x) + "");
                            cv.put("transactionnumber", temp2);
                            dbWriter.insert("orders", null, cv);
                        }
                        dbWriter.execSQL("INSERT INTO receipts(transactionnumber) values(" + temp2 + ") ");
                        dbWriter.execSQL("INSERT INTO receipts(invoicenumber) values(" + temp2 + ")");
                        itemQuantityList.clear();
                        itemPriceList.clear();
                        itemNameList.clear();
                        itemCodeList.clear();
                    }
                    try {
                        products.add("--------------------------------------");
                        products.add("Invoice Number " + temp2 + "");
                        products.add(quantityCount + " item(s)" + "Subtotal\t" + totalPrice + "");
                        products.add("Vatable" + "" + "\t\t" + vattable2);
                        products.add("Vat" + "" + "\t\t " + vat2);
                        products.add("Total" + " \t\t" + totalPrice + "");
                        products.add("Cash\t\t" + txt_cash.getText().toString() + "");
                        if (due>0 || due==0) {
                            products.add("Due" + " \t\t" + due + "");
                        }
                        else {
                            String change = due.toString().replace("-", "");
                            products.add("Change" + " \t\t" + change + "");
                        }
                        mSerialPrinter.sydneyDotMatrix7by7();
                        mSerialPrinter.printString(products);
                        mSerialPrinter.walkPaper(50);
                        mSerialPrinter.sendLineFeed();
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
//            });

//    }

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
                cancelna();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void cancelna(){
        txt_cash.setText("P0.00");
        searchText.setText("");
        items.clear();
        items.add("Quantity");
        items.add("Name");
        items.add("Price");
        items.add("Total Price");
        searchText.requestFocus();
        lbl_due.setText("P0.00");
        lbl_total.setText("P0.00");
        lbl_sub.setText("P0.00");
        lbl_tax.setText("P0.00");
    }
    private void sleep(int ms) {
        try {
            java.lang.Thread.sleep(ms);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private class SerialDataHandler extends Handler {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SerialPortOperaion.SERIAL_RECEIVED_DATA_MSG:
                    SerialPortOperaion.SerialReadData data = (SerialPortOperaion.SerialReadData)msg.obj;
                    StringBuilder sb=new StringBuilder();
                    for(int x=0;x<data.size;x++)
                        sb.append(String.format("%02x", data.data[x]));
            }
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
}