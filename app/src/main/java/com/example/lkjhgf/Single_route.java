package com.example.lkjhgf;

import android.app.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapText;
import com.example.lkjhgf.Adapter.MyArrayAdapter;
import com.example.lkjhgf.Color.ButtonBootstrapBrandInvisible;
import com.example.lkjhgf.Color.ButtonBootstrapBrandVisible;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.schildbach.pte.dto.Location;

public class Single_route extends Activity {

    // Konstanten fuer Intent
    public static final String EXTRA__DATE = "com.example.lkjhgf.EXTRA_DATE";
    public static final String EXTRA__ISARRIVALTIME = "com.example.lkjhgf.ISARRIVALTIME";
    public static final String EXTRA__START = "com.example.lkjhgf.EXTRA_START";
    public static final String EXTRA__STOPOVER = "com.example.lkjhgf.EXTRA_STOPOVER";
    public static final String EXTRA__DESTINATION = "com.example.lkjhgf.EXTRA_DESTINATION";

    private BootstrapButton date_button, arrival_departure_button, start_button, stopover_button, destination_button, back_button, settings_button, further_button;
    private TextView date_view, arrival_departure_view;
    private AutoCompleteTextView start_view, destination_view, stopover_view;

    private boolean isArrivalTime;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private TimePickerDialog.OnTimeSetListener onTimeSetListener;

    private Location start_location, destination_location, stopover_location;

    private Calendar selectedDate;

    public void change_view_to_settings() {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public void change_view_to_possible_connections() {
        if (!checkAmbigous()) {
            //Nutzereingaben holen
           Intent intent = new Intent(this, Possible_connections_single.class);

            //Nutzereingaben an die naechste Ansicht weiterleiten
            intent.putExtra(EXTRA__DATE, selectedDate);
            intent.putExtra(EXTRA__ISARRIVALTIME, isArrivalTime);
            intent.putExtra(EXTRA__START, start_location);
            intent.putExtra(EXTRA__STOPOVER, stopover_location);
            intent.putExtra(EXTRA__DESTINATION, destination_location);

            startActivity(intent);
        }
    }

    private boolean completed_form() {
        if (((TextView) this.findViewById(R.id.editText1)).getText().length() == 0
                || ((TextView) this.findViewById(R.id.editText2)).getText().length() == 0
                || ((TextView) this.findViewById(R.id.editText3)).getText().length() == 0
                || ((TextView) this.findViewById(R.id.editText5)).getText().length() == 0) {
            return false;
        }
        return true;
    }

    private boolean checkAmbigous() {
        if (start_location == null || destination_location == null){
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Main_activity.class);
        startActivity(intent);
        finish();
    }

    private void set_arrival_departure_button() {
        Context context = arrival_departure_button.getContext();
        BootstrapText.Builder builder = new BootstrapText.Builder(context);

        if (isArrivalTime) {
            builder.addText("Ankunftszeit:");
        } else {
            builder.addText("Abfahrtszeit");
        }

        arrival_departure_button.setBootstrapText(builder.build());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_route);

        selectedDate = Calendar.getInstance();

        initVar();

        setOnClickListener();

        start_view.setAdapter(new MyArrayAdapter(this, start_view.getThreshold()));
        start_view.setOnItemClickListener((parent, view, position, id) -> start_location = ((MyArrayAdapter.LocationHolder) parent.getItemAtPosition(position)).location);
        destination_view.setAdapter(new MyArrayAdapter(this, destination_view.getThreshold()));
        destination_view.setOnItemClickListener((parent, view, position, id) -> destination_location = ((MyArrayAdapter.LocationHolder) parent.getItemAtPosition(position)).location);
        stopover_view.setAdapter(new MyArrayAdapter(this, stopover_view.getThreshold()));
        stopover_view.setOnItemClickListener((parent, view, position, id) -> stopover_location = ((MyArrayAdapter.LocationHolder) parent.getItemAtPosition(position)).location);

    }


    private void initVar() {
        //Variablen initialisieren, einfachere Bennenung: Nummer -> Name
        date_button = findViewById(R.id.BootstrapButton7);
        arrival_departure_button = findViewById(R.id.BootstrapButton8);
        start_button = findViewById(R.id.BootstrapButton9);
        stopover_button = findViewById(R.id.BootstrapButton10);
        destination_button = findViewById(R.id.BootstrapButton11);
        back_button = findViewById(R.id.BootstrapButton12);
        settings_button = findViewById(R.id.BootstrapButton13);
        further_button = findViewById(R.id.BootstrapButton14);

        date_view = findViewById(R.id.editText1);
        arrival_departure_view = findViewById(R.id.editText2);
        start_view = findViewById(R.id.editText3);
        stopover_view = findViewById(R.id.editText4);
        destination_view = findViewById(R.id.editText5);

        // Button-Design
        date_button.setBootstrapBrand(new ButtonBootstrapBrandInvisible());
        arrival_departure_button.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        start_button.setBootstrapBrand(new ButtonBootstrapBrandInvisible());
        stopover_button.setBootstrapBrand(new ButtonBootstrapBrandInvisible());
        destination_button.setBootstrapBrand(new ButtonBootstrapBrandInvisible());
        back_button.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        settings_button.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        further_button.setBootstrapBrand(new ButtonBootstrapBrandVisible());

    }

    private void setOnClickListener() {
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

        further_button.setOnClickListener(v -> {

            if (completed_form()) {
                change_view_to_possible_connections();
            } else {
                //TODO Anzeigen eines Hinweises -> alle Felder ausfuellen
                System.out.println("Bitte komplett ausfuellen");
            }

        });

        date_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = selectedDate.get(Calendar.YEAR);
                int month = selectedDate.get(Calendar.MONTH);
                int dayOfMonth = selectedDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(Single_route.this,
                        R.style.kalenderStyle,
                        onDateSetListener,
                        year,
                        month,
                        dayOfMonth);

                dialog.show();
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                selectedDate.set(year, month, dayOfMonth);
                Date date_from_calendar = selectedDate.getTime();
                DateFormat dateFormat = new SimpleDateFormat("dd. MM yyyy");
                date_view.setText(dateFormat.format(date_from_calendar));
            }
        };

        arrival_departure_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = selectedDate.get(Calendar.HOUR_OF_DAY);
                int min = selectedDate.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(Single_route.this,
                        R.style.UhrStyle,
                        onTimeSetListener,
                        hour,
                        min,
                        true);

                dialog.show();
            }
        });

        onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                selectedDate.set(selectedDate.get(Calendar.YEAR),
                        selectedDate.get(Calendar.MONTH),
                        selectedDate.get(Calendar.DAY_OF_MONTH),
                        hourOfDay,
                        minute);
                Date date_from_calendar = selectedDate.getTime();
                DateFormat dateFormat = new SimpleDateFormat("HH : mm");
                arrival_departure_view.setText(dateFormat.format(date_from_calendar));
            }
        };

    }

}
