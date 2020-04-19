package com.example.lkjhgf.helper.form;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.lkjhgf.color.ButtonBootstrapBrandInvisible;
import com.example.lkjhgf.color.ButtonBootstrapBrandVisible;
import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.util.Utils;
import com.example.lkjhgf.activites.Settings;
import com.example.lkjhgf.helper.util.UtilsString;

import java.util.Calendar;
import java.util.Date;

/**
 * Handhabung der Buttons der Formularansicht
 */
class ButtonClass {

    private View view;
    private Activity activity;

    private Form form;
    private Form_Text text;

    //Alle Buttons in der Ansicht
    private BootstrapButton dateButton, arrivalDepartureButton;
    private BootstrapButton startButton, stopoverButton, destinationButton;
    private BootstrapButton backButton, settingsButton;
    private BootstrapButton furtherButton;
    private BootstrapButton clearStart, clearDestination, clearStopover;

    // Jeweiliger Dialog für die Auswahl eines Datums bzw. einer Uhrzeit
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private TimePickerDialog.OnTimeSetListener onTimeSetListener;

    /**
     * Initialisierung der Attribute <br/>
     * <p>
     * Initialisierung der Buttons in {@link #findButtons()}, anschließend Design dieser anpassen
     * in {@link #designButtons()} <br/>
     *
     * @param view     Layout
     * @param activity aufrufende Aktivität, wird benötigt für den Abbrechen-Button
     * @param form     zugehöriges Formular
     */
    ButtonClass(View view, Activity activity, Form form) {
        this.view = view;
        this.activity = activity;
        this.form = form;

        findButtons();
        designButtons();
    }

    /**
     * Initialisierung der Button-Attribute <br/>
     * <p>
     * Zuordnung Attribut <-> ID
     */
    private void findButtons() {
        dateButton = view.findViewById(R.id.BootstrapButton7);
        arrivalDepartureButton = view.findViewById(R.id.BootstrapButton8);
        startButton = view.findViewById(R.id.BootstrapButton9);
        stopoverButton = view.findViewById(R.id.BootstrapButton10);
        destinationButton = view.findViewById(R.id.BootstrapButton11);
        backButton = view.findViewById(R.id.BootstrapButton12);
        settingsButton = view.findViewById(R.id.BootstrapButton13);
        furtherButton = view.findViewById(R.id.BootstrapButton14);
        clearStart = view.findViewById(R.id.BootstrapButton33);
        clearStopover = view.findViewById(R.id.BootstrapButton34);
        clearDestination = view.findViewById(R.id.BootstrapButton35);
    }

    /**
     * Layout der Buttons anpassen <br/>
     * <p>
     * Die Buttons die der Nutzer klicken kann, sind in dem "sichtbaren" Layout gehalten {@link ButtonBootstrapBrandVisible} <br/>
     * Die nicht "nutzbaren" Buttons hingegen in dem Design {@link ButtonBootstrapBrandInvisible}.
     */
    private void designButtons() {
        dateButton.setBootstrapBrand(new ButtonBootstrapBrandInvisible());
        arrivalDepartureButton.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        startButton.setBootstrapBrand(new ButtonBootstrapBrandInvisible());
        stopoverButton.setBootstrapBrand(new ButtonBootstrapBrandInvisible());
        destinationButton.setBootstrapBrand(new ButtonBootstrapBrandInvisible());
        backButton.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        settingsButton.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        furtherButton.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        clearStart.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        clearStopover.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        clearDestination.setBootstrapBrand(new ButtonBootstrapBrandVisible());
    }

    /**
     * Setzen der onClickListener <br/>
     * <p>
     * Nicht alle Buttons sind klickbar; <br/>
     * Nur die Buttons für : <br/>
     * - das Leeren der Textfelder für Start-, Zielpunkt und Zwischenhalt <br/>
     * - Wechsel zwischen Ankunfts-/ Abfahrtszeit <br/>
     * - Öffnen von Einstellungen, zurück; weiter <br/>
     * - Klick auf die Textfelder für das Datum und die Uhrzeit öffnen die jeweiligen Picker Ansichten
     */
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

    /**
     * Wechsel zwischen Ankunftszeit und Abfahrtszeit <br/>
     * <p>
     * Der Text wechselt von Ankunftszeit auf Abfahrtszeit bzw. von Abfahrtszeit auf Ankunftszeit
     * {@link com.example.lkjhgf.helper.util.UtilsString#arrivalDepartureTime(boolean isArrivalTime)} <br/>
     * Die logische Variable, die diesen Wert enthält, ist ebenfalls auf das logische Gegenteil gesezt
     *
     * @preconditions der Nutzer hat auf den Button gedrückt
     * @postconditions der Text ändert sich auf das Gegenteil, die Variable für die Ankunftszeit
     * ist auf das logische Gegenteil gesetzt
     */
    private void setArrivalDepartureButtonSetOnClickListener() {
        arrivalDepartureButton.setOnClickListener(v -> {
            form.isArrivalTime = !form.isArrivalTime;
            Utils.setArrivalDepartureButton(arrivalDepartureButton, form.isArrivalTime);
        });
    }

    /**
     * Leert die Eingabe für das Ziel <br/>
     *
     * @preconditions Klicken des Buttons
     * @postconditions Das Textfeld für das Ziel ist leer, die Location im Formular wurde
     * auf null gesetzt
     */
    private void setClearDestinationSetOnClickListener() {
        clearDestination.setOnClickListener(v -> {
            form.destinationLocation = null;
            text.destination_view.setText("");
        });
    }

