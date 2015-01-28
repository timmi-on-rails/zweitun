package com.example.zweitun;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "zweitun";

    private static final String TASKS_TABLE_CREATE =
            "CREATE TABLE tasks                    ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                    "name TEXT," +
                                                    "max_priority INTEGER," +
                                                    "due_at TEXT," +
                                                    "priority REAL," +
                                                    "time_scale REAL," +
                                                    "completed TEXT);";
    private static final String CATEGORIES_TABLE_CREATE =
            "CREATE TABLE categories ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                      "name TEXT," +
                                      "visible INTEGER);";

    DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TASKS_TABLE_CREATE);
        db.execSQL(CATEGORIES_TABLE_CREATE);

        db.execSQL("INSERT INTO tasks (name, priority, completed) VALUES('test1', 0, '');");
        db.execSQL("INSERT INTO tasks (name, priority, completed) VALUES('test2', 1, '');");
        db.execSQL("INSERT INTO tasks (name, priority, completed) VALUES('test3', 2, '');");
        db.execSQL("INSERT INTO tasks (name, priority, completed) VALUES('test4', 3, '');");
        db.execSQL("INSERT INTO tasks (name, priority, completed) VALUES('test5', 4, 'a');");

        db.execSQL("INSERT INTO categories (name, visible) VALUES('cat1', 0);");
        db.execSQL("INSERT INTO categories (name, visible) VALUES('cat2', 1);");
        db.execSQL("INSERT INTO categories (name, visible) VALUES('cat3', 0);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
