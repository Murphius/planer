package com.example.lkjhgf.publicTransport.provider;

public class Farezone {
    private int id;
    private String name;

    public Farezone(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean equals(Object o){
        if(! (o instanceof Farezone)){
            return false;
        }
        Farezone other = (Farezone) o;
        return id == other.id;
    }
}
