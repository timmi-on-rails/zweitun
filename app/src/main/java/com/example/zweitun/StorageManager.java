package com.example.zweitun;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class StorageManager {
    private SQLiteDatabase db;
    private DBOpenHelper dbOpenHelper;

    public StorageManager(Context context) {
        dbOpenHelper = new DBOpenHelper(context);
        db = dbOpenHelper.getWritableDatabase();
    }

    public void setCategoryVisibility(int id, boolean visibility) {
        if (visibility)
            db.execSQL("UPDATE categories SET visible=1 WHERE _id=" + id);
        else
            db.execSQL("UPDATE categories SET visible=0 WHERE _id=" + id);
    }

    public void close() {
        dbOpenHelper.close();
    }

    public void deleteTask(int id) {
        db.execSQL("DELETE FROM tasks WHERE _id='"+id+"'");
    }

    public void deleteAllCompletedTasks() {
        db.execSQL("DELETE FROM tasks WHERE completed != ''");
    }

    public void completeTask(int id) {
        db.execSQL("UPDATE tasks SET completed='done' WHERE _id='" + id + "'");
    }

    public Cursor getPendingTasks() {
        return db.query("tasks", new String[] {"_id", "name", "priority"}, "completed = ''", null, null, null, "priority DESC");
    }

    public Cursor getCompletedTasks() {
        return db.query("tasks", new String[] {"_id", "name", "priority", "completed"}, "completed != ''", null, null, null, "completed DESC");
    }

    public Cursor getCategories() {
        return db.query("categories", new String[] {"_id", "name", "visible"}, null, null, null, null, "name ASC");
    }

    public void addTask(String task_name, int max_priority, String due_at, float time_scale) {
        db.execSQL("INSERT INTO tasks (name, max_priority, due_at, time_scale, completed) VALUES('" + task_name + "'," + max_priority + ",'" + due_at + "','" + time_scale + "', 0);");
    }
}
