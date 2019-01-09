package com.jscandic.uit.jscandictionary.Database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import com.jscandic.uit.jscandictionary.Database.DatabaseDescription.Dictionary;
import com.jscandic.uit.jscandictionary.R;

public class DictContentProvider extends ContentProvider {
    private DatabaseAccess databaseAccess;
    public static boolean isQueryingExactly;

    // UriMatcher helps ContentProvider determine operation to perform
    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    // constants used with UriMatcher to determine operation to perform
    private static final int A_WORD = 1; // manipulate one contact
    private static final int ALL_WORDS = 2; // manipulate contacts table

    // static block to configure this ContentProvider's UriMatcher
    static {
        // Uri specified word (*)
        uriMatcher.addURI(
                DatabaseDescription.AUTHORITY,
                Dictionary.TABLE_NAME + "/*",
                A_WORD);

        // Uri for table
        uriMatcher.addURI(
                DatabaseDescription.AUTHORITY,
                Dictionary.TABLE_NAME,
                ALL_WORDS);
    }

    @Override
    public boolean onCreate() {
        databaseAccess = DatabaseAccess.getInstance(getContext());
        return true;
    }


    // required method: Not used in this app, so we return null
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        Uri newUri = null;

        switch (uriMatcher.match(uri)){
            case ALL_WORDS:
                long result = databaseAccess.getWritableDatabase().insert(
                        Dictionary.TABLE_NAME,
                        null,
                        contentValues
                );
                if(result > 0)
                {
                    newUri = Dictionary.buildContactUri(
                            contentValues.getAsString(Dictionary.COLUMN_WORD)
                    );
                    getContext().getContentResolver().notifyChange(newUri, null);
                }
                else
                    throw new SQLException(
                            getContext().getString(R.string.insert_failed)
                    );
                break;

            default:
                throw new UnsupportedOperationException(
                        getContext().getString(R.string.invalid_insert_uri) + uri
                );
        }

        return newUri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        int result = 0;

        switch (uriMatcher.match(uri)){
            case A_WORD:
                result = databaseAccess.getWritableDatabase().delete(
                        Dictionary.TABLE_NAME,
                        Dictionary.COLUMN_WORD + "=" + "'" + uri.getLastPathSegment() + "'",
                        null
                );
                break;
            case ALL_WORDS:
                result = databaseAccess.getWritableDatabase().delete(
                        Dictionary.TABLE_NAME,
                        s,
                        strings
                );
                break;
            default:
                throw new UnsupportedOperationException(
                        getContext().getString(R.string.invalid_delete_uri) + uri
                );
        }

        if(result > 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return result;
    }

    // query the databases
    @Override
    public Cursor query(Uri uri, String[] projection,
                        String selection, String[] selectionArgs, String sortOrder) {

        // create SQLiteQueryBuilder for querying contacts table
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(Dictionary.TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case A_WORD:
                if(isQueryingExactly){
                    queryBuilder.appendWhere(
                            Dictionary.COLUMN_WORD + "=" + "'" + uri.getLastPathSegment() + "'"
                    );
                }
                else {
                    queryBuilder.appendWhere(
                            Dictionary.COLUMN_WORD + " LIKE " + "'%" + uri.getLastPathSegment() + "%'"
                    );
                }
                break;
            case ALL_WORDS:
                break;
            default:
                throw new UnsupportedOperationException(
                        getContext().getString(R.string.invalid_query_uri) + uri);
        }

        // execute the query to select one or all contacts
        Cursor cursor = queryBuilder.query(
                databaseAccess.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        // configure to watch for content changes
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }



    // update an existing contact in the databases
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int result = 0;

        switch (uriMatcher.match(uri))
        {
            case A_WORD:
                result = databaseAccess.getWritableDatabase().update(
                        Dictionary.TABLE_NAME,
                        values,
                        Dictionary.COLUMN_WORD + "=" + "'" + uri.getLastPathSegment() + "'",
                        null
                );
                break;
            case ALL_WORDS:
                result = databaseAccess.getWritableDatabase().update(
                        Dictionary.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );
                break;
            default:
                throw new UnsupportedOperationException(
                        getContext().getString(R.string.invalid_update_uri) + uri
                );
        }

        if(result > 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return result;
    }

}
