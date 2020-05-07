package com.example.lkjhgf.recyclerView.futureTrips.util;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lkjhgf.R;
import com.example.lkjhgf.recyclerView.futureTrips.OnItemClickListener;

/**
 * Der Aufbau der Ansicht für eine geplante Fahrt <br/>
 * <p>
 * Textfelder in die Klasse {@link TextViewClass} verschoben, <br/>
 * Buttons in die Klasse {@link ButtonClass} verschoben
 */
public class FutureTripViewHolder extends RecyclerView.ViewHolder {

    public ButtonClass buttonClass;
    public TextViewClass textViewClass;
    public RecyclerView recyclerView;
    public View view;

    /**
     * Konstruktor, in dem die einzelnen Klassen für die Textfelder und Buttons initialisiert werden,
     * sowie der RecyclerView für die Kompaktansicht der Fahrt zugreifbar gemacht wird
     *
     * @param itemView            Ansicht
     * @param onItemClickListener Handling der Klicks auf das Item, die Buttons
     */
    public FutureTripViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
        super(itemView);
        textViewClass = new TextViewClass(itemView);
        buttonClass = new ButtonClass(itemView, this);

        //RecyclerView
        view = itemView.findViewById(R.id.CardView1);
        recyclerView = itemView.findViewById(R.id.recyclerView6);
        recyclerView.setHasFixedSize(true);

        // Wenn auf Buttons geklickt wird
        buttonClass.setOnClickListener(onItemClickListener);

        //Wenn auf das Item selbst geklickt wird
        itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });

    }
}
