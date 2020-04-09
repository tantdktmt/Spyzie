package com.tantd.spyzie.data.model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Location {

    @Id
    public long _id;
    public double lat;
    public double lon;

    public Location(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "Location{" +
                "_id=" + _id +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
