package com.example.lkjhgf.recyclerView.tickets.detailedView;

import com.example.lkjhgf.recyclerView.detailedView.CloseUpPublicItem;

import de.schildbach.pte.dto.Trip;

public class CloseUpItemTicketList extends CloseUpPublicItem {
    /**
     * Aufruf des Konstruktors der Oberklasse <br/>
     * Festlegen von Icon, Gleisen, Linienname, Linienziel und Zwischenhalten
     *
     * @param publicTrip enthält alle Informationen zum Streckenabschnitt
     */
    public CloseUpItemTicketList(Trip.Public publicTrip) {
        super(publicTrip);
    }
}
