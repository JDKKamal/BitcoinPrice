package com.jdkgroup.model.callapi.currentprice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class MainCurrentPrice {
    @SerializedName("disclaimer")
    @Expose
    String disclaimer;
    @SerializedName("chartName")
    @Expose
    String chartName;
    @SerializedName("time")
    @Expose
    ModelTime time;

    @SerializedName("bpi")
    @Expose
    private Map<String, ModelBpiDetail> bpi;

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public String getChartName() {
        return chartName;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    public ModelTime getTime() {
        return time;
    }

    public void setTime(ModelTime time) {
        this.time = time;
    }

    public Map<String, ModelBpiDetail> getBpi() {
        return bpi;
    }

    public void setBpi(Map<String, ModelBpiDetail> bpi) {
        this.bpi = bpi;
    }
}
