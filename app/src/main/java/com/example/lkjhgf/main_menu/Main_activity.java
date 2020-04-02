package com.example.lkjhgf.main_menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.TypefaceProvider;

import com.example.lkjhgf.Color.ButtonBootstrapBrandVisible;
import com.example.lkjhgf.R;
import com.example.lkjhgf.helper.Utils;
import com.example.lkjhgf.trip.singleTrip.StartView_Form;

public class Main_activity extends Activity {

    public static String EXTRA_NUMBER = "com.example.lkjhgf.main_menu.EXTRA_NUMBER";

    public void change_view_to_single_route(){
        Intent intent = new Intent(this, StartView_Form.class);
        startActivity(intent);
    }

    public void change_view_to_settings(){
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public void change_view_to_information_page(){
        Intent intent = new Intent(this, Informationpage.class);
        startActivity(intent);
    }

    public void change_view_to_multiple_routes(){
        Intent intent = new Intent(this, com.example.lkjhgf.trip.multipleTrips.StartView_Form.class);
        intent.putExtra(EXTRA_NUMBER, 1);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        System.out.println(Utils.setLocationName("Münster", "Münster(Westf)") + "------------------------------------------");
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
            //TODO
        });

        ticket_overwiew.setOnClickListener(v -> {
            //TODO
        });

        information_page.setOnClickListener(v -> change_view_to_information_page());

        settings.setOnClickListener(v -> change_view_to_settings());
    }

}
