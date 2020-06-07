package com.example.androidwebbrowser.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BrowserBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "browserBase.db";

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table "+BrowserDbSchema.FavoriteTable.NAME+
                "("+BrowserDbSchema.FavoriteTable.Cols.URL+" text primary key,"+
                    BrowserDbSchema.FavoriteTable.Cols.TITLE+")");







    }

    public BrowserBaseHelper(Context conext){
        super(conext,DATABASE_NAME,null,VERSION);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
