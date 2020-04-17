package com.example.lkjhgf.recyclerView.detailedView.components;

import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lkjhgf.R;

/**
 * Ansicht eines Zwischenhaltes <br/>
 *
 * Aufgrund der wenigen Parameter, wurde keine extra Klasse für die Textfelder erstellt
 */
class DetailViewHolder extends RecyclerView.ViewHolder {

   TextView timeOfDeparture;
   TextView delay;
   TextView stopName;
   Resources resources;

    /**
     * Initalisierung der Attribute <br/>
     *
     * Zuordnung Attribut - ID
     * @param itemView - Layout, in welchem nach den IDs gesucht werden soll
     */
    DetailViewHolder(View itemView) {
        super(itemView);
        timeOfDeparture = itemView.findViewById(R.id.textView65);
        stopName = itemView.findViewById(R.id.textView66);
        delay = itemView.findViewById(R.id.textView67);
        resources = itemView.getResources();
    }
}
