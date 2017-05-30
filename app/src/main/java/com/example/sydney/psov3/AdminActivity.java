package com.example.sydney.psov3;

import android.app.Dialog;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sydney.psov3.adapter.AdapterProd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static com.example.sydney.psov3.Constants.*;

public class AdminActivity extends AppCompatActivity implements OnItemSelectedListener {

    LinearLayout ll_admin_welcome,ll_admin_staff,ll_admin_product,ll_admin_product_add,ll_admin_product_edit;
    Button btn_adminManStaff, btn_adminManProd, btn_staff_update_cancel, btn_admin_staff_update, btn_prod, btn_admin_prod_cancel,
            btn_admin_prod_add,btn_admin_prod_cancel_edit,btn_admin_prod_edit,btn_admin_prod_import;
    Spinner spn_admin_staff_id,spn_admin_staff_pos;
    DB_Data db_data;
    EditText txt_admin_staff_fname, txt_admin_staff_lname,txt_admin_staff_pass,txt_admin_prod_id,txt_admin_prod_name,txt_admin_prod_desc,
            txt_admin_prod_price,txt_admin_prod_quan,txt_admin_prod_id_edit,txt_admin_prod_name_edit,txt_admin_prod_desc_edit,
            txt_admin_prod_price_edit,txt_admin_prod_quan_edit;
    ListView lv_admin_prod;
    ArrayList<Product> productArrayList;
    AdapterProd adapterProd=null;
    android.widget.SearchView search_prod;
    public static final int requestcode = 1;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        db_data = new DB_Data(this);
        lv_admin_prod=(ListView)findViewById(R.id.lv_admin_prod);
        productArrayList = new ArrayList<>();
        adapterProd = new AdapterProd(this, R.layout.single_row, productArrayList);
        lv_admin_prod.setAdapter(adapterProd);
        search_prod=(android.widget.SearchView)findViewById(R.id.search_prod);
        btn_adminManStaff = (Button)findViewById(R.id.btn_adminManStaff);
        btn_staff_update_cancel = (Button)findViewById(R.id.btn_staff_update_cancel);
        btn_admin_staff_update = (Button)findViewById(R.id.btn_admin_staff_update);
        btn_adminManProd=(Button)findViewById(R.id.btn_adminManProd);
        btn_prod=(Button)findViewById(R.id.btn_prod);
        btn_admin_prod_cancel=(Button)findViewById(R.id.btn_admin_prod_cancel);
        btn_admin_prod_add=(Button)findViewById(R.id.btn_admin_prod_add) ;
        btn_admin_prod_edit=(Button)findViewById(R.id.btn_admin_prod_add_edit);
        btn_admin_prod_cancel_edit=(Button)findViewById(R.id.btn_admin_prod_cancel_edit);
        btn_admin_prod_import=(Button)findViewById(R.id.btn_admin_prod_import);

        ll_admin_staff=(LinearLayout)findViewById(R.id.ll_admin_staff);
        ll_admin_welcome=(LinearLayout)findViewById(R.id.ll_admin_welcome);
        ll_admin_product=(LinearLayout)findViewById(R.id.ll_admin_prod);;
        ll_admin_welcome.setVisibility(View.VISIBLE);
        ll_admin_product_add=(LinearLayout)findViewById(R.id.ll_admin_prod_add);
        ll_admin_product_edit=(LinearLayout)findViewById(R.id.ll_admin_prod_edit);

        txt_admin_staff_fname=(EditText)findViewById(R.id.txt_admin_staff_fname);
        txt_admin_staff_lname=(EditText)findViewById(R.id.txt_admin_staff_lname);
        txt_admin_staff_pass=(EditText)findViewById(R.id.txt_admin_staff_pass);
        txt_admin_prod_id=(EditText)findViewById(R.id.txt_admin_prod_id);
        txt_admin_prod_name=(EditText)findViewById(R.id.txt_admin_prod_name);
        txt_admin_prod_desc=(EditText)findViewById(R.id.txt_admin_prod_desc);
        txt_admin_prod_price=(EditText)findViewById(R.id.txt_admin_prod_price);
        txt_admin_prod_quan=(EditText)findViewById(R.id.txt_admin_prod_quan);
        txt_admin_prod_id_edit=(EditText)findViewById(R.id.txt_admin_prod_id_edit);
        txt_admin_prod_name_edit=(EditText)findViewById(R.id.txt_admin_prod_name_edit);
        txt_admin_prod_desc_edit=(EditText)findViewById(R.id.txt_admin_prod_desc_edit);
        txt_admin_prod_price_edit=(EditText)findViewById(R.id.txt_admin_prod_price_edit);
        txt_admin_prod_quan_edit=(EditText)findViewById(R.id.txt_admin_prod_quan_edit);

