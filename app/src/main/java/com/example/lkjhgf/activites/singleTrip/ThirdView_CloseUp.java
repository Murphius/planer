package com.example.lkjhgf.activites.singleTrip;

import android.app.Activity;
import android.os.Bundle;

import com.example.lkjhgf.helper.closeUp.SingleCloseUp;
import com.example.lkjhgf.helper.service.SinglePossibleConnection;
import com.example.lkjhgf.R;

import de.schildbach.pte.dto.Trip;

public class ThirdView_CloseUp extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_view_detail);

        Trip trip = (Trip) getIntent().getSerializableExtra(SinglePossibleConnection.EXTRA_TRIP);

        new SingleCloseUp(this, findViewById(R.id.constraintLayout2), trip);
    }





}
