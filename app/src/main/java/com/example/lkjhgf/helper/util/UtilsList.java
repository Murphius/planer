package com.example.lkjhgf.helper.util;

import com.example.lkjhgf.R;
import com.example.lkjhgf.recyclerView.detailedView.CloseUpItem;
import com.example.lkjhgf.recyclerView.detailedView.CloseUpPrivateItem;
import com.example.lkjhgf.recyclerView.detailedView.CloseUpPublicItem;
import com.example.lkjhgf.recyclerView.detailedView.components.StopoverItem;
import com.example.lkjhgf.recyclerView.possibleConnections.ConnectionItem;
import com.example.lkjhgf.recyclerView.possibleConnections.components.JourneyItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import de.schildbach.pte.dto.Product;
import de.schildbach.pte.dto.Stop;
import de.schildbach.pte.dto.Trip;

/**
 *   Enthält Funktionen, die in verschiednen Klassen verwendet werden <br/>
 *   <p>
 *   Das Erzeugen der Listen für die RecyclerViews findet hier statt
 */
public final class UtilsList {
    /**
     * Wandelt eine Liste an Trips in eine Liste von ConnectionItems um <br/>
     * <p>
     * Die Umwandlung erfolgt, um den RecyclierView ({@link com.example.lkjhgf.helper.service.PossibleConnections})
     * zu füllen. <br/>
     * <p>
     * Ruft {@link #journeyItems(List Trip.Legs)} auf -> Kompakte Ansicht der Abfolge von Verkehrsmitteln
     *
     * @param trips Liste an möglichen Verbindungen
     * @return Liste mit {@link ConnectionItem}, für die Service Ansicht
     */
    public static ArrayList<ConnectionItem> fillConnectionList(List<Trip> trips) {
        // Keine Ergebnisse gefunden
        if (trips == null) {
            return new ArrayList<>();
        }
        ArrayList<ConnectionItem> connection_items = new ArrayList<>();
        for (Trip trip : trips) {
            if (trip.isTravelable()) {
                connection_items.add(new ConnectionItem(trip, journeyItems(trip.legs)));
            }
        }
        return connection_items;
    }
    /**
     * Wandelt eine Liste an Trips in eine Liste von ConnectionItems um, deren Fahrten alle in der Zukunft liegen <br/>
     * <p>
     * Die Umwandlung erfolgt, um den RecyclierView ({@link com.example.lkjhgf.helper.service.PossibleConnections})
     * zu füllen. <br/>
     * <p>
     * Ruft {@link #journeyItems(List Trip.Legs)} auf -> Kompakte Ansicht der Abfolge von Verkehrsmitteln
     *
     * @param trips Liste an möglichen Verbindungen
     * @return Liste mit {@link ConnectionItem}, für die Service Ansicht, welche Fahrten in der Zukunft liegen
     */
    public static ArrayList<ConnectionItem> fillFutureConnectionList(List<Trip> trips){
        if (trips == null){
            return new ArrayList<>();
        }
        ArrayList<ConnectionItem> connectionItems = new ArrayList<>();
        for(Iterator<Trip> iterator = trips.iterator(); iterator.hasNext(); ){
            Trip trip = iterator.next();
            if(trip.getFirstDepartureTime().before(Calendar.getInstance().getTime())){
                iterator.remove();
            }else if(trip.isTravelable()){
                connectionItems.add(new ConnectionItem(trip, journeyItems(trip.legs)));
            }
        }
        return connectionItems;
    }

