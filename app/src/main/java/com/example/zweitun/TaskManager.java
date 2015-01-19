package com.example.zweitun;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TaskManager {
    private SQLiteDatabase db;
    private DBOpenHelper dbOpenHelper;

    public TaskManager(Context context) {
        dbOpenHelper = new DBOpenHelper(context);
        db = dbOpenHelper.getWritableDatabase();
    }

    public Cursor getTasks() {
        return db.query("tasks", new String[] {"_id", "name", "priority"}, null, null, null, null, null);
    }

    public void close() {
        if (dbOpenHelper != null) {
            dbOpenHelper.close();
        }
    }

    public void addTask(String task_name, int priority) {
        db.execSQL("INSERT INTO tasks (name, priority) VALUES('" + task_name + "'," + priority + ");");
    }
}
