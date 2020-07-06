package com.example.lkjhgf.activities;

import android.app.Activity;

import android.os.Bundle;
import android.widget.TextView;
import android.text.util.Linkify;

import com.example.lkjhgf.R;

import java.util.regex.Pattern;

/**
 * Informationen zur Quelle der Icons, Verbindungsinformationen, ...
 */
public class Informationpage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_page);

        TextView textView = findViewById(R.id.textView102);

        String text = "Alle Verbindungs- und Ticketangaben ohne Gewähr. \n\n";
        text += "Quelle für die Verbindungsangaben: Verkehrsverbund Rhein Ruhr \n";
        text+= "\n";
        text += "Basis für die App: Öffi \n";
        text += "\n";
        text += "Quellen für die Icon-Symbole: \n";
        text += "\tBus, Tram, Zug, U-Bahn, Fähre, Gondel, S-Bahn, Fernzug, AST: Freepik from www.flaticon.com\n";
        text += "\tLaufen, Uhr, Fahrrad: Freepik from www.flaticon.com\n";
        text += "\tHaltestelle: Freepik from www.flaticon.com\n";
        text += "\tAdresse: iconixar from www.flaticon.com\n";
        text += "\tPOI: surang from www.flaticon.com\n";

        textView.setText(text);

        Pattern pattern1 = Pattern.compile("surang");
        Pattern pattern2 = Pattern.compile("iconixar");
        Pattern pattern3 = Pattern.compile("Freepik");
        Pattern pattern4 = Pattern.compile("Öffi");
        Pattern pattern5 = Pattern.compile("Verkehrsverbund Rhein Ruhr");

        Linkify.addLinks(textView, Linkify.WEB_URLS );
        Linkify.addLinks(textView, pattern1, "https://www.flaticon.com/authors/", null, null );
        Linkify.addLinks(textView, pattern2, "https://www.flaticon.com/authors/", null, null );
        Linkify.addLinks(textView, pattern3, "https://www.flaticon.com/authors/", null, null );
        Linkify.addLinks(textView, pattern4, "https://github.com/schildbach/public-transport-enabler", null, (s,u) -> u.replace("Öffi",""));
        Linkify.addLinks(textView, pattern5, "https://www.vrr.de/de/startseite/", null, (s,u) -> u.replace("Verkehrsverbund Rhein Ruhr",""));

    }

}

