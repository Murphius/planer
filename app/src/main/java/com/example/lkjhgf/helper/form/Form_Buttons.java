package com.example.lkjhgf.helper.form;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.lkjhgf.color.ButtonBootstrapBrandInvisible;
import com.example.lkjhgf.color.ButtonBootstrapBrandVisible;
import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.util.Utils;
import com.example.lkjhgf.activites.Settings;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

class Form_Buttons {

    private View view;
    private Activity activity;
    private Context context;

    private Form form;
    private Form_Text text;

    private BootstrapButton date_button, arrival_departure_button;
    private BootstrapButton start_button, stopover_button, destination_button;
    private BootstrapButton back_button, settings_button;
    private BootstrapButton further_button;
    private BootstrapButton clearStart, clearDestination, clearStopover;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private TimePickerDialog.OnTimeSetListener onTimeSetListener;

    Form_Buttons(View view, Activity activity, Form form) {
        this.view = view;
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.form = form;

        findButtons();
        designButtons();
    }

    void setText(Form_Text text) {
        this.text = text;
    }

    private void findButtons() {
        date_button = view.findViewById(R.id.BootstrapButton7);
        arrival_departure_button = view.findViewById(R.id.BootstrapButton8);
        start_button = view.findViewById(R.id.BootstrapButton9);
        stopover_button = view.findViewById(R.id.BootstrapButton10);
        destination_button = view.findViewById(R.id.BootstrapButton11);
        back_button = view.findViewById(R.id.BootstrapButton12);
        settings_button = view.findViewById(R.id.BootstrapButton13);
        further_button = view.findViewById(R.id.BootstrapButton14);
        clearStart = view.findViewById(R.id.BootstrapButton33);
        clearStopover = view.findViewById(R.id.BootstrapButton34);
        clearDestination = view.findViewById(R.id.BootstrapButton35);
    }

    private void designButtons() {
        date_button.setBootstrapBrand(new ButtonBootstrapBrandInvisible());
        arrival_departure_button.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        start_button.setBootstrapBrand(new ButtonBootstrapBrandInvisible());
        stopover_button.setBootstrapBrand(new ButtonBootstrapBrandInvisible());
        destination_button.setBootstrapBrand(new ButtonBootstrapBrandInvisible());
        back_button.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        settings_button.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        further_button.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        clearStart.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        clearStopover.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        clearDestination.setBootstrapBrand(new ButtonBootstrapBrandVisible());
    }

    public void setOnClickListener() {
        setArrivalDepartureButtonSetOnClickListener();
        setClearDestinationSetOnClickListener();
        setClearStopoverSetOnClickListener();
        setClearStartSetOnClickListener();
        setDateOnClickListener();
        setTimeOnClickListener();
        setBackOnClickListener();
        setSettingsOnClickListener();
        setFurtherOnClickListener();
    }

    private void setArrivalDepartureButtonSetOnClickListener() {
        arrival_departure_button.setOnClickListener(v -> {
            form.isArrivalTime = !form.isArrivalTime;
            Utils.setArrivalDepartureButton(arrival_departure_button, form.isArrivalTime);
        });
    }

    private void setClearDestinationSetOnClickListener() {
        clearDestination.setOnClickListener(v -> {
            form.destinationLocation = null;
            text.destination_view.setText("");
        });
    }

    private void setClearStopoverSetOnClickListener() {
        clearStopover.setOnClickListener(v -> {
            form.stopoverLocation = null;
            text.stopover_view.setText("");
        });
    }

    private void setClearStartSetOnClickListener() {
        clearStart.setOnClickListener(v -> {
            form.startLocation = null;
            text.start_view.setText("");
        });
    }

    private void setDateOnClickListener() {
        text.date_view.setOnClickListener(v -> {
            int year = form.selectedDate.get(Calendar.YEAR);
            int month = form.selectedDate.get(Calendar.MONTH);
            int dayOfMonth = form.selectedDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(view.getContext(),
                    R.style.kalenderStyle,
                    onDateSetListener,
                    year,
                    month,
                    dayOfMonth);

            dialog.show();
        });

        onDateSetListener = (view, year, month, dayOfMonth) -> {
            form.selectedDate.set(year, month, dayOfMonth);
            Date date_from_calendar = form.selectedDate.getTime();
            DateFormat dateFormat = new SimpleDateFormat("dd. MM. yyyy", Locale.GERMANY);
            text.date_view.setText(dateFormat.format(date_from_calendar));
        };
    }

    private void setTimeOnClickListener() {
        text.arrival_departure_view.setOnClickListener(v -> {
            int hour = form.selectedDate.get(Calendar.HOUR_OF_DAY);
            int min = form.selectedDate.get(Calendar.MINUTE);

            TimePickerDialog dialog = new TimePickerDialog(view.getContext(),
                    R.style.UhrStyle,
                    onTimeSetListener,
                    hour,
                    min,
                    true);

            dialog.show();
        });

        onTimeSetListener = (view, hourOfDay, minute) -> {
            form.selectedDate.set(form.selectedDate.get(Calendar.YEAR),
                    form.selectedDate.get(Calendar.MONTH),
                    form.selectedDate.get(Calendar.DAY_OF_MONTH),
                    hourOfDay,
                    minute);
            Date date_from_calendar = form.selectedDate.getTime();
            DateFormat dateFormat = new SimpleDateFormat("HH : mm", Locale.GERMANY);
            text.arrival_departure_view.setText(dateFormat.format(date_from_calendar));
        };
    }

    private void setBackOnClickListener() {
        back_button.setOnClickListener(v -> onBackPressed());
    }

    private void onBackPressed() {
        activity.onBackPressed();
    }

    private void setSettingsOnClickListener() {
        settings_button.setOnClickListener(v -> change_view_to_settings());
    }

    private void change_view_to_settings() {
        Intent intent = new Intent(view.getContext(), Settings.class);
        context.startActivity(intent);
    }

    private void setFurtherOnClickListener() {
        further_button.setOnClickListener(v -> {
            if(form.checkFormComplete()){
                form.changeViewToPossibleConnections();
            }
        });
    }

}