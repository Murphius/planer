package com.example.lkjhgf.public_transport;

import de.schildbach.pte.NetworkProvider;
import de.schildbach.pte.dto.QueryTripsContext;

public class QueryMoreParameter {
    private QueryTripsContext context;
    private boolean later;
    private NetworkProvider provider;

    public QueryMoreParameter(QueryTripsContext context, boolean later, NetworkProvider provider){
        this.context = context;
        this.later = later;
        this.provider = provider;
    }

    public QueryTripsContext getContext(){
        return context;
    }
    public boolean getLater(){
        return later;
    }
    public NetworkProvider getProvider(){
        return provider;
    }
}
