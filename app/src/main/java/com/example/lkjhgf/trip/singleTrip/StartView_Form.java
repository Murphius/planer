package com.example.lkjhgf.trip.singleTrip;

import android.app.Activity;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lkjhgf.helper.form.Form;
import com.example.lkjhgf.helper.form.SingleTrip;
import com.example.lkjhgf.R;

import de.schildbach.pte.VrrProvider;

public class StartView_Form extends Activity {

    Form form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_single_route);

        ConstraintLayout layout = findViewById(R.id.constraintLayout);

        form = new SingleTrip(this, layout, new VrrProvider());

        form.setOnClickListener();
        
        form.setAdapter();
    }

}
