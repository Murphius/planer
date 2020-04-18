package com.example.lkjhgf.helper.form;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.UtilsString;
import com.example.lkjhgf.helper.futureTrip.TripIncomplete;
import com.example.lkjhgf.activites.MainMenu;
import com.example.lkjhgf.activites.multipleTrips.ShowAllPossibleConnections;

import java.util.Calendar;

import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.dto.Trip;

public class MultipleTrip extends Form {

    private int numTrip;
    private int numAdult, numChildren;

    public static String EXTRA_NUM_TRIP = "com.example.lkjhgf.helper.form.EXTRA_NUM_TRIP";
    public static String EXTRA_NUM_ADULT = "com.example.lkjhgf.helper.form.EXTRA_NUM_ADULT";
    public static String EXTRA_NUM_CHILDREN = "com.example.lkjhgf.helper.form.EXTRA_NUM_CHILDREN";

    public MultipleTrip(Activity activity,
                        View view,
                        NetworkProvider provider,
                        Trip trip,
                        int numChildren,
                        int numAdult) {
        this(activity, view, provider);
        String setText = numAdult + "";
        this.numAdult = numAdult;
        text.numAdultView.setText(setText);
        setText = numChildren + "";
        this.numChildren = numChildren;
        text.numChildrenView.setText(setText);
        selectedDate.setTime(trip.getFirstDepartureTime());
        text.date_view.setText(UtilsString.setDate(trip.getFirstDepartureTime()));
        text.arrival_departure_view.setText(UtilsString.setTime(trip.getFirstDepartureTime()));
        startLocation = trip.from;
        destinationLocation = trip.to;
        text.start_view.setText(UtilsString.setLocationName(startLocation));
        text.destination_view.setText(UtilsString.setLocationName(destinationLocation));

    }

    public MultipleTrip(Activity activity,
                        View view,
                        NetworkProvider provider) {
        super(activity, view, provider);
        TextView titleView = view.findViewById(R.id.app_name2);
        Intent intent = activity.getIntent();
        int numTrip1 = intent.getIntExtra(MainMenu.EXTRA_NUMBER, 1);
        int numTrip2 = intent.getIntExtra(TripIncomplete.EXTRA_NUM_TRIP, 1);
        int numTrip3 = intent.getIntExtra(TripIncomplete.EXTRA_NUM_TRIP, 1);
        numTrip = Integer.max(numTrip1, numTrip2);
        numTrip = Integer.max(numTrip3, numTrip);
        String titleString = numTrip + ". Fahrt";
        titleView.setText(titleString);
    }

    public boolean checkFormComplete() {
        System.out.println("MULTIPLE TRIP CHECK COMPLETE");
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

    void changeViewToPossibleConnections() {
        intent = new Intent(context, ShowAllPossibleConnections.class);
        intent.putExtra(EXTRA_NUM_TRIP, numTrip);
        intent.putExtra(EXTRA_NUM_ADULT, numAdult);
        intent.putExtra(EXTRA_NUM_CHILDREN, numChildren);
        super.changeViewToPossibleConnections();
    }

    @Override
    public void copy() {
        selectedDate = Calendar.getInstance();
        text.arrival_departure_view.setText("");
        text.date_view.setText("");
    }
}
