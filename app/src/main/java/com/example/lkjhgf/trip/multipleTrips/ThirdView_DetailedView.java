package com.example.lkjhgf.trip.multipleTrips;

import android.app.Activity;
import android.os.Bundle;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.closeUp.MultipleCloseUp;
import com.example.lkjhgf.helper.service.MultiplePossibleConnection;

import de.schildbach.pte.dto.Trip;

public class ThirdView_DetailedView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_view_detail);

        Trip trip = (Trip) getIntent().getSerializableExtra(MultiplePossibleConnection.EXTRA_ITEM);

        new MultipleCloseUp(this, findViewById(R.id.constraintLayout2), trip);
    }

}
