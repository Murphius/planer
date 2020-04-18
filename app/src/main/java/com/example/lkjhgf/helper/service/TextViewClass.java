package com.example.lkjhgf.helper.service;

import android.view.View;
import android.widget.TextView;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.util.UtilsString;

/**
 * Handhabung der Textfelder des Layouts für die Ansicht möglicher Verbindungen
 */
class TextViewClass {

    private TextView dateView, arrival_departure_timeView, arrival_departureView;
    private TextView departurePointView, user_stopoverView, destinationView, stopoverView;
    TextView numChildren, numAdult, numChildrenView, numAdultView;

    /**
     * Aufruf der Funktionen zur initialierung der Attribute & füllen der Textfelder
     *
     * @param view                - Layout
     * @param possibleConnections - enthält die Informationen zum füllen der Textfelder
     */
    TextViewClass(View view, PossibleConnections possibleConnections) {
        findTextViews(view);
        fillTextViews(possibleConnections);
    }

    /**
     * Initialisierung der Attribute
     * <p>
     * Zuordnung Attribut -> ID --> einfachere Handhabung
     *
     * @param view Layout in dem nach den Elementen gesucht werden soll
     */
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

    /**
     * Füllen der Textfelder mit den Informationen aus dem Parameter
     * <p>
     * Nutzt Hilfsfunktionen der Klasse {@link UtilsString}
     *
     * @param possibleConnections enthält die Informationen, welche angezeigt werden sollen
     */
    private void fillTextViews(PossibleConnections possibleConnections) {
        // Ankunftszeit / Abfahrtszeit
        arrival_departureView.setText(UtilsString.arrivalDepartureTime(possibleConnections.isArrivalTime));
        //Abfahrtsort
        departurePointView.setText(UtilsString.setLocationName(possibleConnections.start));
        //Zwischenhalt
        if (possibleConnections.stopover != null) {
            user_stopoverView.setText(UtilsString.setLocationName(possibleConnections.stopover));
        } else {
            //Wenn kein Zwischenhalt angegeben ist, sollen die Textfelder nicht angezeigt werden
            user_stopoverView.setVisibility(View.GONE);
            stopoverView.setVisibility(View.GONE);
        }
        //Ziel
        destinationView.setText(UtilsString.setLocationName(possibleConnections.destination));
        //Datum
        dateView.setText(UtilsString.setDate(possibleConnections.user_date_time));
        //Zeit
        arrival_departure_timeView.setText(UtilsString.setTime(possibleConnections.user_date_time));
    }

}
