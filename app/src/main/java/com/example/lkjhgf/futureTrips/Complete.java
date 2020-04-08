package com.example.lkjhgf.futureTrips;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.lkjhgf.R;
import com.example.lkjhgf.futureTrips.recyclerView.TripItem;
import com.example.lkjhgf.helper.futureTrip.MyTrip;
import com.example.lkjhgf.helper.futureTrip.TripComplete;
import com.example.lkjhgf.main_menu.Main_activity;

import de.schildbach.pte.dto.Trip;

import static com.example.lkjhgf.helper.closeUp.CloseUp.EXTRA_TRIP;

public class Complete extends Activity {

    private MyTrip myTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.future_trips);

        View view = findViewById(R.id.constraintLayout3);

        Intent intent = getIntent();
        Trip trip = (Trip) intent.getSerializableExtra(EXTRA_TRIP);

        if(trip != null){
            TripItem newTripItem = new TripItem(trip, true);
            myTrip = new TripComplete(this, view, newTripItem);
        }else{
            myTrip = new TripComplete(this, view, null);
        }
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this.getApplicationContext(), Main_activity.class);
        myTrip.saveData();
        finishAffinity();
        startActivity(intent);
    }

    @Override
    public void onResume(){
        myTrip.saveData();
        super.onResume();
    }

}
