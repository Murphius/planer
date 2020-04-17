package com.example.lkjhgf.recyclerView.possibleConnections.components;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lkjhgf.R;

import java.util.ArrayList;

/**
 * Adapter für das Anzeigen eines Fahrtabschnitts mit Icon und Linie / Zeit
 */
public class JourneyAdapter extends RecyclerView.Adapter<JourneyViewHolder> {

    /**
     * Liste mit allen Fahrtabschnitten
     */
    private ArrayList<JourneyItem> journeyItems;

    public JourneyAdapter(ArrayList<JourneyItem> journeyItems) {
        this.journeyItems = journeyItems;
    }

    /**
     * Erzeugt eine neue Ansicht
     *
     * @return weiteres "Exemplar" der Ansicht
     */
    @Override
    @NonNull
    public JourneyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.journey, parent, false);
        return new JourneyViewHolder(view);
    }

    /**
     * Füllt das Layout mit den Informationen des Elements an der entsprechenden Position <br/>
     * <p>
     * Füllen der Ansicht erfolgt in {@link JourneyViewHolder#fillView(JourneyItem)}
     *
     * @param holder   gibt an, welches Layout gefüllt werden soll
     * @param position gibt an, welches Element genutzt werden soll, um die Ansicht zu füllen
     */
    @Override
    public void onBindViewHolder(@NonNull JourneyViewHolder holder, int position) {
        JourneyItem currentItem = journeyItems.get(position);
        holder.fillView(currentItem);
    }

    @Override
    public int getItemCount() {
        return journeyItems.size();
    }
}
