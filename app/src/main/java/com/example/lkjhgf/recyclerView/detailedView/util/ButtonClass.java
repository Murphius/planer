package com.example.lkjhgf.recyclerView.detailedView.util;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.lkjhgf.Color.ButtonBootstrapBrandVisible;
import com.example.lkjhgf.R;
import com.example.lkjhgf.recyclerView.detailedView.OnItemClickListener;

 class ButtonClass {

    BootstrapButton stopoverLocationsShow;
    BootstrapButton showOnMap;

    ButtonClass(DetailedConnectionViewHolder detailedConnectionViewHolder, View view, OnItemClickListener onItemClickListener){
        stopoverLocationsShow = view.findViewById(R.id.BootstrapButton29);
        stopoverLocationsShow.setBootstrapSize(1.5f);
        stopoverLocationsShow.setBootstrapBrand(new ButtonBootstrapBrandVisible());

        showOnMap = view.findViewById(R.id.BootstrapButton36);
        showOnMap.setBootstrapBrand(new ButtonBootstrapBrandVisible());

        setOnClickListener(detailedConnectionViewHolder, onItemClickListener);
    }

    private void setOnClickListener(DetailedConnectionViewHolder viewHolder,OnItemClickListener listener){
        showOnMap.setOnClickListener(v -> {
            if(listener != null){
                int position = viewHolder.getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
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
