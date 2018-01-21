package com.jdkgroup.customview.appcompatedittext;

import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;

public class AppEditTextChangedListener implements TextWatcher {

    /*
    TODO CALLING
    appEditFilter.addTextChangedListener(new AppEditTextChangedListener(this, appEditFilter));

    @Override
    public void onTextChanged(String str) {
        filterAdapter.getFilter().filter(str);
    }
    */

    private AppCompatEditText appCompatEditText;
    private OnEditTextChangedListener mListener;

    public AppEditTextChangedListener(OnEditTextChangedListener mListener, AppCompatEditText appCompatEditText) {
        this.appCompatEditText = appCompatEditText;
        this.mListener = mListener;
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    public void afterTextChanged(Editable s) {
        mListener.onTextChanged(s.toString());
    }

    public interface OnEditTextChangedListener {
        void onTextChanged(String str);
    }
}
