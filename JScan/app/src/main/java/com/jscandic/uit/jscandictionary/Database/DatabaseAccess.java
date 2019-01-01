package com.jscandic.uit.jscandictionary.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private static DatabaseAccess instance;

    private DatabaseAccess(Context context)
    {
        openHelper = new DictDataHelper(context);
    }

    public static DatabaseAccess getInstance(Context context)
    {
        if(instance == null)
            instance = new DatabaseAccess(context);
        return instance;
    }

    public SQLiteDatabase getReadableDatabase()
    {
        if(openHelper != null)
            return openHelper.getReadableDatabase();
        else
            return null;
    }

    public SQLiteDatabase getWritableDatabase()
    {
        if(openHelper != null)
            return openHelper.getWritableDatabase();
        else
            return null;
    }
}
