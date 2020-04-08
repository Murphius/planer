package com.example.lkjhgf.futureTrips.recyclerView;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lkjhgf.R;
import com.example.lkjhgf.trip.secondView_service.secondView_components.JourneyAdapter;

import java.util.ArrayList;

public class TripAdapter extends RecyclerView.Adapter<FutureTrip> {


    private ArrayList<TripItem> tripItems;
    private OnItemClickListener onItemClickListener;
    private Activity activity;


    public TripAdapter(ArrayList<TripItem> tripItems, Activity activity){
        this.activity = activity;
        this.tripItems = tripItems;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public FutureTrip onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.future_item, parent, false);
        return new FutureTrip(v, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FutureTrip holder, int position) {
        TripItem currentItem = tripItems.get(position);
        if(currentItem.isComplete()){
            holder.view.setBackgroundColor(activity.getResources().getColor(R.color.mr_and_mrs_jones_25,null));
            holder.recyclerView.setBackgroundColor(activity.getResources().getColor(R.color.mr_and_mrs_jones_25, null));
            holder.textViewClass.numAdult.setVisibility(View.GONE);
            holder.textViewClass.userNumAdult.setVisibility(View.GONE);
            holder.textViewClass.numChildren.setVisibility(View.GONE);
            holder.textViewClass.userNumChildren.setVisibility(View.GONE);
            holder.textViewClass.line.setVisibility(View.GONE);
        }else{
            String text = currentItem.getNumAdult() + "";
            holder.textViewClass.userNumAdult.setText(text);
            text = currentItem.getNumChildren() + "";
            holder.textViewClass.userNumChildren.setText(text);
        }
        holder.setTrip(currentItem.getTrip());
        RecyclerView.Adapter journey_adapter = new JourneyAdapter(currentItem.getJourney_items());
        Context context = holder.itemView.getContext();
        RecyclerView.LayoutManager journey_layout = new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL,
                false);
        holder.recyclerView.setLayoutManager(journey_layout);
        holder.recyclerView.setAdapter(journey_adapter);

        holder.textViewClass.fillTextView(currentItem.getTrip(), activity.getResources());
        //TODO Buttons mit Funktion
    }

    @Override
    public int getItemCount() {
        return tripItems.size();
    }
}
