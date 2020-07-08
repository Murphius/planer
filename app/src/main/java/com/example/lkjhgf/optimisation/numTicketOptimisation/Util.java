package com.example.lkjhgf.optimisation.numTicketOptimisation;

import com.example.lkjhgf.activities.MainMenu;
import com.example.lkjhgf.optimisation.Ticket;

import java.util.ArrayList;

public final class Util {
    /**
     * Sucht in einer Liste nach dem Eintrag mit den geringsten Kosten
     *
     * @param costs Liste mit allen Kosten
     * @return Index des Eintrags mit dem geringsten Wert
     */
    public static int getMinCostsIndex(ArrayList<Integer> costs) {
        int minCost = costs.get(0);
        int index = 0;
        for (int i = 1; i < costs.size(); i++) {
            if (costs.get(i) < minCost) {
                minCost = costs.get(i);
                index = i;
            }
        }
        return index;
    }

    /**
     * Sucht den maximalen Preisstufen Index, für den das Ticket noch den gleichen Preis hat
     * @param oldIndex Startindex
     * @return Preisstufenindex, für den der Preis gleich ist, wie für den ausgangs Index, aber
     * der Leistungsumfang größer ist
     */
    public static int maxFarezoneIndex(int oldIndex, Ticket ticket){
        while (oldIndex < MainMenu.myProvider.getPreisstufenSize() - 1) {
            if(ticket.getPrice(oldIndex) >= ticket.getPrice(oldIndex + 1)){
                oldIndex++;
            }else{
                break;
            }
        }
        return oldIndex;
    }

}
