package com.jdkgroup.baseclass;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import com.jdkgroup.bitcoinprice.R;
import butterknife.BindView;

public abstract class SimpleMVPActivity<P extends BasePresenter<V>, V extends BaseView> extends BaseActivity {
    private P presenter;

    @BindView(R.id.toolBar)
    public Toolbar toolBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolBar);

        presenter = createPresenter();
        presenter.attachView(attachView());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public abstract
    @NonNull
    P createPresenter();

    public abstract
    @NonNull
    V attachView();

    public P getPresenter() {
        return presenter;
    }

    public boolean hasInternet() {
        return isInternet();
    }

    public Activity getActivity() {
        return this;
    }
}
