package com.example.lkjhgf;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.os.Bundle;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.TypefaceProvider;

import com.example.lkjhgf.Color.ButtonBootstrapBrandVisible;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypefaceProvider.registerDefaultIconSets();
        setContentView(R.layout.activity_start_view);

        ((BootstrapButton) this.findViewById(R.id.fahrtauskunft_button_start)).setBootstrapBrand(new ButtonBootstrapBrandVisible());
        this.findViewById(R.id.fahrtauskunft_button_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_single_route();
            }
        });

        ((BootstrapButton) this.findViewById(R.id.fahrtenplanen_button_start)).setBootstrapBrand(new ButtonBootstrapBrandVisible());
        this.findViewById(R.id.fahrtenplanen_button_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_fahrten_planen();
            }
        });

        ((BootstrapButton) this.findViewById(R.id.fahrtenuebersicht_button_start)).setBootstrapBrand(new ButtonBootstrapBrandVisible());

        ((BootstrapButton) this.findViewById(R.id.fahrtscheinuebersicht_button_start)).setBootstrapBrand(new ButtonBootstrapBrandVisible());

        ((BootstrapButton) this.findViewById(R.id.settings_start)).setBootstrapBrand(new ButtonBootstrapBrandVisible());

        ((BootstrapButton) this.findViewById(R.id.info_start)).setBootstrapBrand(new ButtonBootstrapBrandVisible());

        ((BootstrapButton) this.findViewById(R.id.info_start)).setBootstrapSize(2f);

        ((BootstrapButton) this.findViewById(R.id.info_start)).setBootstrapSize(2f);
        this.findViewById(R.id.info_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_information_page();
            }
        });

        ((BootstrapButton) this.findViewById(R.id.settings_start)).setBootstrapSize(2f);
        this.findViewById(R.id.settings_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_settings();
            }
        });



    }

    public void open_single_route(){
        Intent intent = new Intent(this, Single_route.class);
        startActivity(intent);
        finish();
    }

    public void open_settings(){
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
        finish();
    }

    public void open_information_page(){
        Intent intent = new Intent(this, InformationPage.class);
        startActivity(intent);
        finish();
    }

    public void open_fahrten_planen(){
        Intent intent = new Intent(this, Multiple_routes.class);
        intent.putExtra("Count", 1);
        startActivity(intent);
        finish();
    }


}
