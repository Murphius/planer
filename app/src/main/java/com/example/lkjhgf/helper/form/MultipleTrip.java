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

import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.dto.Trip;

/**
 * Das Formular für Fahrten, die bei der Optimierung von Fahrtkosten berücksichtigt werden sollen
 */
public class MultipleTrip extends Form {

    private int numTrip;
    private int numAdult, numChildren;

    private TextView titleView;

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
                        int numAdult,
                        int numTrip) {
        // "Normale" Ansicht herstellen
        this(activity, view, provider);

        //Informationen über die #reisender Personen anzeigen
        String setText = numAdult + "";
        this.numAdult = numAdult;
        text.numAdultView.setText(setText);
        setText = numChildren + "";
        this.numChildren = numChildren;
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
        titleView = view.findViewById(R.id.app_name2);
        Intent intent = activity.getIntent();

        // Die Nr. des Tripps
        numTrip = intent.getIntExtra(MainMenu.EXTRA_NUM_TRIP, 1);
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
        intent.putExtra(MainMenu.EXTRA_NUM_TRIP, numTrip);
        intent.putExtra(MainMenu.NUM_ADULT, numAdult);
        intent.putExtra(MainMenu.NUM_CHILDREN, numChildren);
        super.changeViewToPossibleConnections();
    }

}
