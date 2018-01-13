package com.jdkgroup.view;

import com.jdkgroup.baseclass.BaseView;
import com.jdkgroup.model.callapi.currentprice.MainCurrentPrice;
import com.jdkgroup.model.supportedcurrencies.ModelCurrencyDetail;

import java.util.List;

public interface CurrencyView extends BaseView {
    void callCurrency(List<ModelCurrencyDetail> response);
    void callCurrentPriceWithCurrency(MainCurrentPrice response);
}
