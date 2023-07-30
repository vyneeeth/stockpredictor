package com.seventhelement.stock_predictor.listeners;

public interface PredictionCallback {

    public void beforeApiCall();

    public void onResult(String result);
}
