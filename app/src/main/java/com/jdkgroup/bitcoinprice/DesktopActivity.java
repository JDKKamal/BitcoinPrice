package com.jdkgroup.bitcoinprice;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.jdkgroup.baseclass.BaseActivity;
import com.jdkgroup.bitcoinprice.activity.CloseActivity;
import com.jdkgroup.bitcoinprice.activity.CurrencyActivity;
import com.jdkgroup.bitcoinprice.activity.CurrentPriceActivity;
import com.jdkgroup.bitcoinprice.activity.CurrentPriceHalfPieActivity;
import com.jdkgroup.bitcoinprice.adapter.DesktopAdapter;
import com.jdkgroup.utils.AppUtils;
import com.jdkgroup.utils.Logging;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jdkgroup.utils.AppUtils.appUtilsInstance;

public class DesktopActivity extends BaseActivity implements DesktopAdapter.ItemListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.toolBar)
    Toolbar toolBar;

    private DesktopAdapter desktopAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desktop);
        ButterKnife.bind(this);

        bindViews();
        setSupportActionBar(toolBar);

        toolBar.setTitle("Bitcoin Price");

        setRecyclerView(recyclerView, 0, recyclerViewLinearLayout);

        desktopAdapter = new DesktopAdapter(this, listDesktop());
        desktopAdapter.setOnListener(this);
        recyclerView.setAdapter(desktopAdapter);
    }

    private List<String> listDesktop() {
        List<String> listStr = new ArrayList<>();
        listStr.add("Today");
        listStr.add("Current Price");
        listStr.add("Currency");
        listStr.add("Close");

        return listStr;
    }

    @Override
    public void onClickDesktop(String str) {
        Logging.i(str);

        if (str.equalsIgnoreCase("Today")) {
            appUtilsInstance().startActivity(getActivity(), CloseActivity.class);
        } else if (str.equalsIgnoreCase("Current Price")) {
            appUtilsInstance().startActivity(getActivity(), CurrentPriceActivity.class);
        } else if (str.equalsIgnoreCase("Currency")) {
            appUtilsInstance().startActivity(getActivity(), CurrencyActivity.class);
        } else if (str.equalsIgnoreCase("Close")) {
            appUtilsInstance().startActivity(getActivity(), CurrentPriceHalfPieActivity.class);
        }
    }
}
