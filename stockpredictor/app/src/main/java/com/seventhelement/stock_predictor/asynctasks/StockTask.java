package com.seventhelement.stock_predictor.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.seventhelement.stock_predictor.listeners.PredictionCallback;
import com.seventhelement.stock_predictor.utils.AppUtils;

public class StockTask extends AsyncTask<String, Void, String> {

        private PredictionCallback predictionCallback;
        public StockTask(PredictionCallback predictionCallback) {this.predictionCallback = predictionCallback; }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if(predictionCallback != null) predictionCallback.beforeApiCall();
        }

        @Override
        protected String doInBackground(String... args) {

            String stockname = args[0];
            Log.d("stock-task",stockname);
            return AppUtils.predictStockPrice(stockname);
        }

        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);

            if(predictionCallback != null) predictionCallback.onResult(result);
        }
    }