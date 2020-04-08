package com.example.lkjhgf.futureTrips.closeUp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.closeUp.FutureSingleCloseUp;

import de.schildbach.pte.dto.Trip;

import static com.example.lkjhgf.helper.futureTrip.MyTrip.EXTRA_TRIP;

public class CompleteCloseUp extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_connection_view_detail);

        Intent intent = getIntent();

        Trip trip = (Trip) intent.getSerializableExtra(EXTRA_TRIP);

        new FutureSingleCloseUp(this, findViewById(R.id.constraintLayout2), trip);
    }
    /*
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), Complete.class);
        startActivity(intent);
    }*/
}
