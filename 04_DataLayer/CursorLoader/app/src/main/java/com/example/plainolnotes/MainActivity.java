package com.example.plainolnotes;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

// The class implements the Loader interface, and in the generic declaration,
// we specify that the LoaderCallback implementation is going to manage a Cursor object

// Loaders execute data operations on the background threads automatically.
// And they elegantly handle changes in configuration such as orientation.

// Implement the Loader interface
public class MainActivity extends ActionBarActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "MainActivity";

    // Make the CursorAdapter a field of the class
    private CursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Call the method that inserts a note into the database
        insertNote("New note");

        // Get the table we want the data to come from
        String[] from = {DBOpenHelper.NOTE_TEXT};

        // Specify the ID of the layouts we want to populate in the ListView
        int[] to = {android.R.id.text1};

        // Create an Adapter, specifying the context, and the default layout
        cursorAdapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_1,
                null,
                from,
                to,
                0);

        // Get a reference to the ListView
        ListView list = (ListView) findViewById(android.R.id.list);

        // Set the CursorAdapter on the ListView
        list.setAdapter(cursorAdapter);

        // Initialize the loader, I don't care about the first 2 values,
        // but use "this" class to manage the loader.
        // Load the data on a background thread from the database
        // using the loader to display in the ListView
        getLoaderManager().initLoader(0, null, this);

    }

    private void insertNote(String noteText) {

        // Create a ContentValues object
        ContentValues values = new ContentValues();

        // Put the column to insert the data, and the data itself into database
        values.put(DBOpenHelper.NOTE_TEXT, noteText);

        // Insert the data into the database, specifying the CONTENT_URI,
        // and the values to put into the DB
        // Is this line where the CursorLoader is called?
        Uri noteUri = getContentResolver().insert(NotesProvider.CONTENT_URI,
                values);

        // Log the id of the item that was inserted into the database
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

/*
    Implement the CursorLoader interface methods
*/

    // The onCreateLoader() method is called whenever data is needed from the Content Provider.
    // When you create the CursorLoader object, it executes the query() method
    // of the ContentProvider class on the background thread.
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        Log.i(TAG, "onCreateLoader called");

        // To do this, we're going to use an instance of the CursorLoader class.
        // The CursorLoader is specifically designed to manage a cursor.
        // The constructor accepts the context, and the ContentURI of my ContentProvider.
        // I'm telling the CursorLoader that this is the ContentProvider where you can get the data.
        // The rest of the arguments are just like calling the query() method.
        // List of columns, a projection, a selection, selection arguments, and a sort order.
        // Selection is null here because it is already specified in the Provider
        // Now I've designated where the data is coming from
        return new CursorLoader(this, NotesProvider.CONTENT_URI,
                null, null, null, null);

        // load the cursor object with the data in the background thread
    }

    /*
        Next are 2 event methods, onLoadFinished(), and onLoaderReset()
     */

    // onLoadFinished() is receiving a Cursor object.  When you create the CursorLoader object,
    // it executes the query() method on the background thread. And when the data comes back,
    // onLoadFinished is called for you.
    // Your job is to take the data represented by the cursor object named "data",
    // and pass it to the CursorAdapter.
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i(TAG, "onLoadFinished called");

        // Update the ListView's data by swapping the cursor with the data from the database
        cursorAdapter.swapCursor(data);
    }

    // The onLoaderReset() method is called whenever the data needs to be wiped out.
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.i(TAG, "onLoaderReset called");

        // Update the ListView's data by swapping the cursor with null data
        cursorAdapter.swapCursor(null);
    }

    /*
     Now you'll never call these three methods yourself. They're all Callback methods.
     But we've implemented the code that's needed to retrieve and then update
     the data whenever necessary.

     Now we've created the database, we've added code to insert the data into the database table,
     we've added the ListView and cursor adaptor to display the data, the ContentProvider to
     manage the data, and finally, the loader to handle everything at the front end asynchronously
     */
}
