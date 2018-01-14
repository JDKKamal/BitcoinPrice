package com.jdkgroup.presenter;

import com.jdkgroup.baseclass.BasePresenter;
import com.jdkgroup.interacter.InterActorCallback;
import com.jdkgroup.model.callapi.currentprice.MainCurrentPrice;
import com.jdkgroup.utils.Logging;
import com.jdkgroup.view.CurrentPriceView;

public class CurrentPricePresenter extends BasePresenter<CurrentPriceView> {
    public void apiCurrentPrice() {
        if (hasInternet()) {
            getAppInteractor().callApiCurrentPrice(getView().getActivity(), new InterActorCallback<MainCurrentPrice>() {
                @Override
                public void onStart() {
                    getView().showProgressDialog(true);
                }

                @Override
                public void onResponse(MainCurrentPrice response) {
                    getView().callCurrentPrice(response);
                }

                @Override
                public void onFinish() {
                    getView().showProgressDialog(false);
                }

                @Override
                public void onError(String message) {
                    getView().onFailure(message);
                }

            });
        }
    }
}