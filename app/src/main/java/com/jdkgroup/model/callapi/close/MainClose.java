package com.jdkgroup.model.callapi.close;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class MainClose {
    @SerializedName("disclaimer")
    @Expose
    String disclaimer;
    @SerializedName("bpi")
    @Expose
    private Map<String, String> bpi;
    @SerializedName("time")
    @Expose
    ModelTime time;

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public Map<String, String> getBpi() {
        return bpi;
    }

    public void setBpi(Map<String, String> bpi) {
        this.bpi = bpi;
    }

    public ModelTime getTime() {
        return time;
    }

    public void setTime(ModelTime time) {
        this.time = time;
    }
}
