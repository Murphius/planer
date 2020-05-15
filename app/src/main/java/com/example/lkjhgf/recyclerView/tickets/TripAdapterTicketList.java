package com.example.lkjhgf.recyclerView.tickets;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.lkjhgf.R;
import com.example.lkjhgf.recyclerView.futureTrips.TripAdapter;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;
import com.example.lkjhgf.recyclerView.futureTrips.util.FutureTripViewHolder;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Füllen des RecyclerViews für die Fahrten, die zu einem Ticket gehören
 */
public class TripAdapterTicketList extends TripAdapter {
    TripAdapterTicketList(ArrayList<TripItem> tripItems, Activity activity) {
        super(tripItems, activity);
    }

    /**
     * @see TripAdapter#onBindViewHolder(FutureTripViewHolder holder, int position)
     * Die Buttons editieren, löschen,... sind nicht sichtbar;
     * Fahrten, die in der Vergangenheit sind, werden eingefärbt
     */
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