        spn_admin_staff_id=(Spinner)findViewById(R.id.spn_admin_staff_id);
        spn_admin_staff_id.setOnItemSelectedListener(this);
        spn_admin_staff_pos=(Spinner)findViewById(R.id.spn_admin_staff_pos);

        lv_admin_prod = (ListView)findViewById(R.id.lv_admin_prod);
        loadSpinnerData();
        btn_adminManStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_admin_welcome.setVisibility(View.GONE);
                ll_admin_staff.setVisibility(View.VISIBLE);
            }
        });
        btn_staff_update_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_admin_welcome.setVisibility(View.VISIBLE);
                ll_admin_staff.setVisibility(View.GONE);
            }
        });
        btn_adminManProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_admin_welcome.setVisibility(View.GONE);
                ll_admin_product.setVisibility(View.VISIBLE);
            }
        });
        btn_prod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_admin_welcome.setVisibility(View.GONE);
                ll_admin_product_add.setVisibility(View.VISIBLE);
                ll_admin_product.setVisibility(View.GONE);
            }
        });
        btn_admin_prod_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_admin_welcome.setVisibility(View.GONE);
                ll_admin_product_add.setVisibility(View.GONE);
                ll_admin_product.setVisibility(View.VISIBLE);
            }
        });
        btn_admin_prod_cancel_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_admin_welcome.setVisibility(View.GONE);
                ll_admin_product_edit.setVisibility(View.GONE);
                ll_admin_product.setVisibility(View.VISIBLE);
                listGo();
            }
        });
        btn_admin_prod_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db_data.updateProd(txt_admin_prod_id_edit.getText().toString(),txt_admin_prod_name_edit.getText().toString(),txt_admin_prod_desc_edit.getText().toString(),txt_admin_prod_price_edit.getText().toString(),txt_admin_prod_quan_edit.getText().toString());
                Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_LONG).show();
                txt_admin_prod_desc_edit.setText("");
                txt_admin_prod_quan_edit.setText("");
                txt_admin_prod_id_edit.setText("");
                txt_admin_prod_name_edit.setText("");
                txt_admin_prod_price_edit.setText("");
            }
        });
        btn_admin_staff_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db_data.updateStaff(txt_admin_staff_fname.getText().toString(),txt_admin_staff_lname.getText().toString(),spn_admin_staff_id.getSelectedItem().toString(),txt_admin_staff_pass.getText().toString(),spn_admin_staff_pos.getSelectedItem().toString());
            }
        });
        btn_admin_prod_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pid=txt_admin_prod_id.getText().toString().trim();
                String pname=txt_admin_prod_name.getText().toString().trim();
                String pdesc=txt_admin_prod_desc.getText().toString().trim();
                String pprice=txt_admin_prod_price.getText().toString().trim();
                String pquan=txt_admin_prod_quan.getText().toString().trim();
