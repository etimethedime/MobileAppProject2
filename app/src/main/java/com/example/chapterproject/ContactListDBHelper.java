package com.example.chapterproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ContactListDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyContactList.db";
    public static final int DATABASE_VERSION = 2;

    private static final String CREATE_TABLE_CONTACT = "CREATE TABLE contact (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "contactname TEXT NOT NULL, " +
            "streetaddress TEXT, " +
            "city TEXT, " +
            "state TEXT, " +
            "zipcode TEXT, " +
            "phonenumber TEXT, " +
            "cellnumber TEXT, " +
            "email TEXT, " +
            "birthday TEXT, " +
            "contactphoto BLOB);";

    public ContactListDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CONTACT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            try {
                db.execSQL("ALTER TABLE contact ADD COLUMN contactphoto BLOB");
            } catch (Exception e) {
                Log.e("DB Upgrade", "Error adding column contactphoto", e);
            }
        }
    }
}
