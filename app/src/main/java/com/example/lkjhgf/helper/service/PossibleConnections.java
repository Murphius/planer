package com.example.lkjhgf.helper.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.lkjhgf.publicTransport.QueryTask;
import com.example.lkjhgf.helper.form.Form;

import java.util.Date;

import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.QueryTripsResult;
import de.schildbach.pte.dto.Trip;

public abstract class PossibleConnections {

    protected Context context;
    protected Activity activity;
    protected Intent intent;

    private TextViews text;

    Date user_date_time;
    Location start, destination, stopover;
    boolean isArrivalTime;
    QueryTripsResult result;

    public static String EXTRA_TRIP = "com.example.lkjhgf.helper.service.EXTRA_ITEM";


    PossibleConnections(Activity activity, View view, Intent intent, NetworkProvider provider){
        this.activity = activity;
        context = activity.getApplicationContext();

        user_date_time = (Date)intent.getSerializableExtra(Form.EXTRA_DATE);
        start = (Location) intent.getSerializableExtra(Form.EXTRA_START);
        stopover = (Location) intent.getSerializableExtra(Form.EXTRA_STOPOVER);
        destination = (Location) intent.getSerializableExtra(Form.EXTRA_DESTINATION);
        isArrivalTime = intent.getBooleanExtra(Form.EXTRA_ISARRIVALTIME, false);
        result = (QueryTripsResult) intent.getSerializableExtra(QueryTask.EXTRA_QUERY_TRIPS_RESULT);

        text = new TextViews(view, this);
        RecyclerViewService recyclerViewService = new RecyclerViewService(view, activity, this, provider);

        new Buttons(view, activity).setRecyclerView(recyclerViewService);
    }

    public abstract void change_view_connection_detail(Trip trip);

    void change_view_connection_detail(Trip trip, Intent newIntent){
        newIntent.putExtra(EXTRA_TRIP, trip);
        activity.startActivity(newIntent);
    }

    TextViews getText(){
        return text;
    }
}
