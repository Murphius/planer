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

public class Connection_view_detail extends Activity {

    private TextView date, time_of_arrival, time_of_departure, duration, num_changes, preisstufe;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Detailed_connection_adapter adapter;
    private BootstrapButton button_back, button_accept;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_view_detail);

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

        Intent intent = getIntent();
        String date_value = intent.getStringExtra(Possible_connections_single.EXTRA_DATE);
        String time_of_departure_value = intent.getStringExtra(Possible_connections_single.EXTRA_TIME_OF_DEPARTURE);
        String time_of_arrival_value = intent.getStringExtra(Possible_connections_single.EXTRA_TIME_OF_ARRIVAL);
        int duration_value = intent.getIntExtra(Possible_connections_single.EXTRA_DURATION,0);
        int num_changes_value = intent.getIntExtra(Possible_connections_single.EXTRA_NUM_CHANGES, 0);
        String preisstufe_value = intent.getStringExtra(Possible_connections_single.EXTRA_PREISSTUFE);
        id = intent.getStringExtra(Possible_connections_single.EXTRA_ID);

        date.setText(date_value);
        time_of_departure.setText(time_of_departure_value);
        time_of_arrival.setText(time_of_arrival_value);
        duration.setText("" + duration_value);
        num_changes.setText("" + num_changes_value);
        preisstufe.setText(preisstufe_value);

        button_back.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        button_accept.setBootstrapBrand(new ButtonBootstrapBrandVisible());

        button_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Fahrt soll unter zukuenftige Fahrten angezeigt werden, wenn der Zeitpunkt in der Zukunft liegt
                // -> Wechsel der Ansicht, speichern der Fahrt
            }
        });

        button_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });

        final ArrayList<Detailed_connection_item> items = new ArrayList<>();

        items.add(new Detailed_connection_item("1","2","a","b", "Gleis 3", "Gleis 4", "U35", "Bochum Hustad", R.drawable.ic_underground_train));
        items.add(new Detailed_connection_item("3","4","c","d", "Gleis 5", "Gleis 6", "S 2", "Dortmund HBF", R.drawable.ic_regionaltrain));
        items.add(new Detailed_connection_item("1","2","a","a", "Gleis 3", "Gleis 1", "", "", R.drawable.ic_walk));
        items.add(new Detailed_connection_item("5","7","a","a", "Gleis 3", "Gleis 3", "", "", R.drawable.ic_time));

        System.out.println(items.size());

        adapter = new Detailed_connection_adapter(items);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setFocusable(false);

    }
}
