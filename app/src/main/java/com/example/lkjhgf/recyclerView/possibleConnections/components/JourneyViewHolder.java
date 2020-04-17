package com.example.lkjhgf.recyclerView.possibleConnections.components;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lkjhgf.R;

/**
 * Enthält die Komponenten der kompakten Ansicht eines Verbindungsabschnitts <br/>
 *
 * <p>
 *     Da die Ansicht sehr kompakt ist, keine zusätzlichen Klassen für die Attribute
 * </p>
 */
class JourneyViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;
    private TextView timeOrLine;

    /**
     * Initalisierung der Attribute <br/>
     * Zuordnung Attribut - ID
     * @param itemView in welchem Layout nach den IDs gesucht werden soll
     */
    JourneyViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageView1);
        timeOrLine = itemView.findViewById(R.id.textView37);
    }

    /**
     * Füllt die Ansicht mit den Attributen des übergebenen Elements <br/>
     *
     * @param journeyItem enthält die Informationen, die in der Ansicht angezeigt werden sollen
     */
    void fillView(JourneyItem journeyItem){
        imageView.setImageResource(journeyItem.getImageResource());
        timeOrLine.setText(journeyItem.getTimeOrLine());
    }
}
