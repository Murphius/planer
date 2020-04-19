package com.example.lkjhgf.helper.form;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.util.UtilsString;
import com.example.lkjhgf.helper.futureTrip.TripIncomplete;
import com.example.lkjhgf.activites.MainMenu;
import com.example.lkjhgf.activites.multipleTrips.ShowAllPossibleConnections;

import java.util.Calendar;

import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.dto.Trip;

/**
 * Das Formular für Fahrten, die bei der Optimierung von Fahrtkosten berücksichtigt werden sollen
 */
public class MultipleTrip extends Form {

    private int numTrip;
    private int numAdult, numChildren;

    public static String EXTRA_NUM_TRIP = "com.example.lkjhgf.helper.form.EXTRA_NUM_TRIP";
    public static String EXTRA_NUM_ADULT = "com.example.lkjhgf.helper.form.EXTRA_NUM_ADULT";
    public static String EXTRA_NUM_CHILDREN = "com.example.lkjhgf.helper.form.EXTRA_NUM_CHILDREN";

    /**
     * Wenn eine Fahrt editiert oder kopiert werden soll, sollen die zugehörigen Informationen
     * angezeigt werden <br/>
     * <p>
     * Nutzt {@link #MultipleTrip(Activity, View, NetworkProvider)} als Basis für den View
     *
     * @param trip        Trip der editiert oder kopiert werden soll
     * @param numChildren #Kinder
     * @param numAdult    #Erwachsene
     */
    public MultipleTrip(Activity activity,
                        View view,
                        NetworkProvider provider,
                        Trip trip,
                        int numChildren,
                        int numAdult) {
        // "Normale" Ansicht herstellen
        this(activity, view, provider);

        //Informationen über die #reisender Personen anzeigen
        String setText = numAdult + "";
        this.numAdult = numAdult;
        text.numAdultView.setText(setText);
        setText = numChildren + "";
        this.numChildren = numChildren;
        text.numChildrenView.setText(setText);

        //Informationen der Verbindung den Attributen zuweisen
        startLocation = trip.from;
        destinationLocation = trip.to;
        selectedDate.setTime(trip.getFirstDepartureTime());

        // Informationen der Verbindung in den jeweiligen Textfelder anzeigen
        text.date_view.setText(UtilsString.setDate(selectedDate.getTime()));
        text.arrivalDepartureView.setText(UtilsString.setTime(selectedDate.getTime()));
        text.start_view.setText(UtilsString.setLocationName(startLocation));
        text.destination_view.setText(UtilsString.setLocationName(destinationLocation));
    }

    /**
     * Neben den Informationen zu Start, Ziel, Zeitpunkt, werden auch die #reisender Personen
     * sowie die #Fahrt "benätigt"
     *
     * @see Form#Form(Activity, View, NetworkProvider)
     */
    public MultipleTrip(Activity activity,
                        View view,
                        NetworkProvider provider) {
        super(activity, view, provider);
        TextView titleView = view.findViewById(R.id.app_name2);
        Intent intent = activity.getIntent();

        // Die Nr. des Tripps
        int numTrip1 = intent.getIntExtra(MainMenu.EXTRA_NUMBER, 1);
        int numTrip2 = intent.getIntExtra(TripIncomplete.EXTRA_NUM_TRIP, 1);
        int numTrip3 = intent.getIntExtra(TripIncomplete.EXTRA_NUM_TRIP, 1);
        numTrip = Integer.max(numTrip1, numTrip2);
        numTrip = Integer.max(numTrip3, numTrip);
        String titleString = numTrip + ". Fahrt";
        titleView.setText(titleString);
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
        numAdult = 0;
        numChildren = 0;
        String numAdultString = text.numAdultView.getText().toString();
        String numChildrenString = text.numChildrenView.getText().toString();
        if (!numAdultString.isEmpty()) {
            numAdult = Integer.parseInt(numAdultString);
        }
        if (!numChildrenString.isEmpty()) {
            numChildren = Integer.parseInt(numChildrenString);
        }
        if (numAdult + numChildren <= 0) {
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
        intent.putExtra(EXTRA_NUM_TRIP, numTrip);
        intent.putExtra(EXTRA_NUM_ADULT, numAdult);
        intent.putExtra(EXTRA_NUM_CHILDREN, numChildren);
        super.changeViewToPossibleConnections();
    }

    /**
     * Wenn eine Fahrt kopiert wurde, bleiben alle Parameter unverändert, bis auf
     * das Datum. <br/>
     * <p>
     * Das Datum wird default mäßig auf das aktuelle Datum gesetzt. Die entsprechenden Textfelder
     * hingegen werden geleert.
     */
    @Override
    public void copy() {
        selectedDate = Calendar.getInstance();
        text.arrivalDepartureView.setText("");
        text.date_view.setText("");
    }
}
