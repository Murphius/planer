package com.example.lkjhgf.activities;

import android.app.Activity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.lkjhgf.R;

public class Informationpage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_page);

        TextView textView = findViewById(R.id.textView102);

        String text = "Alle Verbindungsangaben ohne Gewähr. \n";
        text += "Quelle für die Verbindungsangaben: Verkehrsverbund Rhein Ruhr \n";
        text+= "\n";
        text += "Basis für die App: Öffi \n";
        text += "\n";
        text += "Quelle für die Icon-Symbole: \n";
        text += "\tBus, Tram, Zug, U-Bahn, Fähre, Gondel, S-Bahn, Fernzug, AST: Freepik\n";
        text += "\tLaufen, Uhr, Fahrrad: Freepik\n";
        text += "\tHaltestelle: Freepik\n";
        text += "\tAdresse: iconixar\n";
        text += "\tPOI: surang\n";

        textView.setText(text);




    }

}

