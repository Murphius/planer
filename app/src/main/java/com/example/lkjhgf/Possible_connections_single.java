package com.example.lkjhgf;

import android.app.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.lkjhgf.Color.ButtonBootstrapBrandVisible;

import java.util.ArrayList;

public class Possible_connections_single extends Activity{

    public static String EXTRA_DATE = "com.example.lkjhgf.EXTRA_DATE";
    public static String EXTRA_TIME_OF_ARRIVAL = "com.example.lkjhgf.EXTRA_TIME_OF_ARRIVAL";
    public static String EXTRA_TIME_OF_DEPARTURE = "com.example.lkjhgf.EXTRA_TIME_OF_DEPARTURE";
    public static String EXTRA_NUM_CHANGES = "com.example.lkjhgf.EXTRA_NUM_CHANGES";
    public static String EXTRA_DURATION = "com.example.lkjhgf.EXTRA_DURATION";
    public static String EXTRA_PREISSTUFE = "com.example.lkjhgf.EXTRA_PREISSTUFE";
    public static String EXTRA_ID = "com.example.lkjhgf.EXTRA_ID";

    private BootstrapButton earlierButton, editButton, laterButton;
    private TextView dateView, arrival_departure_timeView, arrival_departureView, departure_pointView, stopoverView, destinationView;
    private RecyclerView recyclerView;

    private boolean is_arrival_time;
    private ArrayList<Connection_item> connection_items;


