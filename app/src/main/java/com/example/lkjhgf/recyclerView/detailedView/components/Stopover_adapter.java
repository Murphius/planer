package com.example.lkjhgf.recyclerView.detailedView.components;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.Utils;

import java.util.ArrayList;

/**
 * Verbindung zwischen der Daten und der Anzeige für Zwischenhalten<br/>
 * Zwischenhalte mit Abfahrtszeit, Verspätung, Name der Haltestelle
 */
public class Stopover_adapter extends RecyclerView.Adapter<DetailViewHolder> {
    /**
     * Liste mit allen Zwischenhalten
     */
    private ArrayList<Stopover_item> stopovers;



    public Stopover_adapter(ArrayList<Stopover_item> stopovers) {
        this.stopovers = stopovers;
    }

    /**
     * Erzeugen einer neuen Ansicht
     * @return neue Layoutkopie
     */
    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stopover_view,
                parent,
                false);
        return new DetailViewHolder(view);
    }

    /**
     * Füllt die Ansicht der jeweiligen Position
     * @param holder - Layout / Ansicht
     * @param position des Listenitems, dessen Informationen für die Ansicht genutzt werden
     */
    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        Stopover_item stopover = stopovers.get(position);

        String time = stopover.getTimeOfDeparture();
        String name = stopover.getNameOfStop();
        int delay = stopover.getDelay();

        //Abfahrtszeit
        if (!time.isEmpty()) {
            holder.timeOfDeparture.setText(time);
        } else {
            /* Wenn keine Uhrzeit angegeben ist, soll das Layout trotzdem einheitlich bleiben,
             weshalb das Layout mit einer Dummyzeit gefüllt wird, und diese dem Nutzer
             nicht angezeigt wird */
            String text = "00 : 00";
            holder.timeOfDeparture.setText(text);
            holder.timeOfDeparture.setVisibility(View.INVISIBLE);
        }
        //Haltestellenname
        holder.stopName.setText(name);

        //Verspätung
        Utils.setDelayView(holder.delay, delay, holder.resources);

    }

    @Override
    public int getItemCount() {
        return stopovers.size();
    }

}