package com.example.chapterproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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
        boolean didSuceed = false;
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
            didSuceed = database.insert("contact", null, initialValues) >0;

        } catch (Exception e) {

        }
        return didSuceed;
        }

        public boolean updateContact(Contact c) {
            boolean didSuceed = false;
            try {
                Long rowId = (long) c.getId();
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
                didSuceed = database.update("contact", updateValues, "_id=" + rowId, null) > 0;
            } catch (Exception e) {

            }
            return didSuceed;
        }

}
