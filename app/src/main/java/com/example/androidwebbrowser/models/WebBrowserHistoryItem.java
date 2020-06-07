package com.example.androidwebbrowser.models;

import android.graphics.Bitmap;

import java.util.Date;

public class WebBrowserHistoryItem {

    private String mUrl;
    private String mTitle;
    private Date mDate;

    public WebBrowserHistoryItem(String url,String title){
        mUrl=url;
        mTitle=title;
    }

    public WebBrowserHistoryItem(String url, String title, Date date){
        mUrl=url;
        mTitle=title;
        mDate=date;
    }

    public WebBrowserHistoryItem(String url){
        mUrl=url;
    }

    public String getUrl(){
        return mUrl;
    }

    public String getTitle(){
        return mTitle;
    }
    public Date getDate(){
        return mDate;
    }

}
