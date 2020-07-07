package com.example.lkjhgf.helper.util;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapText;
import com.example.lkjhgf.R;
import com.example.lkjhgf.optimisation.Ticket;
import com.example.lkjhgf.recyclerView.futureTrips.TripTicketInformationHolder;

import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import de.schildbach.pte.dto.Fare;


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
     * Ist die Verspätung <= 5 Minuten, so wird die Verspätung grün eingefärbt,
     * sonst rot.
     * Setzt die "echte" Zeit
     * @param textView  - Anzeige die gefüllt werden soll
     * @param delay     - Verspätung in ms
     * @param plannedTime - geplante Abfahrtszeit
     * @param resources - für die Farben der Verspätung benötigt
     */
    private static void setDelayView(TextView textView, long delay, Date plannedTime, Resources resources) {
        if(plannedTime == null){
            textView.setText("");
        }
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(plannedTime.getTime() + delay);
        textView.setText(UtilsString.setTime(c.getTime()));
        if (longToInt(delay) <= 5) {
            textView.setTextColor(resources.getColor(R.color.my_green, null));
        } else {
            textView.setTextColor(resources.getColor(R.color.berry, null));
        }
    }

    /**
     * Füllt die Anzeige mit der Verspätung <br/>
     * <p>
     * Ist die Verspätung <= 5 Minuten, so wird die Verspätung grün eingefärbt,
     * sonst rot.
     * Setzt die "echte" Zeit
     * @param textView  - Anzeige die gefüllt werden soll
     * @param delay     - Verspätung in ms
     * @param plannedTime - geplante Abfahrtszeit
     * @param resources - für die Farben der Verspätung benötigt
     * @param hideView - gibt an, ob bei keiner Verspätung das Textfeld für Verspätungen angezeigt werden soll oder nicht
     */
    public static void setDelayView(TextView textView, long delay, Date plannedTime, Resources resources, boolean hideView) {
       setDelayView(textView, delay, plannedTime, resources);
       if(hideView && Utils.longToInt(delay) == 0){
           textView.setVisibility(View.INVISIBLE);
       }
    }


    /**
     * Füllt die Anzeige mit der Verspätung <br/>
     * <p>
     * Ist die Verspätung <= 5 Minuten, so wird die Verspätung grün eingefärbt,
     * sonst rot.
     * Setzt die "echte" Zeit
     * @param textView  - Anzeige die gefüllt werden soll
     * @param delay     - Verspätung in Minuten
     * @param resources - für die Farben der Verspätung benötigt
     */
    public static void setDelayView(TextView textView, int delay, Resources resources) {
        String text = "+ " + delay;
        textView.setText(text);
        if(delay == 0){
            textView.setVisibility(View.INVISIBLE);
        }
        if (delay <= 5) {
            textView.setTextColor(resources.getColor(R.color.my_green, null));
        } else {
            textView.setTextColor(resources.getColor(R.color.berry, null));
        }
    }

    /**
     * Prüft ob zwei Intervalle sich überlappen
     *
     * @param start1 Startzeit von Fahrt 1
     * @param end1 Endezeit von Fahrt 1
     * @param start2 Startzeit von Fahrt 2
     * @param end2 Endzeit von Fahrt 2
     * @return true wenn sich die Fahrten überlappen, sonst false
     */
    public static boolean isOverlapping(Date start1, Date end1, Date start2, Date end2) {
        return start1.before(end2) && start2.before(end1);
    }

    /**
     * Fasst die Tickets mit fester Fahrtenanzahl zusammen, und zählt die entsprechende Häufigkeit
     *
     * @return Ein Triple mit den Tickets, der jeweiligen Häufigkeit und den Preisstufen
     */
    public static Triplet<ArrayList<Ticket>, ArrayList<Integer>, ArrayList<String>> shortTicketInformationNumTicket(HashMap<Fare.Type, ArrayList<TripTicketInformationHolder>> allTicketInformations) {
        ArrayList<Ticket> ticketsToUse = new ArrayList<>();
        ArrayList<String> preisstufeToUse = new ArrayList<>();
        ArrayList<Integer> num = new ArrayList<>();

        for (Fare.Type type : allTicketInformations.keySet()) {
            ArrayList<TripTicketInformationHolder> ticketList = allTicketInformations.get(type);
            if (ticketList != null && !ticketList.isEmpty()) {
                ticketsToUse.add(ticketList.get(0).getTicket());
                preisstufeToUse.add(ticketList.get(0).getTicketFarezone());
                num.add(ticketList.get(0).getQuantity());
                for (int i = 1; i < ticketList.size(); i++) {
                    boolean contains = false;
                    for (int j = 0; j < ticketsToUse.size() && !contains; j++) {
                        if (ticketList.get(i).getTicket().getName().equals(ticketsToUse.get(j).getName())) {
                            if (ticketList.get(i).getTicketFarezone().equals(preisstufeToUse.get(j))) {
                                num.set(j, num.get(j) + ticketList.get(i).getQuantity());
                            } else {
                                ticketList.add(ticketList.get(i));
                                preisstufeToUse.add(ticketList.get(i).getTicketFarezone());
                                num.add(ticketList.get(i).getQuantity());
                            }
                            contains = true;
                        }
                    }
                    if (!contains) {
                        ticketsToUse.add(ticketList.get(i).getTicket());
                        preisstufeToUse.add(ticketList.get(i).getTicketFarezone());
                        num.add(ticketList.get(i).getQuantity());
                    }
                }
            }
        }
        return Triplet.with(ticketsToUse, num, preisstufeToUse);
    }
}
