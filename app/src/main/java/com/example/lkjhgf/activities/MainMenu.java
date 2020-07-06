package com.example.lkjhgf.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.TypefaceProvider;

import com.example.lkjhgf.activities.ticketOverview.TicketOverview;
import com.example.lkjhgf.color.ButtonBootstrapBrandVisible;
import com.example.lkjhgf.R;
import com.example.lkjhgf.activities.futureTrips.Complete;
import com.example.lkjhgf.activities.singleTrip.UserForm;
import com.example.lkjhgf.publicTransport.provider.MyProvider;
import com.example.lkjhgf.publicTransport.provider.MyVRRprovider;

/**
 * Hauptmenü, je nachdem, auf welchen Button der Nutzer klickt, wird die jeweilige Ansicht geöffnet
 */
public class MainMenu extends Activity {
    public static String NUM_PERSONS_PER_CLASS = "com.example.lkjhgf.main_menu.NUM_NUM_PERSONS_PER_CLASS";
    public static String EXTRA_NUM_TRIP = "com.example.lkjhgf.main_menu.EXTRA_NUM_TRIP";
    public static String EXTRA_TRIP = "com.example.lkjhgf.main_menu.EXTRA_TRIP";
    public static String EXTRA_CLASS = "com.example.lkjhgf.main_menu.EXTRA_CLASS";

    //An dieser Stelle -> ändern für andere Provider
    public static MyProvider myProvider = new MyVRRprovider();

    /**
     * Planen einer einzelnen Fahrt, keine Fahrkostenoptimierung
     */
    public void change_view_to_single_route() {
        Intent intent = new Intent(this, UserForm.class);
        startActivity(intent);
    }

    /**
     * Öffnen der Einstellungen
     */
    public void change_view_to_settings() {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    /**
     * Anzeigen einer Hilfsseite, mit Erklärungen zur Funktionsweise App
     */
    public void change_view_to_information_page() {
        Intent intent = new Intent(this, Informationpage.class);
        startActivity(intent);
    }

    /**
     * Planen mehrerer Fahrten, diese werden jeweils bei der Fahrkostenoptimierung berücksichtigt
     */
    public void change_view_to_multiple_routes() {
        Intent intent = new Intent(this, com.example.lkjhgf.activities.multipleTrips.UserForm.class);
        intent.putExtra(EXTRA_NUM_TRIP, 1);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypefaceProvider.registerDefaultIconSets();
        setContentView(R.layout.activity_start_view);

        // Initialisierung, Übersichtlichkeit: Button mit Ziffer -> Name
        BootstrapButton single_route = findViewById(R.id.BootstrapButton1);
        BootstrapButton multiple_routes = findViewById(R.id.BootstrapButton2);
        BootstrapButton future_connections = findViewById(R.id.BootstrapButton3);
        BootstrapButton ticket_overwiew = findViewById(R.id.BootstrapButton4);
        BootstrapButton information_page = findViewById(R.id.BootstrapButton5);
        BootstrapButton settings = findViewById(R.id.BootstrapButton6);

        // Buttonlayout
        single_route.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        multiple_routes.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        future_connections.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        ticket_overwiew.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        information_page.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        settings.setBootstrapBrand(new ButtonBootstrapBrandVisible());
        settings.setBootstrapSize(2f);
        information_page.setBootstrapSize(2f);


        // OnKlick Listener für die einzelnen Button
        single_route.setOnClickListener(v -> change_view_to_single_route());

        multiple_routes.setOnClickListener(v -> change_view_to_multiple_routes());

        future_connections.setOnClickListener(v -> {
            Intent intent = new Intent(this, Complete.class);
            startActivity(intent);
        });

        ticket_overwiew.setOnClickListener(v -> {
            Intent intent = new Intent(this, TicketOverview.class);
            startActivity(intent);
        });

        information_page.setOnClickListener(v -> change_view_to_information_page());

        settings.setOnClickListener(v -> change_view_to_settings());
    }

}
