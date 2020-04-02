package com.example.lkjhgf.futureTrips.recyclerView;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.lkjhgf.R;

import de.schildbach.pte.dto.Trip;

public class FutureTrip extends RecyclerView.ViewHolder{
    public TextViewClass textViewClass;
    public ButtonClass buttonClass;
    public RecyclerView recyclerView;
    View view;
    Trip trip;
    public FutureTrip(@NonNull View itemView) {
        super(itemView);
        textViewClass = new TextViewClass(itemView);
        buttonClass = new ButtonClass(itemView);
        view = itemView.findViewById(R.id.CardView1);
        recyclerView = itemView.findViewById(R.id.recyclerView6);
        recyclerView.setHasFixedSize(true);
    }

    public void setTrip(Trip trip){
        this.trip = trip;
    }
}
