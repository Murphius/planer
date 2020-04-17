package com.example.lkjhgf.helper.futureTrip;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.lkjhgf.color.ButtonBootstrapBrandVisible;
import com.example.lkjhgf.adapter.InterfaceAdapter;
import com.example.lkjhgf.R;
import com.example.lkjhgf.activites.futureTrips.closeUp.Complete;
import com.example.lkjhgf.activites.futureTrips.closeUp.Incomplete;
import com.example.lkjhgf.recyclerView.futureTrips.OnItemClickListener;
import com.example.lkjhgf.recyclerView.futureTrips.TripAdapter;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;
import com.example.lkjhgf.activites.singleTrip.CopyTrip;
import com.example.lkjhgf.activites.singleTrip.EditTrip;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import de.schildbach.pte.dto.Trip;

import static android.content.Context.MODE_PRIVATE;

public abstract class MyTrip {

    static String ALL_SAVED_TRIPS = "com.example.lkjhgf.futureTrips.ALL_SAVED_TRIPS";
    static String SAVED_TRIPS = "com.example.lkjhgf.futureTrips.SAVED_TRIPS";
    private static String SAVED_TRIPS_TASK = "com.example.lkjhgf.futureTrips.SAVED_TRIPS_TASK";

    public static String EXTRA_TRIP = "com.example.lkjhgf.futureTrips.EXTRA_TRIP";
    public static String EXTRA_NUM_ADULT = "com.example.lkjhgf.futureTrips.EXTRA_NUM_ADULT";
    public static String EXTRA_NUM_CHILDREN = "com.example.lkjhgf.futureTrips.EXTRA_NUM_CHILDREN";
    public static String EXTRA_NUM_TRIP = "com.example.lkjhgf.helper.futureTrip.EXTRA_NUM_TRIP";

    Activity activity;
    ArrayList<TripItem> tripItems;

    private RecyclerView recyclerView;

    private TripAdapter adapter;

    BootstrapButton abort, addTrip, calculateTickets;

    String dataPath;

    MyTrip(Activity activity, View view, TripItem tripItem, String dataPath) {
        this.activity = activity;
        this.dataPath = dataPath;
        loadData();

        insertTrip(tripItem);

        findID(view);
    }

    void insertTrip(TripItem tripItem){
        if (tripItem != null) {
            if (!tripItems.contains(tripItem)) {
                tripItems.add(tripItem);
                sortTrips();
            }
        }
    }

