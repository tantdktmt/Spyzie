package com.tantd.spyzie.data.model;

import java.util.List;

public class Contact {

    public long id;
    public String name;
    public List<String> phone;

    public Contact(long id, String name, List<String> phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone=" + phone +
                '}';
    }
}
