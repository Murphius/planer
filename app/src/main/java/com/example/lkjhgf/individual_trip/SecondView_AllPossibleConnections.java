package com.example.lkjhgf.individual_trip;

import android.app.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.lkjhgf.Color.ButtonBootstrapBrandVisible;
import com.example.lkjhgf.individual_trip.secondView_service.Connection_adapter;
import com.example.lkjhgf.individual_trip.secondView_service.Connection_item;
import com.example.lkjhgf.individual_trip.secondView_service.secondView_components.Journey_item;
import com.example.lkjhgf.R;
import com.example.lkjhgf.main_menu.Settings;
import com.example.lkjhgf.public_transport.QueryMoreParameter;
import com.example.lkjhgf.public_transport.QueryMoreTask;
import com.example.lkjhgf.public_transport.QueryParameter;
import com.example.lkjhgf.public_transport.QueryTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.VrrProvider;
import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.QueryTripsResult;
import de.schildbach.pte.dto.Trip;
import de.schildbach.pte.dto.TripOptions;

public class SecondView_AllPossibleConnections extends Activity {

    public static String EXTRA_TRIP = "com.example.lkjhgf.EXTRA_TRIP";

    private BootstrapButton earlierButton, editButton, laterButton;
    private TextView dateView, arrival_departure_timeView, arrival_departureView, departure_pointView, stopoverView, destinationView;
    private RecyclerView recyclerView;

    private boolean is_arrival_time;
    private ArrayList<Connection_item> connection_items;
    private Date user_date_time;
    private Location start, destination, stopover;
    private Connection_adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private TripOptions tripOptions;
    private NetworkProvider provider;
    private QueryTripsResult result;

    private void init() {
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

        fillView();

        // Button-Design
        earlierButton.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        laterButton.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        editButton.setBootstrapBrand(new ButtonBootstrapBrandVisible());

        provider = new VrrProvider();
    }

    private void fillView() {
        //Inhalte aus der vorherigen Seite holen und anschliessend in die Textfelder fuellen
        Intent intent = getIntent();

        Calendar user_date = (Calendar) intent.getSerializableExtra(StartView_Form.EXTRA__DATE);
        is_arrival_time = intent.getBooleanExtra(StartView_Form.EXTRA__ISARRIVALTIME, false);
        user_date_time = user_date.getTime();
        start = (Location) intent.getSerializableExtra(StartView_Form.EXTRA__START);
        stopover = (Location) intent.getSerializableExtra(StartView_Form.EXTRA__STOPOVER);
        destination = (Location) intent.getSerializableExtra(StartView_Form.EXTRA__DESTINATION);

        setDateAndTime();

        departure_pointView.setText(start.place + " " + start.name);

        if (stopover != null) {
            stopoverView.setText(stopover.place + " " + stopover.name);
        }

        destinationView.setText(destination.place + " " + destination.name);

        if (!is_arrival_time) {
            arrival_departureView.setText("Abfahrtszeit:");
        } else {
            arrival_departureView.setText("Ankunftszeit:");
        }
    }

