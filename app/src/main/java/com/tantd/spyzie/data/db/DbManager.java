package com.tantd.spyzie.data.db;

import com.tantd.spyzie.data.model.Call;
import com.tantd.spyzie.data.model.Contact;
import com.tantd.spyzie.data.model.Location;
import com.tantd.spyzie.data.model.Sms;

import java.util.Collection;
import java.util.List;

/**
 * Created by tantd on 4/9/2020.
 */
public interface DbManager {

    <T> void put(T t);

    void putLocations(Collection<Location> locations);

//    <T> List<T> findAll(Class<T> tClass);
    List<?> findAll(Class<?> tClass);

    List<?> find(Class<?> tClass, long offset, long limit);

    void removeAll(Class<?> tClass);

    void removeLocations(Collection<Location> locations);

    void removeSms(Collection<Sms> sms);

    void removeCalls(Collection<Call> calls);

    void removeContacts(Collection<Contact> contacts);
}
