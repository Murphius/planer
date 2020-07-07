package com.example.lkjhgf.recyclerView.futureTrips.util;

import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.util.Utils;
import com.example.lkjhgf.helper.util.UtilsString;

import de.schildbach.pte.dto.Trip;

/**
 * Enthält alle Textfelder der Ansicht für eine geplante Fahrt
 */
public class TextViewClass {

    private TextView userDate;
    private TextView timeOfDeparture, timeOfArrival;
    private TextView delayDeparture, delayArrival;
    private TextView duration;
    private TextView startLocation, destinationLocation;
    private TextView numChanges;
    private TextView preisstufe;
    //einezelne horizontale Linie, unsichtbar wenn Personenanzahl unsichtbar ist
    public View line;
    public TextView numAdult, userNumAdult;
    public TextView numChildren, userNumChildren;
    public TextView ticketInformationHolder, ticketInformationView;

    /**
     * Initialisierung der Varibalen in der Funktion {@link #findTextView(View)}
     *
     * @param view In welcher Ansicht gesucht werden soll
     */
    TextViewClass(View view) {
        findTextView(view);
    }

    /**
     * Zuordnung Attribut - ID
     *
     * @param view - In welcher Ansicht gesucht werden soll
     */
    private void findTextView(View view) {
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

        ticketInformationHolder = view.findViewById(R.id.textView94);
        ticketInformationView = view.findViewById(R.id.textView93);

        line = view.findViewById(R.id.view6);
    }

    /**
     * Füllt die Textfelder mit den Informationen aus dem Trip <br/>
     *
     * @param trip      - Alle Informationen zur Reise
     * @param resources - Benötigt, um die Textfarbe zu ändern
     */
    public void fillTextView(Trip trip, Resources resources) {
        // Datum, Ankunfts, Abfahrtszeit
        userDate.setText(UtilsString.setDate(trip.getFirstDepartureTime()));

        Trip.Leg firstLeg = trip.legs.get(0);
        Trip.Leg lastLeg = trip.legs.get(trip.legs.size() - 1);
        if (firstLeg instanceof Trip.Public) {
            Trip.Public firstPublic = (Trip.Public) firstLeg;
            if (firstPublic.departureStop != null) {
                timeOfDeparture.setText(UtilsString.setTime(firstPublic.getDepartureTime(true)));
                Utils.setDelayView(delayDeparture, firstPublic.getDepartureDelay(), firstPublic.getDepartureTime(true), resources, true);
            }
        } else {
            timeOfDeparture.setText(UtilsString.setTime(trip.getFirstDepartureTime()));
        }
        if (lastLeg instanceof Trip.Public) {
            Trip.Public lastPublic = (Trip.Public) lastLeg;
            if (lastPublic.arrivalStop != null) {
                timeOfArrival.setText(UtilsString.setTime(lastPublic.getArrivalTime(true)));
                Utils.setDelayView(delayArrival, lastPublic.getArrivalDelay(), lastPublic.getArrivalTime(true), resources, true);
            }
        } else {
            timeOfArrival.setText(UtilsString.setTime(trip.getLastArrivalTime()));
        }
        // Dauer der Fahrt
        long durationHour = Utils.durationToHour(trip.getDuration());
        long durationMinute = Utils.durationToMinutes(trip.getDuration());
        duration.setText(UtilsString.durationString(durationHour, durationMinute));
        //Umstiege
        numChanges.setText(UtilsString.setNumChanges(trip));
        // Preisstufe
        preisstufe.setText(UtilsString.setPreisstufenName(trip));
        // Start- und Zielpunkt
        startLocation.setText(UtilsString.setLocationName(trip.from));
        destinationLocation.setText(UtilsString.setLocationName(trip.to));
    }
}
