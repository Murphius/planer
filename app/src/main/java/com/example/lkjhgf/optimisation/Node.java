package com.example.lkjhgf.optimisation;

import com.example.lkjhgf.recyclerView.futureTrips.TripItem;

import java.util.ArrayList;

public class Node {
    private int id;
    private String name;
    private ArrayList<TripItem> tripItems;

    public Node(int id, String name, ArrayList<TripItem> tripItems){
        this.id = id;
        this.name = name;
        this.tripItems = tripItems;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public ArrayList<TripItem> getTripItems() {
        return tripItems;
    }
}
