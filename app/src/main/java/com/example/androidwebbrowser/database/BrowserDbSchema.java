package com.example.androidwebbrowser.database;

import java.util.Date;

public class BrowserDbSchema  {

    public static final class FavoriteTable {
    public static final String NAME="favorites";

    public static final class Cols{

        public static final String URL="url";
        public static final String TITLE="title";
    }

    }

    public static final class HistoryTable{
        public static final String NAME="history";

        public static final class Cols{
            public static final String URL="url";
            public static final String TITLE="title";
            public static final String DATE="date";
            public static final String UUID="uuid";
        }

    }



}
