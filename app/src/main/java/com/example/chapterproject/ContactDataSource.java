package com.example.chapterproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class ContactDataSource {
    private SQLiteDatabase database;
    private ContactListDBHelper dbHelper;

    public ContactDataSource(Context context) {
        dbHelper = new ContactListDBHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean insertContact(Contact c) {
        boolean didSucceed = false;
        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put("contactname", c.getContactName());
            initialValues.put("streetaddress", c.getStreetAddress());
            initialValues.put("city", c.getCity());
            initialValues.put("state", c.getState());
            initialValues.put("zipcode", c.getZipCode());
            initialValues.put("phonenumber", c.getHomePhoneNumber());
            initialValues.put("cellnumber", c.getCellNumber());
            initialValues.put("email", c.getEmail());
            initialValues.put("birthday", c.getBirthday());

            long result = database.insert("contact", null, initialValues);

            if (result > 0) {
                didSucceed = true;
            } else {
                Log.e("ContactDataSource", "Error inserting contact into database");
            }
        } catch (Exception e) {
            Log.e("ContactDataSource", "Error during contact insertion", e);
        }
        return didSucceed;
    }

    public boolean updateContact(Contact c) {
        boolean didSucceed = false;
        try {
            Long rowId = c.getId();
            ContentValues updateValues = new ContentValues();
            updateValues.put("contactname", c.getContactName());
            updateValues.put("streetaddress", c.getStreetAddress());
            updateValues.put("city", c.getCity());
            updateValues.put("state", c.getState());
            updateValues.put("zipcode", c.getZipCode());
            updateValues.put("phonenumber", c.getHomePhoneNumber());
            updateValues.put("cellnumber", c.getCellNumber());
            updateValues.put("email", c.getEmail());
            updateValues.put("birthday", c.getBirthday());

            int rowsUpdated = database.update("contact", updateValues, "_id=" + rowId, null);

            if (rowsUpdated > 0) {
                didSucceed = true;
                Log.d("ContactDataSource", "Contact updated successfully, ID: " + rowId);
            } else {
                Log.e("ContactDataSource", "Error updating contact with ID: " + rowId);
            }
        } catch (Exception e) {
            Log.e("ContactDataSource", "Error during contact update", e);
        }
        return didSucceed;
    }

    public int getLastContactId() {
        int lastId = -1;
        Cursor cursor = null;
        try {
            String query = "SELECT MAX(_id) FROM contact";
            cursor = database.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                lastId = cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.e("ContactDataSource", "Error getting last contact ID", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lastId;
    }

    public ArrayList<String> getContactNames() {
        ArrayList<String> contactNames = new ArrayList<>();
        Cursor cursor = null;
        try {
            String query = "SELECT contactname FROM contact";
            cursor = database.rawQuery(query, null);
            while (cursor.moveToNext()) {
                contactNames.add(cursor.getString(0));
            }
        } catch (Exception e) {
            Log.e("ContactDataSource", "Error getting contact names", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return contactNames;
    }



}
