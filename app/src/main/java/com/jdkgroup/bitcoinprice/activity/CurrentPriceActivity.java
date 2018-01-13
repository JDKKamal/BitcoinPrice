package com.jdkgroup.bitcoinprice.activity;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.jdkgroup.baseclass.SimpleMVPActivity;
import com.jdkgroup.bitcoinprice.R;
import com.jdkgroup.customview.mpchart.MyMarkerView;
import com.jdkgroup.model.callapi.currentprice.MainCurrentPrice;
import com.jdkgroup.model.callapi.currentprice.ModelBpiDetail;
import com.jdkgroup.model.chart.ChartCurrentPrice;
import com.jdkgroup.presenter.CurrentPricePresenter;
import com.jdkgroup.view.CurrentPriceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class CurrentPriceActivity extends SimpleMVPActivity<CurrentPricePresenter, CurrentPriceView> implements CurrentPriceView, OnChartGestureListener, OnChartValueSelectedListener {

    @BindView(R.id.lineChart)
    LineChart lineChart;

    private XAxis xAxis;
    private YAxis leftAxis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_price);

        bindViews();

        toolBar.setTitle("Current Price");
        toolBarSetFont(toolBar);

        lineChartInit();
        lineChartListener();
        markerViewInit(R.layout.custom_marker_view);
        AxisXYInit(XAxis.XAxisPosition.BOTTOM);
        legendInit(Legend.LegendForm.LINE);

        getPresenter().apiCurrentPrice();
    }

    private void lineChartListener() {
        lineChart.setOnChartGestureListener(this);
        lineChart.setOnChartValueSelectedListener(this);
    }

    private void lineChartInit() {
        lineChart.setDrawGridBackground(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.getAxisRight().setEnabled(false); //TODO FIXED DISPLAY
    }

    private void markerViewInit(int layoutResource) {
        MyMarkerView markerView = new MyMarkerView(this, layoutResource);
        markerView.setChartView(lineChart);
        lineChart.setMarker(markerView);
    }

    private void AxisXYInit(XAxis.XAxisPosition xAxisPosition) {
        xAxis = lineChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setPosition(xAxisPosition);

        leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawLimitLinesBehindData(true);
    }

    private void legendInit(Legend.LegendForm legendForm) {
        Legend legend = lineChart.getLegend();
        legend.setForm(legendForm);
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
        lineChart.invalidate(); //TODO CLICK SHOW CHART DISPLAY SOLUTION
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        if (lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
            lineChart.highlightValues(null);
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    private void setData(List<ChartCurrentPrice> listChartCurrentPrice) {
        ArrayList<String> listBottomValue = new ArrayList<>();
        ArrayList<Entry> listEntry = new ArrayList<>();

        for (int i = 0; i < listChartCurrentPrice.size(); i++) {
            ChartCurrentPrice chartCurrentPrice = listChartCurrentPrice.get(i);
            listEntry.add(new Entry(chartCurrentPrice.getIndex(), chartCurrentPrice.getRate_float()));
            listBottomValue.add(chartCurrentPrice.getCode());
        }
        bottomValueSet(listBottomValue);
        lineDataSetInit(listEntry);
    }

    private void bottomValueSet(ArrayList<String> listBottomValue) {
        Object[] bottomValue = listBottomValue.toArray();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter((value, axis) -> bottomValue[(int) value % bottomValue.length].toString());
    }

    private void lineDataSetInit(ArrayList<Entry> listEntry) {
        LineDataSet lineDataSet;
        if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0) {
            lineDataSet = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            lineDataSet.setValues(listEntry);
        } else {
            lineDataSet = new LineDataSet(listEntry, "Current Price");
            lineDataSet.setDrawIcons(false);

            lineDataSet.enableDashedLine(10f, 5f, 0f);
            lineDataSet.enableDashedHighlightLine(10f, 5f, 0f);
            lineDataSet.setColor(Color.BLACK);
            lineDataSet.setCircleColor(Color.BLACK);
            lineDataSet.setLineWidth(2f);
            lineDataSet.setCircleRadius(3f);
            lineDataSet.setDrawCircleHole(false);
            lineDataSet.setValueTextSize(9f);
            lineDataSet.setDrawFilled(true);
            lineDataSet.setFormLineWidth(1f);
            lineDataSet.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            lineDataSet.setFormSize(15.f);

            if (Utils.getSDKInt() >= 18) {
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                lineDataSet.setFillDrawable(drawable);
            } else {
                lineDataSet.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> listILineDataSet = new ArrayList<>();
            listILineDataSet.add(lineDataSet);
            listILineDataSet.trimToSize();

            LineData data = new LineData(listILineDataSet);

            // set data
            lineChart.setData(data);
        }
    }
}
