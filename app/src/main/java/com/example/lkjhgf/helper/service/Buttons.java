package com.example.lkjhgf.helper.service;

import android.app.Activity;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.lkjhgf.color.ButtonBootstrapBrandVisible;
import com.example.lkjhgf.R;

public class Buttons {

    private Activity activity;

    private  RecyclerViewService recyclerViewService;

    private BootstrapButton earlierButton, editButton, laterButton;

    Buttons(View view, Activity activity){
        this.activity = activity;
        findButtons(view);
        designButtons();
        setOnClickListener();
    }

    private void findButtons(View view){
        earlierButton = view.findViewById(R.id.BootstrapButton23);
        editButton = view.findViewById(R.id.BootstrapButton24);
        laterButton = view.findViewById(R.id.BootstrapButton25);
    }

    void setRecyclerView(RecyclerViewService recyclerViewService){
        this.recyclerViewService = recyclerViewService;
    }

    private void designButtons(){
        earlierButton.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        laterButton.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        editButton.setBootstrapBrand(new ButtonBootstrapBrandVisible());
    }

    private void setOnClickListener(){
        editButton.setOnClickListener(v -> activity.onBackPressed());
        laterButton.setOnClickListener(view -> recyclerViewService.newConnectionsLater());
        earlierButton.setOnClickListener(v -> recyclerViewService.newConnectionsEarlier());
    }

}
