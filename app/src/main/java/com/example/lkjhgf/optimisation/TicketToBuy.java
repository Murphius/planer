package com.example.lkjhgf.optimisation;

import androidx.annotation.NonNull;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.publicTransport.provider.Farezone;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

/**
 * Enthält alle Informationen zu einem Ticket <br/>
 * Darunter fällt das Ticket, die Preisstufe des Tickets, die Fahrten für dass das Ticket genutzt
 * werden soll und eine ID
 */
public class TicketToBuy implements Comparable<TicketToBuy> {
    /**
     * Ticket(typ), der gekauft werden soll
     */
    private Ticket ticket;
    /**
     * Preisstufe des Fahrscheins
     */
    private String preisstufe;
    /**
     * Fahrten, für die der Fahrschein genutzt werden soll
     */
    private ArrayList<TripQuantity> tripQuantities;
    /**
     * Wie viele Fahrten mit dem Ticket noch gemacht werden können
     */
    private int freeTrips;
    /**
     * ID
     */
    private UUID ticketID;

    /**
     * Tarifgebiete oder Waben, in denen das Ticket gültig ist
     */
    private Set<Farezone> validFarezones;
    /**
     * Zentralregion
     */
    private int mainRegionID;
    /**
     * Unterscheidung: Ticket ist in zwei Waben oder in einem/mehreren Tarifgebiet(en) gültig
     */
    private boolean isZweiWabenTarif;

    public TicketToBuy(Ticket ticket, String preisstufe) {
        this.ticket = ticket;
        this.preisstufe = preisstufe;
        tripQuantities = new ArrayList<>();
        calculateFreeTrips();
        ticketID = UUID.randomUUID();
        validFarezones = new HashSet<>();
    }

    /**
     * Ermittelt wie viele Fahrten auf dem Ticket noch frei sind <br/>
     * <p>
     * Bei Zeittickets ist dieser Wert immer maximal;
     * bei Mengenfahrscheinen hingegen die Anzahl der möglichen Fahrten - die genutze Anzahl
     */
    private void calculateFreeTrips() {
        if (freeTrips != Integer.MAX_VALUE) {
            if (ticket instanceof NumTicket) {
                int usedTrips = 0;
                for (TripQuantity tripQuantity : tripQuantities) {
                    usedTrips += tripQuantity.getQuantity();
                }
                freeTrips = ((NumTicket) ticket).getNumTrips() - usedTrips;
            } else {
                freeTrips = Integer.MAX_VALUE;
            }
        }
    }

    /**
     * Fügt eine Fahrt der Liste der zugehörigen Fahrten hinzu <br/>
     * <p>
     * Wenn die Fahrt schon in der Liste zugehöriger Fahrten entahlten ist, wird ihre Anzahl erhöht; <br/>
     * falls nicht, wird ein neues {@link TripQuantity} Objekt erzeugt und der Liste hinzugefügt. <br/>
     * <p>
     * Für Mengenfahrscheine wird die Anzahl freier Fahrten verringert
     *
     * @param tripItem Fahrt die hinzugefügt werden soll
     */
    public void addTripItem(TripItem tripItem) {
        //Für Mengenfahrscheine -> eine Fahrt weniger möglich
        if (freeTrips != Integer.MAX_VALUE) {
            freeTrips--;
        }
        //Prüfen, ob die Fahrt bereits enthalten ist
        for (TripQuantity quantity : tripQuantities) {
            if (quantity.checkContains(tripItem)) {
                //falls ja, die Anzahl um eins erhöhen
                quantity.add();
                return;
            }
        }
        //Fahrt noch nicht enthalten -> neu hinzufügen
        tripQuantities.add(new TripQuantity(tripItem));
    }

    /**
     * Fügt mehrere Fahrten dem Ticket hinzu <br/>
     * <p>
     * Ruft für jedes Item das hinzugefügt werden soll {@link #addTripItem(TripItem)} auf
     *
     * @param tripItems Liste der Fahrten die hinzugefügt werden soll
     */
    public void addTripItems(ArrayList<TripItem> tripItems) {
        for (TripItem t : tripItems) {
            addTripItem(t);
        }
    }

    /**
     * Prüft für eine Fahrt, ob alle durchfahrenen Tarifgebiete / Waben, ob diese von dem Ticket
     * abgedeckt werden <br/>
     * <p>
     * Wenn das Ticket im Zwei-Waben-Tarif genutzt wird, werden die Waben verglichen, sonst
     * die Tarifgebiete.
     *
     * @param tripItem zu prüfende Fahrt
     * @return true - alle durchquerten Zonen werden von dem Ticket abgedeckt, false - sonst
     */
    public boolean checkFarezone(TripItem tripItem) {
        for (Integer crossedFarezone : tripItem.getCrossedFarezones()) {
            boolean contains = false;
            for (Farezone f : validFarezones) {
                //Im zwei-Waben-Tarif werden die Wabennr. verglichen
                if (isZweiWabenTarif) {
                    if (f.getId() == crossedFarezone) {
                        contains = true;
                        break;
                    }
                } else {
                    //Sonst werden die Tarifgebiete verglichen -> Wabennr. / 10 = Tarifgebiet ID
                    if (f.getId() == crossedFarezone / 10) {
                        contains = true;
                        break;
                    }
                }
            }
            //Wenn eine Zone nicht enthalten ist -> Abbruch
            if (!contains) {
                return false;
            }
        }
        //Alle Zonen enthalten
        return true;
    }

