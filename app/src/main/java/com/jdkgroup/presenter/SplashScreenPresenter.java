package com.jdkgroup.presenter;

import android.os.Handler;

import com.jdkgroup.baseclass.BasePresenter;
import com.jdkgroup.bitcoinprice.DesktopActivity;
import com.jdkgroup.bitcoinprice.activity.MVPDemo;
import com.jdkgroup.utils.AppUtils;
import com.jdkgroup.view.SplashScreenView;

import static com.jdkgroup.utils.AppUtils.appUtilsInstance;
import static com.jdkgroup.utils.Preference.preferenceInstance;

public class SplashScreenPresenter extends BasePresenter<SplashScreenView> {
    public void getSplashScreenWait(int timeOut) {
        new Handler().postDelayed(() -> {
            preferenceInstance(getView().getActivity()).setIsLogin(true);

            appUtilsInstance().startActivity(getView().getActivity(), MVPDemo.class);
            getView().getActivity().finish();

        }, timeOut);
    }
}