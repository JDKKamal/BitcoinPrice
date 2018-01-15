package com.jdkgroup.presenter;

import com.jdkgroup.baseclass.BasePresenter;
import com.jdkgroup.interacter.InterActorCallback;
import com.jdkgroup.model.callapi.currentprice.MainCurrentPrice;
import com.jdkgroup.model.supportedcurrencies.ModelCurrencyDetail;
import com.jdkgroup.view.CurrencyView;

import java.util.List;

public class CurrencyPresenter extends BasePresenter<CurrencyView> {
    public void apiCurrency() {
            getAppInteractor().callApiCurrency(getView().getActivity(), new InterActorCallback<List<ModelCurrencyDetail>>() {
                @Override
                public void onStart() {
                    getView().showProgressDialog(true);
                }

                @Override
                public void onResponse(List<ModelCurrencyDetail> response) {
                    getView().callCurrency(response);
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

    public void apiCurrentPriceWithCurrency(String currency) {
            getAppInteractor().callApiCurrentPriceWithCurrency(getView().getActivity(), currency, new InterActorCallback<MainCurrentPrice>() {
                @Override
                public void onStart() {
                    getView().showProgressDialog(true);
                }

                @Override
                public void onResponse(MainCurrentPrice response) {
                    getView().callCurrentPriceWithCurrency(response);
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