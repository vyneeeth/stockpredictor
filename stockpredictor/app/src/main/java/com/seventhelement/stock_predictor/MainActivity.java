package com.seventhelement.stock_predictor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import java.io.InputStream;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity  {

    private MyAdapter myAdapter;
    LinearLayoutManager linearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SearchView searchView=findViewById(R.id.search_view);


        InputStream inputStream=getResources().openRawResource(R.raw.nasdaq);
        CSVFile csvFile=new CSVFile(inputStream);
        ArrayList<ExampleItem> example_list=csvFile.read();

        RecyclerView recyclerView=findViewById(R.id.myrecyclerview);
        linearLayoutManager=new LinearLayoutManager(this);
        myAdapter=new MyAdapter(example_list,getApplicationContext());
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

    public void nse_btn(View view) {
        Intent intent=new Intent(this,nseactivity.class);
        startActivity(intent);
    }

    public void bse_btn(View view) {
        Intent intent=new Intent(this,bseactivity.class);
        startActivity(intent);
    }

    public void nyse_btn(View view) {
        Intent intent=new Intent(this,nyseactivity.class);
        startActivity(intent);
    }


}