package com.example.lkjhgf.recyclerView.tickets;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lkjhgf.R;
import com.example.lkjhgf.recyclerView.futureTrips.TripAdapter;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;
import com.example.lkjhgf.recyclerView.futureTrips.util.FutureTripViewHolder;

import java.util.ArrayList;
import java.util.Calendar;

public class TripAdapterTicketList extends TripAdapter {
    public TripAdapterTicketList(ArrayList<TripItem> tripItems, Activity activity) {
        super(tripItems, activity);
    }

    @Override
    public void onBindViewHolder(@NonNull FutureTripViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.buttonClass.hideButtons();

        holder.separatorLine.setVisibility(View.GONE);

        if (tripItems.get(position).getTrip().getLastArrivalTime().before(Calendar.getInstance().getTime())) {
            holder.itemView.setBackgroundColor(activity.getResources().getColor(R.color.un_graceful, null));
        }
    }
}
