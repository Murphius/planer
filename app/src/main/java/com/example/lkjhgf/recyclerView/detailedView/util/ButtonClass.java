package com.example.lkjhgf.recyclerView.detailedView.util;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.lkjhgf.color.ButtonBootstrapBrandVisible;
import com.example.lkjhgf.R;
import com.example.lkjhgf.recyclerView.detailedView.OnItemClickListener;

/**
 * Enthält alle Buttons eines einzelnen Fahrtabschnitts
 */
class ButtonClass {

    BootstrapButton stopoverLocationsShow;
    BootstrapButton showOnMap;

    /**
     * Initalisierung der Attribute <br/>
     *
     * <p>
     * Zuoordnung Attribute - ID <br/>
     * Button Design
     * OnClickListener setzen in {@link #setOnClickListener(DetailedConnectionViewHolder, OnItemClickListener)}
     * </p>
     *
     * @param detailedConnectionViewHolder - Zugeordnete Ansicht
     * @param view                         - Layout in dem gesucht wird
     * @param onItemClickListener          - Interface, dessen Funktionen beim Klick aufgerufen werden sollen
     *                                     <br/>
     */
    ButtonClass(DetailedConnectionViewHolder detailedConnectionViewHolder, View view, OnItemClickListener onItemClickListener) {
        stopoverLocationsShow = view.findViewById(R.id.BootstrapButton29);
        stopoverLocationsShow.setBootstrapSize(1.5f);
        stopoverLocationsShow.setBootstrapBrand(new ButtonBootstrapBrandVisible());

        showOnMap = view.findViewById(R.id.BootstrapButton36);
        showOnMap.setBootstrapBrand(new ButtonBootstrapBrandVisible());

        setOnClickListener(detailedConnectionViewHolder, onItemClickListener);
    }

    /**
     * OnClickListener für die zwei Buttons
     *
     * @param viewHolder -> für die Position des geklickten Elements
     * @param listener   ->  stellt Funktionen zur Verfügung
     */
    private void setOnClickListener(DetailedConnectionViewHolder viewHolder, OnItemClickListener listener) {
        showOnMap.setOnClickListener(v -> {
            if (listener != null) {
                int position = viewHolder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onShowDetails(position);
                }
            }
        });


        stopoverLocationsShow.setOnClickListener(v -> {
            if (listener != null) {
                int position = viewHolder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onShowDetails(position);
                }
            }
        });
    }
}
