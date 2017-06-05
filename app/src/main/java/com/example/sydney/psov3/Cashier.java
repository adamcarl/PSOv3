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
import android.support.annotation.IdRes;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sydney.psov3.adapter.AdapterOrder;
import com.hdx.lib.serial.SerialParam;
import com.hdx.lib.serial.SerialPortOperaion;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hdx.HdxUtil;
import static com.example.sydney.psov3.Constants.*;

public class Cashier extends AppCompatActivity {
    int temp = 0,temp2 = 1,quantityCount = 0,itemcodeCol,discType=0,code,dialogVar;
    double vattable,vat,subTotal,itempriceCol,itempricetotalCol,discount=0.0,discounted,totalPrice;
    Double due;
    Cursor cursor;
    DB_Data db_data;
    ContentValues cv;
    ArrayList<String> items;
    EditText txt_search,txt_cash;
    String itemnameCol,formatted,vat2,vattable2,subTotal2,due2,totalPrice2,itempricetotalCol2,discount2,discounted2,firstname;
    ArrayList<Integer>itemQuantityList = new ArrayList<Integer>();
    ArrayList<Double>itemPriceList = new ArrayList<Double>();
    ArrayList<String>itemNameList = new ArrayList<String>();
    List<String>row2t;
    ArrayList<Integer>itemCodeList  = new ArrayList<Integer>();
//    SerialPrinter mSerialPrinter = SerialPrinter.GetSerialPrinter();
    RelativeLayout layout;
    ArrayList<String> products = new ArrayList<String>();
    SQLiteDatabase dbReader;
    SQLiteDatabase dbWriter;
    TabHost tab_host;
    TextView lbl_sub,lbl_tax,lbl_total,lbl_due,lbl_dc,lbl_discount;
    Button btn_print;
    RadioButton rb_ndisc,rb_spdisc,rb_ddisc;
    RadioGroup rg_discount;
    Bill bill;
    List<List<String>> t2Rows = new ArrayList<List<String>>();
    ListView lv_order;
    ArrayList<Order> orderArrayList;
    AdapterOrder adapterOrder=null;
    protected void onCreate(Bundle savedInstanceState) {
        db_data = new DB_Data(this);
        dbReader = db_data.getReadableDatabase();
        dbWriter = db_data.getWritableDatabase();
        bill = new Bill();
        //bill.main();
        cv = new ContentValues();
        try {
//            mSerialPrinter.OpenPrinter(new SerialParam(9600, "/dev/ttyS3", 0), new SerialDataHandler());
//            HdxUtil.SetPrinterPower(1);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier);
        rb_ndisc = (RadioButton)findViewById(R.id.rb_ndisc);
        rb_spdisc = (RadioButton)findViewById(R.id.rb_rpdisc);
        rb_ddisc = (RadioButton)findViewById(R.id.rb_ddisc);
        txt_search =(EditText) findViewById(R.id.txt_cashier_search);
        txt_cash = (EditText)findViewById(R.id.txt_cash);
        layout = (RelativeLayout)findViewById(R.id.rl_cashier);
        tab_host = (TabHost)findViewById(R.id.tabHost);
        tab_host.setup();
        lbl_sub=(TextView)findViewById(R.id.lbl_subtotal);
        lbl_tax=(TextView)findViewById(R.id.lbl_tax);
        lbl_total=(TextView)findViewById(R.id.lbl_total);
        lbl_due=(TextView)findViewById(R.id.lbl_due);
        lbl_dc=(TextView)findViewById(R.id.lbl_dc);
        rg_discount = (RadioGroup)findViewById(R.id.rg_discount);
        lbl_discount = (TextView)findViewById(R.id.lbl_discount);
        orderArrayList = new ArrayList<>();
        adapterOrder = new AdapterOrder(this, R.layout.single_order, orderArrayList);
//        lv_order.setAdapter(adapterOrder);
        Toolbar mtoolBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mtoolBar);
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
        products.add("Name"+"\t"+"Quantity"+"\t"+"Price");
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
                    formatted = NumberFormat.getCurrencyInstance().format((parsed / 100));
                    formatted = formatted.replace('$','P');
                    current = formatted;
                    txt_cash.setText(formatted);
                    txt_cash.setSelection(formatted.length());

                    txt_cash.addTextChangedListener(this);
                    String deym = txt_cash.getText().toString().replace(",", "");
                    double d = Double.parseDouble(deym.replace("P",""));
                    due = totalPrice - d;
                    formatted = NumberFormat.getCurrencyInstance().format((due/1));
                    formatted = formatted.replace("$","");
                    try{
                    if (due>0 || due==0) {
                        lbl_dc.setText("Due");
                        lbl_due.setText(formatted);
                        btn_print.setVisibility(View.GONE);
                    }
                    else {
                            lbl_dc.setText("Change");
                            formatted = NumberFormat.getCurrencyInstance().format((due / 1));
                            formatted = formatted.replaceAll("[$-]", "P");
                            lbl_due.setText(formatted);
                            btn_print.setText("Print Receipt");
                            btn_print.setVisibility(View.VISIBLE);
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
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
        rg_discount.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(rb_spdisc.isChecked()){
                    discType = 1;
                    discount = subTotal * 0.20;
                    vat = 0.0;
                    vattable = subTotal / 1.12;
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
                }
                else if(rb_ddisc.isChecked()){
                    discType=2;
                    vattable = subTotal / 1.12;
                    vat = vattable * 0.0;

                    vat2 = NumberFormat.getCurrencyInstance().format((vat/1));
                    vat2 = vat2.replace("$","");
                    lbl_tax.setText(""+vat2+"");

                    vattable2 = NumberFormat.getCurrencyInstance().format((vattable/1));
                    vattable2 = vattable2.replace("$","");
                    lbl_total.setText(""+vattable2+"");
                    lbl_discount.setText("0.00");
                    totalPrice = vattable;
                    totalPrice2 = NumberFormat.getCurrencyInstance().format((totalPrice/1));
                    totalPrice2 = totalPrice2.replace("$","");
                    lbl_due.setText(totalPrice2);
                }
                else{
                    vattable = subTotal / 1.12;
                    vat = vattable * 0.12;
                    vat2 = NumberFormat.getCurrencyInstance().format((vat/1));
                    vat2 = vat2.replace("$","");
                    lbl_tax.setText(""+vat2+"");
                    discount=0.0;
                    lbl_discount.setText("0.00");
                    subTotal2 = NumberFormat.getCurrencyInstance().format((subTotal/1));
                    subTotal2 = subTotal2.replace("$","");
                    totalPrice = subTotal;
                    totalPrice2 = NumberFormat.getCurrencyInstance().format((totalPrice/1));
                    totalPrice2 = totalPrice2.replace("$","");
                    lbl_due.setText(totalPrice2);
                    lbl_total.setText(""+totalPrice2+"");
                }
            }
        });
        //tab_host.setOnTabChangedListener(new AnimatedTabHostListener(this,tab_host));
    }
    public void priceClick(View view){
        txt_cash.requestFocus();
    }
    public void search(View view) {
        try {
            code = Integer.parseInt(txt_search.getText().toString());
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
                                    itempriceCol = cursor.getDouble(cursor.getColumnIndex(PRICE_PRODUCT));
                                    itemnameCol = cursor.getString(cursor.getColumnIndex(NAME_PRODUCT));
                                    itemcodeCol = cursor.getInt(cursor.getColumnIndex(ID_PRODUCT));
                                    itemQuantityList.add(dialogVar);
                                    itemPriceList.add(itempriceCol);
                                    itemNameList.add(itemnameCol);
                                    itemCodeList.add(itemcodeCol);
                                    itempricetotalCol = dialogVar * itempriceCol;
                                    itempricetotalCol2 = NumberFormat.getCurrencyInstance().format((itempricetotalCol/1));
                                    itempricetotalCol2 = itempricetotalCol2.replace("$","");
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
                                        subTotal = subTotal += total.get(x);
                                        quantityCount++;
                                    }
                                    vattable = subTotal / 1.12;
                                    vat = vattable * 0.12;
                                    vattable2 = NumberFormat.getCurrencyInstance().format((vattable/1));
                                    vat2 = NumberFormat.getCurrencyInstance().format((vat/1));
                                    subTotal2 = NumberFormat.getCurrencyInstance().format((subTotal/1));
                                    vat2 = vat2.replace("$","");
                                    vattable2 = vattable2.replace("$","");
                                    subTotal2 = subTotal2.replace("$","");
                                    products.add("" + itemnameCol + "\t " + dialogVar + "\t \t \t" + itempriceCol + "");
                                    lbl_sub.setText("" + vattable2 + "");
                                    lbl_tax.setText("" + vat2 + "");
                                    lbl_total.setText("" + subTotal2 + "");
                                    ArrayList<String> temp = new ArrayList<String>();
                                    temp.add(itemnameCol);//example I don't know the order you need
                                    temp.add(itempriceCol+"");//example I don't know the order you need
                                    temp.add(dialogVar+"");//example I don't know the order you need
                                    temp.add(itempricetotalCol2);//example I don't know the order you ne
                                    t2Rows.add(temp);
                                    // TODO: 5/29/2017 addItem
                                    totalPrice = subTotal;
                                    due = totalPrice;
                                    due2 = NumberFormat.getCurrencyInstance().format((subTotal/1));
                                    due2 = due2.replace("$","");
                                    lbl_due.setText("" + due2 + "");
                                }
                        }catch (Exception ex){
                            ex.printStackTrace();
                            lbl_due.setText(subTotal2);
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
        bill.main();

//        String deym = txt_cash.getText().toString().replaceAll("[P,]", "");
//        String rDisc = discType+"";
//        try {
//                        db_data.addReceipt(rDisc,deym,null,null);
//                        products.add("--------------------------------------");
//                        products.add("Invoice Number " + temp2 + "");
//                        products.add(quantityCount + " item(s)" + "Subtotal\t" + subTotal + "");
//                        products.add("Vatable" + "" + "\t\t" + vattable2);
//                        products.add("Vat" + "" + "\t\t " + vat2);
//                        products.add("Total" + " \t\t" + subTotal + "");
//                        products.add("Cash\t\t" + txt_cash.getText().toString() + "");
//                        if (due>0 || due==0) {
//                            products.add("Due" + " \t\t" + due + "");
//                        }
//                        else {
//                            String change = due.toString().replace("-", "");
//                            products.add("Change" + " \t\t" + change + "");
//                        }
//                        mSerialPrinter.sydneyDotMatrix7by7();
//                        mSerialPrinter.printString(products);
//                        mSerialPrinter.walkPaper(50);
//                        mSerialPrinter.sendLineFeed();
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    } finally {
//                        itemQuantityList.clear();
//                        itemPriceList.clear();
//                        itemNameList.clear();
//                        itemCodeList.clear();
//                    }
//                    cancelna();
////                }
//            });
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
                cancelna();
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
        txt_cash.setText("P0.00");
        txt_search.setText("");
        items.clear();
        items.add("Quantity");
        items.add("Name");
        items.add("Price");
        items.add("Total Price");
        txt_search.requestFocus();
        lbl_due.setText("0.00");
        lbl_total.setText("0.00");
        lbl_sub.setText("0.00");
        lbl_tax.setText("0.00");
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
        dbWriter.execSQL("INSERT INTO sessions(time,date,username) VALUES(time('now'),date('now'),'"+firstname+"') ");
        finish();
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
}