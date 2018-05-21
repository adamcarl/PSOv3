package com.example.sydney.psov3.POJO;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.example.sydney.psov3.CustomButtonForDrawableTop;

/**
 * Created by PROGRAMMER2 on 5/21/2018.
 * ABZTRAK INC.
 */

public class SetListener {
    public void setListener(EditText[] etArray,
                            ImageButton[] imgArray,
                            Button[] btnArray,
                            RadioButton[] rbArray,
                            CheckBox[] chkArray,
                            Spinner[] spnArray,
                            CustomButtonForDrawableTop[] customButtonArray,
                            View.OnKeyListener listener){
        for (EditText anEtArray : etArray) {
            anEtArray.setOnKeyListener(listener);
        }
        for (ImageButton anImgArray : imgArray) {
            anImgArray.setOnKeyListener(listener);
        }
        for (Button aBtnArray : btnArray) {
            aBtnArray.setOnKeyListener(listener);
        }
        for (RadioButton aRbArray : rbArray) {
            aRbArray.setOnKeyListener(listener);
        }
        for (CheckBox aChkArray : chkArray) {
            aChkArray.setOnKeyListener(listener);
        }
        for (CustomButtonForDrawableTop aCustomButtonArray : customButtonArray) {
            aCustomButtonArray.setOnKeyListener(listener);
        }
        for (Spinner aSpnArray : spnArray) {
            aSpnArray.setOnKeyListener(listener);
        }
    }
}
