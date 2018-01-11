package com.jdkgroup.model.chart;

public class ChartCurrentPrice
{
    private int index;
    private String code;
    private Float rate_float;

    public ChartCurrentPrice(int index, String code, Float rate_float) {
        this.index = index;
        this.code = code;
        this.rate_float = rate_float;
    }

    public float getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getRate_float() {
        return rate_float;
    }

    public void setRate_float(float rate_float) {
        this.rate_float = rate_float;
    }
}
