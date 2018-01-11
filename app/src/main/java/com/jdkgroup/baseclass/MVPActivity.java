package com.jdkgroup.baseclass;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.jdkgroup.bitcoinprice.R;

import butterknife.BindView;

public abstract class MVPActivity<P extends BasePresenter<V>, V extends BaseView> extends BaseActivity {

    @BindView(R.id.toolBar)
    public Toolbar toolBar;

    private P presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
        presenter.attachView(attachView());

        setSupportActionBar(toolBar);
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

    public Activity getActivity() {
        return this;
    }
}
