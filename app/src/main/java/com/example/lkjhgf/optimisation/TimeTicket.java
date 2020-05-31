package com.example.lkjhgf.optimisation;

import de.schildbach.pte.dto.Fare;

public class TimeTicket extends Ticket {

    private int startHour;
    private int endHour;
    /**
     * Gültigkeitsdauer des Tickets
     */
    private int maxDuration;
    /**
     *  Wie viele Fahrten mindestens zurückgelegt werden müssen, damit sich das Ticket lohnt
     */
    private int minNumTrips;
    /**
     * Liefert einen neuen Fahrschein
     *
     * @param prices Preisliste
     * @param name   des Fahrscheins
     * @param type
     */
    public TimeTicket(int[] prices, String name, Fare.Type type, int maxDuration, int minNumTrips) {
        super(prices, name, type);
        this.maxDuration = maxDuration;
        this.minNumTrips = minNumTrips;
    }

    public int getStartTime(){
        return startHour;
    }

    public int getEndTime(){
        return endHour;
    }

    public int getMaxDuration(){
        return maxDuration;
    }

    public int getMinNumTrips(){
        return minNumTrips;
    }
}
