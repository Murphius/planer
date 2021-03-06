package com.example.lkjhgf.helper.util;

import com.example.lkjhgf.optimisation.NumTicket;
import com.example.lkjhgf.optimisation.Ticket;
import com.example.lkjhgf.optimisation.TicketToBuy;
import com.example.lkjhgf.optimisation.TimeTicket;
import com.example.lkjhgf.publicTransport.provider.MyVRRprovider;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.LocationType;
import de.schildbach.pte.dto.Stop;
import de.schildbach.pte.dto.Trip;

/**
 * Enthält Funktionen, die in verschiednen Klassen verwendet werden <br/>
 * <p>
 * Viele Methoden, die einen String zurückgeben abhängig von dem Funktionsparameter
 */
public final class UtilsString {
    /**
     * Gibt die Anzahl der Umstiege einer Verbindung als String an <br/>
     * <p>
     * Ruft die Funktion {@link #setNumChanges(int numChanges)} auf
     *
     * @param trip Verbindung
     * @return String, die die Anzahl der Umstiege angibt
     */
    public static String setNumChanges(Trip trip) {
        if (trip.getNumChanges() != null) {
            return setNumChanges(trip.getNumChanges());
        } else {
            return setNumChanges(0);
        }
    }

    /**
     * Wandelt int -> String um und hängt dabei ein "X" an
     *
     * @param num_changes Anzahl Umstiege
     * @return hängt an den Parameter ein "x" an
     */
    public static String setNumChanges(int num_changes) {
        return num_changes + "x ";
    }

    /**
     * Gibt den passenden String auf deutsch zurück <br/>
     * <p>
     * Verwendet für das Füllen von Anzeigen -> {@link com.example.lkjhgf.helper.form.Form} und
     * {@link com.example.lkjhgf.helper.service.PossibleConnections}
     *
     * @param isArrivalTime gibt an, ob es sich um die Ankunftszeit oder Abfahrtszeit handelt
     * @return "Ankunftszeit" oder "Abfahrtszeit, abhängig vom Parameter
     */
    public static String arrivalDepartureTime(boolean isArrivalTime) {
        if (isArrivalTime) {
            return "Ankunftszeit";
        } else {
            return "Abfahrtszeit";
        }
    }

    /**
     * Gibt den Namen eines Ortes zurück <br/>
     * <p>
     * Der Name eines Ortes kann leider nicht einfach aus location.name und location.place
     * zusammengesetzt werden, da diese nicht einheitlich sind (Herten Herten Mitte vs. Bf)
     * Deshalb wird in dieser Funktion überprüft, ob Location.Place ein Teilstring von Location.name
     * ist. <br/>
     * Diese Funktion ist noch nicht optimal, denn trotz der Überprüfung auf Doppelungen, gibt es
     * Fälle, die so nicht gefunden werden können (Recklinghausen RE Süd Bf)
     *
     * @param location Ort für den der Name ohne Doppelungen gesucht wird
     * @return String (Name des Ortes) mit möglichst wenig Doppelungen
     */
    public static String setLocationName(Location location) {
        if (location.name != null && location.place != null) {
            //Aufteilen des Namens an den Leerzeichen
            String[] splits = location.name.split(" |, ");
            //Ausgangslage ist der "Stadtname"
            StringBuilder placeName = new StringBuilder(location.place);
            // Für jeden Teil des Namens, wird überprüft, ob dieser im neuen Namen enthlaten ist
            // ja -> überspringen
            // nein -> anhängen
            for (String split : splits) {
                if (!location.place.contains(split)) {
                    placeName.append(" ").append(split);
                }
            }
            return placeName.toString();
        } else if (location.place == null && location.name != null) { // nur der Name des Punktes
            return location.name;
        } else if (location.place != null) { // "Stadt" des Punktes und ein "?" als Kennzeichnung, dass der genaue Punkt unbekannt ist
            return location.place + " ?";
        } else {// Keine Aussage machbar
            return "?";
        }
    }

