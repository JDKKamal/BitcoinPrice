package com.jdkgroup.bitcoinprice.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.jdkgroup.baseclass.SimpleMVPActivity;
import com.jdkgroup.bitcoinprice.R;
import com.jdkgroup.model.callapi.currentprice.MainCurrentPrice;
import com.jdkgroup.model.callapi.currentprice.ModelBpiDetail;
import com.jdkgroup.model.chart.ChartCurrentPrice;
import com.jdkgroup.presenter.CurrentPricePresenter;
import com.jdkgroup.view.CurrentPriceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class CurrentPriceHalfPieActivity extends SimpleMVPActivity<CurrentPricePresenter, CurrentPriceView> implements CurrentPriceView {

    @BindView(R.id.pieChart)
    PieChart pieChart;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_price_half_pie);

        bindViews();
        toolBar.setTitle("Current Price");

        getPresenter().apiCurrentPrice();

        pieCharInit();
        legendInit();


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void pieCharInit()
    {
        pieChart.setBackgroundColor(Color.WHITE);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setDrawCenterText(true);
        pieChart.setRotationEnabled(false);
        pieChart.setHighlightPerTapEnabled(true);
        pieChart.setMaxAngle(180f); // HALF CHART
        pieChart.setRotationAngle(180f);
        pieChart.setCenterTextOffset(0, -20);
        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        // entry label styling
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setEntryLabelTextSize(12f);
    }

    public void  legendInit()
    {
        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setXEntrySpace(7f);
        legend.setYEntrySpace(0f);
        legend.setYOffset(0f);
    }

    @NonNull
    @Override
    public CurrentPricePresenter createPresenter() {
        return new CurrentPricePresenter();
    }

    @NonNull
    @Override
    public CurrentPriceView attachView() {
        return this;
    }

    @Override
    public void onFailure(String message) {

    }

    @Override
    public void callCurrentPrice(MainCurrentPrice response) {
        List<ChartCurrentPrice> listChartCurrentPrice = new ArrayList<>();

        int index = 0;
        for (Map.Entry<String, ModelBpiDetail> entry : response.getBpi().entrySet()) {

            listChartCurrentPrice.add(new ChartCurrentPrice(index, entry.getValue().getCode(), entry.getValue().getRate_float()));
            index++;
        }

        setData(listChartCurrentPrice);
    }

    private void setData(List<ChartCurrentPrice> listChartCurrentPrice ) {
        ArrayList<PieEntry> listPieEntry = new ArrayList<>();

        for (int i = 0; i < listChartCurrentPrice.size(); i++) {
            ChartCurrentPrice chartCurrentPrice = listChartCurrentPrice.get(i);
            listPieEntry.add(new PieEntry(chartCurrentPrice.getRate_float(), chartCurrentPrice.getCode()));
        }

        PieDataSet dataSet = new PieDataSet(listPieEntry, "Current Price");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        pieChart.setData(data);

        pieChart.invalidate();
    }


}
