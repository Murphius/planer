package com.example.lkjhgf.trip.singleTrip;

import android.app.Activity;

import android.content.Intent;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.service.SinglePossibleConnection;

import de.schildbach.pte.VrrProvider;

public class SecondView_AllPossibleConnections extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_possible_connections_single);

        Intent intent = getIntent();

        ConstraintLayout layout = findViewById(R.id.constraintLayout1);

        new SinglePossibleConnection(this,
                layout,
                intent,
                new VrrProvider());

    }

}






