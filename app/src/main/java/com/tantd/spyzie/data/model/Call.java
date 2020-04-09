package com.tantd.spyzie.data.model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Call {

    @Id
    public long _id;
    public long id;
    public String name;
    public String number;
    public int type;
    public long time;
    public long duration;

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
