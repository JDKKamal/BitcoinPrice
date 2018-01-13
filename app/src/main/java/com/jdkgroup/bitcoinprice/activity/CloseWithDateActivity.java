package com.jdkgroup.bitcoinprice.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;

import com.jdkgroup.baseclass.SimpleMVPActivity;
import com.jdkgroup.bitcoinprice.R;
import com.jdkgroup.bitcoinprice.adapter.CloseAdapter;
import com.jdkgroup.model.callapi.close.MainClose;
import com.jdkgroup.model.callapi.close.ModelBpiDetail;
import com.jdkgroup.presenter.ClosePresenter;
import com.jdkgroup.view.CloseView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CloseWithDateActivity extends SimpleMVPActivity<ClosePresenter, CloseView> implements CloseView {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.appBtnRefresh)
    AppCompatButton appBtnRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close);
        ButterKnife.bind(this);

        bindViews();
        toolBar.setTitle("Close");

        setRecyclerView(recyclerView, 0, recyclerViewLinearLayout);

        getPresenter().apiClose(this);
    }

    @NonNull
    @Override
    public ClosePresenter createPresenter() {
        return new ClosePresenter();
    }

    @NonNull
    @Override
    public CloseView attachView() {
        return this;
    }

    @Override
    public void callClose(MainClose response) {
        List<ModelBpiDetail> listBpiDetail = new ArrayList<>();

        int index = 0;
        for (Map.Entry<String, String> entry : response.getBpi().entrySet()) {
            listBpiDetail.add(new ModelBpiDetail(entry.getKey(), entry.getValue()));
            index++;
        }

        recyclerView.setAdapter(new CloseAdapter(this, listBpiDetail));
    }

    @Override
    public void onFailure(String message) {

    }

    @OnClick(R.id.appBtnRefresh)
    public void onViewClicked() {
        getPresenter().apiClose(this);
    }
}
