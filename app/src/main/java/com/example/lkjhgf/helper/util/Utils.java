package com.example.lkjhgf.helper.util;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapText;
import com.example.lkjhgf.R;

import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * Enthält Funktionen, die in verschiednen Klassen verwendet werden <br/>
 * <p>
 * Verschiedene Funktionen, wie Umrechnen von Einheiten, Text direkt setzen
 */
public final class Utils {

    /**
     * Ändert beim Button den Text in Ankunfzeit bzw. Abfahrtszeit, abhängig vom Parameter <br/>
     * <p>
     * Nutzt die Funktion {@link UtilsString#arrivalDepartureTime(boolean)}
     *
     * @param button        Button der den anderen Text erhalten soll
     * @param isArrivalTime gibt an ob auf dem Button Ankunftszeit oder Abfahrtszeit stehen soll
     * @preconditions der Parameter isArrivalTime wurde vorher verneint
     */
    public static void setArrivalDepartureButton(BootstrapButton button, boolean isArrivalTime) {
        Context context = button.getContext();
        BootstrapText.Builder builder = new BootstrapText.Builder(context);
        builder.addText(UtilsString.arrivalDepartureTime(isArrivalTime));
        button.setBootstrapText(builder.build());
    }

    /**
     * Umrechnung long (millisekunden) -> int (Minuten)
     *
     * @param l Long der umgweandelt werden soll
     * @return Parameter in Minuten umgewandelt
     */
    public static int longToInt(long l) {
        return (int) (l / (1000 * 60) % 60);
    }

    /**
     * Umrechnung Millisekunden -> Stunden
     *
     * @param duration Parameter der umgewandelt werden soll
     * @return Anzahl an Stunden als Long
     */
    public static long durationToHour(long duration) {
        return TimeUnit.HOURS.convert(duration, TimeUnit.MILLISECONDS) % 24;
    }

    /**
     * Umrechnung Millisekunden -> Minuten (long)
     *
     * @param duration Parameter in ms der umgweandelt werden soll
     * @return Anzahl Minuten als long
     */
    public static long durationToMinutes(long duration) {
        return TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS) % 60;
    }

    /**
     * Füllt die Anzeige mit der Verspätung <br/>
     * <p>
     * Wenn die Verspätung 0min ist, wird die Anzeige vor dem Nutzer verborgen <br/>
     * Ist die Verspätung <= 5 Minuten, so wird die Verspätung grün eingefärbt,
     * sonst rot
     *
     * @param textView  - Anzeige die gefüllt werden soll
     * @param delay     - Verspätung in Minuten
     * @param resources - für die Farben der Verspätung benötigt
     */
    public static void setDelayView(TextView textView, int delay, Resources resources) {
        String text = "+ " + delay;
        textView.setText(text);
        if (delay == 0) {
            textView.setVisibility(View.INVISIBLE);
        } else if (delay <= 5) {
            textView.setTextColor(resources.getColor(R.color.my_green, null));
        } else {
            textView.setTextColor(resources.getColor(R.color.maroon, null));
        }
    }

    public static boolean isOverlapping(Date start1, Date end1, Date start2, Date end2) {
        return start1.before(end2) && start2.before(end1);
    }
}
