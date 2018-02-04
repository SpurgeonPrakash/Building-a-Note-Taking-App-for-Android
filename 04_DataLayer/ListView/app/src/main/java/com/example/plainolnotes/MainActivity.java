package com.example.plainolnotes;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class MainActivity extends ActionBarActivity {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Goal : Add data to the database, and retrieve the data and
        // display it in a ListView.

        insertNote("New note");

        // Display Data in the ListView
        // Query the current database table, by calling the query() method
        // of the contentResolver(), and passing in the args...
        // When you query it, you'll get back a Cursor object.
        // 1. The ContentProvider URI, to access the database
        // 2. The List of Columns in the DB Table
        Cursor cursor = getContentResolver().query(NotesProvider.CONTENT_URI,
                DBOpenHelper.ALL_COLUMNS, null, null, null, null);

        // I know have a cursor object that has references to all of my data.

        // In order to display the data, I'll use a class called the SimpleCursorAdapter.
        // First you need an array of Strings, which will be a list of the columns to use when
        // when you display the data in your layout.
        String[] from = {DBOpenHelper.NOTE_TEXT};

        // Next you need an array of integers, that you typically need too, which is a list
        // of resource id's for Views or Controls, that are going to be used to display the information
        // android.R.id.text1 is the id of a TextView that's going to be used in a layout file
        // that's also delivered with the SDK, we're going to refer to that next
        int[] to = {android.R.id.text1};

        // Create an instance of the CursorAdapter class, and we'll instantiate
        // it with a new SimpleCursorAdapter
        // The args include...
        // 1. The context
        // 2. The layout (ID of a layout build into the SDK that displays a single TextView)
        // The ID of the TextView is "text1"
        // 3. The Cursor object
        // 4. The list of columns that contain my data
        // 5. The list of view that will display the data
        // 6. The last argument is called "flags", an integer that can be set to 0
        CursorAdapter cursorAdapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_1,
                cursor,
                from,
                to,
                0
        );

        // Now I've created a Cursor and a CursorAdapter
        // Next I need a reference to my ListView
        // Give the id specified in the Layout file
        ListView list = (ListView) findViewById(android.R.id.list);

        // Pass the data into the List
        list.setAdapter(cursorAdapter);

        // So now, each time I start up the app, I'll be inserting a new note.
        // Then I'm querying the data from the database with the query() method,
        // I'm declaring the two arrays saying where the data's coming from, and
        // what's going to display it. And then I'm creating the CursorAdapter
        // wrapped around the Cursor. Then I'm getting a reference to the ListView,
        // and passing the Adapter to the ListView.

    }


    private void insertNote(String noteText) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT, noteText);
        Uri noteUri = getContentResolver().insert(NotesProvider.CONTENT_URI,
                values);
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
