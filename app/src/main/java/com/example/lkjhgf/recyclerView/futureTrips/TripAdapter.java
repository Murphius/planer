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
import com.example.lkjhgf.recyclerView.possibleConnections.components.JourneyAdapter;

import java.util.ArrayList;
import java.util.Calendar;

import de.schildbach.pte.dto.Fare;

/**
 * Adapter zwischen Verbindungsdaten und Ansicht
 */
public class TripAdapter extends RecyclerView.Adapter<FutureTripViewHolder> {

    /**
     * Liste aller gespeicherten Trips, welche angezeigt werden sollen
     */
    protected ArrayList<TripItem> tripItems;
    private OnItemClickListener onItemClickListener;
    protected Activity activity;


    public TripAdapter(ArrayList<TripItem> tripItems, Activity activity){
        this.activity = activity;
        this.tripItems = tripItems;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * Erzeugt neuen View
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

        // Recycler View für die Abfolge von Verkehrsmitteln
        JourneyAdapter journey_adapter = new JourneyAdapter(currentItem.getJourneyItems());
        Context context = holder.itemView.getContext();
        RecyclerView.LayoutManager journey_layout = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.recyclerView.setLayoutManager(journey_layout);
        holder.recyclerView.setAdapter(journey_adapter);
        if(currentItem.getFirstDepartureTime().before(Calendar.getInstance().getTime())){
            holder.buttonClass.hideButtons();
        }
        // einzelne Fahrt
        if(currentItem.isComplete()){
            // zur Unterscheidung wird die Farbe anders gesetzt
            holder.view.setBackgroundColor(activity.getResources().getColor(R.color.mr_and_mrs_jones_25,null));
            // diese Fahrten haben keine Personenanzahl
            hidePersonView(holder);
        }else{
            if(currentItem.getLastArrivalTime().before(Calendar.getInstance().getTime())){
                holder.view.setBackgroundColor(activity.getResources().getColor(R.color.un_graceful, null));
            }
            setNumPersonView(holder, currentItem);
            //Ticket anzeigen, wenn kein Ticket vorhanden -> keine Anzeige
            if(!currentItem.hasNoTicket()){
                holder.textViewClass.ticketInformationHolder.setText(currentItem.getTicketListAsString());
            }else{
                holder.textViewClass.ticketInformationHolder.setVisibility(View.GONE);
                holder.textViewClass.ticketInformationView.setVisibility(View.GONE);
            }
        }
        holder.textViewClass.fillTextView(currentItem.getTrip(), activity.getResources());
    }

    /**
     * Füllt die Felder für die Personenanzahl mit den entsprechenden Zahlen <br/>
     *
     * Muss je nach dem, welche Altersstrukturen der Provider anbietet, bearbeitet werden
     * @param holder Ansicht die gefüllt werden soll
     * @param currentItem Fahrt die angezeigt werden soll
     */
    private void setNumPersonView(@NonNull FutureTripViewHolder holder, TripItem currentItem){
        //TODO Erweiterung für weitere Nutzerklassen
        // Fahrten, die optimiert werden sollen, haben die Anzahl an Personen hinterlegt
        String text = currentItem.getNumUserClass(Fare.Type.ADULT) + "";
        holder.textViewClass.userNumAdult.setText(text);
        text = currentItem.getNumUserClass(Fare.Type.CHILD) + "";
        holder.textViewClass.userNumChildren.setText(text);
    }

    /**
     * Verbirgt die Felder für die Anzahl reisender Personen
     * @param holder zu bearbeitende Ansicht
     */
    private void hidePersonView(@NonNull FutureTripViewHolder holder){
        holder.textViewClass.numAdult.setVisibility(View.GONE);
        holder.textViewClass.userNumAdult.setVisibility(View.GONE);
        holder.textViewClass.numChildren.setVisibility(View.GONE);
        holder.textViewClass.userNumChildren.setVisibility(View.GONE);
        holder.textViewClass.line.setVisibility(View.GONE);
        holder.textViewClass.ticketInformationView.setVisibility(View.GONE);
        holder.textViewClass.ticketInformationHolder.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return tripItems.size();
    }
}
