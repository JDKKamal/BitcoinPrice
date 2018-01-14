package com.jdkgroup.presenter;

import android.os.Handler;

import com.jdkgroup.baseclass.BasePresenter;
import com.jdkgroup.bitcoinprice.activity.CurrentPriceHalfPieActivity;
import com.jdkgroup.utils.AppUtils;
import com.jdkgroup.utils.Preference;
import com.jdkgroup.view.SplashScreenView;

import static com.jdkgroup.utils.Preference.*;

public class SplashScreenPresenter extends BasePresenter<SplashScreenView> {
    public void getSplashScreenWait(int timeOut) {
        new Handler().postDelayed(() -> {
            preferenceInstance(getView().getActivity()).setIsLogin(true);

            AppUtils.startActivity(getView().getActivity(), CurrentPriceHalfPieActivity.class);
            getView().getActivity().finish();

        }, timeOut);
    }
}