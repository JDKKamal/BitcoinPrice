package com.jdkgroup.bitcoinprice;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.jdkgroup.baseclass.SimpleMVPActivity;
import com.jdkgroup.bitcoinprice.activity.CurrentPriceHalfPieActivity;
import com.jdkgroup.interacter.AppInteractor;
import com.jdkgroup.presenter.SplashScreenPresenter;
import com.jdkgroup.utils.AppUtils;
import com.jdkgroup.view.SplashScreenView;

import static com.jdkgroup.utils.Preference.preferenceInstance;

public class SplashScreen extends SimpleMVPActivity<SplashScreenPresenter, SplashScreenView> implements SplashScreenView {

    private final int SPLASH_TIME_OUT = 3000;
    private AppInteractor appInteractor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);

        //TODO TOKEN IS APPLY WEB SERVICE
        preferenceInstance(this).setIsToken(false);
        preferenceInstance(this).setDeviceToken("bearer ac65df43b1a76c8672f3f4da2c282f822a7bf39c40b47de7af930dc21110f0f4");

        //TODO GET DEVICE INFORMATION
        appInteractor = new AppInteractor();
        appInteractor.getDeviceInfo(getActivity());

        if (!preferenceInstance(this).isLogin()) {
            //TODO SPLASH SCREEN TIME OUT
            getPresenter().getSplashScreenWait(SPLASH_TIME_OUT);
        } else {
            AppUtils.startActivity(getActivity(), DesktopActivity.class);
            finish();
        }
    }

    @NonNull
    @Override
    public SplashScreenPresenter createPresenter() {
        return new SplashScreenPresenter();
    }

    @NonNull
    @Override
    public SplashScreenView attachView() {
        return this;
    }

    @Override
    public void setSplashScreenWait() {

    }

    @Override
    public void onFailure(String message) {

    }
}