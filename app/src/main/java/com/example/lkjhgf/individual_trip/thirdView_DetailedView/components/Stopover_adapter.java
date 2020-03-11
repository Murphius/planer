package com.example.lkjhgf.individual_trip.thirdView_DetailedView.components;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lkjhgf.R;

import java.util.ArrayList;

public class Stopover_adapter extends RecyclerView.Adapter<Stopover_adapter.DetailViewHolder> {
    private ArrayList<Stopover_item> stopovers;

    public static class DetailViewHolder extends RecyclerView.ViewHolder {
        public TextView timeOfDeparture;
        public TextView stopName;
        public DetailViewHolder(View itemView){
            super(itemView);
            timeOfDeparture = itemView.findViewById(R.id.textView65);
            stopName = itemView.findViewById(R.id.textView66);
        }
    }

    public Stopover_adapter(ArrayList<Stopover_item> stopovers){
        this.stopovers = stopovers;
    }

    @Override
    public DetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stopover_view, parent, false);
        DetailViewHolder detailViewHolder = new DetailViewHolder(view);
        return  detailViewHolder;
    }

    @Override
    public void onBindViewHolder(DetailViewHolder holder, int position) {
        Stopover_item stopover = stopovers.get(position);
        holder.stopName.setText(stopover.getNameOfStop());
        holder.timeOfDeparture.setText(stopover.getTimeOfDeparture());
    }

    @Override
    public int getItemCount() {
        return stopovers.size();
    }

}