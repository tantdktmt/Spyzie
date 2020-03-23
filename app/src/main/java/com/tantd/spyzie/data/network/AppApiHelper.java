package com.tantd.spyzie.data.network;

import com.rx2androidnetworking.Rx2AndroidNetworking;
import com.tantd.spyzie.data.network.model.Event;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by HP on 8/21/2017.
 */

public class AppApiHelper implements ApiHelper {

    private static AppApiHelper instance;

    public static synchronized AppApiHelper getInstance() {
        if (instance == null) {
            instance = new AppApiHelper();
        }
        return instance;
    }

    private AppApiHelper() {
    }

    @Override
    public Single<List<Event>> getSchedules() {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_SCHEDULES)
                .build().getObjectListSingle(Event.class);
    }
}
