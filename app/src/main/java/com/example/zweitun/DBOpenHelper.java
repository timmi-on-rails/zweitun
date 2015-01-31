package com.example.zweitun;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "zweitun";

    private static final String TASKS_TABLE_CREATE =
        "CREATE TABLE tasks ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                             "name TEXT, " +
                             "max_priority INTEGER, " +
                             "due_at TEXT, " +
                             "priority REAL, " +
                             "time_scale INTEGER, " +
                             "deleted_at TEXT, " +
                             "list_id INTEGER);";

    private static final String LISTS_TABLE_CREATE =
        "CREATE TABLE lists (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);";

    DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TASKS_TABLE_CREATE);
        db.execSQL(LISTS_TABLE_CREATE);

        //db.execSQL("INSERT INTO categories (name) VALUES('cat1', 0);");


        ContentValues values = new ContentValues();
        values.put("name", "Tasks");
        long id = db.insert("lists", null, values);

        db.execSQL("INSERT INTO lists (name) VALUES('Lustiger Scheiss');");
for(int i=0; i<100; i++) {
    db.execSQL("INSERT INTO tasks (name, priority, deleted_at, list_id) VALUES('Schuhe putzen', 0, '','" + id + "');");
    db.execSQL("INSERT INTO tasks (name, priority, deleted_at, list_id) VALUES('PENNY', 1, '','" + id + "');");
    db.execSQL("INSERT INTO tasks (name, priority, deleted_at, list_id) VALUES('Arbeitslos melden', 2, '','" + id + "');");
    db.execSQL("INSERT INTO tasks (name, priority, deleted_at, list_id) VALUES('Lisa', 4, '','" + id + "');");
    db.execSQL("INSERT INTO tasks (name, priority, deleted_at, list_id) VALUES('Bad putzen', 3, 'a','" + id + "');");
}
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
