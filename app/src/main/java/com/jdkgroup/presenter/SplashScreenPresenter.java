package com.jdkgroup.presenter;

import android.os.Handler;

import com.jdkgroup.baseclass.BasePresenter;
import com.jdkgroup.bitcoinprice.DesktopActivity;
import com.jdkgroup.utils.AppUtils;
import com.jdkgroup.view.SplashScreenView;

import static com.jdkgroup.utils.Preference.preferenceInstance;

public class SplashScreenPresenter extends BasePresenter<SplashScreenView> {
    public void getSplashScreenWait(int timeOut) {
        new Handler().postDelayed(() -> {
            preferenceInstance(getView().getActivity()).setIsLogin(true);

            AppUtils.startActivity(getView().getActivity(), DesktopActivity.class);
            getView().getActivity().finish();

        }, timeOut);
    }
}