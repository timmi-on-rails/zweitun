package com.example.zweitun;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.zweitun.ui.TaskCursorAdapter;


public class MainActivity extends ActionBarActivity {
    private static final int ADD_TASK = 1;
    private TaskManager tm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tm = new TaskManager(this);

        displayListView();
    }

    private void displayListView() {
        Cursor cursor = tm.getTasks();
        if (cursor != null) {
            cursor.moveToFirst();
        }

        TaskCursorAdapter dataAdapter = new TaskCursorAdapter(this, R.layout.task_info,cursor,0);

        ListView listView = (ListView) findViewById(R.id.listView);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_TASK) {
            if (resultCode == RESULT_OK) {
                String task_name = data.getStringExtra(NewTaskActivity.TASK_NAME);
                int priority = data.getIntExtra(NewTaskActivity.PRIORITY, 2);
                tm.addTask(task_name, priority);
                displayListView();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_search) {
            Intent intent = new Intent(this, NewTaskActivity.class);
            startActivityForResult(intent, ADD_TASK);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
