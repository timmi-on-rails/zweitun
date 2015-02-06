package com.example.zweitun;

import android.content.Context;
import android.database.Cursor;

/**
 * Loads completed/deleted tasks
 */
public class TrashLoader extends SimpleCursorLoader {
    public TrashLoader(Context context) {
        super(context);
    }

    @Override
    public Cursor loadInBackground() {
        return StorageManager.getInstance(getContext()).getDeletedTasks();
    }
}
