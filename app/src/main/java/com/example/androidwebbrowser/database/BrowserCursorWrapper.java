package com.example.androidwebbrowser.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.androidwebbrowser.models.WebBrowserHistoryItem;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

public class BrowserCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public BrowserCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public WebBrowserHistoryItem getFavorite(){
        String url = getString(getColumnIndex(BrowserDbSchema.FavoriteTable.Cols.URL));
        String title = getString(getColumnIndex(BrowserDbSchema.FavoriteTable.Cols.TITLE));

        return new WebBrowserHistoryItem(url,title);
    }

    public WebBrowserHistoryItem getHistoryItem(){
        String url = getString(getColumnIndex(BrowserDbSchema.HistoryTable.Cols.URL));
        String title = getString(getColumnIndex(BrowserDbSchema.HistoryTable.Cols.TITLE));
        UUID uuid = UUID.fromString(getString(getColumnIndex(BrowserDbSchema.HistoryTable.Cols.UUID)));
        long date = getLong(getColumnIndex(BrowserDbSchema.HistoryTable.Cols.DATE));




        return new WebBrowserHistoryItem(uuid,url,title,new Date(date));
    }
}
