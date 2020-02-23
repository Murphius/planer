package com.example.lkjhgf;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapText;
import com.example.lkjhgf.Color.ButtonBootstrapBrandInvisible;
import com.example.lkjhgf.Color.ButtonBootstrapBrandVisible;

public class Multiple_routes extends Activity {

    private boolean isArrivalTime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_routes);

        ((BootstrapButton) this.findViewById(R.id.date_button)).setBootstrapBrand(new ButtonBootstrapBrandInvisible());

        int count = getIntent().getIntExtra("Count", 1);

        ((TextView) this.findViewById(R.id.multiple_routes_name)).setText(count + ". Fahrt");

        ((BootstrapButton) this.findViewById(R.id.an_abfahrt_button_einzelne_verbindung)).setBootstrapBrand(new ButtonBootstrapBrandVisible());
        final BootstrapButton btn = this.findViewById(R.id.an_abfahrt_button_einzelne_verbindung);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isArrivalTime = !isArrivalTime;
                Context context = btn.getContext();
                BootstrapText.Builder builder = new BootstrapText.Builder(context);

                if (isArrivalTime) {
                    builder.addText(context.getText(R.string.ankunft));
                } else {
                    builder.addText(context.getText(R.string.abfahrt));
                }

                btn.setBootstrapText(builder.build());

            }
        });

        ((BootstrapButton) this.findViewById(R.id.start_button)).setBootstrapBrand(new ButtonBootstrapBrandInvisible());

        ((BootstrapButton) this.findViewById(R.id.stopover_button)).setBootstrapBrand(new ButtonBootstrapBrandInvisible());

        ((BootstrapButton) this.findViewById(R.id.destination_button)).setBootstrapBrand(new ButtonBootstrapBrandInvisible());

        ((BootstrapButton) this.findViewById(R.id.cancel_button)).setBootstrapBrand(new ButtonBootstrapBrandVisible());
        this.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_main_activity();
            }
        });

        ((BootstrapButton) this.findViewById(R.id.settings_button)).setBootstrapBrand(new ButtonBootstrapBrandVisible());
        this.findViewById(R.id.settings_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_settings();
            }
        });

        ((BootstrapButton) this.findViewById(R.id.accept_button)).setBootstrapBrand(new ButtonBootstrapBrandVisible());
    }

    public void open_main_activity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void open_settings(){
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }
}
