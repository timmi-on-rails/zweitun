package com.example.zweitun;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;


public class TasksFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LIST_ID_KEY = "list_id";
    private static final int LOADER_ID = 0;
    private long list_id;
    private static TaskCursorAdapter adapter = null;
    private LoaderManager loaderManager;

    public long getListId() {
        return list_id;
    }

    public static TasksFragment newInstance(long list_id) {
        TasksFragment tasksFragment = new TasksFragment();
        Bundle args = new Bundle();
        args.putLong(LIST_ID_KEY, list_id);
        tasksFragment.setArguments(args);
        return tasksFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list_id = getArguments().getLong(LIST_ID_KEY);
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.swipe_list_view, container, false);
        return view;
    }*/

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.d("klickl", "klick");
        if (adapter.ids.contains(id)) {
            adapter.ids.remove(id);
        } else {
            adapter.ids.add(id);
        }
        //v.refreshDrawableState();

        adapter.notifyDataSetChanged();



        super.onListItemClick(l, v, position, id);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new TaskCursorAdapter(getActivity(), R.layout.list_item_task, null, 0);
        setListAdapter(adapter);

        loaderManager = getLoaderManager();
        loaderManager.initLoader(LOADER_ID, null, this);

        //((SwipeListView) getListView()).setSwipeListViewListener(new BaseSwipeListViewListener() {
           /* @Override
            public void onClickFrontView(int position) {
                Log.d("swipe", String.format("onClickFrontView %d", position));
            }

            @Override
            public void onClickBackView(int position) {
                Log.d("swipe", String.format("onClickBackView %d", position));
            }

            @Override
            public void onDismiss(int[] reverseSortedPositions) {
                for (int position : reverseSortedPositions) {
                    StorageManager.getInstance(getActivity()).completeTask(getListAdapter().getItemId(position));
                    refreshCursor();
                }
            }
        });*/
    }

    public void refreshCursor() {
        loaderManager.restartLoader(LOADER_ID, null, TasksFragment.this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new TasksLoader(getActivity(), list_id);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
