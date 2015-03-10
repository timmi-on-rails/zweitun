package com.example.zweitun;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {
    private static final int NEW_TASK = 1;
    private static final int SHOW_LISTS = 2;
    private static final String LIST_ID_KEY = "list_id";

    private TasksFragment tasksFragment;
    private ListsCursorAdapter navigationAdapter;

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putLong(LIST_ID_KEY, navigationAdapter.getItemId(getSupportActionBar().getSelectedNavigationIndex()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** Initialize action bar */
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setIcon(R.drawable.ic_logo);

        /** Setup navigation */
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        ActionBar.OnNavigationListener navigationListener = new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int i, long l) {
                savePersistentNavigationId(l);

                /** Instantiate fragment */
                tasksFragment = (TasksFragment) getSupportFragmentManager().findFragmentByTag("tasks_fragment");
                if (tasksFragment == null || tasksFragment.getListId() != l) {
                    tasksFragment = TasksFragment.newInstance(l);
                    getSupportFragmentManager().beginTransaction()
                            .replace(android.R.id.content, tasksFragment, "tasks_fragment")
                            .commit();
                }
                return true;
            }
        };
        navigationAdapter = new ListsCursorAdapter(this, R.layout.navigation_spinner_item, R.layout.navigation_spinner_dropdown_item, getSupportLoaderManager(), 0);
        actionBar.setListNavigationCallbacks(navigationAdapter, navigationListener);

        /** Restore selected navigation index */
        long id;
        if (savedInstanceState != null) {
            id = savedInstanceState.getLong(LIST_ID_KEY, 0);
        } else {
            id = PreferenceManager.getDefaultSharedPreferences(this).getLong(LIST_ID_KEY, 0);
        }
        setSelectedNavigationId(id);
    }

    private void setSelectedNavigationId(long id) {
        int position = navigationAdapter.getPosition(id);

        if (position >= 0) {
            getSupportActionBar().setSelectedNavigationItem(position);
        }
    }

    public void reload() {
        ActionBar actionBar = getSupportActionBar();
        long list_id = navigationAdapter.getItemId(actionBar.getSelectedNavigationIndex());
        navigationAdapter.reload();
        setSelectedNavigationId(list_id);
    }

    /**
     *  Save the navigation index to shared preferences
     *  Chance to restore the list selection on app restart
     */
    private void savePersistentNavigationId(long id) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(this).edit();
        edit.putLong(LIST_ID_KEY, id);
        edit.commit();
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
            case R.id.action_show_trash:
                startActivity(new Intent(this, TrashActivity.class));
                return true;
            case R.id.action_show_lists:
                startActivityForResult(new Intent(this, ListsActivity.class), SHOW_LISTS);
                return true;
            case R.id.action_new_task:
                startActivityForResult(new Intent(this, NewTaskActivity.class), NEW_TASK);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case NEW_TASK:
                if (resultCode == RESULT_OK) {
                    String task_name = data.getStringExtra(NewTaskActivity.TASK_NAME);
                    int max_priority = data.getIntExtra(NewTaskActivity.MAX_PRIORITY, 2);
                    String due_at = data.getStringExtra(NewTaskActivity.DUE_AT);
                    int time_scale = data.getIntExtra(NewTaskActivity.TIME_SCALE, -1);

                    if (tasksFragment != null) {
                        StorageManager.getInstance(this).addTask(task_name, max_priority, due_at, time_scale, tasksFragment.getListId());
                        tasksFragment.reload();
                    }
                }
                break;
            case SHOW_LISTS:
                reload();
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
