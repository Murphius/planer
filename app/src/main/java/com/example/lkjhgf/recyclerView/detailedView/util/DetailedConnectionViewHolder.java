package com.example.lkjhgf.recyclerView.detailedView.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beardedhen.androidbootstrap.font.Typicon;
import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.util.Utils;
import com.example.lkjhgf.recyclerView.detailedView.CloseUpItem;
import com.example.lkjhgf.recyclerView.detailedView.CloseUpPrivateItem;
import com.example.lkjhgf.recyclerView.detailedView.CloseUpPublicItem;
import com.example.lkjhgf.recyclerView.detailedView.OnItemClickListener;
import com.example.lkjhgf.recyclerView.detailedView.components.Stopover_adapter;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Ansicht eines einzelnen Fahrtabschnitts <br/>
 * <p>
 * Die Textfelder sind in die Klasse {@link TextViewClass} verschoben <br/>
 * und die Buttons in die Klasse {@link ButtonClass} verschoben.
 * </p>
 */
public class DetailedConnectionViewHolder extends RecyclerView.ViewHolder {

    private Activity activity;

    private TextViewClass textViewClass;
    private ButtonClass buttonClass;
    public ImageView icon;
    private View horizontalLine;

    public RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public DetailedConnectionViewHolder(Activity activity,
                                        View itemView,
                                        OnItemClickListener listener) {
        super(itemView);

        this.activity = activity;

        textViewClass = new TextViewClass(itemView);

        buttonClass = new ButtonClass(this, itemView, listener);

        // Ansicht mit der Abfolge der Verkehrsmittel
        recyclerView = itemView.findViewById(R.id.recyclerView4);
        recyclerView.setHasFixedSize(true);

        //Icon für das jeweilige Verkehrsmittel
        icon = itemView.findViewById(R.id.imageView);

        //Trennlinie
        horizontalLine = itemView.findViewById(R.id.view4);

        layoutManager = new LinearLayoutManager(itemView.getContext());
    }

    /**
     * Unabhängig davon, welche Art von Verbindung es sich handelt, die Felder
     * Abfahrtszeit, -ort, sowie Ankunftszeit, -ort, die Dauer und das jeweilige
     * Icon werden gesetzt
     *
     * @param item das Objekt, für welches die Ansicht gefüllt wird
     */
    public void closeUpItemSetUpView(CloseUpItem item) {
        textViewClass.start_view.setText(item.getDeparture());
        textViewClass.destination_view.setText(item.getDestination());

        icon.setImageResource(item.getImage_resource());

        textViewClass.time_of_arrival_view.setText(item.getTime_of_arrival());
        textViewClass.time_of_departure_view.setText(item.getTime_of_departure());
    }

    /**
     * Füllt die Ansicht für einen Abschnitt, der mit dem ÖPNV zurückgelegt werden soll.
     * <br/>
     *
     * @param closeUpPublicItem Objekt, dessen Informationen in der Ansicht zu sehen sein sollen
     */
    public void publicItemSetUpView(CloseUpPublicItem closeUpPublicItem) {
        // Für den ÖPNV werden dem Nutzer die Zwischenhalte angezeigt, jedoch nicht der Pfad auf
        // der Karte
        buttonClass.showOnMap.setVisibility(View.GONE);

        //Gleis
        textViewClass.start_platform_view.setText(closeUpPublicItem.getDeparturePlatform());
        textViewClass.destination_platform_view.setText(closeUpPublicItem.getDestinationPlatform());
        // Ziel der Linie
        textViewClass.destination_of_number_view.setText(closeUpPublicItem.getDestinationOfNumber());
        // Bezeichner der Linie
        textViewClass.number_view.setText(closeUpPublicItem.getNumber());

        // Verspätung bei der Abfahrt bzw. bei der Ankunft
        Utils.setDelayView(textViewClass.delay_departure,
                closeUpPublicItem.getDelayDeparture(),
                activity.getResources());
        Utils.setDelayView(textViewClass.delay_arrival,
                closeUpPublicItem.getDelayArrival(),
                activity.getResources());


        if (closeUpPublicItem.isShowDetails()) {
            recyclerView.setVisibility(View.VISIBLE);
            buttonClass.stopoverLocationsShow.setTypicon(Typicon.TY_ARROW_UP);
            horizontalLine.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.GONE);
            buttonClass.stopoverLocationsShow.setTypicon(Typicon.TY_ARROW_DOWN);
            horizontalLine.setVisibility(View.GONE);
        }

        // Wenn die Strecke keine Zwischenhalte hat, soll das Layout gleich bleiben, der Nutzer
        // jedoch den Button für die Zwischenhalte sehen können
        if (closeUpPublicItem.getStopoverItems().isEmpty()) {
            buttonClass.stopoverLocationsShow.setVisibility(View.INVISIBLE);
        }

        //RecyclerView mit den Zwischenhalten
        adapter = new Stopover_adapter(closeUpPublicItem.getStopoverItems());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    /**
     * Füllt die Ansicht für einen individuell zurückzulegenden Abschnitt
     * <br/>
     *
     * @param closeUpPrivateItem enthält die Informationen, die in der Ansicht angezeigt werden
     *                           sollen
     */
    public void privateItemSetUpView(CloseUpPrivateItem closeUpPrivateItem) {
        // Bei einem individullen Abschnitt sind keine Verspätungen möglich, es können auch
        //keine Zwischenhalte angezeigt werden, keine Linie sowie kein Linienziel
        buttonClass.stopoverLocationsShow.setVisibility(View.INVISIBLE);
        textViewClass.destination_of_number_view.setVisibility(View.GONE);
        textViewClass.number_view.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);


        //Da der VRR keine GPS Koordinaten für die Fußgängernavigation zur Verfügung stellt,
        //und ich für Google Directions nicht zahlen möchte, wird zur Navigation eine
        //Anwendung (-> GoogleMaps) geöffnet
        LatLng departure = closeUpPrivateItem.getDepartureLocation();
        LatLng destination = closeUpPrivateItem.getDestinationLocation();
        /*URL, die die Anfrage repräsentiert
         %f -> Float Werte = Lat bzw Lng der jeweiligen Koordinate
        Locale.ENGLISH -> WICHTIG, mit GERMAN -> funktioniert nicht -> vermutlich formatierung
        der Ziffern
        */
        String uri = String.format(Locale.ENGLISH,
                "http://maps.google.com/maps?saddr=%f,%f&daddr=%f,%f",
                departure.latitude,
                departure.longitude,
                destination.latitude,
                destination.longitude);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        // Google Maps sollte geöffnet werden
        intent.setPackage("com.google.android.apps.maps");
        try {
            // Starten von Google Maps
            activity.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            //Wenn Google Maps nicht installiert ist, versuche andere Anwendung zu starten
            try {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                activity.startActivity(unrestrictedIntent);
            } catch (ActivityNotFoundException innerEx) {
                //Keine Navigationssoftware gefunden
                Toast.makeText(activity.getApplicationContext(),
                        "Please install a maps application",
                        Toast.LENGTH_LONG).show();
            }
        }

        //die Anzeige der Zwischenhalte entfällt / ist leer
        adapter = new Stopover_adapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

}