package com.seventhelement.stock_predictor.utils;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class AppUtils {

    private AppUtils() {}

    public static String readInputStreamAsString(InputStream in)
            throws IOException {

        BufferedInputStream bis = new BufferedInputStream(in);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int result = bis.read();
        while(result != -1) {
            byte b = (byte)result;
            buf.write(b);
            result = bis.read();
        }
        return buf.toString();
    }

    public static String predictStockPrice(String stockname) {

        String result = "";

        try {

            String urlString = "https://stock-prediction7.herokuapp.com";
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(0);
                urlConnection.setReadTimeout(0);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setDoInput(true);
                String data = URLEncoder.encode("stockname", "UTF-8") + "=" + URLEncoder.encode(stockname.trim(), "UTF-8");
                urlConnection.connect();
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(data);
                wr.flush();
                InputStream stream = urlConnection.getInputStream();
                String responseHtml = AppUtils.readInputStreamAsString(stream);

                result = responseHtml.substring(responseHtml.indexOf("[[") + 2, responseHtml.indexOf("]]"));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }

        } catch (Throwable t) {
            t.printStackTrace();
        }

        return result;
    }
}
