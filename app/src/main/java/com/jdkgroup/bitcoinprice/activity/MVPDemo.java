package com.jdkgroup.bitcoinprice.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.jdkgroup.baseclass.SimpleMVPActivity;
import com.jdkgroup.bitcoinprice.R;
import com.jdkgroup.model.callapi.currentprice.MainCurrentPrice;
import com.jdkgroup.presenter.CurrentPricePresenter;
import com.jdkgroup.utils.AppUtils;
import com.jdkgroup.view.CurrentPriceView;

import static com.jdkgroup.utils.AppUtils.appUtilsInstance;

public class MVPDemo extends SimpleMVPActivity<CurrentPricePresenter, CurrentPriceView> implements CurrentPriceView {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        bindViews();
        toolBar.setTitle("Current Price");

        getPresenter().apiCurrentPrice();

    }


    @Override
    public void onFailure(String message) {
        appUtilsInstance().showToast(getActivity(), message + "");
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
    public void callCurrentPrice(MainCurrentPrice response) {
        appUtilsInstance().showToast(getActivity(), response + "");
    }
}
