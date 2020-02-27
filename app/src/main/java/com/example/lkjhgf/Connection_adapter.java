package com.example.lkjhgf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Connection_adapter extends RecyclerView.Adapter<Connection_adapter.ConnectionViewHolder> {

    private ArrayList<Connection_item> connection_list;
    private RecyclerView.LayoutManager journey_layout;
    private RecyclerView.Adapter journey_adapter;

    public static class ConnectionViewHolder extends RecyclerView.ViewHolder{
        public TextView departure_view, arrival_view, duration_view, num_changes_view, preisstufe_view;
        public RecyclerView journey_items;


        public ConnectionViewHolder(View itemView) {
            super(itemView);
            departure_view = itemView.findViewById(R.id.TextView32);
            arrival_view = itemView.findViewById(R.id.TextView33);
            duration_view = itemView.findViewById(R.id.TextView34);
            num_changes_view = itemView.findViewById(R.id.TextView35);
            preisstufe_view = itemView.findViewById(R.id.TextView36);
            journey_items = itemView.findViewById(R.id.recyclerView2);
            journey_items.setHasFixedSize(true);
        }
    }

    public Connection_adapter(ArrayList<Connection_item> ConnectionList){
        connection_list = ConnectionList;
    }

    @Override
    public ConnectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.connections, parent, false);
        ConnectionViewHolder connectionViewHolder = new ConnectionViewHolder(view);
        return connectionViewHolder;
    }

    @Override
    public void onBindViewHolder(ConnectionViewHolder holder, int position) {
        Connection_item current_connection = connection_list.get(position);
        holder.arrival_view.setText(current_connection.get_time_of_arrival());
        holder.departure_view.setText(current_connection.get_time_of_departure());
        holder.duration_view.setText("" + current_connection.get_duration());
        holder.num_changes_view.setText("" + current_connection.get_num_changes());
        holder.preisstufe_view.setText(current_connection.get_preisstufe());
        journey_adapter = new JourneyAdapter(current_connection.get_journey_items());
        Context context = holder.itemView.getContext();
        journey_layout = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        holder.journey_items.setLayoutManager(journey_layout);
        holder.journey_items.setAdapter(journey_adapter);
    }

    @Override
    public int getItemCount() {
        return connection_list.size();
    }


}
