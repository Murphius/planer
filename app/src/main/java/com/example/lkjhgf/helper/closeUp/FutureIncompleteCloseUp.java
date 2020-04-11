package com.example.lkjhgf.helper.closeUp;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.example.lkjhgf.activites.futureTrips.Incomplete;

import de.schildbach.pte.dto.Trip;

public class FutureIncompleteCloseUp extends MultipleCloseUp {
    public FutureIncompleteCloseUp(Activity activity,
                                   View view,
                                   Trip trip) {
        super(activity, view, trip);
        textViews.ticket.setVisibility(View.VISIBLE);
        textViews.useTicket.setVisibility(View.VISIBLE);
        buttons.button_accept.setVisibility(View.INVISIBLE);
        buttons.button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity.getApplicationContext(), Incomplete.class);
                activity.startActivity(intent);
            }
        });
    }
}
