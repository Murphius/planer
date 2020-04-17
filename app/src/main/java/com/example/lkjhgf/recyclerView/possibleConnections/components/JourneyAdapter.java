package com.example.lkjhgf.recyclerView.possibleConnections.components;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lkjhgf.R;

import java.util.ArrayList;

public class JourneyAdapter extends RecyclerView.Adapter<JourneyViewHolder> {

    private  ArrayList<JourneyItem> journeyItems;

    public JourneyAdapter(ArrayList<JourneyItem> journeyItems){
        this.journeyItems = journeyItems;
    }

    @Override
    @NonNull
    public JourneyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.journey,parent,false);
        return new JourneyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JourneyViewHolder holder, int position) {
        JourneyItem currentItem = journeyItems.get(position);
        holder.fillView(currentItem);
    }

    @Override
    public int getItemCount() {
        return journeyItems.size();
    }
}
