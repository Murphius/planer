package com.example.lkjhgf.recyclerView.detailedView.components;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.Utils;

import java.util.ArrayList;

public class Stopover_adapter extends RecyclerView.Adapter<Stopover_adapter.DetailViewHolder> {
    private ArrayList<Stopover_item> stopovers;

    static class DetailViewHolder extends RecyclerView.ViewHolder {
        TextView timeOfDeparture;
        TextView delay;
        TextView stopName;
        Resources resources;

        DetailViewHolder(View itemView) {
            super(itemView);
            timeOfDeparture = itemView.findViewById(R.id.textView65);
            stopName = itemView.findViewById(R.id.textView66);
            delay = itemView.findViewById(R.id.textView67);
            resources = itemView.getResources();
        }
    }

    public Stopover_adapter(ArrayList<Stopover_item> stopovers) {
        this.stopovers = stopovers;
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stopover_view,
                parent,
                false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        Stopover_item stopover = stopovers.get(position);

        String time = stopover.getTimeOfDeparture();
        String name = stopover.getNameOfStop();
        int delay = stopover.getDelay();

        if (!time.isEmpty()) {
            System.out.println("stimmt -----------------------------------------------");
            holder.timeOfDeparture.setText(time);
        } else {
            System.out.println("Sollte nicht passieren ------------------------------------------");
            String text = "00 : 00";
            holder.timeOfDeparture.setText(text);
            holder.timeOfDeparture.setVisibility(View.INVISIBLE);
        }
        holder.stopName.setText(name);

        Utils.setDelayView(holder.delay, delay, holder.resources);

    }

    @Override
    public int getItemCount() {
        return stopovers.size();
    }

}