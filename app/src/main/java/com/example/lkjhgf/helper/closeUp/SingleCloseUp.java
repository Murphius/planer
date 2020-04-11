package com.example.lkjhgf.helper.closeUp;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.example.lkjhgf.activites.futureTrips.Complete;

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
        textViews.useTicket.setVisibility(View.GONE);
        textViews.ticket.setVisibility(View.GONE);

        //Nur Fahrten, die in der Zukunft liegen, können "gespeichert" werden
        buttons.button_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(trip.getFirstDepartureTime().before(Calendar.getInstance().getTime())){
                    Toast.makeText(activity.getApplicationContext(), "Nur zukünftige Fahrten können gespeichert werden", Toast.LENGTH_SHORT).show();
                }else{
                    onAcceptClicked();
                }
            }
        });
    }

    @Override
    public void onAcceptClicked() {
        Intent newIntent = new Intent(activity, Complete.class);
        super.onAcceptClicked(newIntent);
    }

}
