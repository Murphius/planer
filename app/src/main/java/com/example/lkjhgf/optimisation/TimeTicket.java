package com.example.lkjhgf.optimisation;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.Calendar;

import de.schildbach.pte.dto.Fare;

public class TimeTicket extends Ticket {

    /**
     * Früheste Abfahrtszeit
     */
    private int startHour;
    /**
     * Späteste Endzeitpunkt
     */
    private int endHour;
    /**
     * Gibt an, ob Start- und Enduhrzeit am Wochenende nicht gelten
     */
    private boolean hasSpecialRule;
    /**
     * Gültigkeitsdauer des Tickets
     */
    private long maxDuration;
    /**
     * Wie viele Fahrten mindestens zurückgelegt werden müssen, damit sich das Ticket lohnt
     */
    private int[] minNumTrips;

    /**
     * Wie viele Personen das Ticket nutzen können
     */
    private int numPersons;

    /**
     * Liefert einen neuen Fahrschein
     *
     * @param prices         Preisliste
     * @param name           des Fahrscheins
     * @param type           Personengruppe, die den Fahrschein nutzen darf
     * @param minNumTrips    für jede Preisstufe, wie viele Fahrten mindestens gemacht werden müssen, damit sich das Ticket lohnt
     * @param startHour      ab wie viel Uhr das Ticket genutzt werden kann
     * @param endHour        bis wie viel Uhr das Ticket maximal genutzt werden darf
     * @param maxDuration    wie viele ms das Ticket maximal genutzt werden darf
     * @param hasSpecialRule ob am Wochenende die Start- und Endzeit ebenfalls beachtet werden muss
     */
    public TimeTicket(int[] prices, String name, Fare.Type type, long maxDuration, int[] minNumTrips, int startHour, int endHour, boolean hasSpecialRule, int numPersons) {
        super(prices, name, type);
        this.maxDuration = maxDuration;
        this.minNumTrips = minNumTrips;
        this.startHour = startHour;
        this.endHour = endHour;
        this.hasSpecialRule = hasSpecialRule;
        this.numPersons = numPersons;
    }

    /**
     * Prüft, ob der Fahrschein für diese Fahrt genutzt werden könnte <br/>
     * <p>
     * Prüft, ob der Fahrschein in der jeweiligen Preisstufe verfügbar ist, ob die Fahrt in das
     * Zeitfenster fällt, in dem das Ticket genutzt werden kann und ob die Fahrt nicht länger dauert,
     * als die möglich Dauer des Fahrscheins
     *
     * @param tripItem zu überprüfende Fahrt
     * @return true - wenn das Ticket genutzt werden kann, sonst false
     */
    public boolean isValidTrip(TripItem tripItem) {
        //Überprüfung der Preisstufe
        if (getPrice(MainMenu.myProvider.getPreisstufenIndex(tripItem.getPreisstufe())) == Integer.MAX_VALUE) {
            return false;
        }
        //Dauer des Trips > Ticketdauer
        if (tripItem.getLastArrivalTime().getTime() - tripItem.getFirstDepartureTime().getTime() > maxDuration) {
            return false;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(tripItem.getFirstDepartureTime().getTime());
        //Wenn es ein Ticket mit Sonderregel für das Wochenende ist
        if (hasSpecialRule) {
            //Überprüfen, ob die Fahrt am Wochenende stattfindet
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                //Keine Beachtung von Start- und Endzeit nötig
                return tripItem.getLastArrivalTime().getTime() - tripItem.getFirstDepartureTime().getTime() <= maxDuration;
            }
        }

        //Startuhrzeit
        int tripStartHour = calendar.get(Calendar.HOUR_OF_DAY);
        calendar.setTimeInMillis(tripItem.getLastArrivalTime().getTime());
        //Enduhrzeit
        int tripEndHour = calendar.get(Calendar.HOUR_OF_DAY);
        //Bei 18h30 muss die Endzeit erhöht werden, denn ein Fahrschein dessen Endzeit 18h ist, dürfte nicht für diese Fahrt verwendet werden
        if (calendar.get(Calendar.MINUTE) != 0) {
            tripEndHour++;
        }
        if (tripStartHour >= startHour && tripEndHour >= endHour) {//Beide Zeitpunkte vor Mitternacht
            return true;
        } else if (tripStartHour >= startHour && tripEndHour <= endHour) {//Startzeit vor Mitternacht, Endzeit nach Mitternacht
            return true;
        } else if (tripStartHour <= endHour && tripEndHour <= endHour) {//Beide Zeitpunkte nach Mitternacht
            return true;
        } else {//Sonst Ticket ungültig
            return false;
        }
    }

    public long getMaxDuration() {
        return maxDuration;
    }

    public int[] getMinNumTrips() {
        return minNumTrips;
    }

    public int getMinNumTrips(int preisstufenIndex) {
        return minNumTrips[preisstufenIndex];
    }

    public int getStartHour() {
        return startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public boolean hasSpecialRule() {
        return hasSpecialRule;
    }

    public int getNumPersons() {
        return numPersons;
    }
}
