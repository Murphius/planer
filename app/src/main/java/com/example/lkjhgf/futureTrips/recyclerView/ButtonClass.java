package com.example.lkjhgf.futureTrips.recyclerView;

import android.app.Activity;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.lkjhgf.Color.ButtonBootstrapBrandVisible;
import com.example.lkjhgf.R;

class ButtonClass {

    private BootstrapButton edit;
    private BootstrapButton delete;
    private BootstrapButton copy;

    ButtonClass(View view){
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

    private void setOnClickListener(){
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                // Wechseln in die Ansicht des Formulars
                // Intent: Start, Ziel, Datum, Uhrzeit
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                //Entfernen der Fahrt aus der Liste
            }
        });
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                //Wechseln in die Ansicht des Formulars
                //Intent: Start, Ziel
            }
        });
    }
}
