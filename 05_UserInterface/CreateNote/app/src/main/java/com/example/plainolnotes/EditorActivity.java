package com.example.plainolnotes;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


public class EditorActivity extends ActionBarActivity {

    // Use this field to remember what I'm doing, whether I'm inserting or
    // updating a note
    private String action;

    // Field to represent the EditText object
    private EditText editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Set the value of the 2 fields
        editor = (EditText) findViewById(R.id.editText);

        // Get the Intent that launched this Activity
        Intent intent = getIntent();

        // Get the parcelable Extra from the Intent
        Uri uri = intent.getParcelableExtra(NotesProvider.CONTENT_ITEM_TYPE);

        // If the URI was passed in, it won't be null. But if the user pressed
        // the insert button, it will be null
        if (uri == null) {

            // Insert a new note
            action = Intent.ACTION_INSERT;

            // Set the title of the app to be "New Note"
            setTitle(getString(R.string.new_note));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    // Respond to the action bar's Up/Home button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Examine the menu item
        switch (item.getItemId()) {

            // When the user touches the back button, the ID is always the same.
            case android.R.id.home:

                // Finish editing the note
                finishEditing();

                // Break from the switch
                break;
        }

        // I've always handled the menu selection by returning true
        return true;
    }

    // This method will be called when the user pressed the device's back
    // button or the up button on the toolbar
    private void finishEditing() {

        // First step is to find out what the user typed in
        // . trim() eliminates any leading or following white space
        String newText = editor.getText().toString().trim();

        // Evaluate my current action
        switch (action) {

            // If the operation is an insertion
            case Intent.ACTION_INSERT:

                // If the new text is blank ...
                if (newText.length() == 0) {

                    // Cancel whatever operation was requested
                    setResult(RESULT_CANCELED);

                } else {

                    // Insert the note into the database
                    insertNote(newText);
                }
        }

        // Close this Activity and go back to the MainActivity
        finish();
    }

    private void insertNote(String noteText) {

        // Create the values object
        ContentValues values = new ContentValues();

        // Put the text into it
        values.put(DBOpenHelper.NOTE_TEXT, noteText);

        // Insert the data into the database table
        getContentResolver().insert(NotesProvider.CONTENT_URI,
                values);

        // I completed the operation that was requested
        setResult(RESULT_OK);
    }

    // Method called when the user presses the back button on the device
    @Override
    public void onBackPressed() {

        // Finish editing the note
        finishEditing();
    }
}
