package com.example.lkjhgf;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.lkjhgf.Color.ButtonBootstrapBrandVisible;

import java.util.ArrayList;

public class Possible_connections_single extends Activity {

    private BootstrapButton earlier_button, edit_button, later_button;
    private TextView date_text, arrival_departure_time, arrival_departure_view, start_text, stopover_text,destination_text;
    private RecyclerView possible_connection_show;

    private boolean is_arrival_time;

    private RecyclerView.Adapter connections_adapter;
    private RecyclerView.LayoutManager connections_layoutManager;
    
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
        possible_connection_show = findViewById(R.id.recyclerView1);
        possible_connection_show.setHasFixedSize(true);
        connections_layoutManager = new LinearLayoutManager(this);

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

        //Anzeigen der einzelnen Verbindungen
        ArrayList<Connection_item> list_of_connections = new ArrayList<>();
        ArrayList<Journey_item> list_of_journey_elements = new ArrayList<>();

        list_of_journey_elements.add(new Journey_item(R.drawable.ic_bus, "SB 27"));
        list_of_journey_elements.add(new Journey_item(R.drawable.ic_tram, "306"));
        list_of_journey_elements.add(new Journey_item(R.drawable.ic_bus, "3"));
        list_of_journey_elements.add(new Journey_item(R.drawable.ic_tram, "2"));
        list_of_journey_elements.add(new Journey_item(R.drawable.ic_bus, "1"));
        list_of_journey_elements.add(new Journey_item(R.drawable.ic_tram, "10"));

        connections_adapter = new Connection_adapter(list_of_connections);
        //TODO hier muessen die Angaben aus Oeffi geholt werden!
        list_of_connections.add(new Connection_item("5", "3", 2, "A", list_of_journey_elements));
        list_of_journey_elements = new ArrayList<>();
        list_of_journey_elements.add(new Journey_item(R.drawable.ic_walk, "5 Minuten"));
        list_of_journey_elements.add(new Journey_item(R.drawable.ic_regionaltrain, "RE 43"));
        list_of_journey_elements.add(new Journey_item(R.drawable.ic_time, "5 Minuten"));
        list_of_journey_elements.add(new Journey_item(R.drawable.ic_underground_train, "U5"));
        list_of_journey_elements.add(new Journey_item(R.drawable.ic_walk, "5 Minuten"));
        list_of_journey_elements.add(new Journey_item(R.drawable.ic_non_regional_traffic, "ICE 647"));
        list_of_journey_elements.add(new Journey_item(R.drawable.ic_taxi, "AST 7"));
        list_of_connections.add(new Connection_item("3", "4", 1, "B", list_of_journey_elements));
        list_of_connections.add(new Connection_item("2", "1", 3, "C", list_of_journey_elements));
        list_of_connections.add(new Connection_item("6", "9", 14, "D", list_of_journey_elements));
        list_of_connections.add(new Connection_item("7", "10", 13, "E", list_of_journey_elements));
        list_of_connections.add(new Connection_item("8", "11", 12, "F", list_of_journey_elements));

        possible_connection_show.setLayoutManager(connections_layoutManager);
        possible_connection_show.setAdapter(connections_adapter);
    }
}
