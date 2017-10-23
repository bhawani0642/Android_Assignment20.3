package com.acadgild.deletecontact;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Pri on 10/23/2017.
 */

public class ContactHelper {
    public static int deleteContact(ContentResolver contactHelper, String number) {

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        String contactID = String.valueOf(getContactID(contactHelper, number));

        Log.d("Contact ID: ", contactID);

        String[] args = new String[]{contactID};
        ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI).withSelection(ContactsContract.RawContacts.CONTACT_ID + "=?", args).build());
        try {
            ContentProviderResult[] contentProviderResult = contactHelper.applyBatch(ContactsContract.AUTHORITY, ops);
            Log.d("CP: ", String.valueOf(contentProviderResult[0].count));
            return contentProviderResult[0].count;
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();

        }

        return -1;
    }

    private static long getContactID(ContentResolver contactHelper, String number) {
        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String[] projection = {ContactsContract.PhoneLookup._ID};
        Cursor cursor = null;

        try {
            cursor = contactHelper.query(contactUri, projection, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    long personID = cursor.getLong(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
                    return personID;
                }
                cursor.close();
            } else {
                Log.d("Contact ID: ", "null");
            }
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return -1;
    }
}