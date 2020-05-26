package com.tantd.spyzie.data.model;

import com.google.gson.annotations.Expose;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Sms {

    @Id
    public long _id;
    @Expose
    public long id;
    @Expose
    public String address;
    @Expose
    public long time;
    @Expose
    public String body;
    @Expose
    public boolean isIncoming;

    public Sms() {
    }

    public Sms(long id, String address, long time, String body, boolean isIncoming) {
        this.id = id;
        this.address = address;
        this.time = time;
        this.body = body;
        this.isIncoming = isIncoming;
    }

    @Override
    public String toString() {
        return "Sms{" +
                "_id=" + _id +
                ", id=" + id +
                ", address='" + address + '\'' +
                ", time=" + time +
                ", body='" + body + '\'' +
                ", isIncoming=" + isIncoming +
                '}';
    }
}
