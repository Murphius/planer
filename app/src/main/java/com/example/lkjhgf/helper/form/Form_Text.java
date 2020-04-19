package com.example.lkjhgf.helper.form;

import android.content.Context;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.example.lkjhgf.adapter.MyArrayAdapter;
import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.util.UtilsString;

import de.schildbach.pte.dto.Trip;

/**
 * Handhabung der TextViews in der Formularansicht
 */
class Form_Text {

    private View view;
    private Context context;
    private Form form;

    TextView date_view, arrivalDepartureView;
    AutoCompleteTextView start_view, destination_view, stopover_view;
    TextView numChildren, numChildrenView;
    TextView numAdult, numAdultView;

    /**
     * Initialisierung der Attribute <br/>
     * <p>
     * Initialisierung der TextViews in {@link #findTextViews()}
     *
     * @param view    Layout
     * @param context für die Adapter / Vorschläge möglicher Haltestellen
     * @param form    zugehlriges Formular
     */
    Form_Text(View view, Context context, Form form) {
        this.view = view;
        this.context = context;
        this.form = form;
        findTextViews();
    }

    /**
     * Initialisierung der Textviews <br/>
     * <p>
     * Zuoordnung Attribut <-> ID für eine einfachere Handhabung
     */
    private void findTextViews() {
        date_view = view.findViewById(R.id.editText1);
        arrivalDepartureView = view.findViewById(R.id.editText2);
        start_view = view.findViewById(R.id.editText3);
        stopover_view = view.findViewById(R.id.editText4);
        destination_view = view.findViewById(R.id.editText5);

        numChildren = view.findViewById(R.id.textView75);
        numAdult = view.findViewById(R.id.textView74);
        numChildrenView = view.findViewById(R.id.editText14);
        numAdultView = view.findViewById(R.id.editText13);
    }

    /**
     * Die Textfelder für die Punkte mit Vorschlägen benötigen "Filter" mit Vorschlägen <br/>
     * <p>
     * Die Orts-Vorschläge werden vom Provider erzeugt, passend zur Nutzereingabe.
     * Dies geschieht in der Klasse {@link MyArrayAdapter}.
     *
     * @preconditions Der Nutzer klickt auf eins der drei Textfelder, und tippt eine Adresse ein,
     * anschließend wählt er einen der Vorschläge aus
     * @postconditions die gewählte Adresse ist gespeichert und wird bei der Suche nach Verbindungen
     * berücksichtigt, sowie in der Ansicht angezeigt
     */
    void setAdapter() {
        start_view.setAdapter(new MyArrayAdapter(context, start_view.getThreshold()));
        start_view.setOnItemClickListener((parent, view, position, id) -> form.startLocation = ((MyArrayAdapter.LocationHolder) parent.getItemAtPosition(
                position)).location);
        destination_view.setAdapter(new MyArrayAdapter(context, destination_view.getThreshold()));
        destination_view.setOnItemClickListener((parent, view, position, id) -> form.destinationLocation = ((MyArrayAdapter.LocationHolder) parent.getItemAtPosition(
                position)).location);
        stopover_view.setAdapter(new MyArrayAdapter(context, stopover_view.getThreshold()));
        stopover_view.setOnItemClickListener((parent, view, position, id) -> form.stopoverLocation = ((MyArrayAdapter.LocationHolder) parent.getItemAtPosition(
                position)).location);
    }

    /**
     * Füllt die Ansicht mit den Informationen aus dem Trip
     *
     * @param trip die kopierte Fahrt
     * @preconditions Der Nutzer editiert oder kopiert eine Fahrt
     * @postconditions Die Ansicht enthält die gleichen Informationen, wie die in der Fahrt hinterlegten
     */
    void fillTextViews(Trip trip) {
        form.selectedDate.setTime(trip.getFirstDepartureTime());
        date_view.setText(UtilsString.setDate(form.selectedDate.getTime()));
        arrivalDepartureView.setText(UtilsString.setTime(form.selectedDate.getTime()));

        form.startLocation = trip.from;
        start_view.setText(UtilsString.setLocationName(trip.from));

        form.destinationLocation = trip.to;
        destination_view.setText(UtilsString.setLocationName(trip.to));
    }

}
