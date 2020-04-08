package com.example.lkjhgf.futureTrips.recyclerView;

import android.app.Activity;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.lkjhgf.Color.ButtonBootstrapBrandVisible;
import com.example.lkjhgf.R;

import java.util.concurrent.Future;

class ButtonClass {

    private  FutureTrip futureTrip;

    private BootstrapButton edit;
    private BootstrapButton delete;
    private BootstrapButton copy;

    ButtonClass(View view, FutureTrip futureTrip){
        this.futureTrip = futureTrip;
        findButtons(view);
    }

    private void findButtons(View view){
        edit = view.findViewById(R.id.BootstrapButton18);
        delete = view.findViewById(R.id.BootstrapButton19);
        copy = view.findViewById(R.id.BootstrapButton20);
        designButtons();
    }

    private void designButtons(){
        edit.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        delete.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        copy.setBootstrapBrand(new ButtonBootstrapBrandVisible());
    }

    void setOnClickListener(OnItemClickListener onItemClickListener){
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    int position = futureTrip.getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        onItemClickListener.onDeleteClicked(position);
                    }
                }
            }
        });
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    int position = futureTrip.getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        onItemClickListener.onCopyClicked(position);
                    }
                }
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    int position = futureTrip.getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        onItemClickListener.onEditClicked(position);
                    }
                }
            }
        });


    }


}
