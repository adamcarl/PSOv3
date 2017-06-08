package com.example.sydney.psov3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by PROGRAMMER2 on 6/3/2017.
 */

public class ManageProfile  extends AppCompatActivity{
    //MENU//MENU//MENU//MENU//MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_priveleges_main_menu,menu);

        MenuItem importCSV = menu.findItem(R.id.menu_import_product);
        MenuItem addProduct = menu.findItem(R.id.menu_add_product);
        importCSV.setVisible(false);
        addProduct.setVisible(false);

        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.menu_logout:
                Intent intent = new Intent(ManageProfile.this, MainActivity.class);
                startActivity(intent);
                //Todo Function Here for saving Transaction
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}