package com.zb.botoken.model;

import java.math.BigInteger;

public class Currency {

    private String currencyName;

    private String currencyCount;

    public Currency(String currencyName, String currencyCount) {
        this.currencyName = currencyName;
        this.currencyCount = currencyCount;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencyCount() {
        return currencyCount;
    }

    public void setCurrencyCount(String currencyCount) {
        this.currencyCount = currencyCount;
    }
}
