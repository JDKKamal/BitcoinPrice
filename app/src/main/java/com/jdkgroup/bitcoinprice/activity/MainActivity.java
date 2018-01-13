package com.jdkgroup.bitcoinprice.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.jdkgroup.baseclass.SimpleMVPActivity;
import com.jdkgroup.bitcoinprice.R;
import com.jdkgroup.model.callapi.currentprice.MainCurrentPrice;
import com.jdkgroup.presenter.CurrentPricePresenter;
import com.jdkgroup.view.CurrentPriceView;

public class MainActivity extends SimpleMVPActivity<CurrentPricePresenter, CurrentPriceView> implements CurrentPriceView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_price);

        getPresenter().apiCurrentPrice();
    }

    @NonNull
    @Override
    public CurrentPricePresenter createPresenter() {
        return new CurrentPricePresenter();
    }

    @NonNull
    @Override
    public CurrentPriceView attachView() {
        return this;
    }

    @Override
    public void onFailure(String message) {

    }

    @Override
    public void callCurrentPrice(MainCurrentPrice response) {
      
    }
}
