package com.example.lkjhgf.helper.closeUp;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.service.MultiplePossibleConnection;

import de.schildbach.pte.dto.Trip;

public class MultipleCloseUp extends CloseUp {

    private int numTrip;
    private int numAdult, numChildren;

    public MultipleCloseUp(Activity activity,
                    View view,
                    Trip trip) {
        super(activity, view, trip);

        Intent intent = activity.getIntent();
        numTrip = intent.getIntExtra(MultiplePossibleConnection.EXTRA_NUM_TRIP, 1);
        numAdult = intent.getIntExtra(MultiplePossibleConnection.EXTRA_NUM_ADULT, 0);
        numChildren = intent.getIntExtra(MultiplePossibleConnection.EXTRA_NUM_CHILDREN, 0);

        String setText = numTrip + ". Fahrt \n Detaillierte Fahrt";

        TextView viewTitle = view.findViewById(R.id.textView);
        viewTitle.setText(setText);

        setText = numAdult +"";
        textViews.numAdult.setText(setText);

        setText = numChildren + "";
        textViews.numChildren.setText(setText);
    }
}
