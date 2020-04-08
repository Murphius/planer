package com.example.lkjhgf.helper.closeUp;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.example.lkjhgf.futureTrips.Complete;

import de.schildbach.pte.dto.Trip;

public class FutureSingleCloseUp extends SingleCloseUp {

    public FutureSingleCloseUp(Activity activity,
                               View view,
                               Trip trip) {
        super(activity, view, trip);
        textViews.ticket.setVisibility(View.VISIBLE);
        textViews.useTicket.setVisibility(View.VISIBLE);
        buttons.button_accept.setVisibility(View.INVISIBLE);
        buttons.button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity.getApplicationContext(), Complete.class);
                activity.startActivity(intent);
            }
        });
    }
}
