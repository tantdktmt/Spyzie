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
    public double lon;@Expose
    public long time;

    public Location() {
    }

    public Location(double lat, double lon, long time) {
        this.lat = lat;
        this.lon = lon;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Location{" +
                "_id=" + _id +
                ", lat=" + lat +
                ", lon=" + lon +
                ", time=" + time +
                '}';
    }

    public boolean isSameValueWith(Location other) {
        return other != null && lat == other.lat && lon == other.lon;
    }
}
