package com.tantd.spyzie.data.model;

import com.google.gson.annotations.Expose;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Call {

    @Id
    public long _id;
    @Expose
    public long id;
    @Expose
    public String name;
    @Expose
    public String number;
    @Expose
    public int type;
    @Expose
    public long time;
    @Expose
    public long duration;

    public Call() {
    }

    public Call(long id, String name, String number, int type, long time, long duration) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.type = type;
        this.time = time;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Call{" +
                "_id=" + _id +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", type=" + type +
                ", time=" + time +
                ", duration=" + duration +
                '}';
    }
}
