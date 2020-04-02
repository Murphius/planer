package com.example.lkjhgf.helper.futureTrip;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.lkjhgf.Color.ButtonBootstrapBrandVisible;
import com.example.lkjhgf.R;
import com.example.lkjhgf.futureTrips.recyclerView.TripAdapter;
import com.example.lkjhgf.futureTrips.recyclerView.TripItem;

import java.util.ArrayList;

public abstract class MyTrip {

    Activity activity;
    ArrayList<TripItem> tripItems;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    BootstrapButton abort, addTrip, calculateTickets;

    public MyTrip(Activity activity, View view, ArrayList<TripItem> tripItems){
        this.activity = activity;
        this.tripItems = tripItems;
        findID(view); }

    void recyclerViewBlah(){
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(activity.getApplicationContext());
        adapter = new TripAdapter(tripItems, activity);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void findID(View view) {
        recyclerView = view.findViewById(R.id.recyclerView5);

        abort = view.findViewById(R.id.BootstrapButton15);
        addTrip = view.findViewById(R.id.BootstrapButton16);
        calculateTickets = view.findViewById(R.id.BootstrapButton17);

        abort.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        addTrip.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        calculateTickets.setBootstrapBrand(new ButtonBootstrapBrandVisible());
    }


    void startNextActivity(Intent newIntent) {
        //TODO speichern und so
        activity.startActivity(newIntent);
    }
}
