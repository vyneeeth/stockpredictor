package com.seventhelement.stock_predictor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.SearchView;

import java.io.InputStream;
import java.util.ArrayList;

public class nyseactivity extends AppCompatActivity {
    private SearchView searchView;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    LinearLayoutManager linearLayoutManager;
    ArrayList<ExampleItem> examplelist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nyseactivity);

        searchView=findViewById(R.id.search_view);



        InputStream inputStream=getResources().openRawResource(R.raw.nysedata);
        CSVFile csvFile=new CSVFile(inputStream);
        examplelist=csvFile.read();

        recyclerView=findViewById(R.id.nyse_recycler);
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