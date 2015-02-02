package com.example.zweitun;

import android.content.Context;
import android.database.Cursor;


public class TasksLoader extends SimpleCursorLoader {
    private Context context;
    private long list_id;

    public TasksLoader(Context context, long list_id) {
        super(context);
        this.context = context;
        this.list_id = list_id;
    }

    @Override
    public Cursor loadInBackground() {
        return StorageManager.getInstance(context).getTasks(list_id);
    }
}
