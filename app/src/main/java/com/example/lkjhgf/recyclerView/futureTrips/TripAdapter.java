package com.example.lkjhgf.recyclerView.futureTrips;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lkjhgf.R;
import com.example.lkjhgf.recyclerView.futureTrips.util.FutureTripViewHolder;
import com.example.lkjhgf.trip.secondView_service.secondView_components.JourneyAdapter;

import java.util.ArrayList;

/**
 * Adapter zwischen Verbindungsdaten und Ansicht
 */
public class TripAdapter extends RecyclerView.Adapter<FutureTripViewHolder> {

    /**
     * Liste aller gespeicherten Trips, welche angezeigt werden sollen
     */
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

    /**
     * Erzeugt neuen View
     * @param parent
     * @param viewType
     * @return neue Ansicht
     */
    @NonNull
    @Override
    public FutureTripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.future_item, parent, false);
        return new FutureTripViewHolder(v, onItemClickListener);
    }

    /**
     * Ein einzelnes Layoutfragment wird mit den Informationen des an der jeweiligen Position gespeicherten
     * Elements gefüllt
     *
     * @param holder Layout, welches gefüllt werden soll
     * @param position gibt die Position des aktuellen Items in der Liste an, für das der ViewHolder
     *                 gefüllt werden soll
     *
     * Verbindung zwischen Ansicht und Daten
     */
    @Override
    public void onBindViewHolder(@NonNull FutureTripViewHolder holder, int position) {
        TripItem currentItem = tripItems.get(position);
        // einzelne Fahrt
        if(currentItem.isComplete()){
            // zur Unterscheidung wird die Farbe anders gesetzt
            holder.view.setBackgroundColor(activity.getResources().getColor(R.color.mr_and_mrs_jones_25,null));
            holder.recyclerView.setBackgroundColor(activity.getResources().getColor(R.color.mr_and_mrs_jones_25, null));
            // diese Fahrten haben keine Personenanzahl
            holder.textViewClass.numAdult.setVisibility(View.GONE);
            holder.textViewClass.userNumAdult.setVisibility(View.GONE);
            holder.textViewClass.numChildren.setVisibility(View.GONE);
            holder.textViewClass.userNumChildren.setVisibility(View.GONE);
            holder.textViewClass.line.setVisibility(View.GONE);
        }else{
            // Fahrten, die optimiert werden sollen, haben die Anzahl an Personen hinterlegt
            String text = currentItem.getNumAdult() + "";
            holder.textViewClass.userNumAdult.setText(text);
            text = currentItem.getNumChildren() + "";
            holder.textViewClass.userNumChildren.setText(text);
        }
        //TODO entfernen ??
        //holder.setTrip(currentItem.getTrip());
        // Recycler View für die Abfolge von Verkehrsmitteln
        RecyclerView.Adapter journey_adapter = new JourneyAdapter(currentItem.getJourneyItems());
        Context context = holder.itemView.getContext();
        RecyclerView.LayoutManager journey_layout = new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL,
                false);
        holder.recyclerView.setLayoutManager(journey_layout);
        holder.recyclerView.setAdapter(journey_adapter);

        holder.textViewClass.fillTextView(currentItem.getTrip(), activity.getResources());
    }

    @Override
    public int getItemCount() {
        return tripItems.size();
    }
}