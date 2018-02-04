package com.example.plainolnotes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by romellbolton on 1/29/18.
 */

// The SQLiteOpenHelper class is a member of the Android SDK, and you can use it to
// define your database and manage connections to it
public class DBOpenHelper extends SQLiteOpenHelper {

    // In order to define the structure of the database, I'll need to declare a whole bunch of
    // constants.

    //Constants for db name and version
    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 1;

    //Constants for identifying table and columns
    public static final String TABLE_NOTES = "notes";

    // The name of the id column must be _id. That's because this database is going to be
    // exposed to the app through a ContentProvider, and that's the expected name of a primary
    // key column when a ContentProvider is managing the data.
    public static final String NOTE_ID = "_id";

    // Body of the note, the value that the user will see
    public static final String NOTE_TEXT = "noteText";

    // Date / Time value
    public static final String NOTE_CREATED = "noteCreated";

    // SQL to create table
    // The ID column is a primary key and it's auto incrementing, so it's value will be
    // automatically assigned each time a new row is inserted into the table.
    // NOTE_TEXT is a simple text column
    // NOTE_CREATED has a default value of the current time stamp. CURRENT_TIMESTAMP is an
    // SQLite function. The user will never see the created value in the final but, but I'll be
    // using that value to sort the notes to present them on the screen.
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NOTES + " (" +
                    NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NOTE_TEXT + " TEXT, " +
                    NOTE_CREATED + " TEXT default CURRENT_TIMESTAMP" +
                    ")";

    // Constructor accepts the current context
    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // The onCreate() method is called the first time the DBOpenHelper class is instantiated and it results in
    // creating the database structure if it doesn't already exist.
    @Override
    public void onCreate(SQLiteDatabase db) {
        // When the onCreate() method is called, an instance of an SQLite database object is
        // passed in, and it's name is "db".

        // Create the Table
        db.execSQL(TABLE_CREATE);
    }

    // The onUpgrade() method is called when you've changed the database version, and the user opens
    // the app for the first time after that happens. You can put code into the onUpgrade() method
    // that can rebuild the database with new structure.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop the Table if it already exists, and then recreate it using the current Table structure
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);

        // Create the Table
        onCreate(db);
    }
}
