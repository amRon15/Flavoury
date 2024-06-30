package com.example.flavoury.ui.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.flavoury.ui.login.LoginActivity;

import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Local.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_LOCAL = "Local";
    private static final String COLUMN_UID = "Uid";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='Local'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() == 0) {
            String createTableQuery = "CREATE TABLE Local (Uid TEXT primary key)";
            db.execSQL(createTableQuery);
            String userHistory = "CREATE TABLE UserHistory (UName TEXT, Date DATETIME default CURRENT_TIMESTAMP, PRIMARY KEY(UName, Date))";
            String recipeHistory = "CREATE TABLE RecipeHistory (RName TEXT, Date DATETIME default CURRENT_TIMESTAMP, PRIMARY KEY(RName, Date))";
            db.execSQL(userHistory);
            db.execSQL(recipeHistory);
        }
        cursor.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void saveUserHistory(String text){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("UName", text);

        db.insert("UserHistory", null, values);
        db.close();
    }

    public void saveRecipeHistory(String text){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("RName", text);

        db.insert("RecipeHistory", null, values);
        db.close();
    }

    public ArrayList<String> getUserHistory(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM UserHistory ORDER BY Date", null);

        ArrayList<String> usernameList = new ArrayList<>();
        while (cursor.moveToNext()){
            int index = cursor.getColumnIndex("UName");
            if (index != -1){
                String username = cursor.getString(index);
                usernameList.add(username);
            }
        }
        cursor.close();
        return usernameList;
    }

    public ArrayList<String> getRecipeHistory(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM RecipeHistory ORDER BY Date", null);

        ArrayList<String> usernameList = new ArrayList<>();
        while (cursor.moveToNext()){
            int index = cursor.getColumnIndex("RName");
            if (index != -1){
                String username = cursor.getString(index);
                usernameList.add(username);
            }
        }
        cursor.close();
        return usernameList;
    }


    public void deleteRecipeHistory(String text){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("RecipeHistory", "RName = ? ", new String[]{text});
    }

    public void deleteUserHistory(String text){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("UserHistory", "UName = ? ",new String[]{text});
    }

    public void deleteUid() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOCAL, null, null);
    }

    public void saveUid(String uid) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Uid", uid);

        db.insert("Local", null, values);
        db.close();

    }

    public String getUid() {
        SQLiteDatabase db = this.getReadableDatabase();
        String uid = null;

        String[] columns = {"Uid"};
        Cursor cursor = db.query("Local", columns, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int index = cursor.getColumnIndex("Uid");
            if (index != -1) {
                uid = cursor.getString(index);
            }
        }


        cursor.close();
        db.close();

        return uid;
    }

    public String getRid() {
        SQLiteDatabase db = this.getReadableDatabase();
        String Rid = null;

        String[] columns = {"Rid"};
        Cursor cursor = db.query("Local", columns, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int index = cursor.getColumnIndex("Rid");
            if (index != -1) {
                Rid = cursor.getString(index);
            }
        }

        cursor.close();
        db.close();

        return Rid;
    }
}
