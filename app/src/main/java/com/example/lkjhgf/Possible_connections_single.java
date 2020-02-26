package com.example.lkjhgf;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.lkjhgf.Color.ButtonBootstrapBrandVisible;

public class Possible_connections_single extends Activity {

    private boolean isArrivalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_possible_connections_single);

        Intent intent = getIntent();

        String date = intent.getStringExtra(Single_route.EXTRA__DATE);
        TextView date_view = findViewById(R.id.date);

        isArrivalTime = intent.getBooleanExtra(Single_route.EXTRA__ISARRIVALTIME, false);
        TextView isArrivalTime_view = findViewById(R.id.arrival_departure_time);

        String time = intent.getStringExtra(Single_route.EXTRA__TIME);
        TextView time_view = findViewById(R.id.time);

        String start = intent.getStringExtra(Single_route.EXTRA__START);
        TextView textView_start = findViewById(R.id.start);

        String stopover = intent.getStringExtra(Single_route.EXTRA__STOPOVER);
        TextView textView_stopover = findViewById(R.id.stopover);

        String destination = intent.getStringExtra(Single_route.EXTRA__DESTINATION);
        TextView textView_destination = findViewById(R.id.destination);

        textView_start.setText(start);
        textView_stopover.setText(stopover);
        textView_destination.setText(destination);
        time_view.setText(time);
        date_view.setText(date);
        if(!isArrivalTime){
            isArrivalTime_view.setText("Abfahrtszeit:");
        }else{
            isArrivalTime_view.setText("Ankunftszeit:");
        }

        ((BootstrapButton) this.findViewById(R.id.fruehere_fahrten_button)).setBootstrapBrand(new ButtonBootstrapBrandVisible());

        ((BootstrapButton) this.findViewById(R.id.spaetere_fahrten_button)).setBootstrapBrand(new ButtonBootstrapBrandVisible());

        ((BootstrapButton) this.findViewById(R.id.fahrt_anpassen_button)).setBootstrapBrand(new ButtonBootstrapBrandVisible());
        this.findViewById(R.id.fahrt_anpassen_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_single_route();
            }
        });

    }

    public void open_single_route(){
        onBackPressed();
    }

}
