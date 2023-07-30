package com.seventhelement.stock_predictor;



import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class CSVFile {
    InputStream inputStream;
    public CSVFile(InputStream inputStream){

        this.inputStream = inputStream;
    }

    public ArrayList read(){
        ArrayList<ExampleItem> resultList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            reader.readLine();
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(",");
                ExampleItem item=new ExampleItem();
                item.setSymbol(row[0]);
                item.setName(row[1]);

                resultList.add(item);

            }
            Log.d("List Item","here  "+ resultList);
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: "+e);
            }
        }
        return resultList;
    }
}
