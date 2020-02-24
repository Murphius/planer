package com.example.lkjhgf;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.lkjhgf.Color.ButtonBootstrapBrandVisible;

import org.w3c.dom.Text;

public class Possible_connections extends Activity {

    public static final String EXTRA__DATE = "com.example.lkjhgf.EXTRA_DATE";
    public static final String EXTRA__ISARRIVALTIME = "com.example.lkjhgf.ISARRIVALTIME";
    public static final String EXTRA__TIME = "com.example.lkjhgf.EXTRA_TIME";
    public static final String EXTRA__START = "com.example.lkjhgf.EXTRA_START";
    public static final String EXTRA__STOPOVER = "com.example.lkjhgf.EXTRA_STOPOVER";
    public static final String EXTRA__DESTINATION = "com.example.lkjhgf.EXTRA_DESTINATION";

    private boolean isArrivalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_possible_connections);

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
        Intent intent = new Intent(this, Single_route.class);

        TextView date_text = this.findViewById(R.id.date);
        String date = date_text.getText().toString();

        TextView time_text = this.findViewById(R.id.time);
        String time = time_text.getText().toString();

        TextView start_text = this.findViewById(R.id.start);
        String start = start_text.getText().toString();

        TextView stopover_text = this.findViewById(R.id.stopover);
        String stopover = stopover_text.getText().toString();

        TextView destination_text = this.findViewById(R.id.destination);
        String destination = destination_text.getText().toString();

        intent.putExtra(EXTRA__DATE, date);
        intent.putExtra(EXTRA__ISARRIVALTIME,isArrivalTime);
        intent.putExtra(EXTRA__TIME,time);
        intent.putExtra(EXTRA__START,start);
        intent.putExtra(EXTRA__STOPOVER, stopover);
        intent.putExtra(EXTRA__DESTINATION,destination);

        startActivity(intent);
        finish();
    }

}
