package com.jscandic.uit.jscandictionary.Database;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseDescription {
    // ContentProvider's name: typically the package name
    public static final String AUTHORITY =
            "com.jscandic.uit.jscandictionary.Database";

    // base URI used to interact with the ContentProvider
    private static final Uri BASE_CONTENT_URI =
            Uri.parse("content://" + AUTHORITY);

    // nested class defines contents of the contacts table
    public static final class Dictionary implements BaseColumns {
        public static final String TABLE_NAME = "jvd"; // table's name

        // Uri for the contacts table
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        // column names for contacts table's columns
        public static final String COLUMN_WORD          = "Word";
        public static final String COLUMN_SYNONYMOUS    = "Synonymous";
        public static final String COLUMN_ENGLISH       = "English";
        public static final String COLUMN_VIETNAMEE     = "Vietnamese";
        public static final String COLUMN_ISFAVORITE    = "IsFavorite";

        // creates a Uri for a specific contact
        public static Uri buildContactUri(String word) {
            return CONTENT_URI.buildUpon().appendPath(word).build();
        }
    }
}
