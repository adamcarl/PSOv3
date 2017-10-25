package com.example.sydney.psov3;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.jolimark.JmPrinter;
import com.jolimark.UsbPrinter;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.example.sydney.psov3.Constants.COLUMN_TRANSACTION_DATETIME;
import static com.example.sydney.psov3.Constants.COLUMN_TRANSACTION_TYPE;
import static com.example.sydney.psov3.Constants.TABLE_TRANSACTION;

public class ManageReport extends AppCompatActivity implements TransactionAdapter.OnRecyclerItemClickListener {

    //FTP
    final String FTPHost = "files.000webhost.com";
    final String user = "attendancemonitor";
    final String pass = "darksalad12";
    final int PORT = 21;
    final int PICK_FILE = 1;
    String filename, filepath;
    AlertDialog.Builder builder = null;
    AlertDialog alertDialog = null;
    SQLiteDatabase dbReader;
    private TransactionAdapter transAdapter;
    private List<Transactions> transactionList;
    private DB_Data db_data;
    private Cursor cursor;
    private SearchView searchView;
    private Spinner spinner;
    private int spinnerSelected;
    private RecyclerView recyclerView;
    private String colWhere;
    private JmPrinter mPrinter;
    private UsbPrinter marksPrinter = new UsbPrinter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_report);

        db_data = new DB_Data(this);
        dbReader = db_data.getReadableDatabase();
        try {
            marksPrinter.Open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mPrinter = new JmPrinter();           //Create a 78M printer object


        spinner = (Spinner) findViewById(R.id.spinnerTransactionSearch);
        spinner.setSelection(0); //initially Product ID

        allOnClickLiteners();
        transactionList = fill_with_data();
//        cursor = db_data.getData();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        transAdapter = new TransactionAdapter(getApplication(), transactionList, this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(transAdapter);

    }


    private void allOnClickLiteners() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerSelected = spinner.getSelectedItemPosition();
                processSelectedSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
    }

    private void processSelectedSpinner() {
        if (spinnerSelected == 0) {
            colWhere = _ID;
            searchTransactions();
        } else if (spinnerSelected == 1) {
            colWhere = COLUMN_TRANSACTION_DATETIME;
            searchTransactions();
        }
        if (searchView.getQuery().toString().trim().toLowerCase().equals("")) {
            transactionList = fill_with_data();
            transAdapter = new TransactionAdapter(getApplication(), transactionList, this);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(transAdapter);

        }
    }

    private List<Transactions> searchTransactions() {
        transactionList.clear();
        String searchItem = searchView.getQuery().toString().trim().toLowerCase();
        if (searchItem.equals("")) {
            fill_with_data();
        } else if (!searchItem.equals("")) {
            db_data.getReadableDatabase();
            Cursor c = db_data.searchTransactions(searchItem, colWhere);

            while (c.moveToNext()) {
                int tid = c.getInt(0);
                String ttype = c.getString(1);
                String tdateTime = c.getString(2);

                transactionList.add(new Transactions(tid, ttype, tdateTime));
            }
            transAdapter.notifyDataSetChanged();
            c.close();
        }
        return transactionList;
    }

    private List<Transactions> fill_with_data() {
        transactionList = new ArrayList<>();
        transactionList.clear();

        SQLiteDatabase db = db_data.getReadableDatabase();
        String[] mWHERE_ARGS = {"copy", "cancel"};
        String QueryBaKamo = "SELECT * FROM " + TABLE_TRANSACTION + " WHERE " + COLUMN_TRANSACTION_TYPE + " = ? OR " + COLUMN_TRANSACTION_TYPE + " = ?";
        Cursor cursor = db.rawQuery(QueryBaKamo, mWHERE_ARGS);
        while (cursor.moveToNext()) {
            int transNum = cursor.getInt(0);
            String transType = cursor.getString(1);
            String DateTime = cursor.getString(2);

            transactionList.add(new Transactions(transNum, transType, DateTime));
        }
        cursor.close();

        return transactionList;
    }

    //MENU//MENU//MENU//MENU//MENU

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_priveleges_main_menu, menu);

        MenuItem importCSV = menu.findItem(R.id.menu_import_product);
        MenuItem addProduct = menu.findItem(R.id.menu_add_product);
        MenuItem showLog = menu.findItem(R.id.menu_show_log);
        importCSV.setVisible(false);
        addProduct.setVisible(false);
        showLog.setVisible(false);


        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);

            SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            searchView = (SearchView) menu.findItem(R.id.productSearch).getActionView();
            searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

            //SEARCH MENU
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    spinnerSelected = spinner.getSelectedItemPosition();

                    if (spinnerSelected == 0 || spinnerSelected == 1 && searchView.getQuery().toString().trim().toLowerCase().equals("")) {
                        transactionList = fill_with_data();
                        transAdapter = new TransactionAdapter(getApplication(), transactionList, ManageReport.this);
                        recyclerView.setLayoutManager(new GridLayoutManager(getApplication(), 1, GridLayoutManager.VERTICAL, false));
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(transAdapter);
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    spinnerSelected = spinner.getSelectedItemPosition();
                    processSelectedSpinner();
                    return false;
                }
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_logout:
                Intent intent = new Intent(ManageReport.this, MainActivity.class);
                startActivity(intent);
                //Todo Function Here for saving Transaction
                return true;

            case R.id.menu_send_to_server:
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("file/*");
                startActivityForResult(i, PICK_FILE);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FILE && data.getData() != null) {
            filepath = data.getData().getPath();
            filename = data.getData().getLastPathSegment();
            new UploadFile().execute(filepath, FTPHost, user, pass);

//            alertDialog.show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRecyclerItemClick(View childView, final int position) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_viewjournal, null);
        alertDialogBuilder.setView(alertLayout);

        AppCompatButton btnPrint = (AppCompatButton) alertLayout.findViewById(R.id.btnPrint);
        AppCompatButton btnExit = (AppCompatButton) alertLayout.findViewById(R.id.btnExit);
        AppCompatTextView txtJournalContent = (AppCompatTextView) alertLayout.findViewById(R.id.txtJournalContent);

        final AlertDialog alertDialogModify = alertDialogBuilder.create();
        alertDialogModify.show();
        txtJournalContent.setText("SHOULD SUPPLY BY TRANSACTION!!!");
        //ONLY QUANTITY CAN BE EDITED!
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    alertDialogModify.dismiss();
//                    String consecutive = String.format("%1$06d", position);
//                    String[] itemID = new String[]{_ID, COLUMN_TRANSACTION_TYPE};

//                    db_data.addTransaction("Copy", getCurrentDate(), "admin", 0, 0,"");
//                    Cursor cursor1 = dbReader.query(TABLE_TRANSACTION, itemID, null, null, null, null, null);
//                    cursor1.moveToLast();
//                    String newConsecutive = cursor1.getString(0); //COLUMN _ID of TABLE_TRANSACTION
//                    cursor1.close();

                    ArrayList<String> reprint = new ArrayList<>(); //Use to duplicate last print
//                    String paPrint = db_data.getPrintForTransactionNumber(position+1).replace(consecutive,newConsecutive);
                    String paPrint = db_data.getPrintForTransactionNumber(position + 1);
                    reprint.add("Duplicate");
                    reprint.add(paPrint);
                    printFunction(reprint);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogModify.dismiss();
            }
        });

    }

    private void printFunction(ArrayList<String> list) {
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
            db_data.deleteAllTempItemInvoice(); //DELETE ALL TEMP ITEMS

            if (!retnVale) {
                Toast.makeText(ManageReport.this, mPrinter.GetLastPrintErr(), Toast.LENGTH_SHORT).show();
            }
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }
    }

    String getCurrentDate() {
        Date strToDate = null;
        try {
            Date currDate = new Date();
            final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MMM-dd-yyyy hh:mm a");
            String dateToStr = dateTimeFormat.format(currDate);
            strToDate = dateTimeFormat.parse(dateToStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strToDate.toString();
    }

    private class UploadFile extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            FTPClient client = new FTPClient();
            try {
                client.connect(params[1], PORT);
                client.login(params[2], params[3]);
                client.enterLocalPassiveMode();
                client.changeWorkingDirectory("/public_html/CSVFiles");
                client.setFileType(FTP.BINARY_FILE_TYPE);
                return client.storeFile(filename, new FileInputStream(new File(params[0])));
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("FTP", e.toString());
                return false;
            } finally {
                try {
                    client.logout();
                    client.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean sucess) {
            if (sucess)
                Toast.makeText(ManageReport.this, "File Sent", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(ManageReport.this, "Error", Toast.LENGTH_LONG).show();
        }

    }
}