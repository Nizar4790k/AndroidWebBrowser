package com.example.androidwebbrowser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.androidwebbrowser.database.BrowserBaseHelper;
import com.example.androidwebbrowser.database.BrowserCursorWrapper;
import com.example.androidwebbrowser.database.BrowserDbSchema;
import com.example.androidwebbrowser.models.WebBrowserHistoryItem;

import java.util.ArrayList;
import java.util.List;

public class BrowserLab {

    private static BrowserLab sBrowserlab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static BrowserLab get(Context context){
        if(sBrowserlab==null){
            sBrowserlab= new BrowserLab(context);
        }
        return sBrowserlab;
    }

    private BrowserLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new BrowserBaseHelper(mContext).getWritableDatabase();
    }

    public void addFavorite(WebBrowserHistoryItem webBrowserHistoryItem){
        ContentValues values = getFavoriteContentValues(webBrowserHistoryItem);
        mDatabase.insert(BrowserDbSchema.FavoriteTable.NAME,null,values);
    }

    public void addHistoryItem(WebBrowserHistoryItem webBrowserHistoryItem){
        ContentValues values = getHistoryContentValues(webBrowserHistoryItem);
        mDatabase.insert(BrowserDbSchema.HistoryTable.NAME,null,values);
    }


    private static ContentValues getFavoriteContentValues(WebBrowserHistoryItem webBrowserHistoryItem){
        ContentValues values= new ContentValues();

        values.put(BrowserDbSchema.FavoriteTable.Cols.URL, webBrowserHistoryItem.getUrl());
        values.put(BrowserDbSchema.FavoriteTable.Cols.TITLE,webBrowserHistoryItem.getTitle());
        return values;
    }

    private static ContentValues getHistoryContentValues(WebBrowserHistoryItem webBrowserHistoryItem){
        ContentValues values = new ContentValues();
        values.put(BrowserDbSchema.HistoryTable.Cols.DATE,webBrowserHistoryItem.getDate().getTime());
        values.put(BrowserDbSchema.HistoryTable.Cols.UUID,webBrowserHistoryItem.getUUID().toString());
        values.put(BrowserDbSchema.HistoryTable.Cols.URL,webBrowserHistoryItem.getUrl());
        values.put(BrowserDbSchema.HistoryTable.Cols.TITLE,webBrowserHistoryItem.getTitle());

        return values;

    }


    public  List<WebBrowserHistoryItem> getFavorites(){

        List<WebBrowserHistoryItem> webBrowserHistoryItems = new ArrayList<>();

        BrowserCursorWrapper cursor = queryFavorites(null,null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                webBrowserHistoryItems.add(cursor.getFavorite());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }





        return webBrowserHistoryItems;
    }


    public  List<WebBrowserHistoryItem> getHisoryItems(){

        List<WebBrowserHistoryItem> webBrowserHistoryItems = new ArrayList<>();

        BrowserCursorWrapper cursor = queryHistoryItems(null,null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                webBrowserHistoryItems.add(cursor.getHistoryItem());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }





        return webBrowserHistoryItems;
    }


    public boolean isFavorite(String url){

        BrowserCursorWrapper cursorWrapper = queryFavorites(
                BrowserDbSchema.FavoriteTable.Cols.URL+" = ?",new String[]{url});

        if(cursorWrapper.getCount()>0){
            return true;
        }

        return false;
    }


    private BrowserCursorWrapper queryFavorites(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                BrowserDbSchema.FavoriteTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new BrowserCursorWrapper(cursor);
    }

    private BrowserCursorWrapper queryHistoryItems(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                BrowserDbSchema.HistoryTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                BrowserDbSchema.HistoryTable.Cols.DATE+" DESC " // orderBy
        );
        return new BrowserCursorWrapper(cursor);
    }


    public void removeFavorite(WebBrowserHistoryItem f){


        mDatabase.delete(BrowserDbSchema.FavoriteTable.NAME,
                BrowserDbSchema.FavoriteTable.Cols.URL+" = ?",
                new String[]{f.getUrl()});


    }

    public void removeAllFavorites(){
        mDatabase.delete(BrowserDbSchema.FavoriteTable.NAME,null,null);
    }

    public void removeHistoryItem(WebBrowserHistoryItem f){


        mDatabase.delete(BrowserDbSchema.HistoryTable.NAME,
                BrowserDbSchema.HistoryTable.Cols.UUID+" = ?",
                new String[]{f.getUUID().toString()});


    }

    public void  removeAllHistory(){
        mDatabase.delete(BrowserDbSchema.HistoryTable.NAME,null,null);
    }



}
