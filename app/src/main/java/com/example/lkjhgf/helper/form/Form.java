package com.example.lkjhgf.helper.form;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.example.lkjhgf.main_menu.Settings;
import com.example.lkjhgf.public_transport.QueryParameter;
import com.example.lkjhgf.public_transport.QueryTask;

import java.util.Calendar;

import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.Trip;
import de.schildbach.pte.dto.TripOptions;


public abstract class Form {

    public static final String EXTRA_DATE = "com.example.lkjhgf.individual_trip.form.EXTRA_DATE";
    public static final String EXTRA_START = "com.example.lkjhgf.individual_trip.form.EXTRA_START";
    public static final String EXTRA_DESTINATION = "com.example.lkjhgf.individual_trip.form.EXTRA_DESTINATION";
    public static final String EXTRA_ISARRIVALTIME = "com.example.lkjhgf.individual_trip.form.EXTRA_ISARRIVALTIME";
    public static final String EXTRA_STOPOVER = "com.example.lkjhgf.individual_trip.form.EXTRA_STOPOVER";

    Location startLocation, stopoverLocation, destinationLocation;
    Calendar selectedDate;
    boolean isArrivalTime;
    private NetworkProvider provider;

    protected Context context;
    protected Activity activity;
    protected Intent intent;


    private Form_Buttons buttons;
    protected Form_Text text;

    Form(Activity activity, View view, NetworkProvider provider) {
        this.activity = activity;
        this.provider = provider;
        context = activity.getApplicationContext();
        buttons = new Form_Buttons(view, activity, this);
        text = new Form_Text(view, activity, this);
        buttons.setText(text);

        // Aktuellen Zeitpunkt als Defaultwert
        selectedDate = Calendar.getInstance();
    }

    Form(Activity activity, View view, NetworkProvider provider, Trip trip){
        this(activity, view, provider);
        text.fillTextViews(trip);
        selectedDate.setTime(trip.getFirstDepartureTime());
    }

    public void setOnClickListener() {
       buttons.setOnClickListener();
    }

    public void setAdapter() {
        text.setAdapter();
    }

    Form_Text getText() {
        return text;
    }

    boolean checkFormComplete() {
        if (text.date_view.getText().length() == 0) {
            Toast.makeText(context,
                    "Bitte ein Datum eingeben",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (text.arrival_departure_view.getText().length() == 0) {
            Toast.makeText(context,
                    "Bitte eine Uhrzeit eingeben",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (text.start_view.getText().length() == 0 || startLocation == null) {
            Toast.makeText(context,
                    "Bitte einen eindeutigen Startpunkt eingeben",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (text.destination_view.getText().length() == 0 || destinationLocation == null) {
            Toast.makeText(context,
                    "Bitte einen eindeutigen Zielpunkt eingeben",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void setIntentExtras() {
        intent.putExtra(EXTRA_DATE, selectedDate.getTime());
        intent.putExtra(EXTRA_ISARRIVALTIME, isArrivalTime);
        intent.putExtra(EXTRA_START, startLocation);
        intent.putExtra(EXTRA_STOPOVER, stopoverLocation);
        intent.putExtra(EXTRA_DESTINATION, destinationLocation);
    }

    void changeViewToPossibleConnections(){
        setIntentExtras();

        TripOptions tripOptions = Settings.getTripOptions(activity);

        QueryParameter queryParameter = new QueryParameter(startLocation,stopoverLocation,destinationLocation,selectedDate.getTime(), !isArrivalTime, tripOptions);
        new QueryTask(provider, activity, intent).execute(queryParameter);
    }

    public abstract void copy();
}
