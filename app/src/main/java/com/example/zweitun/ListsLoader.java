package com.example.zweitun;

import android.content.Context;
import android.database.Cursor;

/**
 * Loads the lists.
 */
public class ListsLoader extends SimpleCursorLoader {
    public ListsLoader(Context context) {
        super(context);
    }

    @Override
    public Cursor loadInBackground() {
        return StorageManager.getInstance(getContext()).getLists();
    }
}