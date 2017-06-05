package com.example.sydney.psov3;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity implements OnItemSelectedListener {

    TextView txt_admin_hello;
    LinearLayout ll_admin_welcome,ll_admin_staff;
    Button btn_adminManStaff, btn_adminManProd, btn_staff_update_cancel, btn_admin_staff_update;
    Spinner spn_admin_staff_id,spn_admin_staff_pos;
    DB_Data db_data;
    EditText txt_admin_staff_fname, txt_admin_staff_lname,txt_admin_staff_pass;
    ListView lv_admin_prod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        db_data = new DB_Data(this);

        btn_adminManStaff = (Button)findViewById(R.id.btn_adminManStaff);
        btn_staff_update_cancel = (Button)findViewById(R.id.btn_staff_update_cancel);
        btn_admin_staff_update = (Button)findViewById(R.id.btn_admin_staff_update);

        ll_admin_staff=(LinearLayout)findViewById(R.id.ll_admin_staff);
        ll_admin_welcome=(LinearLayout)findViewById(R.id.ll_admin_welcome);
        ll_admin_welcome.setVisibility(View.VISIBLE);
        txt_admin_hello = (TextView)findViewById(R.id.txt_admin_hello);
        txt_admin_hello.setText("Good Morning!");
        txt_admin_staff_fname=(EditText)findViewById(R.id.txt_admin_staff_fname);
        txt_admin_staff_lname=(EditText)findViewById(R.id.txt_admin_staff_lname);
        txt_admin_staff_pass=(EditText)findViewById(R.id.txt_admin_staff_pass);
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
        btn_admin_staff_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db_data.updateStaff(txt_admin_staff_fname.getText().toString(),txt_admin_staff_lname.getText().toString(),spn_admin_staff_id.getSelectedItem().toString(),txt_admin_staff_pass.getText().toString(),spn_admin_staff_pos.getSelectedItem().toString());
            }
        });
    }
    class singleRow{
        String prod_id;
        String prod_name;
        String prod_desc;
        String prod_price;
        String prod_quan;

    }
    class viewAdapter extends BaseAdapter{

        ArrayList<singleRow> list;
        viewAdapter(Context c){
            list=new ArrayList<singleRow>();

            Resources res=c.getResources();
        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.search_menu, menu);
//
//         // Associate searchable configuration with the SearchView
//        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) menu.findItem(R.id.m_search).getActionView();
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setIconifiedByDefault(false);
//        return true;
//    }

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
}