    void recyclerViewBlah() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity.getApplicationContext());
        adapter = new TripAdapter(tripItems, activity);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                MyTrip.this.onItemClick(position);
            }

            @Override
            public void onDeleteClicked(int position) {
                MyTrip.this.onDeleteClicked(position);
            }

            public void onCopyClicked(int position) {
                myOnCopyClicked(position);
            }


            public void onEditClicked(int position) {
                editClickMethod(position);
            }
        });
    }

    private void myOnCopyClicked(int position){
        TripItem current = tripItems.get(position);

        Intent newIntent;
        if (current.isComplete()){
            newIntent = new Intent(activity.getApplicationContext(), CopyTrip.class);
        }else{
            newIntent = new Intent(activity.getApplicationContext(), com.example.lkjhgf.activites.multipleTrips.CopyTrip.class);
            newIntent.putExtra(EXTRA_NUM_TRIP, position);
            newIntent.putExtra(EXTRA_NUM_ADULT, current.getNumAdult());
            newIntent.putExtra(EXTRA_NUM_CHILDREN, current.getNumChildren());
        }
        newIntent.putExtra(EXTRA_TRIP, current.getTrip());
        startNextActivity(newIntent);
    }


    private void onDeleteClicked(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Diese Fahrt wirklich löschen?");
        builder.setCancelable(false);
        builder.setPositiveButton("Ja", (dialog, which) -> removeItemAtPosition(position));
        builder.setNegativeButton("Nein", (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void onItemClick(int position) {
        TripItem current = tripItems.get(position);
        Intent newIntent;
        if (tripItems.get(position).isComplete()) {
            newIntent = new Intent(activity.getApplicationContext(), Complete.class);
        } else {
            newIntent = new Intent(activity.getApplicationContext(), Incomplete.class);
            newIntent.putExtra(EXTRA_NUM_ADULT, current.getNumAdult());
            newIntent.putExtra(EXTRA_NUM_CHILDREN, current.getNumAdult());
        }
        newIntent.putExtra(EXTRA_TRIP, tripItems.get(position).getTrip());
        startNextActivity(newIntent);
    }

    private void editClickMethod(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Beim Editieren wird diese Fahrt gelöscht und über zurück NICHT wieder hergestellt!" +
                "\nWirklich fortfahren ?");
        builder.setCancelable(false);
        builder.setNegativeButton("Nein", ((dialog, which) -> dialog.cancel()));
        builder.setPositiveButton("Ja", (dialog, which) -> startEdit(position));
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void startEdit(int position){
        TripItem current = tripItems.get(position);
        tripItems.remove(position);
        adapter.notifyItemRemoved(position);
        Intent newIntent;
        if (current.isComplete()){
            newIntent = new Intent(activity.getApplicationContext(), EditTrip.class);
        }else{
            newIntent = new Intent(activity.getApplicationContext(), com.example.lkjhgf.activites.multipleTrips.EditTrip.class);
            newIntent.putExtra(EXTRA_NUM_ADULT, current.getNumAdult());
            newIntent.putExtra(EXTRA_NUM_CHILDREN, current.getNumChildren());
        }
        newIntent.putExtra(EXTRA_TRIP, current.getTrip());
        startNextActivity(newIntent);
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

    void removeItemAtPosition(int position) {
        tripItems.remove(position);
        adapter.notifyItemRemoved(position);
    }

    void startNextActivity(Intent newIntent) {
        saveData();
        activity.startActivity(newIntent);
    }

    private void sortTrips() {
        Collections.sort(tripItems, (tripItem1, tripItem2) -> {
            if (tripItem1.getTrip().getFirstDepartureTime().before(tripItem2.getTrip().getFirstDepartureTime())) {
                return -1;
            } else {
                return 1;
            }
        });
    }

    public void saveData() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(dataPath,
                MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        GsonBuilder builder = new GsonBuilder();
        InterfaceAdapter adapter = new InterfaceAdapter();
        builder.registerTypeAdapter(Trip.Public.class, adapter);
        builder.registerTypeAdapter(Trip.Individual.class, adapter);
        Gson gson = builder.create();
        String json = gson.toJson(tripItems);
        editor.putString(SAVED_TRIPS_TASK, json);
        editor.apply();
    }

    void loadData() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(dataPath,
                MODE_PRIVATE);

        String json = sharedPreferences.getString(SAVED_TRIPS_TASK, null);
        InterfaceAdapter adapter = new InterfaceAdapter();
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Trip.Public.class, adapter);
        builder.registerTypeAdapter(Trip.Individual.class, adapter);
        builder.registerTypeAdapter(Trip.Leg.class, adapter);
        Gson gson = builder.create();
        Type type = new TypeToken<ArrayList<TripItem>>() {
        }.getType();
        tripItems = gson.fromJson(json, type);
        if (tripItems == null) {
            tripItems = new ArrayList<>();
        } else {
            Date today = Calendar.getInstance().getTime();
            long oneDay = 86400000; // 24h = 86 400 000 ms
            for (TripItem item : tripItems) {
                if (item.getTrip().getLastArrivalTime().getTime() + oneDay - today.getTime() <= 0) {
                    tripItems.remove(item);
                    if (tripItems.isEmpty()) {
                        tripItems = new ArrayList<>();
                        return;
                    }
                }
            }
        }
    }

}