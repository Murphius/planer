package com.example.lkjhgf.futureTrips;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.lkjhgf.R;
import com.example.lkjhgf.futureTrips.recyclerView.TripItem;
import com.example.lkjhgf.helper.futureTrip.MyTrip;
import com.example.lkjhgf.helper.futureTrip.TripIncomplete;
import com.example.lkjhgf.main_menu.Main_activity;

import de.schildbach.pte.dto.Trip;

import static com.example.lkjhgf.helper.closeUp.CloseUp.EXTRA_TRIP;
import static com.example.lkjhgf.helper.closeUp.MultipleCloseUp.EXTRA_NUM_ADULT;
import static com.example.lkjhgf.helper.closeUp.MultipleCloseUp.EXTRA_NUM_CHILDREN;

public class Incomplete extends Activity {

    private MyTrip myTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.future_trips);

        View view = findViewById(R.id.constraintLayout3);

        Intent intent = getIntent();
        Trip trip = (Trip) intent.getSerializableExtra(EXTRA_TRIP);
        int numAdult = intent.getIntExtra(EXTRA_NUM_ADULT,0);
        int numChildren = intent.getIntExtra(EXTRA_NUM_CHILDREN, 0);

        TripItem newTripItem = new TripItem(trip, false, numAdult, numChildren);

        myTrip = new TripIncomplete(this, view, newTripItem);
    }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Beim Abbrechen werden alle bisher geplanten Fahrten verworfen \n Wirklich abbrechen?");
        builder.setCancelable(false);
        builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), Main_activity.class);
                finishAffinity();
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }



}
