package com.example.lkjhgf.trip.singleTrip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.form.Form;
import com.example.lkjhgf.helper.form.SingleTrip;
import com.example.lkjhgf.main_menu.Main_activity;

import de.schildbach.pte.VrrProvider;
import de.schildbach.pte.dto.Trip;

import static com.example.lkjhgf.helper.futureTrip.MyTrip.EXTRA_TRIP;

public class EditSingleTrip extends Activity {

    Form form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_single_route);

        ConstraintLayout layout = findViewById(R.id.constraintLayout);

        Intent intent = getIntent();

        Trip trip = (Trip) intent.getSerializableExtra(EXTRA_TRIP);

        form = new SingleTrip(this, layout, new VrrProvider(), trip);

        form.setOnClickListener();

        form.setAdapter();
    }



}
