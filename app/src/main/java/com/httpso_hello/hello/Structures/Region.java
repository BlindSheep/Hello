package com.httpso_hello.hello.Structures;

/**
 * Created by mixir on 18.10.2017.
 */

public class Region {

    public int id;
    public String name;

    public Region(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}