package com.example.sydney.psov3.cashierBaKamo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sydney.psov3.AdminActivity;
import com.example.sydney.psov3.DB_Data;

import java.text.NumberFormat;
import java.util.ArrayList;

import static com.example.sydney.psov3.Constants.*;

/**
 * Created by sydney on 6/3/2017.
 */

public class Invoice {

    private Context ctx;
    private DB_Data db_data;
    private SQLiteDatabase dbReader,dbWriter;
    private Cursor cursor;
    private int dialogVar;
    private double itempriceCol;
    private int itemcodeCol;
    private String itemnameCol;

    public void searchProduct(int id){
        init();
        try {
            int rows = cursor.getCount();
            if (rows >= 1) {
                // 1. Instantiate an AlertDialog.Builder with its constructor
                final EditText dialogText = new EditText(ctx);
                dialogText.setInputType(InputType.TYPE_CLASS_NUMBER);
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle("Enter Quantity");
                builder.setView(dialogText);
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        try {
                            if (dialogText.getText().toString().equals("")){
                                Toast.makeText(ctx, "Please Enter Quantity.", Toast.LENGTH_SHORT).show();
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
                        }
                    }
                });
                Dialog dialog = builder.create();
                dialog.show();
            } else Toast.makeText(ctx, "Product cant be found", Toast.LENGTH_LONG).show();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void init() {
        db_data = new DB_Data(ctx);
        dbReader = db_data.getReadableDatabase();
        dbWriter = db_data.getWritableDatabase();
    }
}
