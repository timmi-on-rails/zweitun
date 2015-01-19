package com.example.zweitun;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "zweitun";
    private static final String TASKS_TABLE_NAME = "tasks";
    private static final String TASKS_TABLE_CREATE =
            "CREATE TABLE " + TASKS_TABLE_NAME + " ( _id integer PRIMARY KEY autoincrement, name TEXT, priority INT );";

    DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TASKS_TABLE_CREATE);
        db.execSQL("INSERT INTO tasks (name, priority) VALUES('test1', 1);");
        db.execSQL("INSERT INTO tasks (name, priority) VALUES('test2', 2);");
        db.execSQL("INSERT INTO tasks (name, priority) VALUES('test3', 3);");
        db.execSQL("INSERT INTO tasks (name, priority) VALUES('test4', 4);");
        db.execSQL("INSERT INTO tasks (name, priority) VALUES('test5', 5);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}