package com.example.zweitun;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;


public class TasksActivity extends ActionBarActivity {
    private static final int NEW_TASK = 1;

    StorageManager sm;
    TrashFragment trashFragment;
    TasksFragment tasksFragment;
    CategoriesFragment categoriesFragment;

    @Override
    public void onDestroy() {
        super.onDestroy();
        sm.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        sm = new StorageManager(this);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.ic_logo);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        NavigationFragmentPagerAdapter navigationFragmentPagerAdapter = new NavigationFragmentPagerAdapter(getSupportFragmentManager());
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(navigationFragmentPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        String[] actions = new String[] { getResources().getString(R.string.title_tasks), getResources().getString(R.string.title_trash), getResources().getString(R.string.title_categories) };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(actionBar.getThemedContext(), R.layout.navigation_spinner_item, actions);
        adapter.setDropDownViewResource(R.layout.navigation_spinner_dropdown_item);
        ActionBar.OnNavigationListener navigationListener = new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int i, long l) {
                viewPager.setCurrentItem(i);
                return true;
            }
        };
        actionBar.setListNavigationCallbacks(adapter, navigationListener);
    }

    private void updateFragments() {
        //Toast.makeText(getActivity().getApplicationContext(), "16. onDestroy()", Toast.LENGTH_SHORT).show();

        trashFragment.onRefresh();
        tasksFragment.onRefresh();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEW_TASK) {
            if (resultCode == RESULT_OK) {
                String task_name = data.getStringExtra(NewTaskActivity.TASK_NAME);
                int max_priority = data.getIntExtra(NewTaskActivity.MAX_PRIORITY, 2);
                String due_at = data.getStringExtra(NewTaskActivity.DUE_AT);
                float time_scale = data.getFloatExtra(NewTaskActivity.TIME_SCALE, -1);

                sm.addTask(task_name, max_priority, due_at, time_scale);
                updateFragments();
            }
        }
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
            case R.id.action_new_task:
                startActivityForResult(new Intent(this, NewTaskActivity.class), NEW_TASK);
                return true;
            case R.id.action_new_category:
                //startActivityForResult(new Intent(this, NewCategoryActivity.class), NEW_CATEGORY);
                return true;
            case R.id.action_discard:
                new AlertDialog.Builder(this)
                        .setMessage(R.string.alert_discard)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                sm.deleteAllCompletedTasks();
                                updateFragments();
                            }
                        })
                        .setNegativeButton(R.string.no, null)
                        .show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class NavigationFragmentPagerAdapter extends FragmentPagerAdapter {
        public NavigationFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    tasksFragment = TasksFragment.newInstance();
                    return tasksFragment;
                case 1:
                    trashFragment = TrashFragment.newInstance();
                    return trashFragment;
                case 2:
                    categoriesFragment = CategoriesFragment.newInstance();
                    return categoriesFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    public static class TasksFragment extends SwipeRefreshListFragment {
        TaskCursorAdapter adapter;

        public static TasksFragment newInstance() { return new TasksFragment(); }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_tasks, menu);
            super.onCreateOptionsMenu(menu, inflater);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            adapter = new TaskCursorAdapter(getActivity(), R.layout.task_info, null, 0);
            setListAdapter(adapter);
            swapCursor();
        }

        @Override
        public void onListItemClick(ListView l, View v, final int position, long id) {
            new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.alert_complete_task)
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Cursor cursor = (Cursor) getListAdapter().getItem(position);

                            ((TasksActivity) getActivity()).sm.completeTask(cursor.getInt(cursor.getColumnIndex("_id")));
                            ((TasksActivity) getActivity()).updateFragments();
                        }
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();

            super.onListItemClick(l, v, position, id);
        }

        @Override
        public void onRefresh() {
            swapCursor();
            setRefreshing(false);
        }

        private void swapCursor() {
            adapter.swapCursor(((TasksActivity) getActivity()).sm.getPendingTasks());
        }
    }

    public static class TrashFragment extends SwipeRefreshListFragment {
        TrashCursorAdapter adapter;

        public static TrashFragment newInstance() { return new TrashFragment(); }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_trash, menu);
            super.onCreateOptionsMenu(menu, inflater);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            adapter = new TrashCursorAdapter(getActivity(), R.layout.task_info, null, 0);
            setListAdapter(adapter);
            swapCursor();
        }

        @Override
        public void onRefresh() {
            swapCursor();
            setRefreshing(false);
        }

        private void swapCursor() {
            adapter.swapCursor(((TasksActivity) getActivity()).sm.getCompletedTasks());
        }
    }

    public static class CategoriesFragment extends SwipeRefreshListFragment {
        CategoryCursorAdapter adapter;

        public static CategoriesFragment newInstance() { return new CategoriesFragment(); }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_categories, menu);
            super.onCreateOptionsMenu(menu, inflater);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            adapter = new CategoryCursorAdapter(getActivity(), R.layout.list_item_category, null, 0);
            setListAdapter(adapter);
            swapCursor();
        }

        @Override
        public void onListItemClick(ListView l, View v, final int position, long id) {
            CheckBox checkBox = (CheckBox) v.findViewById(R.id.category_checked);
            boolean checked = checkBox.isChecked();

            Cursor cursor = (Cursor) getListAdapter().getItem(position);
            int category_id = cursor.getInt(cursor.getColumnIndex("_id"));

            checkBox.setChecked(!checked);
            ((TasksActivity) getActivity()).sm.setCategoryVisibility(category_id, !checked);

            super.onListItemClick(l, v, position, id);
        }

        @Override
        public void onRefresh() {
            swapCursor();
            setRefreshing(false);
        }

        private void swapCursor() {
            adapter.swapCursor(((TasksActivity) getActivity()).sm.getCategories());
        }
    }
}
