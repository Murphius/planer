package com.example.lkjhgf.helper.closeUp;

import android.app.Activity;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.util.UtilsList;
import com.example.lkjhgf.recyclerView.detailedView.CloseUpAdapter;
import com.example.lkjhgf.recyclerView.detailedView.CloseUpPrivateItem;
import com.example.lkjhgf.recyclerView.detailedView.CloseUpPublicItem;
import com.example.lkjhgf.recyclerView.detailedView.CloseUpItem;

import java.util.ArrayList;

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
        adapter.setOnItemClickListener(position -> {
            items.get(position).setShowDetails();
            adapter.notifyDataSetChanged();
        });
        recyclerView.setFocusable(false);
    }
}
