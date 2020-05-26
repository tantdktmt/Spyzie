package com.tantd.spyzie.data.model;

import com.google.gson.annotations.Expose;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Location {

    @Id
    public long _id;
    @Expose
    public double lat;
    @Expose
    public double lon;

    public Location() {
    }

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

    public boolean isSameValueWith(Location other) {
        return other != null && lat == other.lat && lon == other.lon;
    }
}
