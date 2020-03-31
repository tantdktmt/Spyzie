package com.tantd.spyzie.data.network;

import android.location.Location;
import android.util.Log;

import com.rx2androidnetworking.Rx2AndroidNetworking;
import com.tantd.spyzie.data.model.Event;
import com.tantd.spyzie.util.Constants;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by HP on 8/21/2017.
 */

public class AppApiManager implements ApiManager {

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
        Log.d(Constants.LOG_TAG, "[AppApiManager] sendLocationData, lat=" + location.getLatitude() + ", lon=" + location.getLongitude());
        // TODO: Use Rx2AndroidNetworking to post location data to server
    }
}
