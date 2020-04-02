package com.example.lkjhgf.helper.form;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.futureTrip.TripIncomplete;
import com.example.lkjhgf.main_menu.Main_activity;
import com.example.lkjhgf.trip.multipleTrips.SecondView_AllPossibleConnections;

import java.util.Calendar;

import de.schildbach.pte.NetworkProvider;

public class MultipleTrip extends Form {

    private int numTrip;
    private int numAdult, numChildren;

    public static String EXTRA_NUM_TRIP = "com.example.lkjhgf.helper.form.EXTRA_NUM_TRIP";
    public static String EXTRA_NUM_ADULT = "com.example.lkjhgf.helper.form.EXTRA_NUM_ADULT";
    public static String EXTRA_NUM_CHILDREN = "com.example.lkjhgf.helper.form.EXTRA_NUM_CHILDREN";

    public MultipleTrip(Activity activity,
                        View view,
                        NetworkProvider provider) {
        super(activity, view, provider);
        TextView titleView = view.findViewById(R.id.app_name2);
        Intent intent = activity.getIntent();
        int numTrip1 = intent.getIntExtra(Main_activity.EXTRA_NUMBER, 1);
        int numTrip2 = intent.getIntExtra(TripIncomplete.EXTRA_NUM_TRIP, 1);
        numTrip = Integer.max(numTrip1, numTrip2);

        String titleString = numTrip + ". Fahrt";
        titleView.setText(titleString);
    }

    public boolean checkFormComplete(){
        System.out.println("MULTIPLE TRIP CHECK COMPLETE");
        if(!super.checkFormComplete()){
            return false;
        }
        if(Calendar.getInstance().getTime().after(selectedDate.getTime())){
            Toast.makeText(context, "Bitte einen Zeitpunkt in der Zukunft auswählen", Toast.LENGTH_SHORT).show();
            return false;
        }
        numAdult = 0;
        numChildren = 0;
        String numAdultString = text.numAdultView.getText().toString();
        String numChildrenString = text.numChildrenView.getText().toString();
        if(!numAdultString.isEmpty()){
            numAdult = Integer.parseInt(numAdultString);
        }
        if(!numChildrenString.isEmpty()){
            numChildren = Integer.parseInt(numChildrenString);
        }
        if(numAdult + numChildren <= 0){
            Toast.makeText(context,"Die Personenanzahl muss mindestens eins sein", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    void changeViewToPossibleConnections(){
        intent = new Intent(context, SecondView_AllPossibleConnections.class);
        intent.putExtra(EXTRA_NUM_TRIP,numTrip);
        intent.putExtra(EXTRA_NUM_ADULT, numAdult);
        intent.putExtra(EXTRA_NUM_CHILDREN, numChildren);
        super.changeViewToPossibleConnections();
    }
}
