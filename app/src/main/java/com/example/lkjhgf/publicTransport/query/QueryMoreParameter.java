package com.example.lkjhgf.publicTransport.query;

import de.schildbach.pte.dto.QueryTripsContext;

/**
 * Fasst alle Parameter, die für die Provider Anfrage, für weitere Fahrten benötigt werden <br/>
 *
 * Weitere Fahrten können früher oder später stattfinden (later) <br/>
 * Benötigt wird auch der Context der Fahrt. <br/>
 * Der Provider könnte alternativ auch in {@link QueryMoreTask} im Konstruktor übergeben werden
 */
public class QueryMoreParameter {
    private QueryTripsContext context;
    private boolean later;

    public QueryMoreParameter(QueryTripsContext context, boolean later){
        this.context = context;
        this.later = later;
    }

    public QueryTripsContext getContext(){
        return context;
    }
    boolean getLater(){
        return later;
    }
}
