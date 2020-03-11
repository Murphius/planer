package com.example.lkjhgf.individual_trip.thirdView_DetailedView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.lkjhgf.Color.ButtonBootstrapBrandVisible;
import com.example.lkjhgf.individual_trip.thirdView_DetailedView.components.Stopover_adapter;
import com.example.lkjhgf.R;

import java.util.ArrayList;


public class CloseUp_adapter extends RecyclerView.Adapter<CloseUp_adapter.Detailed_connection_viewHolder> {

    private ArrayList<CloseUp_item> closeUp_items;
    private OnItemClickListener listener;

    private  RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public interface OnItemClickListener {
        void onShowDetails(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class Detailed_connection_viewHolder extends RecyclerView.ViewHolder {

        public TextView time_of_departure_view, start_view, start_platform_view, number_view, destination_of_number_view, time_of_arrival_view, destination_view, destination_platform_view;
        public ImageView icon;
        public BootstrapButton stopoverLocationsShow;
        public RecyclerView recyclerView;

        public Detailed_connection_viewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            stopoverLocationsShow = itemView.findViewById(R.id.BootstrapButton29);
            stopoverLocationsShow.setBootstrapBrand(new ButtonBootstrapBrandVisible());
            stopoverLocationsShow.setBootstrapSize(1.5f);

            time_of_departure_view = itemView.findViewById(R.id.textView5);
            start_view = itemView.findViewById(R.id.textView57);
            start_platform_view = itemView.findViewById(R.id.textView58);
            number_view = itemView.findViewById(R.id.textView61);
            destination_of_number_view = itemView.findViewById(R.id.textView62);
            time_of_arrival_view = itemView.findViewById(R.id.textView60);
            destination_view = itemView.findViewById(R.id.textView63);
            destination_platform_view = itemView.findViewById(R.id.textView59);

            recyclerView = itemView.findViewById(R.id.recyclerView4);
            recyclerView.setHasFixedSize(true);

            icon = itemView.findViewById(R.id.imageView);

            stopoverLocationsShow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onShowDetails(position);
                        }
                    }
                }
            });
        }

        public Detailed_connection_viewHolder get(){
            return this;
        }

    }

    public CloseUp_adapter(ArrayList<CloseUp_item> items) {
        closeUp_items = items;
    }

    public Detailed_connection_viewHolder getDetailedConnectionViewHolder(){
        return  getDetailedConnectionViewHolder();
    }

    @Override
    public Detailed_connection_viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detailed_journey,
                parent,
                false);
        Detailed_connection_viewHolder detailed_connection_viewHolder = new Detailed_connection_viewHolder(
                view, listener);
        return detailed_connection_viewHolder;
    }

    @Override
    public void onBindViewHolder(Detailed_connection_viewHolder holder, int position) {
        CloseUp_item current = closeUp_items.get(position);
        holder.icon.setImageResource(current.getImage_resource());
        holder.destination_view.setText(current.getDestination());
        holder.time_of_arrival_view.setText(current.getTime_of_arrival());
        holder.destination_of_number_view.setText(current.getDestination_of_number());
        holder.number_view.setText(current.getNumber());
        holder.start_view.setText(current.getStart());
        holder.time_of_departure_view.setText(current.getTime_of_departure());
        holder.destination_platform_view.setText(current.getDestination_platform());
        holder.start_platform_view.setText(current.getStart_platform());
        adapter = new Stopover_adapter(current.getStopovers());
        layoutManager = new LinearLayoutManager(holder.itemView.getContext());

        holder.recyclerView.setAdapter(adapter);
        holder.recyclerView.setLayoutManager(layoutManager);
        if(current.getShowDetails()){
            holder.recyclerView.setVisibility(View.VISIBLE);
        }else{
            holder.recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return closeUp_items.size();
    }

}
