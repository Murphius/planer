package com.example.lkjhgf.helper.closeUp;

import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.Utils;


import de.schildbach.pte.dto.Trip;

public class TextViews {

    private CloseUp closeUp;

    private Resources resources;

    private TextView date;
    private TextView time_of_arrival, time_of_departure;
    private TextView duration, num_changes, preisstufe;
    private TextView delay_departure, delay_arrival;
    TextView numChildren, numChildrenView;
    TextView numAdult, numAdultView;
    TextView ticket, useTicket;
    View view;

    TextViews(View view, Resources resources, CloseUp closeUp) {
        this.closeUp = closeUp;
        this.resources = resources;

        findViews(view);
        fillTextView();
    }

    private void findViews(View view) {
        date = view.findViewById(R.id.textView56);
        time_of_departure = view.findViewById(R.id.textView6);
        time_of_arrival = view.findViewById(R.id.textView48);
        duration = view.findViewById(R.id.textView50);
        num_changes = view.findViewById(R.id.textView52);
        preisstufe = view.findViewById(R.id.textView54);
        delay_arrival = view.findViewById(R.id.textView71);
        delay_departure = view.findViewById(R.id.textView70);
        numAdult = view.findViewById(R.id.textView83);
        numAdultView = view.findViewById(R.id.textView82);
        numChildren = view.findViewById(R.id.textView81);
        numChildrenView = view.findViewById(R.id.textView41);
        ticket = view.findViewById(R.id.textView89);
        useTicket = view.findViewById(R.id.textView90);
        this.view = view.findViewById(R.id.view5);
    }

    private void fillTextView() {
        String text = closeUp.trip.getNumChanges() + "x";
        num_changes.setText(text);

        if (closeUp.trip.fares != null) {
            preisstufe.setText(closeUp.trip.fares.get(0).units);
        } else {
            preisstufe.setText(" ? ");
        }

        duration.setText(Utils.durationString(Utils.durationToHour(closeUp.trip.getDuration()),
                Utils.durationToMinutes(closeUp.trip.getDuration())));

        //TODO ueberpruefen
        date.setText(Utils.setDate(closeUp.trip.getFirstDepartureTime()));
        time_of_departure.setText(Utils.setTime(closeUp.trip.getFirstDepartureTime()));
        time_of_arrival.setText(Utils.setTime(closeUp.trip.getLastArrivalTime()));

        //TODO ueberpruefen
        Trip.Leg firstLeg = closeUp.trip.legs.get(0);
        Trip.Leg lastLeg = closeUp.trip.legs.get(closeUp.trip.legs.size()-1);

        if(firstLeg instanceof Trip.Public){
            Utils.setDelayView(delay_departure, Utils.longToMinutes(((Trip.Public)firstLeg).getDepartureDelay()), resources);
        }
        if(lastLeg instanceof  Trip.Public){
            Utils.setDelayView(delay_arrival, Utils.longToMinutes(((Trip.Public)lastLeg).getArrivalDelay()), resources);
        }

    }
}
