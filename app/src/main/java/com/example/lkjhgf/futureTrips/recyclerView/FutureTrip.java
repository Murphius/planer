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
    public FutureTrip(@NonNull View itemView, OnItemClickListener onItemClickListener) {
        super(itemView);
        textViewClass = new TextViewClass(itemView);
        buttonClass = new ButtonClass(itemView, this);
        view = itemView.findViewById(R.id.CardView1);
        recyclerView = itemView.findViewById(R.id.recyclerView6);
        recyclerView.setHasFixedSize(true);

        buttonClass.setOnClickListener(onItemClickListener);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        onItemClickListener.onItemClick(position);
                    }
                }
            }
        });

    }

    public void setTrip(Trip trip){
        this.trip = trip;
    }
}