    /**
     * Leert die Eingabe für den Zwischenhalt <br/>
     *
     * @preconditions Klicken des Buttons
     * @postconditions Das Textfeld für den Zwischenhalt ist leer, die Location im Formular ist null
     */
    private void setClearStopoverSetOnClickListener() {
        clearStopover.setOnClickListener(v -> {
            form.stopoverLocation = null;
            text.stopover_view.setText("");
        });
    }

    /**
     * Leert die Eingabe für den Startpunt <br/>
     *
     * @preconditions Klicken des Buttons
     * @postconditions Dast Textfeld für den Startpunkt ist leer, die Location im Formular ist null
     */
    private void setClearStartSetOnClickListener() {
        clearStart.setOnClickListener(v -> {
            form.startLocation = null;
            text.start_view.setText("");
        });
    }

    /**
     * Planung dieser Fahrt abbrechen <br/>
     *
     * @preconditions Der Nutzer hat zurück geklickt
     * @postconditions Der Nutzer kommt in die vorherige Aktivität
     */
    private void setBackOnClickListener() {
        backButton.setOnClickListener(v -> activity.onBackPressed());
    }

    /**
     * Einstellungen öffnen <br/>
     *
     * @preconditions Der Nutzer hat den Button geklickt
     * @postconditions Wechsel in die Ansicht der Einstellungen, aus dieser kehrt der Nutzer
     * zurück in diese Ansicht zurück
     */
    private void setSettingsOnClickListener() {
        settingsButton.setOnClickListener(v -> changeViewToSettings());
    }

    /**
     * Wechselt in die Aktivität "Einstellungen" <br/>
     *
     * @preconditions Der Nutzer hat auf den Button "Einstellungen" geklickt
     */
    private void changeViewToSettings() {
        Intent intent = new Intent(activity.getApplicationContext(), Settings.class);
        activity.startActivity(intent);
    }

    /**
     * Sucht nach möglichen Verbindungen und zeigt diese an, wenn das Formular vollständig ausgefüllt ist <br/>
     * <p>
     * Überprüft, ob das Formular vollständig ausgefüllt ist ({@link Form#checkFormComplete()}), wenn ja
     * werden passende Verbindungen gesucht und diese dem Nutzer angezeigt ({@link Form#changeViewToPossibleConnections()}). <br/>
     * Wenn jedoch noch angaben im Formular fehlen, wird der Nutzer darüber informiert.
     *
     * @preconditions Klicken des Buttons
     * @postconditions wenn das Formular ausgefüllt ist, werden passende Verbindungen gesucht, und diese
     * anschließend dem Nutzer angezeigt
     */
    private void setFurtherOnClickListener() {
        furtherButton.setOnClickListener(v -> {
            if (form.checkFormComplete()) {
                form.changeViewToPossibleConnections();
            }
        });
    }

    /**
     * Öffnet einen Dialog mit Kalendaransicht, in dem der Nutzer ein Datum auswählen kann <br/>
     * <p>
     * Als Defaultwert ist der aktuelle Tag ausgewählt, oder das zuletzt gewählte Datum. <br/>
     * Automatische überprüfung, ob ein gültiges Datum eingegeben wurde (33.14. nicht möglich)
     *
     * @preconditions Der Nutzer hat auf das Textfeld für das Datum geklickt
     * @postconditions Das gewählte Datum wird gespeichert und dem Nutzer im Textfeld angezeigt
     */
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

        // Wenn der Nutzer ein Datum ausgewählt hat, wird dieses im Textfeld angezeigt & in
        // der Variable selectedDate gespeichert
        onDateSetListener = (view, year, month, dayOfMonth) -> {
            form.selectedDate.set(year, month, dayOfMonth);
            Date date_from_calendar = form.selectedDate.getTime();
            text.date_view.setText(UtilsString.setDate(date_from_calendar));
        };
    }

    /**
     * Öffnet einen Dialog mit 24h Ansicht einer Uhr, in welcher der Nutzer eine Uhrzeit auswählen kann <br/>
     * <p>
     * Als Defaultwert ist die aktuelle Uhrzeit, oder die zuletzt gewählte Zeit, angezeigt. <br/>
     * Automatische überprüfung, ob eine Uhrzeit gültig ist (25:65 nicht möglich)
     *
     * @preconditions Der Nutzer hat das Textfeld in welches die Uhrzeit eingegeben werden soll
     * angeklickt
     * @postconditions Die gewählte Uhrzeit wird gespeichert und dem Nutzer angezeigt
     */
    private void setTimeOnClickListener() {
        text.arrivalDepartureView.setOnClickListener(v -> {
            int hour = form.selectedDate.get(Calendar.HOUR_OF_DAY);
            int min = form.selectedDate.get(Calendar.MINUTE);

            TimePickerDialog dialog = new TimePickerDialog(view.getContext(),
                    R.style.ClockStyle,
                    onTimeSetListener,
                    hour,
                    min,
                    true);

            dialog.show();
        });

        // wenn der Nutzer eine Zeit auswählt, soll diese Zeit gespeichert werden
        // & anschließend dem Nutzer angezeigt werden
        onTimeSetListener = (view, hourOfDay, minute) -> {
            form.selectedDate.set(form.selectedDate.get(Calendar.YEAR),
                    form.selectedDate.get(Calendar.MONTH),
                    form.selectedDate.get(Calendar.DAY_OF_MONTH),
                    hourOfDay,
                    minute);
            Date timeFromClock = form.selectedDate.getTime();
            text.arrivalDepartureView.setText(UtilsString.setTime(timeFromClock));
        };
    }


    void setText(Form_Text text) {
        this.text = text;
    }

}