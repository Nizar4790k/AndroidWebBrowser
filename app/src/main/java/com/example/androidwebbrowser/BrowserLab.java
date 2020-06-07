package com.example.androidwebbrowser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Browser;

import com.example.androidwebbrowser.database.BrowserBaseHelper;
import com.example.androidwebbrowser.database.BrowserCursorWrapper;
import com.example.androidwebbrowser.database.BrowserDbSchema;
import com.example.androidwebbrowser.models.Favorite;

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

    public void addFavorite(Favorite favorite){
        ContentValues values = getFavoriteContentValues(favorite);
        mDatabase.insert(BrowserDbSchema.FavoriteTable.NAME,null,values);
    }


    private static ContentValues getFavoriteContentValues(Favorite favorite){
        ContentValues values= new ContentValues();

        values.put(BrowserDbSchema.FavoriteTable.Cols.URL,favorite.getUrl());

        return values;
    }



    public  List<Favorite> getFavorites(){

        List<Favorite> favorites = new ArrayList<>();

        BrowserCursorWrapper cursor = queryFavorites(null,null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                favorites.add(cursor.getFavorite());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }





        return  favorites;
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

    public void removeFavorite(Favorite f){



        mDatabase.delete(BrowserDbSchema.FavoriteTable.NAME,
                BrowserDbSchema.FavoriteTable.Cols.URL+" = ?",
                new String[]{f.getUrl()});


    }


}
