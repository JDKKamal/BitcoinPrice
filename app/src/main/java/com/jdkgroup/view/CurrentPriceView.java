package com.jdkgroup.view;

import com.jdkgroup.baseclass.BaseView;
import com.jdkgroup.model.callapi.currentprice.MainCurrentPrice;

public interface CurrentPriceView extends BaseView {
    void callCurrentPrice(MainCurrentPrice response);
}
