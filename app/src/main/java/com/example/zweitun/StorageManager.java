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

    public void emptyTrash() {
        db.execSQL("DELETE FROM tasks WHERE deleted_at != ''");
    }

    public void completeTask(long id) {
        db.execSQL("UPDATE tasks SET deleted_at='done' WHERE _id='" + id + "'");
    }

    public Cursor getTasks(long list_id) {
        return db.query("tasks", new String[] { "_id", "name", "priority", "due_at" }, "deleted_at = '' AND list_id = '" + list_id + "'", null, null, null, "tasks.priority DESC");
    }

    public Cursor getdeleted_atTasks() {
        return db.query("tasks LEFT JOIN lists ON (lists._id = tasks.list_id)", new String[] {"tasks._id AS _id", "tasks.name AS task_name", "tasks.priority AS task_priority", "tasks.due_at AS task_due_at", "lists.name AS list_name" }, "tasks.deleted_at != ''", null, null, null, "tasks.deleted_at DESC");
    }

    public Cursor getLists() {
        return db.query("lists", new String[] { "_id", "name" }, null, null, null, null, "name ASC");
    }

    public void addTask(String task_name, int max_priority, String due_at, int time_scale, long list_id) {
        db.execSQL("INSERT INTO tasks (name, max_priority, due_at, time_scale, deleted_at, list_id) VALUES('" + task_name + "'," + max_priority + ",'" + due_at + "','" + time_scale + "', '', "+list_id+");");
    }
}