    /**
     * Erzeugt aus der Liste mit Fahrtabschnitten einer Verbindung eine kompakte übersicht <br/>
     * <p>
     * Für jedes Leg wird nur der Typ (Icon) und die Linie (für ÖPNV) oder die Dauer (für individuelle Abschnitte)
     * hinterlegt -> ermöglicht einen schnellen Überblick <br/>
     * <p>
     * Für Abschnitte, die mit dem ÖPNV zurückgelegt werden, wird die Funktion {@link #publicItems(Trip.Public)}
     * aufgerufen. <br/>
     * Für individuell zurückzulegende Abschnitte hingegen die Funktion {@link #individualItems(Trip.Individual)}.
     *
     * @param legs Liste mit Fahrtabschnitten {@link Trip.Leg}
     * @return Liste mit {@link JourneyItem}, für die schnelle Übersicht, welche Verbindungen genutzt
     * werden
     */
    public static ArrayList<JourneyItem> journeyItems(List<Trip.Leg> legs) {
        ArrayList<JourneyItem> journeyItems = new ArrayList<>();
        for (Trip.Leg leg : legs) {
            if (leg instanceof Trip.Public) {
                journeyItems.add(publicItems((Trip.Public) leg));
            } else {
                journeyItems.add(individualItems((Trip.Individual) leg));
            }
        }
        return journeyItems;
    }

    /**
     * Liefert die wichtigsten Informationen zum Überblick über den Fahrtabschnitt<br/>
     * <p>
     * Enthält das Icon des Types sowie den Linienbezeichner
     *
     * @param publicTrip ein öffentlicher Fahrtabschnitt
     * @return eine Kompaktansicht eines öffentlichen Fahrtabschnitts
     */
    private static JourneyItem publicItems(Trip.Public publicTrip) {
        String name = publicTrip.line.label;
        Product product = publicTrip.line.product;
        if (product == null) {
            return new JourneyItem(R.drawable.ic_android, name);
        }
        int icon = iconPublic(publicTrip.line.product);
        return new JourneyItem(icon, name);
    }

    /**
     * Liefert die wichtigsten Informationen für einen individuell zurückgelegten Abschnitt <br/>
     * <p>
     * Enthält das Icon für die Art des Abschnitts, sowie dessen Dauer in Minuten
     *
     * @param individualTrip ein individueller Fahrtabschnitt
     * @return eine Kompaktübersicht eines individuellen Fahrtabschnitts
     */
    private static JourneyItem individualItems(Trip.Individual individualTrip) {
        String time = individualTrip.min + " min";
        int icon = iconIndividual(individualTrip.type);
        return new JourneyItem(icon, time);
    }

    /**
     * Erzeugt eine Liste für die Zwischenhalte, welche für den RecyclerView genutzt wird <br/>
     * <p>
     * Liste mit Objekten der Klasse {@link StopoverItem}, in welchen Name der Haltestelle, Abfahrtszeit,
     * sowie ggf. Verspätung gespeichert sind.
     *
     * @param publicTrip Einzelner Verbindungsabschnitt des ÖPNV
     * @return Liste aller Zwischenhalte mit den Details zu dem jeweiligen Halt
     */
    public static ArrayList<StopoverItem> createStopoverList(Trip.Public publicTrip) {
        ArrayList<StopoverItem> stopoverItems = new ArrayList<>();

        List<Stop> stops = publicTrip.intermediateStops;

        if (stops != null) {
            for (Stop stop : stops) {
                //TODO für den ersten Zwischenhalt prüfen
                int delay;
                if (stop.getDepartureDelay() != null) { // Wenn die Funktion nicht null liefert, gibt es eine geplante Abfahrtszeit
                    delay = Utils.longToInt(stop.getDepartureDelay());
                    stopoverItems.add(new StopoverItem(UtilsString.setTime(stop.plannedDepartureTime),
                            UtilsString.setLocationName(stop.location),
                            delay));
                } // "Ausprobieren", welche Uhrzeit hinterlegt ist -> die erste von diesen wird genutzt
                else if (stop.plannedDepartureTime != null) {
                    stopoverItems.add(new StopoverItem(UtilsString.setTime(stop.plannedDepartureTime),
                            UtilsString.setLocationName(stop.location),
                            0));
                } else if (stop.predictedDepartureTime != null) {
                    stopoverItems.add(new StopoverItem(UtilsString.setTime(stop.predictedDepartureTime),
                            UtilsString.setLocationName(stop.location),
                            0));
                } else if (stop.plannedArrivalTime != null) {
                    stopoverItems.add(new StopoverItem(UtilsString.setTime(stop.plannedArrivalTime),
                            UtilsString.setLocationName(stop.location),
                            0));
                } else if (stop.predictedArrivalTime != null) {
                    stopoverItems.add(new StopoverItem(UtilsString.setTime(stop.predictedArrivalTime),
                            UtilsString.setLocationName(stop.location),
                            0));
                } else { // keine Zeit hinterlegt -> leere Zeit
                    stopoverItems.add(new StopoverItem("", UtilsString.setLocationName(stop.location), 0));
                }
            }
        }

        return stopoverItems;
    }

