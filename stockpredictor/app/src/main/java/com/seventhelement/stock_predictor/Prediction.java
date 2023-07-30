package com.seventhelement.stock_predictor;


import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.seventhelement.stock_predictor.asynctasks.StockTask;
import com.seventhelement.stock_predictor.listeners.PredictionCallback;

import java.util.ArrayList;
import lecho.lib.hellocharts.view.LineChartView;

public class Prediction extends AppCompatActivity implements PredictionCallback {

    public static String stocksymbol, stockname, url;
    TextView predview, stocksymboltv, stocknametv;
    public static double ans[] = new double[2];
    public static TextView orgview, date, predval, openp, highp, lowp, closep, volume;
    public static LineChartView lineChartView;
    public static Context ct;
    public static ProgressBar progressBar;


    public static ArrayList<Float> d1array = new ArrayList<>();


    public static GraphView graphView;
    public static double[] xaxis = new double[60];
    public static double[] yaxis = new double[60];
    public static AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);


        fetchdata process = new fetchdata();
        process.execute();

        progressBar = findViewById(R.id.progressBar);
        predview = findViewById(R.id.pred_valtv);
        orgview = findViewById(R.id.org_val);
        stocknametv = findViewById(R.id.stock_name);
        stocksymboltv = findViewById(R.id.stock_symbol);
        predval = findViewById(R.id.pred_val);
        date = findViewById(R.id.date);
        openp = findViewById(R.id.openprice);
        highp = findViewById(R.id.highprice);
        lowp = findViewById(R.id.lowprice);
        closep = findViewById(R.id.closeprice);
        volume = findViewById(R.id.volume);
        graphView = findViewById(R.id.graph);


        Intent intent = getIntent();
        stocksymbol = intent.getStringExtra("Symbol");
        stockname = intent.getStringExtra("Name");

        stocksymboltv.setText(stocksymbol);
        stocknametv.setText(stockname);

        new StockTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, stocksymbol);
    }


    public static void stockgraph() {

        xaxis = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
                20, 21, 22, 23, 24, 25, 26, 27, 28, 29,
                30, 31, 32, 33, 34, 35, 36, 37, 38, 39,
                40, 41, 42, 43, 44, 45, 46, 47, 48, 49,
                50, 51, 52, 53, 54, 55, 56, 57, 58, 59,
                60};
        yaxis = fetchdata.target;
        Log.d("d1arr", "-->" + yaxis.length);
        Log.d("d2arr", "-->" + xaxis.length);

        try {
            PointsGraphSeries<DataPoint> series;       //an Object of the PointsGraphSeries for plotting scatter graphs
            series = new PointsGraphSeries<>(data());   //initializing/defining series to get the data from the method 'data()'
            graphView.addSeries(series);                   //adding the series to the GraphView
            series.setShape(PointsGraphSeries.Shape.POINT);
            series.setSize(5);
            Log.d("idk2", " " + ans);
            String disp[] = new String[2];
            for(int i = 0; i < 2; i++)
                disp[i] = String.format("%.2f", ans[i]);
            predval.setText("In the next week: " + disp[0] + "\nThe week after: " + disp[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DataPoint[] data() {

        DataPoint[] values = new DataPoint[60];     //creating an object of type DataPoint[] of size 'n'
        try {
            float sigmax = 1830, sigmay = 0, sigmax2 = 73810, sigmaxy = 0;
            for (int i = 0; i < 60; i++) {
                DataPoint v = new DataPoint(xaxis[i], yaxis[i]);
                sigmay += yaxis[i];
                sigmaxy += xaxis[i] * yaxis[i];
                values[i] = v;
            }
            float a = ((sigmay * sigmax2) - (sigmax * sigmaxy)) / (60 * sigmax2 - sigmax * sigmax);
            float b = ((60 * sigmaxy) - (sigmax * sigmay)) / ((60 * sigmax2) - (sigmax * sigmax));
            int days = 60;
            for(int i = 0; i < 2; i++) {
                days += 7;
                ans[i] = a + days * b;
            }
            Log.d("idk:", sigmaxy + ", " + sigmax + ", " + sigmay + ", " + sigmax2 + ", " + a + ", " + b + ", ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }


    @Override
    public void beforeApiCall() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResult(String result) {
        progressBar.setVisibility(View.GONE);
        Log.d("idk3", "check " + ans);
    }
}