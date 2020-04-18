package com.example.lkjhgf.recyclerView.possibleConnections;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.Utils;
import com.example.lkjhgf.helper.UtilsString;
import com.example.lkjhgf.recyclerView.possibleConnections.components.JourneyAdapter;

/**
 *
 */
class ConnectionViewHolder extends RecyclerView.ViewHolder {

    private TextView departureView, arrivalView, durationView, numChangesView, preisstufeView, delayDepartureView, delayArrivalView;
    private RecyclerView journeyItems;
    private Resources resources;

    /**
     * Initalisierung der Attribute, OnClickListener für die gesamte Ansicht <br/>
     *
     * <p>
     * Zuordnung Attribut - ID
     * </p>
     *
     * @param itemView Layout, in dem die Textfelder und der RecyclerView sich befinden
     * @param listener dessen Methode soll bei einem Klick auf das Item ausgeführt werden
     */
    ConnectionViewHolder(View itemView, OnItemClickListener listener) {
        super(itemView);
        //Textfelder
        departureView = itemView.findViewById(R.id.textView32);
        arrivalView = itemView.findViewById(R.id.textView33);
        durationView = itemView.findViewById(R.id.textView34);
        numChangesView = itemView.findViewById(R.id.textView35);
        preisstufeView = itemView.findViewById(R.id.textView36);
        delayArrivalView = itemView.findViewById(R.id.textView73);
        delayDepartureView = itemView.findViewById(R.id.textView72);
        journeyItems = itemView.findViewById(R.id.recyclerView2);
        journeyItems.setHasFixedSize(true);
        this.resources = itemView.getResources();

        //Klick auf das ganze Element (egal wo geklickt wird)
        itemView.setOnClickListener(v -> {
            if (listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position);
                }
            }
        });
    }

    /**
     * Füllt die Ansicht mit den Werten des Parameters
     *
     * @param item enthält die Informationen, welche in der Ansicht angezeigt werden sollen
     */
    void fillView(ConnectionItem item) {
        // Textfelder füllen
        arrivalView.setText(item.getTimeOfArrival());
        departureView.setText(item.getTimeOfDeparture());
        durationView.setText(item.getDurationString());
        numChangesView.setText(UtilsString.setNumChanges(item.getNumChanges()));
        preisstufeView.setText(item.getPreisstufe());

        //TODO -> auskommentiertes entfernen ?
        //int delayArrival = item.getDelayArrival();
        //int delayDeparture = item.getDelayDeparture();

        //if(delayArrival != 0){
        Utils.setDelayView(delayArrivalView, item.getDelayArrival(), resources);
        //}
        //if(delayDeparture != 0){
        Utils.setDelayView(delayDepartureView, item.getDelayDeparture(), resources);
        //}

        //Erzeugen / Füllen des recyclerViews mit der kompakten Ansicht des zweiten
        RecyclerView.Adapter journeyAdapter = new JourneyAdapter(item.getJourneyItems());
        Context context = itemView.getContext();

        //Die Anzeige der Abfolge der einzelnen Verkehrsmittel soll horizontal erfolgen und nicht
        // vertikal
        RecyclerView.LayoutManager journeyLayout = new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL,
                false);

        journeyItems.setLayoutManager(journeyLayout);
        journeyItems.setAdapter(journeyAdapter);
    }
}
