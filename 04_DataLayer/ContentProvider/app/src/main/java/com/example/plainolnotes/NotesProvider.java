package com.example.plainolnotes;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by romellbolton on 1/29/18.
 */

// The content provider interface creates a standardized mechanism for getting to
// the application's data, regardless of whether it's SQLite database, JSON text,
// or any other kinds of structured data.
// The ContentProvider interface has methods for inserting, updating, and deleting
// data, and querying it.
public class NotesProvider extends ContentProvider {

    // We need these constants

    // Declare the Authority
    // This is a globally unique String that identifies the ContentProvider to the
    // the Android Framework.
    private static final String AUTHORITY = "com.example.plainolnotes.notesprovider";

    // Declare the Base Path
    // The Base Path represents the entire data set. My Database only has 1 table, so I've
    // given the base path the name of the table, "notes".
    // A name that points to a table or file (a path).
    private static final String BASE_PATH = "notes";

    // Declare the Content URI
    // The Content URI is a uniform resource identifier, that identifies the Content Provider
    // It includes the "Authority" and the "Base Path".
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    // EX: content://com.example.plainolnotes.notesprovider/notes

    // The next two constants that are integers represent operations,
    // things you can do with the Content Provider. The numeric values are arbitrary,
    // these are simply ways of identifying the operations, and they're private so they're
    // only used within this class.

    // Constant to identify the requested operation

    // Notes means "Give me the data"
    private static final int NOTES = 1;

    // The NOTES_ID operation will deal with only a single record
    private static final int NOTES_ID = 2;

    // Add a URI matcher, putting it in a static initializer.
    // This code will execute the first time anything is called in this class.

    // The purpose of the URIMatcher class is to parse a URI and then tell you which
    // operation has been requested. I have 2 operations that are possible,
    // and I'll add these to the URIMatcher. I'll do that in the static initializer
    private static final
    UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // The addURI() method looks for at LEAST 3 values:
        // 1. The Authority
        // 2. The Base Path
        // 3. And then one of the operations
        uriMatcher.addURI(AUTHORITY, BASE_PATH, NOTES);

        // The "/#" is a wild card, it means any numerical value.
        // So if I get a URI that starts with the Base Path, and then
        // ends with a "/" and a "Number", that means I'm looking for a
        // particular note. A particular row in the database table.
        // I'll identify that pattern with "NOTES_ID"
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", NOTES_ID);

        // Now we've registered these operations
    }

    // Declare the Database
    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {

        // Add code to let us manage the database in the Provider's onCreate() method

        // Get the OpenHelper
        DBOpenHelper helper = new DBOpenHelper(getContext());

        // Get the database using th OpenHelper
        database = helper.getWritableDatabase();

        // Return true
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
