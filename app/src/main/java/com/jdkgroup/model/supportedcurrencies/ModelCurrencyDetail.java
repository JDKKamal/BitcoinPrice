package com.jdkgroup.model.supportedcurrencies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelCurrencyDetail {
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("country")
    @Expose
    private String country;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}
