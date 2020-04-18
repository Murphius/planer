package com.example.lkjhgf.helper.service;

import android.view.View;
import android.widget.TextView;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.UtilsString;


class TextViews {

    private TextView dateView, arrival_departure_timeView, arrival_departureView;
    private TextView departurePointView, user_stopoverView, destinationView, stopoverView;
    TextView numChildren, numAdult, numChildrenView, numAdultView;

    private PossibleConnections possibleConnections;

    TextViews(View view, PossibleConnections possibleConnections) {
        this.possibleConnections = possibleConnections;
        findTextViews(view);
        fillTextViews();

    }

    private void findTextViews(View view) {
        dateView = view.findViewById(R.id.textView7);
        arrival_departure_timeView = view.findViewById(R.id.textView8);
        departurePointView = view.findViewById(R.id.textView9);
        user_stopoverView = view.findViewById(R.id.textView10);
        destinationView = view.findViewById(R.id.textView11);
        arrival_departureView = view.findViewById(R.id.textView13);
        stopoverView = view.findViewById(R.id.textView15);
        numChildren = view.findViewById(R.id.textView79);
        numChildrenView = view.findViewById(R.id.textView76);
        numAdult = view.findViewById(R.id.textView78);
        numAdultView = view.findViewById(R.id.textView77);
    }

    private void fillTextViews() {
        arrival_departureView.setText(UtilsString.arrivalDepartureTime(possibleConnections.isArrivalTime));

        String setText = UtilsString.setLocationName(possibleConnections.start);
        departurePointView.setText(setText);
        if (possibleConnections.stopover != null) {
            setText = UtilsString.setLocationName(possibleConnections.stopover);
            user_stopoverView.setText(setText);
        } else {
            user_stopoverView.setVisibility(View.GONE);
            stopoverView.setVisibility(View.GONE);
        }
        setText = UtilsString.setLocationName(possibleConnections.destination);
        destinationView.setText(setText);

        dateView.setText(UtilsString.setDate(possibleConnections.user_date_time));
        arrival_departure_timeView.setText(UtilsString.setTime(possibleConnections.user_date_time));
    }

}
