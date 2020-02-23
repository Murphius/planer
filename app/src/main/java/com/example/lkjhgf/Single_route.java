package com.example.lkjhgf;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapText;
import com.example.lkjhgf.Color.ButtonBootstrapBrandInvisible;
import com.example.lkjhgf.Color.ButtonBootstrapBrandVisible;

public class Single_route extends Activity {

    public static final String EXTRA__DATE = "com.example.lkjhgf.EXTRA_DATE";
    public static final String EXTRA__ISARRIVALTIME = "com.example.lkjhgf.ISARRIVALTIME";
    public static final String EXTRA__TIME = "com.example.lkjhgf.EXTRA_TIME";
    public static final String EXTRA__START = "com.example.lkjhgf.EXTRA_START";
    public static final String EXTRA__STOPOVER = "com.example.lkjhgf.EXTRA_STOPOVER";
    public static final String EXTRA__DESTINATION = "com.example.lkjhgf.EXTRA_DESTINATION";


    private boolean isArrivalTime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_route);

        ((BootstrapButton) this.findViewById(R.id.date_button)).setBootstrapBrand(new ButtonBootstrapBrandInvisible());

        ((BootstrapButton) this.findViewById(R.id.an_abfahrt_button_einzelne_verbindung)).setBootstrapBrand(new ButtonBootstrapBrandVisible());
        final BootstrapButton btn = this.findViewById(R.id.an_abfahrt_button_einzelne_verbindung);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isArrivalTime = !isArrivalTime;
                Context context = btn.getContext();
                BootstrapText.Builder builder = new BootstrapText.Builder(context);

                if (isArrivalTime) {
                    builder.addText(context.getText(R.string.ankunft));
                } else {
                    builder.addText(context.getText(R.string.abfahrt));
                }

                btn.setBootstrapText(builder.build());

            }
        });
        ((BootstrapButton) this.findViewById(R.id.start_button)).setBootstrapBrand(new ButtonBootstrapBrandInvisible());

        ((BootstrapButton) this.findViewById(R.id.stopover_button)).setBootstrapBrand(new ButtonBootstrapBrandInvisible());

        ((BootstrapButton) this.findViewById(R.id.destination_button)).setBootstrapBrand(new ButtonBootstrapBrandInvisible());

        ((BootstrapButton) this.findViewById(R.id.cancel_button)).setBootstrapBrand(new ButtonBootstrapBrandVisible());
        this.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_main_activity();
            }
        });

        ((BootstrapButton) this.findViewById(R.id.settings_button)).setBootstrapBrand(new ButtonBootstrapBrandVisible());
        this.findViewById(R.id.settings_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_settings();
            }
        });

        ((BootstrapButton) this.findViewById(R.id.accept_button)).setBootstrapBrand(new ButtonBootstrapBrandVisible());
        this.findViewById(R.id.accept_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(completed_form()){
                    open_possible_connections();
                }else{
                    //TODO Anzeigen eines Hinweises -> alle Felder ausfuellen
                    System.out.println("test");
                }

            }
        });
    }

    public void open_main_activity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void open_settings(){
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public void open_possible_connections(){
        EditText date_text = this.findViewById(R.id.date_text);
        String date = date_text.getText().toString();

        EditText time_text = this.findViewById(R.id.time_text);
        String time = time_text.getText().toString();

        EditText start_text = this.findViewById(R.id.start_text);
        String start = start_text.getText().toString();

        EditText stopover_text = this.findViewById(R.id.stopover_text);
        String stopover = stopover_text.getText().toString();

        EditText destination_text = this.findViewById(R.id.destination_text);
        String destination = destination_text.getText().toString();

        Intent intent = new Intent(this, Possible_connections.class);

        intent.putExtra(EXTRA__DATE, date);
        intent.putExtra(EXTRA__ISARRIVALTIME,isArrivalTime);
        intent.putExtra(EXTRA__TIME,time);
        intent.putExtra(EXTRA__START,start);
        intent.putExtra(EXTRA__STOPOVER, stopover);
        intent.putExtra(EXTRA__DESTINATION,destination);

        startActivity(intent);
    }

    private boolean completed_form(){
        if (((TextView) this.findViewById(R.id.date_text)).getText().length() == 0
            ||((TextView) this.findViewById(R.id.time_text)).getText().length() == 0
            || ((TextView) this.findViewById(R.id.start_text)).getText().length() == 0
            || ((TextView) this.findViewById(R.id.destination_text)).getText().length() == 0){
            return false;
        }
        return true;
    }




}
