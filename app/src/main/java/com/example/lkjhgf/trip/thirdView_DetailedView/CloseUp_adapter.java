package com.example.lkjhgf.trip.thirdView_DetailedView;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.font.Typicon;
import com.example.lkjhgf.Color.ButtonBootstrapBrandVisible;
import com.example.lkjhgf.helper.Utils;
import com.example.lkjhgf.trip.thirdView_DetailedView.components.Stopover_adapter;
import com.example.lkjhgf.R;
import com.example.lkjhgf.trip.thirdView_DetailedView.components.Stopover_item;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Locale;

public class CloseUp_adapter extends RecyclerView.Adapter<CloseUp_adapter.Detailed_connection_viewHolder> {

    private ArrayList<CloseUp_item> closeUp_items;
    private OnItemClickListener listener;

    private Context applicationContext;
    private Activity activity;

    private  RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public interface OnItemClickListener {
        void onShowDetails(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class Detailed_connection_viewHolder extends RecyclerView.ViewHolder {

        public TextView time_of_departure_view, start_view, start_platform_view;
        public TextView number_view, destination_of_number_view;
        public TextView time_of_arrival_view, destination_view, destination_platform_view;
        public TextView delay_departure, delay_arrival;
        public TextView ticketView, useTicket;
        public ImageView icon;
        public int myGreen, myRed;
        public BootstrapButton stopoverLocationsShow;
        public BootstrapButton showOnMap;
        public RecyclerView recyclerView;
        public View view;

        public Detailed_connection_viewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            stopoverLocationsShow = itemView.findViewById(R.id.BootstrapButton29);
            stopoverLocationsShow.setBootstrapSize(1.5f);
            stopoverLocationsShow.setBootstrapBrand(new ButtonBootstrapBrandVisible());

            showOnMap = itemView.findViewById(R.id.BootstrapButton36);
            showOnMap.setBootstrapBrand(new ButtonBootstrapBrandVisible());

            time_of_departure_view = itemView.findViewById(R.id.textView5);
            start_view = itemView.findViewById(R.id.textView57);
            start_platform_view = itemView.findViewById(R.id.textView58);
            number_view = itemView.findViewById(R.id.textView61);
            destination_of_number_view = itemView.findViewById(R.id.textView62);
            time_of_arrival_view = itemView.findViewById(R.id.textView60);
            destination_view = itemView.findViewById(R.id.textView63);
            destination_platform_view = itemView.findViewById(R.id.textView59);

            delay_departure = itemView.findViewById(R.id.textView68);
            delay_arrival = itemView.findViewById(R.id.textView69);

            ticketView = itemView.findViewById(R.id.textView89);
            useTicket = itemView.findViewById(R.id.textView90);

            recyclerView = itemView.findViewById(R.id.recyclerView4);
            recyclerView.setHasFixedSize(true);

            icon = itemView.findViewById(R.id.imageView);

            view = itemView.findViewById(R.id.view4);



            showOnMap.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onShowDetails(position);
                        }
                    }
                }
            });


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

            myGreen = itemView.getResources().getColor(R.color.my_green, null);
            myRed = itemView.getResources().getColor(R.color.maroon, null);
        }

    }

    public CloseUp_adapter(ArrayList<CloseUp_item> items, Activity activity) {
        closeUp_items = items;
        this.activity = activity;
        applicationContext = activity.getApplicationContext();
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

        holder.start_view.setText(current.getDeparture());
        holder.destination_view.setText(current.getDestination());

        holder.icon.setImageResource(current.getImage_resource());

        holder.time_of_arrival_view.setText(current.getTime_of_arrival());
        holder.time_of_departure_view.setText(current.getTime_of_departure());

        if(current.getTicket() == null){
            //TODO
            //holder.useTicket.setVisibility(View.GONE);
            //holder.ticketView.setVisibility(View.GONE);
        }else{
            //TODO
            //holder.useTicket.setText(current.getTicket.toString());
        }

        if(current instanceof CloseUp_publicItem){
            holder.showOnMap.setVisibility(View.GONE);

            CloseUp_publicItem closeUpPublicItem = (CloseUp_publicItem) current;

            holder.start_platform_view.setText(closeUpPublicItem.getDeparturePlatform());
            holder.destination_platform_view.setText(closeUpPublicItem.getDestinationPlatform());

            holder.destination_of_number_view.setText(closeUpPublicItem.getDestination_of_number());
            holder.number_view.setText(closeUpPublicItem.getNumber());

            Utils.setDelayView(holder.delay_departure, closeUpPublicItem.getDelayDeparture(), activity.getResources());
            Utils.setDelayView(holder.delay_arrival, closeUpPublicItem.getDelayArrival(), activity.getResources());

            if(current.isShowDetails()){
                holder.recyclerView.setVisibility(View.VISIBLE);
                holder.stopoverLocationsShow.setTypicon(Typicon.TY_ARROW_UP);
                holder.view.setVisibility(View.VISIBLE);
            }else{
                holder.recyclerView.setVisibility(View.GONE);
                holder.stopoverLocationsShow.setTypicon(Typicon.TY_ARROW_DOWN);
                holder.view.setVisibility(View.GONE);
            }
            adapter = new Stopover_adapter(closeUpPublicItem.getStopoverItems());
            if(closeUpPublicItem.getStopoverItems().isEmpty()){
                holder.stopoverLocationsShow.setVisibility(View.INVISIBLE);
            }
        }else{// instance of IndividualItem
            holder.stopoverLocationsShow.setVisibility(View.INVISIBLE);
            holder.destination_of_number_view.setVisibility(View.GONE);
            holder.number_view.setVisibility(View.GONE);
            holder.recyclerView.setVisibility(View.GONE);
            adapter = new Stopover_adapter(new ArrayList<Stopover_item>());

            if(current.isShowDetails()){
                CloseUp_privateItem privateItem = (CloseUp_privateItem) current;
                LatLng departure = privateItem.getDepartureLocation();
                LatLng destination = privateItem.getDestinationLocation();
                String uri = String.format(Locale.ENGLISH,
                        "http://maps.google.com/maps?saddr=%f,%f&daddr=%f,%f",
                        departure.latitude,
                        departure.longitude,
                        destination.latitude,
                        destination.longitude);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                activity.startActivity(intent);
                try {
                    activity.startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    try {
                        Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        activity.startActivity(unrestrictedIntent);
                    } catch (ActivityNotFoundException innerEx) {
                        Toast.makeText(activity.getApplicationContext(),
                                "Please install a maps application",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        }

        layoutManager = new LinearLayoutManager(holder.itemView.getContext());

        holder.recyclerView.setAdapter(adapter);
        holder.recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public int getItemCount() {
        return closeUp_items.size();
    }

}
