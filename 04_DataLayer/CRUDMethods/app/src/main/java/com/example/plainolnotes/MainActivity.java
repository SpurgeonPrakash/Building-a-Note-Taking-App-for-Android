package com.example.plainolnotes;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        insertNote("New note");

    }

    // Insert a brand new note
    private void insertNote(String noteText) {

        // Create and instantiate a ContentValues object
        ContentValues values = new ContentValues();

        // Add value to the ContentValue object
        // The key needs to be the name of the column you're assigning the value to
        values.put(DBOpenHelper.NOTE_TEXT, noteText);

        // Call the ContentProvider
        // To get to the ContentProvider, call "getContentResolver()",
        // and it has the CRUD methods.
        // For the first parameter, pass the Content URI constant from the Provider,
        // That identifies the ContentProvider that I want to communicate with.
        // Then pass in the values object
        Uri noteUri = getContentResolver().insert(NotesProvider.CONTENT_URI,
                values);

        // Now, I've inserted a row into the database table
        // When you call the insert() method, you get back a URI object.
        // Create a reference to the URI object "Uri noteUri", and log the
        // result using logcat.
        // To get the primary key value from the newly inserted row,
        // call "noteUri.getLastPathSegment()"
        Log.d("MainActivity", "Inserted note " + noteUri.getLastPathSegment());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
