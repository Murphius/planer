package com.example.lkjhgf.optimisation;

import java.io.Serializable;

/**
 * Ein Fahrschein mit allen Preisen die dieser Fahrschein haben kann sowie dem Namen des
 * Fahrscheins
 */
public abstract class Ticket implements Serializable {
    /**
     * Preise in Cent
     */
    private int[] prices;
    private String name;

    /**
     * Liefert einen neuen Fahrschein
     * @param prices Preisliste
     * @param name des Fahrscheins
     */
    public Ticket(int[] prices, String name) {
        this.prices = prices;
        this.name = name;
    }

    /**
     * Liefert alle Preise
     * @return Preise aller Preisstufen in Cent
     */
    public int[] getPrices() {
        return prices;
    }

    /**
     * Gibt den Preis der Preisstufe an der jewiligen Position
     * @param index auf den Zugegriffen werden soll
     * @return Preis der jeweiligen Preisstufe des Tickets in Cent
     */
    public int getPrice(int index){
        return prices[index];
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString(){
        return name;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Ticket)){
            return false;
        }
        return name.equals(((Ticket) o).name);
    }
}
