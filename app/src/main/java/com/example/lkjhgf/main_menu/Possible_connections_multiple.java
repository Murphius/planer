package com.example.lkjhgf.main_menu;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.lkjhgf.Color.ButtonBootstrapBrandVisible;
import com.example.lkjhgf.R;
import com.example.lkjhgf.main_menu.Multiple_routes;


public class Possible_connections_multiple extends Activity {

    private BootstrapButton earlier_button, edit_button, later_button;

    private TextView date_text, arrival_departure_text, arrival_departure_view, start_text, stopover_text, destination_text, num_adult_text, num_children_text;

    private boolean is_arrival_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_possible_connections_multiple);

        //Variablen initialisieren, einfachere Bennenung: Nummer -> Name
        earlier_button = findViewById(R.id.BootstrapButton26);
        edit_button = findViewById(R.id.BootstrapButton27);
        later_button = findViewById(R.id.BootstrapButton28);

        date_text = findViewById(R.id.textView17);
        arrival_departure_text = findViewById(R.id.textView18);
        start_text = findViewById(R.id.textView19);
        stopover_text = findViewById(R.id.textView20);
        destination_text = findViewById(R.id.textView21);
        num_adult_text = findViewById(R.id.textView22);
        num_children_text = findViewById(R.id.textView23);
        arrival_departure_view = findViewById(R.id.textView25);

        //Button Layout
        earlier_button.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        edit_button.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        later_button.setBootstrapBrand(new ButtonBootstrapBrandVisible());

        //Inhalt aus der vorherigen Ansicht holen
        Intent intent = getIntent();

        String date = intent.getStringExtra(Multiple_routes.EXTRA__DATE);
        String time = intent.getStringExtra(Multiple_routes.EXTRA__TIME);
        is_arrival_time = intent.getBooleanExtra(Multiple_routes.EXTRA__ISARRIVALTIME,false);
        String start = intent.getStringExtra(Multiple_routes.EXTRA__START);
        String stopover = intent.getStringExtra(Multiple_routes.EXTRA__STOPOVER);
        String destination = intent.getStringExtra(Multiple_routes.EXTRA__DESTINATION);
        int num_adults = intent.getIntExtra(Multiple_routes.EXTRA_NUM_ADULTS, 0);
        int num_children = intent.getIntExtra(Multiple_routes.EXTRA_NUM_CHILDREN, 0);

        date_text.setText(date);
        arrival_departure_text.setText(time);
        start_text.setText(start);
        stopover_text.setText(stopover);
        destination_text.setText(destination);
        num_adult_text.setText("" + num_adults);
        num_children_text.setText("" + num_children);
        if(is_arrival_time){
            arrival_departure_view.setText("Ankunftszeit:");
        }else{
            arrival_departure_view.setText("Abfahrtszeit:");
        }


        //OnClickListener fuer die einzelnen Buttons
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
}