    private Connection_adapter adapter;
    private RecyclerView.LayoutManager layoutManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_possible_connections_single);

        // Variablen initialisieren, einfachere Bennenung: Nummer -> Name
        earlierButton = findViewById(R.id.BootstrapButton23);
        editButton = findViewById(R.id.BootstrapButton24);
        laterButton = findViewById(R.id.BootstrapButton25);

        dateView = findViewById(R.id.textView7);
        arrival_departure_timeView = findViewById(R.id.textView8);
        departure_pointView = findViewById(R.id.textView9);
        stopoverView = findViewById(R.id.textView10);
        destinationView = findViewById(R.id.textView11);
        arrival_departureView = findViewById(R.id.textView13);
        recyclerView = findViewById(R.id.recyclerView1);






        //Inhalte aus der vorherigen Seite holen und anschliessend in die Textfelder fuellen
        Intent intent = getIntent();

        String user_date = intent.getStringExtra(Single_route.EXTRA__DATE);
        is_arrival_time = intent.getBooleanExtra(Single_route.EXTRA__ISARRIVALTIME, false);
        String time = intent.getStringExtra(Single_route.EXTRA__TIME);
        String start = intent.getStringExtra(Single_route.EXTRA__START);
        String stopover = intent.getStringExtra(Single_route.EXTRA__STOPOVER);
        String destination = intent.getStringExtra(Single_route.EXTRA__DESTINATION);

        dateView.setText(user_date);
        arrival_departure_timeView.setText(time);
        departure_pointView.setText(start);
        stopoverView.setText(stopover);
        destinationView.setText(destination);

        if(!is_arrival_time){
            arrival_departureView.setText("Abfahrtszeit:");
        }else{
            arrival_departureView.setText("Ankunftszeit:");
        }

        // Button-Design
        earlierButton.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        laterButton.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        editButton.setBootstrapBrand(new ButtonBootstrapBrandVisible());

        //OnClickListener fuer die einzelnen Buttons
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        laterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Parameter der Funktion -> wie viel spaeter die Abfahrt erfolgen soll
                newConnectionsWithLater(5);
            }
        });
        earlierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newConnectionsEarlier(5);
            }
        });


        createConnectionList();
        buildRecyclerView();



    }

    private void change_view_connection_detail(Connection_item connection){
        Intent intent = new Intent(this, Connection_view_detail.class);

        intent.putExtra(EXTRA_DATE, dateView.getText().toString());
        intent.putExtra(EXTRA_TIME_OF_DEPARTURE, connection.get_time_of_departure());
        intent.putExtra(EXTRA_TIME_OF_ARRIVAL, connection.get_time_of_arrival());
        intent.putExtra(EXTRA_DURATION, connection.get_duration());
        intent.putExtra(EXTRA_NUM_CHANGES, connection.get_num_changes());
        intent.putExtra(EXTRA_PREISSTUFE, connection.get_preisstufe());
        intent.putExtra(EXTRA_ID, connection.get_id());

        startActivity(intent);
    }

    private void newConnectionsEarlier(int minutes){
        //TODO Oeffi Abfrage mit frueherer Zeit
        ArrayList<Journey_item> list_of_journey_elements = new ArrayList<>();

        list_of_journey_elements.add(new Journey_item(R.drawable.ic_underground_train, "U11"));
        list_of_journey_elements.add(new Journey_item(R.drawable.ic_walk, "3 Minuten"));

        connection_items.add(0, new Connection_item("5", "3", 2, "A", "abc",list_of_journey_elements));

        list_of_journey_elements = new ArrayList<>();
        list_of_journey_elements.add(new Journey_item(R.drawable.ic_regionaltrain,"RE11"));
        list_of_journey_elements.add(new Journey_item(R.drawable.ic_tram,"3"));

        connection_items.add(0, new Connection_item("10", "9", 1, "B", "sdlkaf",list_of_journey_elements));

        adapter.notifyDataSetChanged();

    }

    private void newConnectionsWithLater(int minutes){
        //TODO Oeffi Abfrage mit spaeterer Zeit
        ArrayList<Journey_item> list_of_journey_elements = new ArrayList<>();

        list_of_journey_elements.add(new Journey_item(R.drawable.ic_bus, "SB 27"));
        list_of_journey_elements.add(new Journey_item(R.drawable.ic_tram, "306"));

        connection_items.add(new Connection_item("5", "3", 2, "A", "abc",list_of_journey_elements));

        list_of_journey_elements = new ArrayList<>();
        list_of_journey_elements.add(new Journey_item(R.drawable.ic_underground_train,"U11"));
        list_of_journey_elements.add(new Journey_item(R.drawable.ic_tram,"3"));

        connection_items.add(new Connection_item("10", "9", 1, "B", "sdlkaf",list_of_journey_elements));

        adapter.notifyDataSetChanged();
    }

    public void createConnectionList(){
        connection_items = new ArrayList<>();
        ArrayList<Journey_item> list_of_journey_elements = new ArrayList<>();

        list_of_journey_elements.add(new Journey_item(R.drawable.ic_bus, "SB 27"));
        list_of_journey_elements.add(new Journey_item(R.drawable.ic_tram, "306"));
        list_of_journey_elements.add(new Journey_item(R.drawable.ic_bus, "3"));
        list_of_journey_elements.add(new Journey_item(R.drawable.ic_tram, "2"));
        list_of_journey_elements.add(new Journey_item(R.drawable.ic_bus, "1"));
        list_of_journey_elements.add(new Journey_item(R.drawable.ic_tram, "10"));

        adapter = new Connection_adapter(connection_items);
        //TODO hier muessen die Angaben aus Oeffi geholt werden!
        // ID -> OEFFI
        connection_items.add(new Connection_item("5", "3", 2, "A", "abc",list_of_journey_elements));
        list_of_journey_elements = new ArrayList<>();
        list_of_journey_elements.add(new Journey_item(R.drawable.ic_walk, "5 Minuten"));
        list_of_journey_elements.add(new Journey_item(R.drawable.ic_regionaltrain, "RE 43"));
        list_of_journey_elements.add(new Journey_item(R.drawable.ic_time, "5 Minuten"));
        list_of_journey_elements.add(new Journey_item(R.drawable.ic_underground_train, "U5"));
        list_of_journey_elements.add(new Journey_item(R.drawable.ic_walk, "5 Minuten"));
        list_of_journey_elements.add(new Journey_item(R.drawable.ic_non_regional_traffic, "ICE 647"));
        list_of_journey_elements.add(new Journey_item(R.drawable.ic_taxi, "AST 7"));
        connection_items.add(new Connection_item("3", "4", 1, "B", "def",list_of_journey_elements));
        connection_items.add(new Connection_item("2", "1", 3, "C", "jkl", list_of_journey_elements));
        connection_items.add(new Connection_item("6", "9", 14, "D", "mno",list_of_journey_elements));
        connection_items.add(new Connection_item("7", "10", 13, "E", "123",list_of_journey_elements));
        connection_items.add(new Connection_item("8", "11", 12, "F", "as", list_of_journey_elements));
    }

    public void buildRecyclerView(){
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.set_on_item_click_listener(new Connection_adapter.On_item_click_listener() {
            @Override
            public void onItemClick(int position) {
                Connection_item connection = connection_items.get(position);
                change_view_connection_detail(connection);
            }
        });
    }
}
