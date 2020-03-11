package com.example.lkjhgf.individual_trip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.lkjhgf.Color.ButtonBootstrapBrandVisible;
import com.example.lkjhgf.individual_trip.thirdView_DetailedView.components.Stopover_item;
import com.example.lkjhgf.individual_trip.thirdView_DetailedView.CloseUp_adapter;
import com.example.lkjhgf.individual_trip.thirdView_DetailedView.CloseUp_item;
import com.example.lkjhgf.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.schildbach.pte.dto.Stop;
import de.schildbach.pte.dto.Trip;

public class ThirdView_CloseUp extends Activity {

    private TextView date, time_of_arrival, time_of_departure, duration, num_changes, preisstufe;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CloseUp_adapter adapter;
    private BootstrapButton button_back, button_accept;

    private Trip trip;

    private long duration_hours, duration_minutes;



    private ArrayList<CloseUp_item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_view_detail);

        init();
        fillView();
        setOnClickListener();
        createConnectionList();

    }

    private void init() {
        // Variablen initialisieren, einfachere Bennenung: Nummer -> Name
        date = findViewById(R.id.textView56);
        time_of_departure = findViewById(R.id.textView6);
        time_of_arrival = findViewById(R.id.textView48);
        duration = findViewById(R.id.textView50);
        num_changes = findViewById(R.id.textView52);
        preisstufe = findViewById(R.id.textView54);
        button_accept = findViewById(R.id.BootstrapButton31);
        button_back = findViewById(R.id.BootstrapButton30);

        recyclerView = findViewById(R.id.recyclerView3);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);

        button_back.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        button_accept.setBootstrapBrand(new ButtonBootstrapBrandVisible());
    }

    private void fillView() {
        Intent intent = getIntent();

        trip = (Trip) intent.getSerializableExtra(SecondView_AllPossibleConnections.EXTRA_TRIP);

        num_changes.setText(trip.getNumChanges() + "");
        if(trip.fares != null){
            preisstufe.setText(trip.fares.get(0).units);
        }else{
            preisstufe.setText("?");
        }

        duration_hours = TimeUnit.HOURS.convert(trip.getDuration(), TimeUnit.MILLISECONDS) % 24;
        duration_minutes = TimeUnit.MINUTES.convert(trip.getDuration(), TimeUnit.MILLISECONDS) % 60;

        duration.setText(getDuration_string());

        setDateAndTime();

    }

    private void setOnClickListener() {
        button_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Fahrt soll unter zukuenftige Fahrten angezeigt werden, wenn der Zeitpunkt in der Zukunft liegt
                // -> Wechsel der Ansicht, speichern der Fahrt
            }
        });

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void createConnectionList() {
        items = new ArrayList<>();
        fillDetailedConnectionList();

        adapter = new CloseUp_adapter(items);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new CloseUp_adapter.OnItemClickListener(){
            @Override
            public void onShowDetails(int position){
                items.get(position).setShowDetails();
                adapter.notifyDataSetChanged();
            }
        });
        recyclerView.setFocusable(false);
    }


    private void fillDetailedConnectionList() {
        final List<Trip.Leg> legs = trip.legs;

        for (Trip.Leg l : legs) {

            DateFormat dateFormat = new SimpleDateFormat("HH : mm");
            String departureTime = dateFormat.format(l.getDepartureTime());
            String arrivalTime = dateFormat.format(l.getArrivalTime());
            String departureLocation = l.departure.place;
            String arrivalLocation = l.arrival.place;

            if (l instanceof Trip.Public) {
                Trip.Public publicTrip = (Trip.Public) l;
                ArrayList<Stopover_item> stopovers = fillDetailsList(publicTrip);
                String start_platform = "Gleis";
                String destination_platform = "Gleis";
                if(publicTrip.arrivalStop.plannedArrivalPosition != null){
                    start_platform += " " + publicTrip.departureStop.plannedDeparturePosition.toString();
                }
                if(publicTrip.arrivalStop.plannedArrivalPosition != null){
                    destination_platform += " " + publicTrip.arrivalStop.plannedArrivalPosition.toString();
                }
                CloseUp_item connection_item = new CloseUp_item(
                        departureTime,
                        arrivalTime,
                        departureLocation + " "  + publicTrip.departureStop.location.name ,
                        arrivalLocation + " "  + publicTrip.arrivalStop.location.name,
                        start_platform,
                        destination_platform,
                        "" +publicTrip.line.label,
                        publicTrip.destination.name,
                        iconPublic(publicTrip),
                        stopovers);
                items.add(connection_item);
            } else {
                Trip.Individual individualTrip = (Trip.Individual) l;
                ArrayList<Stopover_item> stopovers = fillDetailsList(individualTrip);
                CloseUp_item connection_item = new CloseUp_item(
                        departureTime,
                        arrivalTime,
                        departureLocation,
                        arrivalLocation,
                        individualTrip.departure.name,
                        individualTrip.arrival.name,
                        individualTrip.min + "min",
                        "",
                        iconIndividual(individualTrip),
                        stopovers
                        );
                items.add(connection_item);
            }
        }
    }

    private ArrayList<Stopover_item> fillDetailsList(Trip.Individual individualTrip){
        ArrayList<Stopover_item> stopovers = new ArrayList<>();
        stopovers.add(new Stopover_item(individualTrip.distance + "", " "));
        return stopovers;
    }

    private ArrayList<Stopover_item> fillDetailsList(Trip.Public publicTrip){
        ArrayList<Stopover_item> stopovers = new ArrayList<>();
        List<Stop> stops = publicTrip.intermediateStops;
        for(Stop stop : stops){
            Date arrivalTime = stop.plannedArrivalTime;
            DateFormat dateFormat = new SimpleDateFormat("HH : mm");
            String nameOfStop = stop.location.place + ", " + stop.location.name;
            if(arrivalTime != null){
                stopovers.add(new Stopover_item(dateFormat.format(arrivalTime),nameOfStop ));
            }else {
                stopovers.add(new Stopover_item(" ", nameOfStop));
            }

        }
        return stopovers;
    }

    private int iconPublic(Trip.Public publicTrip) {
        switch (publicTrip.line.product) {
            case HIGH_SPEED_TRAIN:
                return R.drawable.ic_non_regional_traffic;
            case SUBWAY:
                return R.drawable.ic_underground_train;
            case SUBURBAN_TRAIN:
                return R.drawable.ic_s_bahn;
            case TRAM:
                return R.drawable.ic_tram;
            case CABLECAR:
                return R.drawable.ic_gondola;
            case FERRY:
                return R.drawable.ic_ship;
            case REGIONAL_TRAIN:
                return R.drawable.ic_regionaltrain;
            case BUS:
                return R.drawable.ic_bus;
            case ON_DEMAND:
                return R.drawable.ic_taxi;
            default:
                return R.drawable.ic_android;
        }
    }

    private int iconIndividual(Trip.Individual individualTrip) {
        switch (individualTrip.type) {
            case WALK:
                return R.drawable.ic_walk;
            case TRANSFER:
                return R.drawable.ic_time;
            case CAR:
                return R.drawable.ic_taxi;
            case BIKE:
                return R.drawable.ic_bike;
            default:
                return R.drawable.ic_android;
        }
    }

    private void setDateAndTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd. MMMM yyyy");
        date.setText(dateFormat.format(trip.getFirstDepartureTime()));
        dateFormat = new SimpleDateFormat("HH : mm");
        time_of_arrival.setText(dateFormat.format(trip.getLastArrivalTime()));
        time_of_departure.setText(dateFormat.format(trip.getFirstDepartureTime()));
    }

    private String getDuration_string() {
        String result = "";
        if (duration_hours >= 1) {
            result += duration_hours + "h ";
        }
        if (duration_minutes < 1) {
            result += "00";
        } else if (duration_minutes < 10 & duration_hours >= 1) {
            result += "0" + duration_minutes;
        } else if (duration_minutes < 10) {
            result += duration_minutes + "min";
        } else {
            result += duration_minutes;
        }

        return result;
    }

}
