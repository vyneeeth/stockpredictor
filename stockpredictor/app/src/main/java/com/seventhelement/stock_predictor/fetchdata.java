package com.seventhelement.stock_predictor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;



//import static com.seventhelement.stock_predictor.Prediction.d2array;
import static com.seventhelement.stock_predictor.Prediction.stocksymbol;
import static com.seventhelement.stock_predictor.Prediction.url;

public class fetchdata extends AsyncTask<Void,Void,Void > {
    String data= "";
    public static String orgdata,opendata,highdata,lowdata,volumedata,realtimedate= "";
    String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    final private ArrayList<Double> closed = new ArrayList<Double>();
    final private ArrayList<Float> closedtoint = new ArrayList<Float>();
    public static double[] target;
    InputStream inputStream;


    @SuppressLint("SimpleDateFormat")
    @Override
    protected Void doInBackground(Void... voids) {

        try {

            try {
                Log.d("stocksymbol",stocksymbol);

                URL url = new URL("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol="
                        + stocksymbol + "&apikey=8L1NA3KMED22CFE5");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                inputStream= con.getInputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line = "";

                while (line != null) {
                    line = bufferedReader.readLine();
                    data = data + line;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            JSONObject jsonObject = new JSONObject(data);
            JSONObject jsonObject1 = (JSONObject) jsonObject.get("Meta Data");
            String date1 = jsonObject1.get("3. Last Refreshed").toString();
            JSONObject times = (JSONObject) jsonObject.getJSONObject("Time Series (Daily)");

            Log.d("Date", "Today date is" + date);
            JSONObject JO;

            JSONArray keys = times.names();
            for (int i = 0; i < 60; i++) {

                String key;
                key = keys.getString(i);

                try {
                    JSONObject day = times.getJSONObject(key);
                    closed.add(day.getDouble("4. close"));

                } catch(JSONException e){
                    e.printStackTrace();
                }

            }

            JO = times.getJSONObject(date1);

            if (closed.size() <59) {
                Log.d("Less Data", "Data not available");
            }
            else {

                Log.d("Closed data Array", "--->" + closed);
                Log.d("Closed data Array", "-->" + closed.size());

                for (Double d : closed) {
                    closedtoint.add(d.floatValue());
                }
                Log.d("COnverted Array", "--->" + closedtoint);
                Log.d("COnverted Array Size", "--->" + closedtoint.size());

                Collections.reverse(closedtoint);
                target = new double[closedtoint.size()];
                for (int i = 0; i < target.length; i++) {
                    target[i] = closedtoint.get(i).doubleValue();  // java 1.4 style
                    // or:
                    target[i] = closedtoint.get(i);                // java 1.5+ style (outboxing)
                }
            }
            opendata = JO.get("1. open").toString();
            highdata = JO.get("2. high").toString();
            lowdata = JO.get("3. low").toString();
            orgdata = JO.get("4. close").toString();
            volumedata = JO.get("5. volume").toString();
            realtimedate = date1;








        } catch(JSONException e){
            e.printStackTrace();
        }

        if (closed.size() <60) {
            Log.d("Less Data 3", "Data not available");
            /*checkyahooapi();*/
        }
        return null;
    }

    /*private void checkyahooapi() {

        try {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://apidojo-yahoo-finance-v1.p.rapidapi.com/stock/v3/get-historical-data?symbol="+stocksymbol+"&region=IN")
                .get()
                .addHeader("x-rapidapi-key", "41a4ea830emsh2753cf810e4821cp135cb6jsn9af10d9ba1da")
                .addHeader("x-rapidapi-host", "apidojo-yahoo-finance-v1.p.rapidapi.com")
                .build();


            Response response = client.newCall(request).execute();
            Log.d("stock price","lsk"+response);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (closed.size() <60) {
            Log.d("Less Data 2", "Data not available");

        }
        else {
            Prediction.orgview.setText(orgdata);
            Prediction.volume.setText(volumedata);
            Prediction.highp.setText(highdata);
            Prediction.lowp.setText(lowdata);
            Prediction.closep.setText(orgdata);
            Prediction.openp.setText(opendata);
            Prediction.date.setText(realtimedate);
            Prediction.predval.setText("");
            Prediction.stockgraph();
        }

    }



}