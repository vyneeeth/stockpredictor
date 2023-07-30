package com.seventhelement.stock_predictor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.SearchView;

import java.io.InputStream;
import java.util.ArrayList;

public class bseactivity extends AppCompatActivity {
    private MyAdapter myAdapter;
    LinearLayoutManager linearLayoutManager;
    ArrayList<ExampleItem> examplelist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bseactivity);
        SearchView searchView=findViewById(R.id.search_view);



        InputStream inputStream=getResources().openRawResource(R.raw.bsedata);
        CSVFile csvFile=new CSVFile(inputStream);
        examplelist=csvFile.read();

        RecyclerView recyclerView=findViewById(R.id.bse_recycler);
        linearLayoutManager=new LinearLayoutManager(this);
        myAdapter=new MyAdapter(examplelist,getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                myAdapter.getFilter().filter(s);
                return false;
            }
        });
    }
}