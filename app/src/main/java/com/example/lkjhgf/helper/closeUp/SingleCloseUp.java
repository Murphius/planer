package com.example.lkjhgf.helper.closeUp;

import android.app.Activity;
import android.view.View;

import java.util.Calendar;

import de.schildbach.pte.dto.Trip;

public class SingleCloseUp extends CloseUp {

    public SingleCloseUp(Activity activity,
                         View view,
                         Trip trip){
        super(activity, view, trip);
        textViews.numChildrenView.setVisibility(View.GONE);
        textViews.numChildren.setVisibility(View.GONE);
        textViews.numAdultView.setVisibility(View.GONE);
        textViews.numAdult.setVisibility(View.GONE);
        textViews.view.setVisibility(View.GONE);

        //Nur Fahrten, die in der Zukunft liegen, können "gespeichert" werden
        if(trip.getFirstDepartureTime().before(Calendar.getInstance().getTime())){
            buttons.button_accept.setClickable(false);
        }

    }

}
