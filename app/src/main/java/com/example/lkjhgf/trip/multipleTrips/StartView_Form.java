package com.example.lkjhgf.trip.multipleTrips;

import android.app.Activity;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.form.Form;
import com.example.lkjhgf.helper.form.MultipleTrip;

import de.schildbach.pte.VrrProvider;

public class StartView_Form extends Activity {

    Form form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_single_route);

        ConstraintLayout layout = findViewById(R.id.constraintLayout);

        form = new MultipleTrip(this, layout, new VrrProvider());

        form.setOnClickListener();

        form.setAdapter();
    }
}