    /**
     * Findet das entsprechende Symbol, für den genutzten ÖPNV-Typ
     *
     * @param product ÖPNV-Typ
     * @return Icon, der dem Typ entspricht
     */
    public static int iconPublic(Product product) {
        if (product == null) {
            return R.drawable.ic_android;
        }
        switch (product) {
            case HIGH_SPEED_TRAIN:
                return R.drawable.ic_non_regional_traffic;
            case SUBWAY:
                return R.drawable.ic_underground_train;
            case SUBURBAN_TRAIN:
                return R.drawable.ic_s_bahn;
            case TRAM:
                return R.drawable.ic_tram;
            case CABLECAR:
                return R.drawable.ic_gondola;
            case FERRY:
                return R.drawable.ic_ship;
            case REGIONAL_TRAIN:
                return R.drawable.ic_regionaltrain;
            case BUS:
                return R.drawable.ic_bus;
            case ON_DEMAND:
                return R.drawable.ic_taxi;
            default:
                return R.drawable.ic_android;
        }
    }

    /**
     * Findet zum individuellen Verbindungstyp ein passendes Icon <br/>
     *
     * @param individualType gibt den Type des Verbindungsabschnitts an
     * @return Icon, der den jeweiligen Type repräsentiert
     */
    public static int iconIndividual(Trip.Individual.Type individualType) {
        switch (individualType) {
            case WALK:
                return R.drawable.ic_walk;
            case TRANSFER:
                return R.drawable.ic_time;
            case CAR:
                return R.drawable.ic_taxi;
            case BIKE:
                return R.drawable.ic_bike;
            default:
                return R.drawable.ic_android;
        }
    }

    /**
     * "Formt" die Liste der Fahrtabschnitte "um", so dass diese für den RecyclerView genutzt werden können<br/>
     * <p>
     * Abhängig davon, ob es sich um einen öffentlichen oder individuellen Abschnitt handlet, wird ein Objekt
     * von zwei möglichen Klassen, die von {@link CloseUpItem} erben, erstellt. <br/>
     * inidvidueller Abschnitt: {@link CloseUpPrivateItem} <br/>
     * öffentlicher Abschnitt: {@link CloseUpPublicItem} <br/>
     * <p>
     * Genutzt wird die neue Liste für die detaillierte Ansicht einer Fahrt
     * ({@link com.example.lkjhgf.helper.closeUp.CloseUp}). <br/>
     * Jeder Leg, bzw. jedes CloseUpItem
     * repräsentiert einen einzelnen Fahrtabschnitt.
     *
     * @param legs Liste an Fahrtabschnitten - wird in eine Liste von {@link CloseUpItem} "umgeformt"
     * @return Eine Liste an {@link CloseUpItem}, welche für den RecyclerView genutzt werden
     */
    public static ArrayList<CloseUpItem> fillDetailedConnectionList(List<Trip.Leg> legs) {
        //TODO Verbindungsarten -> andere Farben
        // versuchen Umstiege anzuzeigen
        if (legs == null) {
            return new ArrayList<>();
        }

        ArrayList<CloseUpItem> items = new ArrayList<>();
        for (Trip.Leg leg : legs) {
            if (leg instanceof Trip.Public) {
                items.add(new CloseUpPublicItem((Trip.Public) leg));
            } else {
                items.add(new CloseUpPrivateItem((Trip.Individual) leg));
            }
        }
        return items;
    }
}
