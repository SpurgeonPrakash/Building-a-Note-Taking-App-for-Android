package com.example.plainolnotes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;


public class EditorActivity extends ActionBarActivity {

    private String action;
    private EditText editor;

    // The noteFilter will be a WHERE clause that'll use in SQL statements
    private String noteFilter;

    // oldText will contain the existing text of the selected note
    private String oldText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        editor = (EditText) findViewById(R.id.editText);

        Intent intent = getIntent();

        Uri uri = intent.getParcelableExtra(NotesProvider.CONTENT_ITEM_TYPE);

        if (uri == null) {
            action = Intent.ACTION_INSERT;
            setTitle(getString(R.string.new_note));
        } else { // If a URI has been passed in, I want to edit a note

            // Reset the action
            action = Intent.ACTION_EDIT;

            // Set the NoteFilter, or the WHERE clause
            // Get the primary key of the specific note
            noteFilter = DBOpenHelper.NOTE_ID + "=" + uri.getLastPathSegment();

            // Retrieve the 1 row from the database
            Cursor cursor = getContentResolver().query(
                    uri, // The content URI representing the primary key of the table
                    DBOpenHelper.ALL_COLUMNS, // The columns to return for each row
                    noteFilter, // Selection criteria
                    null,
                    null);

            // Now I'll have a cursor that gives me access to the one record that
            // matched the requested primary key value

            // To retrieve the data

            // Move to the one and only row
            cursor.moveToFirst();

            // Set the oldTextValue to the selected now
            oldText = cursor.getString(cursor.getColumnIndex(DBOpenHelper.NOTE_TEXT));

            // Set the text of the selected note
            editor.setText(oldText);

            // Move the cursor to the end of the existing text
            editor.requestFocus();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                finishEditing();
                break;
        }

        return true;
    }

    private void finishEditing() {
        String newText = editor.getText().toString().trim();

        switch (action) {
            case Intent.ACTION_INSERT:
                if (newText.length() == 0) {
                    setResult(RESULT_CANCELED);
                } else {
                    insertNote(newText);
                }
            case Intent.ACTION_EDIT: // If the Action is Edit ...

                // If the edit leaves characters ...
                if (newText.length() == 0) {

                    // Call the deleteNote() method
//                    deleteNote();

                    // If the text hasn't changed ...
                } else if (oldText.equals(newText)) {

                    // Cancel the Edit operation, nothing's changed
                    setResult(RESULT_CANCELED);

                } else {

                    // Update the note
                    updateNote(newText);
                }
        }

        // Go back to the MainActivity
        finish();
    }

    // Update the data in the database
    private void updateNote(String noteText) {

        // Create a ContentValues object
        ContentValues values = new ContentValues();

        // Put the data into the values object
        values.put(DBOpenHelper.NOTE_TEXT, noteText);

        // Insert the data into the database
        getContentResolver().update(NotesProvider.CONTENT_URI, values, noteFilter, null);

        // Create Toast
        Toast.makeText(this, R.string.note_updated, Toast.LENGTH_SHORT).show();

        // Set the result to OK
        setResult(RESULT_OK);
    }

    private void insertNote(String noteText) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT, noteText);
        getContentResolver().insert(NotesProvider.CONTENT_URI, values);
        setResult(RESULT_OK);
    }

    @Override
    public void onBackPressed() {
        finishEditing();
    }
}
