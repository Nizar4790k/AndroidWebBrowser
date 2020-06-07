package com.example.androidwebbrowser.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.androidwebbrowser.models.WebBrowserHistoryItem;

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
}