    private void setDateAndTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd. MMMM yyyy");
        dateView.setText(dateFormat.format(user_date_time));
        dateFormat = new SimpleDateFormat("HH : mm");
        arrival_departure_timeView.setText(dateFormat.format(user_date_time));
    }

    private void setOnClickListener() {
        //OnClickListener fuer die einzelnen Buttons
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        laterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newConnectionsLater();
            }
        });
        earlierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newConnectionsEarlier();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_possible_connections_single);

        init();
        setOnClickListener();

        createConnectionList();
        buildRecyclerView();
    }

    private void change_view_connection_detail(Connection_item connection) {
        Intent intent = new Intent(this, ThirdView_CloseUp.class);

        intent.putExtra(EXTRA_TRIP, connection.getTrip());

        startActivity(intent);
    }

    private void newConnectionsEarlier() {
        if (result == null) {
            return;
        }

        QueryMoreParameter query = new QueryMoreParameter(result.context, false, provider);

        AsyncTask<QueryMoreParameter, Void, QueryTripsResult> execute = new QueryMoreTask().execute(
                query);

        try {
            QueryTripsResult resultEarlier = execute.get();
            List<Trip> trips = resultEarlier.trips;
            connection_items.clear();
            fillConnectionList(trips);
            // Aktualisierung der Zeit
            user_date_time = trips.get(0).legs.get(0).getDepartureTime();
            setDateAndTime();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }

    private void newConnectionsLater() {
        if (result == null) {
            return;
        }

        QueryMoreParameter query = new QueryMoreParameter(result.context, true, provider);

        AsyncTask<QueryMoreParameter, Void, QueryTripsResult> execute = new QueryMoreTask().execute(
                query);

        try {
            QueryTripsResult resultLater = execute.get();
            List<Trip> trips = resultLater.trips;
            connection_items.clear();
            fillConnectionList(trips);
            List<Trip.Leg> legs = trips.get(trips.size() - 1).legs;
            //Aktualisierung der Zeit
            user_date_time = trips.get(0).legs.get(0).getDepartureTime();
            setDateAndTime();
            user_date_time = legs.get(legs.size() - 1).getArrivalTime();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }

    public void createConnectionList() {
        result = null;

        tripOptions = Settings.getTripOptions(this);

        QueryParameter query = new QueryParameter(start,
                stopover,
                destination,
                user_date_time,
                !is_arrival_time,
                tripOptions, provider);

        AsyncTask<QueryParameter, Void, QueryTripsResult> execute = new QueryTask().execute(query);

        connection_items = new ArrayList<>();
        connection_items.clear();

        try {
            result = execute.get();
            List<Trip> trip = result.trips;
            if (trip != null) {
                fillConnectionList(trip);
            } else {
                Toast.makeText(this, "Keine Fahrten gefunden", Toast.LENGTH_SHORT);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        adapter = new Connection_adapter(connection_items);
    }

    public void buildRecyclerView() {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.set_on_item_click_listener(position -> {
            Connection_item connection = connection_items.get(position);
            change_view_connection_detail(connection);
        });
    }

    private void fillConnectionList(List<Trip> trips) {
        for (Trip trip : trips) {
            ArrayList<Journey_item> list_of_journey_elements = new ArrayList<>();
            if (trip.isTravelable()) {
                List<Trip.Leg> legs = trip.legs;
                for (Trip.Leg l : legs) {
                    int icon = R.drawable.ic_bus;
                    String name = "";
                    if (l instanceof Trip.Public) {
                        Trip.Public p = (Trip.Public) l;
                        name = p.line.label;
                        switch (p.line.product) {
                            case HIGH_SPEED_TRAIN:
                                icon = R.drawable.ic_non_regional_traffic;
                                break;
                            case SUBWAY:
                                icon = R.drawable.ic_underground_train;
                                break;
                            case SUBURBAN_TRAIN:
                                icon = R.drawable.ic_s_bahn;
                                break;
                            case TRAM:
                                icon = R.drawable.ic_tram;
                                break;
                            case CABLECAR:
                                icon = R.drawable.ic_gondola;
                                break;
                            case FERRY:
                                icon = R.drawable.ic_ship;
                                break;
                            case REGIONAL_TRAIN:
                                icon = R.drawable.ic_regionaltrain;
                                break;
                            case BUS:
                                icon = R.drawable.ic_bus;
                                break;
                            case ON_DEMAND:
                                icon = R.drawable.ic_taxi;
                                break;
                            default:
                                icon = R.drawable.ic_android;
                                break;
                        }
                    } else {
                        switch (((Trip.Individual) l).type) {
                            case WALK:
                                icon = R.drawable.ic_walk;
                                break;
                            case TRANSFER:
                                icon = R.drawable.ic_time;
                                break;
                            case CAR:
                                icon = R.drawable.ic_taxi;
                                break;
                            case BIKE:
                                icon = R.drawable.ic_bike;
                                break;
                            default:
                                icon = R.drawable.ic_android;
                                break;
                        }
                        name = ((Trip.Individual) l).min + "min";
                    }
                    list_of_journey_elements.add(new Journey_item(icon, name));
                }
            }
            connection_items.add(new Connection_item(trip, list_of_journey_elements));
        }
    }
}
