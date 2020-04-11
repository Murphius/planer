package com.example.lkjhgf.helper.closeUp;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.example.lkjhgf.R;
import com.example.lkjhgf.activites.futureTrips.Incomplete;
import com.example.lkjhgf.helper.service.MultiplePossibleConnections;

import de.schildbach.pte.dto.Trip;

public class MultipleCloseUp extends CloseUp {

    public static String EXTRA_NUM_ADULT = "com.example.lkjhgf.helper.closeUp.EXTRA_NUM_ADULT";
    public static String EXTRA_NUM_CHILDREN = "com.example.lkjhgf.helper.closeUp.EXTRA_NUM_CHILDREN";
    public static String EXTRA_NUM_TRIP = "com.example.lkjhgf.helper.closeUp.EXTRA_NUM_TRIP";

    private int numTrip;
    private int numAdult, numChildren;

    public MultipleCloseUp(Activity activity,
                    View view,
                    Trip trip) {
        super(activity, view, trip);

        Intent intent = activity.getIntent();
        numTrip = intent.getIntExtra(MultiplePossibleConnections.EXTRA_NUM_TRIP, 1);
        numAdult = intent.getIntExtra(MultiplePossibleConnections.EXTRA_NUM_ADULT, 0);
        numChildren = intent.getIntExtra(MultiplePossibleConnections.EXTRA_NUM_CHILDREN, 0);

        String setText = numTrip + ". Fahrt \n Detaillierte Fahrt";

        TextView viewTitle = view.findViewById(R.id.textView);
        viewTitle.setText(setText);

        setText = numAdult +"";
        textViews.numAdult.setText(setText);

        setText = numChildren + "";
        textViews.numChildren.setText(setText);
    }

    @Override
    public void onAcceptClicked() {
        Intent newIntent = new Intent(activity, Incomplete.class);
        newIntent.putExtra(EXTRA_NUM_ADULT, numAdult);
        newIntent.putExtra(EXTRA_NUM_CHILDREN, numChildren);
        newIntent.putExtra(EXTRA_NUM_TRIP, numTrip);
        super.onAcceptClicked(newIntent);
    }


}
