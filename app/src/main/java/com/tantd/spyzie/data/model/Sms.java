package com.tantd.spyzie.data.model;

import java.util.Date;

public class Sms {

    public String sender;
    public long time;
    public String content;

    public Sms(String sender, long time, String content) {
        this.sender = sender;
        this.time = time;
        this.content = content;
    }

    public Sms() {
    }

    @Override
    public String toString() {
        return "Sms{" +
                "sender='" + sender + '\'' +
                ", time=" + new Date(time) +
                ", content='" + content + '\'' +
                '}';
    }
}
