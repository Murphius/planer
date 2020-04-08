package com.example.lkjhgf.trip.multipleTrips;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.form.Form;
import com.example.lkjhgf.helper.form.MultipleTrip;

import de.schildbach.pte.VrrProvider;
import de.schildbach.pte.dto.Trip;

import static com.example.lkjhgf.helper.futureTrip.MyTrip.EXTRA_NUM_ADULT;
import static com.example.lkjhgf.helper.futureTrip.MyTrip.EXTRA_NUM_CHILDREN;
import static com.example.lkjhgf.helper.futureTrip.MyTrip.EXTRA_TRIP;

public class EditMultipleTrip extends Activity {

    Form form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_single_route);

        ConstraintLayout layout = findViewById(R.id.constraintLayout);

        Intent intent = getIntent();

        Trip trip = (Trip) intent.getSerializableExtra(EXTRA_TRIP);
        int numChildren = intent.getIntExtra(EXTRA_NUM_CHILDREN, 0);
        int numAdult = intent.getIntExtra(EXTRA_NUM_ADULT, 0);

        form = new MultipleTrip(this, layout, new VrrProvider(), trip, numChildren, numAdult);

        form.setOnClickListener();

        form.setAdapter();
    }
}
