package com.example.plainolnotes;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class NotesProvider extends ContentProvider {

    private static final String AUTHORITY = "com.example.plainolnotes.notesprovider";
    private static final String BASE_PATH = "notes";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    // Constant to identify the requested operation
    private static final int NOTES = 1;
    private static final int NOTES_ID = 2;

    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, NOTES);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", NOTES_ID);
    }

    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {

        DBOpenHelper helper = new DBOpenHelper(getContext());
        database = helper.getWritableDatabase();
        return true;
    }

    // The query method will get data back from the database table "notes". It will either retrieve
    // all the notes, or just a single row. But I need to say exactly which columns I want to retrieve.
    // I'll define the column list in my OpenHelper class.
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        // The query method will call the query method of the database.
        // 1. The Table Name
        // 2. List of columns
        // 3. Selection; a where clause used to filter the data (null returns all the data)
        // 4. Sort Order; a order by clause, sort the notes by its creation date, in descending order
        return database.query(DBOpenHelper.TABLE_NOTES, DBOpenHelper.ALL_COLUMNS,
                selection, null, null, null,
                DBOpenHelper.NOTE_CREATED + " DESC");
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    // The insert method returns a URI, and that URI is supposed to match this pattern ...
    // BASE_PATH +  "/", NOTES_ID
    // NOTES_ID is the primary key value
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        // Get the primary key value of the item that was inserted
        long id = database.insert(DBOpenHelper.TABLE_NOTES,
                null,
                values);

        // Create the URI to pass back that includes the new primary key value
        // URI's parse() method let's you put together a String, and return the equivalent URI
        return Uri.parse(BASE_PATH + "/" + id);

        // Now  I've put together the expected URI pattern and returned it
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        // Return the result of calling the delete() method from the database object
        // The delete() method will return an integer value that represent the number of rows
        // deleted
        return database.delete(DBOpenHelper.TABLE_NOTES, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        // Return a value, returned by the database object's update() method
        // The update() method will return an integer value that represent the number of rows
        // updated
        return database.update(DBOpenHelper.TABLE_NOTES, values, selection, selectionArgs);
    }
}
