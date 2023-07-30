package com.seventhelement.stock_predictor;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static com.seventhelement.stock_predictor.Prediction.ct;
import static com.seventhelement.stock_predictor.Prediction.progressBar;

import com.seventhelement.stock_predictor.asynctasks.StockTask;
import com.seventhelement.stock_predictor.listeners.PredictionCallback;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements Filterable{


    private ArrayList<ExampleItem> maindatalist;
    private ArrayList<ExampleItem> filterdatalist;
    Context context;

    public MyAdapter(ArrayList<ExampleItem> maindatalist,Context ct){
        this.maindatalist=maindatalist;
        filterdatalist=new ArrayList<>(maindatalist);
        context=ct;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view =layoutInflater.inflate(R.layout.row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ExampleItem exampleItem=maindatalist.get(position);
        holder.symbol.setText(exampleItem.getSymbol());
        holder.name.setText(exampleItem.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String charSequence = holder.symbol.getText().toString();
                Log.d("char",charSequence);
                String charSequence2 =  holder.name.getText().toString();
                Intent intent = new Intent(MyAdapter.this.context, Prediction.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Symbol", charSequence);
                intent.putExtra("Name", charSequence2);

                MyAdapter.this.context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return maindatalist.size();
    }





    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView symbol;
        public TextView name;
        public Context context;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            context=itemView.getContext();
            symbol=itemView.findViewById(R.id.symbol);
            name=itemView.findViewById(R.id.name);

        }

    }
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<ExampleItem> filteredlist=new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filteredlist.addAll(filterdatalist);
            }
            else{
                String filterpattern=constraint.toString().toLowerCase().trim();

                for(ExampleItem item: filterdatalist){
                    if(item.getName().toLowerCase().contains(filterpattern)){
                        filteredlist.add(item);
                    }
                    else if(item.getSymbol().toLowerCase().contains(filterpattern)){
                        filteredlist.add(item);
                    }
                }
            }

            FilterResults filterResults=new FilterResults();
            filterResults.values=filteredlist;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            maindatalist.clear();
            maindatalist.addAll((ArrayList) filterResults.values);
            notifyDataSetChanged();
        }
    };
}
