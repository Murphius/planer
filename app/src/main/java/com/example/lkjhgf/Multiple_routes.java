package com.example.lkjhgf;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapText;
import com.example.lkjhgf.Color.ButtonBootstrapBrandInvisible;
import com.example.lkjhgf.Color.ButtonBootstrapBrandVisible;

public class Multiple_routes extends Activity {

    //Konstanten fuer Intent
    public static final String EXTRA__DATE = "com.example.lkjhgf.EXTRA_DATE";
    public static final String EXTRA__ISARRIVALTIME = "com.example.lkjhgf.ISARRIVALTIME";
    public static final String EXTRA__TIME = "com.example.lkjhgf.EXTRA_TIME";
    public static final String EXTRA__START = "com.example.lkjhgf.EXTRA_START";
    public static final String EXTRA__STOPOVER = "com.example.lkjhgf.EXTRA_STOPOVER";
    public static final String EXTRA__DESTINATION = "com.example.lkjhgf.EXTRA_DESTINATION";
    public static final String EXTRA_NUM_CHILDREN = "com.example.lkjhgf.EXTRA_NUM_CHILDREN";
    public static final String EXTRA_NUM_ADULTS = "com.example.lkjhgf.EXTRA_NUM_ADULTS";

    private boolean is_arrival_time;
    private BootstrapButton date_button, arrival_departure_button, start_button, stopover_button, destination_button, back_button, settings_button, forward_button;
    private TextView activity_name, date_text, arrival_departure_text, start_text, stopover_text, destination_text, num_passenger_children_text, num_passenger_adult_text;
    private  int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_routes);

        // Variablen initialisieren, einfachere Bennenung: Nummer -> Name
        date_button = findViewById(R.id.BootstrapButton15);
        arrival_departure_button = findViewById(R.id.BootstrapButton16);
        start_button = findViewById(R.id.BootstrapButton17);
        stopover_button = findViewById(R.id.BootstrapButton18);
        destination_button = findViewById(R.id.BootstrapButton19);
        back_button = findViewById(R.id.BootstrapButton20);
        settings_button = findViewById(R.id.BootstrapButton21);
        forward_button = findViewById(R.id.BootstrapButton22);

        activity_name = findViewById(R.id.TextView1);

        date_text = findViewById(R.id.EditText6);
        arrival_departure_text = findViewById(R.id.EditText7);
        start_text = findViewById(R.id.EditText8);
        stopover_text = findViewById(R.id.EditText9);
        destination_text = findViewById(R.id.EditText10);
        num_passenger_adult_text = findViewById(R.id.EditText11);
        num_passenger_children_text = findViewById(R.id.EditText12);

        date_button.setBootstrapBrand(new ButtonBootstrapBrandInvisible());
        arrival_departure_button.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        start_button.setBootstrapBrand(new ButtonBootstrapBrandInvisible());
        stopover_button.setBootstrapBrand(new ButtonBootstrapBrandInvisible());
        destination_button.setBootstrapBrand(new ButtonBootstrapBrandInvisible());
        back_button.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        settings_button.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        forward_button.setBootstrapBrand(new ButtonBootstrapBrandVisible());

        //Inhalt aus Intent holen
        count = getIntent().getIntExtra("Count", 1);
        activity_name.setText(count + ". Fahrt");


        //OnClickListener
        arrival_departure_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_arrival_time = !is_arrival_time;
                set_arrival_departure_button();
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

       settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_view_to_settings();
            }
        });

       forward_button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               change_view_to_possible_connections();
           }
       });

    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    public void change_view_to_settings(){
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    private void set_arrival_departure_button(){
        Context context = arrival_departure_button.getContext();
        BootstrapText.Builder builder = new BootstrapText.Builder(context);

        if(is_arrival_time){
            builder.addText("Ankunftszeit:");
        }else{
            builder.addText("Abfahrtszeit");
        }

        arrival_departure_button.setBootstrapText(builder.build());
    }

    public void change_view_to_possible_connections(){
        // Nutzereingaben holen
        String date = date_text.getText().toString();
        String arrival_departure_time = arrival_departure_text.getText().toString();
        String start = start_text.getText().toString();
        String stopover = stopover_text.getText().toString();
        String destination = destination_text.getText().toString();
        int num_adult = Integer.parseInt(num_passenger_adult_text.getText().toString());
        int num_children = Integer.parseInt(num_passenger_children_text.getText().toString());

        Intent intent = new Intent(this, Possible_connections_single.class);

        //Nutzereingaben an die naechste Ansicht weiterleiten
        intent.putExtra(EXTRA__DATE, date);
        intent.putExtra(EXTRA__ISARRIVALTIME,is_arrival_time);
        intent.putExtra(EXTRA__TIME,arrival_departure_time);
        intent.putExtra(EXTRA__START,start);
        intent.putExtra(EXTRA__STOPOVER, stopover);
        intent.putExtra(EXTRA__DESTINATION,destination);
        intent.putExtra(EXTRA_NUM_CHILDREN,num_children);
        intent.putExtra(EXTRA_NUM_ADULTS,num_adult);

        startActivity(intent);
    }
}
