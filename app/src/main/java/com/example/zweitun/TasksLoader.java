package com.example.zweitun;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

/**
 * Loads the tasks of the specified list.
 */
public class TasksLoader extends SimpleCursorLoader {
    private long listId;

    public TasksLoader(Context context, Bundle args) {
        super(context);
        this.listId = args.getLong(TasksFragment.LIST_ID_KEY);
    }

    @Override
    public Cursor loadInBackground() {
        return StorageManager.getInstance(getContext()).getTasks(listId);
    }
}
