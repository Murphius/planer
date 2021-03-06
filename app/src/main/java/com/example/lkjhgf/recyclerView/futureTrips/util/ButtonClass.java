package com.example.lkjhgf.recyclerView.futureTrips.util;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.lkjhgf.color.ButtonBootstrapBrandVisible;
import com.example.lkjhgf.R;
import com.example.lkjhgf.recyclerView.futureTrips.OnItemClickListener;

/**
 * Enthält alle Buttons der Ansicht für eine geplante Fahrt
 */
public class ButtonClass {

    private FutureTripViewHolder futureTripViewHolder;

    private BootstrapButton edit;
    private BootstrapButton delete;
    private BootstrapButton copy;

    /**
     * Initialisierung {@link #findButtons(View)}
     * @param view - Ansicht
     * @param futureTripViewHolder ermöglicht Zugriff auf die Position der RecyclerView Position
     */
    ButtonClass(View view, FutureTripViewHolder futureTripViewHolder){
        this.futureTripViewHolder = futureTripViewHolder;
        findButtons(view);
    }

    /**
     * Zuoordnung Attribute - ID, sowie Anpassung des Layouts in {@link #designButtons()}<br/>
     *
     * @param view Ansicht die die Buttons enthält
     */
    private void findButtons(View view){
        edit = view.findViewById(R.id.BootstrapButton18);
        delete = view.findViewById(R.id.BootstrapButton19);
        copy = view.findViewById(R.id.BootstrapButton20);
        designButtons();
    }

    /**
     * Farbe und Schriftart der Buttons anpassen
     */
    private void designButtons(){
        edit.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        delete.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        copy.setBootstrapBrand(new ButtonBootstrapBrandVisible());
    }

    /**
     * Setzen der OnClickListener für die einzelnen Buttons,
     * jeder Button nutzt eine andere Funktion von {@link OnItemClickListener}
     */
    void setOnClickListener(OnItemClickListener onItemClickListener){
        delete.setOnClickListener(v -> {
            if(onItemClickListener != null){
                int position = futureTripViewHolder.getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    onItemClickListener.onDeleteClicked(position);
                }
            }
        });
        copy.setOnClickListener(v -> {
            if(onItemClickListener != null){
                int position = futureTripViewHolder.getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    onItemClickListener.onCopyClicked(position);
                }
            }
        });

        edit.setOnClickListener(v -> {
            if(onItemClickListener != null){
                int position = futureTripViewHolder.getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    onItemClickListener.onEditClicked(position);
                }
            }
        });
    }

    public void hideButtons(){
        edit.setVisibility(View.GONE);
        copy.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
    }
}
