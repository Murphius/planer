package com.example.lkjhgf;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.lkjhgf.Color.ButtonBootstrapBrandVisible;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class Detailed_connection_adapter extends RecyclerView.Adapter<Detailed_connection_adapter.Detailed_connection_viewHolder> {

    private ArrayList<Detailed_connection_item> detailed_connection_items;

    public static class Detailed_connection_viewHolder extends RecyclerView.ViewHolder {

        public TextView time_of_departure_view, start_view, start_platform_view, number_view, destination_of_number_view, time_of_arrival_view, destination_view, destination_platform_view;
        public ImageView icon;

        public Detailed_connection_viewHolder(View itemView) {
            super(itemView);

            BootstrapButton show_stopover = itemView.findViewById(R.id.BootstrapButton29);
            show_stopover.setBootstrapBrand(new ButtonBootstrapBrandVisible());
            show_stopover.setBootstrapSize(1.5f);

            time_of_departure_view = itemView.findViewById(R.id.textView5);
            start_view = itemView.findViewById(R.id.textView57);
            start_platform_view = itemView.findViewById(R.id.textView58);
            number_view = itemView.findViewById(R.id.textView61);
            destination_of_number_view = itemView.findViewById(R.id.textView62);
            time_of_arrival_view = itemView.findViewById(R.id.textView60);
            destination_view = itemView.findViewById(R.id.textView63);
            destination_platform_view = itemView.findViewById(R.id.textView59);

            icon = itemView.findViewById(R.id.imageView);
        }
    }

      public Detailed_connection_adapter(ArrayList<Detailed_connection_item> items){
            detailed_connection_items = items;
      }

        @Override
        public Detailed_connection_viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detailed_journey, parent, false);
            Detailed_connection_viewHolder detailed_connection_viewHolder = new Detailed_connection_viewHolder(view);
            return detailed_connection_viewHolder;
        }

        @Override
        public void onBindViewHolder(Detailed_connection_viewHolder holder, int position) {
            Detailed_connection_item current = detailed_connection_items.get(position);
            holder.icon.setImageResource(current.getImage_resource());
            holder.destination_view.setText(current.getDestination());
            holder.time_of_arrival_view.setText(current.getTime_of_arrival());
            holder.destination_of_number_view.setText(current.getDestination_of_number());
            holder.number_view.setText(current.getNumber());
            holder.start_view.setText(current.getStart());
            holder.time_of_departure_view.setText(current.getTime_of_departure());
            holder.destination_platform_view.setText(current.getDestination_platform());
            holder.start_platform_view.setText(current.getStart_platform());
        }

        @Override
        public int getItemCount() {
            return  detailed_connection_items.size();
        }
}
