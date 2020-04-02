package com.example.lkjhgf.trip.secondView_service;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lkjhgf.helper.Utils;
import com.example.lkjhgf.trip.secondView_service.secondView_components.JourneyAdapter;
import com.example.lkjhgf.R;

import java.util.ArrayList;

public class Connection_adapter extends RecyclerView.Adapter<Connection_adapter.ConnectionViewHolder> {

    private ArrayList<Connection_item> connection_list;
    private On_item_click_listener on_item_click_listener;

    public interface On_item_click_listener {
        void onItemClick(int position);
    }

    public void set_on_item_click_listener(On_item_click_listener listener){
        on_item_click_listener = listener;
    }

    static class ConnectionViewHolder extends RecyclerView.ViewHolder{
        TextView departure_view, arrival_view, duration_view, num_changes_view, preisstufe_view, delayDeparture_view, delayArrival_view;
        RecyclerView journey_items;
        Resources resources;

        ConnectionViewHolder(View itemView, final On_item_click_listener listener) {
            super(itemView);
            departure_view = itemView.findViewById(R.id.textView32);
            arrival_view = itemView.findViewById(R.id.textView33);
            duration_view = itemView.findViewById(R.id.textView34);
            num_changes_view = itemView.findViewById(R.id.textView35);
            preisstufe_view = itemView.findViewById(R.id.textView36);
            delayArrival_view = itemView.findViewById(R.id.textView73);
            delayDeparture_view = itemView.findViewById(R.id.textView72);
            journey_items = itemView.findViewById(R.id.recyclerView2);
            journey_items.setHasFixedSize(true);
            this.resources = itemView.getResources();

            itemView.setOnClickListener(v -> {
                if(listener != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

    public Connection_adapter(ArrayList<Connection_item> ConnectionList){
        connection_list = ConnectionList;
    }

    @Override
    @NonNull
    public ConnectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.connections, parent, false);
        return new ConnectionViewHolder(view, on_item_click_listener);
    }

    @Override
    public void onBindViewHolder(ConnectionViewHolder holder, int position) {
        Connection_item current_connection = connection_list.get(position);
        holder.arrival_view.setText(current_connection.get_time_of_arrival());
        holder.departure_view.setText(current_connection.get_time_of_departure());
        holder.duration_view.setText(current_connection.get_duration_string());
        String text = current_connection.get_num_changes() + "x";
        holder.num_changes_view.setText(text);
        holder.preisstufe_view.setText(current_connection.get_preisstufe());

        int delayArrival = current_connection.getDelayArrival();
        int delayDeparture = current_connection.getDelayDeparture();

        if(delayArrival != 0){
            Utils.setDelayView(holder.delayArrival_view, delayArrival, holder.resources);
        }
        if(delayDeparture != 0){
            Utils.setDelayView(holder.delayDeparture_view, delayDeparture, holder.resources);
        }

        RecyclerView.Adapter journey_adapter = new JourneyAdapter(current_connection.get_journey_items());
        Context context = holder.itemView.getContext();
        RecyclerView.LayoutManager journey_layout = new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL,
                false);

        holder.journey_items.setLayoutManager(journey_layout);
        holder.journey_items.setAdapter(journey_adapter);
    }

    @Override
    public int getItemCount() {
        return connection_list.size();
    }

}