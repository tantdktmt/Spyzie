package com.tantd.spyzie.data.network;

import com.rx2androidnetworking.Rx2AndroidNetworking;
import com.tantd.spyzie.data.model.Event;

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
}
