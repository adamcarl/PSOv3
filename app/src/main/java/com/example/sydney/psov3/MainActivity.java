package com.example.sydney.psov3;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button btn_login;
    EditText txt_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_login= (Button)findViewById(R.id.btn_login);

        FragmentManager fm = getFragmentManager();
        addShowHideListener(R.id.btn_admin, fm.findFragmentById(R.id.fragment1));
        addShowHideListener(R.id.btn_back, fm.findFragmentById(R.id.frag_welcome));

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.animator.fade_in,
                android.R.animator.fade_out);
        if (fm.findFragmentById(R.id.fragment1).isHidden()) {
            ft.show(fm.findFragmentById(R.id.fragment1));
//                    ft.hide(fm.findFragmentById(R.id.frag_welcome));
//                    button.setText("Hide");
        } else {
            ft.hide(fm.findFragmentById(R.id.fragment1));
//                    button.setText("Show");
        }
        ft.commit();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt_pass.equals("password")){

                }else{

                }
            }
        });

    }

    void addShowHideListener(int buttonId, final Fragment fragment) {
        final Button button = (Button) findViewById(buttonId);
        final FragmentManager fm = getFragmentManager();
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.setCustomAnimations(android.R.animator.fade_in,
                        android.R.animator.fade_out);
                if (fragment.isHidden()) {
                    ft.show(fragment);
//                    ft.hide(fm.findFragmentById(R.id.frag_welcome));
//                    button.setText("Hide");
                } else {
                    ft.hide(fragment);
//                    button.setText("Show");
                }
                ft.commit();
            }
        });
    }

    public static class FirstFragment extends Fragment {
        TextView mTextView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_login, container, false);

            // Retrieve the text editor, and restore the last saved state if needed.
            if (savedInstanceState != null) {
                mTextView.setText(savedInstanceState.getCharSequence("text"));
            }
            return v;
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);

            // Remember the current text, to restore if we later restart.
            outState.putCharSequence("text", mTextView.getText());
        }
    }
    public static class SecondFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_welcome, container, false);

            // Retrieve the text editor and tell it to save and restore its state.
            // Note that you will often set this in the layout XML, but since
            // we are sharing our layout with the other fragment we will customize
            // it here.
            return v;
        }
    }

}