    /**
     * Wandelt ein Date-Objekt in einen Datum (String) um (Datum, nicht Zeitpunkt!) <br/>
     *
     * @param date Zeitpunkt, der formatiert werden soll
     * @return Tag des Zeitpunkts in Form von dd. MM. YYYY
     */
    public static String setDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd. MM. YYYY", Locale.GERMANY);
        return dateFormat.format(date);
    }

    /**
     * Wandelt ein Date-Objekt in eine Uhrzeit (String) um <br/>
     *
     * @param date Zeitpunkt, der formatiert werden soll
     * @return Uhrzeit in Form HH : mm
     */
    public static String setTime(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("HH : mm", Locale.GERMANY);
        return dateFormat.format(date);
    }

    /**
     * Erzeugt eine "schöne" Zeichenfolge für die Dauer einer Reise <br/>
     * <p>
     * -> keine führende Null <br/>
     * -> keine "0h"... <br/>
     * -> abhängig von der Anzahl der Minuten eine führende 0 -> "x h 0y" <br/>
     * -> min nur als Nachsatz, wenn Stunden = 0
     *
     * @param durationHours   Anzahl der Stunden
     * @param durationMinutes Anzahl der Minuten
     * @return String der die Dauer der Fahrt angibt
     */
    public static String durationString(long durationHours, long durationMinutes) {
        String result = "";
        if (durationHours >= 1) {
            result += durationHours + "h ";
        }
        if (durationMinutes < 1) {
            result += "00";
        } else if (durationMinutes < 10 & durationHours >= 1) {
            result += "0" + durationMinutes;
        } else if (durationMinutes < 10) {
            result += durationMinutes + "min";
        } else {
            result += durationMinutes + "min";
        }
        return result;
    }

    /**
     * Gibt das Abfahrts-, oder Ankunftsgleis als String zurück <br/>
     * <p>
     * Wenn möglich wird das aktuell vorgesehene Gleis zurückgegeben, alternativ das geplante
     * Gleis; <br/>
     * Sollten beide Informationen nicht zur Verfügung stehen, wird ein ? ausgegeben (& der Nutzer muss
     * gucken wie er das Gleis findet)
     *
     * @param stop      Haltestelle
     * @param isArrival ob Start oder Ziel
     * @return String bestehend aus Gleis + entsprechender Bezeichnung
     */
    public static String platform(Stop stop, boolean isArrival) {
        String platform = "Gleis ";
        if (isArrival) {
            if (stop.predictedArrivalPosition != null) {
                return platform + stop.predictedArrivalPosition.toString();
            } else if (stop.plannedArrivalPosition != null) {
                return platform + stop.plannedArrivalPosition.toString();
            }
        } else {
            if (stop.predictedDeparturePosition != null) {
                return platform + stop.predictedDeparturePosition.toString();
            } else if (stop.plannedDeparturePosition != null) {
                return platform + stop.plannedDeparturePosition.toString();
            }
        }
        return platform + "?";
    }

    /**
     * Wandelt einen Cent Betrag in € um, mit korrektor Formatierung
     *
     * @param costs Kosten in Cent, welche angezeigt werden sollen
     * @return String Cent-Betrag in Euro mit deutscher Formatierung
     */
    public static String centToString(int costs) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        double currencyAmmount = (costs / 100) + (double) (costs % 100) / 100;
        return currencyFormatter.format(currencyAmmount);
    }

    /**
     * Liefert die Preisstufe einer Fahrt <br/>
     *
     * @param trip Trip dessen Preisstufe gesucht wird
     * @return String, der die Preisstufe der Fahrt angibt
     */
    public static String setPreisstufenName(Trip trip) {
        if (trip.fares != null) {
            return trip.fares.get(0).units;
        } else {
            return "?";
        }
    }

    /**
     * Liefert den Endzeitpunkt eines Tickets <br/>
     * <p>
     * Für Zeittickets ist der Endzeitpunkt je nach Ticket nicht einfach nur die Startzeit plus
     * x Stunden, sondern abhängig von der Startzeit. <br/>
     * Zusätzlich wird hier das Datum des Endzeitpunkts korrekt als String formatiert (ggf. muss z.B.
     * +1 gerechnet werden)
     *
     * @param ticketToBuy Ticket dessen Endzeitpunkt ermittelt werden soll
     * @return String der den Endzeitpunkt des Tickets angibt - "" bei NumTicket
     */
    public static String endDate(TicketToBuy ticketToBuy) {
        if (ticketToBuy.getTicket() instanceof NumTicket) {
            return "";
        }
        TimeTicket t = (TimeTicket) ticketToBuy.getTicket();
        Calendar c = Calendar.getInstance();
        if (t.equals(MyVRRprovider.happyHourTicket)) {
            c.setTimeInMillis(ticketToBuy.getFirstDepartureTime().getTime());
            int startH = c.get(Calendar.HOUR_OF_DAY);
            if (startH < 6) {
                return setDate(ticketToBuy.getFirstDepartureTime()) + " 06 : 00";
            } else {
                c.setTimeInMillis(c.getTimeInMillis() + 24 * 60 * 60 * 1000);
                return setDate(c.getTime()) + " 6h 00";
            }
        } else if (t.equals(MyVRRprovider.vierStundenTicket)) {
            c.setTimeInMillis(ticketToBuy.getFirstDepartureTime().getTime());
            if (!(c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
                if (c.get(Calendar.HOUR_OF_DAY) >= 23) {
                    c.setTimeInMillis(c.getTimeInMillis() + 24 * 60 * 60 * 1000);
                    String result = setDate(c.getTime());
                    c.setTimeInMillis(c.getTimeInMillis() - 20 * 60 * 60 * 1000);
                    return result + " " + setTime(c.getTime());
                } else if (c.get(Calendar.HOUR_OF_DAY) < 3) {
                    return setDate(c.getTime()) + " 03 : 00";
                }
            }
        }
        c.setTimeInMillis(ticketToBuy.getFirstDepartureTime().getTime() + t.getMaxDuration());
        return setDate(c.getTime()) + " " +
                setTime(c.getTime());
    }

    /**
     * Erzeugt den zusammenhängenden String aus den Parametern
     *
     * @param ticketsToUse    zu nutzende Fahrscheine
     * @param num             wie oft dieses Ticket gebraucht wird
     * @param preisstufeToUse Preisstufe des Tickets
     * @return Stringbuilder zusammengebaut aus den Informationen
     */
    public static StringBuilder buildStringTicketList(ArrayList<Ticket> ticketsToUse, ArrayList<Integer> num, ArrayList<String> preisstufeToUse) {
        StringBuilder value = new StringBuilder();
        for (int i = 0; i < ticketsToUse.size(); i++) {
            if (ticketsToUse.get(i) instanceof NumTicket) {
                NumTicket numTicket = (NumTicket) ticketsToUse.get(i);
                int wholeTickets = num.get(i) / numTicket.getNumTrips();
                int restTicket = num.get(i) % numTicket.getNumTrips();
                if (wholeTickets != 0) {
                    value.append(wholeTickets).append("x ").append(numTicket.toString()).append("\n \tPreisstufe: ").append(preisstufeToUse.get(i)).append("\n \talle Fahrten entwerten");
                }
                if (restTicket != 0) {
                    value.append("1x ").append(numTicket.toString()).append(" \n \tPreisstufe: ").append(preisstufeToUse.get(i)).append("\n \t").append(restTicket).append("x entwerten");
                }
            } else {
                TimeTicket timeTicket = (TimeTicket) ticketsToUse.get(i);
                int wholeTickets = num.get(i);
                value.append(wholeTickets).append("x ").append(timeTicket.toString()).append("\n \tPreisstufe: ").append(preisstufeToUse.get(i));
            }
            if (i != ticketsToUse.size() - 1) {
                value.append("\n");
            }
        }
        return value;
    }

}