    /**
     * Überpürft für mehrere Fahrten, ob alle durchfahrenen Tarifgebiete mit diesem Ticket
     * abgedeckt werden<br/>
     * <p>
     * Ruft für jede einezelne Fahrt {@link #checkFarezone(TripItem)} auf
     *
     * @param tripItems Zu prüfende Fahrten
     * @return false - mindestens eine Fahrt kann nicht dem Ticket zugew
     */
    public boolean checkFarezones(ArrayList<TripItem> tripItems) {
        for (TripItem tripItem : tripItems) {
            if (!checkFarezone(tripItem)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Entfernt eine Fahrt aus der Liste der zugeorneten Fahrten
     *
     * @param tripID ID der zu löschenden Fahrt
     * @return true - wenn dadurch alle Fahrten des Fahrscheins frei sind, nur für Mengenfahrscheine relevant <br/>
     * false - wenn noch mindestens eine Fahrt des Fahrscheins genutzt wird
     */
    public boolean removeTrip(String tripID) {
        // über alle zugeordneten Fahrten iterieren
        for (Iterator<TripQuantity> tripQuantityIterator = tripQuantities.iterator(); tripQuantityIterator.hasNext(); ) {
            TripQuantity currentTripQuantity = tripQuantityIterator.next();
            //prüfen, ob die IDs übereinstimmen
            if (currentTripQuantity.getTripItem().getTripID().equals(tripID)) {
                int quantity = currentTripQuantity.getQuantity();
                //Entfernen der Fahrt
                tripQuantityIterator.remove();
                if (ticket instanceof NumTicket) {
                    //Fahrten freigeben
                    if (freeTrips != Integer.MAX_VALUE) {
                        freeTrips += quantity;
                    }
                    //Ticket besteht nur noch aus freien Fahrten
                    if (((NumTicket) ticket).getNumTrips() == freeTrips) {
                        return true;
                    } else {
                        return tripQuantities.size() == 0;
                    }
                }
            }
        }
        //Ticket hat noch nicht genutzte Fahrten
        return false;
    }

    /**
     * Festlegen der Gültigkeitsbereiche des Tickets sowie dessen Zentralregion <br/>
     * <p>
     * Die Zentralregion hat verschiedene Funktionen, abhängig von der Preisstufe: <br/>
     * Preisstufe D: keine, Preisstufe C: Regionennr., Preisstufe B: Zentralestarifgebiet, <br/>
     * A (ein Tarifgebiet): Tarifgebiet, A (zwei Waben): eine von den zwei Waben <br/>
     *
     * @param validFarezones Tarifzonen, in denen das Ticket genutzt werden kann
     * @param mainRegion     identifizierung, wie das Ticket entwertet werden soll
     */
    public void setValidFarezones(Set<Farezone> validFarezones, int mainRegion) {
        this.validFarezones = validFarezones;
        this.mainRegionID = mainRegion;
    }

    /**
     * Festlegen der Gültigkeitsbereicher des Tickets <br/>
     * <p>
     * Über isZweiWabenTarif wird bei durchkreuzten Regionen unterschieden, ob Waben oder Tarifgebiete
     * überpürft werden müssen <br/>
     * Ruft {@link #setValidFarezones(Set validFarezones, int mainRegionID)} auf
     *
     * @param validFarezones   gültige Zonen
     * @param mainRegionID     Zentralregion
     * @param isZweiWabenTarif gibt an, ob das Ticket im zwei-Waben-Tarif genutzt wird oder nicht
     */
    public void setValidFarezones(Set<Farezone> validFarezones, int mainRegionID, boolean isZweiWabenTarif) {
        setValidFarezones(validFarezones, mainRegionID);
        this.isZweiWabenTarif = isZweiWabenTarif;
    }

    /**
     * Prüft, ob alle zugeordneten Fahrten in der Zukunft liegen oder nicht
     *
     * @return false - wenn mindestens eine Fahrt in der Vergangenheit liegt (VOR dem aktuellen Zeitpunkt) <br/>
     * true - wenn alle Fahrten nach dem aktuellen Zeitpunkt liegen <br/>
     * Wichtig ist die ABfahrtszeit, nicht die Ankunftszeit
     */
    public boolean isFutureTicket() {
        for (TripQuantity tripQuantity : tripQuantities) {
            if (tripQuantity.getTripItem().getFirstDepartureTime().before(Calendar.getInstance().getTime())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Prüft ob alle Fahrten eines Fahrscheins mehr als der Offset in der Vergangenheit liegen
     * <p>
     * Der Offset soll 0 sein, beim laden für die Optimierung und 24h für das Anzeigen von Fahrscheinen
     *
     * @param offset gibt an, wie lange die Fahrten in der Vergangenheit liegen sollen
     * @return false - das Ticket ist aktuell, oder hat noch mindestens eine freie Fahrt
     * true - das Ticket ist nicht mehr gültig oder alle Fahrten liegen in der Vergangenheit
     */
    public boolean isPastTicket(long offset) {
        if (ticket instanceof TimeTicket) {
            // Bei einem Zeitticket wir der gesamte Geltungsbereich + offset betrachtet
            // Ist dieser kleiner, ist das Ticket noch aktuell
            return getFirstDepartureTime().getTime() + ((TimeTicket) ticket).getMaxDuration() + offset < Calendar.getInstance().getTime().getTime();
        } else {
            if (freeTrips != 0) { // Ohne diese Abfrage, werden NumTickets mit noch freien Fahrten, deren letzte Fahrt jedoch länger als der Offset her ist, gelöscht
                return false;
            } else {
                for (TripQuantity tripQuantity : tripQuantities) {//Prüfen, ob alle Fahrten länger als der Offset her sind
                    if (tripQuantity.getTripItem().getLastArrivalTime().getTime() + offset > Calendar.getInstance().getTime().getTime()) {
                        return false;
                    }
                }
                return true;
            }
        }
    }

    public String getPreisstufe() {
        return preisstufe;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public int getFreeTrips() {
        return freeTrips;
    }

    public UUID getTicketID() {
        return ticketID;
    }

    public Set<Farezone> getValidFarezones() {
        return validFarezones;
    }

    public boolean isZweiWabenTarif() {
        return isZweiWabenTarif;
    }

    public int getMainRegionID() {
        return mainRegionID;
    }

    public ArrayList<TripQuantity> getTripQuantities() {
        return tripQuantities;
    }

    public ArrayList<TripItem> getTripList() {
        ArrayList<TripItem> tripItems = new ArrayList<>();
        for (TripQuantity tripQuantity : tripQuantities) {
            tripItems.add(tripQuantity.getTripItem());
        }
        return tripItems;
    }

    public Date getFirstDepartureTime() {
        return tripQuantities.get(0).getTripItem().getFirstDepartureTime();
    }

    public Date getLastArrivalTime() {
        return tripQuantities.get(tripQuantities.size() - 1).getTripItem().getLastArrivalTime();
    }

    @NonNull
    @Override
    public String toString() {
        return ticket.getName() + " " + getTicketID().toString();
    }

    /**
     * Equals nicht anhand der ID sondern anhand der Namen und Preisstufen
     *
     * @param o zu vergleichendes Objekt
     * @return true - Preisstufe und Ticket stimmen überein; <br/>
     * false - Preisstufe oder Ticket stimmen nicht überein
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TicketToBuy)) {
            return false;
        }
        TicketToBuy other = (TicketToBuy) o;
        return ticket.equals(other.ticket) && preisstufe.equals(other.preisstufe);
    }

    /**
     * Vergleich von zwei TicketToBuy Elementen <br/>
     * <p>
     * Bei gleichem Ticket, wird nach der Preisstufe sortiert <br/>
     * Bei Fahrscheinen mit einer festen Fahrtenanzahl wird sonst nach der Anzahl möglicher Fahrten sortiert <br/>
     * Bei Zeitfahrscheinen hingegen nach der maximalen Geltungsdauer
     *
     * @param o zu vergleichendes Ticket
     * @return 1 -> this > o, 0 -> this = o, -1 -> this < o
     */
    @Override
    public int compareTo(TicketToBuy o) {
        if (ticket.equals(o.ticket)) {
            // bei gleichen Tickets wird anhand der Preisstufen unterschieden, welches Ticket kleiner/größer ist
            return MainMenu.myProvider.getPreisstufenIndex(preisstufe) - MainMenu.myProvider.getPreisstufenIndex(o.preisstufe);
        } else {
            if (ticket instanceof NumTicket && o.ticket instanceof NumTicket) {
                Integer thisNumTrip = ((NumTicket) ticket).getNumTrips();
                Integer otherNumTrip = ((NumTicket) o.ticket).getNumTrips();
                //Bei Fahrscheinen mit einer unterschiedlichen Anzahl an Fahrten, wird anhand der möglichen Fahrten unterschieden
                return thisNumTrip.compareTo(otherNumTrip);
            } else {
                long maxDuration = ((TimeTicket) ticket).getMaxDuration();
                long oMaxDuration = ((TimeTicket) o.ticket).getMaxDuration();
                return Long.compare(maxDuration, oMaxDuration);
            }
        }
    }
}
