package com.example.lkjhgf.helper.closeUp;

import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.util.Utils;
import com.example.lkjhgf.helper.util.UtilsString;


import de.schildbach.pte.dto.Trip;

/**
 * Handling aller Textfelder in der Detailansicht von einer Fahrt
 */
public class TextViewClass {

    private CloseUp closeUp;

    private Resources resources;

    private TextView date;
    private TextView time_of_arrival, time_of_departure;
    private TextView duration, num_changes, preisstufe;
    private TextView delay_departure, delay_arrival;
    TextView numChildren, numChildrenView;
    TextView numAdult, numAdultView;
    TextView ticketView, useTicket;
    View view;

    /**
     * Initialiserung der Attribute und füllen der Textfelder <br/>
     * <p>
     * Die initialisierung der Attribute: {@link #findViews(View)} <br/>
     * Das füllen der Textfelder: {@link #fillTextView()}
     *
     * @param view      Layout
     * @param resources für die Textfarbe bei Verspätungen
     * @param closeUp   um auf die Fahrt zugreifen zu können
     */
    TextViewClass(View view, Resources resources, CloseUp closeUp) {
        this.closeUp = closeUp;
        this.resources = resources;

        findViews(view);
        fillTextView();
    }

    /**
     * Initialisierung der Attribute <br/>
     * <p>
     * Zuordnung Attribut <-> ID für eine einfachere Handhabung <br/>
     *
     * @param view Layout
     */
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
        this.view = view.findViewById(R.id.view5);
        ticketView = view.findViewById(R.id.textView89);
        useTicket = view.findViewById(R.id.textView90);
    }

    /**
     * Füllt die Ansicht, mit den Informationen der Fahrt <br/>
     * <p>
     * Dabei werden die wesentlichen Informationen zur Fahrt angezeigt: <br/>
     * #Umsteige, Preisstufe, Dauer, Abfahrtszeit, Ankunftszeit, (ggf. Verspätungen), Datum
     */
    private void fillTextView() {
        //#Umstiege
        num_changes.setText(UtilsString.setNumChanges(closeUp.trip));

        //Preisstufe
        if (closeUp.trip.fares != null) {
            preisstufe.setText(closeUp.trip.fares.get(0).units);
        } else {
            preisstufe.setText(" ? ");
        }

        //Dauer
        duration.setText(UtilsString.durationString(Utils.durationToHour(closeUp.trip.getDuration()),
                Utils.durationToMinutes(closeUp.trip.getDuration())));

        //TODO ueberpruefen
        //Datum + Abfahrtszeit + Ankunftszeit
        date.setText(UtilsString.setDate(closeUp.trip.getFirstDepartureTime()));
        time_of_departure.setText(UtilsString.setTime(closeUp.trip.getFirstDepartureTime()));
        time_of_arrival.setText(UtilsString.setTime(closeUp.trip.getLastArrivalTime()));

        //TODO ueberpruefen
        //Falls es sich um ÖPNV Verbindungen handelt, ggf. die Verspätung anzeigen
        Trip.Leg firstLeg = closeUp.trip.legs.get(0);
        Trip.Leg lastLeg = closeUp.trip.legs.get(closeUp.trip.legs.size() - 1);
        if (firstLeg instanceof Trip.Public) {
            Utils.setDelayView(delay_departure, Utils.longToInt(((Trip.Public) firstLeg).getDepartureDelay()), resources);
        }
        if (lastLeg instanceof Trip.Public) {
            Utils.setDelayView(delay_arrival, Utils.longToInt(((Trip.Public) lastLeg).getArrivalDelay()), resources);
        }
    }
}
