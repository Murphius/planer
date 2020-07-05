package com.example.lkjhgf.helper.form;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.util.UtilsString;
import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.activities.multipleTrips.ShowAllPossibleConnections;


import java.util.Calendar;
import java.util.HashMap;

import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.dto.Fare;
import de.schildbach.pte.dto.Trip;

/**
 * Das Formular für Fahrten, die bei der Optimierung von Fahrtkosten berücksichtigt werden sollen
 */
public class MultipleTrip extends Form {

    private int numTrip;
    private HashMap<Fare.Type, Integer> numPersonsPerClass;

    private TextView titleView;
    private View separator;

    /**
     * Wenn eine Fahrt editiert oder kopiert werden soll, sollen die zugehörigen Informationen
     * angezeigt werden <br/>
     * <p>
     * Nutzt {@link #MultipleTrip(Activity, View)} als Basis für den View
     *
     * @param trip               Trip der editiert oder kopiert werden soll
     * @param numPersonsPerClass gibt die Anzahl der reisenden Personen pro "Klasse" an zB Erwachsene - 5
     * @param numTrip            #Trip
     */
    public MultipleTrip(Activity activity,
                        View view, Trip trip,
                        HashMap<Fare.Type, Integer> numPersonsPerClass,
                        int numTrip) {
        // "Normale" Ansicht herstellen
        this(activity, view);
        if (numPersonsPerClass == null) {
            this.numPersonsPerClass = new HashMap<>();
        } else {
            this.numPersonsPerClass = numPersonsPerClass;
        }

        //Informationen über die #reisender Personen anzeigen
        //ToDo bei anderen Providern müssen mehr Textfelder angelegt werden & diese anschließend mit den Werten befüllt werden
        String setText = numPersonsPerClass.get(Fare.Type.ADULT) + "";
        text.numAdultView.setText(setText);
        setText = numPersonsPerClass.get(Fare.Type.CHILD) + "";
        text.numChildrenView.setText(setText);

        this.numTrip = numTrip;
        String titleString = numTrip + ". Fahrt";
        titleView.setText(titleString);


        //Informationen der Verbindung den Attributen zuweisen
        startLocation = trip.from;
        destinationLocation = trip.to;
        selectedDate.setTime(trip.getFirstDepartureTime());

        // Informationen der Verbindung in den jeweiligen Textfelder anzeigen
        text.date_view.setText(UtilsString.setDate(selectedDate.getTime()));
        text.arrivalDepartureView.setText(UtilsString.setTime(selectedDate.getTime()));
        text.start_view.setText(UtilsString.setLocationName(startLocation));
        text.destination_view.setText(UtilsString.setLocationName(destinationLocation));

        hideStopover(view);
    }

    /**
     * Neben den Informationen zu Start, Ziel, Zeitpunkt, werden auch die #reisender Personen
     * sowie die #Fahrt "benätigt"
     *
     * @see Form#Form(Activity, View)
     */
    public MultipleTrip(Activity activity,
                        View view) {
        super(activity, view);
        if (numPersonsPerClass == null) {
            numPersonsPerClass = new HashMap<>();
        }
        titleView = view.findViewById(R.id.app_name2);
        Intent intent = activity.getIntent();

        // Die Nr. des Tripps
        numTrip = intent.getIntExtra(MainMenu.EXTRA_NUM_TRIP, 1);
        String titleString = numTrip + ". Fahrt";
        titleView.setText(titleString);

        hideStopover(view);
    }

    /**
     * @return false - wenn {@link Form#checkFormComplete()} false ist <br/>
     * false - wenn der Zeitpunkt nicht in der Zukunft liegt  <br/>
     * false - wenn insgesammt nicht mindestens eine Person reist <br/>
     * true - wenn alle diese Bedingungen erfüllt sind
     * @see Form#checkFormComplete()
     */
    public boolean checkFormComplete() {
        if (!super.checkFormComplete()) {
            return false;
        }
        if (Calendar.getInstance().getTime().after(selectedDate.getTime())) {
            Toast.makeText(context,
                    "Bitte einen Zeitpunkt in der Zukunft auswählen",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        //ToDo Erweiterung bei anderem Provider
        String numAdultString = text.numAdultView.getText().toString();
        String numChildrenString = text.numChildrenView.getText().toString();
        int numAdult = 0;
        int numChildren = 0;
        if (!numAdultString.isEmpty()) {
            try {
                numAdult = Integer.parseInt(numAdultString);
            } catch (NumberFormatException e) {
                Toast.makeText(context, "Die Zahl der reisenden Personen ist zu groß", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        numPersonsPerClass.put(Fare.Type.ADULT, numAdult);

        if (!numChildrenString.isEmpty()) {
            try {
                numChildren = Integer.parseInt(numChildrenString);
            } catch (NumberFormatException e) {
                Toast.makeText(context, "Die Zahl der reisenden Kinder ist zu groß", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        numPersonsPerClass.put(Fare.Type.CHILD, numChildren);
        int sumPersons = 0;
        for (Fare.Type type : numPersonsPerClass.keySet()) {
            sumPersons += numPersonsPerClass.get(type);
        }
        if (sumPersons <= 0) {
            Toast.makeText(context,
                    "Die Personenanzahl muss mindestens eins sein",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Zusätzliche Informationen an die nächste Aktivität übergeben; festlegen, welche
     * Aktivität als nächstes gestartet werden soll <br/>
     *
     * @see Form#changeViewToPossibleConnections()
     */
    void changeViewToPossibleConnections() {
        intent = new Intent(context, ShowAllPossibleConnections.class);
        intent.putExtra(MainMenu.EXTRA_NUM_TRIP, numTrip);
        intent.putExtra(MainMenu.NUM_PERSONS_PER_CLASS, numPersonsPerClass);
        super.changeViewToPossibleConnections();
    }

    /**
     * Versteckt die Felder / Buttons für den Zwischenhalt <br/>
     * <p>
     * Abhängig vom Provider, beim VRR enthält das Ergebnis der Abfrage mit Zwischenhalt jedoch keine Auskunft
     * über die Preisstufe der Verbindung
     *
     * @param view Layout
     */
    private void hideStopover(View view) {
        //ToDo bei anderen Providern eventuell möglich ein Via anzugeben, beim VRR hingegen ist dann die Preisstufe unbekannt
        text.stopover_view.setVisibility(View.GONE);
        buttons.stopoverButton.setVisibility(View.GONE);
        buttons.clearStopover.setVisibility(View.GONE);
        separator = view.findViewById(R.id.view9);
        separator.setVisibility(View.GONE);
    }

}
