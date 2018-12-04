package com.codemaven.xadapp.helper;


import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.codemaven.xadapp.R;

// class to enter otp
public class GenericTextWatcher implements TextWatcher {

    private View view;
    EditText et2,et3,et4;

    public GenericTextWatcher(View view,EditText ed2,EditText ed3,EditText ed4) {
        this.view = view;
        this.et2  = ed2;
        this.et3 = ed3;
        this.et4 = ed4;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = s.toString();
        switch (view.getId()) {
            case R.id.editText1:
                if (text.length() == 1)
                    et2.requestFocus();
                break;
            case R.id.editText2:
                if (text.length() == 1)
                    et3.requestFocus();
                break;
            case R.id.editText3:
                if (text.length() == 1)
                    et4.requestFocus();
                break;
            case R.id.editText4:
                break;
        }

    }
}