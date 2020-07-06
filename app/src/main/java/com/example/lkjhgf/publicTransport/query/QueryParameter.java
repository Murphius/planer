package com.example.lkjhgf.publicTransport.query;

import com.example.lkjhgf.helper.MyURLParameter;

import java.util.Date;

import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.TripOptions;

/**
 * Enthält alle Parameter die für eine Routenauskunft benötigt werden, damit der Provider die
 * Anfrage beantworten kann <br/>
 * <p>
 * Benötigt werden Start, Ziel, Zeitpunkt (Abfahrts- oder Ankunftszeit -> hinterlegt im Boolean),
 * sowie die Routenoptionen ({@link com.example.lkjhgf.activities.Settings})
 *
 * @preconditions der Nutzer hat das Formular ({@link com.example.lkjhgf.helper.form.Form}) ausgefüllt,
 * und auf weiter geklickt
 */
public class QueryParameter {
    private Location from, via, to;
    private Date date;
    private boolean isDeparture;
    private TripOptions options;

    public QueryParameter(Location from,
                          Location via,
                          Location to,
                          Date date,
                          boolean isDeparture,
                          TripOptions options) {
        this.from = from;
        this.via = via;
        this.to = to;
        this.date = date;
        this.isDeparture = isDeparture;
        this.options = options;
    }

    public QueryParameter(MyURLParameter parameter){
        this(parameter.getStartLocation(), parameter.getVia(), parameter.getDestinationLocation(), parameter.getStartDate(), parameter.isDepartureTime(), parameter.getTripOptions());
    }

    public Location getFrom() {
        return from;
    }

    Location getVia() {
        return via;
    }

    Location getTo() {
        return to;
    }

    Date getDate() {
        return date;
    }

    boolean isDeparture() {
        return isDeparture;
    }

    TripOptions getOptions() {
        return options;
    }
}
