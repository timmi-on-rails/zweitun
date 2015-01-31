package com.example.zweitun;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;


public class MainActivity extends ActionBarActivity {
    private static final int NEW_TASK = 1;
    StorageManager storageManager;
    private TasksFragment tasksFragment;

    @Override
    public void onDestroy() {
        super.onDestroy();
        storageManager.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        storageManager = new StorageManager(this);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setIcon(R.drawable.ic_logo);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.navigation_spinner_item, storageManager.getLists(), new String[]{"name"}, new int[]{android.R.id.text1});
        adapter.setDropDownViewResource(R.layout.navigation_spinner_dropdown_item);
        ActionBar.OnNavigationListener navigationListener = new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int i, long l) {
                tasksFragment = TasksFragment.newInstance(l);
                getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, tasksFragment)
                    .commit();
                return true;
            }
        };
        actionBar.setListNavigationCallbacks(adapter, navigationListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_new_category:
                //startActivityForResult(new Intent(this, NewCategoryActivity.class), NEW_CATEGORY);
                return true;
            case R.id.action_new_task:
                startActivityForResult(new Intent(this, NewTaskActivity.class), NEW_TASK);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEW_TASK) {
            if (resultCode == RESULT_OK) {
                String task_name = data.getStringExtra(NewTaskActivity.TASK_NAME);
                int max_priority = data.getIntExtra(NewTaskActivity.MAX_PRIORITY, 2);
                String due_at = data.getStringExtra(NewTaskActivity.DUE_AT);
                int time_scale = data.getIntExtra(NewTaskActivity.TIME_SCALE, -1);

                if (tasksFragment != null) {
                    this.storageManager.addTask(task_name, max_priority, due_at, time_scale, tasksFragment.getListId());
                    tasksFragment.refreshCursor();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
