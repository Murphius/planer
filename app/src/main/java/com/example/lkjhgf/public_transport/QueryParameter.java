package com.example.lkjhgf.public_transport;

import java.security.Provider;
import java.util.Date;

import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.TripOptions;

public class QueryParameter {
    private Location from, via, to;
    private Date date;
    private boolean dep;
    private TripOptions options;
    private NetworkProvider provider;

    public QueryParameter(Location from, Location via, Location to, Date date, boolean dep, TripOptions options, NetworkProvider provider){
        this.from = from;
        this.via = via;
        this.to = to;
        this.date = date;
        this.dep = dep;
        this.options = options;
        this.provider = provider;
    }

    public Location getFrom() {
        return from;
    }
    public Location getVia(){
        return via;
    }
    public Location getTo(){
        return to;
    }
    public Date getDate(){
        return date;
    }
    public boolean isDep(){
        return dep;
    }
    public TripOptions getOptions(){
        return options;
    }
    public NetworkProvider getProvider() {
        return provider;
    }
}
