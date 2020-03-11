package com.example.lkjhgf.individual_trip.secondView_service.secondView_components;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lkjhgf.R;

import java.util.ArrayList;

public class JourneyAdapter extends RecyclerView.Adapter<JourneyAdapter.JourneyViewHolder> {

    private  ArrayList<Journey_item> journey_items;

    public static class JourneyViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView time;

        public JourneyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView1);
            time = itemView.findViewById(R.id.textView37);
        }
    }

    public JourneyAdapter(ArrayList<Journey_item> Journey_items){
        journey_items = Journey_items;
    }

    @Override
    public JourneyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.journey,parent,false);
        JourneyViewHolder journeyViewHolder = new JourneyViewHolder(view);
        return journeyViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull JourneyViewHolder holder, int position) {
        Journey_item current_item = journey_items.get(position);
        holder.imageView.setImageResource(current_item.get_image_resource());
        holder.time.setText(current_item.get_time());
    }

    @Override
    public int getItemCount() {
        return journey_items.size();
    }
}
