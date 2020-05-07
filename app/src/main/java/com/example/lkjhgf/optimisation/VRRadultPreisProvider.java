package com.example.lkjhgf.optimisation;

import java.util.ArrayList;

public class VRRadultPreisProvider extends PreisProvider {

    public  VRRadultPreisProvider(ArrayList<Ticket> tickets){
        super(tickets);
    }

    /**
     * @see PreisProvider#getPrice(Ticket ticket, String preisstufe)
     */
    @Override
    public int getPrice(Ticket ticket, String preisstufe) {
        if (ticket instanceof NumTicket) {
            switch (preisstufe) {
                case "K":
                    return ticket.getPrice(0);
                case "A1":
                    return ticket.getPrice(1);
                case "A2":
                    return ticket.getPrice(2);
                case "A3":
                    return ticket.getPrice(3);
                case "B":
                    return ticket.getPrice(4);
                case "C":
                    return ticket.getPrice(5);
                case "D":
                    return ticket.getPrice(6);
                default:
                    return Integer.MAX_VALUE;
            }
        }
        //TODO Zeittickets
        return Integer.MAX_VALUE;
    }
}
