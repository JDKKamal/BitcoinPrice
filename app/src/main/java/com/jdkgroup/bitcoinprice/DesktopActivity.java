package com.jdkgroup.bitcoinprice;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.jdkgroup.baseclass.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DesktopActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desktop);

        bindViews();
    }

    private List<String> listDesktop() {
        List<String> listStr = new ArrayList<>();
        listStr.add("Today");
        listStr.add("Current Price");
        listStr.add("Currency");


        return listStr;
    }

}
