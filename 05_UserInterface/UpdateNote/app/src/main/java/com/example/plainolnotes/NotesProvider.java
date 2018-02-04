package com.example.plainolnotes;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class NotesProvider extends ContentProvider{

    private static final String AUTHORITY = "com.example.plainolnotes.notesprovider";
    private static final String BASE_PATH = "notes";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );

    // Constant to identify the requested operation
    private static final int NOTES = 1;
    private static final int NOTES_ID = 2;

    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    public static final String CONTENT_ITEM_TYPE = "Note";

    static {
        // If the URI matches just the "AUTHORITY" and the "PATH", return the value "1"
        // If "1" is returned, use the selection criteria that is already passed into the query method
        uriMatcher.addURI(AUTHORITY, BASE_PATH, NOTES);
        // If the URI matches the authority and the path, and has a number for the primary key, return the value "2"
        // If "2" is returned, use the selection criteria to look for that particular id
        uriMatcher.addURI(AUTHORITY, BASE_PATH +  "/#", NOTES_ID);
    }

    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {

        DBOpenHelper helper = new DBOpenHelper(getContext());
        database = helper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        // Compare the URI to my URI patterns that I established earlier
        // If I got a URI that ends in a numeric value, I know I only want a single row
        // Take the URI , and match against all the uri patterns that i specified,
        // if it matches with one, return the number
        // if it doesn't match with any, return -1
        if (uriMatcher.match(uri) == NOTES_ID) {

            // Reset the selection value
            selection = DBOpenHelper.NOTE_ID + "=" + uri.getLastPathSegment();

            // Now the selection will be used in the database query
        }

        return database.query(DBOpenHelper.TABLE_NOTES, DBOpenHelper.ALL_COLUMNS,
                selection, null, null, null,
                DBOpenHelper.NOTE_CREATED + " DESC");
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = database.insert(DBOpenHelper.TABLE_NOTES,
                null, values);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return database.delete(DBOpenHelper.TABLE_NOTES, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return database.update(DBOpenHelper.TABLE_NOTES,
                values, selection, selectionArgs);
    }
}
