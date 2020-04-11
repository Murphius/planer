package com.example.lkjhgf.helper.service;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.form.MultipleTrip;
import com.example.lkjhgf.activites.multipleTrips.DetailedView;

import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.dto.Trip;

public class MultiplePossibleConnections extends PossibleConnections {

    private int numTrip;
    private int numAdult, numChildren;

    public static String EXTRA_NUM_TRIP = "com.example.lkjhgf.helper.service.EXTRA_NUM_TRIP";
    public static String EXTRA_NUM_ADULT = "com.example.lkjhgf.helper.service.EXTRA_NUM_ADULT";
    public static String EXTRA_NUM_CHILDREN = "com.example.lkjhgf.helper.service.EXTRA_NUM_CHILDREN";

    public MultiplePossibleConnections(Activity activity,
                                       View view,
                                       Intent intent,
                                       NetworkProvider provider) {
        super(activity, view, intent, provider);

        numTrip = intent.getIntExtra(MultipleTrip.EXTRA_NUM_TRIP, 1);
        String numTripString = numTrip + ". Fahrt \n mögliche Verbindungen";
        TextView numTripView = view.findViewById(R.id.app_name_3);
        numTripView.setText(numTripString);
        TextViews textViews = getText();
        numAdult = intent.getIntExtra(MultipleTrip.EXTRA_NUM_ADULT,0);
        numChildren = intent.getIntExtra(MultipleTrip.EXTRA_NUM_CHILDREN,0);
        String putText = numAdult + "";
        textViews.numAdult.setText( putText);
        putText = numChildren + "";
        textViews.numChildren.setText(putText);
    }

    @Override
    public void change_view_connection_detail(Trip trip) {
        Intent newIntent = new Intent(context, DetailedView.class);
        newIntent.putExtra(EXTRA_NUM_TRIP, numTrip);
        newIntent.putExtra(EXTRA_NUM_ADULT, numAdult);
        newIntent.putExtra(EXTRA_NUM_CHILDREN, numChildren);
        super.change_view_connection_detail(trip, newIntent);
    }
}
