package com.example.lkjhgf.helper.closeUp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.util.UtilsList;
import com.example.lkjhgf.recyclerView.detailedView.CloseUpAdapter;
import com.example.lkjhgf.recyclerView.detailedView.CloseUpPrivateItem;
import com.example.lkjhgf.recyclerView.detailedView.CloseUpPublicItem;
import com.example.lkjhgf.recyclerView.detailedView.CloseUpItem;
import com.example.lkjhgf.recyclerView.detailedView.OnItemClickListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Handhabung des RecyclerViews in der detaillierten Ansicht <br/>
 * Jedes Item der Liste entspricht einem Fahrtabschnitt {@link CloseUpItem}
 */
public class CloseUpRecyclerView {

    private CloseUpAdapter adapter;

    private ArrayList<CloseUpItem> items;

    /**
     * Füllt den RecyclerView, setzt Layout Manager, OnClickListener, und Adapter
     *
     * @param activity - wird gebraucht um  die  einzelnen RecyclerView View Fragmente zu erzeugen
     * @param view     - Layout
     * @param closeUp  - zugriff auf die Fahrt
     */
    CloseUpRecyclerView(Activity activity, View view, CloseUp closeUp) {
        //RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView3);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity.getApplicationContext());

        // Umwandlung Legs -> "Darstellungs Objekte" für die einzelnen Fahrtabschnitte
        items = UtilsList.fillDetailedConnectionList(closeUp.trip.legs);
        adapter = new CloseUpAdapter(items, activity);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // Wenn der Nutzer auf den Button für das Anzeigen von Details klickt, sollen entweder die
        // Zwischenhalte angezeigt werden, oder die Option GoogleMaps zu öffnen
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onShowDetails(int position) {
                items.get(position).setShowDetails();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onOpenGoogleMaps(int position) {
                CloseUpPrivateItem closeUpPrivateItem = (CloseUpPrivateItem) items.get(position);
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
            }
        });
        recyclerView.setFocusable(false);
    }
}
