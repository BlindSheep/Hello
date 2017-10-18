package com.httpso_hello.hello.Structures;

/**
 * Created by mixir on 18.10.2017.
 */

public class City {

    private int id;
    private String name;
    private int region_id;

    public City(int id, String name, int region_id){
        this.id = id;
        this.name = name;
        this.region_id = region_id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getRegion_id() {
        return region_id;
    }
}