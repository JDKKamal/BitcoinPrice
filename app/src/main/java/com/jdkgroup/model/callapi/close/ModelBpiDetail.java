package com.jdkgroup.model.callapi.close;

public class ModelBpiDetail {
    private String date;
    private String rate;

    public ModelBpiDetail(String date, String rate) {
        this.date = date;
        this.rate = rate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRate() {
        return rate;
    }

    public void setRate_float(String rate) {
        this.rate = rate;
    }
}
