package com.example.lkjhgf.futureTrips.recyclerView;

import android.app.Activity;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.Utils;

import de.schildbach.pte.dto.Trip;

class TextViewClass {

    private TextView userDate;
    private TextView timeOfDeparture, timeOfArrival;
    private TextView delayDeparture, delayArrival;
    private TextView duration;
    private TextView startLocation, destinationLocation;
    private TextView numChanges;
    private TextView preisstufe;
    View line;
    TextView numAdult, userNumAdult;
    TextView numChildren, userNumChildren;

    TextViewClass(View view){
        findTextView(view);
    }

    private void findTextView(View view){
        userDate = view.findViewById(R.id.textView17);
        timeOfDeparture = view.findViewById(R.id.textView19);
        timeOfArrival = view.findViewById(R.id.textView22);

        delayDeparture = view.findViewById(R.id.textView20);
        delayArrival = view.findViewById(R.id.textView23);

        duration = view.findViewById(R.id.textView25);

        startLocation = view.findViewById(R.id.textView91);
        destinationLocation = view.findViewById(R.id.textView88);

        preisstufe = view.findViewById(R.id.textView27);
        numChanges = view.findViewById(R.id.textView29);

        numAdult = view.findViewById(R.id.textView30);
        userNumAdult = view.findViewById(R.id.textView84);

        numChildren = view.findViewById(R.id.textView85);
        userNumChildren = view.findViewById(R.id.textView86);

        line = view.findViewById(R.id.view6);
    }


    void fillTextView(Trip trip, Resources resources){
        userDate.setText(Utils.setDate(trip.getFirstDepartureTime()));
        timeOfDeparture.setText(Utils.setTime(trip.getFirstDepartureTime()));
        timeOfArrival.setText(Utils.setTime(trip.getLastArrivalTime()));

        int delay = 0;
        if(trip.getFirstPublicLeg() != null){
            if(trip.getFirstPublicLeg().getDepartureDelay() != null){
                delay = Utils.longToMinutes(trip.getFirstPublicLeg().getDepartureDelay());
            }
        }
        Utils.setDelayView(delayDeparture, delay, resources);

        delay = 0;
        if(trip.getLastPublicLeg() != null){
            if(trip.getLastPublicLeg().getArrivalDelay() != null){
                delay = Utils.longToMinutes(trip.getLastPublicLeg().getArrivalDelay());
            }
        }
        Utils.setDelayView(delayArrival, delay, resources);

        long durationHour = Utils.durationToHour(trip.getDuration());
        long durationMinute = Utils.durationToMinutes(trip.getDuration());
        duration.setText(Utils.durationString(durationHour, durationMinute));

        if(trip.getNumChanges() != null){
            String text = trip.getNumChanges() + "x";
            numChanges.setText(text);
        }

        preisstufe.setText(trip.fares.get(0).units);

        startLocation.setText(Utils.setLocationName(trip.from));
        destinationLocation.setText(Utils.setLocationName(trip.to));
    }

}
