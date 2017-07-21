package com.example.sydney.psov3;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.sydney.psov3.BaseApp;
import com.example.sydney.psov3.R;
import com.example.sydney.psov3.utils.AidlUtil;

/**
 *
 * Created by Administrator on 2017/4/27.
 */

public abstract class BaseActivity extends AppCompatActivity{
    BaseApp baseApp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseApp = (BaseApp) getApplication();
    }

    /**
     *
     Set the title
     * @param title
     */
    void setMyTitle(String title){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
    }

    void setMyTitle(@StringRes int title){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        if(!baseApp.isAidl()){
            actionBar.setSubtitle("bluetooth®");
        }else{
            actionBar.setSubtitle("");
        }
    }


}
