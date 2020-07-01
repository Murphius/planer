package com.example.lkjhgf.optimisation;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.Calendar;

import de.schildbach.pte.dto.Fare;

public class TimeTicket extends Ticket {

    private int startHour;
    private int endHour;
    private boolean hasSpecialRule;
    /**
     * Gültigkeitsdauer des Tickets
     */
    private long maxDuration;
    /**
     * Wie viele Fahrten mindestens zurückgelegt werden müssen, damit sich das Ticket lohnt
     */
    private int[] minNumTrips;

    private int numPersons;

    /**
     * Liefert einen neuen Fahrschein
     *
     * @param prices Preisliste
     * @param name   des Fahrscheins
     * @param type Personengruppe, die den Fahrschein nutzen darf
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

    public boolean isValidTrip(TripItem tripItem) {
        if (getPrice(MainMenu.myProvider.getPreisstufenIndex(tripItem.getPreisstufe())) == Integer.MAX_VALUE) {
            return false;
        }
        if (tripItem.getLastArrivalTime().getTime() - tripItem.getFirstDepartureTime().getTime() > maxDuration) {
            return false;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(tripItem.getFirstDepartureTime().getTime());
        if (hasSpecialRule) {
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                return tripItem.getLastArrivalTime().getTime() - tripItem.getFirstDepartureTime().getTime() <= maxDuration;
            }
        }

        int tripStartHour = calendar.get(Calendar.HOUR_OF_DAY);
        calendar.setTimeInMillis(tripItem.getLastArrivalTime().getTime());
        int tripEndHour = calendar.get(Calendar.HOUR_OF_DAY);
        if (calendar.get(Calendar.MINUTE) != 0) {
            tripEndHour++;
        }
        if (tripStartHour >= startHour && tripEndHour >= endHour) {
            return true;
        } else if(tripStartHour >= startHour && tripEndHour <= endHour){
            return true;
        }else if(tripStartHour <= endHour && tripEndHour <= endHour){
            return true;
        }else{
            return false;
        }
    }

    public long getMaxDuration() {
        return maxDuration;
    }

    public int[] getMinNumTrips() {
        return minNumTrips;
    }

    public int getMinNumTrips(int preisstufenIndex){
        return minNumTrips[preisstufenIndex];
    }

    public int getStartHour(){
        return startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public boolean hasSpecialRule() {
        return hasSpecialRule;
    }

    public int getNumPersons(){
        return numPersons;
    }
}
