package com.example.lkjhgf;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.lkjhgf.Color.ButtonBootstrapBrandVisible;

public class Possible_connections_single extends Activity {

    private BootstrapButton earlier_button, edit_button, later_button;
    private TextView date_text, arrival_departure_time, arrival_departure_view, start_text, stopover_text,destination_text;
    private boolean is_arrival_time;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_possible_connections_single);

        // Variablen initialisieren, einfachere Bennenung: Nummer -> Name
        earlier_button = findViewById(R.id.BootstrapButton23);
        edit_button = findViewById(R.id.BootstrapButton24);
        later_button = findViewById(R.id.BootstrapButton25);

        date_text = findViewById(R.id.TextView7);
        arrival_departure_time = findViewById(R.id.TextView8);
        start_text = findViewById(R.id.TextView9);
        stopover_text = findViewById(R.id.TextView10);
        destination_text = findViewById(R.id.TextView11);
        arrival_departure_view = findViewById(R.id.TextView13);

        //Inhalte aus der vorherigen Seite holen und anschliessend in die Textfelder fuellen
        Intent intent = getIntent();

        String date = intent.getStringExtra(Single_route.EXTRA__DATE);
        is_arrival_time = intent.getBooleanExtra(Single_route.EXTRA__ISARRIVALTIME, false);
        String time = intent.getStringExtra(Single_route.EXTRA__TIME);
        String start = intent.getStringExtra(Single_route.EXTRA__START);
        String stopover = intent.getStringExtra(Single_route.EXTRA__STOPOVER);
        String destination = intent.getStringExtra(Single_route.EXTRA__DESTINATION);

        date_text.setText(date);
        arrival_departure_time.setText(time);
        start_text.setText(start);
        stopover_text.setText(stopover);
        destination_text.setText(destination);

        if(!is_arrival_time){
            arrival_departure_view.setText("Abfahrtszeit:");
        }else{
            arrival_departure_view.setText("Ankunftszeit:");
        }

        // Button-Design
        earlier_button.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        later_button.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        edit_button.setBootstrapBrand(new ButtonBootstrapBrandVisible());

        //OnClickListener fuer die einzelnen Buttons
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
