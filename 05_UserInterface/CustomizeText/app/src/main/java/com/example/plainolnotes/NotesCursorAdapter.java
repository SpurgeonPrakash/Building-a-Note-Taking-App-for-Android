package com.example.plainolnotes;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by romellbolton on 2/1/18.
 */

// Create my own version of the CursorAdapter
public class NotesCursorAdapter extends CursorAdapter {

    /*
     Android provides adapter classes specifically to display data from an SQLite database query.
     There is SimpleCursorAdapter class, which is more simpler and you cannot use your own custom
     xml layout and you don't have the control of the layout. In order to use custom xml layout,
     Android provides CursorAdapter.

     A CursorAdapter makes it easy to use when the resource of a listview is coming from database
     and you can have more control over the binding of data values to layout controls.
     In the newView() method, you simply inflate the view and return it.
     In the bindView() method, you set the elements of your view.
     */

    public NotesCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    // The newView() method returns a view, and that view will be created based on
    // the layout that defines the list item display
    // In the newView() method, you simply inflate the view and return it.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        // Create a layoutInflater object by calling ".from(context)"
        // Call it's inflate method, and pass in the ID of the new layout,
        // parent and false
        return LayoutInflater
                .from(context)
                .inflate(R.layout.note_list_item, parent, false);

        // So I'm inflating my layout for the note_list_item,
        // and passing it back whenever the newView() method is called.
    }

    // When you bind the View, you receive an instance of the Cursor object,
    // and it will already point to the particular row of your database that's
    // supposed to be displayed
    // For the views that exists, we want to go and show that the data
    // In the bindView() method, you set the elements of your view.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // The cursor has the data for the specific row

        // First I'll get the text of the note ...
        // To do that, you need to know the position of the column
        // that contains the text in the data set.
        // The effective and robust way of doing this is to address the column
        // by it's name
        String noteText = cursor.getString(
            cursor.getColumnIndex(DBOpenHelper.NOTE_TEXT));

        // Now I have the text for the note that I want to display

        // Next I need to detect whether there's a line feed in the String.
        // 10 is "\n" in ASCII
        int pos = noteText.indexOf(10);
        // 10 in the ASCII value of a line feed character

        // Now I'll find out whether I found one
        if (pos != -1) {
            // Then I found a line feed
            // Reset the value of noteText ...
            // 0 is where I want to start the substring,
            // pos is where I found the line feed,
            // Then I will append to that a literal String of ellipses
            noteText = noteText.substring(0, pos) + " ...";
        }

        // Get a reference to my TextView control from the View object that was passed in
        TextView tv = (TextView) view.findViewById(R.id.tvNote);

        // Display the text
        tv.setText(noteText);
    }
}
