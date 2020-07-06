package com.example.lkjhgf.helper.form;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.example.lkjhgf.activities.Settings;
import com.example.lkjhgf.helper.MyURLParameter;
import com.example.lkjhgf.publicTransport.query.QueryParameter;
import com.example.lkjhgf.publicTransport.query.QueryTask;

import java.util.Calendar;

import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.QueryTripsResult;
import de.schildbach.pte.dto.Trip;
import de.schildbach.pte.dto.TripOptions;

/**
 * Handhabung der Nutzereingaben und des gesammten Layouts für das Formular <br/>
 * <p>
 * Die Buttons sind in der Klasse {@link ButtonClass} und die Textfelder in die Klasse
 * {@link TextViewClass} ausgelagert <br/>
 */
public abstract class Form {

    //Intent für die Übergabe an die nächste Aktivität
    public static final String EXTRA_MYURLPARAMETER = "com.example.lkjhgf.individual_trip.form.EXTRA_MYURLPARAMETER";

    // Parameter der Fahrt
    Location startLocation, stopoverLocation, destinationLocation;
    Calendar selectedDate;
    boolean isArrivalTime;

    protected Context context;
    protected Activity activity;
    protected Intent intent;

    protected ButtonClass buttons;
    protected TextViewClass text;

    /**
     * Erstellen der Klassen für die Buttons und Textfelder, initialisierung der Attribute <br/>
     *
     * @param activity - Aufrufende Aktivität, benötigt für das Starten der nächsten Aktivität
     * @param view     - Layout
     */
    Form(Activity activity, View view) {
        this.activity = activity;
        context = activity.getApplicationContext();

        buttons = new ButtonClass(view, activity, this);
        text = new TextViewClass(view, activity, this);
        buttons.setText(text);

        // Aktuellen Zeitpunkt als Defaultwert
        selectedDate = Calendar.getInstance();
    }

    /**
     * Füllt das Layout mit den Attributen des Trips welcher übergeben wurde <br/>
     *
     * @param activity aufrufende Aktivität
     * @param view     Layout
     * @param trip     - Trip der bearbeitet werden soll
     * @preconditions der Nutzer hat eine Fahrt zum Editieren oder Kopieren ausgewählt
     */
    Form(Activity activity, View view, Trip trip) {
        this(activity, view);
        // Die Angaben sollen mit den angaben der Fahrt übereinstimmen
        text.fillTextViews(trip);
        // Der Zeitpunkt soll mit dem der übergebenen Fahrt übereinstimmen
        selectedDate.setTime(trip.getFirstDepartureTime());
    }

    /**
     * Überprüft ob das Formular vollständig ausgefüllt ist <br/>
     * <p>
     * Ein Formular ist vollständig ausgefüllt, wenn ein Datum, eine Uhrzeit, ein Startpunkt und
     * ein Zielpunkt gewählt wurden. <br/>
     * Hat der Nutzer eins dieser Parameter vergessen, so wird er darüber informiert.
     *
     * @return true -> der Nutzer hat die für eine Fahrt notwendigen Parameter
     * (Datum, Uhrzeit, Start-, Zieladresse) angegeben <br/>
     * false -> der Nutzer hat einen dieser Parameter vergessen
     * @preconditions Der Nutzer hat den Button für "weiter" geklickt
     */
    boolean checkFormComplete() {
        if (text.date_view.getText().length() == 0) {
            Toast.makeText(context,
                    "Bitte ein Datum eingeben",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (text.arrivalDepartureView.getText().length() == 0) {
            Toast.makeText(context,
                    "Bitte eine Uhrzeit eingeben",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (text.start_view.getText().length() == 0 || startLocation == null) {
            Toast.makeText(context,
                    "Bitte einen eindeutigen Startpunkt eingeben",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (text.destination_view.getText().length() == 0 || destinationLocation == null) {
            Toast.makeText(context,
                    "Bitte einen eindeutigen Zielpunkt eingeben",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    /**
     * Übergibt die Eingaben des Nutzers an die nächste Schicht, startet eine Anfrage an den Provider
     * mit diesen <br/>
     * <p>
     * Das setzen der Intent Werte erfolgt in der Funktion {@link #setIntentExtras()}. <br/>
     * Die Anfrage wird hier gestartet, die Verarbeitung der Anfrage erfolgt jedoch in der nächsten
     * Aktivität. Diese wird in {@link QueryTask#onPostExecute(QueryTripsResult)} gestartet.
     *
     * @preconditions der Nutzer hat "weiter" geklickt und die "überprüfung" des Formulars war erfolgreich
     * @postconditions Anzeigen möglicher Verbindungen
     */
    void changeViewToPossibleConnections() {
        setIntentExtras();

        TripOptions tripOptions = Settings.getTripOptions(activity);

        QueryParameter queryParameter = new QueryParameter(startLocation, stopoverLocation, destinationLocation, selectedDate.getTime(), !isArrivalTime, tripOptions);
        new QueryTask(activity, intent).execute(queryParameter);
    }

    /**
     * Wenn eine Fahrt kopiert wurde, bleiben alle Parameter unverändert, bis auf
     * das Datum. <br/>
     * <p>
     * Das Datum wird default mäßig auf das aktuelle Datum gesetzt. Die entsprechenden Textfelder
     * hingegen werden geleert.
     */
    public void copy() {
        selectedDate = Calendar.getInstance();
        text.arrivalDepartureView.setText("");
        text.date_view.setText("");

    }

    /**
     * Packt in den Intent die Nutzereingaben <br/>
     * <p>
     * An die nächste Aktivität werden der Zeitpunkt (Datum & Uhrzeit), Abfahrts-/Ankunftszeit,
     * Startpunkt, Zwischenhalt und Endhalt übergeben
     *
     * @preconditions erfolgreiche Prüfung des Formulars, Nutzer hat auf "weiter" geklickt
     */
    private void setIntentExtras() {
        MyURLParameter myURLParameter = new MyURLParameter(selectedDate.getTime(), startLocation, stopoverLocation, destinationLocation, !isArrivalTime, Settings.getTripOptions(activity));
        intent.putExtra(EXTRA_MYURLPARAMETER, myURLParameter);
    }

    public void setOnClickListener() {
        buttons.setOnClickListener();
    }

    public void setAdapter() {
        text.setAdapter();
    }

    TextViewClass getText() {
        return text;
    }
}