//                try {
                    db_data.addProduct(pid,pname,pdesc,pprice,pquan);
                    Toast.makeText(AdminActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                    txt_admin_prod_id.setText("");
                    txt_admin_prod_name.setText("");
                    txt_admin_prod_desc.setText("");
                    txt_admin_prod_price.setText("");
                    txt_admin_prod_quan.setText("");
//                }
//                catch (Exception e){
//                    e.printStackTrace();
//                }
            }
        });
        btn_admin_prod_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
                fileintent.setType("gagt/sdf");
                try {
                    startActivityForResult(fileintent, requestcode);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(AdminActivity.this, "Failed to import.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        lv_admin_prod.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                txt_admin_prod_id_edit.setText(productArrayList.get(position).getP_id());
                txt_admin_prod_name_edit.setText(productArrayList.get(position).getP_name());
                txt_admin_prod_desc_edit.setText(productArrayList.get(position).getP_desc());
                txt_admin_prod_price_edit.setText(""+productArrayList.get(position).getP_price()+"");
                txt_admin_prod_quan_edit.setText(""+productArrayList.get(position).getP_quan()+"");
                ll_admin_product.setVisibility(View.GONE);
                ll_admin_product_edit.setVisibility(View.VISIBLE);
            }
        });
        search_prod.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                listGo();
                return false;
            }
        });
        listGo();
    }
    public static class FirstFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_admin_welcome, container, false);
        }
    }
    public static class SecondFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_admin_staff, container, false);
        }
    }
    public static class ThirdFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_admin_product, container, false);
        }
    }
    public static class FourthFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_prod_add, container, false);
        }
    }
    public static class FifthFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_prod_edit, container, false);
        }
    }
     private void loadSpinnerData() {
        // Spinner Drop down elements
        List<String> lables = db_data.getAllLabels();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spn_admin_staff_id.setAdapter(dataAdapter);
    }
            @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String label = parent.getItemAtPosition(position).toString();
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "You selected: " + label, Toast.LENGTH_LONG).show();
        loadStaff();
    }
            @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
    private void loadStaff(){
        String cnum = spn_admin_staff_id.getSelectedItem().toString();
        String[] staff111;
        staff111=db_data.selectStaff(cnum);
//        String[] array = staff111.toArray(new String [staff111.size()]);
        //staff111=db_data.selectStaff(cnum);
        txt_admin_staff_fname.setText(staff111[0]);
        txt_admin_staff_lname.setText(staff111[1]);
        txt_admin_staff_pass.setText(staff111[2]);
        if(staff111[3].equals("Manager")){
            spn_admin_staff_pos.setSelection(0);
        }
        else if(staff111[3].equals("Supervisor")){
            spn_admin_staff_pos.setSelection(1);
        }
        else{
            spn_admin_staff_pos.setSelection(2);
        }
    }
    public void listGo(){
        productArrayList.clear();
        String arg = search_prod.getQuery().toString().trim().toLowerCase();
        String aarg = "%"+arg+"%";
        String[] ALL = {ID_PRODUCT, NAME_PRODUCT, DESC_PRODUCT, PRICE_PRODUCT, QUAN_PRODUCT};
        String WHERE = ID_PRODUCT + " LIKE ? OR " + NAME_PRODUCT + " LIKE ? OR " + DESC_PRODUCT + " LIKE ? OR " + PRICE_PRODUCT + " LIKE ? OR " + QUAN_PRODUCT + " LIKE ?";
        String[] WHERE_ARG = {aarg, aarg, aarg, aarg, aarg};
        SQLiteDatabase db = db_data.getReadableDatabase();
        Cursor curse = db.query(TABLE_NAME_PRODUCT, ALL, WHERE, WHERE_ARG, null, null, null);
        while (curse.moveToNext()) {
            String pid = curse.getInt(0) + "";
            String pname = curse.getString(1);
            String pdesc = curse.getString(2);
            double pdprice = curse.getDouble(3);
            int pdquan = curse.getInt(4);
            productArrayList.add(new Product(pid, pname, pdesc, pdprice, pdquan));
        }
        adapterProd.notifyDataSetChanged();
        curse.close();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;
        switch (requestCode) {
            case requestcode:
                String filepath = data.getData().getPath();
                SQLiteDatabase db = db_data.getWritableDatabase();
                String tableName = TABLE_NAME_PRODUCT;
                db.execSQL("delete from " + tableName);
                try {
                    if (resultCode == RESULT_OK) {
                        try {

                            FileReader file = new FileReader(filepath);

                            BufferedReader buffer = new BufferedReader(file);
                            ContentValues contentValues = new ContentValues();
                            String line = "";
                            db.beginTransaction();

                            while ((line = buffer.readLine()) != null) {

                                String[] str = line.split(",", 5);  // defining 3 columns with null or blank field //values acceptance
                                //Id, Company,Name,Price
                                String pId = str[0].toString();
                                String pName = str[1].toString();
                                String pDesc = str[2].toString();
                                String pPrice = str[3].toString();
                                String pQuan = str[4].toString();

                                contentValues.put(ID_PRODUCT, pId);
                                contentValues.put(NAME_PRODUCT, pName);
                                contentValues.put(DESC_PRODUCT, pDesc);
                                contentValues.put(PRICE_PRODUCT, pPrice);
                                contentValues.put(QUAN_PRODUCT, pQuan);
                                db.insert(tableName, null, contentValues);
                                Toast.makeText(this, "Successfully Updated Database", Toast.LENGTH_LONG).show();
                            }
                            db.setTransactionSuccessful();
                            db.endTransaction();
                        } catch (IOException e) {
                            if (db.inTransaction())
                                db.endTransaction();
                            Dialog d = new Dialog(this);
                            d.setTitle(e.getMessage().toString() + "first");
                            d.show();
                            // db.endTransaction();
                        }
                    } else {
                        if (db.inTransaction())
                            db.endTransaction();
                        Dialog d = new Dialog(this);
                        d.setTitle("Only CSV files allowed");
                        d.show();
                    }
                } catch (Exception ex) {
                    if (db.inTransaction())
                        db.endTransaction();

                    Dialog d = new Dialog(this);
                    d.setTitle(ex.getMessage().toString() + "second");
                    d.show();
                    // db.endTransaction();
                }
        }
        listGo();
    }
}