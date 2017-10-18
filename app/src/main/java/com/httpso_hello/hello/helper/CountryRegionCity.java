package com.httpso_hello.hello.helper;

import com.httpso_hello.hello.Structures.City;
import com.httpso_hello.hello.Structures.Region;

/**
 * Created by mixir on 18.10.2017.
 */

public class CountryRegionCity {

    public static Region[] getRegionsForSpinner(){
        Region[] regions = new Region[1];
        regions[0] = new Region(3952, "Кировская обл.");
        return regions;
    }

    public static City[] getCitysForSpinner(){
        City[] city = new City[1];
        city[0] = new City(3963, "Киров", 3952);
        return city;
    }

    public static City[] getCitysByRegionIdForSpinner(int regionId){
        City[] allCity = getCitysForSpinner();
        City[] filteredCity = new City[allCity.length];

        for(int i = 0; i < filteredCity.length; i++) {
            if (allCity[i].getRegion_id() == regionId) {
            }
        }
        return filteredCity;
    }

}