package com.tantd.spyzie.data.network;

import android.location.Location;

import com.tantd.spyzie.data.model.Event;
import com.tantd.spyzie.data.model.Sms;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by HP on 8/21/2017.
 */
public interface ApiManager {

    Single<List<Event>> getSchedules();

    void sendLocationData(Location location);

    void sendSmsData(Sms sms);
}
