package com.jdkgroup.presenter;

import android.app.Activity;
import android.os.Handler;

import com.jdkgroup.baseclass.BasePresenter;
import com.jdkgroup.bitcoinprice.activity.CurrentPriceActivity;
import com.jdkgroup.utils.AppUtils;
import com.jdkgroup.view.SplashScreenView;

public class SplashScreenPresenter extends BasePresenter<SplashScreenView> {
    public void getSplashScreenWait(int timeOut, final Activity activity) {
        new Handler().postDelayed(() -> {
            AppUtils.startActivity(activity, CurrentPriceActivity.class);
            activity.finish();
        }, timeOut);
    }
}