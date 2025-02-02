package com.example.chapterproject;

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
}
