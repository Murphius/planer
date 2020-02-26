package com.example.lkjhgf;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.os.Bundle;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.TypefaceProvider;

import com.example.lkjhgf.Color.ButtonBootstrapBrandVisible;

public class MainActivity extends Activity {

    private BootstrapButton single_route, multiple_routes, future_connections, ticket_overwiew, information_page, settings;

    public void change_view_to_single_route(){
        Intent intent = new Intent(this, Single_route.class);
        startActivity(intent);
        finish();
    }

    public void change_view_to_settings(){
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public void change_view_to_information_page(){
        Intent intent = new Intent(this, InformationPage.class);
        startActivity(intent);
    }

    public void change_view_to_multiple_routes(){
        Intent intent = new Intent(this, Multiple_routes.class);
        intent.putExtra("Count", 1);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        TypefaceProvider.registerDefaultIconSets();
        setContentView(R.layout.activity_start_view);

        // Initialisierung, Übersichtlichkeit: Button mit Ziffer -> Name

        single_route = findViewById(R.id.BootstrapButton1);
        multiple_routes = findViewById(R.id.BootstrapButton2);
        future_connections = findViewById(R.id.BootstrapButton3);
        ticket_overwiew = findViewById(R.id.BootstrapButton4);
        information_page = findViewById(R.id.BootstrapButton5);
        settings = findViewById(R.id.BootstrapButton6);

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
        single_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_view_to_single_route();
            }
        });

        multiple_routes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_view_to_multiple_routes();
            }
        });

        future_connections.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });

        ticket_overwiew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        information_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_view_to_information_page();
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_view_to_settings();
            }
        });
    }

}
