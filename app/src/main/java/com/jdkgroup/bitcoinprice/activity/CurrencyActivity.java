package com.jdkgroup.bitcoinprice.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.jdkgroup.baseclass.SimpleMVPActivity;
import com.jdkgroup.bitcoinprice.R;
import com.jdkgroup.dialog.SpinnerDialog;
import com.jdkgroup.model.callapi.currentprice.MainCurrentPrice;
import com.jdkgroup.model.callapi.currentprice.ModelBpiDetail;
import com.jdkgroup.model.chart.ChartCurrentPrice;
import com.jdkgroup.model.supportedcurrencies.ModelCurrencyDetail;
import com.jdkgroup.presenter.CurrencyPresenter;
import com.jdkgroup.utils.AppUtils;
import com.jdkgroup.view.CurrencyView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jdkgroup.utils.AppUtils.appUtilsInstance;

public class CurrencyActivity extends SimpleMVPActivity<CurrencyPresenter, CurrencyView> implements CurrencyView, OnChartValueSelectedListener {

    @BindView(R.id.pieChart)
    PieChart pieChart;
    @BindView(R.id.appEdtCurrency)
    AppCompatEditText appEdtCurrency;

    ModelCurrencyDetail selectedCurrency = null;
    List<ModelCurrencyDetail> listCurrencyDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
        ButterKnife.bind(this);

        bindViews();
        toolBar.setTitle("Currency");

        pieChartInit();
        legendInit();

    }

    @Override
    protected void onStart() {
        super.onStart();
        getPresenter().apiCurrency();
    }

    @NonNull
    @Override
    public CurrencyPresenter createPresenter() {
        return new CurrencyPresenter();
    }

    @NonNull
    @Override
    public CurrencyView attachView() {
        return this;
    }

    public void pieChartInit() {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setCenterText(generateCenterSpannableText());

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);

        pieChart.setDrawCenterText(true);

        pieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        // entry label styling
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(12f);

        // add a selection listener
        pieChart.setOnChartValueSelectedListener(this);
    }

    private void legendInit() {
        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setXEntrySpace(7f);
        legend.setYEntrySpace(0f);
        legend.setYOffset(0f);
    }


    private void setData(List<ChartCurrentPrice> listChartCurrentPrice) {
        ArrayList<PieEntry> listPieEntry = new ArrayList<>();

        for (int i = 0; i < listChartCurrentPrice.size(); i++) {
            ChartCurrentPrice chartCurrentPrice = listChartCurrentPrice.get(i);
            listPieEntry.add(new PieEntry(chartCurrentPrice.getRate_float(), chartCurrentPrice.getCode() + " (" + String.valueOf(chartCurrentPrice.getRate_float()) + ")"));
        }

        PieDataSet dataSet = new PieDataSet(listPieEntry, "Election Results");

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        //add a lot of colors
        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS) colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS) colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS) colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS) colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS) colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.BLACK);
        pieChart.setData(data);

        // undo all highlights
        pieChart.highlightValues(null);

        pieChart.invalidate();
    }

    private SpannableString generateCenterSpannableText() {
        SpannableString s = new SpannableString("Current Price\ndeveloped by Lakhani Kamlesh");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 13, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 13, s.length() - 15, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 13, s.length() - 15, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 13, s.length() - 15, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 13, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 15, s.length(), 0);
        return s;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    @Override
    public void onFailure(String message) {

    }

    @Override
    public void callCurrency(List<ModelCurrencyDetail> response) {
        listCurrencyDetail = response;
    }

    @Override
    public void callCurrentPriceWithCurrency(MainCurrentPrice response) {
        List<ChartCurrentPrice> listChartCurrentPrice = new ArrayList<>();

        int index = 0;
        for (Map.Entry<String, ModelBpiDetail> entry : response.getBpi().entrySet()) {

            listChartCurrentPrice.add(new ChartCurrentPrice(index, entry.getValue().getCode(), entry.getValue().getRate_float()));
            index++;
        }
        setData(listChartCurrentPrice);
    }

    public void openCurrencyDialog() {
        if (listCurrencyDetail.size() > 0) {
            SpinnerDialog sd = new SpinnerDialog(this, getStringFromId(R.string.sp_select_currency), new SpinnerDialog.OnItemClick() {
                @Override
                public void selectedItem(Object object) {
                    selectedCurrency = (ModelCurrencyDetail) object;
                    appEdtCurrency.setText(selectedCurrency.getCountry());

                    getPresenter().apiCurrentPriceWithCurrency(selectedCurrency.getCurrency());
                }
            }, listCurrencyDetail);
            sd.show();
        } else {
            appUtilsInstance().showToastById(this, R.string.no_data);
        }
    }

    @OnClick(R.id.appEdtCurrency)
    public void onViewClicked() {
        openCurrencyDialog();
    }
}
