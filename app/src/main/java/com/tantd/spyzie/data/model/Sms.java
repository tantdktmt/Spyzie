package com.tantd.spyzie.data.model;

public class Sms {

    public String id;
    public String address;
    public String time;
    public String body;
    public boolean isIncoming;

    public Sms() {
    }

    public Sms(String id, String address, String time, String body, boolean isIncoming) {
        this.id = id;
        this.address = address;
        this.time = time;
        this.body = body;
        this.isIncoming = isIncoming;
    }

    @Override
    public String toString() {
        return "Sms{" +
                "id='" + id + '\'' +
                ", address='" + address + '\'' +
                ", time='" + time + '\'' +
                ", body='" + body + '\'' +
                ", isIncoming=" + isIncoming +
                '}';
    }
}
