package com.example.flavoury.ui.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.flavoury.ui.login.LoginActivity;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Local.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_LOCAL = "Local";
    private static final String COLUMN_UID = "Uid";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //我push呢個先 聽日搞

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='Local'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() == 0) {
            String createTableQuery = "CREATE TABLE Local (Uid TEXT PRIMARY KEY)";
            db.execSQL(createTableQuery);
        }
        cursor.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void saveUid(String uid) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Uid", uid);

        db.update("Local", values, null, null);

        db.close();
    }

    public String getUid() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " + COLUMN_UID + " FROM " + TABLE_LOCAL;
        Cursor cursor = null;
        String Uid = null;

        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(COLUMN_UID);
                if (columnIndex != -1) {
                    Uid = cursor.getString(columnIndex);
                }
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }

            db.close();
        }

        return Uid;
    }
}
