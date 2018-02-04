package com.example.plainolnotes;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
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
import android.widget.Toast;


public class MainActivity extends ActionBarActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {
    private CursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] from = {DBOpenHelper.NOTE_TEXT};
        int[] to = {android.R.id.text1};
        cursorAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, null, from, to, 0);

        ListView list = (ListView) findViewById(android.R.id.list);
        list.setAdapter(cursorAdapter);

        getLoaderManager().initLoader(0, null, this);

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

        // Inspect the id of the variable I just created
        switch (id) {

            // If the "Create Sample Data" menu item is selected
            case R.id.action_create_sample:

                // Call insertSampleData() method
                insertSampleData();

                // Break from the switch statement
                break;

            // If the "Delete All" M=menu item is selected
            case R.id.action_delete_all:

                // Call deleteAllNotes() method
                deleteAllNotes();

                // Break from the switch statement
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void insertSampleData() {

        // Insert 3 notes
        insertNote("Simple notes");
        insertNote("Multi-line\nnote");
        insertNote("Very long notes with a lot of text that exceeds the width of the screen");

        // Each time you change the data in the database, you need to tell your loader object
        // that it needs to restart, that it needs to be re-read the data from the back-end database.
        restartLoader();
    }

    private void restartLoader() {
        // Each time you change the data in the database, you need to tell your loader object
        // that it needs to restart, that it needs to be re-read the data from the back-end database.
        getLoaderManager().restartLoader(0, null, this);
    }

    private void deleteAllNotes() {

        // deleteAllNotes() will start by asking the user to confirm that they really want to
        // delete everything. // I'll use a dialog box to do that.

        // Create a Dialog Listener to handle the positive button click on a Dialog
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {

                        // If the positive "yes" button is clicked
                        if (button == DialogInterface.BUTTON_POSITIVE) {

                            // Insert Data management code here
                            // Get the ContentResolver and call it's delete method
                            // I want to delete everything, so i pass in null for the
                            // where clause and the selectionArgs
                            getContentResolver().delete(
                                  NotesProvider.CONTENT_URI,
                                    null,
                                    null
                            );

                            // Restart the loader to refresh the screen
                            restartLoader();

                            // Now that I've deleted everything,
                            // I want to show that it's been deleted
                            // Use Toast message to tell the user what happened
                            Toast.makeText(MainActivity.this,
                                    getString(R.string.all_deleted),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                };

        // Create a Dialog Window, setting the message, and buttons that have a Dialog Listener
        // object added to it
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.are_you_sure))
                // Using String resources that are apart of the Android SDK for the
                // positive and negative buttons on the Dialog box
                // i.e "android.R.string.yes", and "android.R.string.no"
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();
    }
    

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, NotesProvider.CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }
}
