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

public class Single_route extends Activity {

    // Konstanten fuer Intent
    public static final String EXTRA__DATE = "com.example.lkjhgf.EXTRA_DATE";
    public static final String EXTRA__ISARRIVALTIME = "com.example.lkjhgf.ISARRIVALTIME";
    public static final String EXTRA__TIME = "com.example.lkjhgf.EXTRA_TIME";
    public static final String EXTRA__START = "com.example.lkjhgf.EXTRA_START";
    public static final String EXTRA__STOPOVER = "com.example.lkjhgf.EXTRA_STOPOVER";
    public static final String EXTRA__DESTINATION = "com.example.lkjhgf.EXTRA_DESTINATION";

    private BootstrapButton date_button, arrival_departure_button, start_button, stopover_button, destination_button, back_button, settings_button, further_button;
    private TextView date_view, arrival_departure_view, start_view, stopover_view, destination_view;

    private boolean isArrivalTime;

    public void change_view_to_settings(){
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public void change_view_to_possible_connections(){

        //Nutzereingaben holen
        String date = date_view.getText().toString();
        String time = arrival_departure_view.getText().toString();
        String start = start_view.getText().toString();
        String stopover = stopover_view.getText().toString();
        String destination = destination_view.getText().toString();

        Intent intent = new Intent(this, Possible_connections_single.class);

        //Nutzereingaben an die naechste Ansicht weiterleiten
        intent.putExtra(EXTRA__DATE, date);
        intent.putExtra(EXTRA__ISARRIVALTIME,isArrivalTime);
        intent.putExtra(EXTRA__TIME,time);
        intent.putExtra(EXTRA__START,start);
        intent.putExtra(EXTRA__STOPOVER, stopover);
        intent.putExtra(EXTRA__DESTINATION,destination);

        startActivity(intent);
    }

    private boolean completed_form(){
        if (((TextView) this.findViewById(R.id.EditText1)).getText().length() == 0
                ||((TextView) this.findViewById(R.id.EditText2)).getText().length() == 0
                || ((TextView) this.findViewById(R.id.EditText3)).getText().length() == 0
                || ((TextView) this.findViewById(R.id.EditText5)).getText().length() == 0){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void set_arrival_departure_button(){
        Context context = arrival_departure_button.getContext();
        BootstrapText.Builder builder = new BootstrapText.Builder(context);

        if(isArrivalTime){
            builder.addText("Ankunftszeit:");
        }else{
            builder.addText("Abfahrtszeit");
        }

        arrival_departure_button.setBootstrapText(builder.build());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_route);

        //Variablen initialisieren, einfachere Bennenung: Nummer -> Name
        date_button = findViewById(R.id.BootstrapButton7);
        arrival_departure_button = findViewById(R.id.BootstrapButton8);
        start_button = findViewById(R.id.BootstrapButton9);
        stopover_button = findViewById(R.id.BootstrapButton10);
        destination_button = findViewById(R.id.BootstrapButton11);
        back_button = findViewById(R.id.BootstrapButton12);
        settings_button = findViewById(R.id.BootstrapButton13);
        further_button = findViewById(R.id.BootstrapButton14);

        date_view = findViewById(R.id.EditText1);
        arrival_departure_view = findViewById(R.id.EditText2);
        start_view = findViewById(R.id.EditText3);
        stopover_view = findViewById(R.id.EditText4);
        destination_view = findViewById(R.id.EditText5);

        // Button-Design
        date_button.setBootstrapBrand(new ButtonBootstrapBrandInvisible());
        arrival_departure_button.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        start_button.setBootstrapBrand(new ButtonBootstrapBrandInvisible());
        stopover_button.setBootstrapBrand(new ButtonBootstrapBrandInvisible());
        destination_button.setBootstrapBrand(new ButtonBootstrapBrandInvisible());
        back_button.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        settings_button.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        further_button.setBootstrapBrand(new ButtonBootstrapBrandVisible());

        // OnClick Listener für die einzelnen Buttons
        arrival_departure_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isArrivalTime = !isArrivalTime;
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

        further_button.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        this.findViewById(R.id.BootstrapButton14).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(completed_form()){
                    change_view_to_possible_connections();
                }else{
                    //TODO Anzeigen eines Hinweises -> alle Felder ausfuellen
                    System.out.println("Bitte komplett ausfuellen");
                }

            }
        });

    }

}
