package com.example.lkjhgf.recyclerView.detailedView;

import com.example.lkjhgf.helper.Utils;

import de.schildbach.pte.dto.Trip;

/**
 * Repräsentiert einen einzelnen Fahrtabschnitt <br/>
 * Z.B. Laufen, Fahrt mit einer S-Bahn, ...
 *
 * @preconditions der Nutzer hat unter den möglichen Fahrten eine Fahrt angeklickt; <br/>
 * oder der Nutzer hat unter den geplanten Fahrten eine Fahrt angeklickt <br/>
 * <p>
 * Abhängig davon, ob es sich um einen individullen Abschnitt handelt, oder um einen ÖPNV Abschnitt
 * werden die Textfelder anders ausgefüllt und die Zwischenhalte unterscheiden sich <br/>
 * Siehe in den Klasssen {@link CloseUpPrivateItem} und {@link CloseUpPublicItem}
 * </p>
 */
public abstract class CloseUpItem {

    private String time_of_departure, time_of_arrival;
    private String departure, destination;
    int image_resource;
    Trip.Leg leg;

    private boolean showDetails;

    /**
     * Initialisierung der Variablen, so dass die Informationen nicht immer wieder neu in
     * Zeichenketten umgewandelt werden müssen <br/>
     *
     * @param leg - enthält alle Informationen zu dem Fahrtabschnitt
     *            <p>
     *            Wichtig sind hauptsächlich die initialisierung des Legs und von showDetails
     */
    CloseUpItem(Trip.Leg leg) {
        this.leg = leg;
        time_of_departure = Utils.setTime(leg.getDepartureTime());
        time_of_arrival = Utils.setTime(leg.getArrivalTime());

        departure = Utils.setLocationName(leg.departure);
        destination = Utils.setLocationName(leg.arrival);

        showDetails = false;
    }

    public String getTime_of_departure() {
        return time_of_departure;
    }

    public String getTime_of_arrival() {
        return time_of_arrival;
    }

    public int getImage_resource() {
        return image_resource;
    }

    public String getDestination() {
        return destination;
    }

    public String getDeparture() {
        return departure;
    }

    public boolean isShowDetails() {
        return showDetails;
    }

    /**
     * Wenn der Nutzer auf den Details anzeigen / verbergen Button klickt, wird das Attribut
     * immer auf das Gegenteil gesetzt <br/>
     * verbergen -> anzeigen bzw. anzeigen -> verbergen
     */
    public void setShowDetails() {
        showDetails = !showDetails;
    }

    //TODO hier soll das Ticket zuruckgegeben werden
    public Object getTicket() {
        return null;
    }

}
